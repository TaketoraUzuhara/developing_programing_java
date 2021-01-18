import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MsgClient extends JFrame implements ActionListener{
    public static final String HOST = "localhost";
    public static final int PORT = 51001;

    JTextField tf = new JTextField("", 40);
    JTextArea ta = new JTextArea(20, 50);
    JButton bs = new JButton("Submit");
    private Container c;
    PrintWriter pw;

    public MsgClient(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Messanger");
        setSize(600, 400);
        c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(tf);
        c.add(bs);
        bs.addActionListener(this);
        c.add(ta);
        ta.setEditable(false);

        Socket socket = null;
        try{
            socket = new Socket(HOST, PORT);
        }catch(IOException e){
            System.err.println("Error occured: "+e);
            System.exit(0);
        }

        MsgRecvThread mrt = new MsgRecvThread(socket);
        mrt.start();
    }

    public class MsgRecvThread extends Thread{
        Socket socket;

        public MsgRecvThread(Socket s){
            socket = s;
        }

        public void run (){
            try{
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                pw = new PrintWriter(socket.getOutputStream(), true);
                while(true){
                    String inputLine = br.readLine();
                    if(inputLine != null){
                        ta.append(inputLine + "\n");
                    }else{
                        break;
                    }
                }
                socket.close();
            }catch(IOException e){
                System.err.println("Error occured: "+e);
                System.exit(0);
            }
        }
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand() == "Submit"){
            String msg = tf.getText();
            tf.setText("");
            if(msg.length() > 0){
                pw.println(msg);
                pw.flush();
            }
        }
    }

    public static void main(String[] args){
        MsgClient mc = new MsgClient();
        mc.setVisible(true);
    }
}
