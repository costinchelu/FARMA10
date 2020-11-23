package farma.view;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import farma.model.*;
import farma.model.Documente;
import farma.util.DBConstants;
import farma.util.UserLogged;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

public class FacturaIesireController implements Initializable, DBConstants, ControllerStocTransfer {

    @FXML private ComboBox<Partener> numePartenerCB;
    @FXML private TextField nrDocumentTF;
    @FXML private DatePicker dataEmitereDP;
    @FXML private TextField termenPlataTF;
    @FXML private TextField obsFacturaTF;
    @FXML private ComboBox<Operatori> operatorCB;
    @FXML private Label cuiPartenerLB;
    @FXML private Label contPartenerLB;
    @FXML private Label telefonPartenerLB;
    @FXML private Label adresaPartenerLB;
    @FXML private TableView<Stoc> tabelIesiriTV;
    @FXML private TableColumn<Stoc, String> denumireProdusTC;
    @FXML private TableColumn<Stoc, String> concentratieTC;
    @FXML private TableColumn<Stoc, Integer> cantitateTC;
    @FXML private TableColumn<Stoc, Float> pretVanzareTC;
    @FXML private TableColumn<Stoc, Float> pretImpusTC;
    @FXML private TableColumn<Stoc, Integer> procentTvaTC;
    @FXML private TableColumn<Stoc, Float> valoareTvaTC;
    @FXML private TableColumn<Stoc, LocalDate> bbdTC;
    @FXML private TableColumn<Stoc, String> lotTC;
    @FXML private TableColumn<Stoc, String> producatorTC;
    @FXML private TableColumn<Stoc, String> formaTC;
    @FXML private TableColumn<Stoc, Long> codBareTC;
    @FXML private Label operatorLB;
    @FXML private Label valFTvaLB;
    @FXML private Label valTvaLB;
    @FXML private Label valDocLB;
    @FXML private Label msgLB;
    @FXML private Button stergePozitieBTN;
    @FXML private Button adaugaPozitieBTN;
    @FXML private Button okBTN;
    @FXML private Button anulareBTN;

    ObservableList<Partener> parteneriObservableList = FXCollections.observableArrayList();
    ObservableList<Operatori> operatoriObservableList = FXCollections.observableArrayList();
    ObservableList<Stoc> stocPentruTabel = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // dezactivare buton stergere
        stergePozitieBTN.setDisable(true);

        // configurare Text Field Termen de plata - valoare default 120
        termenPlataTF.setText("120");


        // configurare Date Picker - valori LocalDate.now()
        dataEmitereDP.setValue(LocalDate.now());
        // utilizat pentru restrictionarea perioadei selectabile din Date Picker (validare imput data)
        LocalDate aziMinusOLuna = LocalDate.now().minus(Period.ofMonths(1));
        restrictDatePicker(dataEmitereDP, aziMinusOLuna, LocalDate.now());


