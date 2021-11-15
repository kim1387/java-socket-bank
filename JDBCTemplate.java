package server3;


import java.sql.*;

public class JDBCTemplate {


    private static Connection conn = null;
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost/bank?serverTimezone=UTC";
        String userName = "root";
        String password = "bubbler0217";

        if(conn==null) {
            try {
                conn = DriverManager.getConnection(url, userName, password);
                System.out.println("conn = "+conn);
                //conn.setAutoCommit(false);
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void close(Statement stmt) {
        try {
            if(stmt!= null && stmt.isClosed())
                stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rset) {
        try {
            if(rset!= null && rset.isClosed())
                rset.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}