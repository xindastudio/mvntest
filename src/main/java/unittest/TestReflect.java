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
	private List<Date> dates;
	private Date[] dateArray;
	private Map<String, Date> dateMap;
	private ArrayList<Date> adates;
	private HashMap<String, Date> hdateMap;
	private Set<String> sets;

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
			for (Method m : ms) {
				if (m.getParameterTypes().length < 1
						|| !m.getName().startsWith("set")) {
					continue;
				}
				System.out.println("method : " + m);
				System.out.println("\tparameter type : "
						+ m.getParameterTypes()[0]);
				System.out.println("\tparameter component type : "
						+ m.getParameterTypes()[0].getComponentType());
				if (m.getParameterTypes()[0].isArray()) {
					System.out.println("\t\tcomponent type : "
							+ (m.getParameterTypes()[0]).getComponentType());
				} else if (Collection.class == (m.getParameterTypes()[0])
						|| Collection.class.isAssignableFrom(m
								.getParameterTypes()[0])
						|| Map.class == (m.getParameterTypes()[0])
						|| Map.class.isAssignableFrom(m.getParameterTypes()[0])) {
					Type[] ts = m.getGenericParameterTypes();
					Type[] ats = null;
					for (int i = 0; i < ts.length; i++) {
						if (ParameterizedType.class.isAssignableFrom(ts[i]
								.getClass())) {
							System.out.println("\t\towner type : "
									+ ((Class<?>) (((ParameterizedType) ts[i])
											.getOwnerType())));
							System.out.println("\t\traw type : "
									+ ((Class<?>) (((ParameterizedType) ts[i])
											.getRawType())));
							ats = ((ParameterizedType) ts[i])
									.getActualTypeArguments();
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
