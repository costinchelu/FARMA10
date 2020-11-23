package farma.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil implements DBConstants{

    private static Connection conn = null;

    public static void dbConnect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Nu a fost gasit driver-ul OJDBC");
            e.printStackTrace();
            throw e;
        }

        System.out.println("Oracle JDBC Driver inregistrat.");

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
        } catch (SQLException e) {
            System.out.println("Conexiunea la baza de date a esuat " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            throw e;
        }
    }
}
