package farma.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Nomenclator {

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

    /**
     * constructor cu idNomenclator
     */
    public Nomenclator(SimpleLongProperty idNomenclator,
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
     * constructor fara idNomenclator
     */
    public Nomenclator(SimpleStringProperty denumireProd,
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

    @Override
    public String toString() {
        return getDenumireProd();
    }
}
