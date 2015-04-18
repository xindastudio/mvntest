package unittest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class TestSet {
	private static Map<String, String> map1 = new LinkedHashMap<String, String>();
	private static Map<String, List<String>> map2 = new LinkedHashMap<String, List<String>>();

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File f1 = new File("E:/reginMap.txt");
		File f2 = new File("E:/result_有过不达标.txt");
		File f3 = new File("E:/result_有过不达标_且最近一次也不达.txt");
		File f4 = new File("E:/result_有过不达标_但最近一次达标.txt");
		
		File d2 = new File("E:/有过不达标.txt");
		File d3 = new File("E:/有过不达标_且最近一次也不达.txt");
		File d4 = new File("E:/有过不达标_但最近一次达标.txt");
		if (!d2.exists()) {
			d2.createNewFile();
		}
		if (!d3.exists()) {
			d3.createNewFile();
		}
		if (!d4.exists()) {
			d4.createNewFile();
		}
		readMap(f1);
		copy(f2, d2);
		copy(f3, d3);
		copy(f4, d4);
		
	}
	
	private static void readMap(File file) {
		BufferedReader br = null;
		String line = null;
		String[] param = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while ((line = br.readLine()) != null) {
				param = line.split(",");
				System.out.println(param[0] + "," + param[1]);
				map1.put(param[0], param[1]);
				map2.put(param[1], new ArrayList<String>());
			}
			map1.put("null", "");
			map1.put(null, "");
			map1.put("", "");
			map2.put("", new ArrayList<String>());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try { br.close(); } catch (Exception e) { }
			}
		}
	}
	
	private static void copy(File src, File des) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		String line = null;
		String[] param = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(des)));
			for (Entry<String, List<String>> e : map2.entrySet()) {
				e.getValue().clear();
			}
			while ((line = br.readLine()) != null) {
				param = line.split(",");
				map2.get(map1.get(param[2])).add(param[0]);
			}
			for (Entry<String, List<String>> e : map2.entrySet()) {
				if (e.getValue().size() > 0) {
					for (String adno : e.getValue()) {
						bw.write(e.getKey() + "," + adno + "\n");
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != br) {
				try { br.close(); } catch (Exception e) { }
			}
			if (null != bw) {
				try { bw.close(); } catch (Exception e) { }
			}
		}
	}

}
