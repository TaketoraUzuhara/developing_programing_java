import java.util.Scanner;

public class kadai02_2{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("input number n: ");
        int n = sc.nextInt();
        int value;
        int[] values = new int[n];
        for(int i=0; i<n; i++){
            System.out.print("input number value: ");
            value = sc.nextInt();
            values[i] = value;
        }
        int min=values[0],max=values[0];
        for(int i=1;i<n;i++){
            if(values[i]<min){
                min = values[i];
            }else if(values[i]>max){
                max = values[i];
            }
        }
        System.out.print("min= "+min+"\n"+"max= "+max);
    }
}

/*
input number n: 5
input number value: 23
input number value: 41
input number value: 6
input number value: 85
input number value: 23
min= 6
max= 85
*/