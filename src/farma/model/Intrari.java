package farma.model;

import farma.util.DBConstants;
import javafx.beans.property.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Intrari implements DBConstants {

    private SimpleLongProperty idIntrare;
    private SimpleLongProperty idNomenclator;
    private SimpleLongProperty idDocument;
    private SimpleStringProperty lotProdus;
    private SimpleObjectProperty<LocalDate> bbdProdus;
    private SimpleIntegerProperty cantitateIntrata;
    private SimpleFloatProperty pretAchizitie;
    private SimpleFloatProperty discountProdus;
    private SimpleStringProperty observatiiProdus;

    private SimpleStringProperty denumireProdus;
    private SimpleFloatProperty pretPentruVanzare;
    private SimpleFloatProperty pretImpus;
    private SimpleFloatProperty adaosProdus;
    private SimpleIntegerProperty procentTva;
    private SimpleFloatProperty valoareTva;

    public Intrari(SimpleLongProperty idNomenclator,
                   SimpleLongProperty idDocument,
                   SimpleStringProperty lotProdus,
                   SimpleObjectProperty<LocalDate> bbdProdus,
                   SimpleIntegerProperty cantitateIntrata,
                   SimpleFloatProperty pretAchizitie,
                   SimpleFloatProperty discountProdus,
                   SimpleStringProperty observatiiProdus,
                   SimpleStringProperty denumireProdus,
                   SimpleFloatProperty pretPentruVanzare,
                   SimpleFloatProperty pretImpus,
                   SimpleFloatProperty adaosProdus,
                   SimpleIntegerProperty procentTva,
                   SimpleFloatProperty valoareTva) {
        this.idNomenclator = idNomenclator;
        this.idDocument = idDocument;
        this.lotProdus = lotProdus;
        this.bbdProdus = bbdProdus;
        this.cantitateIntrata = cantitateIntrata;
        this.pretAchizitie = pretAchizitie;
        this.discountProdus = discountProdus;
        this.observatiiProdus = observatiiProdus;
        this.denumireProdus = denumireProdus;
        this.pretPentruVanzare = pretPentruVanzare;
        this.pretImpus = pretImpus;
        this.adaosProdus = adaosProdus;
        this.procentTva = procentTva;
        this.valoareTva = valoareTva;
    }

    public Intrari(SimpleLongProperty idIntrare,
                   SimpleLongProperty idNomenclator,
                   SimpleLongProperty idDocument,
                   SimpleStringProperty lotProdus,
                   SimpleObjectProperty<LocalDate> bbdProdus,
                   SimpleIntegerProperty cantitateIntrata,
                   SimpleFloatProperty pretAchizitie,
                   SimpleFloatProperty discountProdus,
                   SimpleStringProperty observatiiProdus,
                   SimpleStringProperty denumireProdus,
                   SimpleFloatProperty pretPentruVanzare,
                   SimpleFloatProperty pretImpus,
                   SimpleFloatProperty adaosProdus,
                   SimpleIntegerProperty procentTva,
                   SimpleFloatProperty valoareTva) {
        this.idIntrare = idIntrare;
        this.idNomenclator = idNomenclator;
        this.idDocument = idDocument;
        this.lotProdus = lotProdus;
        this.bbdProdus = bbdProdus;
        this.cantitateIntrata = cantitateIntrata;
        this.pretAchizitie = pretAchizitie;
        this.discountProdus = discountProdus;
        this.observatiiProdus = observatiiProdus;
        this.denumireProdus = denumireProdus;
        this.pretPentruVanzare = pretPentruVanzare;
        this.pretImpus = pretImpus;
        this.adaosProdus = adaosProdus;
        this.procentTva = procentTva;
        this.valoareTva = valoareTva;
    }

    public Intrari(SimpleLongProperty idNomenclator,
                   SimpleStringProperty lotProdus,
                   SimpleObjectProperty<LocalDate> bbdProdus,
                   SimpleIntegerProperty cantitateIntrata,
                   SimpleFloatProperty pretAchizitie,
                   SimpleFloatProperty discountProdus,
                   SimpleStringProperty observatiiProdus,
                   SimpleStringProperty denumireProdus,
                   SimpleFloatProperty pretPentruVanzare,
                   SimpleFloatProperty pretImpus,
                   SimpleFloatProperty adaosProdus,
                   SimpleIntegerProperty procentTva,
                   SimpleFloatProperty valoareTva) {
        this.idNomenclator = idNomenclator;
        this.lotProdus = lotProdus;
        this.bbdProdus = bbdProdus;
        this.cantitateIntrata = cantitateIntrata;
        this.pretAchizitie = pretAchizitie;
        this.discountProdus = discountProdus;
        this.observatiiProdus = observatiiProdus;
        this.denumireProdus = denumireProdus;
        this.pretPentruVanzare = pretPentruVanzare;
        this.pretImpus = pretImpus;
        this.adaosProdus = adaosProdus;
        this.procentTva = procentTva;
        this.valoareTva = valoareTva;
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

    public long getIdNomenclator() {
        return idNomenclator.get();
    }

    public SimpleLongProperty idNomenclatorProperty() {
        return idNomenclator;
    }

    public void setIdNomenclator(long idNomenclator) {
        this.idNomenclator.set(idNomenclator);
    }

    public long getIdDocument() {
        return idDocument.get();
    }

    public SimpleLongProperty idDocumentProperty() {
        return idDocument;
    }

    public void setIdDocument(long idDocument) {
        this.idDocument.set(idDocument);
    }

    public String getLotProdus() {
        return lotProdus.get();
    }

    public SimpleStringProperty lotProdusProperty() {
        return lotProdus;
    }

    public void setLotProdus(String lotProdus) {
        this.lotProdus.set(lotProdus);
    }

    public LocalDate getBbdProdus() {
        return bbdProdus.get();
    }

    public SimpleObjectProperty<LocalDate> bbdProdusProperty() {
        return bbdProdus;
    }

    public void setBbdProdus(LocalDate bbdProdus) {
        this.bbdProdus.set(bbdProdus);
    }

    public int getCantitateIntrata() {
        return cantitateIntrata.get();
    }

    public SimpleIntegerProperty cantitateIntrataProperty() {
        return cantitateIntrata;
    }

    public void setCantitateIntrata(int cantitateIntrata) {
        this.cantitateIntrata.set(cantitateIntrata);
    }

    public float getPretAchizitie() {
        return pretAchizitie.get();
    }

    public SimpleFloatProperty pretAchizitieProperty() {
        return pretAchizitie;
    }

    public void setPretAchizitie(float pretAchizitie) {
        this.pretAchizitie.set(pretAchizitie);
    }

    public float getDiscountProdus() {
        return discountProdus.get();
    }

    public SimpleFloatProperty discountProdusProperty() {
        return discountProdus;
    }

    public void setDiscountProdus(float discountProdus) {
        this.discountProdus.set(discountProdus);
    }

    public String getObservatiiProdus() {
        return observatiiProdus.get();
    }

    public SimpleStringProperty observatiiProdusProperty() {
        return observatiiProdus;
    }

    public void setObservatiiProdus(String observatiiProdus) {
        this.observatiiProdus.set(observatiiProdus);
    }

    public String getDenumireProdus() {
        return denumireProdus.get();
    }

    public SimpleStringProperty denumireProdusProperty() {
        return denumireProdus;
    }

    public void setDenumireProdus(String denumireProdus) {
        this.denumireProdus.set(denumireProdus);
    }

    public float getPretPentruVanzare() {
        return pretPentruVanzare.get();
    }

    public SimpleFloatProperty pretPentruVanzareProperty() {
        return pretPentruVanzare;
    }

    public void setPretPentruVanzare(float pretPentruVanzare) {
        this.pretPentruVanzare.set(pretPentruVanzare);
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

    public float getAdaosProdus() {
        return adaosProdus.get();
    }

    public SimpleFloatProperty adaosProdusProperty() {
        return adaosProdus;
    }

    public void setAdaosProdus(float adaosProdus) {
        this.adaosProdus.set(adaosProdus);
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

    public float getValoareTva() {
        return valoareTva.get();
    }

    public SimpleFloatProperty valoareTvaProperty() {
        return valoareTva;
    }

    public void setValoareTva(float valoareTva) {
        this.valoareTva.set(valoareTva);
    }


    /**
     * Metoda foloseste un script PL-SQL pentru a insera o intrare in DB
     * si apoi folosind secventa curenta a id-ului intrarii sa insereze
     * o intrare in stoc corespunzatoare
     */
    public void insereazaIntrareSiStocInBd() throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String stringBbd = getBbdProdus().format(formatter);
        int nrIntrariInserate = 0;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(INTRARI_STOC_INSERT);

            ps.setLong(1, getIdNomenclator());
            ps.setLong(2, getIdDocument());
            ps.setString(3, getLotProdus());
            ps.setString(4, stringBbd);
            ps.setInt(5, getCantitateIntrata());
            ps.setFloat(6, getPretAchizitie());
            ps.setFloat(7, getDiscountProdus());
            ps.setString(8, getObservatiiProdus());
            ps.setFloat(9, getPretPentruVanzare());
            ps.setInt(10, getCantitateIntrata());

            nrIntrariInserate = ps.executeUpdate();
            if (nrIntrariInserate == 0) {
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
