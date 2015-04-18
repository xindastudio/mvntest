package unittest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.danga.MemCached.MemCachedClient;

public class TestMemcached {

	public static void main(String[] args) {
		String[] paths = new String[] { "classpath:conf/spring/spring-memcached.xml" };
		ClassPathXmlApplicationContext cpac = new ClassPathXmlApplicationContext(
				paths);
		MemCachedClient mcc = (MemCachedClient) cpac.getBean("memCachedClient");
		boolean suc = mcc.set("keySequence", 1);
		System.out.println(suc);
		System.out.println(mcc.get("keySequence"));
		cpac.close();
	}

}
