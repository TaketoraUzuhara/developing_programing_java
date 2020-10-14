//kadai03_2 Taketora Uzuhara 18D8101028G
import java.util.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;

public class Kadai03_2 extends Application{
    private Label lb;
    private Button bt;

    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage stage)throws Exception{
        lb = new Label();
        bt = new Button("更新");
        Calendar now = new GregorianCalendar();
        String daytime = now.getTime().toString();
        StringBuffer str = new StringBuffer();
        Formatter fmt = new Formatter(str);
        fmt.format("%02d:%02d:%02d", now.get(Calendar.HOUR_OF_DAY),
        now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
        lb.setText(str.toString());
        BorderPane bp = new BorderPane();
        bp.setCenter(lb);
        bp.setLeft(bt);
        bt.setOnAction(new RefreshEventHandler());
        Scene sc = new Scene(bp, 300, 300);
        stage.setScene(sc);
        stage.setTitle("現在時刻");
        stage.show();
    }

    class RefreshEventHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            Calendar now = new GregorianCalendar();
            String daytime = now.getTime().toString();
            StringBuffer str = new StringBuffer();
            Formatter fmt = new Formatter(str);
            fmt.format("%02d:%02d:%02d", now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
            lb.setText(str.toString());
        }
    }

    
}