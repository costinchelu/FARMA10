package farma.util;

import java.io.StringReader;

public interface DBConstants {

    String JAVA_SDK_VERSION = System.getProperty("java.version");
    String JAVA_FX_VERSION = System.getProperty("javafx.version");

    String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	String DB_URL = "jdbc:oracle:thin:@localhost:1521/XE";
    String DB_USERNAME = "LIC";
    String DB_PASS = "Costin";

    String DCI_SELECT_ALL = "SELECT * FROM dci ORDER BY den_dci";
    String DCI_INSERT_INTO = "INSERT INTO dci VALUES (dci_seq.NEXTVAL, ?, ?, ?, ?)";
    String DCI_DELETE = "DELETE FROM dci WHERE id_dci = ?";
    String DCI_UPDATE = "UPDATE dci SET den_dci = ?, cod_dci = ?, grupa_atc = ?, observatii = ? WHERE id_dci = ?";

    String NOMENCLATOR_INSERT_INTO = "INSERT INTO nomenclator VALUES "
            + "(NOMENCLATOR_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String NOMENCLATOR_DELETE = "DELETE FROM nomenclator WHERE id_nomenclator = ?";
    String NOMENCLATOR_UPDATE =
            "UPDATE nomenclator SET denumire_prod = ?, cod_bare = ?, concentratie = ?, "
            + "adaos = ?, ambalare = ?, pret_impus = ?, observatii = ?, id_forma = ?, "
                    + "id_tva = ?, id_dci = ?, id_producator = ? WHERE id_nomenclator = ?";

    String OPERATOR_INSERT = "INSERT INTO operatori (id_operator, nume_operator, prenume_operator, password, salt) " +
            "VALUES " +
            "(OPERATORI_SEQ.nextval, ?, ?, ?, ?)";

    String OPERATOR_SELECT_PASS = "SELECT password, salt FROM operatori WHERE id_operator = ?";

    String OPERATOR_UPDATE_PASS = "UPDATE operatori SET password = ?, salt = ? WHERE id_operator = ?";

    String SELECT_ALL_NOMENCLATOR_EXT = "SELECT nomenclator.id_nomenclator, nomenclator.denumire_prod, nomenclator.cod_bare, "
            + "nomenclator.concentratie, nomenclator.adaos, nomenclator.ambalare, nomenclator.pret_impus, "
            + "nomenclator.observatii, nomenclator.id_forma, nomenclator.id_tva, nomenclator.id_dci, "
            + "nomenclator.id_producator, forma.nume_forma, tva.procent_tva, dci.den_dci, "
            + "producatori.nume_producator  FROM nomenclator LEFT OUTER JOIN forma ON nomenclator.id_forma = forma.id_forma "
            + "LEFT OUTER JOIN tva ON nomenclator.id_tva = tva.id_tva LEFT OUTER JOIN dci ON nomenclator.id_dci = dci.id_dci "
            + "LEFT OUTER JOIN producatori ON nomenclator.id_producator = producatori.id_producator "
            + "ORDER BY nomenclator.denumire_prod";

    String SELECT_ID_NOMENCLATOR_FOLOSITE = "SELECT DISTINCT id_nomenclator FROM intrari";
    String SELECT_ID_PARTENER_FOLOSITE = "SELECT DISTINCT id_partener FROM document";
    String SELECT_ID_DCI_FOLOSITE = "SELECT DISTINCT id_dci FROM nomenclator";

    String SELECT_ALL_PARTENERI = "SELECT * FROM parteneri ORDER BY nume_partener";
    String SELECT_ALL_OPERATORI = "SELECT * FROM operatori WHERE dezactivat != 1 ORDER BY nume_operator";
    String SELECT_ALL_OPERATORI_F_ADMIN = "SELECT * FROM operatori WHERE id_operator > 12 AND dezactivat != 1 ORDER BY nume_operator";
    String DEZACTIVEAZA_OPERATOR = "UPDATE operatori SET dezactivat = 1 WHERE id_operator = ?";
    String SELECT_ALL_NOMENCLATOR = "SELECT * FROM nomenclator ORDER BY denumire_prod";
    String SELECT_ALL_TVA = "SELECT * FROM tva ORDER BY procent_tva";
    String SELECT_ALL_PRODUCATORI = "SELECT * FROM producatori ORDER BY nume_producator";
    String SELECT_ALL_FORMA = "SELECT * FROM forma ORDER BY nume_forma";
    String SELECT_ALL_DCI = "SELECT * FROM dci ORDER BY den_dci";

    String DOCUMENT_INSERT_INTO = "INSERT INTO document VALUES(DOCUMENT_SEQ.nextval, ?, TO_DATE(?, 'YYYY.MM.DD'), "
            + "TO_DATE(?, 'YYYY.MM.DD'), ?, ?, ?, ?, ?)";

