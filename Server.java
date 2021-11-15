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


        // 계속 클라이언트 요청을 받기 위해 무한 루프
        while (true)
        {

            System.out.println("서버 열림");
            // 요청을 accept
            service_socket = connect_socket.accept(); // Socket[addr=/127.0.0.1,port=59096,localport=9381] 이러한 형식이 s에 저장

            System.out.println("[Log]새로운 클라이언트 요청: " + service_socket);

            // 데이터 전송 스트림 생성
            InputStream dataInputStream = service_socket.getInputStream();
            OutputStream outputStream = service_socket.getOutputStream();

            System.out.println("[Log]새로운 클라이언트 핸들러 생성중");

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
                // 값을 읽는 부분
                byte[] received_byte = new byte[50];
                AccountManager accountManager = new AccountManager();
                dataInputStream.read(received_byte);
                received = new String(received_byte);

                System.out.println(received);



                // 전송받은 문자열을 ' ' 단위로 쪼개서 저장
                StringTokenizer stringTokenizer = new StringTokenizer(received, " ");
                String cmd = null; // 명령어
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
                                System.out.println("로그인 성공");
                                this.isloggedin = true;
                            }
                            break;

                        case "signup":
                            String signupId = null;
                            String signupPW = null;
                            if (stringTokenizer.hasMoreTokens()) signupId = stringTokenizer.nextToken();
                            if (stringTokenizer.hasMoreTokens()) signupPW = stringTokenizer.nextToken();
                            System.out.println(signupId+"회원가입을 진행합니다.");
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
                            System.out.println("계좌 조회");
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
            System.out.println("클라이언트 연결 종료");
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

}
