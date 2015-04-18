package unittest;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wuliwei
 * 
 */
public class EncryptDecryptUtil {
	private static final int MAX_ENCRYPT_BLOCK_SIZE = 117;
	private static final int MAX_DECRYPT_BLOCK_SIZE = 128;

	/**
	 * 随机生成公钥、私钥，并用Base64编码以字符串形式返回
	 * 
	 * @return {公钥, 私钥}
	 * @throws Exception
	 */
	public static String[] genPubPriKeyBase64String() throws Exception {
		String[] pubPri = new String[2];
		Key[] keys = genPubPriKey();
		pubPri[0] = Base64.encodeBase64String(keys[0].getEncoded());
		pubPri[1] = Base64.encodeBase64String(keys[1].getEncoded());
		return pubPri;
	}

	/**
	 * 将用Base64编码的字符串形式的公钥、私钥转成Key对象
	 * 
	 * @param pubPri
	 *            Base64编码字符串形式的{公钥, 私钥}
	 * @return {公钥, 私钥}
	 * @throws Exception
	 */
	public static Key[] tranPubPriKey(String[] pubPri) throws Exception {
		Key[] keys = new Key[2];
		KeyFactory kf = KeyFactory.getInstance("RSA");
		KeySpec ks = new X509EncodedKeySpec(Base64.decodeBase64(pubPri[0]));
		keys[0] = kf.generatePublic(ks);
		ks = new PKCS8EncodedKeySpec(Base64.decodeBase64(pubPri[1]));
		keys[1] = kf.generatePrivate(ks);
		return keys;
	}

	/**
	 * 随机生成公钥、私钥
	 * 
	 * @return {公钥, 私钥}
	 * @throws Exception
	 */
	public static Key[] genPubPriKey() throws Exception {
		Key[] pubPri = new Key[2];
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		KeyPair kp = kpg.generateKeyPair();
		pubPri[0] = kp.getPublic();
		pubPri[1] = kp.getPrivate();
		return pubPri;
	}

	/**
	 * @param key
	 * @param data
	 *            加密前的内容,最大长度为117字节,否则报"Data must not be longer than 117 bytes"
	 *            异常
	 * @return 加密后的内容
	 * @throws Exception
	 */
	public static byte[] encrypt(Key key, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	/**
	 * @param key
	 * @param data
	 *            解密前的内容,最大长度为128字节,否则报"Data must not be longer than 128 bytes"
	 *            异常
	 * @return 解密后的内容
	 * @throws Exception
	 */
	public static byte[] decrypt(Key key, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	/**
	 * @param key
	 * @param src
	 *            加密前的内容
	 * @param des
	 *            加密后的内容
	 * @throws Exception
	 */
	public static void encrypt(Key key, InputStream src, OutputStream des)
			throws Exception {
		byte[] b = new byte[MAX_ENCRYPT_BLOCK_SIZE], temp;
		int len = -1;
		while (-1 != (len = src.read(b))) {
			temp = new byte[len];
			System.arraycopy(b, 0, temp, 0, len);
			des.write(encrypt(key, temp));
		}
	}

	/**
	 * @param key
	 * @param src
	 *            解密前的内容
	 * @param des
	 *            解密后的内容
	 * @throws Exception
	 */
	public static void decrypt(Key key, InputStream src, OutputStream des)
			throws Exception {
		byte[] b = new byte[MAX_DECRYPT_BLOCK_SIZE], temp;
		int len = -1;
		while (-1 != (len = src.read(b))) {
			temp = new byte[len];
			System.arraycopy(b, 0, temp, 0, len);
			des.write(decrypt(key, temp));
		}
	}

}
