package farma.view;

import farma.util.DBConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class GraficOperatiiController implements Initializable, DBConstants {

    @FXML private BarChart<?, ?> grafic;
    @FXML private CategoryAxis graficAxaX;
    @FXML private NumberAxis graficAxaY;
    @FXML private ComboBox<String> selectieCB;
    @FXML private Label totalIntrariLB;
    @FXML private Label totalIesiriLB;
    private XYChart.Series serieIesiri;
    private XYChart.Series serieIntrari;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // combobox
        String[] cbTextArr = {"Luna curenta", "Luna precedenta", "Ultimele 2 luni"};
        ObservableList<String> cbText = FXCollections.observableArrayList();
        cbText.addAll(cbTextArr);
        selectieCB.setItems(cbText);
        selectieCB.getSelectionModel().select(0);


        // grafic
        serieIesiri = new XYChart.Series<>();
        serieIntrari = new XYChart.Series<>();

        graficAxaX.setLabel("Operatii");
        graficAxaY.setLabel("Valoare (lei)");

        serieIesiri.setName("Val. iesiri luna curenta (lei)");
        serieIntrari.setName("Val. intrari luna curenta (lei)");

        try {
            adaugaValoriLaGrafic(SELECT_IESIRI_L_CURENTA, 2);
            adaugaValoriLaGrafic(SELECT_INTRARI_L_CURENTA, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        grafic.getData().addAll(serieIesiri);
        grafic.getData().addAll(serieIntrari);


        EventHandler<ActionEvent> schimbareOptiuneGrafic = event -> {
            serieIesiri.getData().clear();
            serieIntrari.getData().clear();
            grafic.getData().clear();
            serieIesiri = new XYChart.Series<>();
            serieIntrari = new XYChart.Series<>();

            switch (selectieCB.getSelectionModel().getSelectedIndex()) {
                case 0:
                    serieIesiri.setName("Val. iesiri luna curenta (lei)");
                    serieIntrari.setName("Val. intrari luna curenta (lei)");
                    try {
                        adaugaValoriLaGrafic(SELECT_IESIRI_L_CURENTA, 2);
                        adaugaValoriLaGrafic(SELECT_INTRARI_L_CURENTA, 1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    serieIesiri.setName("Val. iesiri luna precedenta (lei)");
                    serieIntrari.setName("Val. intrari luna precedenta (lei)");
                    try {
                        adaugaValoriLaGrafic(SELECT_IESIRI_L_PRECEDENTA, 2);
                        adaugaValoriLaGrafic(SELECT_INTRARI_L_PRECEDENTA, 1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    serieIesiri.setName("Val. iesiri ultimele 2 luni (lei)");
                    serieIntrari.setName("Val. intrari ultimele 2 luni (lei)");
                    try {
                        adaugaValoriLaGrafic(SELECT_IESIRI_ULTIMELE_L, 2);
                        adaugaValoriLaGrafic(SELECT_INTRARI_ULTIMELE_L, 1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            grafic.getData().addAll(serieIesiri);
            grafic.getData().addAll(serieIntrari);
        };
        selectieCB.setOnAction(schimbareOptiuneGrafic);

    }

    /**
     * adauga valori din BD in seriile graficului
     * @param select: expresia folosita la selectia sumei
     * @param operatie: tip operatie (1 = intrare, 2 = iesire)
     * @throws SQLException
     */
    private void adaugaValoriLaGrafic(String select, int operatie) throws SQLException {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(select);

            while (resultSet.next()) {
                if(operatie == 1) {
                    serieIntrari.getData().add(new XYChart.Data("Intrari", resultSet.getFloat(1)));
                    totalIntrariLB.setText("Total intrari: " + resultSet.getFloat(1) + " lei");
                } else {
                    serieIesiri.getData().add(new XYChart.Data("Iesiri", resultSet.getFloat(1)));
                    totalIesiriLB.setText("Total iesiri: " + resultSet.getFloat(1) + " lei");
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Actiune pentru intoarcerea in fereastra de facturi
     */
    public void butonBackToFacturi(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelFacturi.fxml", "Centralizator facturi");
    }
}
