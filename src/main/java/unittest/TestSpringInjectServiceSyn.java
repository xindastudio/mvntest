package unittest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wuliwei
 * 
 */
public class TestSpringInjectServiceSyn {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] path = new String[] { "unittest/test.spring.xml" };
		final SpringInjectService springInjectService = (SpringInjectService) (new ClassPathXmlApplicationContext(
				path).getBean("springInjectService"));
		System.out.println("unsyn test start ...");
		for (int i = 0; i < 10; i++) {
			final int index = i;
			new Thread() {
				public void run() {
					springInjectService.unsynTest(index);
				}
			}.start();
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("unsyn test end ...");
		System.out.println("method test start ...");
		for (int i = 0; i < 10; i++) {
			final int index = i;
			new Thread() {
				public void run() {
					springInjectService.methodsynTest(index);
				}
			}.start();
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("method test end ...");
		System.out.println("object test start ...");
		for (int i = 0; i < 10; i++) {
			final int index = i;
			new Thread() {
				public void run() {
					springInjectService.objectsynTest(index);
				}
			}.start();
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("object test end ...");
	}

}
