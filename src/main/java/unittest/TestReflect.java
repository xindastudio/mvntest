package unittest;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @author wuxufeng
 * 
 */
public class TestReflect {
	private Date date;
	private List<Date> dates;
	private Date[] dateArray;
	private Map<String, Date> dateMap;
	private ArrayList<Date> adates;
	private HashMap<String, Date> hdateMap;
	private Set<String> sets;

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the dates
	 */
	public List<Date> getDates() {
		return dates;
	}

	/**
	 * @param dates
	 *            the dates to set
	 */
	public void setDates(List<Date> dates) {
		this.dates = dates;
	}

	/**
	 * @return the dateArray
	 */
	public Date[] getDateArray() {
		return dateArray;
	}

	/**
	 * @param dateArray
	 *            the dateArray to set
	 */
	public void setDateArray(Date[] dateArray) {
		this.dateArray = dateArray;
	}

	/**
	 * @return the dateMap
	 */
	public Map<String, Date> getDateMap() {
		return dateMap;
	}

	/**
	 * @param dateMap
	 *            the dateMap to set
	 */
	public void setDateMap(Map<String, Date> dateMap) {
		this.dateMap = dateMap;
	}

	/**
	 * @return the adates
	 */
	public ArrayList<Date> getAdates() {
		return adates;
	}

	/**
	 * @param adates
	 *            the adates to set
	 */
	public void setAdates(ArrayList<Date> adates) {
		this.adates = adates;
	}

	/**
	 * @return the hdateMap
	 */
	public HashMap<String, Date> getHdateMap() {
		return hdateMap;
	}

	/**
	 * @param hdateMap
	 *            the hdateMap to set
	 */
	public void setHdateMap(HashMap<String, Date> hdateMap) {
		this.hdateMap = hdateMap;
	}

	/**
	 * @return the sets
	 */
	public Set<String> getSets() {
		return sets;
	}

	/**
	 * @param sets
	 *            the sets to set
	 */
	public void setSets(Set<String> sets) {
		this.sets = sets;
	}

	public static void main(String[] args) {
		// printMethod();

		try {
			Method[] ms = TestReflect.class.getMethods();
			Class<?> pt = null;
			for (Method m : ms) {
				if (m.getParameterTypes().length < 1
						|| !m.getName().startsWith("set")) {
					continue;
				}
				pt = m.getParameterTypes()[0];
				System.out.println("method : " + m);
				System.out.println("\tparameter type : " + pt);
				System.out.println("\tparameter component type : "
						+ pt.getComponentType());
				if (pt.isArray()) {
					System.out.println("\t\tcomponent type : "
							+ pt.getComponentType());
				} else if (Collection.class.isAssignableFrom(pt)
						|| Map.class.isAssignableFrom(pt)) {
					Type[] ts = m.getGenericParameterTypes();
					Type[] ats = null;
					ParameterizedType pit = null;
					for (int i = 0; i < ts.length; i++) {
						if (ParameterizedType.class.isAssignableFrom(ts[i]
								.getClass())) {
							pit = (ParameterizedType) ts[i];
							System.out.println("\t\towner type : "
									+ ((Class<?>) (pit.getOwnerType())));
							System.out.println("\t\traw type : "
									+ ((Class<?>) (pit.getRawType())));
							ats = pit.getActualTypeArguments();
							if (null == ats) {
								continue;
							}
							for (Type t : ats) {
								System.out.println("\t\t\tactual type : "
										+ ((Class<?>) t));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