    String INTRARI_STOC_INSERT = "BEGIN INSERT INTO intrari ( id_intrare, id_nomenclator, id_document, lot, "
            + "bbd, cantitate, pret_achizitie, discount, observatii ) "
            + "VALUES ( INTRARI_SEQ.nextval, ?, ?, ?, TO_DATE(?, 'YYYY.MM.DD'), ?, ?, ?, ? );"
            + "INSERT INTO stoc(id_stoc, id_intrare, pret, cantitate) "
            + "VALUES(STOC_SEQ.nextval, INTRARI_SEQ.currval, ?, ?); END;";

    String SELECT_ALL_STOC_EXT = "SELECT stoc.id_stoc, stoc.id_intrare, stoc.pret, stoc.cantitate, intrari.lot, "
            + "intrari.bbd, nomenclator.denumire_prod, nomenclator.cod_bare, nomenclator.concentratie, "
            + "nomenclator.adaos, nomenclator.pret_impus, producatori.nume_producator, forma.nume_forma, tva.procent_tva "
            + "FROM stoc LEFT OUTER JOIN intrari ON stoc.id_intrare = intrari.id_intrare "
            + "LEFT OUTER JOIN nomenclator ON nomenclator.id_nomenclator = intrari.id_nomenclator "
            + "LEFT OUTER JOIN producatori ON nomenclator.id_producator = producatori.id_producator "
            + "LEFT OUTER JOIN forma ON nomenclator.id_forma = forma.id_forma "
            + "LEFT OUTER JOIN tva ON nomenclator.id_tva = tva.id_tva "
            + "WHERE stoc.cantitate > 0 ORDER BY nomenclator.denumire_prod";

    int SCADE_DIN_STOC = 1;
    int ADAUGA_IN_STOC = -1;
    String UPDATE_STOC = "UPDATE stoc SET cantitate = cantitate - (? * ?) WHERE id_stoc = ?";
    String IESIRI_INSERT = "INSERT INTO iesiri VALUES (IESIRI_SEQ.NEXTVAL, ?, ?, ?)";

    String PARTENERI_SELECT_ALL = "SELECT * FROM parteneri ORDER BY nume_partener";
    String PARTENERI_INSERT_INTO = "INSERT INTO parteneri VALUES (parteneri_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
    String PARTENERI_DELETE = "DELETE FROM parteneri WHERE id_partener = ?";
    String PARTENERI_UPDATE = "UPDATE parteneri SET nume_partener = ?, adresa = ?, telefon = ?, email = ?, " +
            "cui = ?, cont = ? WHERE id_partener = ?";

    String SELECT_FACTURI_INTRARE = "SELECT intrari.id_document, document.nr_document, document.data_emitere, document.data_operare, " +
            "document.termen_plata, document.id_operatie, document.id_partener, document.id_operator, " +
            "document.observatii, parteneri.nume_partener, operatori.nume_operator || ' ' " +
            "|| operatori.prenume_operator AS nume_operator, SUM(intrari.cantitate * intrari.pret_achizitie) " +
            "AS pret_produse, COUNT(intrari.id_document) AS nr_produse FROM intrari " +
            "LEFT OUTER JOIN document ON document.id_document = intrari.id_document " +
            "LEFT OUTER JOIN parteneri ON document.id_partener = parteneri.id_partener " +
            "LEFT OUTER JOIN operatori ON document.id_operator = operatori.id_operator " +
            "GROUP BY intrari.id_document, document.nr_document, document.data_emitere, " +
            "document.data_operare, document.termen_plata, document.id_operatie, " +
            "document.id_partener, document.id_operator, document.observatii, parteneri.nume_partener, " +
            "operatori.nume_operator || ' ' || operatori.prenume_operator ORDER BY document.data_emitere";

    String SELECT_FACTURI_IESIRE = "SELECT iesiri.id_document, document.nr_document, " +
            "document.data_emitere, document.data_operare, document.termen_plata, document.id_operatie, " +
            "document.id_partener, document.id_operator, document.observatii, parteneri.nume_partener, " +
            "operatori.nume_operator || ' ' || operatori.prenume_operator AS nume_operator, " +
            "SUM(iesiri.cantitate * stoc.pret) AS pret_produse, COUNT(iesiri.id_document) AS nr_produse " +
            "FROM iesiri LEFT OUTER JOIN stoc ON iesiri.id_stoc = stoc.id_stoc " +
            "LEFT OUTER JOIN document ON document.id_document = iesiri.id_document " +
            "LEFT OUTER JOIN parteneri ON document.id_partener = parteneri.id_partener " +
            "LEFT OUTER JOIN operatori ON document.id_operator = operatori.id_operator " +
            "GROUP BY iesiri.id_document, document.nr_document, document.data_emitere, " +
            "document.data_operare, document.termen_plata, document.id_operatie, document.id_partener, " +
            "document.id_operator, document.observatii, parteneri.nume_partener, operatori.nume_operator " +
            "|| ' ' || operatori.prenume_operator ORDER BY document.data_emitere";

