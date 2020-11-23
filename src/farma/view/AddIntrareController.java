package farma.view;

import farma.model.Intrari;
import farma.model.Nomenclator;
import farma.util.DBConstants;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AddIntrareController implements Initializable, DBConstants, ControllerClass, ControllerListTransfer {

    @FXML private Label titluLB;
    @FXML private ComboBox<Nomenclator> denProdusCB;
    @FXML private TextField cantitateTB;
    @FXML private TextField pretAchizitieTB;
    @FXML private TextField pretVanzareTB;
    @FXML private TextField discountTB;
    @FXML private TextField lotTB;
    @FXML private DatePicker bbdDP;
    @FXML private TextField observatiiTB;
    @FXML private Label procTvaLB;
    @FXML private Label pretImpusLB;
    @FXML private Label msgLB;
    @FXML private Button okBTN;
    @FXML private Button cancelBTN;
    @FXML private Button calPretBTN;

    private ObservableList<Nomenclator> nomenclatorObservableList = FXCollections.observableArrayList();
    private ObservableList<Intrari> intrariPreluateDinTabel = FXCollections.observableArrayList();
    private Intrari intrarePreluataPtEditare;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // setare Combo Box
        try {
            nomenclatorObservableList = getAllNomenclator();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        denProdusCB.setItems(nomenclatorObservableList);
        denProdusCB.getSelectionModel().select(0);

        // setare Date Picker
        bbdDP.setValue(LocalDate.now());
        LocalDate maxBbd = LocalDate.now().plus(Period.ofYears(10));
        restrictDatePicker(bbdDP, LocalDate.now(), maxBbd);

        // la selectia unui produs din combobox se vor afisa informatii in etichete
        EventHandler<ActionEvent> afisareDetaliiProdus = event -> {
            int procTva = denProdusCB.getValue().getIdTva() == 1 ? 9 : 19;
            procTvaLB.setText("TVA: " + procTva + "%");
            if (denProdusCB.getValue().getPretImpus() == 0.0F) {
                pretImpusLB.setText("Pret maximal (MS): fara impunere de pret");
            } else {
                pretImpusLB.setText("Pret maximal (MS): " + denProdusCB.getValue().getPretImpus());
            }
        };
        denProdusCB.setOnAction(afisareDetaliiProdus);
    }

    /**
     * Metoda folosita la preluarea unui obiect pentru editare
     * @param obiectTransfer
     */
    @Override
    public void preloadObject(Object obiectTransfer) {
        intrarePreluataPtEditare = (Intrari) obiectTransfer;
        intrariPreluateDinTabel.remove(intrarePreluataPtEditare);

        titluLB.setText("Editarea intrarii de pe factura");
        okBTN.setText("OK");


        // setare denumire produs in Combo Box
        Iterator<Nomenclator> iterNomenclator = nomenclatorObservableList.iterator();
        int i = -1;
        while (iterNomenclator.hasNext()) {
            i++;
            if(iterNomenclator.next().getIdNomenclator() == intrarePreluataPtEditare.getIdNomenclator()) {
                break;
            }
        }
        denProdusCB.getSelectionModel().select(i);

        // setari text field-uri
        cantitateTB.setText(String.valueOf(intrarePreluataPtEditare.getCantitateIntrata()));
        pretAchizitieTB.setText(String.valueOf(intrarePreluataPtEditare.getPretAchizitie()));
        pretVanzareTB.setText(String.valueOf(intrarePreluataPtEditare.getPretPentruVanzare()));
        discountTB.setText(String.valueOf(intrarePreluataPtEditare.getDiscountProdus()));
        lotTB.setText(intrarePreluataPtEditare.getLotProdus());
        observatiiTB.setText(intrarePreluataPtEditare.getObservatiiProdus());

        // setare pentru Date Picker
        bbdDP.setValue(intrarePreluataPtEditare.getBbdProdus());
    }

    /**
     * Metoda folosita la preluarea unei pentru editare sau cazul adaugare ca prim produs (unde lista este goala)
     */
    @Override
    public void preloadList(ObservableList<Intrari> listTransfer) {
        if (listTransfer.size() != 0) {
            intrariPreluateDinTabel = listTransfer;
        }
    }

    public void butonOk(ActionEvent event) throws IOException {

        try {
            Intrari intrareCuleasa = preluareObiectIntrareDinView();
            if (intrareCuleasa == null) {
                throw new IllegalArgumentException("Datele pentru produsul de intrare sunt eronate");
            }
            intrariPreluateDinTabel.add(intrareCuleasa);

            SceneChanger sc = new SceneChanger();
            FacturaIntrareController facturaIntrareController = new FacturaIntrareController();
            sc.changeScenesIntrari(event, "FacturaIntrare.fxml", "Factura Intrare",
                    intrariPreluateDinTabel, facturaIntrareController);
        } catch (IllegalArgumentException e) {
               System.err.println(e.getMessage());
        }
    }

    public void butonCancel(ActionEvent event) throws IOException {

        if (intrarePreluataPtEditare != null) {
            intrariPreluateDinTabel.add(intrarePreluataPtEditare);
        }

        SceneChanger sc = new SceneChanger();
        FacturaIntrareController facturaIntrareController = new FacturaIntrareController();
        sc.changeScenesIntrari(event, "FacturaIntrare.fxml", "Factura Intrare",
                intrariPreluateDinTabel, facturaIntrareController);
    }

    /**
     * preia informatiile din view pentru a fi adaugate pe factura
     * @return intrare pentru a fi introdusa in lista de produse de pe factura
     */
    private Intrari preluareObiectIntrareDinView() {

        try {
            if (!valideazaCantitate()) {
                throw new IllegalArgumentException("Denumirea produsului trebuie sa aiba cel putin un caracter!");
            }
            if (!valideazaPretAchizitie()) {
                throw new IllegalArgumentException("Pretul de achizitie este incorect");
            }
            if (!valideazaPretVanzare()) {
                throw new IllegalArgumentException("Pretul de vanzare este incorect");
            }
            if (!valideazaDiscount()) {
                throw new IllegalArgumentException("Discount-ul este incorect");
            }
            if (!valideazaLot()) {
                throw new IllegalArgumentException("Lotul este incorect");
            }
            if (!valideazaObservatii()) {
                throw new IllegalArgumentException("Observatiile sunt completate incorect");
            }

            Nomenclator produsDinNomenclator = denProdusCB.getSelectionModel().getSelectedItem();

            SimpleLongProperty idNomenclator = new SimpleLongProperty(produsDinNomenclator.getIdNomenclator());

            SimpleStringProperty lotProdus;
            if(lotTB.getText().isEmpty()) {
                lotProdus = new SimpleStringProperty("");
            } else {
                lotProdus = new SimpleStringProperty(lotTB.getText());
            }

            SimpleObjectProperty<LocalDate> bbdProdus = new SimpleObjectProperty<>(bbdDP.getValue());
            SimpleIntegerProperty cantitateIntrata = new SimpleIntegerProperty(Integer.parseInt(cantitateTB.getText()));
            SimpleFloatProperty pretAchizitie = new SimpleFloatProperty(Float.parseFloat(pretAchizitieTB.getText()));

            SimpleFloatProperty discountProdus;
            if (discountTB.getText().isEmpty()) {
                discountProdus = new SimpleFloatProperty(0);
            } else {
                discountProdus = new SimpleFloatProperty(Float.parseFloat(discountTB.getText()));
            }

            SimpleStringProperty observatiiProdus;
            if (observatiiTB.getText().isEmpty()) {
                observatiiProdus = new SimpleStringProperty("");
            } else {
                observatiiProdus = new SimpleStringProperty(observatiiTB.getText());
            }

            SimpleStringProperty denumireProdus = new SimpleStringProperty(produsDinNomenclator.getDenumireProd());
            SimpleFloatProperty pretPentruVanzare = new SimpleFloatProperty(Float.parseFloat(pretVanzareTB.getText()));
            SimpleFloatProperty pretImpus = new SimpleFloatProperty(produsDinNomenclator.getPretImpus());
            SimpleFloatProperty adaosProdus = new SimpleFloatProperty(produsDinNomenclator.getAdaos());

            int procTva = produsDinNomenclator.getIdTva() == 1 ? 9 : 19;
            SimpleIntegerProperty procentTva = new SimpleIntegerProperty(procTva);

            float valTva = Float.parseFloat(pretAchizitieTB.getText()) * ((float)procTva / 100) * Integer.parseInt(cantitateTB.getText());
            SimpleFloatProperty valoareTva = new SimpleFloatProperty(valTva);

            msgLB.setText("");
            return new Intrari(idNomenclator, lotProdus, bbdProdus, cantitateIntrata,
                    pretAchizitie, discountProdus, observatiiProdus, denumireProdus, pretPentruVanzare,
                    pretImpus, adaosProdus, procentTva, valoareTva);

        } catch (IllegalArgumentException e) {
            msgLB.setText(e.getMessage());
            return null;
        }
    }

    private boolean valideazaObservatii() {
        if (observatiiTB.getText().length() > 120) {
            return false;
        }
        return true;
    }

    private boolean valideazaLot() {
        if (lotTB.getText().length() > 15) {
            return false;
        }
        return true;
    }

    private boolean valideazaDiscount() {
        if (discountTB.getText().isEmpty()) {
            return true;
        }
        try {
            if (Float.parseFloat(discountTB.getText()) > 99.99) {
                return false;
            }
            return Float.parseFloat(discountTB.getText()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean valideazaPretVanzare() {
        if (pretVanzareTB.getText().isEmpty()) {
            return false;
        }
        try {
            if (Float.parseFloat(pretVanzareTB.getText()) > 99999999.99) {
                return false;
            }
            return Float.parseFloat(pretVanzareTB.getText()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean valideazaPretAchizitie() {
        if (pretAchizitieTB.getText().isEmpty()) {
            return false;
        }
        try {
            if (Float.parseFloat(pretAchizitieTB.getText()) > 99999999.99) {
                return false;
            }
            return Float.parseFloat(pretAchizitieTB.getText()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private boolean valideazaCantitate() {
        if (cantitateTB.getText().isEmpty()) {
            return false;
        }
        try {
            return Integer.parseUnsignedInt(cantitateTB.getText()) <= 999999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Buton calcul pret vanzare
     */
    public void butoCalculPret(ActionEvent event) throws IOException {
        try {
            float calculPretV = (Float.parseFloat(pretAchizitieTB.getText()) * (1 + denProdusCB.getValue().getAdaos() / 100));
            float pretVanzare = roundFloat(calculPretV, 2);
            pretVanzareTB.setText(String.valueOf(pretVanzare));
            msgLB.setText("");
        } catch (NumberFormatException e) {
            msgLB.setText("Pretul de achizitie este gresit");
        }
    }

    /**
     * select * from nomenclator
     * returneaza un ObservableList cu toate produsele din nomenclator pentru a fi folosit in combobox
     */
    public ObservableList<Nomenclator> getAllNomenclator() throws SQLException {

        ObservableList<Nomenclator> nomObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_NOMENCLATOR);

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

                Nomenclator nomenclator = new Nomenclator(idNomenclator, denumireProd, codBare,
                        concentratie, adaos, ambalare, pretImpus, observatii, idForma, idTva, idDci, idProducator);
                nomObservableList.add(nomenclator);
            }

        }catch (Exception e) {
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

        return nomObservableList;
    }

    /**
     * metoda utilitara care permite setarea unor limite pentru alegerea datei din datePicker
     */
    public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
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
