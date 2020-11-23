package farma.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class DocOperatii {

    private SimpleLongProperty idDocument;
    private SimpleLongProperty nrDocument;
    private SimpleObjectProperty<LocalDate> dataEmitere;
    private SimpleObjectProperty<LocalDate> dataOperare;
    private SimpleIntegerProperty termenPlata;
    private SimpleLongProperty idOperatie;
    private SimpleLongProperty idPartener;
    private SimpleLongProperty idOperator;
    private SimpleStringProperty observatii;
    private SimpleStringProperty numePartener;
    private SimpleStringProperty numeOperator;
    private SimpleFloatProperty pretProduse;
    private SimpleIntegerProperty nrProduse;

    public DocOperatii(SimpleLongProperty idDocument,
                       SimpleLongProperty nrDocument,
                       SimpleObjectProperty<LocalDate> dataEmitere,
                       SimpleObjectProperty<LocalDate> dataOperare,
                       SimpleIntegerProperty termenPlata,
                       SimpleLongProperty idOperatie,
                       SimpleLongProperty idPartener,
                       SimpleLongProperty idOperator,
                       SimpleStringProperty observatii,
                       SimpleStringProperty numePartener,
                       SimpleStringProperty numeOperator,
                       SimpleFloatProperty pretProduse,
                       SimpleIntegerProperty nrProduse) {
        this.idDocument = idDocument;
        this.nrDocument = nrDocument;
        this.dataEmitere = dataEmitere;
        this.dataOperare = dataOperare;
        this.termenPlata = termenPlata;
        this.idOperatie = idOperatie;
        this.idPartener = idPartener;
        this.idOperator = idOperator;
        this.observatii = observatii;
        this.numePartener = numePartener;
        this.numeOperator = numeOperator;
        this.pretProduse = pretProduse;
        this.nrProduse = nrProduse;
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

    public LocalDate getDataEmitere() {
        return dataEmitere.get();
    }

    public SimpleObjectProperty<LocalDate> dataEmitereProperty() {
        return dataEmitere;
    }

    public void setDataEmitere(LocalDate dataEmitere) {
        this.dataEmitere.set(dataEmitere);
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

    public int getTermenPlata() {
        return termenPlata.get();
    }

    public SimpleIntegerProperty termenPlataProperty() {
        return termenPlata;
    }

    public void setTermenPlata(int termenPlata) {
        this.termenPlata.set(termenPlata);
    }

    public long getIdOperatie() {
        return idOperatie.get();
    }

    public SimpleLongProperty idOperatieProperty() {
        return idOperatie;
    }

    public void setIdOperatie(long idOperatie) {
        this.idOperatie.set(idOperatie);
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

    public long getIdOperator() {
        return idOperator.get();
    }

    public SimpleLongProperty idOperatorProperty() {
        return idOperator;
    }

    public void setIdOperator(long idOperator) {
        this.idOperator.set(idOperator);
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

    public String getNumePartener() {
        return numePartener.get();
    }

    public SimpleStringProperty numePartenerProperty() {
        return numePartener;
    }

    public void setNumePartener(String numePartener) {
        this.numePartener.set(numePartener);
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

    public float getPretProduse() {
        return pretProduse.get();
    }

    public SimpleFloatProperty pretProduseProperty() {
        return pretProduse;
    }

    public void setPretProduse(float pretProduse) {
        this.pretProduse.set(pretProduse);
    }

    public int getNrProduse() {
        return nrProduse.get();
    }

    public SimpleIntegerProperty nrProduseProperty() {
        return nrProduse;
    }

    public void setNrProduse(int nrProduse) {
        this.nrProduse.set(nrProduse);
    }
}
