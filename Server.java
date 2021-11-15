package server3;

import java.io.*;
import java.util.*;
import java.net.*;


public class Server
{

    public static void main(String[] args) throws IOException
    {
        ServerSocket connect_socket = new ServerSocket(9381);

        Socket service_socket; // service socket


        // ��� Ŭ���̾�Ʈ ��û�� �ޱ� ���� ���� ����
        while (true)
        {

            System.out.println("���� ����");
            // ��û�� accept
            service_socket = connect_socket.accept(); // Socket[addr=/127.0.0.1,port=59096,localport=9381] �̷��� ������ s�� ����

            System.out.println("[Log]���ο� Ŭ���̾�Ʈ ��û: " + service_socket);

            // ������ ���� ��Ʈ�� ����
            InputStream dataInputStream = service_socket.getInputStream();
            OutputStream outputStream = service_socket.getOutputStream();

            System.out.println("[Log]���ο� Ŭ���̾�Ʈ �ڵ鷯 ������");

            ClientHandler clientHandler = new ClientHandler(service_socket, dataInputStream, outputStream);

            Thread thread = new Thread(clientHandler);


            thread.start();

        }
    }
}
class ClientHandler implements Runnable
{
    final InputStream dataInputStream;
    final OutputStream outputStream;
    Socket socket;
    Account account;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket socket, InputStream dataInputStream, OutputStream outputStream) {
        this.dataInputStream = dataInputStream;
        this.outputStream = outputStream;
        this.socket = socket;
        this.isloggedin=false;
    }

    @Override
    public void run() {

        String received;
        while (true)
        {
            try
            {
                // ���� �д� �κ�
                byte[] received_byte = new byte[50];
                AccountManager accountManager = new AccountManager();
                dataInputStream.read(received_byte);
                received = new String(received_byte);

                System.out.println(received);



                // ���۹��� ���ڿ��� ' ' ������ �ɰ��� ����
                StringTokenizer stringTokenizer = new StringTokenizer(received, " ");
                String cmd = null; // ��ɾ�
                if (stringTokenizer.hasMoreTokens()) cmd = stringTokenizer.nextToken();
                if(cmd.equals("exit")){
                    this.isloggedin=false;
                    outputStream.write("bank exit".getBytes());
                    socket.close();
                    continue;
                }
                if (!isloggedin){
                    switch(cmd){
                        //login command
                        case "login":
                            String id = null;
                            String PW = null;

                            if (stringTokenizer.hasMoreTokens()) id = stringTokenizer.nextToken();
                            if (stringTokenizer.hasMoreTokens()) PW = stringTokenizer.nextToken();
                            Account login_account = accountManager.login(id,PW,this.outputStream);
                            if (login_account!=null){//String id, String PW, OutputStream outputStream
                                this.account =login_account;
                                System.out.println("�α��� ����");
                                this.isloggedin = true;
                            }
                            break;

                        case "signup":
                            String signupId = null;
                            String signupPW = null;
                            if (stringTokenizer.hasMoreTokens()) signupId = stringTokenizer.nextToken();
                            if (stringTokenizer.hasMoreTokens()) signupPW = stringTokenizer.nextToken();
                            System.out.println(signupId+"ȸ�������� �����մϴ�.");
                            accountManager.signup(signupId,signupPW,outputStream);
                            break;
                        case "exit":
                            break;

                        default:
                            outputStream.write("login first".getBytes());

                            break;
                    }
                }else {
                    switch(cmd){
                        case "logout":
                            this.isloggedin=false;
                            outputStream.write("bank logout".getBytes());
                            break;

                        case "withdraw":
                            int withdraw_money = 0;
                            if (stringTokenizer.hasMoreTokens()) withdraw_money = Integer.parseInt(stringTokenizer.nextToken());
                            if (this.account!=null){
                                accountManager.withdraw(withdraw_money,this.account,outputStream);
                            }
                            break;

                        case "deposit":
                            int deposit_money = 0;
                            if (stringTokenizer.hasMoreTokens()) deposit_money = Integer.parseInt(stringTokenizer.nextToken());

                            if (this.account!=null){
                                accountManager.deposit(deposit_money,this.account,outputStream);
                            }else{
                                outputStream.write("Unknown account".getBytes());
                            }
                            break;

                        case "checkAccountId":
                            System.out.println("���� ��ȸ");
                            outputStream.write(String.valueOf(account.getAccountId()).getBytes());
                            break;

                        case "checkBalance":
                            accountManager.check_balance(account,outputStream);
                            break;

                        case "sendToMoney":
                            int accountId = 0;
                            int money = 0;

                            if (stringTokenizer.hasMoreTokens()) accountId = Integer.parseInt(stringTokenizer.nextToken());
                            if (stringTokenizer.hasMoreTokens()) money = Integer.parseInt(stringTokenizer.nextToken());
                            accountManager.sendMoney(accountId,money,account,outputStream);
                            break;

                        default:
                            outputStream.write("Unknown Commend".getBytes());
                            break;
                    }
                }

            } catch (SocketException e) {
                //e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try
        {
            this.dataInputStream.close();
            this.outputStream.close();
        }
        catch (SocketException e){
            System.out.println("Ŭ���̾�Ʈ ���� ����");
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

}
