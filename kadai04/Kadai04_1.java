//kadai04_1 Taketora Uzuhara 18D8101028G
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import java.util.*;

public class Kadai04_1 extends Application{
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
                int cx = 300/2;
                int cy = 200/2;
                double theta_sec = (second-15)*6 / 180.0 * Math.PI; //90度分秒針を戻して角度を調整
                double xs = rad_s * Math.cos(theta_sec);
                double ys = rad_s * Math.sin(theta_sec);
                gc2.setStroke(Color.BLACK);
                gc2.strokeOval(cx-RADIUS, cy-RADIUS, RADIUS*2, RADIUS*2);
                gc2.setStroke(Color.RED);
                gc2.strokeLine(cx, cy, (int)(cx + xs), (int)(cy + ys));
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