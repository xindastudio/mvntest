package unittest;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.rpc.*;
import javax.xml.soap.SOAPElement;

public class Simulator {
	public static void main(String[] args) {
//		String EOSServerIP = "10.7.93.27";
//		String port = "8005";
		String EOSServerIP = "10.7.84.49";
		String port = "10081";
		String packageName = "gxlu_interface_akazam";
		String unitID = "0";
		String automata = "biz.login";// 此处所有调用均为biz.+方法名称
		String password = "gxlu";
		String inputXML = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"no\"?>"
				+ "        <root>"
				+ "            <data>"
				+ "                <EOSOperator>"
				+ "                    <userID>sysadmin</userID>"
				+ "                    <password>sysadmin0813</password>"
				+ "                </EOSOperator>"
				+ "            </data>"
				+ "        </root>";
		try {
			// 加OIP头
			SOAPHeaderElement header = new SOAPHeaderElement("", "Esb");
			SOAPElement route = header.addChildElement("Route");
			route.addChildElement("Sender").setValue("10.1109"); // 调用方
			route.addChildElement("Time").setValue("2013-07-05 16:48:39.570");  // 调用时间
			route.addChildElement("ServCode").setValue("31.1109.platformQry_crmClassProQry.SynReq"); // 调用服务名
			route.addChildElement("MsgId").setValue("10.1109_20130705_572607173643"); // 调用消息Id
			
			Service service = new Service();
			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress("http://" + EOSServerIP + ":" + port
//					+ "/axis/services/BizService");
			call.setTargetEndpointAddress("http://10.7.84.49:10081/CSB/Services/CSB_OSSAdapter_ToSh_Service"); // OIP网关地址
			call.setOperationName("runBiz");
			call.addParameter("packageName", XMLType.XSD_STRING,
					ParameterMode.IN);
			call.addParameter("unitId", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("processName", XMLType.XSD_STRING,
					ParameterMode.IN);
			call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("bizDataXML", XMLType.XSD_STRING,
					ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			
			// 设置OIP头
			call.addHeader(header);
			
			String returnStr = (String) call.invoke(new Object[] { packageName,
					unitID, automata, password, inputXML });
			System.out.println(returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
