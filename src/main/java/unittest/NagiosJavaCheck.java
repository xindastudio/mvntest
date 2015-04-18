package unittest;
public class NagiosJavaCheck {

	public static void main(String[] args) {
		try {
			System.out.println(args[1]);
			System.exit(Integer.valueOf(args[0]));
			System.out.flush();
		} catch (Exception e) {
		}
		System.out.println("CRITICAL");
		System.out.flush();
		System.exit(2);
	}

}
