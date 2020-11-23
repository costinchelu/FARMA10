package farma.util.testConnection;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OjdbcLocalTest {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "cheluc_id";
    public static final String QUERY_OPERATORI = "SELECT nume_operator FROM LIC.operatori WHERE id_operator = 1";

    public static void main(String[] args) throws SQLException {
        OracleDataSource ods = new OracleDataSource();
        ods.setURL(DB_URL); // jdbc:oracle:thin@//[hostname]:[port]/[DB service name]
        ods.setUser(USERNAME); // [username]
        ods.setPassword(PASSWORD); // [password]
        Connection conn = ods.getConnection();

//        PreparedStatement stmt = conn.prepareStatement("SELECT 'Hello World!' FROM dual");
        PreparedStatement stmt = conn.prepareStatement(QUERY_OPERATORI);

        ResultSet rslt = stmt.executeQuery();
        while (rslt.next()) {
            System.out.println(rslt.getString(1));
        }

        rslt.close();
        stmt.close();
        conn.close();
    }
}
