package unittest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * @author wuliwei
 *
 */
public class TestTimeDate {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		System.out.println(new SimpleDateFormat("MMM d HH:mm:ss", Locale.ENGLISH).format(new Timestamp(System.currentTimeMillis() - 40 * 24 * 60 * 60 * 1000)));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-06-10 17:10:09").getTime());
		System.out.println(13857009000L + 963L);
		System.out.println(new Timestamp(13857009963L));
		System.out.println(new Timestamp(1385700996003L));
		try {
		long curTime = System.currentTimeMillis();
		if (args.length > 0) {
			try { curTime = Long.valueOf(args[0]); } catch (Exception e) { curTime = System.currentTimeMillis(); }
		}
		System.out.println(curTime + " : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date(curTime)));
		
		String dateStr = "2010-11-09";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
		System.out.println(dateStr + " max day of month : " + cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		System.out.println("date : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-4-19 14:00:30")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long time = 1324186327l * 1000;
		System.out.println("1324186327 * 1000[" + time + "] to timestamp is : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Timestamp(time)));
		
		time = 1324175483l * 1000;
		System.out.println("1324175483 * 1000[" + time + "] to timestamp is : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Timestamp(time)));
		
		time = 1324184437l * 1000;
		System.out.println("1324184437 * 1000[" + time + "] to timestamp is : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Timestamp(time)));
		
		time = 1324180735l * 1000;
		System.out.println("1324180735 * 1000[" + time + "] to timestamp is : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Timestamp(time)));
	}

}
