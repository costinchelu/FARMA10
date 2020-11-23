package farma.view;

import farma.model.Dci;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddDciController implements Initializable, ControllerClass {

    @FXML private TextField denDciTF;
    @FXML private TextField codDciTF;
    @FXML private TextField grupaAtcTF;
    @FXML private TextField observatiiTF;
    @FXML private Label dciMesajLB;
    @FXML private Label headerLB;
    @FXML private Button okBTN;
    private Dci dci;


    /**
     * initializarea clasei
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dciMesajLB.setText("");
    }

    /**
     * Buton pentru intoarcerea la tabela nomenclatorului DCI cu anularea modificărilor
     */
    public void butonBackToTabelDci(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelDci.fxml", "Nomenclator DCI");
    }

    /**
     * citeste din scena si insereaza in BD o noua intrare DCI
     * la acționare a butonului, ne reintoarcem la tabela nomenclatorului DCI
     */
    public void butonAdaugaSalveazaNouDci(ActionEvent event) throws IOException, SQLException {

        if (dci != null) {
            // caz de update Dci
            updateDciDinView();
            dci.updateDciInBd();

        } else {
            // caz de contruire a unui nou dci
            try {
                SimpleStringProperty newDenDci = new SimpleStringProperty(denDciTF.getText());
                SimpleStringProperty newCodDci = new SimpleStringProperty(codDciTF.getText());
                SimpleStringProperty newGrupaAtc = new SimpleStringProperty(grupaAtcTF.getText());
                SimpleStringProperty newObservatii = new SimpleStringProperty(observatiiTF.getText());

                Dci dci = new Dci(newCodDci, newDenDci, newGrupaAtc, newObservatii);

                dciMesajLB.setText("");
                dci.insertIntoDb();
                dciMesajLB.setTextFill(Color.GREEN);
                dciMesajLB.setText("A fost inserata o noua intrare DCI.");

            } catch (Exception e) {
                dciMesajLB.setText(e.getMessage());
            }
        }

        butonBackToTabelDci(event);
    }

    /**
     * Primeste obiectul Dci de la tabela nomenclator DCI și populează campurile cu informatiile de editat
     * @param dciPrimit = tabela DCI trimite un obiect către scena de editare/adaugare
     */
    @Override
    public void preloadObject(Object dciPrimit) {
        this.dci = (Dci) dciPrimit;

        denDciTF.setText(((Dci) dciPrimit).getDenDci());
        grupaAtcTF.setText(((Dci) dciPrimit).getGrupaAtc());
        codDciTF.setText(((Dci) dciPrimit).getCodDci());
        observatiiTF.setText(((Dci) dciPrimit).getObservatii());
        headerLB.setText("Editarea intrarii din nomenclatorul DCI");
        okBTN.setText("OK");
    }

    /**
     * citeste noile valori ale Dci-ului de editat pentru update intrare in BD
     */
    public void updateDciDinView() {
        dci.setDenDci(denDciTF.getText());
        dci.setCodDci(codDciTF.getText());
        dci.setGrupaAtc(grupaAtcTF.getText());
        dci.setObservatii(observatiiTF.getText());
    }
}
