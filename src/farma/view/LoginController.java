package farma.view;

import farma.model.Operatori;
import farma.util.DBConstants;
import farma.util.LogUser;
import farma.util.PasswordGenerator;
import farma.util.UserLogged;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable, DBConstants {

    @FXML private PasswordField passPF;
    @FXML private ComboBox<Operatori> userCB;
    @FXML private Label msgLB;
    @FXML private Button anulareBTN;
    @FXML private Button adaugaBTN;
    @FXML private Button modificaBTN;
    @FXML private Button stergeBTN;
    @FXML private  Button okBTN;
    ObservableList<Operatori> operatoriObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // dezactivam modificaBTN pt. ca primul utilizator selectat este ADMINISTRATOR
        modificaBTN.setDisable(true);
        okBTN.setDisable(true);

        // configurare ComboBox
        try {
            operatoriObservableList = getAllOperatori();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userCB.setItems(operatoriObservableList);
        userCB.getSelectionModel().select(0);

        // dezactiveaza butonul Adauga, daca utilizatorul selectat nu este Administrator
        EventHandler<ActionEvent> activareAdaugaOperator = event -> {
            if(userCB.getSelectionModel().getSelectedItem().getIdOperator() == 12) {
                adaugaBTN.setDisable(false);
                stergeBTN.setDisable(false);
                modificaBTN.setDisable(true);
                okBTN.setDisable(true);
            } else {
                adaugaBTN.setDisable(true);
                stergeBTN.setDisable(true);
                modificaBTN.setDisable(false);
                okBTN.setDisable(false);
            }
            passPF.setText("");
        };
        userCB.setOnAction(activareAdaugaOperator);
    }

    @FXML
    void butonOk(ActionEvent event) throws SQLException, IOException {
        if (userCB.getSelectionModel().getSelectedItem().getIdOperator() == 12) {
            msgLB.setText("Administratorul este folosit doar pentru adaugarea de noi utilizatori");
        } else if (valideazaParola(userCB.getSelectionModel().getSelectedItem().getIdOperator())) {

            // logging in
            LogUser.logUser(
                    userCB.getSelectionModel().getSelectedItem().getIdOperator(),
                    userCB.getSelectionModel().getSelectedItem().toString(),
                    "IN"
            );

            // scriere id utilizator intr-un fisier pentru folosire ulterioara
            UserLogged.writeUser(userCB.getSelectionModel().getSelectedItem().getIdOperator());

            // meniu principal
            SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "MainScene.fxml",
                    "Aplicatie pentru gestiunea stocului de medicamente");
        } else {
            msgLB.setText("Parola este gresita");
        }
    }

    @FXML
    void butonAnulare(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmare iesire");
        alert.setHeaderText("Confirmare pentru iesirea din program");
        alert.setContentText("Sunteti sigur ca doriti sa iesiti?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) anulareBTN.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void butonStergereUtilizator(ActionEvent event) throws IOException, SQLException {
        if (valideazaParola(userCB.getSelectionModel().getSelectedItem().getIdOperator())) {
            SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "StergeUtilizator.fxml", "Stergere utilizator");
        } else {
            msgLB.setText("Parola de administrator este gresita");
        }
    }

    @FXML
    void butonAdaugareUtilizator(ActionEvent event) throws SQLException, IOException {
        if (valideazaParola(userCB.getSelectionModel().getSelectedItem().getIdOperator())) {
            SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "AddUtilizator.fxml", "Adauga utilizator");
        } else {
            msgLB.setText("Parola de administrator este gresita");
        }
    }

    @FXML void butonModificareParola(ActionEvent event) throws SQLException, IOException {
        if (valideazaParola(userCB.getSelectionModel().getSelectedItem().getIdOperator())) {
            SceneChanger sc = new SceneChanger();
            AddUtilizatorController newAddUtilizatorController = new AddUtilizatorController();
            sc.changeScenes(event, "AddUtilizator.fxml", "Modifica parola utilizator",
                    userCB.getSelectionModel().getSelectedItem(), newAddUtilizatorController);
        } else {
            msgLB.setText("Parola este gresita");
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
            resultSet = statement.executeQuery(SELECT_ALL_OPERATORI);

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

    private boolean valideazaParola(Long idOperator) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        boolean validat = false;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(OPERATOR_SELECT_PASS);
            ps.setString(1, String.valueOf(idOperator));
            resultSet = ps.executeQuery();

            String dbPassword = null;
            byte[] dbSalt = null;

            while (resultSet.next()) {

                dbPassword = resultSet.getString("password");
                Blob blob = resultSet.getBlob("salt");

                // convertim blob in byte[]
                int blobLength = (int) blob.length();
                dbSalt = blob.getBytes(1, blobLength);
            }

            // convertim parola introdusa de utilizator in parola criptata
            String userPw = PasswordGenerator.getSHA512Password(passPF.getText(), dbSalt);

            // comparam parolele
            if(userPw.equals(dbPassword)) {
                validat = true;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return validat;
    }

}
