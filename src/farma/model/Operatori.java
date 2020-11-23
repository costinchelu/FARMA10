package farma.model;

import farma.util.DBConstants;
import farma.util.PasswordGenerator;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Operatori implements DBConstants {

    private SimpleLongProperty idOperator;
    private SimpleStringProperty numeOperator;
    private SimpleStringProperty prenumeOperator;
    private SimpleStringProperty password;
    private byte[] salt;

    public Operatori(SimpleLongProperty idOperator,
                     SimpleStringProperty numeOperator,
                     SimpleStringProperty prenumeOperator) {
        this.idOperator = idOperator;
        this.numeOperator = numeOperator;
        this.prenumeOperator = prenumeOperator;
    }

    public Operatori(SimpleStringProperty numeOperator,
                     SimpleStringProperty prenumeOperator,
                     SimpleStringProperty password) throws NoSuchAlgorithmException {
        this.numeOperator = numeOperator;
        this.prenumeOperator = prenumeOperator;
        salt = PasswordGenerator.getSalt();
        String encryptedPassword = password.get();
        encryptedPassword = PasswordGenerator.getSHA512Password(encryptedPassword, salt);
        SimpleStringProperty parola = new SimpleStringProperty(encryptedPassword);
        this.password = parola;
    }

    public Operatori(SimpleLongProperty idOperator,
                     SimpleStringProperty numeOperator,
                     SimpleStringProperty prenumeOperator,
                     SimpleStringProperty password,
                     byte[] salt) {
        this.idOperator = idOperator;
        this.numeOperator = numeOperator;
        this.prenumeOperator = prenumeOperator;
        this.password = password;
        this.salt = salt;
    }

    public long getIdOperator() {
        return idOperator.get();
    }

    public SimpleLongProperty idOperatorProperty() {
        return idOperator;
    }

    public void setIdOperator(long idOperator) {
        this.idOperator.set(idOperator);
    }

    public String getNumeOperator() {
        return numeOperator.get();
    }

    public SimpleStringProperty numeOperatorProperty() {
        return numeOperator;
    }

    public void setNumeOperator(String numeOperator) {
        this.numeOperator.set(numeOperator);
    }

    public String getPrenumeOperator() {
        return prenumeOperator.get();
    }

    public SimpleStringProperty prenumeOperatorProperty() {
        return prenumeOperator;
    }

    public void setPrenumeOperator(String prenumeOperator) {
        this.prenumeOperator.set(prenumeOperator);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public byte[] getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return getNumeOperator() + " " + getPrenumeOperator();
    }

    /**
     * insereaza un operator nou in BD
     */
    public void insertUserIntoDb() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(OPERATOR_INSERT);

            ps.setString(1, getNumeOperator());
            ps.setString(2, getPrenumeOperator());
            ps.setString(3, getPassword());


            Blob blob = conn.createBlob();
            blob.setBytes(1, getSalt());
            ps.setBlob(4, blob);

            int nrIntrari = ps.executeUpdate();
            blob.free();

            if (nrIntrari == 0) {
                throw new SQLException("Nu au fost introduse intrari");
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
    }
}
