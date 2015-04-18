package unittest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;


/**
 * @author wuliwei
 *
 */
public class TestUrl {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String param = "test 8-11.PNG".replaceAll(" ", "%20");
		System.out.println(param);
		String url = "http://localhost/shtel_idc_it/mrtg/images/2010/11/412/" + param;
		BufferedInputStream bis = new BufferedInputStream(new URL(url).openStream());
		File file = new File("E:/test.png");
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = bis.read(buf)) != -1) {
			bos.write(buf, 0, len);
		}
	}

}
