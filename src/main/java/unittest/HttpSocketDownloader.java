package unittest;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author wuliwei
 * 
 */
public class HttpSocketDownloader {
	private int timeout;
	private boolean useGzip;
	private volatile boolean stop;
	private URL url;
	private String host;
	private int port;
	private String path;
	private long startPosition;
	private long endPosition;
	private List<String> headers;
	private Map<String, String> headerMap;
	private List<Byte> content;
	private long downloadSize;
	private String responseCode;
	private boolean isText;
	private String charset;

	/**
	 * 
	 */
	public HttpSocketDownloader() {
		timeout = 30000;
		useGzip = true;
		stop = false;
		url = null;
		host = null;
		port = 80;
		path = null;
		startPosition = -1;
		endPosition = -1;
		headers = new ArrayList<String>();
		headerMap = new LinkedHashMap<String, String>();
		content = new ArrayList<Byte>();
		downloadSize = 0;
		responseCode = null;
		isText = false;
		charset = "UTF-8";
	}

	/**
	 * @param pTimeout
	 */
	public void setTimeout(int pTimeout) {
		timeout = pTimeout;
	}

	/**
	 * @param pUseGzip
	 */
	public void setUseGzip(boolean pUseGzip) {
		useGzip = pUseGzip;
	}

	/**
	 * @param pStop
	 */
	public void setStop(boolean pStop) {
		stop = pStop;
	}

	/**
	 * @param pUrl
	 */
	public void setUrl(URL pUrl) {
		url = pUrl;
		host = url.getHost();
		port = -1 != url.getPort() ? url.getPort()
				: -1 != url.getDefaultPort() ? url.getDefaultPort() : 80;
		path = url.getPath().isEmpty() ? "/" : url.getPath();
	}

	/**
	 * @param pStartPosition
	 */
	public void setStartPosition(long pStartPosition) {
		startPosition = pStartPosition;
	}

	/**
	 * @param pEndPosition
	 */
	public void setEndPosition(long pEndPosition) {
		endPosition = pEndPosition;
	}

	private void reset() {
		headers.clear();
		headerMap.clear();
		content.clear();
		downloadSize = 0;
		responseCode = null;
		isText = false;
		charset = "UTF-8";
	}

