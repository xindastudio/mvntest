package unittest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuliwei
 * 
 */
public class TestThreadLocal {
	private static final AtomicInteger uniqueId = new AtomicInteger(0);

	private static final ThreadLocal<Integer> uniqueNum = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return uniqueId.getAndIncrement();
		}
	};

	/**
	 * @return int
	 */
	public static int getCurrentThreadId() {
		return uniqueNum.get();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread[] t = new Thread[5];
		for (int i = 0; i < t.length; i++) {
			t[i] = new Thread("thread" + i) {
				public void run() {
					int id = TestThreadLocal.getCurrentThreadId();
					System.out.println(Thread.currentThread().getName()
							+ "'s id is " + id);
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					id = TestThreadLocal.getCurrentThreadId();
					System.out.println(Thread.currentThread().getName()
							+ "'s id is " + id);
				}
			};
		}
		for (int i = 0; i < t.length; i++) {
			t[i].start();
		}
	}

}
