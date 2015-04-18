package unittest;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author wuliwei
 * 
 */
public class TestSemaphoreJavaBug {
	/**
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable {
		unreturnTest();
	}

	/**
	 * @throws Throwable
	 */
	public static void leakMemoryTest() throws Throwable {
		final int n = 1000 * 1000;

		long mem = Runtime.getRuntime().freeMemory();
		System.out.println("Free: " + mem);

		Semaphore s = new Semaphore(0);
		int i = 0;
		try {
			while (++i < n)
				s.tryAcquire(1, TimeUnit.MICROSECONDS);
		} catch (OutOfMemoryError oome) {
			System.out.printf("OOME on iteration %d%n", i);
		}

		System.gc();
		long mem2 = Runtime.getRuntime().freeMemory();
		System.out.println("Memory used: " + (mem2 - mem));
	}

	/**
	 * @throws Throwable
	 */
	public static void unreturnTest() throws Throwable {
		Semaphore sem = new Semaphore(0, true);
		System.out.println(sem.tryAcquire(100, TimeUnit.MILLISECONDS));
		System.out.println(sem.tryAcquire(100, TimeUnit.MILLISECONDS));
		System.out.println("OK!");
	}

}
