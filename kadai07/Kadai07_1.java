//Kadai07_1 Taketora Uzuhara 18D8101028G

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

public class Kadai07_1 extends Application{
    private TextField tf;
    private Button [][] bt = new Button[3][4];
    
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage)throws Exception{
        tf = new TextField();
        tf.setEditable(false);
        tf.setMaxWidth(380);
        tf.setFont(Font.font("MonoSpace", 40));
        tf.setAlignment(Pos.CENTER_RIGHT);

        String[][] bt_str = {{"7", "8", "9"}, {"4", "5", "6"}, {"1", "2", "3"}, {"0", "+", "="}};

        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                bt[i][j] = new Button(bt_str[j][i]);
                bt[i][j].setPrefWidth(95);
                bt[i][j].setPrefHeight(95);
                bt[i][j].setFont(Font.font("MonoSpace", 30));
                bt[i][j].setOnAction(new ButtonEventHandler());
            }
        }

        GridPane gp = new GridPane();
        gp.setHgap(2);
        gp.setVgap(2);
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                gp.add(bt[i][j], i, j);
            }
        }
        gp.setAlignment(Pos.CENTER);

        BorderPane bp = new BorderPane();
        bp.setTop(tf);
        bp.setAlignment(tf, Pos.CENTER);
        bp.setCenter(gp);

        Scene sc = new Scene(bp, 400, 600);
        stage.setScene(sc);
        stage.setTitle("Calculator");
        stage.show();
    }

    class ButtonEventHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            String in = ((Button)e.getSource()).getText();
            StringBuffer stb = new StringBuffer(tf.getText());

            if(in != "="){
                stb.append(in);
                String str = new String(stb.toString());
                tf.setText(str);
            }
            else if(in == "="){
                String ans = calculatePlus();
                if(ans != null){
                    tf.setText(ans);    
                }
            }
            
        }

        public String calculatePlus(){
            String regex = "[+]?[0-9]+[+]{1}[0-9]+";
            String que = tf.getText();
            if(que.matches(regex)) {
                String regex2 = "(?=[+])";
                String[] operands = que.toString().split(regex2);
                int op1 = Integer.parseInt(operands[0]);
                int op2 = Integer.parseInt(operands[1].substring(1));
                int ans = op1 + op2;
                return String.valueOf(ans);
            }else{
                return null;
            }
        }
    }
}