package unittest;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class TestUrlDecoder {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String src = "上海";
		String enC = URLEncoder.encode(src, "UTF-8");
		System.out.println(enC);
		System.out.println(URLDecoder.decode("%E4%B8%8A%E6%B5%B7%E5%87%BA%E5%85%A5%E5%A2%83%E6%A3%80%E9%AA%8C%E6%A3%80%E7%96%AB%E5%B1%80IP0540", "UTF-8"));
		System.out.println(URLDecoder.decode(enC, "UTF-8"));
		System.out.println(URLDecoder.decode("%E4%B8%8A%E6%B5%B7", "UTF-8"));
		System.out.println("decodeProvince = " + decodeProvince(enC, null));
		src = "asdf!#| ";
		enC = URLEncoder.encode(src, "UTF-8");
		System.out.println(enC);
		System.out.println(URLDecoder.decode(src, "UTF-8"));
		System.out.println(URLDecoder.decode(enC, "UTF-8"));
		System.out.println(URLDecoder.decode("asdf!#|", "UTF-8"));
		System.out.println("decodeProvince = " + decodeProvince(enC, null));
		src = "？？？？μ· ";
		enC = URLEncoder.encode(src, "UTF-8");
		System.out.println(enC);
		System.out.println(URLDecoder.decode(src, "UTF-8"));
		System.out.println(URLDecoder.decode(enC, "UTF-8"));
		System.out.println(URLDecoder.decode("？？？？μ·", "UTF-8"));
		System.out.println("decodeProvince = " + decodeProvince(enC, null));
	}

	private static String decodeString(String val, String def) {
		if (null != val && val.length() > 0) {
			try {
				val = java.net.URLDecoder.decode(val, "UTF-8");
			} catch (Exception e) {
				val = def;
			}
		}
		return val;
	}

	private static String decodeProvince(String val, String def) {
		val = decodeString(val, def);
		if ("上海".equals(val)) {
			return "上海";
		}
		return def;
	}

}
