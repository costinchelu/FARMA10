package farma.model;

import farma.util.DBConstants;
import javafx.beans.property.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Documente implements DBConstants {

    private SimpleLongProperty idDocument;
    private SimpleLongProperty nrDocument;
    private SimpleObjectProperty<LocalDate> dataEmitere;
    private SimpleObjectProperty<LocalDate> dataOperare;
    private SimpleIntegerProperty termenPlata;
    private SimpleLongProperty idOperatie;
    private SimpleLongProperty idPartener;
    private SimpleLongProperty idOperator;
    private SimpleStringProperty observatii;


    public Documente(SimpleLongProperty idDocument,
                     SimpleLongProperty nrDocument,
                     SimpleObjectProperty<LocalDate> dataEmitere,
                     SimpleObjectProperty<LocalDate> dataOperare,
                     SimpleIntegerProperty termenPlata,
                     SimpleLongProperty idOperatie,
                     SimpleLongProperty idPartener,
                     SimpleLongProperty idOperator,
                     SimpleStringProperty observatii) {
        this.idDocument = idDocument;
        this.nrDocument = nrDocument;
        this.dataEmitere = dataEmitere;
        this.dataOperare = dataOperare;
        this.termenPlata = termenPlata;
        this.idOperatie = idOperatie;
        this.idPartener = idPartener;
        this.idOperator = idOperator;
        this.observatii = observatii;
    }

    public Documente(SimpleLongProperty nrDocument,
                     SimpleObjectProperty<LocalDate> dataEmitere,
                     SimpleObjectProperty<LocalDate> dataOperare,
                     SimpleIntegerProperty termenPlata,
                     SimpleLongProperty idOperatie,
                     SimpleLongProperty idPartener,
                     SimpleLongProperty idOperator,
                     SimpleStringProperty observatii) {
        this.nrDocument = nrDocument;
        this.dataEmitere = dataEmitere;
        this.dataOperare = dataOperare;
        this.termenPlata = termenPlata;
        this.idOperatie = idOperatie;
        this.idPartener = idPartener;
        this.idOperator = idOperator;
        this.observatii = observatii;
    }

    public long getIdDocument() {
        return idDocument.get();
    }

    public SimpleLongProperty idDocumentProperty() {
        return idDocument;
    }

    public void setIdDocument(long idDocument) {
        this.idDocument.set(idDocument);
    }

    public long getNrDocument() {
        return nrDocument.get();
    }

    public SimpleLongProperty nrDocumentProperty() {
        return nrDocument;
    }

    public void setNrDocument(long nrDocument) {
        this.nrDocument.set(nrDocument);
    }

    public LocalDate getDataEmitere() {
        return dataEmitere.get();
    }

    public SimpleObjectProperty<LocalDate> dataEmitereProperty() {
        return dataEmitere;
    }

    public void setDataEmitere(LocalDate dataEmitere) {
        this.dataEmitere.set(dataEmitere);
    }

    public LocalDate getDataOperare() {
        return dataOperare.get();
    }

    public SimpleObjectProperty<LocalDate> dataOperareProperty() {
        return dataOperare;
    }

    public void setDataOperare(LocalDate dataOperare) {
        this.dataOperare.set(dataOperare);
    }

    public int getTermenPlata() {
        return termenPlata.get();
    }

    public SimpleIntegerProperty termenPlataProperty() {
        return termenPlata;
    }

    public void setTermenPlata(int termenPlata) {
        this.termenPlata.set(termenPlata);
    }

    public long getIdOperatie() {
        return idOperatie.get();
    }

    public SimpleLongProperty idOperatieProperty() {
        return idOperatie;
    }

    public void setIdOperatie(long idOperatie) {
        this.idOperatie.set(idOperatie);
    }

    public long getIdPartener() {
        return idPartener.get();
    }

    public SimpleLongProperty idPartenerProperty() {
        return idPartener;
    }

    public void setIdPartener(long idPartener) {
        this.idPartener.set(idPartener);
    }

    public long getIdOperator() {
        return idOperator.get();
    }

    public SimpleLongProperty idOperatorProperty() {
        return idOperator;
    }

    public void setIdOperator(long idOperator) {
        this.idOperator.set(idOperator);
    }

    public String getObservatii() {
        return observatii.get();
    }

    public SimpleStringProperty observatiiProperty() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii.set(observatii);
    }

    @Override
    public String toString() {
        return "Document{" +
                "idDocument=" + idDocument +
                ", nrDocumen=" + nrDocument +
                ", dataEmitere=" + dataEmitere +
                ", dataOperare=" + dataOperare +
                ", termenPlata=" + termenPlata +
                ", idOperatie=" + idOperatie +
                ", idPartener=" + idPartener +
                ", idOperator=" + idOperator +
                ", observatii=" + observatii +
                '}';
    }

    /**
     * Inserare document cu returnarea id-ului
     */
    public long inserareDocumentInBd(long idOperatiePrimit) throws SQLException {
        LocalDate localDateNoW = LocalDate.now(); //For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        String stringDataEmitere = getDataEmitere().format(formatter);
        String stringDataOperare = getDataOperare().format(formatter);
        int nrIntrariInserate = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement(DOCUMENT_INSERT_INTO);
            ps.setLong(1, getNrDocument());
            ps.setString(2, stringDataEmitere);
            ps.setString(3, stringDataOperare);
            ps.setInt(4, getTermenPlata());
            ps.setLong(5, idOperatiePrimit);
            ps.setLong(6, getIdPartener());
            ps.setLong(7, getIdOperator());
            ps.setString(8, getObservatii());

            System.out.println(toString());

            nrIntrariInserate = ps.executeUpdate();
            if (nrIntrariInserate == 0) {
                throw new SQLException("Nu au fost introduse intrari");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) {
                ps.close();
            }
            if(conn != null) {
                conn.close();
            }
        }

        if (nrIntrariInserate == 1) {
            long valoareUltimId = getLastId();
            System.out.println("A fost introdus id " + valoareUltimId);
            return valoareUltimId;
        }
        else return -1;
    }

    /**
     * returneaza ultimul id din tabela document
     * @return max(id_nomenclator) din tabela nomenclator
     */
    public long getLastId() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        long lastId = 0;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            ps = conn.prepareStatement("SELECT MAX(id_document) FROM document");

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                lastId = resultSet.getLong(1);
            }
            else {
                throw new SQLException("Fara id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

        return lastId;
    }
}
