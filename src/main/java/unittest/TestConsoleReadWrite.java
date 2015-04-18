package unittest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @author wuliwei
 *
 */
public class TestConsoleReadWrite {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			String msg = null;
			do {
				msg = br.readLine();
				System.out.println(msg);
			} while (!"bye".equals(msg));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
