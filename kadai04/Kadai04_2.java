//kadai04_2 Taketora Uzuhara 18D8101028G
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import java.util.*;

public class Kadai04_2 extends Application{
    private Canvas cv1;
    private Canvas cv2;
    final int WIDTH = 600;
    final int HEIGHT = 200;
    final int RADIUS = 80;

    public static void main(String[] args) {
        launch(args);    
    }
    public void start(Stage stage)throws Exception{
        cv1 = new Canvas(300, 200);
        cv2 = new Canvas(300, 300);
        GraphicsContext gc1 = cv1.getGraphicsContext2D();
        GraphicsContext gc2 = cv2.getGraphicsContext2D();
        BorderPane bp = new BorderPane();
        bp.setLeft(cv1);
        bp.setRight(cv2);
        Scene sc = new Scene(bp, WIDTH, HEIGHT);
        stage.setScene(sc);
        stage.setTitle("現在時刻");
        stage.show();

        Thread th = new Thread(() -> {
            while(true){
                Calendar cl = new GregorianCalendar();
                int hour = cl.get(Calendar.HOUR_OF_DAY);
                int minute = cl.get(Calendar.MINUTE);
                int second = cl.get(Calendar.SECOND);
                gc1.clearRect(0, 0, 300, 200);
                gc1.setFont(new Font("Arial", 32));
                gc1.fillText(String.format("%2d:%2d:%2d", hour, minute, second), 100, 100);
                gc2.clearRect(0, 0, 300, 200);
                int rad_s = (int)(RADIUS*0.8);
                int rad_m = (int)(RADIUS);
                int rad_h = (int)(RADIUS*0.6);
                int cx = 300/2;
                int cy = 200/2;
                double theta_sec = (second-15)*6 / 180.0 * Math.PI; 
                double theta_min = (minute-15)*6 / 180.0 * Math.PI; 
                double theta_hor = (hour-6)*15 / 180.0 * Math.PI; 
                double xs = rad_s * Math.cos(theta_sec);
                double ys = rad_s * Math.sin(theta_sec);
                double xm = rad_m * Math.cos(theta_min);
                double ym = rad_m * Math.sin(theta_min);
                double xh = rad_h * Math.cos(theta_hor);
                double yh = rad_h * Math.sin(theta_hor);
                gc2.setStroke(Color.BLACK);
                gc2.strokeOval(cx-RADIUS, cy-RADIUS, RADIUS*2, RADIUS*2);
                gc2.setStroke(Color.RED);
                gc2.strokeLine(cx, cy, (int)(cx + xs), (int)(cy + ys));
                gc2.setStroke(Color.BLUE);
                gc2.strokeLine(cx, cy, (int)(cx + xm), (int)(cy + ym));
                gc2.setStroke(Color.BLUE);
                gc2.setLineWidth(3.0);
                gc2.strokeLine(cx, cy, (int)(cx + xh), (int)(cy + yh));
                gc2.setLineWidth(1.0);
                try{
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        });
        th.setDaemon(true);
        th.start();
    }
}