package farma.view;

import farma.model.Dci;
import farma.util.DBConstants;
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

public class TabelDciController implements Initializable, DBConstants {

    @FXML private Label titluLB;
    @FXML private TableView<Dci> tabelTBV;
    @FXML private TableColumn<Dci, String> denDciTCL;
    @FXML private TableColumn<Dci, String> grupaAtcTBC;
    @FXML private TableColumn<Dci, String> codDciTBC;
    @FXML private TableColumn<Dci, String> observatiiTBC;
    @FXML private Button editBTN;
    @FXML private Button stergeBTN;
    @FXML private Button adaugaBTN;
    @FXML private Button backBTN;
    @FXML private Label msgLB;
    List<Long> listaIdDciFolosite;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // dezactivez la inceput butoanele de stergere sau editare
        editBTN.setDisable(true);
        stergeBTN.setDisable(true);

        // ascund eticheta pentru mesaje
        msgLB.setText("");
        msgLB.setTextFill(Color.RED);

        // configurare coloane
        denDciTCL.setCellValueFactory(new PropertyValueFactory<Dci, String>("denDci"));
        grupaAtcTBC.setCellValueFactory(new PropertyValueFactory<Dci, String>("grupaAtc"));
        codDciTBC.setCellValueFactory(new PropertyValueFactory<Dci, String>("codDci"));
        observatiiTBC.setCellValueFactory(new PropertyValueFactory<Dci, String>("observatii"));

        // pentru selectie simpla din TableView
        tabelTBV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // incarca din BD toate DCI si creaza un ObservableList pentru afisarea in TableView
        try {
            loadDci();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        // incarca lista id_nomenclator folosite
        listaIdDciFolosite = new ArrayList<>();
        try {
            listaIdDciFolosite = construiesteListaIdDci();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Incarca o lista de intrari Dci din DB
     */
    public void loadDci() throws SQLException {

        ObservableList<Dci> dciObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(DCI_SELECT_ALL);

            while(resultSet.next()) {
                SimpleLongProperty idDci = new SimpleLongProperty(resultSet.getLong("id_dci"));
                SimpleStringProperty codDci = new SimpleStringProperty(resultSet.getString("cod_dci"));
                SimpleStringProperty denDci = new SimpleStringProperty(resultSet.getString("den_dci"));
                SimpleStringProperty grupaAtc = new SimpleStringProperty(resultSet.getString("grupa_atc"));
                SimpleStringProperty observatii = new SimpleStringProperty(resultSet.getString("observatii"));

                Dci newDci = new Dci(idDci, codDci, denDci, grupaAtc, observatii);
                dciObservableList.add(newDci);
            }

            // dorim sortarea listei dupa denumirea DCI in ordine alfabetica
//            SortedList<Dci> sortedList = new SortedList<>(dciObservableList, Comparator.comparing(Dci::getDenDci));
            tabelTBV.getItems().addAll(dciObservableList);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(statement != null) {
                statement.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Buton pentru adaugarea unei noi intrari in nomenclatorul DCI
     */
    public void addNewDci(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "AddDci.fxml", "Adaugare intrare DCI");
    }

    /**
     * Buton pt. stergerea intrarilor din tabela nomenclatorului DCI
     */
    public void butonStergeDciDinNomenclator(ActionEvent event) throws SQLException {
        ObservableList<Dci> toateIntrarileDci;
        toateIntrarileDci = tabelTBV.getItems();

        if (listaIdDciFolosite.contains(tabelTBV.getSelectionModel().getSelectedItem().getIdDci())) {
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

                Dci dciSters = tabelTBV.getSelectionModel().getSelectedItem();
                long idDciSters = dciSters.getIdDci();

                boolean isDeleted = dciSters.deleteDci(idDciSters);
                if (!isDeleted) {
                    msgLB.setText("Nu a fost stearsa nicio intrare");
                } else {
                    System.out.println("Intrarea cu nr " + idDciSters + " a fost stearsa din BD");
                    toateIntrarileDci.remove(dciSters);
                }
            }
        }
    }

    /**
     * Returneaza o lista de id_dci distincte din tabela nomenclator
     * folosita pentru a decide daca o intrare dci poate fi stearsa
     */
    private List<Long> construiesteListaIdDci() throws SQLException {
        List<Long> lista = new ArrayList<>();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ID_DCI_FOLOSITE);

            while (resultSet.next()) {
                lista.add(resultSet.getLong("id_dci"));
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
     * Buton pt. editarea intrarilor din tabela nomenclatorului DCI
     */
    public void butonEditareDciDinNomenclator(ActionEvent event) throws IOException {

        SceneChanger sc = new SceneChanger();
        Dci dciSelectat = this.tabelTBV.getSelectionModel().getSelectedItem();

        AddDciController newAddDciController = new AddDciController();

        // asta ne permite sa mergem la AddDciController si sa trimitem intrarea selectata din TableView
        sc.changeScenes(event, "AddDci.fxml", "Editare detalii DCI", dciSelectat, newAddDciController);
    }

    /**
     * Buton intoarcere in meniul principal
     */
    public void butonBackToMain(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "MainScene.fxml", "FARMA 10");
    }

    /**
     * Daca o intrare din tabel a fost selectata, activez butoanele de editare si stergere
     */
    public void dciSelectat() {
        editBTN.setDisable(false);
        stergeBTN.setDisable(false);
    }
}
