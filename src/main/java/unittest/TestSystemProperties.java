package unittest;
import java.util.Map.Entry;


/**
 * @author wuliwei
 *
 */
public class TestSystemProperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (Entry<Object, Object> e : System.getProperties().entrySet()) {
			System.out.println(e.getKey() + " = " + e.getValue());
		}
	}

}
