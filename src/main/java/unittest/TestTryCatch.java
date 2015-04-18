package unittest;

public class TestTryCatch extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestTryCatch(String msg) {
		super(msg);
	}

	public static void main(String[] args) throws Exception {
		try {
			throw new TestTryCatch("TestTryCatch test exception");
		} catch (TestTryCatch e) {
			e.printStackTrace();
			throw new Exception("test exception");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
