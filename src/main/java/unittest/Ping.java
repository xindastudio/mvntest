package unittest;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * @author wuliwei
 *
 */
public class Ping {
	private static byte[] b = new byte[32];
	static {
		for (int i = 0; i < b.length; i++) {
			b[i] = 'a';
		}
	}
	
	/**
	 * @param destIp
	 * @param port
	 * @param timeout
	 */
	public static void ping(String destIp, int port, int timeout) {
		long st = System.nanoTime(), et = st;
		byte[] recv = new byte[b.length];
		int len = -1;
		Socket soc = null;
		try {
			soc = new Socket();
			soc.setReuseAddress(true);
			soc.setSoTimeout(timeout);
			st = System.nanoTime();
			//InetAddress.getByName(destIp).isReachable(timeout);
			soc.connect(new InetSocketAddress(destIp, port), timeout);
			//soc.getOutputStream().write(b);
			//soc.getOutputStream().flush();
			//len = soc.getInputStream().read(recv);
			et = System.nanoTime();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				soc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long usedTime = et - st;
		if (usedTime >= 0) {
			System.out.println("Reply from " + destIp + ": bytes=" + len + " times=" + usedTime);
		} else {
			System.out.println("Request timed out.");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String destIp = "192.168.8.7";
		int port = 1234;
		int timeout = 2000;
		ping(destIp, port, timeout);
	}

}
