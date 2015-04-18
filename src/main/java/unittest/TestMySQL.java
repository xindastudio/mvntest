package unittest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author wuliwei
 * 
 */
public class TestMySQL {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String driver = null != args && args.length > 0 ? args[0] : "com.mysql.jdbc.Driver";
		String url = null != args && args.length > 1 ? args[1] : "jdbc:mysql://50.50.50.14/cmspeed";
		String user = null != args && args.length > 2 ? args[2] : "root";
		String password = null != args && args.length > 3 ? args[3] : "huaxia";
		String table = null != args && args.length > 4 ? args[4] : "CM_FTP_SITE";
		String sqlStr = "select count(*) from " + table;

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			System.out.println("驱动类实例化成功!");
			con = DriverManager.getConnection(url, user, password);
			System.out.println("创建连接对像成功!");

			st = con.createStatement();
			System.out.println("创建Statement成功!");

			rs = st.executeQuery(sqlStr);
			System.out.println("操作数据表成功!");

			while (rs.next()) {
				System.out.print(table + " count = " + rs.getLong(1));
			}
		} catch (Exception err) {
			err.printStackTrace(System.out);
		} finally {
			if (null != rs) {
				try { rs.close(); } catch (Exception e) { }
			}
			if (null != st) {
				try { st.close(); } catch (Exception e) { }
			}
			if (null != con) {
				try { con.close(); } catch (Exception e) { }
			}
		}
	}
}
