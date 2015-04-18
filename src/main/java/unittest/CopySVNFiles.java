package unittest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @author wuliwei
 * 
 */
public class CopySVNFiles {
	private static long deleteCount = 0l;
	private static String src = null;
	private static String des = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedWriter bw = null;
		try {
			if (args != null && args.length > 2) {
				String logPath = args[(args.length - 1)] + ".log";
				File logFile = new File(logPath);
				if (!(logFile.exists())) {
					logFile.createNewFile();
				}
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile)));
				src = args[0];
				des = args[1];
				String[] rootPaths = new String[]{src};
				File root = null;
				for (String rootPath : rootPaths) {
					root = new File(rootPath);
					if ((root.exists()) && (root.isDirectory())) {
						listFile(root, bw);
					}
				}
				bw.write("delete file count : " + deleteCount);
			}
			System.out.println("delete file count : " + deleteCount);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.flush();
					bw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void listFile(File root, BufferedWriter logger)
			throws Exception {
		for (File file : root.listFiles())
			if (file.getName().toLowerCase().equals(".svn"))
				deleteFile(file, logger);
			else if (file.isDirectory())
				listFile(file, logger);
	}

	private static void deleteFile(File root, BufferedWriter logger)
			throws Exception {
		if (copyFile(root)) {
			logger.write("create file : " + root.getAbsolutePath().replaceFirst("D:", "E:") + "\r\n");
			deleteCount += 1L;
		}
		if (root.isDirectory()) {
			for (File file : root.listFiles()) {
				deleteFile(file, logger);
			}
		}
	}
	
	private static boolean copyFile(File root) {
		boolean suc = true;
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			File file = new File(root.getAbsolutePath().replaceFirst("D:", "E:"));
			if (!file.exists()) {
				if (root.isDirectory()) {
					file.mkdirs();
				} else {
					file.createNewFile();
					bis = new BufferedInputStream(new FileInputStream(root));
					bos = new BufferedOutputStream(new FileOutputStream(file));
					
					byte[] bytes = new byte[1024];
					int len = 0;
					while ((len = bis.read(bytes)) != -1) {
						bos.write(bytes, 0, len);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			suc = false;
		} finally {
			if (null != bis) {
				try { bis.close(); } catch (Exception e) {}
			}
			if (null != bos) {
				try { bos.close(); } catch (Exception e) {}
			}
		}
		
		return suc;
	}

}
