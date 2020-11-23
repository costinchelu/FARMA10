package farma.view;

import farma.model.Operatori;
import farma.util.DBConstants;
import farma.util.PasswordGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;

public class AddUtilizatorController implements Initializable, ControllerClass, DBConstants {

    @FXML private PasswordField parolaSecondTF;
    @FXML private PasswordField parolaFirstPF;
    @FXML private TextField prenumeTF;
    @FXML private TextField numeTF;
    @FXML private Label msgLB;
    @FXML private Label titleLB;
    private boolean isEditing;
    private Operatori operatorPrimit;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isEditing = false;

    }

    public void butonOk(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException {
        try {
            if (validareIntrare()) {
                if(isEditing) {
                    byte[] newSalt = PasswordGenerator.getSalt();
                    String newPassEncrypted = PasswordGenerator.getSHA512Password(parolaFirstPF.getText(), newSalt);
                    boolean isUpdated = modificaParolaInBD(newPassEncrypted, newSalt);
                    if (!isUpdated) {
                         throw new SQLException();
                    }
                } else {
                    SimpleStringProperty nume = new SimpleStringProperty(numeTF.getText());
                    SimpleStringProperty prenume = new SimpleStringProperty(prenumeTF.getText());
                    SimpleStringProperty parola = new SimpleStringProperty(parolaFirstPF.getText());
                    Operatori operator = new Operatori(nume, prenume, parola);

                    operator.insertUserIntoDb();
                }

                butonBackToLogin(event);
            }
        } catch (SQLException e) {
            msgLB.setText("Parola nu a fost actualizata");
            System.err.println(e.getMessage());
        }
    }

    public void butonBackToLogin(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Login.fxml", "Autentificare utilizator");
    }


    private boolean validareIntrare() {
        if (numeTF.getText().trim().isEmpty() ||
                prenumeTF.getText().length() > 15 || numeTF.getText().length() > 15) {
            msgLB.setText("Introduceti numele utilizatorului!");
            return false;
        } else {
            if (!validareParola()) {
                msgLB.setText("Va rugam verificati parola!");
                return false;
            } else {
                if (prenumeTF.getText().trim().isEmpty()) {
                    prenumeTF.setText(" ");
                }
                return true;
            }
        }
    }

    private boolean validareParola() {
        if (parolaFirstPF.getText().trim().isEmpty()) {
            msgLB.setText("Introduceti o parola");
            return false;
        } else {
            if (parolaFirstPF.getText().length() < 5 && parolaFirstPF.getText().length() > 15) {
                msgLB.setText("Parola trebuie sa contina minim 5 caractere");
                return false;
            }
            if (parolaFirstPF.getText().equals(parolaSecondTF.getText())) {
                return true;
            } else {
                msgLB.setText("Parolele din cele doua campuri nu sunt identice");
                return false;
            }
        }
    }

    @Override
    public void preloadObject(Object obiectTransfer) {
        operatorPrimit = (Operatori) obiectTransfer;

        titleLB.setText("Modificare parola");
        numeTF.setText(operatorPrimit.getNumeOperator());
        numeTF.setDisable(true);
        prenumeTF.setText(operatorPrimit.getPrenumeOperator());
        prenumeTF.setDisable(true);
        msgLB.setText("Introduceti noua parola");
        msgLB.setTextFill(Color.GREEN);

        isEditing = true;
    }

    private boolean modificaParolaInBD(String newPas, byte[] newSalt) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(OPERATOR_UPDATE_PASS);

            ps.setString(1, newPas);
            Blob blob = conn.createBlob();
            blob.setBytes(1, newSalt);
            ps.setBlob(2, blob);
            ps.setLong(3, operatorPrimit.getIdOperator());

            int nrIntrari = ps.executeUpdate();
            blob.free();

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
