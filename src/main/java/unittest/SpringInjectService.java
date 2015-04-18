package unittest;

/**
 * @author wuliwei
 *
 */
public class SpringInjectService {
	/**
	 * @param index
	 */
	public void unsynTest(int index) {
		System.out.println("thread " + index + " enter...");
		try { Thread.sleep(500); } catch (Exception e) { e.printStackTrace(); }
		System.out.println("thread " + index + " leave...");
	}

	/**
	 * @param index
	 */
	public synchronized void methodsynTest(int index) {
		System.out.println("thread " + index + " enter...");
		try { Thread.sleep(500); } catch (Exception e) { e.printStackTrace(); }
		System.out.println("thread " + index + " leave...");
	}

	/**
	 * @param index
	 */
	public void objectsynTest(int index) {
		synchronized (this) {
			System.out.println("thread " + index + " enter...");
			try { Thread.sleep(500); } catch (Exception e) { e.printStackTrace(); }
			System.out.println("thread " + index + " leave...");
		}
	}
}
