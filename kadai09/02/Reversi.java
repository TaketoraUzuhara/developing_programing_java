// Kadai09_2 Taketora Uzuhara 18D8101028G

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

    class MouseProc extends MouseAdapter{
        public void mouseClicked(MouseEvent me){
            Point point = me.getPoint();
            int btn = me.getButton();
            int s;
            int x;
            int y;
            if(btn == MouseEvent.BUTTON3 && point.x >= 80 && point.x <= 720 && point.y >= 80 && point.y <= 720){
                s = 2;
                x = point.x/80 - 1;
                y = point.y/80 - 1;
                board.setStone(x, y, s);
            }else if(btn == MouseEvent.BUTTON1 && point.x >= 80 && point.x <= 720 && point.y >= 80 && point.y <= 720){
                s = 1;
                x = point.x/80 -1;
                y = point.y/80 -1;
                board.setStone(x, y, s);
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
    Board (){}

    void setStone(int x, int y, int s){
        this.check[x*8+y] = s;
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

        st[3][3] = new Stone();
        st[3][3].setObverse(1);
        p.setLocation((int)(4*unit_size+unit_size*0.1), (int)(4*unit_size+unit_size*0.1));
        st[3][3].paint(g, p, rad);

        st[4][4] = new Stone();
        st[4][4].setObverse(1);
        p.setLocation((int)(5*unit_size+unit_size*0.1), (int)(5*unit_size+unit_size*0.1));
        st[4][4].paint(g, p, rad);

        st[4][3] = new Stone();
        st[4][3].setObverse(2);
        p.setLocation((int)(5*unit_size+unit_size*0.1), (int)(4*unit_size+unit_size*0.1));
        st[4][3].paint(g, p, rad);

        st[3][4] = new Stone();
        st[3][4].setObverse(2);
        p.setLocation((int)(4*unit_size+unit_size*0.1), (int)(5*unit_size+unit_size*0.1));
        st[3][4].paint(g, p, rad);

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
            System.out.println("You must set balck or white");
        }
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



