package unittest;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestJAXBContext {
	private static JAXBContext context;
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;
	// @XmlElement(nillable = true)
	@XmlJavaTypeAdapter(JaxbStringSerializer.class)
	private String stringType;
	private int intType;
	private Integer intgerType;
	private long longType;
	private Long longType2;
	private float floatType;
	private Float floatType2;
	private double doubleType;
	private Double doubleType2;

	private static void init() throws Exception {
		context = JAXBContext.newInstance(TestJAXBContext.class);
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		//marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		unmarshaller = context.createUnmarshaller();
	}

	public static void main(String[] args) throws Exception {
		init();
		TestJAXBContext t = new TestJAXBContext();
		output(t);
		System.out.println();

		StringWriter sw = new StringWriter();
		marshaller.marshal(t, sw);

		String out = sw.toString();
		out = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + out;
		System.out.println(out);

		StringReader sr = new StringReader(out);
		t = (TestJAXBContext) unmarshaller.unmarshal(sr);
		output(t);
		System.out.println();

		StringBuilder sb = new StringBuilder();
		sb.append("<testJAXBContext>");
		append(sb, "stringType", null);
		append(sb, "intgerType", null);
		append(sb, "longType2", null);
		append(sb, "floatType2", null);
		append(sb, "doubleType2", null);
		sb.append("</testJAXBContext>");
		out = sb.toString();
		System.out.println(out);

		sr = new StringReader(out);
		t = (TestJAXBContext) unmarshaller.unmarshal(sr);
		output(t);
	}

	private static void output(TestJAXBContext t) throws Exception {
		for (Field f : t.getClass().getDeclaredFields()) {
			if (!Modifier.isStatic(f.getModifiers())) {
				f.setAccessible(true);
				System.out.println(f.getName() + " = " + f.get(t));
			}
		}
	}

	private static void append(StringBuilder sb, String k, Object v) {
		sb.append('<').append(k).append('>').append(null == v ? "" : v)
				.append('<').append('/').append(k).append('>');
	}

}

class JaxbStringSerializer extends XmlAdapter<String, String> {
	public String marshal(String str) throws Exception {
		if (null == str) {
			return null;
		}
		return str;
	}

	public String unmarshal(String str) throws Exception {
		if (null == str || str.length() < 1) {
			return null;
		}
		return str;
	}

}