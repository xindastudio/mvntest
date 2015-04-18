package unittest;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * @author wuliwei
 *
 */
public class TestSocketConnectTimeout {
	private static long maxUsedTime;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ip = "www.qq.com";
		int port = 80;
		maxUsedTime = Long.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			connect(ip, port);
		}
		System.out.println(maxUsedTime + "ms, " + (maxUsedTime / 1000) + "s");
	}
	
	private static void connect(String ip, int port) {
		long connStartTime = System.currentTimeMillis();
		Socket soc = null;
		try {
			soc = new Socket();
			soc.setReuseAddress(true);
			soc.connect(new InetSocketAddress(ip, port), 1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != soc) {
				try {
					soc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		long endTime = System.currentTimeMillis();
		long usedTime = endTime - connStartTime;
		maxUsedTime = Math.max(usedTime, maxUsedTime);
		System.out.println(usedTime + "ms, " + (usedTime / 1000) + "s");
	}

}
