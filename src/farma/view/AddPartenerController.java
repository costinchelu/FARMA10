package farma.view;

import farma.model.Partener;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddPartenerController implements Initializable, ControllerClass {

    @FXML private TextField numePartTF;
    @FXML private TextField adresaTF;
    @FXML private TextField telefonTF;
    @FXML private TextField emailTF;
    @FXML private TextField cuiTF;
    @FXML private TextField ibanTF;
    @FXML private Label msgLB;
    @FXML private Label headerLB;
    @FXML private Button okBTN;
    private Partener partener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Buton pentru intoarcerea la tabela nomenclatorului parteneri cu anularea modificărilor
     */
    public void butonBackToTabelParteneri(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelParteneri.fxml", "Nomenclator parteneri");
    }

    /**
     * citeste din scena si insereaza in BD o noua intrare partener
     * ne intoarcem la scena listei de parteneri
     */
    public void butonSalveazaNouPartener(ActionEvent event) throws IOException, SQLException {
        if (partener != null) {
            // update partener
            updatePartenerDinView();
            partener.updatePartenerInBd();
        } else {
            // construim un nou partener pentru inserare
            try {
                SimpleStringProperty newNumePart = new SimpleStringProperty(numePartTF.getText());
                SimpleStringProperty newAdresaPart = new SimpleStringProperty(adresaTF.getText());
                SimpleStringProperty newTelefonPart = new SimpleStringProperty(telefonTF.getText());
                SimpleStringProperty newEmailPart = new SimpleStringProperty(emailTF.getText());
                SimpleStringProperty newCuiPart = new SimpleStringProperty(cuiTF.getText());
                SimpleStringProperty newIbanPart = new SimpleStringProperty(ibanTF.getText());

                Partener partener = new Partener(newNumePart, newAdresaPart, newTelefonPart, newEmailPart, newCuiPart, newIbanPart);
                partener.insertPartener();

            } catch (Exception e) {
                msgLB.setText(e.getMessage());
            }
        }

        butonBackToTabelParteneri(event);
    }

    /**
     * citeste noile valori ale partenerului de editat
     */
    public void updatePartenerDinView() {
        partener.setNumePartener(numePartTF.getText());
        partener.setAdresa(adresaTF.getText());
        partener.setTelefon(telefonTF.getText());
        partener.setEmail(emailTF.getText());
        partener.setCont(ibanTF.getText());
        partener.setCui(cuiTF.getText());
    }

    @Override
    public void preloadObject(Object partenerPrimit) {
        this.partener = (Partener) partenerPrimit;

        numePartTF.setText(((Partener) partenerPrimit).getNumePartener());
        adresaTF.setText(((Partener) partenerPrimit).getAdresa());
        telefonTF.setText(((Partener) partenerPrimit).getTelefon());
        emailTF.setText(((Partener) partenerPrimit).getEmail());
        cuiTF.setText(((Partener) partenerPrimit).getCui());
        ibanTF.setText(((Partener) partenerPrimit).getCont());
        headerLB.setText("Editarea intrarii din nomenclatorul parteneri");
        okBTN.setText("OK");
    }
}
