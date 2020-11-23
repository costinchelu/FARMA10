package farma.view;

import farma.model.Partener;
import farma.util.DBConstants;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TabelParteneriController implements Initializable, DBConstants {

    @FXML private TableView<Partener> tabelParteneriTV;
    @FXML private TableColumn<Partener, String> numeTC;
    @FXML private TableColumn<Partener, String> adresaTC;
    @FXML private TableColumn<Partener, String> telefonTC;
    @FXML private TableColumn<Partener, String> emailTC;
    @FXML private TableColumn<Partener, String> cuiTC;
    @FXML private TableColumn<Partener, String> contTC;
    @FXML private Label msgLB;
    @FXML private Button stergeBTN;
    @FXML private Button editBTN;
    @FXML private Button adaugaBTN;
    @FXML private Button backBTN;
    List<Long> listaIdParteneriFolosite;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // dezactivez la inceput butoanele de stergere sau editare
        editBTN.setDisable(true);

//        stergeBTN.setVisible(false);
        stergeBTN.setDisable(true);

        // configurare coloane
        numeTC.setCellValueFactory(new PropertyValueFactory<>("numePartener"));
        adresaTC.setCellValueFactory(new PropertyValueFactory<>("adresa"));
        telefonTC.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        emailTC.setCellValueFactory(new PropertyValueFactory<>("email"));
        cuiTC.setCellValueFactory(new PropertyValueFactory<>("cui"));
        contTC.setCellValueFactory(new PropertyValueFactory<>("cont"));

        // selectie simpla
        tabelParteneriTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // incarca din BD parteneri in table view
        try {
            loadParteneri();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // incarca lista id_nomenclator folosite
        listaIdParteneriFolosite = new ArrayList<>();
        try {
            listaIdParteneriFolosite = construiesteListaIdParteneri();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadParteneri() throws SQLException {
        ObservableList<Partener> parteneriObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(PARTENERI_SELECT_ALL);

            while(resultSet.next()) {
                SimpleLongProperty idPartener = new SimpleLongProperty(resultSet.getLong("id_partener"));
                SimpleStringProperty numePartener = new SimpleStringProperty(resultSet.getString("nume_partener"));
                SimpleStringProperty adresa = new SimpleStringProperty(resultSet.getString("adresa"));
                SimpleStringProperty telefon = new SimpleStringProperty(resultSet.getString("telefon"));
                SimpleStringProperty email = new SimpleStringProperty(resultSet.getString("email"));
                SimpleStringProperty cui = new SimpleStringProperty(resultSet.getString("cui"));
                SimpleStringProperty cont = new SimpleStringProperty(resultSet.getString("cont"));

                Partener partener = new Partener(idPartener, numePartener, adresa, telefon, email, cui, cont);
                parteneriObservableList.add(partener);
            }
            tabelParteneriTV.getItems().addAll(parteneriObservableList);

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
     * Buton pentru adaugarea unei noi intrari in nomenclatorul parteneri
     */
    public void addNewPartener(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "AddPartener.fxml", "Adaugare partener in nomenclator");
    }

    /**
     * Buton pt. stergerea intrarilor din tabela nomenclatorului parteneri
     */
    public void butonStergePartDinNomenclator(ActionEvent event) throws SQLException {
        ObservableList<Partener> toateIntrarilePartener;
        toateIntrarilePartener = tabelParteneriTV.getItems();

        if (listaIdParteneriFolosite.contains(tabelParteneriTV.getSelectionModel().getSelectedItem().getIdPartener())) {
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
                Partener partDeSters = tabelParteneriTV.getSelectionModel().getSelectedItem();
                long idPartSters = partDeSters.getIdPartener();

                boolean isDeleted = partDeSters.stergePartener(idPartSters);
                if (!isDeleted) {
                    msgLB.setText("Nu a fost stearsa nicio intrare");
                } else {
                    System.out.println("Intrarea cu nr " + idPartSters + " a fost stearsa din BD");
                    toateIntrarilePartener.remove(partDeSters);
                }
            }
        }
    }

    /**
     * Returneaza o lista de id_parteneri distincte din tabela document
     * folosita pentru a decide daca o intrare nomenclator poate fi stearsa
     */
    private List<Long> construiesteListaIdParteneri() throws SQLException {
        List<Long> lista = new ArrayList<>();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ID_PARTENER_FOLOSITE);

            while (resultSet.next()) {
                lista.add(resultSet.getLong("id_partener"));
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
     * Buton pt. editarea intrarilor din tabela nomenclatorului parteneri
     */
    public void butonEditarePartDinNomenclator(ActionEvent event) throws IOException {

        SceneChanger sc = new SceneChanger();
        Partener partenerSelectat = this.tabelParteneriTV.getSelectionModel().getSelectedItem();

        AddPartenerController newAddPartenerController = new AddPartenerController();
        sc.changeScenes(event, "AddPartener.fxml", "Editare detalii partener", partenerSelectat, newAddPartenerController);
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
    public void partenerSelectat() {
        editBTN.setDisable(false);
        stergeBTN.setDisable(false);
    }
}
