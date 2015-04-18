package unittest;

public class TestNull {
	public static void main(String[] args) {
		Object i = null;
		Integer i2 = (Integer) (null);
		System.out.println(i2);
		i2 = (Integer) i;
		System.out.println(i2);
		i = i2;
		String s1 = (String) i;
		System.out.println(s1);
	}
}
