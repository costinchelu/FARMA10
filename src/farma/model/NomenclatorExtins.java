package farma.model;

import farma.util.DBConstants;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;


public class NomenclatorExtins implements DBConstants {

    private SimpleLongProperty idNomenclator;
    private SimpleStringProperty denumireProd;
    private SimpleLongProperty codBare;
    private SimpleStringProperty concentratie;
    private SimpleFloatProperty adaos;
    private SimpleStringProperty ambalare;
    private SimpleFloatProperty pretImpus;
    private SimpleStringProperty observatii;
    private SimpleLongProperty idForma;
    private SimpleLongProperty idTva;
    private SimpleLongProperty idDci;
    private SimpleLongProperty idProducator;
    private SimpleStringProperty numeForma;
    private SimpleIntegerProperty procentTva;
    private SimpleStringProperty denumireDci;
    private SimpleStringProperty denumireProducator;

    /**
     * Constructor fara id pentru nomenclator simplu
     */
    public NomenclatorExtins(SimpleStringProperty denumireProd,
                             SimpleLongProperty codBare,
                             SimpleStringProperty concentratie,
                             SimpleFloatProperty adaos,
                             SimpleStringProperty ambalare,
                             SimpleFloatProperty pretImpus,
                             SimpleStringProperty observatii,
                             SimpleLongProperty idForma,
                             SimpleLongProperty idTva,
                             SimpleLongProperty idDci,
                             SimpleLongProperty idProducator) {
        this.denumireProd = denumireProd;
        this.codBare = codBare;
        this.concentratie = concentratie;
        this.adaos = adaos;
        this.ambalare = ambalare;
        this.pretImpus = pretImpus;
        this.observatii = observatii;
        this.idForma = idForma;
        this.idTva = idTva;
        this.idDci = idDci;
        this.idProducator = idProducator;
    }

    /**
     * Constructor cu idNomenclatorExtins
     */
    public NomenclatorExtins(SimpleLongProperty idNomenclator,
                             SimpleStringProperty denumireProd,
                             SimpleLongProperty codBare,
                             SimpleStringProperty concentratie,
                             SimpleFloatProperty adaos,
                             SimpleStringProperty ambalare,
                             SimpleFloatProperty pretImpus,
                             SimpleStringProperty observatii,
                             SimpleLongProperty idForma,
                             SimpleLongProperty idTva,
                             SimpleLongProperty idDci,
                             SimpleLongProperty idProducator,
                             SimpleStringProperty numeForma,
                             SimpleIntegerProperty procentTva,
                             SimpleStringProperty denumireDci,
                             SimpleStringProperty denumireProducator) {
        this.idNomenclator = idNomenclator;
        this.denumireProd = denumireProd;
        this.codBare = codBare;
        this.concentratie = concentratie;
        this.adaos = adaos;
        this.ambalare = ambalare;
        this.pretImpus = pretImpus;
        this.observatii = observatii;
        this.idForma = idForma;
        this.idTva = idTva;
        this.idDci = idDci;
        this.idProducator = idProducator;
        this.numeForma = numeForma;
        this.procentTva = procentTva;
        this.denumireDci = denumireDci;
        this.denumireProducator = denumireProducator;
    }

    /**
     * Constructor cu idNomenclator
     */
    public NomenclatorExtins(SimpleLongProperty idNomenclator,
                             SimpleStringProperty denumireProd,
                             SimpleLongProperty codBare,
                             SimpleStringProperty concentratie,
                             SimpleFloatProperty adaos,
                             SimpleStringProperty ambalare,
                             SimpleFloatProperty pretImpus,
                             SimpleStringProperty observatii,
                             SimpleLongProperty idForma,
                             SimpleLongProperty idTva,
                             SimpleLongProperty idDci,
                             SimpleLongProperty idProducator) {
        this.idNomenclator = idNomenclator;
        this.denumireProd = denumireProd;
        this.codBare = codBare;
        this.concentratie = concentratie;
        this.adaos = adaos;
        this.ambalare = ambalare;
        this.pretImpus = pretImpus;
        this.observatii = observatii;
        this.idForma = idForma;
        this.idTva = idTva;
        this.idDci = idDci;
        this.idProducator = idProducator;
    }

    /**
     * Constructor fara idNomenclatorExtins
     */
    public NomenclatorExtins(SimpleStringProperty denumireProd, SimpleLongProperty codBare, SimpleStringProperty concentratie, SimpleFloatProperty adaos, SimpleStringProperty ambalare, SimpleFloatProperty pretImpus, SimpleStringProperty observatii, SimpleLongProperty idForma, SimpleLongProperty idTva, SimpleLongProperty idDci, SimpleLongProperty idProducator, SimpleStringProperty numeForma, SimpleIntegerProperty procentTva, SimpleStringProperty denumireDci, SimpleStringProperty denumireProducator) {
        this.denumireProd = denumireProd;
        this.codBare = codBare;
        this.concentratie = concentratie;
        this.adaos = adaos;
        this.ambalare = ambalare;
        this.pretImpus = pretImpus;
        this.observatii = observatii;
        this.idForma = idForma;
        this.idTva = idTva;
        this.idDci = idDci;
        this.idProducator = idProducator;
        this.numeForma = numeForma;
        this.procentTva = procentTva;
        this.denumireDci = denumireDci;
        this.denumireProducator = denumireProducator;
    }

    public long getIdNomenclator() {
        return idNomenclator.get();
    }

    public SimpleLongProperty idNomenclatorProperty() {
        return idNomenclator;
    }

    public void setIdNomenclator(long idNomenclator) {
        this.idNomenclator.set(idNomenclator);
    }

