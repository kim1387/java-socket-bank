package server3;

public class Account {
    private int accountId;//계좌번호
    private String customerId;//아이디
    private String customerPW;//비밀번호
    private int balance;//잔액
    static int accountId_maker = 10000;// 계좌번호 만들때 쓰는 accountId_maker

    public Account() {

    }

    public Account(int accountId, String customerId, String customerPW, int balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.customerPW = customerPW;
        this.balance = balance;
    }


    //getter setter

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerPW(String customerPW) {
        this.customerPW = customerPW;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerPW() {
        return customerPW;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getBalance() {
        return balance;
    }

}