package farma.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Tva {

    private SimpleLongProperty idTva;
    private SimpleStringProperty tipTva;
    private SimpleIntegerProperty procentTva;

    public Tva(SimpleLongProperty idTva, SimpleStringProperty tipTva, SimpleIntegerProperty procentTva) {
        this.idTva = idTva;
        this.tipTva = tipTva;
        this.procentTva = procentTva;
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

    public String getTipTva() {
        return tipTva.get();
    }

    public SimpleStringProperty tipTvaProperty() {
        return tipTva;
    }

    public void setTipTva(String tipTva) {
        this.tipTva.set(tipTva);
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

    @Override
    public String toString() {
        return  String.valueOf(getProcentTva());
    }
}
