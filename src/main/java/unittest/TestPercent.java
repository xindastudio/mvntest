package unittest;
/**
 * @author wuliwei
 * 
 */
public class TestPercent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test1(1111, 333, 0.991f);
		test1(1111, 7, 0.991f);
		test1(1111, 0, 0.991f);
		test1(1111, 1111, 0.991f);
	}

	private static void test1(int tc, int uc, float p) {
		int qc = tc - uc;
		float up = uc * 1.0f / tc, qp = qc * 1.0f / tc;
		float tp = 1 - p;
		System.out.println(up + ", " + qp + ", " + (up + qp) + ", " + tp);
		int rtc = tc, ruc = uc, rqc = qc;
		if (qc > 0 && qp < p) {
			rtc = (int) (qc * 1.0f / p);
			ruc = rtc - rqc;
		} else if (uc > 0 && qp > p) {
			rtc = (int) (uc * 1.0f / tp);
			rqc = rtc - ruc;
		}
		System.out.println(tc + ", " + uc + ", " + qc);
		System.out.println(rtc + ", " + ruc + ", " + rqc);
		System.out.println((ruc * 1.0f / rtc) + ", " + (rqc * 1.0f / rtc));
		System.out.println();
	}

}
