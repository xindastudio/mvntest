package unittest;

public class TestClassStaticProperty {
	private static boolean flag = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(flag);
		flag = true;
		System.out.println(flag);
		try {
			Class.forName("unittest.TestClassStaticProperty");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(flag);
		flag = true;
		System.out.println(flag);
	}

}