        // configurare Combo Box - valori default
        try {
            parteneriObservableList = getAllParteneri();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        numePartenerCB.setItems(parteneriObservableList);
        numePartenerCB.getSelectionModel().select(2);
        // TODO =  optional - selectare depozit dupa index

        try {
            operatoriObservableList = getAllOperatori();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        operatorCB.setItems(operatoriObservableList);
        operatorCB.getSelectionModel().select(0);


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
        denumireProdusTC.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        concentratieTC.setCellValueFactory(new PropertyValueFactory<>("concentratie"));
        cantitateTC.setCellValueFactory(new PropertyValueFactory<>("cantitateDisp"));
        pretVanzareTC.setCellValueFactory(new PropertyValueFactory<>("pretVanzare"));
        pretImpusTC.setCellValueFactory(new PropertyValueFactory<>("pretImpus"));
        procentTvaTC.setCellValueFactory(new PropertyValueFactory<>("tva"));
        valoareTvaTC.setCellValueFactory(new PropertyValueFactory<>("valoareTva"));
        bbdTC.setCellValueFactory(new PropertyValueFactory<>("bbd"));
        lotTC.setCellValueFactory(new PropertyValueFactory<>("lot"));
        producatorTC.setCellValueFactory(new PropertyValueFactory<>("producator"));
        formaTC.setCellValueFactory(new PropertyValueFactory<>("forma"));
        codBareTC.setCellValueFactory(new PropertyValueFactory<>("codBare"));

        tabelIesiriTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // dezactivare controale operator:
        operatorCB.setVisible(false);
        operatorLB.setVisible(false);
    }

    /**
     * Buton pentru adaugarea unui produs pe factura de iesire
     * trimit catre scena de adaugare, lista provenita de la TableView pentru a fi
     * pastrata si reinnoita de catre scena de adaugare
     */
    @FXML
    void butonAdaugareIPozitie(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        ObservableList<Stoc> listaDeTransferat = tabelIesiriTV.getItems();

        AddIesireController newAddIesireController = new AddIesireController();

        sc.changeScenesStoc(event, "AddIesire.fxml", "Adauga produs pe factura de iesire",
                listaDeTransferat, newAddIesireController);
    }

    /**
     * Anulare introducere factura
     */
    @FXML
    void butonBackToMain(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmarea anulare introducere factura");
        alert.setHeaderText("Confirmare pentru anularea introducerii facturii");
        alert.setContentText("Sunteti sigur ca doriti sa iesiti?\nOperati va anula crearea facturii");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ObservableList<Stoc> stocDinTabel = tabelIesiriTV.getItems();
            if (stocDinTabel != null) {
                try {
                    for (Stoc stoc : stocDinTabel) {
                        stoc.actualizareStoc(stoc.getCantitateDisp(), ADAUGA_IN_STOC);
                    }
                    SceneChanger sc = new SceneChanger();
                    sc.changeScenes(event, "MainScene.fxml", "FARMA 10");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Finalizare factura
     */
    @FXML
    void butonOKIntroducereaFacturii(ActionEvent event) {

        try {
            if (tabelIesiriTV.getItems().size() == 0) {
                throw new IllegalArgumentException("Factura nu contine produse");
            }

            Documente antetFactura = creazaAntetFactura();

            long ultimulIdDocument;
            if (antetFactura != null) {

                printFacturaIesireToPdf();

                ultimulIdDocument = antetFactura.inserareDocumentInBd(2);
                System.out.println("Ultimul id document inserat este " + ultimulIdDocument);

                ObservableList<Stoc> produseFacturaDeIesire = tabelIesiriTV.getItems();
                for (Stoc stoc : produseFacturaDeIesire) {
                    stoc.insertIesireIntoDb(ultimulIdDocument);
                }

                SceneChanger sc = new SceneChanger();
                sc.changeScenes(event, "MainScene.fxml", "FARMA 10");

            } else {
                throw new IllegalArgumentException("Factura nu este completata corect. Va rugam verificati.");
            }
        } catch (IllegalArgumentException | SQLException | IOException | DocumentException e) {
            msgLB.setText(e.getMessage());
        }
    }

    /**
     * Buton pentru stergerea intrarilor din tabela produselor de iesire
     */
    @FXML
    void butonStergePozitie(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmarea stergerii");
        alert.setHeaderText("Confirmare pentru stergerea intrarii");
        alert.setContentText("Sunteti sigur ca doriti stergerea intrarii de pe factura?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            ObservableList<Stoc> stocDinTabel = tabelIesiriTV.getItems();
            Stoc produsDeSters = tabelIesiriTV.getSelectionModel().getSelectedItem();
            boolean isUpdated = produsDeSters.actualizareStoc(produsDeSters.getCantitateDisp(), ADAUGA_IN_STOC);
            if (isUpdated) {
                stocDinTabel.remove(produsDeSters);
                actualizeazaCifreFactura(stocDinTabel);
            } else {
                System.err.println("Stocul nu a fost actualizat in BD");
            }
        }
    }

    /**
     * Creare antet pentru inserare in BD
     * @return = un obiect Document verificat pentru a fi  folosit la inserare in BD
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
            SimpleObjectProperty<LocalDate> finalDataEmitere = new SimpleObjectProperty<>(dataEmitereDP.getValue());
            SimpleObjectProperty<LocalDate> finalDataOperare = new SimpleObjectProperty<>(LocalDate.now());

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
     * Activeaza butonul de stergere atunci cand se va selecta un produs din lista
     */
    @FXML
    void produsSelectat(MouseEvent event) {
        stergePozitieBTN.setDisable(false);
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

    @Override
    public void preloadList(ObservableList<Stoc> stocTransfer) {
        this.stocPentruTabel = stocTransfer;
        tabelIesiriTV.getItems().addAll(stocTransfer);

        actualizeazaCifreFactura(stocTransfer);
    }

    /**
     * actualizeaza etichetele cu datele numerice ale facturii
     * @param listaStocDinTabel: lista cu produsele din tabel
     */
    private void actualizeazaCifreFactura(ObservableList<Stoc> listaStocDinTabel) {
        float pretVanzareTotal = 0;
        float valTvaTotal = 0;
        for (Stoc stoc : listaStocDinTabel) {
            pretVanzareTotal += stoc.getPretVanzare() * stoc.getCantitateDisp();
            valTvaTotal += stoc.getValoareTva() * stoc.getCantitateDisp();
        }
        float valTotalaDocCuTva = valTvaTotal + pretVanzareTotal;
        valTvaLB.setText(roundFloat(valTvaTotal, 2) + " lei");
        valDocLB.setText(roundFloat(valTotalaDocCuTva, 2) + " lei");
        valFTvaLB.setText(roundFloat(pretVanzareTotal, 2) + " lei");
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
     * Metoda pentru tiparirea facturii intr-un fisier pdf
     */
    public void printFacturaIesireToPdf() throws FileNotFoundException, DocumentException {

        //Color whiteColor = new DeviceRgb(255, 255, 255);
        var font14Bold = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        var font18Bold = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        var font12Normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        // creare fisier
        String numeFisier =
                "Iesire " +
                numePartenerCB.getSelectionModel().getSelectedItem().getNumePartener() + " F" +
                nrDocumentTF.getText() + ".pdf";

        Document doc = new Document(PageSize.A4, 1.5f, 1.5f, 30, 20);
        PdfWriter.getInstance(doc, new FileOutputStream("Facturi/" + numeFisier));
        doc.open();

        // antet
        var table1 = new PdfPTable(3);
        table1.setWidths(new int[]{2, 2, 4});

        PdfPCell cellFurnizor = new PdfPCell(new Phrase
                ("FARMA 10\nAdresa: depozit\nTelefon:\nCUI:\n"));
        cellFurnizor.setBorderColor(new BaseColor(255, 255, 255));
        table1.addCell(cellFurnizor);
        PdfPCell cellEmpty = new PdfPCell(new Phrase("        "));
        cellEmpty.setBorderColor(new BaseColor(255, 255, 255));
        table1.addCell(cellEmpty);
        PdfPCell cellClient = new PdfPCell(new Phrase(
                numePartenerCB.getSelectionModel().getSelectedItem().getNumePartener() + "\n" +
                        "Adresa: " + numePartenerCB.getSelectionModel().getSelectedItem().getAdresa() + "\n" +
                        "Tel.: " + numePartenerCB.getSelectionModel().getSelectedItem().getTelefon() + "\n" +
                        "Email: " + numePartenerCB.getSelectionModel().getSelectedItem().getEmail() + "\n" +
                        "CUI: " + numePartenerCB.getSelectionModel().getSelectedItem().getCui() + "\n" +
                        "IBAN: " + numePartenerCB.getSelectionModel().getSelectedItem().getCont() + "\n"
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
        List<Stoc> listaFacturaCurenta = new ArrayList<Stoc>();
        listaFacturaCurenta = tabelIesiriTV.getItems();

        var table2 = new PdfPTable(8);
        table2.setWidths(new int[]{60, 57, 25, 45, 40, 20, 28, 20});

            // cap tabel
        table2.addCell("Produs");
        table2.addCell("Producator");
        table2.addCell("Cantit.");
        table2.addCell("BBD");
        table2.addCell("Lot");
        table2.addCell("TVA (%)");
        table2.addCell("Pret unitar (lei)");
        table2.addCell("Val. TVA (lei)\n  ");

            // continut tabel
        for(Stoc item : listaFacturaCurenta) {
            table2.addCell(item.getDenumire());
            table2.addCell("  \n" + item.getProducator() + "\n" + "  ");
            table2.addCell(String.valueOf(item.getCantitateDisp()));
            table2.addCell(item.getBbd().toString());
            table2.addCell(item.getLot());
            table2.addCell(String.valueOf(item.getTva()));
            table2.addCell(String.valueOf(item.getPretVanzare()));
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

        table5.addCell(" \n  FARMA10\n  \n  \n  \n  L.S.\n  \n  ");
        table5.addCell(" \n  Livrat la:\n  \n  Observatii:\n");
        table5.addCell(" \n  Primire:\n");

        var paragrafFinal = new Paragraph("", font14Bold);
        paragrafFinal.add(table3);
        paragrafFinal.add(table4);
        paragrafFinal.add(table5);


        // finalizare document
        doc.add(paragraphAntet);
        doc.add(paragraphTitlu);
        doc.add(paragrafProduse);
        doc.add(paragrafFinal);
        doc.close();

    }
}
