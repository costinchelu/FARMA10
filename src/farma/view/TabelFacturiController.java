package farma.view;

import farma.model.DocOperatii;
import farma.util.DBConstants;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TabelFacturiController implements Initializable, DBConstants {

    @FXML private ComboBox<String> tipFactCB;
    @FXML private TableView<DocOperatii> tabelTV;
    @FXML private TableColumn<DocOperatii, Long> nrDocTC;
    @FXML private TableColumn<DocOperatii, Float> valFactTC;
    @FXML private TableColumn<DocOperatii, Integer> nrProduseTC;
    @FXML private TableColumn<DocOperatii, LocalDate> dataEmitereTC;
    @FXML private TableColumn<DocOperatii, LocalDate> dataOperareTC;
    @FXML private TableColumn<DocOperatii, Integer> termenTC;
    @FXML private TableColumn<DocOperatii, String> partenerTC;
    @FXML private TableColumn<DocOperatii, String> operatorTC;
    @FXML private TableColumn<DocOperatii, String> obsTC;
    @FXML private Label msgLB;
    @FXML private Button detaliiFacturaBTN;
    ObservableList<DocOperatii> documenteIntrare = FXCollections.observableArrayList();
    ObservableList<DocOperatii> documenteIesire = FXCollections.observableArrayList();

    @FXML private Button graficOpBTN;
    @FXML private Button graficFurnizoriBTN;
    @FXML private Button graficClientiBTN;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // dezactivare buton facturi
        detaliiFacturaBTN.setDisable(true);

        // setari afisare coloane Table View
        nrDocTC.setCellValueFactory(new PropertyValueFactory<>("nrDocument"));
        valFactTC.setCellValueFactory(new PropertyValueFactory<>("pretProduse"));
        nrProduseTC.setCellValueFactory(new PropertyValueFactory<>("nrProduse"));
        dataEmitereTC.setCellValueFactory(new PropertyValueFactory<>("dataEmitere"));
        dataOperareTC.setCellValueFactory(new PropertyValueFactory<>("dataOperare"));
        termenTC.setCellValueFactory(new PropertyValueFactory<>("termenPlata"));
        partenerTC.setCellValueFactory(new PropertyValueFactory<>("numePartener"));
        operatorTC.setCellValueFactory(new PropertyValueFactory<>("numeOperator"));
        obsTC.setCellValueFactory(new PropertyValueFactory<>("observatii"));
        tabelTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // incarcare obiecte din BD
        try {
            documenteIntrare.addAll(getDocumente(SELECT_FACTURI_INTRARE));
            documenteIesire.addAll(getDocumente(SELECT_FACTURI_IESIRE));
        } catch (SQLException e) {
            msgLB.setText("Nu se poate efectua consultarea bazei de date");
            System.err.println(e.getMessage());
        }

        // incarcare combobox
        ObservableList<String> optCombo = FXCollections.observableArrayList();
        String[] optiuniCombo = {"Facturi intrare", "Facturi iesire"};
        optCombo.addAll(optiuniCombo);
        tipFactCB.setItems(optCombo);

        // actiuni pentru alegerea din combobox
        EventHandler<ActionEvent> afisareFacturi = event -> {
            detaliiFacturaBTN.setDisable(true);
            if (tipFactCB.getValue().equals("Facturi intrare")) {
                tabelTV.getItems().clear();
                tabelTV.getItems().addAll(documenteIntrare);
            }
            if (tipFactCB.getValue().equals("Facturi iesire")) {
                tabelTV.getItems().clear();
                tabelTV.getItems().addAll(documenteIesire);

            }
        };
        tipFactCB.setOnAction(afisareFacturi);
    }

    /**
     * Actiune pentru intoarcerea in meniul principal
     */
    public void butonBackToMain(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "MainScene.fxml", "FARMA 10");
    }

    /**
     * Actiune pentru accesarea graficului de operatii
     */
    public void butonGraficOperatii(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "GraficOperatii.fxml", "Raport valori operatii de intrare-iesire");
    }

    /**
     * Actiune pentru accesarea graficului de furnizori
     */
    public void butonGraficFurnizori(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "GraficFurnizori.fxml",
                "Raport valori-intrari pe ultimul trimestru");
    }

    /**
     * Actiune pentru accesarea graficului de clienti
     */
    public void butonGraficClienti(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "GraficClienti.fxml",
                "Raport valori-iesiri pe ultimul trimestru");
    }

    /**
     * Actiuni pentru detalii factura
     */
    public void butonDetaliiFactura(ActionEvent event) throws IOException {
        Long idDocumentSelectat = tabelTV.getSelectionModel().getSelectedItem().getIdDocument();
        SceneChanger sc = new SceneChanger();
        ControllerClass controllerClass = new TabelDetaliiFacturaController();
        sc.changeScenes(event, "TabelDetaliiFactura.fxml", "Detalii factura", idDocumentSelectat, controllerClass);
    }

    /**
     * metoda pentru actiuni la selectia unui produs din lista
     */
    public void produsSelectat() {
        detaliiFacturaBTN.setDisable(false);
    }

    /**
     * Selecteaza documentele din BD
     * @return: documentele de intrare pentru afisare in table view
     */
    public ObservableList<DocOperatii> getDocumente(String operatie) throws SQLException {
        ObservableList<DocOperatii> docOperatiiObsList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(operatie);

            while (resultSet.next()) {
                SimpleLongProperty idDocument = new SimpleLongProperty(resultSet.getLong("id_document"));
                SimpleLongProperty nrDocument = new SimpleLongProperty(resultSet.getLong("nr_document"));
                LocalDate dataEmitereDinBd = resultSet.getDate("data_emitere").toLocalDate();
                LocalDate dataOperareDinBd = resultSet.getDate("data_operare").toLocalDate();
                SimpleObjectProperty<LocalDate> dataEmitere = new SimpleObjectProperty<>(dataEmitereDinBd);
                SimpleObjectProperty<LocalDate> dataOperare = new SimpleObjectProperty<>(dataOperareDinBd);
                SimpleIntegerProperty termenPlata = new SimpleIntegerProperty(resultSet.getInt("termen_plata"));
                SimpleLongProperty idOperatie = new SimpleLongProperty(resultSet.getLong("id_operatie"));
                SimpleLongProperty idPartener = new SimpleLongProperty(resultSet.getLong("id_partener"));
                SimpleLongProperty idOperator = new SimpleLongProperty(resultSet.getLong("id_operator"));
                SimpleStringProperty observatii = new SimpleStringProperty(resultSet.getString("observatii"));
                SimpleStringProperty numePartener = new SimpleStringProperty(resultSet.getString("nume_partener"));
                SimpleStringProperty numeOperator = new SimpleStringProperty(resultSet.getString("nume_operator"));
                Float pretProduseFactura = roundFloat(resultSet.getFloat("pret_produse"), 2);
                SimpleFloatProperty pretProduse = new SimpleFloatProperty(pretProduseFactura);
                SimpleIntegerProperty nrProduse = new SimpleIntegerProperty(resultSet.getInt("nr_produse"));

                DocOperatii document = new DocOperatii(idDocument, nrDocument, dataEmitere, dataOperare, termenPlata, idOperatie,
                        idPartener, idOperator, observatii, numePartener, numeOperator, pretProduse, nrProduse);

                docOperatiiObsList.add(document);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return docOperatiiObsList;
    }

    /**
     * metoda pentru rotunjirea unui float la un anumit numar de zecimale
     */
    public static float roundFloat(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
    }
}
