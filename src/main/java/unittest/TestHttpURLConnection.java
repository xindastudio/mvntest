package unittest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author wuliwei
 * 
 */
public class TestHttpURLConnection {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// test1();
		// test2();
		// test3();
		// testCookie();
		// testSoap();
		// testHttp();
		// getSPUserByUrl("180.171.61.184");
		testSoap(args[0], args[1]);
	}

	/**
	 * @throws Exception
	 */
	public static void test1() throws Exception {
		URL url = new URL("http://www.baidu.com");
		URLConnection con = url.openConnection();
		for (Method m : URLConnection.class.getDeclaredMethods()) {
			if (m.getName().startsWith("get")
					&& 0 == m.getParameterTypes().length) {
				System.out.println(m.getName().substring(3) + " = "
						+ m.invoke(con, new Object[] {}));
			}
		}
		con.connect();
	}

	/**
	 * @throws Exception
	 */
	public static void test2() throws Exception {
		URL url = new URL("http://www.baidu.com");
		URLConnection con = url.openConnection();

		for (Method m : URL.class.getDeclaredMethods()) {
			if (m.getName().startsWith("get")
					&& 0 == m.getParameterTypes().length) {
				System.out.println(m.getName().substring(3) + " = "
						+ m.invoke(url, new Object[] {}));
			}
		}

		System.out.println();

		String key = null, value = null;
		int n = 0;
		do {
			value = con.getHeaderField(n);
			key = con.getHeaderFieldKey(n);
			if (null != key || null != value) {
				System.out.println("key = " + key + ", value = " + value);
			}
			n++;
		} while (null != key || null != value);
	}

	/**
	 * @throws Exception
	 */
	public static void test3() throws Exception {
		URL url = new URL("http://www.baidu.com");
		String host = url.getHost();
		int port = getPort(url);
		long st = System.currentTimeMillis();
		SocketAddress sa = new InetSocketAddress(host, port);
		long dnsTime = System.currentTimeMillis() - st;
		System.out.println("DNS Time is " + dnsTime + " ms");
	}

	public static void testCookie() {
		try {
			URL url = new URL(
					"http://218.1.60.51:8080/stsadmin/ships.txt;JSESSIONID=00D162E7579DA9BC0D58A6E01CFEDD87; Path=/stsadmin; HttpOnly");
			URL serverUrl = url;
			URLConnection con = null;
			con = serverUrl.openConnection();
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			new ObjectOutputStream(con.getOutputStream());
			new ObjectInputStream(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			URL url = new URL(
					"http://218.1.60.51:8080/stsadmin/ships.txt;JSESSIONID=00D162E7579DA9BC0D58A6E01CFEDD87; Path=/stsadmin; HttpOnly?param=params&abc=abc");
			URL serverUrl = url;
			String cookie = getCookie(url.toString());
			if (null != cookie && cookie.length() > 0) {
				String realUrl = getRealUrl(url.toString());
				serverUrl = new URL(realUrl);
			}
			URLConnection con = null;
			con = serverUrl.openConnection();
			if (null != cookie && cookie.length() > 0) {
				con.addRequestProperty("Cookie", cookie);
			}
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			new BufferedOutputStream(con.getOutputStream());
			new BufferedInputStream(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int getPort(URL url) {
		int port = url.getPort();
		if (-1 == port) {
			port = url.getDefaultPort();
		}
		if (-1 == port) {
			port = 80;
		}
		return port;
	}
	
	private static String getCookie(String url) {
		String cookie = null;
		try {
			int start = url.indexOf(';'), end = url.indexOf('?');
			if (-1 != start) {
				start++;
				if (-1 != end && end < start) {
					end = url.indexOf('?', start);
				}
				if (-1 != end) {
					cookie = url.substring(start, end);
				} else {
					cookie = url.substring(start);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookie;
	}
	
	private static String getRealUrl(String url) {
		try {
			String pre = null, suf = null;
			int start = url.indexOf(';'), end = url.indexOf('?');
			if (-1 != start) {
				pre = url.substring(0, start);
			}
			if (-1 != end) {
				suf = url.substring(end);
			}
			if (null != pre) {
				url = pre;
			}
			if (null != suf) {
				url += suf;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	public static void testSoap() {
		try {
			File file = new File("PurdoToHuaXiaService-soapui-project.xml");
			BufferedReader fr = new BufferedReader(new FileReader(file));
			String s = "", line = null;
			while ((line = fr.readLine()) != null) {
				s += line;
			}
			URL url = new URL("http://10.7.70.83:10660/services/HuaXiaService/PurdoToHuaXiaService");
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStream os = con.getOutputStream();
			os.write(s.getBytes());
			os.flush();
			fr = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String r = "";
			while ((line = fr.readLine()) != null) {
				r += (line + "\n");
			}
			System.out.println("send : \n" + s + "\nreceive : " + r);
		} catch (Exception e) {
			
		}
	}

	public static void testSoap(String f, String u) {
		try {
			File file = new File(f);
			BufferedReader fr = new BufferedReader(new FileReader(file));
			String s = "", line = null;
			while ((line = fr.readLine()) != null) {
				s += line;
			}
			URL url = new URL(u);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			//con.setRequestProperty("Accept-Encoding", "gzip,deflate");
			//con.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			con.setRequestProperty("SOAPAction", "");
			//((HttpURLConnection)con).setRequestMethod("POST");
			for (Entry<String, List<String>> etr : con.getRequestProperties().entrySet()) {
				System.out.println(etr.getKey() + " = " + Arrays.toString(etr.getValue().toArray()));
			}
			System.out.println("---------");
			OutputStream os = con.getOutputStream();
			os.write(s.getBytes());
			os.flush();
			for (Entry<String, List<String>> etr : con.getHeaderFields().entrySet()) {
				System.out.println(etr.getKey() + " = " + Arrays.toString(etr.getValue().toArray()));
			}
			System.out.println("---------");
			fr = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String r = "";
			while ((line = fr.readLine()) != null) {
				r += (line + "\n");
			}
			System.out.println("send : \n" + s + "\nreceive : " + r);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testHttp() {
		try {
			File file = new File("siic_auth_new.txt");
			BufferedReader fr = new BufferedReader(new FileReader(file));
			String s = "", line = null;
			while ((line = fr.readLine()) != null) {
				s += line;
			}
			URL url = new URL("http://10.4.0.100:7001/app/siic_auth_new.pl");
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStream os = con.getOutputStream();
			os.write(s.getBytes());
			os.flush();
			fr = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String r = "";
			while ((line = fr.readLine()) != null) {
				r += (line + "\n");
			}
			System.out.println("send : \n" + s + "\nreceive : " + r);
		} catch (Exception e) {
			
		}
	}
	
	public static void getSPUserByUrl(String userIp) {
		int timeMillis = (int)(System.currentTimeMillis() / 1000 - 2 * 24 * 60 * 60);
    	String login = "XXX"; // 一次认证,login用固定值
        String pwd = "";

        String authFlag = "18";
        String key = "zgdn0oh2gyac2ynw0ir";
        String fF2 = appEncrypt(login, key, timeMillis);
        String queryStr = "UserIP=" + userIp + "&Session=" + timeMillis
                + "&Login=" + login + "&Pwd=" + pwd + "&AuthFlag=" + authFlag
                + "&Sig=" + fF2;
        System.out.println(queryStr);
    }

    /**
     * @param passwdClear to set
     * @param key to set
     * @param time to set
     * @return String
     */
    public static String appEncrypt(String passwdClear, String key,int time) {
        if (time < 0){
            time = 0;
        }
        int iKeyLength;
        int iModLength;
        String sSalt;
        String sResultString;

        // Request length from key string.
        iKeyLength = key.length();
        iModLength = time % iKeyLength;
        // Build salt string
        sSalt = key.substring(iModLength, iModLength + 1)
                + key.substring(iKeyLength - 1 - iModLength, iKeyLength
                        - iModLength);
        // Just return result by calling static method
        // s_Salt = "13";
        sResultString = UnixCrypt.crypt(passwdClear, sSalt);
        return sResultString.substring(2);
    }
}
