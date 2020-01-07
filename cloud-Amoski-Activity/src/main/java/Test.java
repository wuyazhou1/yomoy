import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args){
        Map<String, Object> map = new HashMap<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            //加载驱动，获取数据库连接
            con=getConnection();
            //sql准备就绪声明
            pst = con.prepareStatement("select 111 as column_name from dual");
            //运行sql，获取结果
            rs = pst.executeQuery();
            //转换成结果数据
            ResultSetMetaData metaData = rs.getMetaData();
            //获取数据结果列
            int columnCount = metaData.getColumnCount();
            while(rs.next()){
                //map.put(rs.getString(1).trim(), rs.getString(2).trim());
                System.out.println(rs.getString(1).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeAll(con, pst, rs);
        }
    }



    //执行查询的sql语句
    public static Map<String, Object> FindMysqlDataBysql(String sql){
        Map<String, Object> map = new HashMap<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            //加载驱动，获取数据库连接
            con=getConnection();
            //sql准备就绪声明
            pst = con.prepareStatement(sql);
            //运行sql，获取结果
            rs = pst.executeQuery();
            //转换成结果数据
            ResultSetMetaData metaData = rs.getMetaData();
            //获取数据结果列
            int columnCount = metaData.getColumnCount();
            while(rs.next()){
                map.put(rs.getString(1).trim(), rs.getString(2).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeAll(con, pst, rs);
        }
        return map;
    }



    //获取数据库连接
    public static Connection getConnection(){
        Connection con = null;
        try {
            //加载驱动
            Class.forName("oracle.jdbc.OracleDriver");
            //获取连接，登入用户，密码
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.39:1521:orcl",
                    "amoski_user", "amoski_user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    //释放资源
    public static void closeAll(Connection con,Statement stm,ResultSet rs){
        try {
            if(rs!=null){
                rs.close();
            }
            if(stm!=null){
                stm.close();
            }
            if(con!=null){
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
