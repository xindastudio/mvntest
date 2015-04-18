package unittest;

/**
 * @author wuliwei
 *
 */
public class TestUUID {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int count = 10;
		for (int i = 0; i < count; i++) {
			System.out.println(java.util.UUID.randomUUID());
		}
	}

}
