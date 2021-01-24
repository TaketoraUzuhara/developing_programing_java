//kadai14_1 18D8101028G Taketora Uzuhara

import java.sql.*;
import java.io.*;

public class Kadai14_1 {
    public static void main(String[] args) {
        try {
            String url = "jdbc:derby:kadai14DB;create=true";
            String user = "";
            String pw = "";
            //set the table name
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String table = br.readLine();
            Connection cn = DriverManager.getConnection(url, user, pw);

            DatabaseMetaData dm = cn.getMetaData();
            Statement st = cn.createStatement();

            ResultSet tb = dm.getTables(null, null, table, null);

            //If there is a table, get all table data
            if(tb.next()){
                String qry1 = "select * from " + table;
                ResultSet rs = st.executeQuery(qry1);
                ResultSetMetaData rm = rs.getMetaData();
                int cnum = rm.getColumnCount();
                while(rs.next()){
                    for(int i=1; i<=cnum; i++){
                        System.out.print(rm.getColumnName(i) + ":" + rs.getObject(i) + " ");
                        System.out.println("");
                    }
                }
                rs.close();
            }
            //Else if there is no table exist, create a new table
            else{
                String qry2 = "create table " + table + " (ID int, NAME varchar(20))";
                String[] qry3 = {
                    "insert into " + table + " values (2, '乗用車')",
                    "insert into " + table + " values (3, 'オープンカー')",
                    "insert into " + table + " values (4, 'トラック')"
                };
                st.executeUpdate(qry2);
                for (int i=0; i<qry3.length; i++){
                    st.executeUpdate(qry3[i]);
                }
                System.out.println("Success to make a table");
            }
            st.close();
            cn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
