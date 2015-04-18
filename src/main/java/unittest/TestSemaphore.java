package unittest;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author wuliwei
 * 
 */
public class TestSemaphore {
	private static Semaphore sem;
	private static int MAX_WAIT_COUNT;
	private static int MAX_WAIT_TIME;
	private static int handleUsedTime;
	private static long st;

	private static void acquire() {
		int ql = sem.getQueueLength(), ap = sem.availablePermits();
		echo(Thread.currentThread().getName(), "entry", ql, ap);
		if (ql >= MAX_WAIT_COUNT) {
			echo(Thread.currentThread().getName(), "fail", ql, ap);
			return;
		}
		try {
			if (!sem.tryAcquire(MAX_WAIT_TIME, TimeUnit.SECONDS)) {
				ql = sem.getQueueLength();
				ap = sem.availablePermits();
				echo(Thread.currentThread().getName(), "timeout", ql, ap);
				return;
			}
		} catch (Exception e) {
			// 正常情况下不会抛出InterruptedException
			// 如果抛出异常，以此次资源不可用处理，直接返回
			e.printStackTrace();
			ql = sem.getQueueLength();
			ap = sem.availablePermits();
			echo(Thread.currentThread().getName(), "error", ql, ap);
			return;
		}
		ql = sem.getQueueLength();
		ap = sem.availablePermits();
		echo(Thread.currentThread().getName(), "success", ql, ap);
		try {
			Thread.sleep(handleUsedTime * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sem.release();
		}
		ql = sem.getQueueLength();
		ap = sem.availablePermits();
		echo(Thread.currentThread().getName(), "finished", ql, ap);
	}

	private static void echo(String name, String result, int ql, int ap) {
		System.out.println("[" + (System.currentTimeMillis() - st) / 1000 + "]"
				+ name + " acquire " + result + ", available permits is " + ap
				+ ", queue size is " + ql);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int permits = 2;
		boolean fair = true;
		sem = new Semaphore(permits, fair);
		MAX_WAIT_COUNT = 2;
		MAX_WAIT_TIME = 40;
		handleUsedTime = 60;
		st = System.currentTimeMillis();
		for (int i = 0; i < 7; i++) {
			new Thread("thread[" + i + "]") {
				public void run() {
					acquire();
				}
			}.start();
			try {
				Thread.sleep(10 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
