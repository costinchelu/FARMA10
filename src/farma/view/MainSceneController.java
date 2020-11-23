package farma.view;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void butonNomeclatorDci(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelDci.fxml", "Nomeclator DCI");
    }

    public void butonNomeclatorProduse(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelNomProduse.fxml", "Nomeclator produse in stoc");
    }

    public void butonNomenclatorParteneri(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelParteneri.fxml", "Nomeclator parteneri");
    }

    public void butonFacturaIntrare(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FacturaIntrare.fxml", "Factura intrare");
    }

    public void butonFacturaIesire(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FacturaIesire.fxml", "Factura iesire");
    }

    public void butonCentralizatorFacturi(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelFacturi.fxml", "Centralizator facturi");
    }

    public void butonStocCurent(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelStoc.fxml", "Stoc curent");
    }

    public void butonROperatii(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "GraficOperatii.fxml", "Raport valori operatii de intrare-iesire");
    }

    public void butonRClienti(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "GraficClienti.fxml", "Raport valori iesiri pe ultimul trimestru");
    }

    public void butonRFurnizori(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "GraficFurnizori.fxml", "Raport valori intrari pe ultimul trimestru");
    }

}
