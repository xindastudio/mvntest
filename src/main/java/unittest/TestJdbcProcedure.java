package unittest;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

public class TestJdbcProcedure {
	public static void main(String[] args) {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@10.50.107.71:1521:cbims";
		String username = "cbims";
		String password = "cbims";
		Connection conn = null;
		CallableStatement cs = null;
		try {
			long s = System.currentTimeMillis();
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			cs = conn
					.prepareCall("{call CBIMS.Z_CBIMS_SVOD_PPV_DATE(?,?,?,?,?)}");
			String reportTime = "2012-06-05";
			String stationCode = "guangdong";
			float validPercent = 33F;
			cs.setString(1, reportTime);
			cs.setString(2, stationCode);
			cs.setFloat(3, validPercent);
			cs.registerOutParameter(4, java.sql.Types.INTEGER);
			cs.registerOutParameter(5, java.sql.Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(4);
			String msg = cs.getString(5);
			long u = System.currentTimeMillis() - s;
			System.out.println(result + ", " + msg + ", " + u);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != cs) {
				try {
					cs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
