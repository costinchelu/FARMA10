package farma.view;

import farma.model.*;
import farma.util.DBConstants;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AddProduseController implements Initializable, DBConstants, ControllerClass {

    @FXML private Label titluLB;
    @FXML private TextField denProdusTF;
    @FXML private ComboBox<Dci> denDciCB = new ComboBox<>();
    @FXML private ComboBox<Producatori> producatorCB = new ComboBox<>();
    @FXML private TextField concentratieTF;
    @FXML private ComboBox<Forma> formaCB = new ComboBox<>();
    @FXML private ComboBox<Tva> tvaCB = new ComboBox<>();
    @FXML private TextField adaosTF;
    @FXML private TextField pretImpusTF;
    @FXML private TextField ambalareTF;
    @FXML private TextField observatiiTF;
    @FXML private TextField codBareTF;
    @FXML private Label msgLB;
    @FXML private Button adaugaBTN;
    @FXML private Button anuleazaBTN;

    private NomenclatorExtins nom;
    ObservableList<Dci> dciObservableList = FXCollections.observableArrayList();
    ObservableList<Tva> tvaObservableList = FXCollections.observableArrayList();
    ObservableList<Producatori> producatoriObservableList = FXCollections.observableArrayList();
    ObservableList<Forma> formaObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // ascunderea textului etichetei de mesaje
        msgLB.setText("");

        if (nom != null) {
            //pentru editare produs nomeclator
            //updateNomenclatorDinView();

        } else {
            // pentru inserare produs nou

            // setare Combo Box cu valorile TVA (default: 9)
            // TODO = selectare item-uri dupa index
            try {
                tvaObservableList = getAllTva();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tvaCB.setItems(tvaObservableList);
            tvaCB.getSelectionModel().select(0);

            // setare Combo Box cu denumirile DCI (default: fara DCI)
            try {
                dciObservableList = getAllDci();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            denDciCB.setItems(dciObservableList);
            denDciCB.getSelectionModel().select(619);

            // setare Combo Box nume producator (default: NECUNOSCUT)
            try {
                producatoriObservableList = getAllProducatori();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            producatorCB.setItems(producatoriObservableList);
            producatorCB.getSelectionModel().select(354);

            // setare Combo Box forma farmaceutica (default: compr.)
            try {
                formaObservableList = getAllForma();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            formaCB.setItems(formaObservableList);
            formaCB.getSelectionModel().select(18);
        }
    }

    /**
     * Buton pentru intoarcerea la tabela nomenclatorului de produse cu anularea modificărilor
     */
    public void butonBackToNomenclator(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "TabelNomProduse.fxml", "Nomenclator produse in stoc");
    }

    /**
     * citeste din scena si insereaza in BD o noua intrare Nomenclator
     * la acționare a butonului, ne reintoarcem la tabela nomenclatorului de produse
     */
    public void butonAdaugaSalveazaProdus(ActionEvent event) throws IOException, SQLException {

        if (nom != null) {
            // caz de update intrare
            NomenclatorExtins editedNom = creareObiectNomenclatorDinView();

            if (valideazaDenumireProdus() && valideazaAdaos() && valideazaPretImpus() && valideazaCodBare()) {
                editedNom.updateNomenclatorInBd();
                butonBackToNomenclator(event);
            }
        } else {
            // nomenclator nou
            try {
                if (!valideazaDenumireProdus()) {
                    throw new IllegalArgumentException("Denumirea produsului trebuie sa aiba cel putin un caracter!");
                }
                if (!valideazaCodBare()) {
                    throw new IllegalArgumentException("Codul de bare este un numar intreg pozitiv");
                }
                if (!valideazaAdaos()) {
                    throw new IllegalArgumentException("Adaosul poate fi decat un numar pozitiv mai mic sau egal cu 999.99");
                }
                if (!valideazaPretImpus()) {
                    throw new IllegalArgumentException("Pretul impus poate fi decat un numar pozitiv mic sau egal cu 99999999.99");
                }


                SimpleStringProperty newDenProd = new SimpleStringProperty(denProdusTF.getText());

                SimpleLongProperty newCodBare;
                if (codBareTF.getText().trim().isEmpty()) {
                    newCodBare = new SimpleLongProperty(1111111111111L);
                } else {
                    newCodBare = new SimpleLongProperty(Long.parseLong(codBareTF.getText()));
                }

                SimpleStringProperty newConcentratie;
                if (concentratieTF.getText().isEmpty()) {
                    newConcentratie = new SimpleStringProperty("");
                } else {
                    newConcentratie = new SimpleStringProperty(concentratieTF.getText());
                }

                SimpleFloatProperty newAdaos;
                if (adaosTF.getText().trim().isEmpty()) {
                    newAdaos = new SimpleFloatProperty(0F);
                } else {
                    newAdaos = new SimpleFloatProperty(Float.parseFloat(adaosTF.getText()));
                }

                SimpleStringProperty newAmbalare;
                if (ambalareTF.getText().isEmpty()) {
                    newAmbalare = new SimpleStringProperty("");
                } else {
                    newAmbalare = new SimpleStringProperty(ambalareTF.getText());
                }

                SimpleFloatProperty newPretImpus;
                if (pretImpusTF.getText().trim().isEmpty()) {
                    newPretImpus = new SimpleFloatProperty(0F);
                } else {
                    newPretImpus = new SimpleFloatProperty(Float.parseFloat(pretImpusTF.getText()));
                }

                SimpleStringProperty newObservatii;
                if (observatiiTF.getText().isEmpty()) {
                    newObservatii = new SimpleStringProperty("");
                } else {
                    newObservatii = new SimpleStringProperty(observatiiTF.getText());
                }


                SimpleLongProperty newIdForma = new SimpleLongProperty(formaCB.getSelectionModel().getSelectedItem().getIdForma());

                SimpleLongProperty newIdTva = new SimpleLongProperty(tvaCB.getSelectionModel().getSelectedItem().getIdTva());
                SimpleLongProperty newIdDci = new SimpleLongProperty(denDciCB.getSelectionModel().getSelectedItem().getIdDci());
                SimpleLongProperty newIdProducator = new SimpleLongProperty(producatorCB.getSelectionModel().getSelectedItem().getIdProducator());

                NomenclatorExtins newNomenclator = new NomenclatorExtins(newDenProd, newCodBare, newConcentratie,
                        newAdaos, newAmbalare, newPretImpus, newObservatii, newIdForma, newIdTva, newIdDci, newIdProducator);

                System.out.println(newNomenclator.toString());

                msgLB.setText("");
                try {
                    newNomenclator.insertIntoDb();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            } catch (Exception e) {
                msgLB.setText(e.getMessage());
            }

            if (valideazaDenumireProdus() && valideazaAdaos() && valideazaPretImpus() && valideazaCodBare()) {
                butonBackToNomenclator(event);
            }
        }
    }

    private NomenclatorExtins creareObiectNomenclatorDinView() {
        NomenclatorExtins newNomenclator = null;
        try {
            if (!valideazaDenumireProdus()) {
                throw new IllegalArgumentException("Denumirea produsului trebuie sa aiba cel putin un caracter!");
            }
            if (!valideazaCodBare()) {
                throw new IllegalArgumentException("Codul de bare este un numar intreg pozitiv");
            }
            if (!valideazaAdaos()) {
                throw new IllegalArgumentException("Adaosul poate fi decat un numar pozitiv mai mic sau egal cu 999.99");
            }
            if (!valideazaPretImpus()) {
                throw new IllegalArgumentException("Pretul impus poate fi decat un numar pozitiv mic sau egal cu 99999999.99");
            }


            SimpleStringProperty newDenProd = new SimpleStringProperty(denProdusTF.getText());

            SimpleLongProperty newCodBare;
            if (codBareTF.getText().trim().isEmpty()) {
                newCodBare = new SimpleLongProperty(1111111111111L);
            } else {
                newCodBare = new SimpleLongProperty(Long.parseLong(codBareTF.getText()));
            }

            SimpleStringProperty newConcentratie;
            if (concentratieTF.getText().isEmpty()) {
                newConcentratie = new SimpleStringProperty("");
            } else {
                newConcentratie = new SimpleStringProperty(concentratieTF.getText());
            }

            SimpleFloatProperty newAdaos;
            if (adaosTF.getText().trim().isEmpty()) {
                newAdaos = new SimpleFloatProperty(0F);
            } else {
                newAdaos = new SimpleFloatProperty(Float.parseFloat(adaosTF.getText()));
            }

            SimpleStringProperty newAmbalare;
            if (ambalareTF.getText().isEmpty()) {
                newAmbalare = new SimpleStringProperty("");
            } else {
                newAmbalare = new SimpleStringProperty(ambalareTF.getText());
            }

            SimpleFloatProperty newPretImpus;
            if (pretImpusTF.getText().trim().isEmpty()) {
                newPretImpus = new SimpleFloatProperty(0F);
            } else {
                newPretImpus = new SimpleFloatProperty(Float.parseFloat(pretImpusTF.getText()));
            }

            SimpleStringProperty newObservatii;
            if (observatiiTF.getText().isEmpty()) {
                newObservatii = new SimpleStringProperty("");
            } else {
                newObservatii = new SimpleStringProperty(observatiiTF.getText());
            }


            SimpleLongProperty newIdForma = new SimpleLongProperty(formaCB.getSelectionModel().getSelectedItem().getIdForma());

            SimpleLongProperty newIdTva = new SimpleLongProperty(tvaCB.getSelectionModel().getSelectedItem().getIdTva());
            SimpleLongProperty newIdDci = new SimpleLongProperty(denDciCB.getSelectionModel().getSelectedItem().getIdDci());
            SimpleLongProperty newIdProducator = new SimpleLongProperty(producatorCB.getSelectionModel().getSelectedItem().getIdProducator());

            SimpleLongProperty idNom = new SimpleLongProperty(nom.getIdNomenclator());

            newNomenclator = new NomenclatorExtins(idNom, newDenProd, newCodBare, newConcentratie,
                    newAdaos, newAmbalare, newPretImpus, newObservatii, newIdForma, newIdTva, newIdDci, newIdProducator);

            //System.out.println(newNomenclator.toString());

            msgLB.setText("");

        } catch (Exception e) {
            msgLB.setText(e.getMessage());
        }
        return newNomenclator;
    }

    /**
     * Primeste obiectul Dci de la tabela nomenclator DCI și populează campurile cu informatiile de editat
     * @param nomenclatorPrimit = tabela DCI trimite un obiect către scena de editare/adaugare
     */
    @Override
    public void preloadObject(Object nomenclatorPrimit) {
        this.nom = (NomenclatorExtins) nomenclatorPrimit;

        titluLB.setText("Editarea intrarii din nomenclatorul de produse");
        adaugaBTN.setText("OK");

        denProdusTF.setText(((NomenclatorExtins) nomenclatorPrimit).getDenumireProd());
        concentratieTF.setText(((NomenclatorExtins) nomenclatorPrimit).getConcentratie());
        adaosTF.setText(String.valueOf(((NomenclatorExtins) nomenclatorPrimit).getAdaos()));
        pretImpusTF.setText(String.valueOf(((NomenclatorExtins) nomenclatorPrimit).getPretImpus()));
        ambalareTF.setText(((NomenclatorExtins) nomenclatorPrimit).getAmbalare());
        observatiiTF.setText(((NomenclatorExtins) nomenclatorPrimit).getObservatii());
        codBareTF.setText(String.valueOf(((NomenclatorExtins) nomenclatorPrimit).getCodBare()));

        // setare Combo Box cu valorile TVA (cautam valoarea indexului valorii idTva din obiectul preluat)
        try {
            tvaObservableList = getAllTva();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvaCB.setItems(tvaObservableList);

        Iterator<Tva> iterTva = tvaObservableList.iterator();
        int i = -1;
        while (iterTva.hasNext()) {
            i++;
            if (iterTva.next().getIdTva() == nom.getIdTva()) {
                break;
            }
        }
        tvaCB.getSelectionModel().select(i);

        // setare Combo Box cu denumirile DCI (preluat cu ajutorul iteratorului din lista)
        try {
            dciObservableList = getAllDci();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        denDciCB.setItems(dciObservableList);

        Iterator<Dci> iterDci = dciObservableList.iterator();
        i = -1;
        while (iterDci.hasNext()) {
            i++;
            if (iterDci.next().getIdDci() == nom.getIdDci()) {
                break;
            }
        }
        denDciCB.getSelectionModel().select(i);

        // setare Combo Box nume producator (default: NECUNOSCUT)
        try {
            producatoriObservableList = getAllProducatori();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        producatorCB.setItems(producatoriObservableList);

        Iterator<Producatori> iterProducatori = producatoriObservableList.iterator();
        i = -1;
        while (iterProducatori.hasNext()) {
            i++;
            if (iterProducatori.next().getIdProducator() == nom.getIdProducator()) {
                break;
            }
        }
        producatorCB.getSelectionModel().select(i);

        // setare Combo Box forma farmaceutica (default: compr.)
        try {
            formaObservableList = getAllForma();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        formaCB.setItems(formaObservableList);

        Iterator<Forma> iterForma = formaObservableList.iterator();
        i = -1;
        while (iterForma.hasNext()) {
            i++;
            if (iterForma.next().getIdForma() == nom.getIdForma()) {
                break;
            }
        }
        formaCB.getSelectionModel().select(i);
    }

    /**
     * Validari pentru 4 campuri TF
     */
    private boolean valideazaCodBare() {
        if (codBareTF.getText().isEmpty()) {
            return true;
        }
        try {
            Long.parseUnsignedLong(codBareTF.getText());
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean valideazaPretImpus() {
        if(pretImpusTF.getText().isEmpty()) {
            return true;
        }
        try {
            if (Float.parseFloat(pretImpusTF.getText()) > 99999999.99) {
                return false;
            }
            return Float.parseFloat(pretImpusTF.getText()) >= 0;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean valideazaAdaos() {
        if(adaosTF.getText().isEmpty()) {
            return true;
        }
        try {
            if (Float.parseFloat(adaosTF.getText()) > 999.99) {
                return false;
            }
            return Float.parseFloat(adaosTF.getText()) >= 0;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean valideazaDenumireProdus() {
        if (denProdusTF.getText().trim().isEmpty() || denProdusTF.getText().length() > 230) {
            return false;
        }
        return true;
    }

    /**
     * select * FROM dci
     * returneaza un ObservableList cu toate DCI pentru a fi folosit in combobox
     */
    public ObservableList<Dci> getAllDci() throws SQLException {
        ObservableList<Dci> dciObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_DCI);

            while(resultSet.next()) {
                SimpleLongProperty idDci = new SimpleLongProperty(resultSet.getLong("id_dci"));
                SimpleStringProperty codDci = new SimpleStringProperty(resultSet.getString("cod_dci"));
                SimpleStringProperty denDci = new SimpleStringProperty(resultSet.getString("den_dci"));
                SimpleStringProperty grupaAtc = new SimpleStringProperty(resultSet.getString("grupa_atc"));
                SimpleStringProperty observatii = new SimpleStringProperty(resultSet.getString("observatii"));

                Dci newDci = new Dci(idDci, codDci, denDci, grupaAtc, observatii);
                dciObservableList.add(newDci);
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

        return dciObservableList;
    }

    /**
     * select * FROM forma
     * returneaza un ObservableList cu toate FORMA pentru a fi folosit in combobox
     */
    public ObservableList<Forma> getAllForma() throws SQLException {
        ObservableList<Forma> formaObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_FORMA);

            while (resultSet.next()) {
                SimpleLongProperty idForma = new SimpleLongProperty(resultSet.getLong("id_forma"));
                SimpleStringProperty numeForma = new SimpleStringProperty(resultSet.getString("nume_forma"));
                SimpleStringProperty prescurtare = new SimpleStringProperty(resultSet.getString("prescurtare"));

                Forma forma = new Forma(idForma, numeForma, prescurtare);
                formaObservableList.add(forma);
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

        return formaObservableList;
    }

    /**
     * select * FROM producatori
     * returneaza un ObservableList cu toti producatorii pentru a fi folosit in combobox
     */
    public ObservableList<Producatori> getAllProducatori() throws SQLException {
        ObservableList<Producatori> producatoriObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_PRODUCATORI);

            while (resultSet.next()) {
                SimpleLongProperty idProducator = new SimpleLongProperty(resultSet.getLong("id_producator"));
                SimpleStringProperty numeProducator = new SimpleStringProperty(resultSet.getString("nume_producator"));

                Producatori producator = new Producatori(idProducator, numeProducator);
                producatoriObservableList.add(producator);
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

        return producatoriObservableList;
    }

    /**
     * select * FROM tva
     * returneaza un ObservableList cu toate TVA pentru a fi folosit in combobox
     */
    public ObservableList<Tva> getAllTva() throws SQLException {
        ObservableList<Tva> tvaObservableList = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_TVA);

            while (resultSet.next()) {
                SimpleLongProperty idTva = new SimpleLongProperty(resultSet.getLong("id_tva"));
                SimpleStringProperty tipTva = new SimpleStringProperty(resultSet.getString("tip_tva"));
                SimpleIntegerProperty procentTva = new SimpleIntegerProperty(resultSet.getInt("procent_tva"));

                Tva tva = new Tva(idTva, tipTva, procentTva);
                tvaObservableList.add(tva);
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

        return tvaObservableList;
    }
}
