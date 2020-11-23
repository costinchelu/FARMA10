package farma.view;

import farma.model.NomenclatorExtins;
import farma.util.DBConstants;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class TabelNomProduseController implements Initializable, DBConstants {

    @FXML private Label titluLB;
    @FXML private TableView<NomenclatorExtins> tabelTBV;
    @FXML private TableColumn<NomenclatorExtins, String> denProdTCL;
    @FXML private TableColumn<NomenclatorExtins, String> denDCITBC;
    @FXML private TableColumn<NomenclatorExtins, String> concentratieTBC;
    @FXML private TableColumn<NomenclatorExtins, String> producatorTBC;
    @FXML private TableColumn<NomenclatorExtins, Integer> tvaTBCL;
    @FXML private TableColumn<NomenclatorExtins, Float> adaosTBCL;
    @FXML private TableColumn<NomenclatorExtins, Float> pretTBCL;
    @FXML private TableColumn<NomenclatorExtins, Long> codBareTBCL;
    @FXML private Label msgLB;
    @FXML private Button editBTN;
    @FXML private Button stergeBTN;
    @FXML private Button adaugaBTN;
    @FXML private Button backBTN;
    List<Long> listaIdNomenclatorFolosite;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // dezactivare butoane de stergere si editare
        editBTN.setDisable(true);
        stergeBTN.setDisable(true);

        // ascund eticheta pentru mesaje
        msgLB.setText("");
        msgLB.setTextFill(Color.RED);

        // configurare coloane
        denProdTCL.setCellValueFactory(new PropertyValueFactory<>("denumireProd"));
        denDCITBC.setCellValueFactory(new PropertyValueFactory<>("denumireDci"));
        concentratieTBC.setCellValueFactory(new PropertyValueFactory<>("concentratie"));
        producatorTBC.setCellValueFactory(new PropertyValueFactory<>("denumireProducator"));
        tvaTBCL.setCellValueFactory(new PropertyValueFactory<>("procentTva"));
        adaosTBCL.setCellValueFactory(new PropertyValueFactory<>("adaos"));
        pretTBCL.setCellValueFactory(new PropertyValueFactory<>("pretImpus"));
        codBareTBCL.setCellValueFactory(new PropertyValueFactory<>("codBare"));

        // pentru selectie simpla din TableView
        tabelTBV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // incarca din BD toate DCI si creaza un ObservableList pentru afisarea in TableView
        try {
            loadNomenclatorExtins();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        // incarca lista id_nomenclator folosite
        listaIdNomenclatorFolosite = new ArrayList<>();
        try {
            listaIdNomenclatorFolosite = construiesteListaIdNomenclator();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Incarca o lista de intrari DB Nomenclator, dar si din tabelele aditionale si le afiseaza in Table View
     */
    private void loadNomenclatorExtins() throws SQLException {
        ObservableList<NomenclatorExtins> nomenclatorExtinsObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_NOMENCLATOR_EXT);

            while (resultSet.next()) {
                SimpleLongProperty idNomenclator = new SimpleLongProperty(resultSet.getLong("id_nomenclator"));
                SimpleStringProperty denumireProd = new SimpleStringProperty(resultSet.getString("denumire_prod"));
                SimpleLongProperty codBare = new SimpleLongProperty(resultSet.getLong("cod_bare"));
                SimpleStringProperty concentratie = new SimpleStringProperty(resultSet.getString("concentratie"));
                SimpleFloatProperty adaos = new SimpleFloatProperty(resultSet.getFloat("adaos"));
                SimpleStringProperty ambalare = new SimpleStringProperty(resultSet.getString("ambalare"));
                SimpleFloatProperty pretImpus = new SimpleFloatProperty(resultSet.getFloat("pret_impus"));
                SimpleStringProperty observatii = new SimpleStringProperty(resultSet.getString("observatii"));
                SimpleLongProperty idForma = new SimpleLongProperty(resultSet.getLong("id_forma"));
                SimpleLongProperty idTva = new SimpleLongProperty(resultSet.getLong("id_tva"));
                SimpleLongProperty idDci = new SimpleLongProperty(resultSet.getLong("id_dci"));
                SimpleLongProperty idProducator = new SimpleLongProperty(resultSet.getLong("id_producator"));
                SimpleStringProperty numeForma = new SimpleStringProperty(resultSet.getString("nume_forma"));
                SimpleIntegerProperty procentTva = new SimpleIntegerProperty(resultSet.getInt("procent_tva"));
                SimpleStringProperty denumireDci = new SimpleStringProperty(resultSet.getString("den_dci"));
                SimpleStringProperty denumireProducator = new SimpleStringProperty(resultSet.getString("nume_producator"));

                NomenclatorExtins newNomenclator =
                        new NomenclatorExtins(idNomenclator, denumireProd, codBare, concentratie, adaos,
                                ambalare, pretImpus, observatii, idForma, idTva, idDci, idProducator,
                                numeForma, procentTva, denumireDci, denumireProducator);

                nomenclatorExtinsObservableList.add(newNomenclator);
            }

            // in cazul in care dorim sortarea listei dupa denumirea DCI in ordine alfabetica fara ORDER BY:
//            SortedList<NomenclatorExtins> sortedList = new SortedList<NomenclatorExtins>(nomenclatorExtinsObservableList, Comparator.comparing(NomenclatorExtins::getDenumireProd));
            tabelTBV.getItems().addAll(nomenclatorExtinsObservableList);

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
    }

    /**
     * Buton pt. stergerea intrarilor din tabela nomenclatorului de produse
     */
    public void butonStergeProdusDinNomenclator(ActionEvent event) throws SQLException {
        ObservableList<NomenclatorExtins> toateIntrarile;
        toateIntrarile = tabelTBV.getItems();

        if (listaIdNomenclatorFolosite.contains(tabelTBV.getSelectionModel().getSelectedItem().getIdNomenclator())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la stergerea intrarii");
            alert.setHeaderText("Eroare la stergerea intrarii");
            alert.setContentText("Stergerea nu poate fi efectuata \ndeoarece intrarea in nomenclator este  deja utilizata");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmarea stergerii");
            alert.setHeaderText("Confirmare pentru stergerea intrarii");
            alert.setContentText("Sunteti sigur ca doriti stergerea intrarii?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                NomenclatorExtins produsSters = tabelTBV.getSelectionModel().getSelectedItem();
                long idProdusSters = produsSters.getIdNomenclator();

                boolean isDeleted = produsSters.deleteProdusNomenclator(idProdusSters);
                if (!isDeleted) {
                    msgLB.setText("Nu a fost stearsa nicio intrare");
                } else {
                    System.out.println("Intrarea cu nr " + idProdusSters + " a fost stearsa din BD");
                    toateIntrarile.remove(produsSters);
                }
            }
        }
    }

    /**
     * Returneaza o lista de id_nomenclator distincte din tabela intrari
     * folosita pentru a decide daca o intrare nomenclator poate fi stearsa
     */
    public List<Long> construiesteListaIdNomenclator() throws SQLException {
        List<Long> lista = new ArrayList<>();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ID_NOMENCLATOR_FOLOSITE);

            while (resultSet.next()) {
                lista.add(resultSet.getLong("id_nomenclator"));
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
        return lista;
    }

    /**
     * Buton pentru adaugarea unei noi intrari in nomenclatorul produselor
     */
    public void addNewProdus(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "AddProduse.fxml", "Adaugare produs");
    }

    /**
     * Buton pt. editarea intrarilor din tabela nomenclatorului de produse
     */
    public void butonEditareNomenclatorProduse(ActionEvent event) throws IOException {

        SceneChanger sc = new SceneChanger();
        NomenclatorExtins nomenclatorSelectat = this.tabelTBV.getSelectionModel().getSelectedItem();

        AddProduseController newAddProduseController = new AddProduseController();

        sc.changeScenes(event, "AddProduse.fxml",
                "Editare intrare nomenclator", nomenclatorSelectat, newAddProduseController);
    }

    /**
     * Actiune pentru intoarcerea in meniul principal
     */
    public void butonBackToMain(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "MainScene.fxml", "FARMA 10");
    }

    /**
     * Daca o intrare din tabel a fost selectata, activez butoanele de editare si stergere
     */
    public void produsSelectat() {
        editBTN.setDisable(false);
        stergeBTN.setDisable(false);
    }
}
