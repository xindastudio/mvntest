package unittest;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuliwei
 *
 */
public class TestThreadPoolExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		callerRunsPolicyTest();
	}
	
	/**
	 * 
	 */
	public static void callerRunsPolicyTest() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(2),
				new ThreadPoolExecutor.CallerRunsPolicy());
		for (int i = 0; i < 4; i++) {
			final int index = i;
			System.out.println("thread " + index + " execute start...");
			executor.execute(new Runnable() {
				public void run() {
					System.out.println("thread " + index + " sleep 1 minute start...");
					try {
						Thread.sleep(1 * 60 * 1000);
					} catch (Exception e) {
						
					}
					System.out.println("thread " + index + " sleep 1 minute end...");
				}
			});
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				
			}
			System.out.println("thread " + index + " execute end...");
		}
		for (int i = 4; i < 6; i++) {
			final int index = i;
			System.out.println("thread " + index + " execute start...");
			executor.execute(new Runnable() {
				public void run() {
					System.out.println("thread " + index + " sleep 1 second start...");
					try {
						Thread.sleep(1 * 1000);
					} catch (Exception e) {
						
					}
					System.out.println("thread " + index + " sleep 1 second end...");
				}
			});
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				
			}
			System.out.println("thread " + index + " end start...");
		}
	}
}
