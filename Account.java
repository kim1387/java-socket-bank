package server3;

public class Account {
    private int accountId;//���¹�ȣ
    private String customerId;//���̵�
    private String customerPW;//��й�ȣ
    private int balance;//�ܾ�
    static int accountId_maker = 10000;// ���¹�ȣ ���鶧 ���� accountId_maker

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