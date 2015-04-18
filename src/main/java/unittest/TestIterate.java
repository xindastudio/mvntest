package unittest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author wuliwei
 *
 */
public class TestIterate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> l = new ArrayList<String>();
		l.add("1");
		l.add("2");
		l.add("3");
		List<String> l2 = new ArrayList<String>();
		l2.add("2");
		Iterator<String> i = l.iterator();
		while (i.hasNext()) {
			if (!l2.contains(i.next())) {
				i.remove();
			}
		}
		System.out.println(l.toString());
	}

}
