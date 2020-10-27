import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class kadai02_3{
    public static void main(String[] args) {
        String fname = "test.dat";
        if(args.length > 0){
            fname = args[0];
        }

        try{
            Scanner sc = new Scanner(new File(fname));
            int n = sc.nextInt();
            System.out.println("n = " + n);
            int value;
            int[] values = new int[n];
            for(int i=0; i<n; i++){
                value = sc.nextInt();
                values[i] = value;
            }
            Arrays.sort(values);
            int max, max2=404;
            max = values[n-1];
            for(int i=n-1; i>=0; i--){
                if(values[i] < max){
                    max2 = values[i];
                    break;
                }
            }
            if (max2==404){
                System.out.println("max = "+max+"\n"+"max2 = undefined!");
            }else{
                System.out.println("max = "+max+"\n"+"max2 = "+max2);
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}

/*
java kadai02_3.java
n = 5
max= 85
max2= 41

test.dat
---
5
23
41
6
85
23
---

java kadai02_3.java abc.dat 
n = 10
max= 10
max2= 9

abc.dat
---
10
1
2
3
4
5
6
7
8
9
10
---

java kadai02_3.java abc2.dat
n = 10
max = 10
max2 = undefined!

abc2.dat
---
10
10
10
10
10
10
10
10
10
10
10
---

java kadai02_3.java abc3.dat 
n = 10
max = 10
max2 = 9

abc3.dat
---
10
9
9
9
9
9
9
9
10
10
10
---

*/