	/**
	 * 
	 */
	public void start() {
		try {
			while (!stop) {
				reset();
				download();
				if (isRedirect()) {
					setUrl(new URL(headerMap.get("Location")));
					continue;
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		stop = true;
		for (String header : headers) {
			System.out.println(header);
		}
		if (isText) {
			boolean charsetMatched = false;
			int start = -1, end = -1;
			byte[] b = new byte[content.size()];
			for (int i = 0; i < b.length; i++) {
				b[i] = content.get(i);
				if (!charsetMatched) {
					if ('<' == b[i]) {
						start = i;
					} else if ('>' == b[i] && -1 != start) {
						end = i;
						charsetMatched = parseCharset(b, start, end);
						start = end = -1;
					}
				}
			}
			try {
				System.out.println(new String(b, charset));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void download() {
		Socket soc = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			soc = new Socket();
			soc.setReuseAddress(true);
			soc.setSoTimeout(timeout);
			soc.connect(new InetSocketAddress(host, port), timeout);
			os = soc.getOutputStream();
			is = soc.getInputStream();
			sendRequest(os);
			if (parseResponseHeaders(is)) {
				if (isGzip()) {
					is = new GZIPInputStream(is);
				}
				parseResponseContent(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(soc, is, os);
		}
	}

	private void sendRequest(OutputStream os) throws Exception {
		os.write(generateRequestHeaders(host, port, path).getBytes(charset));
		os.flush();
	}

	private String generateRequestHeaders(String host, int port, String path) {
		StringBuilder sb = new StringBuilder();
		String row = "\r\n";
		sb.append("GET ").append(path).append(" HTTP/1.1").append(row);
		sb.append("Host: ").append(host).append(80 != port ? ":" + port : "")
				.append(row);
		if (-1 != startPosition || -1 != endPosition) {
			sb.append("range: bytes=").append(
					-1 != startPosition ? startPosition : "").append('-')
					.append(-1 != endPosition ? endPosition : "").append(row);
		}
		if (useGzip) {
			sb.append("Accept-Encoding: gzip,deflate").append(row);
		}
		sb.append("Referer: http://").append(host).append(
				80 != port ? ":" + port : "").append('/').append(row);
		sb.append("Connection: Close").append(row);
		sb.append(row).append(row);
		return sb.toString();
	}

	private boolean parseResponseHeaders(InputStream is) throws Exception {
		List<Byte> headBytes = new ArrayList<Byte>();
		String header = null, split = ": ", key = null, value = null;
		int b = -1;
		boolean contentTypeParsed = false, responseCodeParsed = false;
		while ((b = is.read()) != -1) {
			if ('\r' != b && '\n' != b) {
				headBytes.add((byte) b);
			} else if ('\n' == b) {
				byte[] temp = new byte[headBytes.size()];
				for (int j = 0; j < temp.length; j++) {
					temp[j] = headBytes.get(j);
				}
				headBytes.clear();
				header = new String(temp);
				headers.add(header);
				if (header.isEmpty()) {
					return true;
				}
				if (!responseCodeParsed) {
					responseCodeParsed = parseResponseCode(header);
				}
				if (-1 != header.indexOf(split)) {
					key = header.substring(0, header.indexOf(split));
					value = header.substring(key.length() + split.length());
					headerMap.put(key, value);
					if (!contentTypeParsed) {
						contentTypeParsed = parseContentType(key, value);
					}
				}
			}
		}
		return false;
	}

	private boolean parseResponseCode(String header) {
		String prefix = "http/";
		if (header.toLowerCase().startsWith(prefix)) {
			int beginIndex = header.indexOf(' ', prefix.length());
			beginIndex = -1 != beginIndex ? beginIndex + 1 : -1;
			int endIndex = -1 != beginIndex ? header.indexOf(' ', beginIndex)
					: -1;
			if (-1 != beginIndex && -1 != endIndex) {
				responseCode = header.substring(beginIndex, endIndex);
			}
			return true;
		}
		return false;
	}

	private boolean parseContentType(String key, String value) {
		String tempKey = key.toLowerCase(), tempValue = value.toLowerCase();
		if ("content-type".equals(tempKey) && tempValue.startsWith("text/")) {
			isText = true;
			int beginIndex = tempValue.indexOf("charset=");
			if (-1 != beginIndex) {
				beginIndex = beginIndex + "charset=".length();
				int endIndex = tempValue.indexOf(';', beginIndex);
				if (-1 == endIndex) {
					endIndex = tempValue.length();
				}
				charset = value.substring(beginIndex, endIndex);
			} else {
				charset = "UTF-8";
			}
			return true;
		}
		return false;
	}

	private boolean isRedirect() {
		return null != responseCode && responseCode.startsWith("3");
	}

	private boolean isGzip() {
		String contentEncoding = headerMap.get("Content-Encoding");
		return null != contentEncoding
				&& contentEncoding.toLowerCase().contains("gzip");
	}

	private void parseResponseContent(InputStream is) throws Exception {
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = is.read(b)) != -1 && !stop) {
			for (int i = 0; i < len; i++) {
				content.add(b[i]);
			}
			downloadSize += len;
		}
	}

	private boolean parseCharset(final byte[] b, int start, int end) {
		try {
			String meta = new String(b, start, end + 1 - start, charset)
					.replaceAll("'", "\"");
			String temp = meta.toLowerCase(), str1 = " content=\"text/html;", str2 = "charset=", str3 = "\"";
			if (temp.startsWith("<meta ")) {
				int beginIndex = temp.indexOf(str1);
				beginIndex = -1 != beginIndex ? temp.indexOf(str2, beginIndex)
						: -1;
				beginIndex = -1 != beginIndex ? str2.length() + beginIndex : -1;
				int endIndex = -1 != beginIndex ? temp
						.indexOf(str3, beginIndex) : -1;
				if (-1 != beginIndex && -1 != endIndex && endIndex > beginIndex) {
					charset = meta.substring(beginIndex, endIndex);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void close(Socket soc, InputStream is, OutputStream os) {
		if (null != os) {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (null != is) {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (null != soc) {
			try {
				soc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final HttpSocketDownloader hsd = new HttpSocketDownloader();
			hsd.setUrl(new URL("http://www.baidu.com"));
			hsd.setTimeout(10000);
			hsd.setUseGzip(false);
			Thread t = new Thread() {
				public void run() {
					long startTime = System.currentTimeMillis();
					hsd.start();
					long size = hsd.downloadSize, time = System
							.currentTimeMillis()
							- startTime;
					System.out.println("download " + size + " byte, used "
							+ time + " ms, speed is " + (size / time * 8)
							+ " kbps.");
				}
			};
			t.start();
			long startTime = System.currentTimeMillis();
			while (!hsd.stop) {
				if ((System.currentTimeMillis() - startTime) > 30 * 1000) {
					hsd.setStop(true);
					break;
				}
				Thread.sleep(1000);
			}
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
