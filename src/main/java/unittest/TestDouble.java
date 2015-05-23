package unittest;

import java.math.BigDecimal;

/**
 * @author wuliwei
 * 
 */
public class TestDouble {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(String
				.valueOf(Double.valueOf(10240).doubleValue() * 1024) + "bps");
		Float f = 0.1F;
		System.out.println(f);
		Double d = ((Number) f).doubleValue();
		System.out.println(d);
		d = f.doubleValue();
		System.out.println(d);
		d = new BigDecimal(Float.toString(f)).doubleValue();
		System.out.println(d);
		d = Double.valueOf(Float.toString(f));
		System.out.println(d);
		d = 0.1D;
		System.out.println(d);
		f = d.floatValue();
		System.out.println(f);
		d = Double.MAX_VALUE;
		f = d.floatValue();
		System.out.println(f);
	}

}
