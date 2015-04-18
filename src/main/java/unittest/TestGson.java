package unittest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TestGson {

	public static void main(String[] args) {
		Map<String, String[]> map = new HashMap<String, String[]>();
		String key = "key1";
		String[] value = new String[] {};
		map.put(key, value);
		key = "key2";
		value = new String[] { "s1", "s2" };
		map.put(key, value);
		key = "key3";
		value = new String[] { "s3", "s4" };
		map.put(key, value);
		System.out.println(new Gson().toJson(map));

		String dest = "{\"key3\":[\"s3\",\"s4\"],\"key2\":[\"s1\",\"s2\"],\"key1\":[]}";
		Map<String, String[]> map2 = new Gson().fromJson(dest,
				new TypeToken<Map<String, String[]>>() {
				}.getType());
		for (Entry<String, String[]> etr : map2.entrySet()) {
			System.out.println(etr.getKey() + " = "
					+ Arrays.toString(etr.getValue()));
		}
	}

}
