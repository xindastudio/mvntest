package unittest;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wuliwei
 * 
 */
public class TestQuartz {

	public void start1() {
		System.out.println("start1 method called...");
	}

	public void start2() {
		System.out.println("start2 method called...");
	}

	public void start3() {
		System.out.println("start3 method called...");
	}

	/**
	 * @param args
	 * @throws SchedulerException
	 * @throws BeansException
	 */
	public static void main(String[] args) throws BeansException,
			SchedulerException {
		String[] path = new String[] { "quartzTest.spring.xml" };
		ApplicationContext ac = new ClassPathXmlApplicationContext(path);
		//((StdScheduler) (ac.getBean("odsReportUploadTaskSchedule"))).start();
		((StdScheduler) (ac.getBean("odsReportStatTaskSchedule"))).start();
	}

}
