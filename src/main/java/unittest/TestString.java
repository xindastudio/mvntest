package unittest;

import java.util.Arrays;

/**
 * @author wuliwei
 * 
 */
public class TestString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testEquals();
	}

	private static void testEquals() {
		String a1 = "a";
		String a2 = "a";
		String b1 = String.valueOf("a");
		String b2 = new String("a");
		System.out.println("a1 == a2 ? " + (a1 == a2));
		System.out.println("b1 == b2 ? " + (b1 == b2));
		System.out.println("a1 == b1 ? " + (a1 == b1));
		System.out.println("a1 == b2 ? " + (a1 == b2));
		System.out.println("a1.equals(a2) ? " + (a1.equals(a2)));
		System.out.println("b1.equals(b2) ? " + (b1.equals(b2)));
		System.out.println("a1.equals(b1) ? " + (a1.equals(b1)));
		System.out.println("a1.equals(b2) ? " + (a1.equals(b2)));
	}

	private static void testReplace() {
		String s = "aaa\rbbb\nccc\rddd\neee\r\nfff";
		System.out.println("String = " + s);
		System.out.println(s.indexOf('\r') != -1);
		System.out.println(s.indexOf('\n') != -1);
		System.out.println("replace \\r\\n, String = "
				+ (s.replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n")));
	}

	private static void sqlStringTest() {
		System.out.println("asldf{file}..ldf{file}...{ageSecs}...{incSize}...."
				.replaceAll("\\{file\\}", "_file_")
				.replaceAll("\\{ageSecs\\}", "_ageSecs_")
				.replaceAll("\\{incSize\\}", "_incSize_"));
		// System.out.println("asldf$file$..ldf$file$...$ageSecs$...$incSize$....".replaceAll("$file$",
		// "_asldf_").replaceAll("$ageSecs$",
		// "_ageSecsasldf_").replaceAll("$incSize$", "_incSizeasldf_"));
		String s = "asdf\r\njkln";
		System.out.println(s + "," + s.replaceAll("\r\n", "\\\\r\\\\n"));
		String sql = "insert into T_SPEED_EXCPT_USER_REVIEW (REGION_CODE,SUB_REGION_CODE,"
				+ "USER_ID,USER_ACCOUNT,LINK_TYPE,OLD_SPEED_UP,OLD_SPEED_DN,"
				+ "RADIUS_SPEED_UP,RADIUS_SPEED_DN,SPEED_UP,SPEED_DOWN,EXPERIENCE_SPEED_UP,"
				+ "EXPERIENCE_SPEED_DN,IMPROVE_SPEED_UP,IMPROVE_SPEED_DN,"
				+ "CREATE_TIME,RESULT_UP,RESULT_DN,RELATE_ID,MONTH,RADIUS_97SPEED_UP,RADIUS_97SPEED_DN,ID) "
				+ "select REGION_CODE,SUB_REGION_CODE,"
				+ "USER_ID,USER_ACCOUNT,LINK_TYPE,OLD_SPEED_UP,OLD_SPEED_DN,"
				+ "RADIUS_SPEED_UP,RADIUS_SPEED_DN,SPEED_UP,SPEED_DOWN,EXPERIENCE_SPEED_UP,"
				+ "EXPERIENCE_SPEED_DN,IMPROVE_SPEED_UP,IMPROVE_SPEED_DN,"
				+ "CREATE_TIME,RESULT_UP,RESULT_DN,ID,MONTH,RADIUS_97SPEED_UP,RADIUS_97SPEED_DN,HIBERNATE_SEQUENCE.nextval "
				+ "from T_SPEED_EXCEPTIONAL_USER t1 where not exists (select 1 from T_SPEED_EXCPT_USER_REVIEW t "
				+ "where t.RELATE_ID=t1.ID) and MONTH='2013-09'";
		System.out.println(sql);
		String line = "0005256701,0,0,";
		String[] temp = line.split(",", -1);
		System.out.println(Arrays.toString(temp));
	}

	private static void test2() {
		System.out.println("1.0.2".compareTo("1.1.0"));
	}

	private static void test1() {
		parse("ad43067659@college	     512      512 2048/2048");
		System.out.println("---");
		parseRep("ad43067659@college	     512      512 2048/2048");
		System.out.println("---");
		parse("18964825859		     512     4096");
		System.out.println("---");
		parseRep("18964825859		     512     4096");
		System.out.println("---");
		parse("13301607475		       0	0");
		System.out.println("---");
		parseRep("13301607475		       0	0");
		System.out.println("---");
		parse("ad53126284		       0	0 2048/2048");
		System.out.println("---");
		parseRep("ad53126284		       0	0 2048/2048");
	}

	private static void parse(String line) {
		String[] params = new String[4];
		int i = 0;
		for (String s : line.split(" ")) {
			s = s.trim();
			if (s.isEmpty()) {
				continue;
			}
			if (i >= params.length) {
				break;
			}
			params[i++] = s;
		}
		String account = params[0];
		String radiusSpeedUp = params[1];
		String radiusSpeedDn = params[2];
		String[] temp = new String[2];
		if (null != params[3]) {
			temp = params[3].split("/");
		}
		String oldSpeedUp = temp.length > 0 ? temp[0] : null;
		String oldSpeedDn = temp.length > 1 ? temp[1] : null;
		System.out.println(Arrays.toString(params));
		System.out.println("[" + account + ", " + radiusSpeedUp + ", "
				+ radiusSpeedDn + ", " + oldSpeedUp + "/" + oldSpeedDn + "]");
	}

	private static void parseRep(String line) {
		String[] params = new String[4];
		int i = 0;
		for (String s : line.replaceAll("\t", " ").split(" ")) {
			s = s.trim();
			if (s.isEmpty()) {
				continue;
			}
			if (i >= params.length) {
				break;
			}
			params[i++] = s;
		}
		String account = params[0];
		String radiusSpeedUp = params[1];
		String radiusSpeedDn = params[2];
		String[] temp = new String[2];
		if (null != params[3]) {
			temp = params[3].split("/");
		}
		String oldSpeedUp = temp.length > 0 ? temp[0] : null;
		String oldSpeedDn = temp.length > 1 ? temp[1] : null;
		System.out.println(Arrays.toString(params));
		System.out.println("[" + account + ", " + radiusSpeedUp + ", "
				+ radiusSpeedDn + ", " + oldSpeedUp + "/" + oldSpeedDn + "]");
	}

}
