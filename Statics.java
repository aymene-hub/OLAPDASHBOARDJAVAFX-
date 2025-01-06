package org.example.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Statics implements Initializable {

    @FXML
    private TableView<getdata> AGRCLI;
    TableColumn<getdata,String> CLIENT_NAME=new TableColumn<getdata,String>("CLIENT_NAME");
    TableColumn<getdata,Float> TOTAL_BENE=new TableColumn<getdata,Float>("TOTAL_BENE");
    TableColumn<getdata,Float> MAX_BENE=new TableColumn<getdata,Float>("MAX_BENE");
    TableColumn<getdata,Float> MIN_BENE=new TableColumn<getdata,Float>("MIN_BENE");
    TableColumn<getdata,Float> AVG_BENE=new TableColumn<getdata,Float>("AVG_BENE");

    @FXML
    private TableView<getdata> AGRMOIS;
    TableColumn<getdata,String> MOIS=new TableColumn<getdata,String>("MOIS");
    TableColumn<getdata,Float> TOTAL_BENEM=new TableColumn<getdata,Float>("TOTAL_BENE");
    TableColumn<getdata,Float> MAX_BENEM=new TableColumn<getdata,Float>("MAX_BENE");
    TableColumn<getdata,Float> MIN_BENEM=new TableColumn<getdata,Float>("MIN_BENE");
    TableColumn<getdata,Float> AVG_BENEM=new TableColumn<getdata,Float>("AVG_BENE");


    @FXML
    private TableView<getdata> AGRPROD;
    TableColumn<getdata,String> PRODUIT_NAME=new TableColumn<getdata,String>("PRODUIT_NAME");
    TableColumn<getdata,Float> TOTAL_BENEP=new TableColumn<getdata,Float>("TOTAL_BENE");
    TableColumn<getdata,Float> MAX_BENEP=new TableColumn<getdata,Float>("MAX_BENE");
    TableColumn<getdata,Float> MIN_BENEP=new TableColumn<getdata,Float>("MIN_BENE");
    TableColumn<getdata,Float> AVG_BENEP=new TableColumn<getdata,Float>("AVG_BENE");


    private Stage stage;
    private Scene scene;
    private Parent parent;





    @FXML
    public void tographemethd(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("graphe.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        Stage stage=new Stage();
        stage.setTitle("statics");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AGRCLI.getColumns().addAll(CLIENT_NAME,TOTAL_BENE,MAX_BENE,MIN_BENE,AVG_BENE);
        AGRPROD.getColumns().addAll(PRODUIT_NAME,TOTAL_BENEP,MAX_BENEP,MIN_BENEP,AVG_BENEP);
        AGRMOIS.getColumns().addAll(MOIS,TOTAL_BENEM,MAX_BENEM,MIN_BENEM,AVG_BENEM);


        showcliagr();
        showproagr();
        showmoisagr();
    }

    public void showcliagr(){
        ObservableList<getdata> cliagr= getagrcli();
        AGRCLI.setItems(cliagr);
        CLIENT_NAME.setCellValueFactory(new PropertyValueFactory<getdata,String>("codecli"));
        TOTAL_BENE.setCellValueFactory(new PropertyValueFactory<getdata,Float>("totbene"));
        MAX_BENE.setCellValueFactory(new PropertyValueFactory<getdata,Float>("maxbene"));
        MIN_BENE.setCellValueFactory(new PropertyValueFactory<getdata,Float>("minbene"));
        AVG_BENE.setCellValueFactory(new PropertyValueFactory<getdata,Float>("avgbene"));

    }

    public ObservableList<getdata> getagrcli(){
        ObservableList<getdata> cliagr= FXCollections.observableArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "finance", "pwd");
            Statement STAT = conn.createStatement();
            ResultSet rst = STAT.executeQuery("SELECT \n" +
                    "    c.nom_client, \n" +
                    "    SUM(b.BENEFICE) AS total_benefice,\n" +
                    "    MAX(b.BENEFICE) AS max_benefice,\n" +
                    "    MIN(b.BENEFICE) AS min_benefice,\n" +
                    "    AVG(b.benefice) AS avg_benefice\n" +
                    "FROM \n" +
                    "   BENEFICE_F b\n" +
                    "   LEFT JOIN client_d c ON c.code_client=b.code_client\n" +
                    "GROUP BY \n" +
                    "       c.nom_client");
            while (rst.next()){
                getdata gtd = new getdata();

                gtd.setCodecli(rst.getString("NOM_CLIENT"));
                gtd.setTotbene(rst.getFloat("TOTAL_BENEFICE"));
                gtd.setMaxbene(rst.getFloat("MAX_BENEFICE"));
                gtd.setMinbene(rst.getFloat("MIN_BENEFICE"));
                gtd.setAvgbene(rst.getFloat("avg_benefice"));
                cliagr.add(gtd);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cliagr;
    }

    public void showproagr(){
        ObservableList<getdata> proagr= getagrpro();
        AGRPROD.setItems(proagr);
        PRODUIT_NAME.setCellValueFactory(new PropertyValueFactory<getdata,String>("codpro"));
        TOTAL_BENEP.setCellValueFactory(new PropertyValueFactory<getdata,Float>("totbenep"));
        MAX_BENEP.setCellValueFactory(new PropertyValueFactory<getdata,Float>("maxbenep"));
        MIN_BENEP.setCellValueFactory(new PropertyValueFactory<getdata,Float>("minbenep"));
        AVG_BENEP.setCellValueFactory(new PropertyValueFactory<getdata,Float>("avgbenep"));

    }


    public ObservableList<getdata> getagrpro(){
        ObservableList<getdata> proagr= FXCollections.observableArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "finance", "pwd");
            Statement STAT = conn.createStatement();
            ResultSet rst = STAT.executeQuery("SELECT \n" +
                    "    p.nom_produit ,\n" +
                    "    SUM(b.BENEFICE) AS total_benefice,\n" +
                    "    MAX(b.BENEFICE) AS max_benefice,\n" +
                    "    MIN(b.BENEFICE) AS min_benefice,\n" +
                    "    AVG(b.benefice) AS avg_benefice\n" +
                    "FROM \n" +
                    "    BENEFICE_F b\n" +
                    "    LEFT JOIN produit_d p ON p.code_produit=b.code_produit\n" +
                    "GROUP BY \n" +
                    "    p.nom_produit");
            while (rst.next()){
                getdata gtd = new getdata();

                gtd.setCodpro(rst.getString("NOM_PRODUIT"));
                gtd.setTotbenep(rst.getFloat("TOTAL_BENEFICE"));
                gtd.setMaxbenep(rst.getFloat("MAX_BENEFICE"));
                gtd.setMinbenep(rst.getFloat("MIN_BENEFICE"));
                gtd.setAvgbenep(rst.getFloat("avg_benefice"));
                proagr.add(gtd);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return proagr;
    }

    public void showmoisagr(){
        ObservableList<getdata> moiagr= getagrmois();
        AGRMOIS.setItems(moiagr);
        MOIS.setCellValueFactory(new PropertyValueFactory<getdata,String>("num_mois"));
        TOTAL_BENEM.setCellValueFactory(new PropertyValueFactory<getdata,Float>("totbenem"));
        MAX_BENEM.setCellValueFactory(new PropertyValueFactory<getdata,Float>("maxbenem"));
        MIN_BENEM.setCellValueFactory(new PropertyValueFactory<getdata,Float>("minbenem"));
        AVG_BENEM.setCellValueFactory(new PropertyValueFactory<getdata,Float>("avgbenem"));

    }

    public ObservableList<getdata> getagrmois(){
        ObservableList<getdata> moisagr= FXCollections.observableArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "finance", "pwd");
            Statement STAT = conn.createStatement();
            ResultSet rst = STAT.executeQuery("SELECT \n" +
                    "    m.num_mois, \n" +
                    "    SUM(b.BENEFICE) AS total_benefice,\n" +
                    "    MAX(b.BENEFICE) AS max_benefice,\n" +
                    "    MIN(b.BENEFICE) AS min_benefice,\n" +
                    "    AVG(b.benefice) AS avg_benefice\n" +
                    "FROM \n" +
                    "   BENEFICE_F b\n" +
                    "   LEFT JOIN mois_d m ON m.num_mois=b.num_mois\n" +
                    "GROUP BY \n" +
                    "       m.num_mois");
            while (rst.next()){
                getdata gtd = new getdata();

                gtd.setNum_mois(rst.getInt("NUM_MOIS"));
                gtd.setTotbenem(rst.getFloat("TOTAL_BENEFICE"));
                gtd.setMaxbenem(rst.getFloat("MAX_BENEFICE"));
                gtd.setMinbenem(rst.getFloat("MIN_BENEFICE"));
                gtd.setAvgbenem(rst.getFloat("avg_benefice"));
                moisagr.add(gtd);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return moisagr;
    }
}
