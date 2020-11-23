package farma.view;


import farma.model.Stoc;
import farma.util.DBConstants;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddIesireController implements Initializable, ControllerStocTransfer, DBConstants {

    @FXML private Label titluLB;
    @FXML private ComboBox<Stoc> stocCB;
    @FXML private TextField cantitateTF;
    @FXML private Label concentratieLB;
    @FXML private Label formaLB;
    @FXML private Label producatorLB;
    @FXML private Label lotLB;
    @FXML private Label bbdLB;
    @FXML private Label cantDisponibilaLB;
    @FXML private Label pretVanzareLB;
    @FXML private Label pretImpusLB;
    @FXML private Label tvaLB;
    @FXML private Label codBareLB;
    @FXML private Label msgLB;
    @FXML private Button adaugareBTN;
    @FXML private Button anulareBTN;

    private ObservableList<Stoc> stocCurent = FXCollections.observableArrayList();
    private ObservableList<Stoc> valoriPreluateDinTabel = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // incarcare valori din stoc in Combo Box
        try {
            stocCurent = getAllStoc();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stocCB.setItems(stocCurent);

        // la selectia unui produs din stocul curent se vor afisa informatii despre acel produs in etichete
        EventHandler<ActionEvent> afisareDetaliiProdus = event -> {
              cantitateTF.setText(String.valueOf(stocCB.getValue().getCantitateDisp()));
              concentratieLB.setText("Concentratie: " + stocCB.getValue().getConcentratie());
              formaLB.setText("Forma farmaceutica: " + stocCB.getValue().getForma());
              producatorLB.setText("Producator: " + stocCB.getValue().getProducator());
              if (stocCB.getValue().getLot() == null) {
                  lotLB.setText("Lot: (necompletat)" );
              } else {
                  lotLB.setText("Lot: " + stocCB.getValue().getLot());
              }
              bbdLB.setText("Data expirarii: " + stocCB.getValue().getBbd());
              cantDisponibilaLB.setText("Cantitate disponibila: " + stocCB.getValue().getCantitateDisp() + " unitati");
              pretVanzareLB.setText("Pret: " + stocCB.getValue().getPretVanzare());
              if (stocCB.getValue().getPretImpus() == 0.0F) {
                  pretImpusLB.setText("Pret maximal (MS): fara impunere de pret");
              } else {
                  pretImpusLB.setText("Pret maximal (MS): " + stocCB.getValue().getPretImpus());
              }
              tvaLB.setText("TVA: " + stocCB.getValue().getTva() + "%");
              codBareLB.setText("Cod de bare: " + stocCB.getValue().getCodBare());
        };
        stocCB.setOnAction(afisareDetaliiProdus);
    }

    public void butonOk(ActionEvent event) throws IOException, SQLException {
        try {
            Stoc stocPentruTransfer = preluareObiectStocDinView();
            if (stocPentruTransfer == null) {
                throw new IllegalArgumentException("Datele pentru inserarea in factura sunt eronate");
            }
            valoriPreluateDinTabel.add(stocPentruTransfer);
            boolean update = stocPentruTransfer.actualizareStoc(stocPentruTransfer.getCantitateDisp(), SCADE_DIN_STOC);

            if (update) {
                SceneChanger sc = new SceneChanger();
                FacturaIesireController facturaIesireController = new FacturaIesireController();
                sc.changeScenesStoc(event, "FacturaIesire.fxml", "Emitere factura",
                        valoriPreluateDinTabel, facturaIesireController);
            } else {
                throw new IllegalArgumentException("Operatia de actualizare a stocului a esuat");
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public void butonCancel(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        FacturaIesireController facturaIesireController = new FacturaIesireController();
        sc.changeScenesStoc(event, "FacturaIesire.fxml", "Emitere factura",
                valoriPreluateDinTabel, facturaIesireController);
    }

    /**
     * preia informatiile din view pentru a fi adaugate pe factura
     * @return intrare pentru a fi introdusa in lista de produse de pe factura
     */
    private Stoc preluareObiectStocDinView() {
        try {
            if (!valideazaCB()) {
                throw  new IllegalArgumentException("Selectati o intrare din stoc");
            }
            if (!valideazaCantitate()) {
                throw  new IllegalArgumentException("Cantitatea introdusa este eronata. Cantitatea maxim admisa " +
                        "este de " + stocCB.getValue().getCantitateDisp() + " unitati de stoc");
            }
            SimpleLongProperty idStoc = new SimpleLongProperty(stocCB.getValue().getIdStoc());
            SimpleLongProperty idIntrare = new SimpleLongProperty(stocCB.getValue().getIdIntrare());
            SimpleFloatProperty pretVanzare = new SimpleFloatProperty(stocCB.getValue().getPretVanzare());
            SimpleIntegerProperty cantitate = new SimpleIntegerProperty(Integer.parseInt(cantitateTF.getText()));
            SimpleStringProperty lot = new SimpleStringProperty(stocCB.getValue().getLot());
            SimpleObjectProperty<LocalDate> bbd = new SimpleObjectProperty<>(stocCB.getValue().getBbd());
            SimpleStringProperty denumire = new SimpleStringProperty(stocCB.getValue().getDenumire());
            SimpleLongProperty codBare = new SimpleLongProperty(stocCB.getValue().getCodBare());
            SimpleStringProperty concentratie = new SimpleStringProperty(stocCB.getValue().getConcentratie());
            SimpleFloatProperty adaos = new SimpleFloatProperty(stocCB.getValue().getAdaos());
            SimpleFloatProperty pretImpus = new SimpleFloatProperty(stocCB.getValue().getPretImpus());
            SimpleStringProperty producator = new SimpleStringProperty(stocCB.getValue().getProducator());
            SimpleStringProperty forma = new SimpleStringProperty(stocCB.getValue().getForma());
            SimpleIntegerProperty tva = new SimpleIntegerProperty(stocCB.getValue().getTva());
            float valTva = pretVanzare.getValue() * tva.getValue() / 100;
            SimpleFloatProperty valoareTva = new SimpleFloatProperty(valTva);

            msgLB.setText("");
            return new Stoc(idStoc, idIntrare, pretVanzare, cantitate, lot, bbd, denumire, codBare,
                    concentratie, adaos, pretImpus, producator, forma, tva, valoareTva);

        } catch (IllegalArgumentException e) {
            msgLB.setText(e.getMessage());
            return null;
        }
    }

    private boolean valideazaCantitate() {
        int cantitateInStoc = stocCB.getValue().getCantitateDisp();
        if (cantitateTF.getText().isEmpty()) {
            return false;
        }
        try {
            return Integer.parseUnsignedInt(cantitateTF.getText()) <= cantitateInStoc;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean valideazaCB() {
        return stocCB.getValue() != null;
    }

    /**
     * Metoda aduna informatiile necesare preluarii produselor din stoc pentru emiterea facturii
     * @return = valorile de stoc folosite pentru preluare in tabelul facturii de iesire
     */
    private ObservableList<Stoc> getAllStoc() throws SQLException {

        ObservableList<Stoc> stocCurentDinBd = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_STOC_EXT);

            while (resultSet.next()) {
                SimpleLongProperty idStoc = new SimpleLongProperty(resultSet.getLong("id_stoc"));
                SimpleLongProperty idIntrare = new SimpleLongProperty(resultSet.getLong("id_intrare"));
                SimpleFloatProperty pretVanzare = new SimpleFloatProperty(resultSet.getFloat("pret"));
                SimpleIntegerProperty cantitateDisp = new SimpleIntegerProperty(resultSet.getInt("cantitate"));
                SimpleStringProperty lot = new SimpleStringProperty(resultSet.getString("lot"));
                LocalDate dateFromDb = resultSet.getDate("bbd").toLocalDate();
                SimpleObjectProperty<LocalDate> bbd = new SimpleObjectProperty<>(dateFromDb);
                SimpleStringProperty denumire = new SimpleStringProperty(resultSet.getString("denumire_prod"));
                SimpleLongProperty codBare = new SimpleLongProperty(resultSet.getLong("cod_bare"));
                SimpleStringProperty concentratie = new SimpleStringProperty(resultSet.getString("concentratie"));
                SimpleFloatProperty adaos = new SimpleFloatProperty(resultSet.getFloat("adaos"));
                SimpleFloatProperty pretImpus = new SimpleFloatProperty(resultSet.getFloat("pret_impus"));
                SimpleStringProperty producator = new SimpleStringProperty(resultSet.getString("nume_producator"));
                SimpleStringProperty forma = new SimpleStringProperty(resultSet.getString("nume_forma"));
                SimpleIntegerProperty tva = new SimpleIntegerProperty(resultSet.getInt("procent_tva"));
                SimpleFloatProperty valoareTva = new SimpleFloatProperty(0);

                Stoc stoc = new Stoc(idStoc, idIntrare, pretVanzare, cantitateDisp, lot, bbd, denumire,
                        codBare, concentratie, adaos, pretImpus, producator, forma, tva, valoareTva);

                // calcul valoare TVA
                float vTva = stoc.getPretVanzare() * stoc.getTva() / 100;
                vTva = roundFloat(vTva, 2);
                stoc.setValoareTva(vTva);

                stocCurentDinBd.add(stoc);
            }

        } catch (SQLException e) {
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

        return stocCurentDinBd;
    }

    @Override
    public void preloadList(ObservableList<Stoc> stocTransfer) {
        if (stocTransfer.size() != 0) {
            valoriPreluateDinTabel = stocTransfer;
        }
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
