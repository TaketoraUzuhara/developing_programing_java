import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ReversiClient extends JPanel{
    public final static int UNIT_SIZE = 80;
    public final static String LHOST = "localhost";
    public final static int PORT = 51001;
    private Board board = new Board();
    private int turn;
    private int myColor;
    private PrintWriter pw;

    public ReversiClient(){
        this(LHOST);
    }

    public void paintComponent(Graphics g){
        String msg1 = "";
        board.paint(g, UNIT_SIZE);
        g.setColor(Color.white);
        if(turn == Stone.black){
            msg1 = "Black turn";
        }else{
            msg1 = "White turn";
        }
        if(turn == myColor){
            msg1 += " Your turn";
        }else{
            msg1 += " Enemy's turn";
        }
        if(myColor == 0){
            msg1 = "Waiting for enemy...";
        }
        String msg2 = "[Black:" + board.countStone(Stone.black) + ", White" + board.countStone(Stone.white) + "]";
        g.drawString(msg1, UNIT_SIZE/2, UNIT_SIZE/2);
        g.drawString(msg2, UNIT_SIZE/2, 19*UNIT_SIZE/2);   
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new FlowLayout());
        if(args.length > 1){
            f.getContentPane().add(new ReversiClient(args[0]));
        }else{
            f.getContentPane().add(new ReversiClient());
        }
        f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    void EndMessageDialog(){
        int black = board.countStone(Stone.black);
        int white = board.countStone(Stone.white);
        String str = "[Black:" + black + ",White" + white + "] ->";
        if(black > white){
            str += "black win!";
        }else if(white > black){
            str += "white win!";
        }else{
            str += "Draw";
        }
        JOptionPane.showMessageDialog(this, str, "Game end", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    void MessageDialog(String str){
        JOptionPane.showMessageDialog(this, str, "Informaiton", JOptionPane.INFORMATION_MESSAGE);
    }

    void changeTurn(){
        if(turn == Stone.black){
            turn = Stone.white;
        }else if(turn == Stone.white){
            turn = Stone.black;
        }
    }

    /* Connect to server*/
    public ReversiClient(String host){
        setPreferredSize(new Dimension(10*UNIT_SIZE, 10*UNIT_SIZE));
        addMouseListener(new MouseProc());
        turn = 1;
        
        Socket socket = null;
        try{
            socket = new Socket(host, PORT);
        }catch(UnknownHostException e){
            System.err.println("We cannot judge the IP address of host...");
        }catch(IOException e){
            System.err.println("Something error has occured!");
            System.exit(0);
        }
        
        MesgRecvThread mrt = new MesgRecvThread(socket);
        mrt.start();
    }

    public class MesgRecvThread extends Thread{
        Socket socket;

        public MesgRecvThread(Socket s){
            socket = s;
        }

        public void run(){
            try{
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                pw = new PrintWriter(socket.getOutputStream(), true);
                while(true){
                    String inputLine = br.readLine();
                    if(inputLine != null){
                        System.out.println(inputLine);
                        String[] inputTokens = inputLine.split(" ");
                        String cmd = inputTokens[0];
                        if (cmd.equals("set")){
                            int x = Integer.parseInt(inputTokens[1]);
                            int y = Integer.parseInt(inputTokens[2]);
                            int c = Integer.parseInt(inputTokens[3]);
                            board.setStoneAndReverse(x, y, c);
                            changeTurn();
                            repaint();
                            //check game finish
                            board.evaluateBoard();
                            if(board.num_grid_black == 0 && board.num_grid_white == 0){
                                String action_msg = "gameover " + myColor;
                                pw.println(action_msg);
                                pw.flush();
                            }
                        }else if(cmd.equals("skip")){
                            int player = Integer.parseInt(inputTokens[1]);
                            if(player == myColor){
                                MessageDialog("You pass");
                            }else{
                                MessageDialog("Enemy pass");
                            }
                            changeTurn();
                            repaint();
                        }else if(cmd.equals("msg")){
                            MessageDialog(inputTokens[1]);
                        }else if(cmd.equals("color")){
                            myColor = Integer.parseInt(inputTokens[1]);
                        }else if(cmd.equals("gamestart")){
                            MessageDialog("Stating game");
                        }else if(cmd.equals("gameover")){
                            EndMessageDialog();
                        }
                    }else{
                        break;
                    }
                }
            }catch(IOException e){
                System.err.println("Something error has occured" + e);
                System.exit(0);
            }
        }
    }

    class MouseProc extends MouseAdapter{
        public void mouseClicked(MouseEvent me){
            if(myColor == 0){
                String action_msg = "msg Pleasewaitwhileenemyhascoming " + myColor ;
                pw.println(action_msg);
                pw.flush();
            }
            if(turn != myColor) return;
            board.evaluateBoard();
            Point point = me.getPoint();
            int btn = me.getButton();
            int x = point.x / UNIT_SIZE -1;
            int y = point.y / UNIT_SIZE -1;
            if (!board.isOnBoard(x, y)) return;
            if(myColor == Stone.black && board.num_grid_black == 0 ||
            myColor == Stone.white && board.num_grid_white == 0){
                String action_msg = "skip " + myColor;
                pw.println(action_msg);
                pw.flush();
            }else if((myColor == Stone.black && board.eval_black[x][y] > 0) || 
            (myColor == Stone.white && board.eval_white[x][y] > 0)){
                String action_msg = "set " + x + " " + y + " " + myColor;
                pw.println(action_msg);
                pw.flush();
            }
        }
    }

    class Stone {
        public final static int black = 1;
        public final static int white = 2;
        private int obverse;
        Stone(){
            obverse = 0;
        }
    
        void setObverse(int color){
            if( color == black || color == white){
                obverse = color;
            }
            else{
                System.out.println("黒か白を設定してください");
            }
        }
        
        int getObverse(){
            return obverse;
        }
    
        void paint(Graphics g, Point p, int rad){
            if(obverse == black){
                g.setColor(Color.black);
                g.fillOval((int)p.getX(), (int)p.getY(), rad, rad);
            }else if(obverse == white){
                g.setColor(Color.white);
                g.fillOval((int)p.getX(), (int)p.getY(), rad, rad);
            }
        }
    
        void doReverse(){
            if(obverse == 1) obverse = 2;
            else if(obverse == 2) obverse = 1;
        }
    }

    class Board{
        private int[][] board = new int[8][8];
        public Stone st[][] = new Stone[8][8];
        private Point[] direction = new Point[8];
        public int num_grid_black;
        public int num_grid_white;
        public int[][] eval_black = new int[8][8];
        public int[][] eval_white = new int[8][8];
    
        Board (){
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    board[i][j] = 0;
                }
            }
            board[3][3] = 1;
            board[4][4] = 1;
            board[3][4] = 2;
            board[4][3] = 2;
    
            direction[0] = new Point(1,0);
            direction[1] = new Point(1,1);
            direction[2] = new Point(0,1);
            direction[3] = new Point(-1,1);
            direction[4] = new Point(-1,0);
            direction[5] = new Point(-1,-1);
            direction[6] = new Point(0,-1);
            direction[7] = new Point(1,-1);
        }
    
        void setStone(int x, int y, int s){
            board[x][y] = s;
        }
    
        void setStoneAndReverse(int x, int y, int s){
            board[x][y] = s;
            for(int i=0; i<8; i++){
                Point d = new Point();
                d = direction[i];
                int check = 0;
                int ls_cx = 0;
                int ls_cy = 0;
                int cx = x + d.x;
                int cy = y + d.y;
                int end_x = cx;
                int end_y = cy;
                while (isOnBoard(cx, cy) && board[cx][cy] != 0 && board[cx][cy] != s){    
                    check = board[cx][cy];
                    cx += d.x;
                    cy += d.y;
                    end_x = cx;
                    end_y = cy;
                }
                ls_cx = cx;
                ls_cy = cy;
                cx = x + d.x;
                cy = y + d.y;
                if(check>0 && isOnBoard(ls_cx, ls_cy) && board[ls_cx][ls_cy] != 0){
                    while(cx != end_x || cy != end_y){
                        board[cx][cy] = s;
                        cx += d.x;
                        cy += d.y;
                    }
                }
            }
        }
    
        boolean isOnBoard(int x, int y){
            if(x>=0 && x<=7 && y>=0 && y<=7){
                return true;
            }else{
                return false;
            }
        }
    
        void evaluateBoard(){
            num_grid_black = 0;
            num_grid_white = 0;
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    eval_black[i][j] = countReverseStone(i, j, 1);
                    if(eval_black[i][j] > 0) num_grid_black++;
                    eval_white[i][j] = countReverseStone(i, j, 2);
                    if(eval_white[i][j] > 0) num_grid_white++;
                }
            }
        }
    
        void printBoard(){
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    System.out.printf("%2d ", board[i][j]);
                }
                System.out.println("");
            }
        }
    
        void printEval(){
            System.out.println("Black(1):");
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    System.out.printf("%2d ", eval_black[i][j]);
                }
                System.out.println("");    
            }
            System.out.println("White(2):");
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    System.out.printf("%2d ", eval_white[i][j]);
                }
                System.out.println("");    
            }
        }
    
        ArrayList<Integer> getLine(int x, int y, Point d){
            ArrayList<Integer> line = new ArrayList<Integer>();
            int cx = x + d.x;
            int cy = y + d.y;
            while (isOnBoard(cx, cy) && board[cx][cy] != 0){
                line.add(board[cx][cy]);
                cx += d.x;
                cy += d.y;
            }
            return line;
        }
    
        int countReverseStone(int x, int y, int s){
            if(board[x][y] != 0) return -1;
            int cnt = 0;
            for(int d=0; d<8; d++){
                ArrayList<Integer> line = new ArrayList<Integer>();
                line = getLine(x, y, direction[d]);
                int n = 0;
                while (n < line.size() && line.get(n) != s) n++;
                if(1 <= n && n < line.size()) cnt += n;
            }
            return cnt;
        }
    
        int countStone(int s){
            int cnt = 0;
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    if(board[i][j] == s){
                        cnt = cnt + 1;
                    }
                }
            }
            return cnt;
        }
    
        void paint(Graphics g, int unit_size){
            int rad = (int)(unit_size*0.8);
            Point p = new Point();
            
            g.setColor(Color.black);
            g.fillRect(0, 0, 800, 800);
            
            g.setColor(new Color(0, 85, 0));
            g.fillRect(unit_size, unit_size, 640, 640);
    
            g.setColor(Color.black);
            for(int i=0; i<9; i++){
                g.drawLine(unit_size, (i+1)*unit_size, 720, (i+1)*unit_size);
            }
            for(int i=0; i<9; i++){
                g.drawLine((i+1)*unit_size, unit_size, (i+1)*unit_size, 720);
            }
    
            for(int i=0; i<2; i++){
                for(int j=0; j<2; j++){
                    g.fillRect((i*4+3)*unit_size-5, (j*4+3)*unit_size-5, 10, 10);
                }
            }
    
    
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    st[i][j] = new Stone();
                    if (board[i][j] == 1 || board[i][j] == 2){
                        st[i][j].setObverse(board[i][j]);
                    }
                    p.setLocation((int)((i+1)*unit_size+unit_size*0.1), (int)((j+1)*unit_size+unit_size*0.1));
                    st[i][j].paint(g, p, rad);
                }
            }
    
        }
    }

}
