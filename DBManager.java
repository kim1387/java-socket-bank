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

        //�����ͺ��̽� ������ ����
        try {
            con = DriverManager.getConnection(url, userName, password);
            System.out.println("�����ͺ��̽� ������ ����Ǿ����ϴ�.");
            state = con.createStatement();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }

        //������ �б� �� ���
        try {
            String sql = "SELECT * FROM account_info";
            assert state != null;
            rs = state.executeQuery(sql);
//            String SQL = "insert into account_info(account_id, customer_id, customer_pw, balance) values(?, ?, ?, ?)";
//            pstmt = con.prepareStatement(SQL); // 4. pstmt.set<������Ÿ��>(? ����, ��) ex).setString(), .setInt ...
//            pstmt.setString(1, "12345");
//            pstmt.setString(2, "123456");
//            pstmt.setString(3, "1234567");
//            pstmt.setString(4, "0");
//            int r = pstmt.executeUpdate();

            //
            System.out.println("������ ����");
            while (rs.next()) {

                int id = rs.getInt("account_id");
                String customerID = rs.getString("customer_id");
                String customerPW = rs.getString("customer_pw");
                int balance = rs.getInt("balance");

                System.out.println("id: " + id + ", ���̵�: " + customerID + ", ��й�ȣ: " + customerPW + ", �ܾ�: " + balance);
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }

        //���� ����
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
            //�������� �ݴ�� close ��
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
