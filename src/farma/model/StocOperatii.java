package farma.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class StocOperatii {

    private SimpleLongProperty idStoc;
    private SimpleLongProperty idIntrare;
    private SimpleFloatProperty pretVanzare;
    private SimpleIntegerProperty cantitateDisp;
    private SimpleStringProperty lot;
    private SimpleObjectProperty<LocalDate> bbd;
    private SimpleStringProperty denumireProd;
    private SimpleLongProperty codBare;
    private SimpleStringProperty concentratie;
    private SimpleFloatProperty adaos;
    private SimpleStringProperty producator;
    private SimpleIntegerProperty tva;
    private SimpleLongProperty idDocument;
    private SimpleLongProperty nrDocument;
    private SimpleObjectProperty<LocalDate> dataOperare;
    private SimpleStringProperty furnizor;
    private SimpleStringProperty dci;

    public StocOperatii(SimpleLongProperty idStoc,
                        SimpleLongProperty idIntrare,
                        SimpleFloatProperty pretVanzare,
                        SimpleIntegerProperty cantitateDisp,
                        SimpleStringProperty lot,
                        SimpleObjectProperty<LocalDate> bbd,
                        SimpleStringProperty denumireProd,
                        SimpleLongProperty codBare,
                        SimpleStringProperty concentratie,
                        SimpleFloatProperty adaos,
                        SimpleStringProperty producator,
                        SimpleIntegerProperty tva,
                        SimpleLongProperty idDocument,
                        SimpleLongProperty nrDocument,
                        SimpleObjectProperty<LocalDate> dataOperare,
                        SimpleStringProperty furnizor,
                        SimpleStringProperty dci) {
        this.idStoc = idStoc;
        this.idIntrare = idIntrare;
        this.pretVanzare = pretVanzare;
        this.cantitateDisp = cantitateDisp;
        this.lot = lot;
        this.bbd = bbd;
        this.denumireProd = denumireProd;
        this.codBare = codBare;
        this.concentratie = concentratie;
        this.adaos = adaos;
        this.producator = producator;
        this.tva = tva;
        this.idDocument = idDocument;
        this.nrDocument = nrDocument;
        this.dataOperare = dataOperare;
        this.furnizor = furnizor;
        this.dci = dci;
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

    public String getDenumireProd() {
        return denumireProd.get();
    }

    public SimpleStringProperty denumireProdProperty() {
        return denumireProd;
    }

    public void setDenumireProd(String denumireProd) {
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

    public String getProducator() {
        return producator.get();
    }

    public SimpleStringProperty producatorProperty() {
        return producator;
    }

    public void setProducator(String producator) {
        this.producator.set(producator);
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

    public long getIdDocument() {
        return idDocument.get();
    }

    public SimpleLongProperty idDocumentProperty() {
        return idDocument;
    }

    public void setIdDocument(long idDocument) {
        this.idDocument.set(idDocument);
    }

    public long getNrDocument() {
        return nrDocument.get();
    }

    public SimpleLongProperty nrDocumentProperty() {
        return nrDocument;
    }

    public void setNrDocument(long nrDocument) {
        this.nrDocument.set(nrDocument);
    }

    public LocalDate getDataOperare() {
        return dataOperare.get();
    }

    public SimpleObjectProperty<LocalDate> dataOperareProperty() {
        return dataOperare;
    }

    public void setDataOperare(LocalDate dataOperare) {
        this.dataOperare.set(dataOperare);
    }

    public String getFurnizor() {
        return furnizor.get();
    }

    public SimpleStringProperty furnizorProperty() {
        return furnizor;
    }

    public void setFurnizor(String furnizor) {
        this.furnizor.set(furnizor);
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
