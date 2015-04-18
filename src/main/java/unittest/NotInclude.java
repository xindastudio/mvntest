package unittest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class NotInclude {

	public static void main(String[] args) {
		String src1 = args[0], src2 = args[1], dest = args[2];
		BufferedReader br1 = null, br2 = null;
		BufferedWriter bw = null;
		try {
			br1 = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File(src1))));
			br2 = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File(src2))));
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(dest))));
			Set<String> s = new HashSet<String>();
			Set<String> s2 = new HashSet<String>();
			int c = 0;
			int c2 = 0;
			String l = null;
			while ((l = br1.readLine()) != null) {
				s.add(l);
				c++;
			}
			while ((l = br2.readLine()) != null) {
				if (!s.contains(l)) {
					bw.write(l + "\n");
				}
				s2.add(l);
				c2++;
			}
			bw.flush();
			System.out.println(src1 + " line count " + c + ", unique count "
					+ s.size());
			System.out.println(src2 + " line count " + c2 + ", unique count "
					+ s2.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(br1);
			close(br2);
			close(bw);
		}
	}

	private static void close(BufferedReader is) {
		if (null != is) {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void close(BufferedWriter os) {
		if (null != os) {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
