package unittest;

import java.math.BigDecimal;

public class TestLong {

	public static void main(String[] args) {
		// System.out.println(Long.parseLong("2.03"));
		System.out.println(Long.MAX_VALUE);
		long i = 2L;
		double j = 2.0;
		System.out.println(i == j);
		System.out.println(Double.parseDouble(new BigDecimal(2).toString()));
	}

}
