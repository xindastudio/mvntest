package unittest;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.DeflaterOutputStream;

import org.apache.commons.codec.binary.Base64;

public class TestDeflaterOutputStream {

	public static void main(String[] args) throws Exception {
		String s = "PFRWUGF5PjxUVlBheWJvZHk%2bPE1zZz48dmVyc2lvbj4xLjAuMDwvdmVyc2lvbj48dHlwZT4yMDAyPC90eXBlPjxmbGFnPjAwPC9mbGFnPjwvTXNnPjxBY2NvdW50Lz48Q2hJbmZvPjx1c2VySWQ%2bdWktMDAwMTwvdXNlcklkPjwvQ2hJbmZvPjxQdXJjaGFzZT48YWNxQklOPmFiLTAwMDE8L2FjcUJJTj48ZGF0ZT4yMDE0MTAxMzE3MDgwOTwvZGF0ZT48dHJhY2VOdW0%2bMjAwMDE2PC90cmFjZU51bT48Y3VycmVuY3k%2bMTU2PC9jdXJyZW5jeT48dHJhbnNBbXQ%2bMDAwMDAwMDAwMDA4PC90cmFuc0FtdD48bWVySWQ%2bbWktMDAwMTwvbWVySWQ%2bPG9yZGVyTnVtPjIwMTQxMDEzLTE8L29yZGVyTnVtPjwvUHVyY2hhc2U%2bPC9UVlBheWJvZHk%2bPE1hYz42OGUzYTE4MjAzODYwNTdlMjViOWRlYjgyMjY2OGQwMTwvTWFjPjwvVFZQYXk%2b";
		s = URLDecoder.decode(s, "UTF-8");
		System.out.println(s);
		byte[] b = s.getBytes("UTF-8");
		b = Base64.decodeBase64(b);
		s = "<TVPay><TVPaybody><orderNum>UC_2</orderNum><Msg><version>1.0.0</version><type>2002</type><flag>00</flag></Msg><Account/><ChInfo><userId>ui-0001</userId></ChInfo><Purchase><acqBIN>ab-0001</acqBIN><date>20141013170809</date><traceNum>200016</traceNum><currency>156</currency><transAmt>000000000008</transAmt><merId>mi-0001</merId><orderNum>20141013-1</orderNum></Purchase><Resp><respCode>00</respCode><respInfo/></Resp></TVPaybody><Mac>68e3a1820386057e25b9deb822668d01</Mac></TVPay>";
		b = s.getBytes("UTF-8");
		System.out.println(new String(b, "UTF-8"));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DeflaterOutputStream d = new DeflaterOutputStream(os);
		d.write(b);
		d.finish();
		os.flush();
		System.out.println(os.size());
		b = os.toByteArray();
		b = Base64.encodeBase64(b);
		s = new String(b, "UTF-8");
		System.out.println(s);
		s = URLEncoder.encode(s, "UTF-8");
		System.out.println(s);
	}

}
