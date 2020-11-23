package farma.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import farma.model.StocOperatii;
import farma.util.DBConstants;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.List;


public class TabelStocController implements Initializable, DBConstants {

    @FXML private TableView<StocOperatii> tabelStocTV;
    @FXML private TableColumn<StocOperatii, String> denProdTC;
    @FXML private TableColumn<StocOperatii, String> concentratieTC;
    @FXML private TableColumn<StocOperatii, Integer> cantitateTC;
    @FXML private TableColumn<StocOperatii, Float> pretTC;
    @FXML private TableColumn<StocOperatii, String> lotTC;
    @FXML private TableColumn<StocOperatii, LocalDate> bbdTC;
    @FXML private TableColumn<StocOperatii, String> furnizorTC;
    @FXML private TableColumn<StocOperatii, Long> nrFacturaTC;
    @FXML private TableColumn<StocOperatii, LocalDate> dataEmitereTC;
    @FXML private TableColumn<StocOperatii, Float> adaosTC;
    @FXML private TableColumn<StocOperatii, Integer> tvaTC;
    @FXML private TableColumn<StocOperatii, Long> codBareTC;
    @FXML private TableColumn<StocOperatii, String> producatorTC;
    @FXML private TableColumn<StocOperatii, String> dciTC;
    @FXML private Label msgLB;
    ObservableList<StocOperatii> listaStocCurent = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // setari afisare coloane Table View
        denProdTC.setCellValueFactory(new PropertyValueFactory<>("denumireProd"));
        concentratieTC.setCellValueFactory(new PropertyValueFactory<>("concentratie"));
        cantitateTC.setCellValueFactory(new PropertyValueFactory<>("cantitateDisp"));
        pretTC.setCellValueFactory(new PropertyValueFactory<>("pretVanzare"));
        lotTC.setCellValueFactory(new PropertyValueFactory<>("lot"));
        bbdTC.setCellValueFactory(new PropertyValueFactory<>("bbd"));
        furnizorTC.setCellValueFactory(new PropertyValueFactory<>("furnizor"));
        nrFacturaTC.setCellValueFactory(new PropertyValueFactory<>("nrDocument"));
        dataEmitereTC.setCellValueFactory(new PropertyValueFactory<>("dataOperare"));
        adaosTC.setCellValueFactory(new PropertyValueFactory<>("adaos"));
        tvaTC.setCellValueFactory(new PropertyValueFactory<>("tva"));
        codBareTC.setCellValueFactory(new PropertyValueFactory<>("codBare"));
        producatorTC.setCellValueFactory(new PropertyValueFactory<>("producator"));
        dciTC.setCellValueFactory(new PropertyValueFactory<>("dci"));

