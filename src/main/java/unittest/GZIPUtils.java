package unittest;
/**
 * 
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

/**
 * @author admin
 *
 */
public class GZIPUtils {
	
	private static Logger logger = Logger.getLogger(GZIPUtils.class);
	
	public static void gzipFile(String srcFilePath, String desFilePath){
		try 
		{ 
			FileInputStream fin=new FileInputStream(new File(srcFilePath)); 
			FileOutputStream fout=new FileOutputStream(new File(desFilePath)); 
			GZIPOutputStream gzout=new GZIPOutputStream(fout); 
			byte[] buf=new byte[1024];
			int num; 
	
			while ((num=fin.read(buf)) != -1) 
			{ 
				gzout.write(buf,0,num); 
			} 
			gzout.close();
			fout.close(); 
			fin.close(); 
		}catch(IOException e) 
		{ 
			logger.error(e);
		} 
	}
	
	public static void main(String[] args) throws Exception {
		String path = "F:/";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Timestamp endTime = new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
		String srcPath = path+"SH_speedtest_"+sdf.format(endTime)+".csv";
		String desPath = path+"SH_speedtest222_"+sdf.format(endTime)+".csv.gz";
		new File(srcPath).createNewFile();
		//new File(desPath).createNewFile();
		gzipFile(srcPath, desPath);

		srcPath = path + "SH_httptest_"+ sdf.format(endTime) + ".csv";
		desPath = path + "SH_httptest222_"+ sdf.format(endTime) + ".csv.gz";
		new File(srcPath).createNewFile();
		gzipFile(srcPath, desPath);
	}
}
