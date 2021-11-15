package server3;


import java.io.OutputStream;
import java.io.IOException;
import java.sql.Connection;


public class AccountManager{

    private Connection connection;
    AccountManager(){
        connection = JDBCTemplate.getConnection();
    }

    // 로그인
    public Account login(String id, String PW, OutputStream outputStream)throws Exception{
        try {
            AccountDao accountDao = new AccountDao();
            Account account = accountDao.login(connection,id,PW);
            if (account != null){
                outputStream.write(("success login").getBytes());

                return account;
            }else {
                outputStream.write(("failed login").getBytes());

                return null;

            }

        }catch (Exception e){
            outputStream.write(("failed login").getBytes());

            return null;

        }
    }

    // 회원가입
    public void signup(String ID, String PW, OutputStream outputStream) throws IOException {
        try {

            Account account = new Account(Account.accountId_maker,ID,PW,0);
            AccountDao accountDao = new AccountDao();
            int rowcount = accountDao.solveRowCount(connection);
            if (accountDao.checkCustomerID(connection,ID)){
                outputStream.write(("fail creation account caused by Duplicate ID").getBytes());

                return;
            }
            accountDao.signupAccount(connection,account,rowcount);
            outputStream.write(("success creation account").getBytes());


        }
        catch (Exception e){
            outputStream.write(("fail creation account").getBytes());

        }

    }

    // 인출
    public void withdraw(int money, Account account, OutputStream outputStream) throws IOException {
        AccountDao accountDao = new AccountDao();
        accountDao.updateAccountBalance(connection,account,-money);
        outputStream.write( ("withdraw "+money+" won").getBytes());
    }

    // 입금
    public void deposit(int money, Account account, OutputStream outputStream) throws IOException {
        AccountDao accountDao = new AccountDao();
        accountDao.updateAccountBalance(connection,account,money);
        outputStream.write(( "deposit "+money+" won").getBytes());
    }
    // 송금
    public void sendMoney(int receiver_account_id, int money,Account sender_account, OutputStream outputStream) throws IOException {
        try {
            AccountDao accountDao = new AccountDao();
            Account receiver_account = accountDao.selectAccount(connection,receiver_account_id);
            if (receiver_account !=null){
                accountDao.sendMoney(connection,receiver_account,sender_account,money);
                outputStream.write(("send "+money+"won to"+receiver_account.getAccountId()).getBytes());
            }else {
                outputStream.write("unknown account".getBytes());
            }
        }catch (Exception e){
            outputStream.write("fail send Money".getBytes());
        }
    }

    // 잔액 조회
    public void check_balance(Account account, OutputStream outputStream) throws IOException {
        AccountDao accountDao = new AccountDao();
        Account updated_account = accountDao.selectAccount(connection,account.getAccountId());
        outputStream.write((updated_account.getBalance()+"won left.").getBytes());
    }

    //byte 변환
}