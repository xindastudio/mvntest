package unittest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wuliwei
 * 
 */
public class TestJackson {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		long s = System.currentTimeMillis();
		Map<String, Object> obj = new HashMap<String, Object>();
		String json = null;
		try {
			obj.put("emptyArray", new String[] {});
			obj.put("stringArray", new String[] { "a", "b" });
			obj.put("num", 1);
			json = mapper.writeValueAsString(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		long e = System.currentTimeMillis();
		System.out.println(json);
		System.out.println("obj to json jackson trans use time " + (e - s));
		s = System.currentTimeMillis();
		try {
			obj = mapper.readValue(json, Map.class);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		e = System.currentTimeMillis();
		System.out.println(obj.get("num"));
		System.out.println(obj.get("stringArray"));
		System.out.println("jackson trans use time " + (e - s));
	}

}
