package unittest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * @author wuliwei
 * 
 */
public class TestHttpServer {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		InetSocketAddress addr = new InetSocketAddress(80);
		HttpServer hs = HttpServer.create(addr, 0);
		HttpHandler hh = new HttpHandler() {
			public void handle(HttpExchange exchange) throws IOException {
				for (Entry<String, List<String>> e : exchange.getRequestHeaders().entrySet()) {
					System.out.println(e.getValue() + ": " + Arrays.toString(e.getValue().toArray()));
				}
				byte[] b = new byte[0];
				int len = -1, sum = 0;
				while ((len = exchange.getRequestBody().read(b)) != -1) {
					sum += len;
				}
				System.out.println(sum);
				byte[] response = ("<?xml version=\"1.0\"?>\n<resource id=\""
						+ System.currentTimeMillis() + "\" name=\"test\" />\n")
						.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						0);
				while (true) {
					exchange.getResponseBody().write("00000009000000041024".getBytes());
					break;
				}
			}
		};
		hs.createContext("/", hh);
		hs.start();
		while (true) {
			try {
				Thread.sleep(1000 * 60);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
