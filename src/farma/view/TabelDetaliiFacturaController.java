package farma.view;

import farma.model.DocItem;
import farma.util.DBConstants;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class TabelDetaliiFacturaController implements Initializable, ControllerClass, DBConstants {

    @FXML private Label tipFacturaLB;
    @FXML private Label detaliiClientLB;
    @FXML private Label detaliiContabLB;
    @FXML private TableView<DocItem> tabProduseTV;
    @FXML private TableColumn<DocItem, String> denumireTC;
    @FXML private TableColumn<DocItem, Integer> cantitateTC;
    @FXML private TableColumn<DocItem, String> lotTC;
    @FXML private TableColumn<DocItem, LocalDate> bbdTC;
    @FXML private TableColumn<DocItem, Integer> procTvatc;
    @FXML private TableColumn<DocItem, Float> pretTC;
    @FXML private TableColumn<DocItem, Float> pretVanzareTC;
    @FXML private TableColumn<DocItem, String> producatorTC;
    @FXML private TableColumn<DocItem, String> concentratieTC;
    @FXML private TableColumn<DocItem, String> formaTC;
    @FXML private TableColumn<DocItem, String> dciTC;
    @FXML private TableColumn<DocItem, String> ambalareTC;

    private Long nrDocument;
    private LocalDate dataDocument;
    private Long idOperatie;
    private String numeOperator;
    private String numePartener;
    private String adresaPartener;
    private String telefonPartener;
    private String emailPartener;
    private String cuiPartener;
    private String contPartener;

    ObservableList<DocItem> items = FXCollections.observableArrayList();


    @Override
    public void preloadObject(Object obiectTransfer) {
        Long idDocument = (Long) obiectTransfer;
        System.out.println(idDocument);

        // interogare detalii factura
        try {
            interogareBDfactura(idDocument);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        // setare detalii factura
        setDetaliiClient();

        // interogare date pt. tabel
        try {
            if (idOperatie == 1) {
                items = interogareBDTabel(idDocument, DETALII_F_INTRARI);
            } else {
                items = interogareBDTabel(idDocument, DETALII_F_IESIRI);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // setare date tabel
        tabProduseTV.getItems().addAll(items);

        // setare cifre factura
        setDetaliiCont();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // afisare coloane Table View
        denumireTC.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        cantitateTC.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        lotTC.setCellValueFactory(new PropertyValueFactory<>("lot"));
        bbdTC.setCellValueFactory(new PropertyValueFactory<>("bbd"));
        procTvatc.setCellValueFactory(new PropertyValueFactory<>("procTva"));
        pretTC.setCellValueFactory(new PropertyValueFactory<>("pretAchizitie"));
        pretVanzareTC.setCellValueFactory(new PropertyValueFactory<>("pretVanzare"));
        producatorTC.setCellValueFactory(new PropertyValueFactory<>("producator"));
        concentratieTC.setCellValueFactory(new PropertyValueFactory<>("concentratie"));
        formaTC.setCellValueFactory(new PropertyValueFactory<>("forma"));
        dciTC.setCellValueFactory(new PropertyValueFactory<>("dci"));
        ambalareTC.setCellValueFactory(new PropertyValueFactory<>("ambalare"));

        tabProduseTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void setDetaliiClient() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        StringBuilder sbClient = new StringBuilder();
        StringBuilder sbFactura = new StringBuilder();
        if (idOperatie == 1) {
            sbFactura.append("Factura de intrare\n\n");
            sbClient.append("Detalii furnizor:\n");
        } else {
            sbFactura.append("Factura de iesire\n\n");
            sbClient.append("Detalii client:\n");
        }
        sbClient.append("Denumire: ").append(numePartener).append("\n");
        sbClient.append("Adresa: ").append(adresaPartener).append("\n");
        sbClient.append("Telefon: ").append(telefonPartener).append("\n");
        sbClient.append("Email: ").append(emailPartener).append("\n");
        sbClient.append("CUI: ").append(cuiPartener).append("\n");
        sbClient.append("Cont: ").append(contPartener);
        sbFactura.append("Factura nr. ").append(nrDocument).append("\n");
        sbFactura.append("Data emiterii: ").append(dataDocument.format(dtf)).append("\n");
        sbFactura.append("Operator: ").append(numeOperator);

        detaliiClientLB.setText(sbClient.toString());
        tipFacturaLB.setText(sbFactura.toString());
    }

    private void interogareBDfactura(Long idDocument) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(DETALII_F_DOCUMENT);
            ps.setLong(1, idDocument);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                nrDocument = resultSet.getLong("nr_document");
                dataDocument = resultSet.getDate("data_document").toLocalDate();
                idOperatie = resultSet.getLong("operatie");
                numeOperator = resultSet.getString("nume_operator");
                numePartener = resultSet.getString("nume_client");
                adresaPartener = resultSet.getString("adresa_client");
                telefonPartener = resultSet.getString("telefon_client");
                emailPartener = resultSet.getString("email_client");
                cuiPartener = resultSet.getString("cui_client");
                contPartener = resultSet.getString("cont_client");
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(ps != null) {
                ps.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
    }

    private ObservableList<DocItem> interogareBDTabel(Long idDocument, String query) throws SQLException {
        ObservableList<DocItem> lista = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            ps = conn.prepareStatement(query);
            ps.setLong(1, idDocument);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                SimpleLongProperty idDoc = new SimpleLongProperty(idDocument);
                SimpleFloatProperty pretAchizitie = new SimpleFloatProperty(roundFloat(resultSet.getFloat("pret_achizitie"), 2));
                SimpleObjectProperty<LocalDate> bbd = new SimpleObjectProperty<LocalDate>(resultSet.getDate("bbd").toLocalDate());
                SimpleStringProperty lot = new SimpleStringProperty(resultSet.getString("lot"));
                SimpleFloatProperty pretVanzare = new SimpleFloatProperty(roundFloat(resultSet.getFloat("pret_vanzare"), 2));
                SimpleIntegerProperty cantitate = new SimpleIntegerProperty(resultSet.getInt("cantitate"));
                SimpleStringProperty denumire = new SimpleStringProperty(resultSet.getString("denumire_produs"));
                SimpleStringProperty concentratie = new SimpleStringProperty(resultSet.getString("concentratie"));
                SimpleStringProperty ambalare = new SimpleStringProperty(resultSet.getString("ambalare"));
                SimpleStringProperty producator = new SimpleStringProperty(resultSet.getString("producator"));
                SimpleStringProperty forma = new SimpleStringProperty(resultSet.getString("forma"));
                SimpleIntegerProperty procTva = new SimpleIntegerProperty(resultSet.getInt("procent_tva"));
                SimpleStringProperty dci = new SimpleStringProperty(resultSet.getString("dci"));

                DocItem docItem = new DocItem(idDoc, pretAchizitie, bbd, lot, cantitate, pretVanzare,
                        denumire, concentratie, ambalare, producator, forma, procTva, dci);

                lista.add(docItem);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(ps != null) {
                ps.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
        return lista;
    }


    private void setDetaliiCont() {
        float pretFTva = 0;
        float valTva = 0;

        for (DocItem item : items) {
            if (idOperatie == 1) {
                pretFTva += roundFloat(item.getPretAchizitie(), 2) * item.getCantitate();
            } else {
                pretFTva += roundFloat(item.getPretVanzare(), 2) * item.getCantitate();
            }
            valTva += roundFloat ((((float)item.getProcTva() / 100) * pretFTva), 2);
        }

        float valDoc = roundFloat(pretFTva + valTva, 2);
        StringBuilder sb = new StringBuilder();
        sb.append("Valoare totala factura = ").append(valDoc).append(" lei\n");
        sb.append("Valoare fara TVA = ").append(pretFTva).append(" lei\n");
        sb.append("Valoare TVA = ").append(valTva).append(" lei");
        detaliiContabLB.setText(sb.toString());
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

    public void butonIntoarcereInListaFacturi(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelFacturi.fxml", "Centralizator facturi");
    }
}
