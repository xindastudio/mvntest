package unittest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author wuliwei
 * 
 */
public class TestSocket {
	private static Socket soc;
	private static BufferedReader br;
	private static BufferedWriter bw;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				connect();
			}
		}.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while (true) {
			try {
				line = br.readLine();
				bw.write(line + "\n");
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void socDefValTest() {
		String host = "218.1.60.50";
		int port = 8080;
		int timeout = 20000;
		Socket soc = null;
		try {
			soc = new Socket();
			System.out.println("before connect...");
			System.out.println("KeepAlive = " + soc.getKeepAlive());
			System.out.println("ReceiveBufferSize = " + soc.getReceiveBufferSize());
			System.out.println("ReuseAddress = " + soc.getReuseAddress());
			System.out.println("SendBufferSize = " + soc.getSendBufferSize());
			System.out.println("SoLinger = " + soc.getSoLinger());
			System.out.println("SoTimeout = " + soc.getSoTimeout());
			System.out.println("TcpNoDelay = " + soc.getTcpNoDelay());
			soc.connect(new InetSocketAddress(host, port), timeout);
			System.out.println("after connect...");
			System.out.println("KeepAlive = " + soc.getKeepAlive());
			System.out.println("ReceiveBufferSize = " + soc.getReceiveBufferSize());
			System.out.println("ReuseAddress = " + soc.getReuseAddress());
			System.out.println("SendBufferSize = " + soc.getSendBufferSize());
			System.out.println("SoLinger = " + soc.getSoLinger());
			System.out.println("SoTimeout = " + soc.getSoTimeout());
			System.out.println("TcpNoDelay = " + soc.getTcpNoDelay());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(soc);
		}
	}
	
	private static void test() {
		for (int i = 0; i < 1000; i++) {
			new Thread("test-" + i) {
				public void run() {
					connect();
				}
			}.start();
		}
	}

	private static void connect() {
		String host = "localhost";
		int port = 2222;
		int timeout = 20000;
		long st = System.currentTimeMillis();
		boolean fail = false;
		try {
			soc = new Socket();
			soc.setSoTimeout(timeout);
			soc.setReuseAddress(true);
			soc.connect(new InetSocketAddress(host, port), timeout);
			br = getReader(soc);
			bw = getWriter(soc);
			String line = null;
			while (null != (line = read())) {
				System.out.println(line);
				if (line.startsWith("t")) {
					Thread.sleep(234);
					long t = System.currentTimeMillis();
					Thread.sleep(334);
					bw.write("t" + t + "\n");
					bw.flush();
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			fail = true;
		} finally {
			close(soc);
		}
		long et = System.currentTimeMillis();
		System.out.println(Thread.currentThread().getName() + " used time "
				+ (et - st) + ", " + fail);
	}

	private static String read() {
		try {
			return br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedReader getReader(Socket soc) {
		try {
			return new BufferedReader(new InputStreamReader(
					soc.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedWriter getWriter(Socket soc) {
		try {
			return new BufferedWriter(new OutputStreamWriter(
					soc.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void close(Socket soc) {
		try {
			if (null != soc) {
				soc.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