    public String getDenumireProd() {
        return denumireProd.get();
    }

    public SimpleStringProperty denumireProdProperty() {
        return denumireProd;
    }

    public void setDenumireProd(String denumireProd) {
        if (denumireProd.trim().isEmpty()) {
            throw new IllegalArgumentException("Introduceti denumirea produsului!");
        }
        this.denumireProd.set(denumireProd);
    }

    public long getCodBare() {
        return codBare.get();
    }

    public SimpleLongProperty codBareProperty() {
        return codBare;
    }

    public void setCodBare(long codBare) {
        this.codBare.set(codBare);
    }

    public String getConcentratie() {
        return concentratie.get();
    }

    public SimpleStringProperty concentratieProperty() {
        return concentratie;
    }

    public void setConcentratie(String concentratie) {
        this.concentratie.set(concentratie);
    }

    public float getAdaos() {
        return adaos.get();
    }

    public SimpleFloatProperty adaosProperty() {
        return adaos;
    }

    public void setAdaos(float adaos) {
        this.adaos.set(adaos);
    }

    public String getAmbalare() {
        return ambalare.get();
    }

    public SimpleStringProperty ambalareProperty() {
        return ambalare;
    }

    public void setAmbalare(String ambalare) {
        this.ambalare.set(ambalare);
    }

    public float getPretImpus() {
        return pretImpus.get();
    }

    public SimpleFloatProperty pretImpusProperty() {
        return pretImpus;
    }

    public void setPretImpus(float pretImpus) {
        this.pretImpus.set(pretImpus);
    }

    public String getObservatii() {
        return observatii.get();
    }

    public SimpleStringProperty observatiiProperty() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii.set(observatii);
    }

    public long getIdForma() {
        return idForma.get();
    }

    public SimpleLongProperty idFormaProperty() {
        return idForma;
    }

    public void setIdForma(long idForma) {
        this.idForma.set(idForma);
    }

    public long getIdTva() {
        return idTva.get();
    }

    public SimpleLongProperty idTvaProperty() {
        return idTva;
    }

    public void setIdTva(long idTva) {
        this.idTva.set(idTva);
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

    public long getIdProducator() {
        return idProducator.get();
    }

    public SimpleLongProperty idProducatorProperty() {
        return idProducator;
    }

    public void setIdProducator(long idProducator) {
        this.idProducator.set(idProducator);
    }

    public String getNumeForma() {
        return numeForma.get();
    }

    public SimpleStringProperty numeFormaProperty() {
        return numeForma;
    }

    public void setNumeForma(String numeForma) {
        this.numeForma.set(numeForma);
    }

    public int getProcentTva() {
        return procentTva.get();
    }

    public SimpleIntegerProperty procentTvaProperty() {
        return procentTva;
    }

    public void setProcentTva(int procentTva) {
        this.procentTva.set(procentTva);
    }

    public String getDenumireDci() {
        return denumireDci.get();
    }

    public SimpleStringProperty denumireDciProperty() {
        return denumireDci;
    }

    public void setDenumireDci(String denumireDci) {
        this.denumireDci.set(denumireDci);
    }

    public String getDenumireProducator() {
        return denumireProducator.get();
    }

    public SimpleStringProperty denumireProducatorProperty() {
        return denumireProducator;
    }

    public void setDenumireProducator(String denumireProducator) {
        this.denumireProducator.set(denumireProducator);
    }

    @Override
    public String toString() {
        return getDenumireProd() + " - " + getCodBare() + " - " + getConcentratie() + " - " + getAdaos()
                + " - " + getAmbalare() + " - " + getPretImpus() + " - " + getObservatii() + " - " + getIdForma()
                + " - " + getIdTva() + " - " + getIdDci() + " - " + getIdProducator();
    }

    /**
     * insereaza un obiect nomenclator in baza de date
     */
    public void insertIntoDb() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(NOMENCLATOR_INSERT_INTO);
            ps.setString(1, getDenumireProd());
            ps.setLong(2, getCodBare());
            ps.setString(3, getConcentratie());
            ps.setFloat(4, getAdaos());
            ps.setString(5, getAmbalare());
            ps.setFloat(6, getPretImpus());
            ps.setString(7, getObservatii());
            ps.setLong(8, getIdForma());
            ps.setLong(9, getIdTva());
            ps.setLong(10, getIdDci());
            ps.setLong(11, getIdProducator());

            System.out.println(toString());

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
    public boolean deleteProdusNomenclator(long idNomenclator) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean isDeleted = false;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(NOMENCLATOR_DELETE);
            ps.setString(1, String.valueOf(idNomenclator));

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
     * actualizeaza un obiect Nomenclator din baza de date
     */
    public boolean updateNomenclatorInBd() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean isUpdated = false;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(NOMENCLATOR_UPDATE);
            ps.setString(1, getDenumireProd());
            ps.setLong(2, getCodBare());
            ps.setString(3, getConcentratie());
            ps.setFloat(4, getAdaos());
            ps.setString(5, getAmbalare());
            ps.setFloat(6, getPretImpus());
            ps.setString(7, getObservatii());
            ps.setLong(8, getIdForma());
            ps.setLong(9, getIdTva());
            ps.setLong(10, getIdDci());
            ps.setLong(11, getIdProducator());
            ps.setLong(12, getIdNomenclator());


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
     * returneaza ultimul id din tabela nomenclator
     * @return max(id_nomenclator) din tabela nomenclator
     */
    public long getLastId() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        long lastId = 0;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement("SELECT MAX(id_nomenclator) FROM nomenclator");

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
