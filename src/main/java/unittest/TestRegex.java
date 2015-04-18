package unittest;

import java.util.Arrays;

/**
 * 
 * @author wuliwei
 * 
 */
public class TestRegex {

	public static void main(String[] args) {
		String s = "a,b|1,2";
		String[] g = s.split("\\" + "|");
		String[] t = null;
		for (String gs : g) {
			t = gs.split(",");
			System.out.println(t.length);
			System.out.println(Arrays.toString(t));
		}
	}

}
