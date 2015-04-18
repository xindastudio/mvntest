package unittest;
import java.lang.reflect.Method;
import java.net.URL;


/**
 * @author wuliwei
 *
 */
public class TestUrlHostName {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		URL u1 = new URL("http://218.1.60.51:8080");
		URL u2 = new URL(u1, "http://218.1.60.50:18080/stsadmin");
		output(u2);
		URL url1 = new URL("http://www.baidu.com/s?wd=ceshi&rsv_bp=0&oq=ceshi&rsp=0&f=3&inputT=1103");
		URL url2 = new URL("http://mp3.baidu.com/m?tn=baidump3&ct=134217728&lm=-1&word=ceshi");
		output(url1);
		output(url2);
	}
	
	private static void output(URL url) throws Exception {
		System.out.println(url);
		for (Method m : URL.class.getDeclaredMethods()) {
			m.setAccessible(true);
			if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
				System.out.println(m.getName() + " = " + m.invoke(url, new Object[]{}));
			}
		}
	}

}
