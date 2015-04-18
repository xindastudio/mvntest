package unittest;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author wuliwei
 *
 */
public class TestUdp {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String sendToHostname;
	private static int sendToPort;
	private static boolean istcp;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendToHostname = args[0];
		sendToPort = Integer.valueOf(args[1]);
		istcp = "tcp".equals(args[2].toLowerCase());
		String content = "<169>" + sdf.format(new Date()) + " test sys log";
		while (true) {
			content = "<169>" + sdf.format(new Date()) + " test sys log";
			sendLog(content);
			System.out.println("send to " + sendToHostname + ":" + sendToPort + " -->" + content);
			try { Thread.sleep(60 * 1000); } catch (Exception e) { }
		}
	}
	
	private static void sendLog(String logContent) {
		if (istcp) {
			sendLogTCP(logContent);
		} else {
			sendLogUDP(logContent);
		}
	}
	
	private static void sendLogTCP(String logContent) {
		Socket socket = null;
		OutputStream os = null;
		try {
			socket = new Socket();
			socket.setReuseAddress(true);
			socket.setSoTimeout(10 * 1000);
			socket.connect(new InetSocketAddress(sendToHostname, sendToPort), 10 * 1000);
			os = socket.getOutputStream();
			os.write(logContent.getBytes("UTF-8"));
			os.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (null != os) {
				try { os.close(); } catch (Exception e) { }
			}
			if (null != socket) {
				try { socket.close(); } catch (Exception e) { }
			}
		}
	}
	
	private static void sendLogUDP(String logContent) {
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = null;
		try {
			buf = logContent.getBytes("UTF-8");
			socket = new DatagramSocket();
			packet = new DatagramPacket(buf, buf.length, new InetSocketAddress(sendToHostname, sendToPort));
			socket.send(packet);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (null != socket) {
				try { socket.close(); } catch (Exception e) { }
			}
		}
	}

}
