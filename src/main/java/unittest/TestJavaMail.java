package unittest;
import java.io.File;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author wuliwei
 * 
 */
public class TestJavaMail {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			JavaMailSenderImpl jm = new JavaMailSenderImpl();
			jm.setHost("mail.hxcomm.cn");
			//jm.setPort(25);
			//jm.setUsername("notifybot");
			jm.setUsername("notifybot@hxcomm.cn");
			jm.setPassword("111111");
			Properties jmp = new Properties();
			//jmp.put("mail.debug", "true");
			//jmp.put("mail.smtp.ehlo", "false");
			jmp.put("mail.smtp.auth", "true");
			//jmp.put("mail.smtp.socketFactory.port", "110");
			//jmp.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			//jmp.put("mail.smtp.socketFactory.fallback", "false");
			//jmp.put("mail.smtp.starttls.enable", "true");
			jm.setJavaMailProperties(jmp);

			MimeMessage message = jm.createMimeMessage();
			MimeMessageHelper messageHelp = new MimeMessageHelper(message,
					true, "GBK");// 中文
			messageHelp.setFrom("notifybot@hxcomm.cn");
			// 接受人地址
			String[] mailTos = "wulw@hua-xia.com.cn".split(",");
			messageHelp.setTo(mailTos);
			// 抄送人地址
			String[] mailCcs = "wulw@hxcomm.cn".split(",");
			messageHelp.setCc(mailCcs);
			messageHelp.setSubject("上海电信宽带测试--周报");
			//messageHelp.setText("测试");
			File attachFile = new File("e:/数据统计20121105~20121111.xls");
			//messageHelp.addAttachment(MimeUtility.encodeWord(attachFile.getName()), attachFile);
			attachFile = new File("e:/上海电信测速网站20121105~20121111.xls");
			messageHelp.addAttachment(MimeUtility.encodeWord(attachFile.getName()), attachFile);
			jm.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
