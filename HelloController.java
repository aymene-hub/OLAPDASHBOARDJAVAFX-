package org.example.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ChoiceBox<String> cbclient;

    @FXML
    private ChoiceBox<String> cbmois;

    @FXML
    private ChoiceBox<String> cbproduit;

    @FXML
    private TableView<getdata> benefice;

    @FXML
    private Button selectitems;


    TableColumn<getdata,String> CLIENTID=new TableColumn<getdata,String>("CLIENT_ID");
    TableColumn<getdata,String> PRODUITID=new TableColumn<getdata,String>("PRODUITID");
    TableColumn<getdata,Integer> MOISID=new TableColumn<getdata,Integer>("MOISID");
    TableColumn<getdata,String> TYPECONDID=new TableColumn<getdata,String>("TYPECONDID");
    TableColumn<getdata,Integer> ANNEID=new TableColumn<getdata,Integer>("ANNEID");
    TableColumn<getdata,String> CATEGORYID=new TableColumn<getdata,String>("CATEGORYID");
    TableColumn<getdata,String> CONTINENTCID=new TableColumn<getdata,String>("CONTINENTCID");
    TableColumn<getdata,String> PAYCID=new TableColumn<getdata,String>("PAYCID");
    TableColumn<getdata,String> CONTINENTFID=new TableColumn<getdata,String>("CONTINENTFID");
    TableColumn<getdata,String> PAYFID=new TableColumn<getdata,String>("PAYFID");
    TableColumn<getdata,Float> BENEF=new TableColumn<getdata,Float>("BENEF");

    @Override
    public  void initialize(URL url, ResourceBundle resourceBundle){
        cbclient.getItems().addAll(dimensionclients);
        cbproduit.getItems().addAll(dimensionproduits);
        cbmois.getItems().addAll(dimensionmois);

    }



    private Stage stage;
    private Scene scene;
    private Parent parent;



    @FXML
    void tostatics(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("statics.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        Stage stage=new Stage();
        stage.setTitle("Page analyse");
        stage.setScene(scene);
        stage.show();
    }






    private String [] dimensionclients={"Pays_D","Continent_D","All","null"};
    private String [] dimensionproduits={"Categorie_D","Type_Cond_D","PAYSF_D","CONTINENTF_D","All","null"};
    private String [] dimensionmois={"Annee_D","All","null"};


    @FXML
    void hundlebtn(ActionEvent event) {


        ObservableList <getdata> prdd= FXCollections.observableArrayList();


        if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")) {

            benefice.getColumns().addAll(CLIENTID,PAYCID,CONTINENTCID,PRODUITID,CATEGORYID,TYPECONDID,PAYFID,CONTINENTFID,MOISID,ANNEID,BENEF);

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "finance", "pwd");
                Statement STAT = conn.createStatement();
                ResultSet RST = STAT.executeQuery("SELECT\n" +
                        "    \n" +
                        "    C.NOM_CLIENT,\n" +
                        "    PAYS.NOM_PAYS AS CLIENT_PAYS,\n" +
                        "    CONT.DESIGNATION_CONTINENT AS CLIENT_CONTINENT,\n" +
                        "    \n" +
                        "    \n" +
                        "    PROD.NOM_PRODUIT,\n" +
                        "    CAT.DESIGNATION_CAT,\n" +
                        "    TYPE_COND.CONDITIONNEMENT,\n" +
                        "    PAYS_F.NOM_PAYS AS PRODUIT_PAYS,\n" +
                        "    CONT_F.DESIGNATION_CONTINENT AS PRODUIT_CONTINENT,\n" +
                        "    \n" +
                        "    B.NUM_MOIS,\n" +
                        "    annee.num_annee,\n" +
                        "    \n" +
                        "    B.BENEFICE\n" +
                        "FROM BENEFICE_F B\n" +
                        "-- Join with CLIENT dimension\n" +
                        "JOIN CLIENT_D C ON B.CODE_CLIENT = C.CODE_CLIENT\n" +
                        "JOIN PAYS_D PAYS ON C.NUM_PAYS = PAYS.NUM_PAYS\n" +
                        "JOIN CONTINENT_D CONT ON PAYS.NUM_CONTINENT = CONT.NUM_CONTINENT\n" +
                        "-- Join with PRODUIT dimension\n" +
                        "JOIN PRODUIT_D PROD ON B.CODE_PRODUIT = PROD.CODE_PRODUIT\n" +
                        "JOIN CATEGORY_D CAT ON PROD.CODE_CAT = CAT.CODE_CAT\n" +
                        "JOIN TYPE_COND_D TYPE_COND ON PROD.NUM_TYPE_COND = TYPE_COND.NUM_TYPE_COND\n" +
                        "JOIN PAYS_D PAYS_F ON PROD.NUM_PAYS_FOURNISSEUR = PAYS_F.NUM_PAYS\n" +
                        "JOIN CONTINENT_D CONT_F ON PAYS_F.NUM_CONTINENT = CONT_F.NUM_CONTINENT\n" +
                        "-- Join with TIME dimension\n" +
                        "JOIN MOIS_D MOIS ON B.NUM_MOIS = MOIS.NUM_MOIS\n" +
                        "JOIN ANNEE_D ANNEE ON MOIS.NUM_ANNEE = ANNEE.NUM_ANNEE\n" +
                        "\n");
                while (RST.next()) {
                    getdata gdd = new getdata();

                    gdd.setCodecli(RST.getString("NOM_CLIENT"));
                    gdd.setNumpays(RST.getString("CLIENT_PAYS"));
                    gdd.setNumcontinent(RST.getString("CLIENT_CONTINENT"));
                    gdd.setCodpro(RST.getString("NOM_PRODUIT"));
                    gdd.setCodecat(RST.getString("DESIGNATION_CAT"));
                    gdd.setNumtypecond(RST.getString("CONDITIONNEMENT"));
                    gdd.setNumpaysp(RST.getString("PRODUIT_PAYS"));
                    gdd.setNumcontinentp(RST.getString("PRODUIT_CONTINENT"));
                    gdd.setNum_mois(RST.getInt("NUM_MOIS"));
                    gdd.setNumanne(RST.getInt("num_annee"));
                    gdd.setBene(RST.getFloat("BENEFICE"));
                    prdd.add(gdd);
                }
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
            benefice.setItems(prdd);
            CLIENTID.setCellValueFactory(new PropertyValueFactory<getdata, String>("codecli"));
            PAYCID.setCellValueFactory(new PropertyValueFactory<getdata, String>("numpays"));
            CONTINENTCID.setCellValueFactory(new PropertyValueFactory<getdata, String>("numcontinent"));
            PRODUITID.setCellValueFactory(new PropertyValueFactory<getdata, String>("codpro"));
            CATEGORYID.setCellValueFactory(new PropertyValueFactory<getdata, String>("codecat"));
            TYPECONDID.setCellValueFactory(new PropertyValueFactory<getdata, String>("numtypecond"));
            PAYFID.setCellValueFactory(new PropertyValueFactory<getdata, String>("numpaysp"));
            CONTINENTFID.setCellValueFactory(new PropertyValueFactory<getdata, String>("numcontinentp"));
            MOISID.setCellValueFactory(new PropertyValueFactory<getdata, Integer>("num_mois"));
            ANNEID.setCellValueFactory(new PropertyValueFactory<getdata, Integer>("numanne"));
            BENEF.setCellValueFactory(new PropertyValueFactory<getdata, Float>("bene"));


        }

        //---------------------------------------------------------------------Pays_D--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,PRODUITID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,PRODUITID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,PRODUITID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,TYPECONDID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,TYPECONDID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,TYPECONDID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,CATEGORYID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,CATEGORYID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,CATEGORYID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,CONTINENTFID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,CONTINENTFID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,CONTINENTFID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,PAYFID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,PAYFID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Pays_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYCID,PAYFID,BENEF);
        }

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------





        //-----------------------------------------------------------------------------------CONTINENT_D---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,PRODUITID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,PRODUITID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,PRODUITID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,TYPECONDID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,TYPECONDID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,TYPECONDID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,CATEGORYID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,CATEGORYID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,CATEGORYID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,CONTINENTFID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,CONTINENTFID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,CONTINENTFID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,PAYFID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,PAYFID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "Continent_D") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTCID,PAYFID,BENEF);
        }
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------





        //--------------------------------------------------------------------------------------------------all----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PRODUITID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PRODUITID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PRODUITID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(TYPECONDID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(TYPECONDID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(TYPECONDID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CATEGORYID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CATEGORYID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")) {
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CATEGORYID, BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTFID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTFID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CONTINENTFID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYFID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYFID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(PAYFID,BENEF);
        }
        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------




        //-----------------------------------------------------------------------------------------------------------null------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,PRODUITID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,PRODUITID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,PRODUITID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "All") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,TYPECONDID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,TYPECONDID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Type_Cond_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,TYPECONDID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,CATEGORYID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,CATEGORYID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "Categorie_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,CATEGORYID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,CONTINENTFID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,CONTINENTFID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "CONTINENTF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,CONTINENTFID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "Annee_D")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,PAYFID,ANNEID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "null")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,PAYFID,MOISID,BENEF);
        }else if (Objects.equals(String.valueOf(cbclient.getSelectionModel().getSelectedItem()), "null") & Objects.equals(String.valueOf(cbproduit.getSelectionModel().getSelectedItem()), "PAYSF_D") & Objects.equals(String.valueOf(cbmois.getSelectionModel().getSelectedItem()), "All")){
            benefice.getColumns().clear();
            benefice.getColumns().addAll(CLIENTID,PAYFID,BENEF);
        }
        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    }



}