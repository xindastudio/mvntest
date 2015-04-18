package unittest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

/**
 * @author wuliwei
 * 
 */
public class TestEncryptDecryptUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test2();
	}

	/**
	 * 
	 */
	public static void test1() {
		try {
			String[] pubPri = EncryptDecryptUtil.genPubPriKeyBase64String();
			Key[] keys = EncryptDecryptUtil.tranPubPriKey(pubPri);
			String str = "加密前加密前加密前加密前加密前加密前加密前加密前加密前加密前加密前加密前加密前asld"
					+ "加密前加密前加密前加密前加密前加密前加密前加密前加密前加密前加密前加密前加密前asld";
			byte[] data = str.getBytes("UTF-8");
			ByteArrayInputStream is = new ByteArrayInputStream(data);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			System.out.println(str.length() + ", " + (data.length));
			System.out.println("src = " + str);
			EncryptDecryptUtil.encrypt(keys[0], is, os);
			data = os.toByteArray();
			str = new String(data, "UTF-8");
			System.out.println(str.length() + ", " + (data.length));
			System.out.println("encrypt = " + str);
			is = new ByteArrayInputStream(data);
			os = new ByteArrayOutputStream();
			// System.out.println(edata.length);
			// String temp = new String(edata, "UTF-8");
			EncryptDecryptUtil.decrypt(keys[1], is, os);
			data = os.toByteArray();
			str = new String(data, "UTF-8");
			System.out.println(str.length() + ", " + (data.length));
			System.out.println("decrypt = " + str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public static void test2() {
		try {
			File src = new File("E:/src.txt");
			File enc = new File("E:/enc.txt");
			File dec = new File("E:/dec.txt");
			if (!src.exists()) {
				src.createNewFile();
			}
			if (!enc.exists()) {
				enc.createNewFile();
			}
			if (!dec.exists()) {
				dec.createNewFile();
			}
			InputStream isSrc = new FileInputStream(src);
			OutputStream osEnc = new FileOutputStream(enc);
			InputStream isEnc = new FileInputStream(enc);
			OutputStream osDec = new FileOutputStream(dec);
			Key[] keys = EncryptDecryptUtil.genPubPriKey();
			EncryptDecryptUtil.encrypt(keys[0], isSrc, osEnc);
			EncryptDecryptUtil.decrypt(keys[1], isEnc, osDec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
