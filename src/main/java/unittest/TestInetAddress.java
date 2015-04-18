package unittest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;


/**
 * @author wuliwei
 *
 */
public class TestInetAddress {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		System.out.println(Arrays.toString(InetAddress.getByName("222.72.96.0").getAddress()));
		System.out.println(bytesToLong(InetAddress.getByName("222.72.96.0").getAddress()));
	}

    /**
     * @param readBuffer
     * @return long
     */
    public static long bytesToLong(byte[] readBuffer) {
    	System.out.println(((long) (readBuffer[0] & 255)));
    	System.out.println(((long) (readBuffer[1] & 255)));
    	System.out.println(((long) (readBuffer[2] & 255)));
    	System.out.println(((long) (readBuffer[3] & 255)));
        return (((long) (readBuffer[0] & 255) << 24)
                + ((long) (readBuffer[1] & 255) << 16)
                + ((long) (readBuffer[2] & 255) << 8) + ((long) (readBuffer[3] & 255)));
    }
}
