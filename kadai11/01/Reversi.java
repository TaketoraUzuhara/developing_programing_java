//Kadai01 Taketora Uzuhara 18D8101028G

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Reversi extends JPanel{
    public final static int UNIT_SIZE = 80;
    private Board board = new Board();
    private int turn;
    private Player[] player = new Player[2];

    //init constractor
    public Reversi(){
        setPreferredSize(new Dimension(10*UNIT_SIZE, 10*UNIT_SIZE));
        addMouseListener(new MouseProc());
        player[0] = new Player(Stone.black, Player.type_human);
        player[1] = new Player(Stone.white, Player.type_computer);
        turn = Stone.black;
    }

    //Draw display
    public void paintComponent(Graphics g){
        String msg1 = "";
        board.paint(g, UNIT_SIZE);
        g.setColor(Color.white);
        if( turn == Stone.black)
            msg1 = "黒の番です";
        else
            msg1 = "白の番です";
        if(player[turn-1].getType() == Player.type_computer)
            msg1 += "(考えています)";
        String msg2 = "[黒:" + board.countStone(Stone.black) + ", 白" + board.countStone(Stone.white) + "]";
        g.drawString(msg1, UNIT_SIZE/2, UNIT_SIZE/2);
        g.drawString(msg2, UNIT_SIZE/2, 19*UNIT_SIZE/2);
    }

    //Switch on
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(new Reversi());
        f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    //Finish message 
    void EndMessageDialog(){
        int black = board.countStone(Stone.black);
        int white = board.countStone(Stone.white);
        String str = "[黒:" + black + ",　白" + white + "]で";
        if(black > white){
            str += "黒の勝ち";
        }
        else if(white > black){
            str += "白の勝ち";
        }
        else{
            str = "引き分け";
        }
        JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    //Message Dialog
    void MessageDialog(String str){
        JOptionPane.showMessageDialog(this, str, "情報", JOptionPane.INFORMATION_MESSAGE);
    }

    void changeTurn(){
        if( turn == Stone.black ) turn = Stone.white;
        else if ( turn == Stone.white )turn = Stone.black;
    }

    class MouseProc extends MouseAdapter{
        public void mouseClicked(MouseEvent me){
            Point point = me.getPoint();
            int btn = me.getButton();
            Point gp = new Point();
            gp.x = point.x / UNIT_SIZE - 1;
            gp.y = point.y / UNIT_SIZE - 1;
            if( !board.isOnBoard(gp.x, gp.y)) return;
            board.evaluateBoard();
            removeMouseListener(this);

            if(player[turn-1].getType() == Player.type_human){
                if((player[turn-1].getColor() == Stone.black && board.num_grid_black == 0) || (player[turn-1].getColor() == Stone.white && board.num_grid_white == 0)){
                    MessageDialog("あなたはパスです");
                    changeTurn();
                    repaint();
                }else if((player[turn-1].getColor() == Stone.black && board.eval_black[gp.x][gp.y] > 0) || (player[turn-1].getColor() == Stone.white && board.eval_white[gp.x][gp.y] > 0)){
                    Point nm = player[turn-1].nextMove(board, gp);
                    board.setStoneAndReverse(nm.x, nm.y, player[turn-1].getColor());
                    changeTurn();
                    repaint();
                }
                //Check end game
                if(board.num_grid_black == 0 && board.num_grid_white == 0){
                    EndMessageDialog();
                }
                if(player[turn-1].getType() == Player.type_human){
                    addMouseListener(this);
                }
            }
            if(player[turn-1].getType() == Player.type_computer){
                Thread th = new TacticsThread();
                th.start();
            }
        }
    }

    //Thread for gaming with computer
    class TacticsThread extends Thread{
        public void run(){
            try{
                Thread.sleep(2000);
                Point nm = player[turn-1].nextMove(board, new Point(-1, -1));
                if(nm.x == -1 && nm.y == -1){
                    MessageDialog("相手はパスです");
                }
                else{
                    board.setStoneAndReverse(nm.x, nm.y, player[turn-1].getColor());
                }
                changeTurn();
                repaint();
                addMouseListener(new MouseProc());
                //Check end game
                if(board.num_grid_black == 0 && board.num_grid_white == 0){
                    EndMessageDialog();
                }
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }

    class Player{
        public final static int type_human = 0;
        public final static int type_computer = 1;
        private int color;
        private int type;

        Player (int c, int t){
            if( c == Stone.black || c == Stone.white){
                color = c;
            }
            else {
                System.out.println("プレイヤーの石は黒か白でなければいけません：" + c);
                System.exit(0);
            }
            if( t == type_human || t == type_computer){
                type = t;
            }
            else{
                System.out.println("プレイヤーは人間かコンピュータでなければいけません：" + t);
                System.exit(0);
            }
        }

        int getColor(){
            return color;
        }
        int getType(){
            return type;
        }
        Point tactics(Board bd){
            
            //Prefer upside of left
            bd.evaluateBoard();
            if(color == Stone.black){
                for(int i=0; i<8; i++){
                    for(int j=0; j<8; j++){
                        if(bd.eval_black[i][j] > 0){
                            return (new Point(i, j));
                        }
                    }
                }
            }else if(color == Stone.white){
                for(int i=0; i<8; i++){
                    for(int j=0; j<8; j++){
                        if(bd.eval_white[i][j] > 0){
                            return (new Point(i, j));
                        }
                    }
                }
            }
            return (new Point(-1, -1));
        }
        Point nextMove(Board bd, Point p){
            if( type == type_human){
                return p;
            }
            else if(type == type_computer){
                return tactics(bd);
            }
            return (new Point(-1, -1));
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
}

