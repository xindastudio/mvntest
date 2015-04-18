package unittest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author wuliwei
 *
 */
public class IpIntToString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String pathSrc = "E:/ips/world-ip.txt", pathDes = "E:/ips/world-ip-new4.txt";
		List<String> ipsSrc = readIpFromFile(pathSrc);
		List<String> ipsDes = new ArrayList<String>();
		String[] ipStr = null, oldIpStr = null;
		String ip = null;
		boolean isFirst = true;
		String ipStart = null, ipEnd = null, name = null, ipStartO = null, ipEndO = null, nameO = null;
		Map<String, String> kv = new HashMap<String, String>();
		for (String s : readIpFromFile("E:/ips/country.TXT")) {
			if (null != s && s.length() > 0) {
				kv.put(s.split(",")[0], s.split(",")[1]);
			}
		}
		Map<String, List<String>> ips = new LinkedHashMap<String, List<String>>();
		for (String temp : ipsSrc) {
			try {
				if (null == temp || temp.isEmpty()) {
					continue;
				}
				ipStr = temp.split(",");
				ipStart = ipStr[0];
				if (ipStart.endsWith(".0.0.0")) {
					ipEnd = ipStart.substring(0, ipStart.length() - 6) + ".255.255.255";
				} else if (ipStart.endsWith(".0.0")) {
					ipEnd = ipStart.substring(0, ipStart.length() - 4) + ".255.255";
				} else if (ipStart.endsWith(".0")) {
					ipEnd = ipStart.substring(0, ipStart.length() - 2) + ".255";
				} else {
					ipEnd = ipStart;
				}
				name = ipStr[1];
				
				if (!isFirst) {
					ip = str_addr(Long.valueOf(oldIpStr[0]).intValue());
					ip = ip + "-" + str_addr(Long.valueOf(ipStr[0]).intValue() - 1);
					ipsDes.add(ip);
					if (ips.get(kv.get(oldIpStr[1])) == null) {
						ips.put(kv.get(oldIpStr[1]), new ArrayList<String>());
					}
					ips.get(kv.get(oldIpStr[1])).add(ip);
				} else {
					isFirst = false;
				}
				oldIpStr = ipStr;
			} catch (Exception e) {
				System.out.println(temp);
				e.printStackTrace();
			}
		}
		ip = str_addr(Long.valueOf(oldIpStr[0]).intValue());
		ipsDes.add(ip);
		if (ips.get(kv.get(oldIpStr[1])) == null) {
			ips.put(kv.get(oldIpStr[1]), new ArrayList<String>());
		}
		ips.get(kv.get(oldIpStr[1])).add(ip + "-255.255.255.255");
		ipsDes.clear();
		for (Entry<String, List<String>> etr : ips.entrySet()) {
			for (String s : etr.getValue()) {
				ipsDes.add(s + ", " + (null == etr.getKey() ? "未知" : etr.getKey()));
			}
		}
		writeIpToFile(pathDes, ipsDes);
	}
	
	private static List<String> readIpFromFile(String path) {
		List<String> l = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					l.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try { br.close(); } catch (Exception e) { }
			}
		}
		return l;
	}
	
	private static void writeIpToFile(String path, List<String> ips) {
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			if (!file.exists() || file.isDirectory()) {
				file.createNewFile();
			}
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			for (String ip : ips) {
				bw.write(ip);
				bw.write("\n");
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != bw) {
				try { bw.close(); } catch (Exception e) { }
			}
		}
	}

    /**
     * @param bytes
     * @param off
     * @return int
     */
    public static int bytesToInt(byte[] bytes, int off) {
        int addr = bytes[3 + off] & 0xFF;
        addr |= ((bytes[2 + off] << 8) & 0xFF00);
        addr |= ((bytes[1 + off] << 16) & 0xFF0000);
        addr |= ((bytes[0 + off] << 24) & 0xFF000000);
        return addr;
    }

    /**
     * @param addr
     * @return String
     */
    public static final String str_addr(int addr) {
        return new StringBuilder().append(((addr >> 24) & 0xff)).append('.')
                .append((addr >> 16) & 0xff).append('.').append(
                        (addr >> 8) & 0xff).append('.').append((addr & 0xff))
                .toString();
    }

}
