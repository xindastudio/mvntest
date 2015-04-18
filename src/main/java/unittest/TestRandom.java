package unittest;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author wuliwei
 * 
 */
public class TestRandom {
	private static int maxUnqualifyCount = 40;

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
		int noTotalCount = 300;
		int dif = noTotalCount < maxUnqualifyCount ? noTotalCount
				: maxUnqualifyCount;
		Set<Integer> dataindex = new TreeSet<Integer>();
		Random r = new Random();
		int next = -1;
		for (int i = 0; i < dif; i++) {
			next = r.nextInt(noTotalCount);
			if (!dataindex.contains(next)) {
				dataindex.add(next);
			} else {
				i--;
			}
		}
		System.out.println(dataindex.size());
		System.out.println(Arrays.toString(dataindex.toArray()));
	}

	/**
	 * 
	 */
	public static void test2() {
		float minQualify = 0.985f;
		float maxQualify = 0.988f;
		int mini = (int) (minQualify * 1000), maxi = (int) (maxQualify * 1000);
		int max = maxi - mini + 1;
		Random r = new Random();
		float minf = Float.MAX_VALUE, maxf = Float.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int addi = r.nextInt(max);
			int curi = mini + addi;
			float curQualify = curi / 1000f;
			System.out.println(max + ", " + addi + ", " + curi + ", "
					+ curQualify);
			minf = Math.min(curQualify, minf);
			maxf = Math.max(curQualify, maxf);
		}
		System.out.println("min : " + minf + ", max : " + maxf);
	}

	/**
	 * 
	 */
	public static void test3() {
		float minQualify = 0.985f;
		float maxQualify = 0.992f;
		int max = (int) ((maxQualify - minQualify) * 1000) + 1;
		System.out.println((maxQualify - minQualify) + ", "
				+ ((maxQualify - minQualify) * 1000) + ", " + max);
	}

	/**
	 * 
	 */
	public static void test4() {
		Random r = new Random();
		int[] arr = new int[10];
		for (int j = 0; j < 5; j++) {
			r = new Random();
			for (int i = 0; i < arr.length; i++) {
				arr[i] = r.nextInt(100);
			}
			System.out.println(Arrays.toString(arr));
		}
		long seed = System.nanoTime();
		for (int j = 0; j < 5; j++) {
			r = new Random(seed);
			for (int i = 0; i < arr.length; i++) {
				arr[i] = r.nextInt(100);
			}
			System.out.println(Arrays.toString(arr));
		}
	}
}
