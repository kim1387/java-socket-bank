package server3;

import java.util.ArrayList;
import java.sql.*;

public class AccountDao {

    public ArrayList<Account> selectAll(Connection conn) {
        ArrayList<Account> list = null;
        Statement stmt = null; //������ ����
        ResultSet rset = null; //SELECT �� ����� �޾��� ��ü

        String sql = "SELECT * FROM ACCOUNT"; //�ڵ����� �����ݷ� �پ SQL���� ���۵ǹǷ� ������ �ʿ� ����
        try {

            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql); //sql�� ���� ��ɹ��� executeQuery()���� �����ϴ� ��
            list = new ArrayList<Account>();
            while(rset.next()) {
                Account account = new Account();
                account.setAccountId(rset.getInt("account_id")); //�÷��� �Ű������� �༭ ���� �߰�
                account.setCustomerId(rset.getString("customer_id"));
                account.setCustomerPW(rset.getString("customer_pw"));
                account.setBalance(rset.getInt("balance"));
                list.add(account); //����Ʈ�� setting �Ϸ�� Account ��ü �߰�
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(stmt);
        }
        return list;
    }


    public Account selectAccount(Connection conn,int accountID) {
        Account account = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null; //SELECT �� ����� �޾��� ��ü

        String sql = "SELECT * FROM ACCOUNT WHERE account_id =?"; //�������� ���̵� �Է� �κ� ?�� ��ü
        try {
            pstmt = conn.prepareStatement(sql); //sql���� ��� prepareStatement�� ����
            pstmt.setInt(1, accountID); //�ε�����, �� ��ġ�� ���� ��
            rset = pstmt.executeQuery(); //������ �Ű������� sql�� �޾ұ� ������ ���⼭ ���� �ʿ� ����
            if(rset.next()) {
                account = new Account();
                account.setAccountId(rset.getInt("account_id")); //�÷��� �Ű������� �༭ ���� �߰�
                account.setCustomerId(rset.getString("customer_id"));
                account.setCustomerPW(rset.getString("customer_pw"));
                account.setBalance(rset.getInt("balance"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(pstmt);
        }
        return account;
    }


    public int solveRowCount(Connection conn) {
        int result = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM account");
            if(rs.next()){
                result = rs.getInt(1);
            }
            System.out.println(result); //sql insert ��ɹ� Ȯ��
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public synchronized int signupAccount(Connection conn,Account account,int rowcount) throws SQLException {
        int result = 0;

        PreparedStatement pstmt = null;
        String sql = "INSERT INTO account VALUES(?,?,?,?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (account.getAccountId()+rowcount));
            pstmt.setString(2, account.getCustomerId());
            pstmt.setString(3, account.getCustomerPW());
            pstmt.setInt(4, account.getBalance());
            result = pstmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return result;
    }


    public synchronized void updateAccountBalance(Connection conn,Account account, int money) {
        PreparedStatement pstmt = null;

        String sql = "UPDATE account SET BALANCE=(BALANCE + ?) WHERE ACCOUNT_ID=?"; //�� ���� �Ϸķ� ��� �������
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setInt(2, account.getAccountId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }
    }
    public synchronized boolean sendMoney(Connection conn, Account receiver_account, Account sender_account, int money) {
        try {
            updateAccountBalance(conn,receiver_account,money);
            updateAccountBalance(conn,sender_account,-money);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }

    }

    public synchronized Account login(Connection conn, String inputId,String inputPW) {
        PreparedStatement pstmt = null;
        ResultSet result;
        String sql = "SELECT * FROM account WHERE customer_id = ?"; //�� ���� �Ϸķ� ��� �������
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, inputId);
            result = pstmt.executeQuery();
            if (result.next()){

                if (result.getString("customer_pw").contentEquals(inputPW)){
                    Account account = new Account();
                    account.setAccountId(result.getInt("account_id")); //�÷��� �Ű������� �༭ ���� �߰�
                    account.setCustomerId(result.getString("customer_id"));
                    account.setCustomerPW(result.getString("customer_pw"));
                    account.setBalance(result.getInt("balance"));
                    return account;
                }
                else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return null;
    }
    public boolean checkCustomerID(Connection conn, String inputId) {
        PreparedStatement pstmt = null;
        ResultSet result;
        String sql = "SELECT customer_id FROM account"; //�� ���� �Ϸķ� ��� �������
        try {
            pstmt = conn.prepareStatement(sql);
            result = pstmt.executeQuery();
            while (result.next()){
                if (result.getString("customer_id").equals(inputId)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return false;
    }

}
