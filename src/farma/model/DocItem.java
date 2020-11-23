package farma.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class DocItem {

    private SimpleLongProperty idDocument;
    private SimpleFloatProperty pretAchizitie;
    private SimpleObjectProperty<LocalDate> bbd;
    private SimpleStringProperty lot;
    private SimpleIntegerProperty cantitate;
    private SimpleFloatProperty pretVanzare;
    private SimpleStringProperty denumire;
    private SimpleStringProperty concentratie;
    private SimpleStringProperty ambalare;
    private SimpleStringProperty producator;
    private SimpleStringProperty forma;
    private SimpleIntegerProperty procTva;
    private SimpleStringProperty dci;


    public DocItem(SimpleLongProperty idDocument,
                   SimpleFloatProperty pretAchizitie,
                   SimpleObjectProperty<LocalDate> bbd,
                   SimpleStringProperty lot,
                   SimpleIntegerProperty cantitate,
                   SimpleFloatProperty pretVanzare,
                   SimpleStringProperty denumire,
                   SimpleStringProperty concentratie,
                   SimpleStringProperty ambalare,
                   SimpleStringProperty producator,
                   SimpleStringProperty forma,
                   SimpleIntegerProperty procTva,
                   SimpleStringProperty dci) {
        this.idDocument = idDocument;
        this.pretAchizitie = pretAchizitie;
        this.bbd = bbd;
        this.lot = lot;
        this.cantitate = cantitate;
        this.pretVanzare = pretVanzare;
        this.denumire = denumire;
        this.concentratie = concentratie;
        this.ambalare = ambalare;
        this.producator = producator;
        this.forma = forma;
        this.procTva = procTva;
        this.dci = dci;
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

    public float getPretAchizitie() {
        return pretAchizitie.get();
    }

    public SimpleFloatProperty pretAchizitieProperty() {
        return pretAchizitie;
    }

    public void setPretAchizitie(float pretAchizitie) {
        this.pretAchizitie.set(pretAchizitie);
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

    public String getLot() {
        return lot.get();
    }

    public SimpleStringProperty lotProperty() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot.set(lot);
    }

    public int getCantitate() {
        return cantitate.get();
    }

    public SimpleIntegerProperty cantitateIntrareProperty() {
        return cantitate;
    }

    public void setCantitateIntrare(int cantitateIntrare) {
        this.cantitate.set(cantitateIntrare);
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

    public String getDenumire() {
        return denumire.get();
    }

    public SimpleStringProperty denumireProperty() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire.set(denumire);
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

    public String getAmbalare() {
        return ambalare.get();
    }

    public SimpleStringProperty ambalareProperty() {
        return ambalare;
    }

    public void setAmbalare(String ambalare) {
        this.ambalare.set(ambalare);
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

    public int getProcTva() {
        return procTva.get();
    }

    public SimpleIntegerProperty procTvaProperty() {
        return procTva;
    }

    public void setProcTva(int procTva) {
        this.procTva.set(procTva);
    }

    public String getDci() {
        return dci.get();
    }

    public SimpleStringProperty dciProperty() {
        return dci;
    }

    public void setDci(String dci) {
        this.dci.set(dci);
    }
}
