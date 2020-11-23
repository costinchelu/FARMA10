package farma.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import farma.model.*;
import farma.util.DBConstants;
import farma.util.UserLogged;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FacturaIntrareController implements Initializable, DBConstants, ControllerListTransfer {

    @FXML private ComboBox<Partener> numePartenerCB;
    @FXML private Label adresaPartenerLB;
    @FXML private Label telefonPartenerLB;
    @FXML private Label cuiPartenerLB;
    @FXML private Label contPartenerLB;
    @FXML private TextField nrDocumentTF;
    @FXML private DatePicker dataEmitereDP;
    @FXML private DatePicker dataOperareDP;
    @FXML private TextField termenPlataTF;
    @FXML private TextField obsFacturaTF;
    @FXML private ComboBox<Operatori> operatorCB;
    @FXML private TableView<Intrari> intrariFacturaTV;
    @FXML private TableColumn<Intrari, String> denumireProdusTC;
    @FXML private TableColumn<Intrari, Integer> cantitateTC;
    @FXML private TableColumn<Intrari, Float> pretAchizitieTC;
    @FXML private TableColumn<Intrari, Float> pretVanzareTC;
    @FXML private TableColumn<Intrari, Float> pretImpusTC;
    @FXML private TableColumn<Intrari, Integer> procentTvaTC;
    @FXML private TableColumn<Intrari, Float> valoareTvaTC;
    @FXML private TableColumn<Intrari, LocalDate> bbdTC;
    @FXML private TableColumn<Intrari, String> lotTC;
    @FXML private TableColumn<Intrari, Float> discountTC;
    @FXML private TableColumn<Intrari, String> observatiiProdusTC;
    @FXML private Label operatorLB;
    @FXML private Label msgLB;
    @FXML private Label adaosValoricLB;
    @FXML private Label valFTvaLB;
    @FXML private Label valTvaLB;
    @FXML private Label valDocLB;
    @FXML private Button stergeIntrareBTN;
    @FXML private Button modificaIntrareBTN;
    @FXML private Button adaugaIntrareBTN;
    @FXML private Button okBTN;
    @FXML private Button anulareBTN;

    ObservableList<Partener> parteneriObservableList = FXCollections.observableArrayList();
    ObservableList<Operatori> operatoriObservableList = FXCollections.observableArrayList();
    ObservableList<Intrari> intraripentruFactura = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // dezactivare butoane stergere si editare
        stergeIntrareBTN.setDisable(true);
        modificaIntrareBTN.setDisable(true);

        // configurare Text Field Termen de plata - valoare default 120
        termenPlataTF.setText("120");

        // configurare Combo Box - valori default
        try {
            parteneriObservableList = getAllParteneri();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        numePartenerCB.setItems(parteneriObservableList);
        numePartenerCB.getSelectionModel().select(2);
        // TODO:  optional - selectare depozit dupa index

        try {
            operatoriObservableList = getAllOperatori();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        operatorCB.setItems(operatoriObservableList);
        operatorCB.getSelectionModel().select(0);

        // configurare Date Picker - valori LocalDate.now()
        dataEmitereDP.setValue(LocalDate.now());
        dataOperareDP.setValue(LocalDate.now());
        // utilizat pentru restrictionarea perioadei selectabile din Date Picker (validare imput data)
        LocalDate aziMinusOLuna = LocalDate.now().minus(Period.ofMonths(1));
        restrictDatePicker(dataEmitereDP, aziMinusOLuna, LocalDate.now());
        restrictDatePicker(dataOperareDP, aziMinusOLuna, LocalDate.now());

        // la selectia unui partener din Combo box, se vor afisa detaliile sale in etichete
        EventHandler<ActionEvent> afisareDetaliiPartener = event -> {
            adresaPartenerLB.setText("Adresa: " + numePartenerCB.getValue().getAdresa());
            telefonPartenerLB.setText("Telefon: " + numePartenerCB.getValue().getTelefon() +
                     "    email: " + numePartenerCB.getValue().getEmail());
            cuiPartenerLB.setText("CUI: " + numePartenerCB.getValue().getCui());
            contPartenerLB.setText("Cont: " + numePartenerCB.getValue().getCont());
        };
        numePartenerCB.setOnAction(afisareDetaliiPartener);

        // configurare Table View
        denumireProdusTC.setCellValueFactory(new PropertyValueFactory<>("denumireProdus"));
        cantitateTC.setCellValueFactory(new PropertyValueFactory<>("cantitateIntrata"));
        pretAchizitieTC.setCellValueFactory(new PropertyValueFactory<>("pretAchizitie"));
        pretVanzareTC.setCellValueFactory(new PropertyValueFactory<>("pretPentruVanzare"));
        pretImpusTC.setCellValueFactory(new PropertyValueFactory<>("pretImpus"));
        procentTvaTC.setCellValueFactory(new PropertyValueFactory<>("procentTva"));
        valoareTvaTC.setCellValueFactory(new PropertyValueFactory<>("valoareTva"));
        bbdTC.setCellValueFactory(new PropertyValueFactory<>("bbdProdus"));
        lotTC.setCellValueFactory(new PropertyValueFactory<>("lotProdus"));
        discountTC.setCellValueFactory(new PropertyValueFactory<>("discountProdus"));
        observatiiProdusTC.setCellValueFactory(new PropertyValueFactory<>("observatiiProdus"));

        intrariFacturaTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // dezactivare controale operator:
        operatorCB.setVisible(false);
        operatorLB.setVisible(false);
    }

    /**
     * Actiune pentru inserarea facturii precum si a intrarilor si stocului
     */
    public void butonOKIntroducereaFacturii(ActionEvent event) throws IOException, SQLException {

        try {
            if (intrariFacturaTV.getItems().size() == 0) {
                throw new IllegalArgumentException("Factura nu contine produse de intrare");
            }

            Documente antetFactura = creazaAntetFactura();

            long ultimulIdDocument;
            if (antetFactura != null) {

                printFacturaIntrareToPdf();

                ultimulIdDocument = antetFactura.inserareDocumentInBd(1);
                System.out.println("Ultimul id document inserat este " + ultimulIdDocument);

                List<Intrari> intrariPentruInserareInBd =
                        copiazaValoriIntrariDinTabel(intrariFacturaTV.getItems(), ultimulIdDocument);

                for (Intrari intrare : intrariPentruInserareInBd) {
                    intrare.insereazaIntrareSiStocInBd();
                }

                butonBackToMain(event);

            } else {
                throw new IllegalArgumentException("Factura nu este completata corect. Va rugam verificati.");
            }

        } catch (IllegalArgumentException | DocumentException e) {
            msgLB.setText(e.getMessage());
        }
    }


    private List<Intrari> copiazaValoriIntrariDinTabel(ObservableList<Intrari> items, long idDocumentPrimit) {
        List<Intrari> rezultat = new ArrayList<>();

        for (Intrari i : items) {
            SimpleLongProperty idIntrare = new SimpleLongProperty(1);
            SimpleLongProperty idNomenclator = new SimpleLongProperty(i.getIdNomenclator());
            SimpleLongProperty idDocument = new SimpleLongProperty(idDocumentPrimit);
            SimpleStringProperty lotProdus = new SimpleStringProperty(i.getLotProdus());
            SimpleObjectProperty<LocalDate> bbdProdus = new SimpleObjectProperty<>(i.getBbdProdus());
            SimpleIntegerProperty cantitateIntrata = new SimpleIntegerProperty(i.getCantitateIntrata());
            SimpleFloatProperty pretAchizitie = new SimpleFloatProperty(i.getPretAchizitie());
            SimpleFloatProperty discountProdus = new SimpleFloatProperty(i.getDiscountProdus());
            SimpleStringProperty observatiiProdus = new SimpleStringProperty(i.getObservatiiProdus());
            SimpleStringProperty denumireProdus = new SimpleStringProperty(i.getDenumireProdus());
            SimpleFloatProperty pretPentruVanzare = new SimpleFloatProperty(i.getPretPentruVanzare());
            SimpleFloatProperty pretImpus = new SimpleFloatProperty(i.getPretImpus());
            SimpleFloatProperty adaosProdus = new SimpleFloatProperty(i.getAdaosProdus());
            SimpleIntegerProperty procentTva = new SimpleIntegerProperty(i.getProcentTva());
            SimpleFloatProperty valoareTva = new SimpleFloatProperty(i.getValoareTva());

            Intrari newIntrare = new Intrari(idIntrare, idNomenclator, idDocument, lotProdus, bbdProdus, cantitateIntrata,
                    pretAchizitie, discountProdus, observatiiProdus, denumireProdus, pretPentruVanzare,
                    pretImpus, adaosProdus, procentTva, valoareTva);
            rezultat.add(newIntrare);
        }
        return rezultat;
    }

    /**
     * Buton pentru stergerea intrarilor din tabela produselor de intrare
     */
    public void butonStergereProdusDinListaFacturii(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmarea stergerii");
        alert.setHeaderText("Confirmare pentru stergerea intrarii");
        alert.setContentText("Sunteti sigur ca doriti stergerea intrarii de pe factura?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ObservableList<Intrari> intrariDinTabel;
            intrariDinTabel = intrariFacturaTV.getItems();

            Intrari produsDeSters = intrariFacturaTV.getSelectionModel().getSelectedItem();
            intrariDinTabel.remove(produsDeSters);

            intraripentruFactura = intrariFacturaTV.getItems();

            actualizeazaCifreFactura(intraripentruFactura);
        }
    }

    /**
     * Buton pentru adaugarea unui produs pe factura de intrare
     * trimit catre scena de adaugare, lista provenita de la TableView pentru a fi
     * pastrata si reinnoita de catre scena de adaugare
     */
    public void butonAdaugareIntrare(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        ObservableList<Intrari> listaDeTransferat = intrariFacturaTV.getItems();

        AddIntrareController newAddIntrareController = new AddIntrareController();

        sc.changeScenesIntrari(event, "AddIntrare.fxml", "Adauga produs pe factura de intrare",
                listaDeTransferat, newAddIntrareController);
    }

    /**
     * Buton pentru editarea unui produs de pe factura de intrare
     * trimit catre scena de adaugare produs lista provenita de la Tableview pentru a fi
     * pastrata si reinnoita de catre scena de adaugare
     * trimit si produsul selectat pentru a fi editat
     */
    public void butonEditareIntrare(ActionEvent event) throws IOException {
        try {
            SceneChanger sc = new SceneChanger();
            ObservableList<Intrari> listaDeTransferat = intrariFacturaTV.getItems();
            Intrari intrareDeTransferat = intrariFacturaTV.getSelectionModel().getSelectedItem();
            if (intrareDeTransferat == null) {
                throw new IllegalArgumentException("Produsul pentru editare nu a fost selectat");
            }

            AddIntrareController newAddIntrareController = new AddIntrareController();

            sc.changeScenesIntrari(event, "AddIntrare.fxml", "Editeaza produs de pe factura de intrare",
                    intrareDeTransferat, listaDeTransferat, newAddIntrareController, newAddIntrareController);
        } catch (IllegalArgumentException e) {
            msgLB.setText(e.getMessage());
        }
    }

    /**
     *
     * @return = un obiect Document verificat pentru a fi folosit la inserare in BD
     */
    private Documente creazaAntetFactura() {
        Documente document = null;
        try {
            if (!valideazaNrDocument()) {
                msgLB.setText("Numarul documentului este eronat");
            }
            if (!valideazaTermenPlata()) {
                msgLB.setText("Termenul de plata este eronat");
            }

            SimpleLongProperty finalNrDocument = new SimpleLongProperty(Long.parseLong(nrDocumentTF.getText()));
            SimpleObjectProperty<LocalDate> finalDataEmitere = new SimpleObjectProperty<LocalDate>(dataEmitereDP.getValue());
            SimpleObjectProperty<LocalDate> finalDataOperare = new SimpleObjectProperty<>(dataOperareDP.getValue());

            SimpleIntegerProperty finalTermenPlata;
            if (termenPlataTF.getText().trim().isEmpty()) {
                finalTermenPlata = new SimpleIntegerProperty(120);
            } else {
                finalTermenPlata = new SimpleIntegerProperty(Integer.parseInt(termenPlataTF.getText()));
            }

            SimpleLongProperty finalIdOperatie = new SimpleLongProperty(1);
            SimpleLongProperty finalIdPartener = new SimpleLongProperty(numePartenerCB.getSelectionModel().getSelectedItem().getIdPartener());
//            SimpleLongProperty finalIdOperator = new SimpleLongProperty(operatorCB.getSelectionModel().getSelectedItem().getIdOperator());
            SimpleLongProperty finalIdOperator = new SimpleLongProperty(UserLogged.readUser());

            SimpleStringProperty finalObservatii;
            if (obsFacturaTF.getText().trim().isEmpty()) {
                finalObservatii = new SimpleStringProperty("");
            } else {
                finalObservatii = new SimpleStringProperty(obsFacturaTF.getText());
            }

            document = new Documente(finalNrDocument, finalDataEmitere, finalDataOperare, finalTermenPlata,
                    finalIdOperatie, finalIdPartener, finalIdOperator, finalObservatii);

        }
        catch (Exception e) {
            msgLB.setText(e.getMessage());
        }
        return document;
    }

    /**
     * @param listTransfer lista transferata din AddIntrare
     *                     primeste lista si o copiaza in ObsList pentru tabel
     */
    @Override
    public void preloadList(ObservableList<Intrari> listTransfer) {
        this.intraripentruFactura = listTransfer;
        intrariFacturaTV.getItems().addAll(listTransfer);

        actualizeazaCifreFactura(listTransfer);
    }

    /**
     * actualizeaza etichetele cu datele numerice ale facturii
     * @param listIntrariDinTabel: lista cu  intrarile din tabel
     */
    private void actualizeazaCifreFactura(ObservableList<Intrari> listIntrariDinTabel) {
        float pretAchizitieTotal = 0;
        float adaosTotal = 0;
        float valTvaTotal = 0;
        for (Intrari intrare : listIntrariDinTabel) {
            adaosTotal += (intrare.getAdaosProdus() / 100) * intrare.getPretAchizitie() * intrare.getCantitateIntrata();
            pretAchizitieTotal += intrare.getPretAchizitie() * intrare.getCantitateIntrata();
            valTvaTotal += intrare.getValoareTva();
        }
        float valTotalaDocCuTva = valTvaTotal + pretAchizitieTotal;
        adaosValoricLB.setText(roundFloat(adaosTotal, 2) + " lei");
        valTvaLB.setText(roundFloat(valTvaTotal, 2) + " lei");
        valFTvaLB.setText(roundFloat(pretAchizitieTotal, 2) + " lei");
        valDocLB.setText(roundFloat(valTotalaDocCuTva, 2) + " lei");
    }

    /**
     * validarea campurilor detaliilor despre document
     * @return = validare campuri
     */
    private boolean valideazaNrDocument() {
        if (nrDocumentTF.getText().isEmpty()) {
            return false;
        }
        try {
            Long.parseUnsignedLong(nrDocumentTF.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean valideazaTermenPlata() {
        if (termenPlataTF.getText().isEmpty()) {
            return true;
        }
        try {
            Integer.parseUnsignedInt(termenPlataTF.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * select * from parteneri
     * returneaza un ObservableList cu toti partenerii pentru a fi folosit in combobox
     */
    public ObservableList<Partener> getAllParteneri() throws SQLException {
        ObservableList<Partener> parteneriObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_PARTENERI);

            while (resultSet.next()) {
                SimpleLongProperty idPartener = new SimpleLongProperty(resultSet.getLong("id_partener"));
                SimpleStringProperty numePartener = new SimpleStringProperty(resultSet.getString("nume_partener"));
                SimpleStringProperty adresaPartener = new SimpleStringProperty(resultSet.getString("adresa"));
                SimpleStringProperty telefonPartener = new SimpleStringProperty(resultSet.getString("telefon"));
                SimpleStringProperty emailPartener = new SimpleStringProperty(resultSet.getString("email"));
                SimpleStringProperty cuiPartener = new SimpleStringProperty(resultSet.getString("cui"));
                SimpleStringProperty contPartener = new SimpleStringProperty(resultSet.getString("cont"));

                Partener partener = new Partener(idPartener, numePartener, adresaPartener,
                                        telefonPartener, emailPartener, cuiPartener, contPartener);

                parteneriObservableList.add(partener);
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

        return parteneriObservableList;
    }

    /**
     * select * FROM operatori
     * returneaza un ObservableList cu toti operatorii pentru a fi folositi in combobox
     */
    public ObservableList<Operatori> getAllOperatori() throws SQLException {
        ObservableList<Operatori> operatoriObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_OPERATORI);

            while (resultSet.next()) {
                SimpleLongProperty idoperator = new SimpleLongProperty(resultSet.getLong("id_operator"));
                SimpleStringProperty numeOperator = new SimpleStringProperty(resultSet.getString("nume_operator"));
                SimpleStringProperty prenumeOperator = new SimpleStringProperty(resultSet.getString("prenume_operator"));

                Operatori operator = new Operatori(idoperator, numeOperator, prenumeOperator);
                operatoriObservableList.add(operator);
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

        return operatoriObservableList;
    }

    /**
     * Actiune pentru intoarcerea in meniul principal
     */
    public void butonBackToMain(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmarea anulare introducere factura");
        alert.setHeaderText("Confirmare pentru anularea introducerii facturii");
        alert.setContentText("Sunteti sigur ca doriti sa iesiti?\nOperati va anula crearea facturii");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "MainScene.fxml", "FARMA 10");
        }
    }

    /**
     * Daca o intrare din tabel a fost selectata, activez butoanele de editare si stergere
     */
    public void produsSelectat() {
        modificaIntrareBTN.setDisable(false);
        stergeIntrareBTN.setDisable(false);
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

    /**
     * printeaza factura de intrare intr-un fisier pdf
     */
    private void printFacturaIntrareToPdf() throws FileNotFoundException, DocumentException {

        var font14Bold = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        var font18Bold = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        var font12Normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        // creare fisier
        String numeFisier =
                "Intrare " +
                        numePartenerCB.getSelectionModel().getSelectedItem().getNumePartener() + " F" +
                        nrDocumentTF.getText() + ".pdf";

        Document doc = new Document(PageSize.A4, 1.5f, 1.5f, 30, 20);
        PdfWriter.getInstance(doc, new FileOutputStream("Facturi/" + numeFisier));
        doc.open();

        // antet
        var table1 = new PdfPTable(3);
        table1.setWidths(new int[]{4, 2, 2});

        PdfPCell cellFurnizor = new PdfPCell(new Phrase(
                numePartenerCB.getSelectionModel().getSelectedItem().getNumePartener() + "\n" +
                        "Adresa: " + numePartenerCB.getSelectionModel().getSelectedItem().getAdresa() + "\n" +
                        "Tel.: " + numePartenerCB.getSelectionModel().getSelectedItem().getTelefon() + "\n" +
                        "Email: " + numePartenerCB.getSelectionModel().getSelectedItem().getEmail() + "\n" +
                        "CUI: " + numePartenerCB.getSelectionModel().getSelectedItem().getCui() + "\n" +
                        "IBAN: " + numePartenerCB.getSelectionModel().getSelectedItem().getCont() + "\n"
        ));
        cellFurnizor.setBorderColor(new BaseColor(255, 255, 255));
        table1.addCell(cellFurnizor);
        PdfPCell cellEmpty = new PdfPCell(new Phrase("        "));
        cellEmpty.setBorderColor(new BaseColor(255, 255, 255));
        table1.addCell(cellEmpty);
        PdfPCell cellClient = new PdfPCell(new Phrase(
                "FARMA 10\nAdresa: depozit\nTelefon:\nCUI:\n"
        ));
        cellClient.setBorderColor(new BaseColor(255, 255, 255));
        table1.addCell(cellClient);

        var paragraphAntet = new Paragraph("", font14Bold);
        paragraphAntet.add(table1);

        // titlu
        var paragraphTitlu = new Paragraph("\n\nFactura nr. " + nrDocumentTF.getText() + "\nData: " +
                dataEmitereDP.getValue().toString() + "\n\n\n", font18Bold);
        paragraphTitlu.setAlignment(Element.ALIGN_CENTER);

        // tabel produse
        List<Intrari> listaFacturaCurenta = new ArrayList<>();
        listaFacturaCurenta = intrariFacturaTV.getItems();

        var table2 = new PdfPTable(7);
        table2.setWidths(new int[]{60, 30, 49, 46, 30, 40, 35});

        // cap tabel
        table2.addCell("Produs");
        table2.addCell("Cantit.");
        table2.addCell("BBD");
        table2.addCell("Lot");
        table2.addCell("TVA (%)");
        table2.addCell("Pret unitar (lei)");
        table2.addCell("Val. TVA (lei)\n  ");

        // continut tabel
        for(Intrari item : listaFacturaCurenta) {
            table2.addCell("  \n" + item.getDenumireProdus() + "\n  ");
            table2.addCell(String.valueOf(item.getCantitateIntrata()));
            table2.addCell(item.getBbdProdus().toString());
            table2.addCell(item.getLotProdus());
            table2.addCell(String.valueOf(item.getProcentTva()));
            table2.addCell(String.valueOf(item.getPretAchizitie()));
            table2.addCell(String.valueOf(roundFloat(item.getValoareTva(), 2)));
            table2.completeRow();
        }

        // inserare tabel in paragraf
        var paragrafProduse = new Paragraph("", font12Normal);
        paragrafProduse.add(table2);

        // tabel total1
        var table3 = new PdfPTable(3);
        table3.setWidths(new int[]{2, 1, 1});
        table3.addCell(" ");

        PdfPCell tab3cell2 = new PdfPCell(new Phrase(" \nValoare TVA:\n  \n  " + valTvaLB.getText() + "\n "));
        tab3cell2.setBorderColorLeft(new BaseColor(255,255,255));

        table3.addCell(tab3cell2);

        PdfPCell tab3cell3 = new PdfPCell(new Phrase(" \nPret fara TVA:\n  \n  " + valFTvaLB.getText() + "\n "));
        tab3cell3.setBorderColorLeft(new BaseColor(255,255,255));
        table3.addCell(tab3cell3);

        // tabel total2
        var table4 = new PdfPTable(2);
        table4.setWidths(new int[]{6, 4});

        table4.addCell(" ");
        PdfPCell tab4cell2 = new PdfPCell(new Phrase(" \nValoare totala:\n  \n  " + valDocLB.getText() + "\n \n "));
        tab4cell2.setBorderColorLeft(new BaseColor(255,255,255));
        table4.addCell(tab4cell2);

        // tabel final
        var table5 = new PdfPTable(3);
        table5.setWidths(new int[]{4, 8, 4});

        table5.addCell(" \n  \n  \n  \n  \n  L.S.\n  \n  ");
        table5.addCell(" \n  Livrat la: \n  \n  Observatii:\n");
        table5.addCell(" \n  Primire: \n");

        //var paragrafFinal = new Paragraph(" ", font14Bold);
        paragrafProduse.add(table3);
        paragrafProduse.add(table4);
        paragrafProduse.add(table5);


        // finalizare document
        doc.add(paragraphAntet);
        doc.add(paragraphTitlu);
        doc.add(paragrafProduse);
        //doc.add(paragrafFinal);
        doc.close();
    }
}




/*      Pentru testare functii

        SimpleLongProperty idIntrare = new SimpleLongProperty(1111);
        SimpleLongProperty idNomenclator = new SimpleLongProperty(1111);
        SimpleLongProperty idDocument = new SimpleLongProperty(1111);
        SimpleStringProperty lotProdus = new SimpleStringProperty("LOT");
        SimpleObjectProperty<LocalDate> bbdProdus = new SimpleObjectProperty<>(LocalDate.now());
        SimpleIntegerProperty cantitateIntrata = new SimpleIntegerProperty(1111);
        SimpleFloatProperty pretAchizitie = new SimpleFloatProperty(1111.11F);
        SimpleFloatProperty discountProdus = new SimpleFloatProperty(1.11F);
        SimpleStringProperty observatiiProdus = new SimpleStringProperty("OBS");
        SimpleStringProperty denumireProdus = new SimpleStringProperty("PRODUS");
        SimpleFloatProperty pretPentruVanzare = new SimpleFloatProperty(1111.11F);
        SimpleFloatProperty pretImpus = new SimpleFloatProperty(1111.11F);
        SimpleFloatProperty adaosProdus = new SimpleFloatProperty(1.11F);
        SimpleIntegerProperty procentTva = new SimpleIntegerProperty(1);
        SimpleFloatProperty valoareTva = new SimpleFloatProperty(1111.11F);

        Intrari i = new Intrari(idIntrare, idNomenclator, idDocument, lotProdus, bbdProdus, cantitateIntrata,
                pretAchizitie, discountProdus, observatiiProdus, denumireProdus, pretPentruVanzare,
                pretImpus, adaosProdus, procentTva, valoareTva);

        intraripentruFactura.add(i);
 */