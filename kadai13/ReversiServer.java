import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.NotLinkException;


public class ReversiServer {
    public static final int PORT = 51001; //Port number
    public static int num_player = 0; //The current number of player
    private static int n = 0; //Final number of connections
    private static Socket[] sk = new Socket[2]; //Socket
    private static InputStreamReader[] isr = new InputStreamReader[2]; //Input

    private static BufferedReader[] br = new BufferedReader[2]; //Buffer reader
    private static PrintWriter[] pw = new PrintWriter[2]; //Output stream
    private static ClientProc[] client = new ClientProc[2]; //Thread

    // Send message for all clients
    public static void broadcast(String str){
        for(int i=0; i<n; i++){
            pw[i].println(str);
            pw[i].flush();
        }
    }

    // Send message for paticular client
    public static void sendMessage(int n, String str){
        pw[n].println(str);
        pw[n].flush();
    }

    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("Waiting for client connection...");
            while(true){
                if(n<2){
                    try{
                        sk[n] = ss.accept();
                        System.out.println("Connecting request has come from client!: #" + n);
                        isr[n] = new InputStreamReader(sk[n].getInputStream());
                        br[n] = new BufferedReader(isr[n]);
                        pw[n] = new PrintWriter(sk[n].getOutputStream(), true);
                        client[n] = new ClientProc(n, sk[n], isr[n], br[n], pw[n]);
                        client[n].start();
                        n++;
                        num_player++;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Over capacity!");
                    sendMessage(0, "color 1"); //First connected player become black
                    sendMessage(1, "color 2"); //Second connected player become white
                    broadcast("Game start");
                    break;
                }
                if(num_player < n){
                    System.out.println("Some client disconnect...");
                    System.exit(0);
                }
            }
            ss.close();
        }catch(Exception e){
            System.out.println("Sorry, We cannot make new socket.");
        }
    }
}

//Thread for each client
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
            while (true){
                String str = br.readLine();
                if(str != null){
                    ReversiServer.broadcast(str);
                }
            }
        }catch(Exception e){
            System.out.println("Disconnected...: #"+ num);
            ReversiServer.num_player--;
            ReversiServer.broadcast("Enemy run away!");
        }
    }
}
