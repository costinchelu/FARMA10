package farma.view;

import farma.util.DBConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class GraficFurnizoriController implements Initializable, DBConstants {

    @FXML private Label valoriGraficLB;
    @FXML private Label valoriTitluLB;
    @FXML private BarChart<?, ?> grafic;
    @FXML private CategoryAxis graficAxaX;
    @FXML private NumberAxis graficAxaY;
    private XYChart.Series[] arrSerii;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String ACHIZITII_LB = "Achizitii pe ultimul trimestru";
        valoriTitluLB.setText(ACHIZITII_LB);
        arrSerii = new XYChart.Series[20];

        graficAxaX.setLabel("Parteneri");
        graficAxaY.setLabel("Valoare (lei)");
        grafic.setTitle(ACHIZITII_LB);
        grafic.setLegendVisible(false);

        try {
            adaugaValoriLaGrafic(SELECT_INTRARI_T);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < arrSerii.length; i++) {
            if(arrSerii[i] != null) {
                grafic.getData().add(arrSerii[i]);
            }
        }
    }

    /**
     adauga valori din BD in seriile graficului
     * @param select: expresia folosita la selectia sumei
     * @throws SQLException
     */
    private void adaugaValoriLaGrafic(String select) throws SQLException {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int itt = 0;
        StringBuilder sb = new StringBuilder();

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(select);

            while (resultSet.next()) {
                arrSerii[itt] = new XYChart.Series<>();
                arrSerii[itt].getData().add(new XYChart.Data
                        (resultSet.getString(1), resultSet.getFloat(2)));
                itt++;
                sb.append(resultSet.getString(1) + ": " + resultSet.getFloat(2) + " lei\n");
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
        valoriGraficLB.setText(sb.toString());
    }

    /**
     * Actiune pentru intoarcerea in fereastra de facturi
     */
    public void butonBackToFacturi(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelFacturi.fxml", "Centralizator facturi");
    }
}
