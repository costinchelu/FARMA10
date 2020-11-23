package farma.view;

import farma.model.Operatori;
import farma.util.DBConstants;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class StergeUtilizatorController implements Initializable, DBConstants {

    @FXML private ComboBox<Operatori> selectieCB;
    @FXML private Label msgLB;
    ObservableList<Operatori> operatoriObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            operatoriObservableList = getAllOperatori();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        selectieCB.setItems(operatoriObservableList);
        selectieCB.getSelectionModel().select(0);

    }

    @FXML
    void butonBack(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Login.fxml", "Autentificare utilizator");
    }

    @FXML
    void butonOk(ActionEvent event) throws SQLException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmare stergere");
        alert.setHeaderText("Confirmare pentru dezactivarea contului de utilizator");
        alert.setContentText("Sunteti sigur ca doriti sa dezactivati " +
                "contul utilizatorului " +
                selectieCB.getSelectionModel().getSelectedItem().toString() + "?\n");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = dezactiveazaUtilizatorInBD();
                if(!isDeleted) {
                    throw new SQLException();
                }
            } catch (SQLException e) {
                msgLB.setText("Contul de utilizator nu a putut fi dezactivat");
                System.err.println(e.getMessage());
            }
            butonBack(event);
        }
    }

    /**
     * select * FROM operatori
     * returneaza un ObservableList cu toti operatorii pentru a fi folositi in combobox
     */
    public ObservableList<Operatori> getAllOperatori() throws SQLException {
        ObservableList<Operatori> operatoriObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_OPERATORI_F_ADMIN);

            while (resultSet.next()) {
                SimpleLongProperty idOperator = new SimpleLongProperty(resultSet.getLong("id_operator"));
                SimpleStringProperty numeOperator = new SimpleStringProperty(resultSet.getString("nume_operator"));
                SimpleStringProperty prenumeOperator = new SimpleStringProperty(resultSet.getString("prenume_operator"));


                Operatori operator = new Operatori(idOperator, numeOperator, prenumeOperator);
                operatoriObservableList.add(operator);
            }

        }catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(statement != null) {
                statement.close();
            }
            if(conn != null) {
                conn.close();
            }
        }

        return operatoriObservableList;
    }

    /**
     * stergere utilizator din BD
     */
    private boolean dezactiveazaUtilizatorInBD() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(DEZACTIVEAZA_OPERATOR);

            ps.setLong(1, selectieCB.getSelectionModel().getSelectedItem().getIdOperator());
            int nrIntrari = ps.executeUpdate();

            if (nrIntrari == 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) {
                ps.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
        return true;
    }

}