        // incarcare table view
        try {
            listaStocCurent.addAll(getStocCurent());
        } catch (SQLException e) {
            msgLB.setText("Nu se poate efectua consultarea bazei de date");
            System.err.println(e.getMessage());
        }
        tabelStocTV.getItems().addAll(listaStocCurent);
    }

    private ObservableList<StocOperatii> getStocCurent() throws SQLException {
        ObservableList<StocOperatii> listaStoc = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_STOC_OPERATII);

            while (resultSet.next()) {
                SimpleLongProperty idStoc = new SimpleLongProperty(resultSet.getLong("id_stoc"));
                SimpleLongProperty idIntrare = new SimpleLongProperty(resultSet.getLong("id_intrare"));
                Float pretVanzareB = roundFloat(resultSet.getFloat("pret"), 2);
                SimpleFloatProperty pretVanzare = new SimpleFloatProperty(pretVanzareB);
                SimpleIntegerProperty cantitateDisp = new SimpleIntegerProperty(resultSet.getInt("cantitate"));
                SimpleStringProperty lot = new SimpleStringProperty(resultSet.getString("lot"));
                LocalDate bbdPrimit = resultSet.getDate("bbd").toLocalDate();
                SimpleObjectProperty<LocalDate> bbd = new SimpleObjectProperty<>(bbdPrimit);
                SimpleStringProperty denumireProd = new SimpleStringProperty(resultSet.getString("denumire_prod"));
                SimpleLongProperty codBare = new SimpleLongProperty(resultSet.getLong("cod_bare"));
                SimpleStringProperty concentratie = new SimpleStringProperty(resultSet.getString("concentratie"));
                Float adaosB = roundFloat(resultSet.getFloat("adaos"), 2);
                SimpleFloatProperty adaos = new SimpleFloatProperty(adaosB);
                SimpleStringProperty producator = new SimpleStringProperty(resultSet.getString("nume_producator"));
                SimpleIntegerProperty tva = new SimpleIntegerProperty(resultSet.getInt("procent_tva"));
                SimpleLongProperty idDocument = new SimpleLongProperty(resultSet.getLong("id_document"));
                SimpleLongProperty nrDocument = new SimpleLongProperty(resultSet.getLong("nr_document"));
                LocalDate dataOperarePrimita = resultSet.getDate("data_operare").toLocalDate();
                SimpleObjectProperty<LocalDate> dataOperare = new SimpleObjectProperty<>(dataOperarePrimita);
                SimpleStringProperty furnizor = new SimpleStringProperty(resultSet.getString("nume_partener"));
                SimpleStringProperty dci = new SimpleStringProperty(resultSet.getString("den_dci"));

                StocOperatii stoc = new StocOperatii(idStoc, idIntrare, pretVanzare, cantitateDisp, lot, bbd,
                        denumireProd, codBare, concentratie, adaos, producator, tva, idDocument, nrDocument, dataOperare,
                        furnizor, dci);

                listaStoc.add(stoc);
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
        return listaStoc;
    }

    /**
     * Actiune pentru intoarcerea in meniul principal
     */
    public void butonBackToMain(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "MainScene.fxml", "FARMA 10");
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
     * Metoda pentru crearea unui raport stoc si tiparirea sa intr-un document pdf
     */
    public void printStocToPdf(ActionEvent event) throws FileNotFoundException, DocumentException {

        // denumirea fisierului
        String pattern = "yyyyMMddHHmmss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        String numeFisier = "StocLaData" + todayAsString + ".pdf";

        String pattern2 = "dd.MM.yyyy";
        DateFormat df2 = new SimpleDateFormat(pattern2);
        String dataPtTitlu = df2.format(today);

        // creare fisier
        var doc = new Document(PageSize.A4.rotate(), 1.5f, 1.5f, 20f, 20f);
        PdfWriter.getInstance(doc, new FileOutputStream("Rapoarte/" + numeFisier));
        doc.open();

        // titlu
        var bold = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        var paragraph = new Paragraph("Stoc la data de " + dataPtTitlu + "\n\n\n", bold);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        // lista stocului
        List<StocOperatii> listaStoc = new ArrayList<>();
        listaStoc = tabelStocTV.getItems();

        // cap tabel stoc
        var table = new PdfPTable(7);
        table.setWidths(new int[]{40, 14, 14, 20 ,20, 36, 16});
        table.addCell("Denumire produs");
        table.addCell("Cantitate (flacoane)");
        table.addCell("Pret vanzare (lei)");
        table.addCell("Lot");
        table.addCell("BBD");
        table.addCell("Furnizor");
        table.addCell("Numar document");

        // continut tabel stoc
        for(StocOperatii item : listaStoc) {
            table.addCell(item.getDenumireProd());
            table.addCell(String.valueOf(item.getCantitateDisp()));
            table.addCell(String.valueOf(item.getPretVanzare()));
            table.addCell(item.getLot());
            table.addCell(item.getBbd().toString());
            table.addCell(item.getFurnizor());
            table.addCell(String.valueOf(item.getNrDocument()));
            table.completeRow();
        }

        var reg = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        var paragraphTable = new Paragraph("", reg);

        // finalizare document
        paragraphTable.add(table);
        doc.add(paragraph);
        doc.add(paragraphTable);
        doc.close();
        msgLB.setText("Stocul a fost tiparit in format pdf in locatia Rapoarte");
    }
}
