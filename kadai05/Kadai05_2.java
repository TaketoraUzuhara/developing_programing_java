//kadai05_2 Taketora Uzuhara 18D8101028G 
import java.util.Scanner;
import java.io.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.geometry.*;

public class Kadai05_2 extends Application{
    private TextField[][] tf = new TextField[9][9];
    private static int[][] board = new int[9][9];
    private static Button bt = new Button(String.valueOf("reset"));
    
    public static void main(String[] args) {
        String fname = "input01.txt";

        if(args.length > 0) fname = args[0];
        
        try {
            Scanner sc = new Scanner(new File(fname)); 
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    board[j][i] = sc.nextInt();
                    if ( board[j][i] < 0 || board[j][i] > 9) board[j][i] = 0;
                }
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }

    public void start(Stage stage) throws Exception{
        
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(board[i][j] == 0){
                    tf[i][j] = new TextField();
                } else {
                    tf[i][j] = new TextField(String.valueOf(board[i][j]));
                    tf[i][j].setEditable(false);
                    tf[i][j].setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                }
                tf[i][j].setMaxWidth(40);
                tf[i][j].setFont(Font.font("MonoSpace", 20));
            }
        }

        int i_start = 0;
        int j_start = 0;

        int row_index = 0;
        int col_index = 0;

        GridPane gp_tmp = new GridPane();
        GridPane gp = new GridPane();

        int gp_row = 0;
        int gp_col = 0;

        for(int k=0; k<9; k++){
            gp_tmp = new GridPane();
            gp_tmp.setHgap(1);
            gp_tmp.setVgap(1);
            if(k<3) i_start = 0;
            else if(k>2 && k<6) i_start = 3;
            else if(k>5 && k<9) i_start = 6;
            j_start = (3*k)%9;
            for(int i=i_start; i<i_start+3; i++){
                for(int j=j_start; j<j_start+3; j++){
                  gp_tmp.add(tf[i][j], row_index, col_index);
                  col_index = col_index + 1;  
                }
                row_index = row_index + 1;
                col_index = 0;
            }
            row_index = 0;
            col_index = 0;
            gp_tmp.setAlignment(Pos.CENTER);
            gp_row = (int)k/3;
            gp_col = (int)k%3;
            gp.add(gp_tmp, gp_row, gp_col);
        }

        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);
        BorderPane bp = new BorderPane();
        bp.setAlignment(bt, Pos.CENTER);
        bp.setMargin(bt, new Insets(0,0,50,0));
        bp.setBottom(bt);
        bp.setCenter(gp);
        Scene sc = new Scene(bp, 600, 600);
        stage.setScene(sc);
        stage.setTitle("Suudoku");
        stage.show();
    }
}