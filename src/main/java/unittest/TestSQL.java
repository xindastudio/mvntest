package unittest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * @author wuliwei
 *
 */
public class TestSQL {
	public static void main(String[] args) {
		         String RL = "jdbc.url=jdbc:oracle:thin:@192.168.0.4:1521:BTest";  
		         String user ="btportal";//这里替换成你自已的数据库用户名  
		         String password = "as1gbhjdr";//这里替换成你自已的数据库用户密码  
		         String sqlStr = "select count(*) from T_DIRECT_IP";  
		 
		         try{     //这里的异常处理语句是必需的.否则不能通过编译!      
		             Class.forName("oracle.jdbc.driver.OracleDriver");  
		             System.out.println( "类实例化成功!" );  
		             System.out.println("slkdjf");  
		             Connection con = DriverManager.getConnection(RL,user,password);  
		             System.out.println( "创建连接对像成功!" );  
		 
		             Statement st = con.createStatement();  
		             System.out.println( "创建Statement成功!" );  
		 
		             ResultSet rs = st.executeQuery( sqlStr );  
		             System.out.println( "操作数据表成功!" );  
		             System.out.println( "----------------!" );  
		 
		             while(rs.next())  
		             {  
		                 System.out.print("T_DIRECT_IP count = " + rs.getLong(1));
		             }  
		             rs.close();  
		             st.close();  
		             con.close();  
		         }  
		         catch(Exception err){  
		             err.printStackTrace(System.out);  
		         }  
		     }  
}
