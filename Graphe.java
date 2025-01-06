package org.example.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Graphe implements Initializable {
    @FXML
    private BarChart barchar;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        XYChart.Series<String,Double> serie1 = new XYChart.Series<String,Double>();



        serie1.setName("CLIENTS");











        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "finance", "pwd");
            Statement STAT = conn.createStatement();
            ResultSet rst = STAT.executeQuery("select\n" +
                    "    c.nom_client,\n" +
                    "    b.benefice\n" +
                    "FROM\n" +
                    "    benefice_f b\n" +
                    "    LEFT JOIN client_d c ON c.code_client=b.code_client");
            while (rst.next()){
                getdata gtd = new getdata();

                gtd.setCodecli(rst.getString("NOM_CLIENT"));
                gtd.setBene(rst.getFloat("BENEFICE"));
                serie1.getData().add(new XYChart.Data<>(gtd.getCodecli(),(double)gtd.getBene()));




            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }







        barchar.getData().addAll(serie1);
    }
}
