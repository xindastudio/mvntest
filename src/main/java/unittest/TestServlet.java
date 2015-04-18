package unittest;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class TestServlet
 */
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String setRespData;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String temp = request.getParameter("setRespData");
		if (null != temp && temp.length() > 0) {
			setRespData = temp;
			return;
		}
		// post1(request, response);
		// post2(request, response);
		post3(request, response);
	}

	public void post1(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		InputStream is = null;
		PrintWriter pw = null;
		try {
			is = request.getInputStream();
			byte[] b = new byte[102400];
			int l = is.read(b);
			String req = l > 0 ? new String(b, 0, l, "UTF-8") : "";
			System.out.println("request : " + req);

			// String files =
			// ",\"files\":[{\"agentNum\":null,\"beginTime\":1400826430020,\"createTime\":\"2014-05-23T14:27:00\",\"fileId\":\"200711\",\"id\":2295,\"lastActiveTime\":1400826480341,\"md5\":null,\"priority\":4,\"queueId\":1067,\"resumable\":false,\"retry\":false,\"sourceFile\":\"\\/usr\\/bestv\\/cms\\/nas\\/2014\\/05\\/23\\/1195\\/1195_.ts\",\"sourceSize\":1040241621,\"status\":1,\"targetFile\":\"ftp:\\/\\/imspftp:imspftp@10.0.128.250:21\\/H264\\/20140523\\/200711.ts\",\"timestamp\":1400826420692024000,\"transferredSize\":589752320},{\"agentNum\":null,\"beginTime\":1400826430019,\"createTime\":\"2014-05-23T14:27:00\",\"fileId\":\"200709\",\"id\":2293,\"lastActiveTime\":1400826480258,\"md5\":null,\"priority\":4,\"queueId\":1067,\"resumable\":false,\"retry\":false,\"sourceFile\":\"\\/usr\\/bestv\\/cms\\/nas\\/2014\\/05\\/23\\/1194\\/1194_.ts\",\"sourceSize\":1040241621,\"status\":1,\"targetFile\":\"ftp:\\/\\/imspftp:imspftp@10.0.128.250:21\\/ASP\\/20140523\\/200709.ts\",\"timestamp\":1400826420735972000,\"transferredSize\":562227200}]";
			String jsonPre = "{\"agentNum\":null,\"assetId\":1036,\"beginTime\":null,\"createTime\":\"2014-05-15T16:59:01\",\"fileCode\":\"Umai:MOVI\\/200645@BESTV.SMG.SMG\",\"fileId\":\"200645\",\"fileType\":1,\"id\":";
			String jsonCen = ",\"lastActiveTime\":null,\"md5\":null,\"movieType\":99,\"priority\":5,\"queueId\":1006,\"resumable\":false,\"retry\":false,\"sourceFile\":\"D:\\/ftp-dir\\/test\\/mv\\/ifstat-1.1.tar.gz\",\"sourceSize\":null,\"status\":-1,\"targetFile\":\"ftp:\\/\\/test:test@localhost:21\\/mv\\/ifstat-1.1.tar.gz_";
			String jsonSur = "\",\"timestamp\":0,\"transferPer\":null,\"transferredSize\":null,\"type\":0}";
			String files = "";
			for (int i = 0; i < 10; i++) {
				if (i != 0) {
					files += ",";
				}
				files += (jsonPre + (i % 19) + jsonCen + i + jsonSur);
			}
			files = ",\"files\":[" + files + "]";
			files = "";

			String resp = "{\"resultCode\":\"1\",\"resultDesc\":\"成功\""
					+ (req.indexOf("restart") > 0 ? files : "") + "}";

			String sitecode = request.getParameter("sitecode");
			String date = request.getParameter("date");
			ObjectMapper om = new ObjectMapper();
			Map<Object, Object> m1 = new HashMap<Object, Object>();
			Map<Object, Object> m2 = new HashMap<Object, Object>();
			m1.put("vodstat", m2);
			m2.put("CLICKS", "49");
			m2.put("DATE", date);
			m2.put("DURATION", "2000000");
			m2.put("MEDIACODE", "mediacode0001");
			m2.put("PROGNAME", "progname0001");
			m2.put("SITECODE", sitecode);
			m2.put("VISITORS", "50");
			// String resp = om.writeValueAsString(m1);

			System.out.println("response : " + resp);
			response.setHeader("Content-Type", "application/json");
			pw = response.getWriter();
			pw.write(resp);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				is.close();
			}
			if (null != pw) {
				pw.close();
			}
		}
	}

	public void post3(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		InputStream is = null;
		PrintWriter pw = null;
		try {
			System.out.println("request : \n");
			Enumeration<String> e = request.getParameterNames();
			String o;
			while (e.hasMoreElements()) {
				o = e.nextElement();
				System.out.println(o + " = " + request.getParameter(o));
			}
			String resp = setRespData;
			System.out.println("\nresponse : " + resp);
			// response.addHeader("Content-type", "text/html;charset=UTF-8");
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setCharacterEncoding("UTF-8");
			pw = response.getWriter();
			pw.write(resp);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				is.close();
			}
			if (null != pw) {
				pw.close();
			}
		}
	}
}
