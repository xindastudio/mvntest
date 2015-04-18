package unittest;

public class TestTimeout {
	private static int timeout = 10;
	private static volatile boolean isTimeout = false;
	private static long lastTime = System.currentTimeMillis();
	private static long lastSize = 0;
	public static Long transferredSize;

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			if (null == transferredSize) {
				transferredSize = 0L;
			}
			isTimeout = (lastTime + (timeout * 1000) < System
					.currentTimeMillis()) && (transferredSize == lastSize);
			if (transferredSize != lastSize) {
				lastTime = System.currentTimeMillis();
				lastSize = transferredSize;
			}
			System.out.println(timeout + ", " + isTimeout + ", " + lastTime
					+ ", " + lastSize + ", " + transferredSize);
			if (isTimeout) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (transferredSize < 9000) {
				transferredSize += 1000;
			}
		}
	}

}
