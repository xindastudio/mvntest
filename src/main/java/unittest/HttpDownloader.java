package unittest;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author wuliwei
 * 
 */
public class HttpDownloader extends Thread {
	private URL url;
	private int threadCount;
	private String destFilePath;
	private volatile int contentLen;
	private volatile int downLoadedDataSize;
	private volatile double usedTime;
	private volatile double curSpeed;

	/**
	 * 
	 */
	public HttpDownloader() {
		this.setDaemon(true);
	}

	/**
	 * @param theURL
	 */
	public void setUrl(URL theURL) {
		url = theURL;
	}

	/**
	 * @param threadCount
	 */
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	/**
	 * @param destFilePath
	 */
	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			File file = new File(destFilePath);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
			if (!file.createNewFile()) {
				throw new Exception("create file [" + destFilePath
						+ "] fail...");
			}
			testDownLoadSpeed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testDownLoadSpeed() {
		InputStream instream = null;
		try {
			long startTime = System.currentTimeMillis();
			URLConnection con = url.openConnection();
			String contentLength = con.getHeaderField("Content-Length");
			try {
				contentLen = Integer.valueOf(contentLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
			int bufSize = 10 * 1024;
			int blockLen = contentLen / threadCount;
			int remainder = contentLen % threadCount;
			int startPos = 0, endPos = 0;
			TestThread[] threads = new TestThread[threadCount];
			for (int i = 0; i < threadCount; i++) {
				startPos = i * blockLen;
				endPos = startPos + blockLen - 1;
				if (i == threadCount - 1) {
					endPos = endPos + remainder;
				}
				threads[i] = new TestThread(startPos, endPos, bufSize);
				threads[i].start();
			}
			boolean isFinished = true;
			int tempDownLoadedSize = 0;
			do {
				isFinished = true;
				tempDownLoadedSize = 0;
				for (int i = 0; i < threads.length; i++) {
					tempDownLoadedSize += threads[i].downLoadedDataSize;
					if (isFinished) {
						isFinished = threads[i].isFinished
								|| !threads[i].isAlive();
					}
				}
				downLoadedDataSize = tempDownLoadedSize;
				usedTime = (System.currentTimeMillis() - startTime) + 0.0D;
				curSpeed = (downLoadedDataSize * 8 * 1000D / 1024) / usedTime;
			} while (!isFinished);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class TestThread extends Thread {
		protected int startPos;
		protected int endPos;
		protected int bufSize;
		public volatile int downLoadedDataSize;
		public volatile boolean isFinished;

		public TestThread(int startPos, int endPos, int bufSize) {
			this.setDaemon(true);
			this.startPos = startPos;
			this.endPos = endPos;
			this.bufSize = bufSize;
			isFinished = false;
		}

		/**
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			testDownLoadSpeed();
			isFinished = true;
		}

		private void testDownLoadSpeed() {
			InputStream instream = null;
			try {
				URLConnection con = url.openConnection();
				con.setRequestProperty("range", "bytes=" + startPos + "-"
						+ endPos);
				instream = con.getInputStream();
				byte[] b = new byte[bufSize];
				int len = 0;
				while ((len = instream.read(b)) != -1 && !Thread.interrupted()) {
					writeFile(destFilePath, startPos + downLoadedDataSize, b, 0, len);
					downLoadedDataSize += len;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (instream != null) {
						instream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static synchronized void writeFile(String filePath, long pos, byte[] b, int off,
			int len) {
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(filePath, "rw");
			if (file.length() < pos + len) {
				file.setLength(pos + len);
				System.out.println(pos + ", " + b.length + ", " + off + ", " + len + ", " + file.length());
			}
			file.seek(pos);
			file.write(b, off, len);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != file) {
				try {
					file.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		HttpDownloader tester = new HttpDownloader();
		URL url = new URL(args[0]);
		tester.setUrl(url);
		tester.setThreadCount(Integer.valueOf(args[1]));
		tester.setDestFilePath(args[2]);
		tester.start();
		while (tester.isAlive()) {
			System.out.println("downloaded data " + tester.downLoadedDataSize
					+ " cur speed " + tester.curSpeed + " used time "
					+ tester.usedTime);
			Thread.sleep(2 * 1000);
		}
		System.out.println("downloaded data " + tester.downLoadedDataSize
				+ " cur speed " + tester.curSpeed + " used time "
				+ tester.usedTime);
	}

}
