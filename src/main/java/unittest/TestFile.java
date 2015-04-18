package unittest;
import java.io.File;

public class TestFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String file = "F:\\/log.txt";
		System.out.println(file + new File("F:\\/log.txt").exists());
	}

	private static void filePro() throws Exception {
		System.out.println(System.getProperty("file.separator"));
		File f = new File("E:/home/huaxia/user_data_sys_app/test_app/log.log");
		System.out.println("f.getParent() : " + f.getParent());
		System.out.println("f.getParentFile().getName() : "
				+ f.getParentFile().getName());
		System.out.println("f.getAbsolutePath() : " + f.getAbsolutePath());
		System.out.println("f.getCanonicalPath() : " + f.getCanonicalPath());
		System.out.println("f.getPath() : " + f.getPath());
		System.out.println("f.getName() : " + f.getName());
		System.out.println("f.getTotalSpace() : " + f.getTotalSpace());
		System.out.println("f.getUsableSpace() : " + f.getUsableSpace());
		System.out.println("f.getFreeSpace() : " + f.getFreeSpace());

		System.out.println("-->空路径\n");
		f = new File("");
		System.out.println("f.getParent() : " + f.getParent());
		System.out.println("f.getAbsolutePath() : " + f.getAbsolutePath());
		System.out.println("f.getCanonicalPath() : " + f.getCanonicalPath());
		System.out.println("f.getPath() : " + f.getPath());
		System.out.println("f.getName() : " + f.getName());
		System.out.println("f.getTotalSpace() : " + f.getTotalSpace());
		System.out.println("f.getUsableSpace() : " + f.getUsableSpace());
		System.out.println("f.getFreeSpace() : " + f.getFreeSpace());
	}

	private static void renamefile() {
		File file = new File("E:/testrename.txt");
		File dest = new File(file.getAbsolutePath()
				+ System.currentTimeMillis());
		System.out.println(dest.getAbsolutePath() + " is exists ? "
				+ dest.exists() + ", file " + file.getName() + " is file ? "
				+ file.isFile());
		if (file.exists() && file.isFile() && file.renameTo(dest)) {
			System.out.println(file.getAbsolutePath() + " is exists ? "
					+ file.exists());
			System.out.println(dest.getAbsolutePath() + " is exists ? "
					+ dest.exists());
		}
	}

	private static void renamefile(String[] args) {
		if (null != args && args.length > 0) {
			String folderPath = args[0];
			File folder = new File(folderPath);
			if (folder.exists() && folder.isDirectory()) {
				String fs = System.getProperty("file.separator");
				String regex = "_bak";
				for (File f : folder.listFiles()) {
					if (f.isFile() && f.getName().endsWith(regex)) {
						f.renameTo(new File(f.getParent() + fs
								+ f.getName().replaceAll(regex, "")));
					}
				}
			}
		}
	}

}
