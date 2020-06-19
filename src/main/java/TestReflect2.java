import cn.tedu.pojo.Emp.Emp;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestReflect2 {public static void main(String[] args) throws Exception {
    // 1.注册数据库驱动
    Class.forName("com.mysql.jdbc.Driver");
    // 2.获取数据库连接
    Connection conn = DriverManager.getConnection("mysql://localhost:3306/yonghedb?characterEncoding=utf-8&amp;serverTimezone=GMT%2B8","root","Syp950628");
    Statement stat = conn.createStatement();
    // 4.发送SQL到服务器执行并返回执行结果
    String sql = "select * from emp";
    ResultSet rs = stat.executeQuery(sql);
    // 5.处理结果(利用反射将结果集中每一行数据封装到 一个Emp对象中，再将Emp对象封装到List集合中)
    List list = new ArrayList();
    while( rs.next() ) { // true 表示当前行有数据
        // 5.1.根据类的全限定类名获取该类的字节码对象
        Class empClz = Class.forName( "com.tedu.pojo.Emp" );
        // 5.2.根据类的字节码对象获取该类的实例:Emp emp = new Emp();
        Emp emp = (Emp) empClz.newInstance();
        // 5.3.根据类的字节码对象获取该类的所有属性字段组成的数组
        Field[] EmpFields = empClz.getDeclaredFields();
        // 5.4.遍历所有的字段数组
        for (Field field : EmpFields) {
            // 设置属性的访问权限，即使private属性也可以进行访问
            field.setAccessible(true);
            // 遍历结果集中的中第一行中的所有列
            ResultSetMetaData rsmd = rs.getMetaData();
            // 根据 rs 中列的个数遍历
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                // 获取每一列的列名
                String colName = rsmd.getColumnName(i + 1);
                // 判断列名和当前外层循环正在遍历的属性名是否相同
                if (colName.equals(field.getName())) {
                    System.out.println("列名："+colName + "---属性名：" + field.getName());
                    // 如果相同，则将当前行及当前列中的值，设置给当前属性
                    field.set( emp ,  rs.getObject( i+1 ) );
                }
            }
        }
        list.add( emp );
    }

    //遍历List集合
    for (Object object : list) {
        System.out.println( object );
    }

    // 6.释放资源
    rs.close();
    stat.close();
    conn.close();
}
}
