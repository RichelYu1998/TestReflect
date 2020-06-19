import cn.tedu.pojo.Emp.Emp;

import java.lang.reflect.Field;
import java.sql.*;

public class TestReflect {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("mysql://localhost:3306/yonghedb?characterEncoding=utf-8&amp;serverTimezone=GMT%2B8","root","Syp950628");
        Statement stmt = conn.createStatement();
        String sql="select * from emp where id=1";
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()){
            Class emClz = Class.forName("cn.tedu.pojo.Emp");
            Emp emp = (Emp) emClz.newInstance();
            System.out.println(emp);
            Field[] EmpFields = emClz.getDeclaredFields();
            for (Field field:EmpFields) {
                field.setAccessible(true);
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 0; i <rsmd.getColumnCount() ; i++) {
                    String colName=rsmd.getColumnName(i+1);
                    if(colName.equals(field.getName())){
                        System.out.println("列名："+colName + "---属性名：" + field.getName());
                        field.set(emp,rs.getObject(i+1));
                    }
                }
            }
            System.out.println(emp);
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("TestJdbc.main()....");
    }
}
