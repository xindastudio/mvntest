package unittest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class HttpClientUtils {

	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);

	public static String postJson(String serviceUrl, String data) {

		HttpURLConnection connection = null;
		StringBuffer sb = new StringBuffer("");

		try {
			// 创建连接
			URL url = new URL(serviceUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");

			connection.setConnectTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS);
			connection.setReadTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS);
			connection.connect();
			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());

			out.writeBytes(data);
			out.flush();
			out.close();
			int code = connection.getResponseCode();
			System.out.println(code);

			if (code == 200) {
				System.out.println(code);
				// 读取响应
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String lines;

				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sb.append(lines);
				}
				reader.close();
			}

			System.out.println(String.format("server:%s,response:%s",
					serviceUrl, sb.toString()));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		String url = "http://localhost:8080/SynService/order";
		String jsonPre = "{\"file\":{\"agentNum\":null,\"assetId\":1036,\"beginTime\":null,\"createTime\":\"2014-05-15T16:59:01\",\"fileCode\":\"Umai:MOVI\\/200645@BESTV.SMG.SMG\",\"fileId\":\"200645\",\"fileType\":1,\"id\":";
		String jsonCen = ",\"lastActiveTime\":null,\"md5\":null,\"movieType\":99,\"priority\":5,\"queueId\":1006,\"resumable\":false,\"retry\":false,\"sourceFile\":\"D:\\/ftp-dir\\/test\\/mv\\/fm72chb136_build_setup.exe\",\"sourceSize\":null,\"status\":-1,\"targetFile\":\"ftp:\\/\\/test:test@localhost:21\\/mv\\/fm72chb136_build_setup.exe_";
		String jsonSur = "\",\"timestamp\":0,\"transferPer\":null,\"transferredSize\":null,\"type\":0},\"priority\":5,\"cmdType\":\"create\",\"agentId\":\"agentNum-0001\"}";
		for (int i = 0; i < 10; i++) {
			HttpClientUtils.postJson(url, jsonPre + (5 + i % 10) + jsonCen + i
					+ jsonSur);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
