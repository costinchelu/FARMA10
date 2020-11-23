package farma.model;

import farma.util.DBConstants;
import javafx.beans.property.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Stoc implements DBConstants {

    private SimpleLongProperty idStoc;
    private SimpleLongProperty idIntrare;
    private SimpleFloatProperty pretVanzare;
    private SimpleIntegerProperty cantitateDisp;
    private SimpleStringProperty lot;
    private SimpleObjectProperty<LocalDate> bbd;
    private SimpleStringProperty denumire;
    private SimpleLongProperty codBare;
    private SimpleStringProperty concentratie;
    private SimpleFloatProperty adaos;
    private SimpleFloatProperty pretImpus;
    private SimpleStringProperty producator;
    private SimpleStringProperty forma;
    private SimpleIntegerProperty tva;
    private SimpleFloatProperty valoareTva;

    public Stoc(SimpleLongProperty idStoc,
                SimpleLongProperty idIntrare,
                SimpleFloatProperty pretVanzare,
                SimpleIntegerProperty cantitateDisp,
                SimpleStringProperty lot,
                SimpleObjectProperty<LocalDate> bbd,
                SimpleStringProperty denumire,
                SimpleLongProperty codBare,
                SimpleStringProperty concentratie,
                SimpleFloatProperty adaos,
                SimpleFloatProperty pretImpus,
                SimpleStringProperty producator,
                SimpleStringProperty forma,
                SimpleIntegerProperty tva,
                SimpleFloatProperty valoareTva) {
        this.idStoc = idStoc;
        this.idIntrare = idIntrare;
        this.pretVanzare = pretVanzare;
        this.cantitateDisp = cantitateDisp;
        this.lot = lot;
        this.bbd = bbd;
        this.denumire = denumire;
        this.codBare = codBare;
        this.concentratie = concentratie;
        this.adaos = adaos;
        this.pretImpus = pretImpus;
        this.producator = producator;
        this.forma = forma;
        this.tva = tva;
        this.valoareTva = valoareTva;
    }

    public long getIdStoc() {
        return idStoc.get();
    }

    public SimpleLongProperty idStocProperty() {
        return idStoc;
    }

    public void setIdStoc(long idStoc) {
        this.idStoc.set(idStoc);
    }

    public long getIdIntrare() {
        return idIntrare.get();
    }

    public SimpleLongProperty idIntrareProperty() {
        return idIntrare;
    }

    public void setIdIntrare(long idIntrare) {
        this.idIntrare.set(idIntrare);
    }

    public float getPretVanzare() {
        return pretVanzare.get();
    }

    public SimpleFloatProperty pretVanzareProperty() {
        return pretVanzare;
    }

    public void setPretVanzare(float pretVanzare) {
        this.pretVanzare.set(pretVanzare);
    }

    public int getCantitateDisp() {
        return cantitateDisp.get();
    }

    public SimpleIntegerProperty cantitateDispProperty() {
        return cantitateDisp;
    }

    public void setCantitateDisp(int cantitateDisp) {
        this.cantitateDisp.set(cantitateDisp);
    }

    public String getLot() {
        return lot.get();
    }

    public SimpleStringProperty lotProperty() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot.set(lot);
    }

    public LocalDate getBbd() {
        return bbd.get();
    }

    public SimpleObjectProperty<LocalDate> bbdProperty() {
        return bbd;
    }

    public void setBbd(LocalDate bbd) {
        this.bbd.set(bbd);
    }

    public String getDenumire() {
        return denumire.get();
    }

    public SimpleStringProperty denumireProperty() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire.set(denumire);
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

    public float getPretImpus() {
        return pretImpus.get();
    }

    public SimpleFloatProperty pretImpusProperty() {
        return pretImpus;
    }

    public void setPretImpus(float pretImpus) {
        this.pretImpus.set(pretImpus);
    }

    public String getProducator() {
        return producator.get();
    }

    public SimpleStringProperty producatorProperty() {
        return producator;
    }

    public void setProducator(String producator) {
        this.producator.set(producator);
    }

    public String getForma() {
        return forma.get();
    }

    public SimpleStringProperty formaProperty() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma.set(forma);
    }

    public int getTva() {
        return tva.get();
    }

    public SimpleIntegerProperty tvaProperty() {
        return tva;
    }

    public void setTva(int tva) {
        this.tva.set(tva);
    }

    public float getValoareTva() {
        return valoareTva.get();
    }

    public SimpleFloatProperty valoareTvaProperty() {
        return valoareTva;
    }

    public void setValoareTva(float valoareTva) {
        this.valoareTva.set(valoareTva);
    }

    @Override
    public String toString() {
        return getDenumire() + "\t: [STOC] : " + getCantitateDisp();
    }


    /**
     * Actualizeaza cantitatea disponibila in stoc
     * @param cantitate = cantitatea pentru actualizare
     * @param tipOperatie = primeste 1 atunci cand cantitatea pentru actualizare trebuie
     *                    scazuta din stocul disponibil si -1 atunci cand respectiva
     *                    cantitate trebuie adaugata in stoc
     */
    public boolean actualizareStoc(int cantitate, int tipOperatie) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean isUpdated = false;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(UPDATE_STOC);

            ps.setInt(1, cantitate);
            ps.setInt(2, tipOperatie);
            ps.setLong(3, getIdStoc());

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
     * Insereaza in tabela iesiri toate articolele de pe factura
     */
    public void insertIesireIntoDb(Long idDocument) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(IESIRI_INSERT);
            ps.setLong(1, idDocument);
            ps.setLong(2, getIdStoc());
            ps.setInt(3, getCantitateDisp());

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
}
