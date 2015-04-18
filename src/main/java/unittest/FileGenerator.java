package unittest;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author wuliwei
 * 
 */
public class FileGenerator {
	private static byte[] b = new byte[1024];
	static {
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		gen(Integer.valueOf(args[0]));
	}

	private static void gen(int mbCount) {
		File file = new File("file_" + mbCount + "Mbytes");
		FileOutputStream fos = null;
		try {
			if (!file.exists() || !file.isFile()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			mbCount = mbCount * 1024;
			for (int i = 0; i < mbCount; i++) {
				fos.write(b);
			}
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
