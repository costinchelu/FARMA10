package farma.util.testConnection;

import java.sql.*;

public class OjdbcRemoteTest {

    private static final String DB_URL = "jdbc:oracle:thin:@37.120.250.20:1521:oracle";
    private static final String USERNAME = "CHELUC_ID";
    private static final String PASSWORD = "cheluc_id";

    public static final String QUERY_CONCEDIU = "SELECT nume_operator FROM operatori WHERE id_operator = 1";

    public static void main(String[] args) {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            System.out.println("Connection OK!");

            statement = conn.createStatement();
            resultSet = statement.executeQuery(QUERY_CONCEDIU);
            while(resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
