// Kadai09_3 Taketora Uzuhara 18D8101028G 

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Reversi extends JPanel{
    public final static int UNIT_SIZE = 80;
    private Board board = new Board();


    public Reversi(){
        setPreferredSize(new Dimension(800,800));
        addMouseListener(new MouseProc());
    }

    public void paintComponent(Graphics g){
        board.paint(g, UNIT_SIZE);
    }

    void EndMessageDialog(){
        int sumBlack = board.countStone(1);
        int sumWhite = board.countStone(2);
        String str = "[黒:" + sumBlack + "," + "白:" + sumWhite + "]";
        if(sumBlack > sumWhite){
            str = str + "黒の勝ち";
            JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
        }else if(sumWhite > sumBlack){
            str = str + "白の価値";
            JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
        }else{
            str = str + "で引き分け";
            JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
        }
        System.exit(0);
    }

    class MouseProc extends MouseAdapter{
        public void mouseClicked(MouseEvent me){
            Point point = me.getPoint();
            int btn = me.getButton();
            int s;
            int x = point.x/80 - 1;
            int y = point.y/80 - 1;
            if(btn == MouseEvent.BUTTON3 && board.isOnBoard(x, y) == true){
                s = 2;
                board.setStone(x, y, s);
                board.evaluateBoard();
            }else if(btn == MouseEvent.BUTTON1 && board.isOnBoard(x, y) == true){
                s = 1;
                board.setStone(x, y, s);
                board.evaluateBoard();
            }else if(board.isOnBoard(x, y) == false){
                System.out.println("ボードの範囲外です");
            }
            System.out.println(board.num_grid_black + "," + board.num_grid_white);
            if(board.num_grid_black == 0 && board.num_grid_white == 0){
                EndMessageDialog();
            }
            repaint();
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
    public Stone st[][] = new Stone[8][8];
    public int[] check = new int[64];
    public int num_grid_black;
    public int num_grid_white;

    Board (){}

    void setStone(int x, int y, int s){
        this.check[x*8+y] = s;
    }

    boolean isOnBoard(int x, int y){
        if(x>=0 && x<=7 && y>=0 && y<=7){
            return true;
        }else{
            return false;
        }
    }

    void evaluateBoard(){
        int cnt = 0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(st[i][j].getObverse() == 0){
                    cnt = cnt + 1;
                }
            }
        }
        num_grid_black = cnt-1;
        num_grid_white = cnt-1;
    }

    int countStone(int s){
        int cnt = 0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(st[i][j].getObverse() == s){
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

        this.check[3*8+3] = 1;
        this.check[4*8+4] = 1;
        this.check[3*8+4] = 2;
        this.check[4*8+3] = 2;

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                st[i][j] = new Stone();
                if (this.check[i*8+j] == 1 || this.check[i*8+j] == 2){
                    st[i][j].setObverse(this.check[i*8+j]);
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

}
