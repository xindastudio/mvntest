package unittest;

/**
 * @author wuliwei
 * 
 */
public class TestInteger {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Long.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE / 1024 / 1024);
		equalsTest();
		int i = -1;
		Integer j = (Integer) i;
		System.out.println(j);
	}

	/**
	 * 
	 */
	public static void equalsTest() {
		Integer i1 = 2;
		Integer a1 = 2;
		Integer i2 = Integer.valueOf(2);
		Integer i3 = Integer.valueOf(2);
		Integer i4 = Integer.valueOf("2");
		Integer i5 = Integer.valueOf("2");

		System.out.println("test ==");

		System.out.println("i1 == i2 ? " + (i1 == i2));
		System.out.println("i1 == a1 ? " + (i1 == a1));
		System.out.println("i1 == i4 ? " + (i1 == i4));
		System.out.println("i2 == i3 ? " + (i2 == i3));
		System.out.println("i2 == i4 ? " + (i2 == i4));
		System.out.println("i4 == i5 ? " + (i4 == i5));

		System.out.println("test equals");

		System.out.println("i1.equals(i2) ? " + i1.equals(i2));
		System.out.println("i1.equals(i4) ? " + i1.equals(i4));
		System.out.println("i2.equals(i3) ? " + i2.equals(i3));
		System.out.println("i2.equals(i4) ? " + i2.equals(i4));
		System.out.println("i4.equals(i5) ? " + i4.equals(i5));

		Integer ia = new Integer(2);
		Integer ib = new Integer(2);
		System.out.println("ia == ib ? " + (ia == ib));
		System.out.println("ia.equals(ib) ? " + ia.equals(ib));
	}

}
