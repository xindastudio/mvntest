package unittest;
import java.io.File;
import java.io.FileWriter;

/**
 * @author wuliwei
 * 
 */
public class FileAppender {

	/**
	 * @param filePath
	 * @param content
	 */
	public static synchronized void appendToFile(String filePath, String content) {
		FileWriter bw = null;
		try {
			File f = new File(filePath);
			if (!f.exists() || f.isDirectory()) {
				f.createNewFile();
			}
			bw = new FileWriter(f, true);
			bw.write(content);
			bw.flush();
		} catch (Exception e) {
		} finally {
			if (null != bw) {
				try {
					bw.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
