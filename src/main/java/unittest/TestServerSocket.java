package unittest;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wuliwei
 * 
 */
public class TestServerSocket {
	private static String respHead = "HTTP/1.1 200 OK"
			+ "Date: Fri, 31 Aug 2012 07:09:27 GMT"
			+ "Server: BWS/1.0"
			+ "Content-Length: 9321"
			+ "Content-Type: text/html;charset=gbk"
			+ "Cache-Control: private"
			+ "Expires: Fri, 31 Aug 2012 07:09:27 GMT"
			+ "Set-Cookie: BAIDUID=B057EE19BEFA489E6B64CE71E0D9A76B:FG=1; expires=Fri, 31-Aug-42 07:09:27 GMT; path=/; domain=.baidu.com"
			+ "P3P: CP=\" OTI DSP COR IVA OUR IND COM \"" + "Connection: Close";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ServerSocket ssoc = new ServerSocket();
		ssoc.setReuseAddress(true);
		ssoc.setSoTimeout(0);
		ssoc.bind(new InetSocketAddress("localhost", 80));
		while (true) {
			try {
				handle(ssoc.accept());
			} catch (Exception e) {

			}
		}
	}

	private static void handle(Socket soc) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = soc.getInputStream();
			byte[] b = new byte[102400];
			int len = -1;
			while ((len = is.read(b)) != -1) {
			System.out.println("len = " + len);
			System.out.println(new String(b, 0, len));}
			System.out.println("end");
			os = soc.getOutputStream();
			os.write(respHead.getBytes());
			os.flush();
			for(int i = 0; i < 10240; i++) {
				os.write('B');
				if ((1 + i) % 100 == 0) {
					os.flush();
				}
			}
			os.flush();
			os.write(b);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(soc, is, os);
		}
	}

	private static void close(Socket soc, InputStream is, OutputStream os) {
		if (null != is) {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (null != os) {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (null != soc) {
			try {
				soc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
