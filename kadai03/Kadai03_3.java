//kadai03_3 Taketora Uzuhara 18D8101028G
import java.util.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;

public class Kadai03_3 extends Application{
    private Canvas cv;

    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage stage)throws Exception{
        cv = new Canvas(300, 200);
        GraphicsContext gc = cv.getGraphicsContext2D();
        BorderPane bp = new BorderPane();
        bp.setCenter(cv);
        Scene sc = new Scene(bp, 300, 300);
        stage.setScene(sc);
        stage.setTitle("現在時刻");
        stage.show();

        Thread th = new Thread(() -> {
            while(true){
                Calendar cl = new GregorianCalendar();
                int hour = cl.get(Calendar.HOUR_OF_DAY);
                int minute = cl.get(Calendar.MINUTE);
                int second = cl.get(Calendar.SECOND);
                gc.clearRect(0, 0, 300, 200);
                gc.setFont(new Font("Arial", 32));
                gc.fillText(String.format("%2d:%2d:%2d", hour, minute, second), 100, 100);
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                }
            }
        });
        th.setDaemon(true);
        th.start();
    }   
}