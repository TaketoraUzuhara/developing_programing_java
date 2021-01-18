import java.util.*;
import java.io.*;
import java.net.*;

public class MsgServer{
    public static final int PORT = 51001;
    private static final int MAX_CON = 5;
    private static int n = 0;
    private static Socket[] sk = new Socket[MAX_CON];
    private static InputStreamReader[] isr = new InputStreamReader[MAX_CON];
    private static BufferedReader[] br = new BufferedReader[MAX_CON];
    private static PrintWriter[] pw = new PrintWriter[MAX_CON];
    private static ClientProc[] client = new ClientProc[MAX_CON];
    private static boolean[] con = new boolean[MAX_CON];

    public static void SetConnectionStatus(int n, boolean b){
        con[n] = b;
    }

    public static void broadcast(String str){
        for(int i=0; i<n; i++){
            if(con[i] == true){
                pw[i].println(str);
                pw[i].flush();
            }
        }
    }

    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("Waiting for client's connection...");
            while(true){
                if(n < MAX_CON){
                    try{
                        sk[n] = ss.accept();
                        System.out.println("Connection Request has occured from client!: #"+n);
                        con[n] = true;
                        isr[n] = new InputStreamReader(sk[n].getInputStream());
                        br[n] = new BufferedReader(isr[n]);
                        pw[n] = new PrintWriter(sk[n].getOutputStream(), true);
                        client[n] = new ClientProc(n, sk[n], isr[n], br[n], pw[n]);
                        client[n].start();
                        n++;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Over capacity");
                    break;
                }
            }
            ss.close();
        } catch(Exception e){
            System.out.println("I coudln't make socket...");
        }
    }
}

class ClientProc extends Thread{
    private int num;
    private Socket sk;
    private InputStreamReader isr;
    private BufferedReader br;
    private PrintWriter pw;

    public ClientProc(int n, Socket s, InputStreamReader i, BufferedReader b, PrintWriter p){
        num = n;
        sk = s;
        isr = i;
        br = b;
        pw = p;
    }

    public void run (){
        try {
            while(true){
                String str = br.readLine();
                if(str != null){
                    MsgServer.broadcast("#"+num+">"+str);
                }
            }
        }catch(Exception e){
            System.out.println("Disconnecting...: #"+num);
            MsgServer.SetConnectionStatus(num, false);
        }
    }
}