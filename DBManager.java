package server3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	public static void main(String args[]) {

        String url = "jdbc:mysql://localhost/bank?serverTimezone=UTC";
        String userName = "root";
        String password = "";

        Connection con = null;
        Statement state = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        //데이터베이스 서버와 연결
        try {
            con = DriverManager.getConnection(url, userName, password);
            System.out.println("데이터베이스 서버와 연결되었습니다.");
            state = con.createStatement();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }

        //데이터 읽기 및 출력
        try {
            String sql = "SELECT * FROM account_info";
            assert state != null;
            rs = state.executeQuery(sql);
//            String SQL = "insert into account_info(account_id, customer_id, customer_pw, balance) values(?, ?, ?, ?)";
//            pstmt = con.prepareStatement(SQL); // 4. pstmt.set<데이터타입>(? 순서, 값) ex).setString(), .setInt ...
//            pstmt.setString(1, "12345");
//            pstmt.setString(2, "123456");
//            pstmt.setString(3, "1234567");
//            pstmt.setString(4, "0");
//            int r = pstmt.executeUpdate();

            //
            System.out.println("데이터 삽입");
            while (rs.next()) {

                int id = rs.getInt("account_id");
                String customerID = rs.getString("customer_id");
                String customerPW = rs.getString("customer_pw");
                int balance = rs.getInt("balance");

                System.out.println("id: " + id + ", 아이디: " + customerID + ", 비밀번호: " + customerPW + ", 잔액: " + balance);
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }

        //연결 해제
        try {
            assert rs != null;
            rs.close();
            state.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            //사용순서와 반대로 close 함
            if (pstmt != null) {
                try { pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (state != null)
                    state.close();
            } catch (SQLException e){
                System.err.println("SQLException : " + e.getMessage());
                e.printStackTrace();
            }
            try {
                if (con != null)
                    con.close();

            } catch (SQLException e){
                System.err.println("SQLException : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
