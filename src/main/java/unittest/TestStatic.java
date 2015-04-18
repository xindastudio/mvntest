package unittest;
/**
 * @author wuliwei
 * 
 */
public class TestStatic {

	private static void test1() {
		System.out.println("static test1 called.");
	}

	private void test2() {
		System.out.println("test2 called.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestStatic t = null;
		t.test1();
		try {
			t.test2();
		} catch (Exception e) {
			e.printStackTrace();
		}
		t = new TestStatic();
		t.test2();
	}

}
