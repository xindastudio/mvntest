package unittest;
import java.util.ArrayList;
import java.util.List;

import cn.com.common.util.concurrent.QueuingMechanismProxy;
import cn.com.common.util.concurrent.QueuingResult;

/**
 * @author wuliwei
 * 
 */
public class TestSemaphoreProxy {
	private static int handleUsedTime;
	private static long st;

	public static void handle() {
		try {
			Thread.sleep(handleUsedTime * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int handle(Integer p1) {
		handle();
		return p1;
	}

	private static List<Integer> handle(int p1, List<Integer> p2) {
		handle();
		p2.add(p1);
		return p2;
	}

	public static String handle(Integer p1, List<Integer> p2, List<String> p3) {
		handle();
		p2.add(p1);
		return "" + p2.size() + "," + p3.size();
	}

	private static void echo(String result, int ap, int ql, Throwable t,
			Object r) {
		System.out.println("["
				+ (System.currentTimeMillis() - st)
				/ 1000
				+ "]"
				+ Thread.currentThread().getName()
				+ " acquire "
				+ result
				+ ", available permits is "
				+ ap
				+ ", queue size is "
				+ ql
				+ (null == r ? "" : ", return type is " + r.getClass()
						+ " and value is " + r));
		if (null != t) {
			t.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int permits = 2;
		boolean fair = true;
		int maxWaitCount = 2;
		long maxWaitTime = 38 * 1000;
		final QueuingMechanismProxy proxy = new QueuingMechanismProxy(permits,
				fair, maxWaitCount, maxWaitTime);
		handleUsedTime = 60;
		st = System.currentTimeMillis();
		int i = 0;
		new Thread("thread[" + (i++) + "]") {
			public void run() {
				QueuingResult pr = proxy.invoke(new TestSemaphoreProxy(),
						"handle", new Class[] {}, new Object[] {});
				echo(pr.getResult(), pr.getCurPermits(), pr.getCurQueueSize(),
						pr.getThrowable(), pr.getInvokeReturn());
			}
		}.start();
		sleep();
		new Thread("thread[" + (i++) + "]") {
			public void run() {
				QueuingResult pr = proxy.invoke(new TestSemaphoreProxy(),
						"handle", new Class[] { Integer.class },
						new Object[] { 1 });
				echo(pr.getResult(), pr.getCurPermits(), pr.getCurQueueSize(),
						pr.getThrowable(), pr.getInvokeReturn());
			}
		}.start();
		sleep();
		for (; i < 6; i++) {
			new Thread("thread[" + i + "]") {
				public void run() {
					QueuingResult pr = proxy.invoke(new TestSemaphoreProxy(),
							"handle", new Class[] { Integer.TYPE, List.class },
							new Object[] { 3, new ArrayList<Integer>() });
					echo(pr.getResult(), pr.getCurPermits(), pr
							.getCurQueueSize(), pr.getThrowable(), pr
							.getInvokeReturn());
				}
			}.start();
			sleep();
		}
		new Thread("thread[" + (i++) + "]") {
			public void run() {
				QueuingResult pr = proxy.invoke(new TestSemaphoreProxy(),
						"handle", new Class[] { Integer.class, List.class,
								List.class }, new Object[] { 6,
								new ArrayList<Integer>(),
								new ArrayList<String>() });
				echo(pr.getResult(), pr.getCurPermits(), pr.getCurQueueSize(),
						pr.getThrowable(), pr.getInvokeReturn());
			}
		}.start();
		sleep();
		sleep();
		sleep();
		new Thread("thread[" + (i++) + "]") {
			public void run() {
				QueuingResult pr = proxy.invoke(new TestSemaphoreProxy(),
						"equals", new Class[] { Object.class },
						new Object[] { 6 });
				echo(pr.getResult(), pr.getCurPermits(), pr.getCurQueueSize(),
						pr.getThrowable(), pr.getInvokeReturn());
			}
		}.start();
		sleep();
	}

	private static void sleep() {
		try {
			Thread.sleep(10 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
