package unittest;

/**
 * @author wuliwei
 *
 */
public class TestChangeableParamLen {
	/**
	 * 
	 */
	public TestChangeableParamLen() {
		System.out.println("ChangeableParamLenTest() called.");
	}
	
	/**
	 * @param params
	 */
	public TestChangeableParamLen(Object... params) {
		System.out.println("ChangeableParamLenTest(int... params) called.");
		System.out.println(params.length);
	}
	
	/**
	 * @param f 
	 * @param params
	 */
	public TestChangeableParamLen(Object f, Object... params) {
		System.out.println("ChangeableParamLenTest(int f, int... params) called.");
		System.out.println(params.length);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestChangeableParamLen();
		new TestChangeableParamLen(new Object[]{});
		new TestChangeableParamLen(new Object[]{1});
		new TestChangeableParamLen(1, new Object[]{1, 2});
	}

}
