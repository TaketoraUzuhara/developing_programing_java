//kadai06_1 Taketora Uzuhara 18D8101028G 

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
import javafx.event.*;
import java.util.*;
import javafx.collections.*;

public class Kadai06_1 extends Application{
    private TextField[][] tf = new TextField[9][9];
    private static int[][] board = new int[9][9];
    private ObservableList<String> ol = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9");
    private  Button reset_bt = new Button(String.valueOf("reset"));
    private Label lb;
    private ArrayList<ComboBox<String>> cb_list = new ArrayList<ComboBox<String>>();
    private String error = "Select number from each combox!";
    
    public static void main(String[] args) {
        //read file
        String fname = "input02.txt";
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

        //register init
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(board[i][j] == 0){
                    ComboBox<String> cb = new ComboBox<String>();
                    cb.setItems(ol);
                    cb.setOnAction(new ComboxEventHandler());
                    cb_list.add(cb);
                } else {
                    ComboBox<String> cb = new ComboBox<String>();
                    cb_list.add(cb);
                    tf[i][j] = new TextField(String.valueOf(board[i][j]));
                    tf[i][j].setEditable(false);
                    tf[i][j].setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                    tf[i][j].setMaxWidth(50);
                }
            }
        }


        //register tp Gridpane
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
            j_start = (3*k)%9;
            if(k<3) i_start = 0;
            else if(k>2 && k<6) i_start = 3;
            else if(k>5 && k<9) i_start = 6;
            for(int i=i_start; i<i_start+3; i++){
                for(int j=j_start; j<j_start+3; j++){
                    if(board[i][j] == 0){    
                        gp_tmp.add(cb_list.get(i*9+j), row_index, col_index);
                    }else{
                        gp_tmp.add(tf[i][j], row_index, col_index);
                    }
                    col_index = col_index + 1;  
                }
                row_index = row_index + 1;
                col_index = 0;
            }
            row_index = 0;
            col_index = 0;
            gp_tmp.setHgap(10);
            gp_tmp.setVgap(5);
            gp_tmp.setAlignment(Pos.CENTER);
            gp_row = (int)k/3;
            gp_col = (int)k%3;
            gp.add(gp_tmp, gp_row, gp_col);
        }

        //register event handler
        reset_bt.setOnAction(new ResetEventHandler());
        

        //register label
        lb = new Label();
        lb.setText(error);

        //register panel adjust layout
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setAlignment(Pos.CENTER);
        BorderPane bp = new BorderPane();
        bp.setAlignment(lb, Pos.CENTER);
        bp.setAlignment(reset_bt, Pos.CENTER);
        bp.setBottom(reset_bt);
        bp.setCenter(lb);
        bp.setTop(gp);
        Scene sc = new Scene(bp, 600, 350);
        stage.setScene(sc);
        stage.setTitle("Suudoku");
        stage.show();

    
    }
    class ResetEventHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    if(board[i][j] == 0) cb_list.get(i*9+j).setValue(null);
                }
            }
            lb.setText(error);
        }
    }

    class ComboxEventHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            int num;
            //judge whether clear or not
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    if(board[i][j] == 0){
                        num = Integer.parseInt(cb_list.get(i*9+j).getValue());
                        if(check(num, i, j) == false){
                            lb.setText(error);
                            return;
                        }
                    }
                }
            }
            lb.setText("Congraturation! You clear the game !!!!");
        }
        //check from all aspect
        public boolean check(int num, int i, int j){
            int pnt = num;
            int start_i = 0;
            int start_j = 0;
            if((int)i/3 == 0) start_i = 0;
            else if((int)i/3 == 1) start_i = 3;
            else if((int)i/3 == 2) start_i = 6;
            
            if((int)j/3 == 0) start_j = 0;
            else if((int)j/3 == 1) start_j = 3;
            else if((int)j/3 == 2) start_j = 6;

            for(int k=0; k<9; k++){
                if(k==j) continue;
                else if(board[i][k] == 0){
                    if(pnt==Integer.parseInt(cb_list.get(i*9+k).getValue())) return false;
                }
                else{
                    if(pnt==Integer.parseInt(tf[i][k].getText())) return false;
                } 
            }
            for(int k=0; k<9; k++){
                if(k==i) continue;
                else if(board[k][j] == 0){
                    if(pnt==Integer.parseInt(cb_list.get(k*9+j).getValue())) return false;
                }
                else{
                    if(pnt==Integer.parseInt(tf[k][j].getText())) return false;
                } 
            }
            for(int k=start_i; k<start_i+3; k++){
                for(int m=start_j; m<start_j+3; m++){
                    if(k==i && m==j) continue;
                    else if(board[k][m] == 0){
                        if(pnt==Integer.parseInt(cb_list.get(k*9+m).getValue())) return false;
                    }
                    else{
                        if(pnt==Integer.parseInt(tf[k][m].getText())) return false;
                    } 
                }
            }
            return true;
        }
        
    }
}
