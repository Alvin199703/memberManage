package manicure;

import java.sql.*;
public class database {
	public  static Connection Database;
	static String driver = "com.mysql.jdbc.Driver"; //����������
	static String url = "jdbc:mysql://localhost:3306/nail?characterEncoding=utf8";//URLָ��Ҫ���ʵ����ݿ���mydata
	static String user = "root";                     //MySQL����ʱ���û���
    static String password = "wp0303";             //MySQL����ʱ������
	public static Connection Connect() {
		try{
	        Class.forName(driver);
	        //1.getConnection()����������MySQL���ݿ⣡��
	        Database = DriverManager.getConnection(url,user,password);
	        if(!Database.isClosed())
	           System.out.println("Succeeded connecting to the Database!");
	        //Database.close();
	    }catch(ClassNotFoundException event) {   
	        //���ݿ��������쳣����
	        System.out.println("Sorry,can`t find the Driver!");   
	        event.printStackTrace();   
	        } catch(SQLException a) {
	        //���ݿ�����ʧ���쳣����
	        a.printStackTrace();  
	        }catch (Exception e) {
	        // TODO: handle exception
	        e.printStackTrace();
	    }
	    finally{
	        System.out.println("���ݿ����ݳɹ���ȡ����");
	    }
		return Database;
	}
}
