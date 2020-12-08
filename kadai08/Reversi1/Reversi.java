//Kadai08_2 Taketora Uzuhara 18D8101028G

import java.awt.*;
import javax.swing.*;

class Board {
    Board (){}

    void paint(Graphics g, int unit_size){
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
    }
}

public class Reversi extends JPanel{
    public final static int UNIT_SIZE = 80;
    Board board = new Board();
    
    public Reversi(){
        setPreferredSize(new Dimension(800,800));
    }

    public void paintComponent(Graphics g){
        board.paint(g, UNIT_SIZE);
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

