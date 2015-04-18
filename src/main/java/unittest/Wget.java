package unittest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Wget {

	public static void main(String[] args) {
		InputStream is = null;
		BufferedReader br = null;
		try {
			String url = args[0];
			is = new URL(url).openStream();
			br = new BufferedReader(new InputStreamReader(is));
			String l = null;
			while ((l = br.readLine()) != null) {
				System.out.println(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
