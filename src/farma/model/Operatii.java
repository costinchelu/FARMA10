package farma.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Operatii {

    private SimpleLongProperty idOperatie;
    private SimpleStringProperty numeOperatie;

    public Operatii(SimpleLongProperty idOperatie, SimpleStringProperty numeOperatie) {
        this.idOperatie = idOperatie;
        this.numeOperatie = numeOperatie;
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

    public String getNumeOperatie() {
        return numeOperatie.get();
    }

    public SimpleStringProperty numeOperatieProperty() {
        return numeOperatie;
    }

    public void setNumeOperatie(String numeOperatie) {
        this.numeOperatie.set(numeOperatie);
    }

    @Override
    public String toString() {
        return "Operatii{" +
                "idOperatie=" + idOperatie +
                ", numeOperatie=" + numeOperatie +
                '}';
    }
}