    String SELECT_STOC_OPERATII = "SELECT stoc.id_stoc, stoc.id_intrare, " +
            "stoc.pret, stoc.cantitate, intrari.lot, intrari.bbd, " +
            "nomenclator.denumire_prod, nomenclator.cod_bare, nomenclator.concentratie, " +
            "nomenclator.adaos, producatori.nume_producator, tva.procent_tva, document.id_document, " +
            "document.nr_document, document.data_operare, parteneri.nume_partener, " +
            "dci.den_dci FROM stoc LEFT OUTER JOIN intrari ON stoc.id_intrare = intrari.id_intrare " +
            "LEFT OUTER JOIN document ON intrari.id_document = document.id_document " +
            "LEFT OUTER JOIN parteneri ON document.id_partener = parteneri.id_partener " +
            "LEFT OUTER JOIN nomenclator ON intrari.id_nomenclator = nomenclator.id_nomenclator " +
            "LEFT OUTER JOIN producatori ON nomenclator.id_producator = producatori.id_producator " +
            "LEFT OUTER JOIN tva ON nomenclator.id_tva = tva.id_tva " +
            "LEFT OUTER JOIN dci ON nomenclator.id_dci = dci.id_dci " +
            "WHERE stoc.cantitate > 0 ORDER BY nomenclator.denumire_prod";


    String SELECT_IESIRI_L_CURENTA = "SELECT SUM(iesiri.cantitate * stoc.pret) FROM iesiri " +
            "LEFT OUTER JOIN stoc ON stoc.id_stoc = iesiri.id_stoc WHERE iesiri.id_document IN ( " +
            "SELECT id_document FROM document WHERE data_emitere > trunc(sysdate, 'mm') " +
            "AND id_operatie = 2)";

    String SELECT_INTRARI_L_CURENTA = "SELECT SUM(intrari.cantitate * intrari.pret_achizitie) " +
            "FROM intrari LEFT OUTER JOIN stoc ON stoc.id_intrare = intrari.id_intrare " +
            "WHERE intrari.id_document IN (SELECT id_document FROM document " +
            " WHERE data_emitere > trunc(sysdate, 'mm') AND id_operatie = 1)";

    String SELECT_IESIRI_L_PRECEDENTA = "SELECT SUM(iesiri.cantitate * stoc.pret) " +
            "FROM iesiri LEFT OUTER JOIN stoc ON stoc.id_stoc = iesiri.id_stoc " +
            "WHERE iesiri.id_document IN (SELECT id_document FROM document " +
            "WHERE data_emitere BETWEEN trunc(add_months(sysdate,-1), 'mm') AND trunc(sysdate, 'mm') " +
            "AND id_operatie = 2)";

    String SELECT_INTRARI_L_PRECEDENTA = "SELECT SUM(intrari.cantitate * intrari.pret_achizitie) " +
            "FROM intrari LEFT OUTER JOIN stoc ON stoc.id_intrare = intrari.id_intrare " +
            "WHERE intrari.id_document IN ( SELECT id_document FROM document WHERE " +
            "data_emitere between trunc(add_months(sysdate,-1), 'mm') and trunc(sysdate, 'mm') " +
            "AND id_operatie = 1)";

    String SELECT_IESIRI_ULTIMELE_L = "SELECT SUM(iesiri.cantitate * stoc.pret) " +
            "FROM iesiri LEFT OUTER JOIN stoc ON stoc.id_stoc = iesiri.id_stoc " +
            "WHERE iesiri.id_document IN (SELECT id_document FROM document " +
            "WHERE data_emitere > trunc(add_months(sysdate,-1), 'mm') " +
            "AND id_operatie = 2)";

    String SELECT_INTRARI_ULTIMELE_L = "SELECT SUM(intrari.cantitate * intrari.pret_achizitie) " +
            "FROM intrari LEFT OUTER JOIN stoc ON stoc.id_intrare = intrari.id_intrare " +
            "WHERE intrari.id_document IN ( SELECT id_document FROM document WHERE " +
            "data_emitere > trunc(add_months(sysdate,-1), 'mm') " +
            "AND id_operatie = 1)";

