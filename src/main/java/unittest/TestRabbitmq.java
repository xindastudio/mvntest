package unittest;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

/**
 * 
 * @author wuxufeng
 * 
 */
public class TestRabbitmq {

	public static void main(String[] args) throws IOException {
		fanout();
	}

	private static Channel channel(String en, String type, String qn, String rk)
			throws IOException {
		ConnectionFactory cf = new ConnectionFactory();
		cf.setUsername("admin");
		cf.setPassword("admin");
		cf.setHost("10.0.0.101");
		cf.setPort(5672);
		cf.setVirtualHost("mytestmq_vh");
		// cf.setUri("amqp://guest:guest@10.0.0.101:5672/mytestmq");
		cf.setConnectionTimeout(5000);
		cf.setAutomaticRecoveryEnabled(false);
		cf.setNetworkRecoveryInterval(5000);
		cf.setRequestedChannelMax(0);
		cf.setRequestedFrameMax(0);
		cf.setRequestedHeartbeat(0);
		cf.setShutdownTimeout(10000);
		cf.setTopologyRecoveryEnabled(true);
		// cf.setClientProperties(clientProperties);
		// cf.setExceptionHandler(exceptionHandler);
		// cf.setSaslConfig(saslConfig);
		// cf.setSharedExecutor(executor);
		// cf.setSocketConfigurator(socketConfigurator);
		// cf.setSocketFactory(factory);
		// cf.setThreadFactory(threadFactory);

		Connection conn = cf.newConnection();
		Channel c = conn.createChannel();
		c.exchangeDeclare(en, type, false, true, false, null);
		c.queueDeclare(qn, false, false, true, null);
		c.queueBind(qn, en, rk, null);
		return c;
	}

	private static void fanout() throws IOException {
		final String en = "mytestexchange";
		final String type = "fanout";
		final String qn = "mytestqueue";
		final String rk = "my.test.routing";
		final Channel cp = channel(en, type, qn, rk);
		new Thread() {
			public void run() {
				try {
					BasicProperties bp = MessageProperties.TEXT_PLAIN;
					String msg = null;
					for (int i = 0; i < 10; i++) {
						msg = "我的测试消息test" + i + ", "
								+ System.currentTimeMillis();
						cp.basicPublish(en, rk, false, false, bp,
								msg.getBytes());
						Thread.sleep(1000);
					}
					cp.close();
					cp.getConnection().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		final Channel cc = channel(en, type, qn, rk);
		String ct = "mytestconsumer";
		final AtomicInteger count = new AtomicInteger(0);
		cc.basicConsume(qn, false, ct, true, true, null,
				new DefaultConsumer(cc) {
					public void handleDelivery(String consumerTag,
							Envelope envelope, BasicProperties properties,
							byte[] body) throws IOException {
						System.out.println(consumerTag + " recv : "
								+ new String(body));
						long dt = envelope.getDeliveryTag();
						getChannel().basicAck(dt, false);
						count.incrementAndGet();
					}
				});
		while (count.get() < 10) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		cc.close();
		cc.getConnection().close();
	}
}
