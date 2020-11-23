package farma.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Producatori {

    private SimpleLongProperty idProducator;
    private SimpleStringProperty numeProducator;

    public Producatori(SimpleLongProperty idProducator, SimpleStringProperty numeProducator) {
        this.idProducator = idProducator;
        this.numeProducator = numeProducator;
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

    public String getNumeProducator() {
        return numeProducator.get();
    }

    public SimpleStringProperty numeProducatorProperty() {
        return numeProducator;
    }

    public void setNumeProducator(String numeProducator) {
        this.numeProducator.set(numeProducator);
    }

    @Override
    public String toString() {
        return getNumeProducator();
    }
}
