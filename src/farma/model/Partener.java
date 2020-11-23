package farma.model;

import farma.util.DBConstants;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;

public class Partener implements DBConstants {

    private SimpleLongProperty idPartener;
    private SimpleStringProperty numePartener;
    private SimpleStringProperty adresa;
    private SimpleStringProperty telefon;
    private SimpleStringProperty email;
    private SimpleStringProperty cui;
    private SimpleStringProperty cont;

    public Partener(SimpleLongProperty idPartener,
                    SimpleStringProperty numePartener,
                    SimpleStringProperty adresa,
                    SimpleStringProperty telefon,
                    SimpleStringProperty email,
                    SimpleStringProperty cui,
                    SimpleStringProperty cont) {
        this.idPartener = idPartener;
        this.numePartener = numePartener;
        this.adresa = adresa;
        this.telefon = telefon;
        this.email = email;
        this.cui = cui;
        this.cont = cont;
    }

    public Partener(SimpleStringProperty numePartener,
                    SimpleStringProperty adresa,
                    SimpleStringProperty telefon,
                    SimpleStringProperty email,
                    SimpleStringProperty cui,
                    SimpleStringProperty cont) {
        this.numePartener = numePartener;
        this.adresa = adresa;
        this.telefon = telefon;
        this.email = email;
        this.cui = cui;
        this.cont = cont;
    }

    public long getIdPartener() {
        return idPartener.get();
    }

    public SimpleLongProperty idPartenerProperty() {
        return idPartener;
    }

    public void setIdPartener(long idPartener) {
        this.idPartener.set(idPartener);
    }

    public String getNumePartener() {
        return numePartener.get();
    }

    public SimpleStringProperty numePartenerProperty() {
        return numePartener;
    }

    public void setNumePartener(String numePartener) {
        this.numePartener.set(numePartener);
    }

    public String getAdresa() {
        return adresa.get();
    }

    public SimpleStringProperty adresaProperty() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa.set(adresa);
    }

    public String getTelefon() {
        return telefon.get();
    }

    public SimpleStringProperty telefonProperty() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon.set(telefon);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getCui() {
        return cui.get();
    }

    public SimpleStringProperty cuiProperty() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui.set(cui);
    }

    public String getCont() {
        return cont.get();
    }

    public SimpleStringProperty contProperty() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont.set(cont);
    }

    @Override
    public String toString() {
        return getNumePartener();
    }

    /**
     * Insereaza o intrare partener in BD
     */
    public void insertPartener() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(PARTENERI_INSERT_INTO);
            ps.setString(1, getNumePartener());
            ps.setString(2, getAdresa());
            ps.setString(3, getTelefon());
            ps.setString(4, getEmail());
            ps.setString(5, getCui());
            ps.setString(6, getCont());

            int nrIntrari = ps.executeUpdate();
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

    /**
     * șterge o intrare partener din baza de date
     * @param idPartSters: id-ul partener de sters
     * @return: true, daca stergerea a fost efectuata cu succes
     */
    public boolean stergePartener(long idPartSters) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean isDeleted = false;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(PARTENERI_DELETE);
            ps.setString(1, String.valueOf(idPartSters));

            int nrIntrari = ps.executeUpdate();
            if (nrIntrari == 0) {
                throw new SQLException("Nu au fost sterse intrari");
            }
            isDeleted = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return isDeleted;
    }

    public boolean updatePartenerInBd() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean isUpdated = false;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(PARTENERI_UPDATE);
            ps.setString(1, getNumePartener());
            ps.setString(2, getAdresa());
            ps.setString(3, getTelefon());
            ps.setString(4, getEmail());
            ps.setString(5, getCui());
            ps.setString(6, getCont());
            ps.setLong(7, getIdPartener());

            int nrIntrari = ps.executeUpdate();
            if (nrIntrari == 0) {
                throw new SQLException("Nu au fost modificate intrari");
            }
            isUpdated = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return isUpdated;
    }
}
