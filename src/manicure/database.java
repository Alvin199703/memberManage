package manicure;

import java.sql.*;
public class database {
	public  static Connection Database;
	static String driver = "com.mysql.jdbc.Driver"; //驱动程序名
	static String url = "jdbc:mysql://localhost:3306/nail?characterEncoding=utf8";//URL指向要访问的数据库名mydata
	static String user = "root";                     //MySQL配置时的用户名
    static String password = "wp0303";             //MySQL配置时的密码
	public static Connection Connect() {
		try{
	        Class.forName(driver);
	        //1.getConnection()方法，连接MySQL数据库！！
	        Database = DriverManager.getConnection(url,user,password);
	        if(!Database.isClosed())
	           System.out.println("Succeeded connecting to the Database!");
	        //Database.close();
	    }catch(ClassNotFoundException event) {   
	        //数据库驱动类异常处理
	        System.out.println("Sorry,can`t find the Driver!");   
	        event.printStackTrace();   
	        } catch(SQLException a) {
	        //数据库连接失败异常处理
	        a.printStackTrace();  
	        }catch (Exception e) {
	        // TODO: handle exception
	        e.printStackTrace();
	    }
	    finally{
	        System.out.println("数据库数据成功获取！！");
	    }
		return Database;
	}
}
