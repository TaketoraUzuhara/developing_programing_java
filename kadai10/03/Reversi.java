// Kadai10_2 Taketora Uzuhara 18D8101028G 

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Reversi extends JPanel{
    public final static int UNIT_SIZE = 80;
    private Board board = new Board();
    private int turn = 1;


    public Reversi(){
        setPreferredSize(new Dimension(800,800));
        addMouseListener(new MouseProc());
    }

    public void paintComponent(Graphics g){
        board.paint(g, UNIT_SIZE, turn);
    }

    void EndMessageDialog(){
        int sumBlack = board.countStone(1);
        int sumWhite = board.countStone(2);
        String str = "[黒:" + sumBlack + "," + "白:" + sumWhite + "]";
        if(sumBlack > sumWhite){
            str = str + "で黒の勝ちです";
            JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
        }else if(sumWhite > sumBlack){
            str = str + "で白の勝ちです";
            JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
        }else{
            str = str + "引き分け";
            JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
        }
        System.exit(0);
    }

    void MessageDialog(String str){
        JOptionPane.showMessageDialog(this, str, "情報", JOptionPane.INFORMATION_MESSAGE);
    }

    void changeTurn(){
        if(turn == 1) turn = 2;
        else if(turn == 2) turn = 1;
    }

    class MouseProc extends MouseAdapter{
        public void mouseClicked(MouseEvent me){
            Point point = me.getPoint();
            int btn = me.getButton();
            int s;
            int x = point.x/80 - 1;
            int y = point.y/80 - 1;
        
            if(btn == MouseEvent.BUTTON3 && board.isOnBoard(x, y) == true && turn == 2){
                s = 2;
                if(board.countReverseStone(x, y, s) > 0){
                    board.setStoneAndReverse(x, y, s);
                    board.printBoard();
                    board.evaluateBoard();
                    board.printEval();
                    changeTurn();
                }
            }else if(btn == MouseEvent.BUTTON1 && board.isOnBoard(x, y) == true && turn == 1){
                s = 1;
                if(board.countReverseStone(x, y, s) > 0){
                    board.setStoneAndReverse(x, y, s);
                    board.evaluateBoard();
                    board.printBoard();
                    board.printEval();
                    changeTurn();
                }
            }else if(board.isOnBoard(x, y) == false){
                System.out.println("ボードの範囲外です");
            }
            repaint();
            if(turn == 1){
                board.evaluateBoard();
                if(board.num_grid_black == 0 && board.num_grid_white != 0){
                    String str = "あなたはパスです";
                    MessageDialog(str);
                    changeTurn();
                    repaint();
                }
            }else if(turn == 2){
                board.evaluateBoard();
                if(board.num_grid_white == 0 && board.num_grid_black != 0){
                    String str = "あなたはパスです";
                    MessageDialog(str);
                    changeTurn();
                    repaint();
                }
            }
            if(board.num_grid_black == 0 && board.num_grid_white == 0){
                EndMessageDialog();
            }
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(new Reversi());
        f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
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

    void paint(Graphics g, int unit_size, int turn){
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

        g.setColor(Color.white);
        String str2 = "[黒:" + countStone(1)+ "," + "白:" + countStone(2) + "]";
        if(turn == 1){
            String str1 = "黒の番です";
            g.drawString(str1, 40, 40);
            g.drawString(str2, 40, 760);
        }else if(turn == 2){
            String str1 = "白の番です";
            g.drawString(str1, 40, 40);
            g.drawString(str2, 40, 760);
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
