package unittest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wuxufeng
 * 
 */
public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestClass t = new TestClass();
		TestClass t2 = new TestClass();
		System.out.println(t.getClass());
		System.out.println(t2.getClass());
		System.out.println(t.getClass() == t2.getClass());

		Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
		System.out.println(map.size());
		map.put(t.getClass(), t);
		System.out.println(map.size());
		map.put(t2.getClass(), t2);
		System.out.println(map.size());

		Set<Class<?>> set = new HashSet<Class<?>>();
		System.out.println(set.size());
		set.add(t.getClass());
		System.out.println(set.size());
		set.add(t2.getClass());
		System.out.println(set.size());

		System.out.println(Integer.class);
		System.out.println(Integer.TYPE);
		System.out.println(Integer.class.isAssignableFrom(Integer.TYPE));
		System.out.println(Integer.TYPE.isAssignableFrom(Integer.class));
		System.out.println(Integer.TYPE == Integer.class);
		System.out.println(Integer.TYPE.isInstance(new Integer(1)));
		System.out.println(Integer.class.isInstance((int) (1)));

		List<String> sl = new ArrayList<String>();
		System.out.println(Collection.class.isAssignableFrom(List.class));
		System.out.println(Collection.class.isAssignableFrom(sl.getClass()));
		System.out.println(sl instanceof Collection);
	}

}
