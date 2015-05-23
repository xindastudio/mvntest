package unittest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

	public static void main(String[] args) {
		test1();
	}

	private static void test1() {
		java.sql.Date d1 = new java.sql.Date(System.currentTimeMillis());
		System.out.println(d1.getClass().equals(java.util.Date.class));
		System.out.println(d1 instanceof java.util.Date);
		System.out.println(new java.sql.Date(System.currentTimeMillis()));
		System.out.println(new java.util.Date(1407148268193L));
		java.util.Date d2 = new java.sql.Date(System.currentTimeMillis());
		System.out.println(d2);
	}

	private static void test2() {
		try {
			String ds = "20130809000000";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			long st = sdf.parse(ds).getTime();
			long ct = System.currentTimeMillis();
			ct = st + 43005627991L;
			System.out.println(new Date(ct));
			System.out.println(sdf.format(new Date(ct)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