    String SELECT_INTRARI_T = "SELECT parteneri.nume_partener, " +
            "SUM(intrari.cantitate * intrari.pret_achizitie) AS valoare FROM intrari " +
            "LEFT OUTER JOIN stoc ON stoc.id_intrare = intrari.id_intrare " +
            "LEFT OUTER JOIN document ON intrari.id_document = document.id_document " +
            "LEFT OUTER JOIN parteneri ON document.id_partener = parteneri.id_partener " +
            "WHERE intrari.id_document IN (SELECT id_document FROM document " +
            "WHERE data_emitere > trunc(add_months(sysdate, - 3), 'mm') " +
            "AND id_operatie = 1) GROUP BY parteneri.nume_partener " +
            "ORDER BY valoare";

    String SELECT_IESIRI_T = "SELECT parteneri.nume_partener, " +
            "SUM(iesiri.cantitate * stoc.pret) AS valoare FROM iesiri " +
            "LEFT OUTER JOIN stoc ON stoc.id_stoc = iesiri.id_stoc " +
            "LEFT OUTER JOIN document ON iesiri.id_document = document.id_document " +
            "LEFT OUTER JOIN parteneri ON document.id_partener = parteneri.id_partener " +
            "WHERE iesiri.id_document IN (SELECT id_document FROM document WHERE " +
            "data_emitere > trunc(add_months(sysdate, - 3), 'mm') AND id_operatie = 2 " +
            ") GROUP BY parteneri.nume_partener ORDER BY valoare";

    String DETALII_F_DOCUMENT = "SELECT  document.nr_document AS nr_document, " +
            "document.data_emitere AS data_document, operatii.id_operatie AS operatie, " +
            "operatori.nume_operator || ' ' || operatori.prenume_operator AS nume_operator, " +
            "parteneri.nume_partener AS nume_client, parteneri.adresa AS adresa_client, " +
            "parteneri.telefon AS telefon_client, parteneri.email AS email_client, " +
            "parteneri.cui AS cui_client, parteneri.cont AS cont_client " +
            "FROM document inner join operatii on operatii.id_operatie = document.id_operatie " +
            "inner join operatori on operatori.id_operator = document.id_operator " +
            "inner join parteneri on parteneri.id_partener = document.id_partener " +
            "WHERE document.id_document = ?";

    String DETALII_F_IESIRI = "SELECT intrari.pret_achizitie AS pret_achizitie, intrari.bbd AS bbd, " +
            "intrari.lot AS lot, stoc.pret AS pret_vanzare, iesiri.cantitate AS cantitate, " +
            "nomenclator.denumire_prod AS denumire_produs, nomenclator.concentratie AS concentratie, " +
            "nomenclator.ambalare AS ambalare, producatori.nume_producator AS producator, " +
            "forma.nume_forma AS forma, tva.procent_tva AS procent_tva, dci.den_dci AS dci " +
            "FROM document full outer join iesiri on document.id_document = iesiri.id_document " +
            "full outer join stoc on iesiri.id_stoc = stoc.id_stoc " +
            "full outer join intrari on stoc.id_intrare = intrari.id_intrare " +
            "full outer join nomenclator on intrari.id_nomenclator = nomenclator.id_nomenclator " +
            "full outer join producatori on nomenclator.id_producator = producatori.id_producator " +
            "full outer join forma on nomenclator.id_forma = forma.id_forma " +
            "full outer join tva on nomenclator.id_tva = tva.id_tva " +
            "full outer join dci on nomenclator.id_dci = dci.id_dci " +
            "WHERE document.id_document = ? ORDER BY nomenclator.denumire_prod";

    String DETALII_F_INTRARI = "SELECT intrari.pret_achizitie AS pret_achizitie, intrari.bbd AS bbd, " +
            "intrari.lot AS lot, intrari.cantitate AS cantitate, stoc.pret AS pret_vanzare, " +
            "nomenclator.denumire_prod AS denumire_produs, nomenclator.concentratie AS concentratie, " +
            "nomenclator.ambalare AS ambalare, producatori.nume_producator AS producator, " +
            "forma.nume_forma AS forma, tva.procent_tva AS procent_tva, " +
            "dci.den_dci AS dci FROM document right outer join intrari on document.id_document = intrari.id_document " +
            "left outer join stoc on intrari.id_intrare = stoc.id_intrare " +
            "right outer join nomenclator on intrari.id_nomenclator = nomenclator.id_nomenclator " +
            "right outer join producatori on nomenclator.id_producator = producatori.id_producator " +
            "right outer join forma on nomenclator.id_forma = forma.id_forma " +
            "right outer join tva on nomenclator.id_tva = tva.id_tva " +
            "right outer join dci on nomenclator.id_dci = dci.id_dci " +
            "WHERE document.id_document = ? ORDER BY nomenclator.denumire_prod";

}
