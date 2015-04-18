package unittest;

import java.lang.reflect.Method;

import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @author wuxufeng
 * 
 */
public class TestReflect {

	public static void main(String[] args) {
		printMethod();
	}

	private static void printMethod() {
		try {
			String name;
			for (Method m : ConnectionFactory.class.getMethods()) {
				name = m.getName();
				if (name.startsWith("set")) {
					name = (name.substring(3, 4).toLowerCase() + name
							.substring(4));
					System.out.println("<property name=\"" + name
							+ "\" value=\"${rabbitmq." + name + "}\" />");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
