package farma.model;

import farma.util.DBConstants;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;


public class Dci implements DBConstants {

    private SimpleLongProperty idDci;
    private SimpleStringProperty codDci;
    private SimpleStringProperty denDci;
    private SimpleStringProperty grupaAtc;
    private SimpleStringProperty observatii;

    /**
     * Constructor cu parametrii - fara idDci
     */
    public Dci(SimpleStringProperty codDci,
               SimpleStringProperty denDci,
               SimpleStringProperty grupaAtc,
               SimpleStringProperty observatii) {
        this.codDci = codDci;
        this.denDci = denDci;
        this.grupaAtc = grupaAtc;
        this.observatii = observatii;
    }

    /**
     * Constructor cu parametrii - cu idDci
     */
    public Dci(SimpleLongProperty idDci,
               SimpleStringProperty codDci,
               SimpleStringProperty denDci,
               SimpleStringProperty grupaAtc,
               SimpleStringProperty observatii) {
        this.idDci = idDci;
        this.codDci = codDci;
        this.denDci = denDci;
        this.grupaAtc = grupaAtc;
        this.observatii = observatii;
    }

    public String getCodDci() {
        return codDci.get();
    }

    public SimpleStringProperty codDciProperty() {
        return codDci;
    }

    public String getDenDci() {
        if(denDci.get().trim().isEmpty()) {
            throw new IllegalArgumentException("Introduceti denumirea comuna internationala.");
        }
        return denDci.get();
    }

    public SimpleStringProperty denDciProperty() {
        return denDci;
    }

    public String getGrupaAtc() {
        return grupaAtc.get();
    }

    public SimpleStringProperty grupaAtcProperty() {
        return grupaAtc;
    }

    public String getObservatii() {
        return observatii.get();
    }

    public SimpleStringProperty observatiiProperty() {
        return observatii;
    }

    public long getIdDci() {
        return idDci.get();
    }

    public SimpleLongProperty idDciProperty() {
        return idDci;
    }

    public void setIdDci(long idDci) {
        this.idDci.set(idDci);
    }

    public void setCodDci(String codDci) {
        this.codDci.set(codDci);
    }

    public void setDenDci(String denDci) {
        this.denDci.set(denDci);
    }

    public void setGrupaAtc(String grupaAtc) {
        this.grupaAtc.set(grupaAtc);
    }

    public void setObservatii(String observatii) {
        this.observatii.set(observatii);
    }

    @Override
    public String toString() {
        return getDenDci();
    }

    /**
     * insereaza un obiect dci in baza de date
     */
    public void insertIntoDb() throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(DCI_INSERT_INTO, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, getCodDci());
            ps.setString(2, getDenDci());
            ps.setString(3, getGrupaAtc());
            ps.setString(4, getObservatii());

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

        System.out.println("A fost introdus id " + getLastId());
    }

    /**
     * sterge un obiect Dci din baza de date
     */
    public boolean deleteDci(long idDci) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean isDeleted = false;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(DCI_DELETE);
            ps.setString(1, String.valueOf(idDci));

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

    /**
     * actualizeaza un obiect Dci din baza de date
     */
    public boolean updateDciInBd() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean isUpdated = false;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(DCI_UPDATE);
            ps.setString(1, getDenDci());
            ps.setString(2, getCodDci());
            ps.setString(3, getGrupaAtc());
            ps.setString(4, getObservatii());
            ps.setLong(5, getIdDci());

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

    /**
     * returneaza ultimul id din tabela dci
     * @return max(id) din tabela dci
     */
    public long getLastId() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        long lastId = 0;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement("SELECT MAX(id_dci) FROM dci");

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                lastId = resultSet.getLong(1);
            }
            else {
                throw new SQLException("Fara id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(ps != null) {
                ps.close();
            }
            if(conn != null) {
                conn.close();
            }
        }

        return lastId;
    }
}
