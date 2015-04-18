package unittest;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NagiosAlarmDatabasePersistence {

	public static void main(String[] args) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		try {
			File f = new File("");
			FileAppender.appendToFile("/usr/local/nagios/log.txt",
					f.getAbsolutePath() + "\n");
			if (null != args) {
				for (int i = 0; i < args.length;) {
					if (args.length > i + 1) {
						params.put(args[i], args[i + 1]);
					}
					i += 2;
				}
			}
			String charset = params.get("--charset");
			if (null != charset && charset.length() > 0) {
				for (Entry<String, String> etr : params.entrySet()) {
					etr.setValue(encode(etr.getValue(), charset));
				}
			}
			for (Entry<String, String> etr : params.entrySet()) {
				FileAppender.appendToFile("/usr/local/nagios/log.txt",
						etr.getKey() + " = " + etr.getValue() + "\n");
			}
		} catch (Exception e) {
		}
	}

	private static String encode(String src, String charset) {
		if (null == src || src.isEmpty()) {
			return src;
		}
		try {
			return new String(src.getBytes(charset), "UTF-8");
		} catch (Exception e) {
		}
		return src;
	}
}
