package unittest;
import net.sf.json.JSONObject;

/**
 * @author wuliwei
 *
 */
public class TestJSONObject {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			JSONObject json = JSONObject.fromObject(null);
			System.out.println("json is null ? " + (null == json));
			System.out.println(json.get("contry"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String object = "{}";
			JSONObject json = JSONObject.fromObject(object);
			System.out.println(json.get("contry"));
			System.out.println("json.get(\"contry\") is null ? "  + (null == json.get("contry")));
			System.out.println(json.getString("contry"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
