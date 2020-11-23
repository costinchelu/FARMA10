package farma.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Forma {

    private SimpleLongProperty idForma;
    private SimpleStringProperty numeForma;
    private SimpleStringProperty prescurtare;

    public Forma(SimpleLongProperty idForma, SimpleStringProperty numeForma, SimpleStringProperty prescurtare) {
        this.idForma = idForma;
        this.numeForma = numeForma;
        this.prescurtare = prescurtare;
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

    public String getNumeForma() {
        return numeForma.get();
    }

    public SimpleStringProperty numeFormaProperty() {
        return numeForma;
    }

    public void setNumeForma(String numeForma) {
        this.numeForma.set(numeForma);
    }

    public String getPrescurtare() {
        return prescurtare.get();
    }

    public SimpleStringProperty prescurtareProperty() {
        return prescurtare;
    }

    public void setPrescurtare(String prescurtare) {
        this.prescurtare.set(prescurtare);
    }

    @Override
    public String toString() {
        return getNumeForma();
    }
}
