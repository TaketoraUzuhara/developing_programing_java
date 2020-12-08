//Kadai08_1 Taketora Uzuhara 18D8101028G

import java.awt.*;
import javax.swing.*;

public class Kadai08_1 extends JPanel{
    private int width = 600;
    private int height = 800;
    public Kadai08_1(){
        setPreferredSize(new Dimension(width, height));
    }
    public void paintComponent(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);

        Polygon p1 = new Polygon();
        Polygon p2 = new Polygon();
        Polygon p3 = new Polygon();
        Polygon p4 = new Polygon();
        Polygon p5 = new Polygon();
        Polygon p6 = new Polygon();
        Polygon p7 = new Polygon();
        Polygon p8 = new Polygon();

        p1.addPoint((int)(width/10.0*2.5), (int)(height/20*3));
        p1.addPoint((int)(width/10.0*3.0), (int)(height/20*4));
        p1.addPoint((int)(width/10.0*2.5), (int)(height/20*5));
        p1.addPoint((int)(width/10.0*2.0), (int)(height/20*4));

        p2.addPoint((int)(width/10*2.5), (int)(height/20*7));
        p2.addPoint((int)(width/10*3), (int)(height/20*8));
        p2.addPoint((int)(width/10*2.5), (int)(height/20*9));
        p2.addPoint((int)(width/10*2), (int)(height/20*8));

        p3.addPoint((int)(width/10*2.5), (int)(height/20*11));
        p3.addPoint((int)(width/10*3), (int)(height/20*12));
        p3.addPoint((int)(width/10*2.5), (int)(height/20*13));
        p3.addPoint((int)(width/10*2), (int)(height/20*12));

        p4.addPoint((int)(width/10*2.5), (int)(height/20*15));
        p4.addPoint((int)(width/10*3), (int)(height/20*16));
        p4.addPoint((int)(width/10*2.5), (int)(height/20*17));
        p4.addPoint((int)(width/10*2), (int)(height/20*16));

        p5.addPoint((int)(width/10*6.5), (int)(height/20*3));
        p5.addPoint((int)(width/10*7), (int)(height/20*4));
        p5.addPoint((int)(width/10*6.5), (int)(height/20*5));
        p5.addPoint((int)(width/10*6), (int)(height/20*4));

        p6.addPoint((int)(width/10*6.5), (int)(height/20*7));
        p6.addPoint((int)(width/10*7), (int)(height/20*8));
        p6.addPoint((int)(width/10*6.5), (int)(height/20*9));
        p6.addPoint((int)(width/10*6), (int)(height/20*8));

        p7.addPoint((int)(width/10*6.5), (int)(height/20*11));
        p7.addPoint((int)(width/10*7), (int)(height/20*12));
        p7.addPoint((int)(width/10*6.5), (int)(height/20*13));
        p7.addPoint((int)(width/10*6), (int)(height/20*12));

        p8.addPoint((int)(width/10*6.5), (int)(height/20*15));
        p8.addPoint((int)(width/10*7), (int)(height/20*16));
        p8.addPoint((int)(width/10*6.5), (int)(height/20*17));
        p8.addPoint((int)(width/10*6), (int)(height/20*16));

        g.setColor(Color.red);
        g.fillPolygon(p1);
        g.fillPolygon(p2);
        g.fillPolygon(p3);
        g.fillPolygon(p4);
        g.fillPolygon(p5);
        g.fillPolygon(p6);
        g.fillPolygon(p7);
        g.fillPolygon(p8);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(new Kadai08_1());
        f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}