package socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * 
 * @author Lee
 * 
 */
public class UDPClient {
    /**
     * 
     */
    private static final Logger logger = Logger.getLogger(UDPClient.class);

    public static final String TEST_UDP_HEADER = "0000000100000001";
    private volatile InnerTestSpeedThread[] curTestThreads = new InnerTestSpeedThread[0];
    private static final byte[] uploadByts = new byte[1024 * 10];
    protected volatile int failedThreadCount = 0;

    private volatile Long startTime = 0L;
    private volatile Long endTime = 0L;

    private String serverIp;
    private int serverPort;
    private String clientName;

    /**
     * 
     * @param clientName
     * @param serverIp
     * @param serverPort
     */
    public UDPClient(String clientName, String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientName = clientName;
    }

    /**
     * test result
     * @return test result unit?? xx kbit/s???
     */
    public int getTestSpeed() {
        double curSpeed = 0;
        double bytesTranfered = 0;

        for (int i = 0; i < curTestThreads.length; i++) {
            // curSpeed += curTestThreads[i].curSpeed;
            bytesTranfered += curTestThreads[i].bytesTranfered;
        }

        curSpeed = (bytesTranfered * 8 * 1000 / 1024 + 0.0D + 0.0D)
                / (endTime - startTime);

        return (int) Math.round(curSpeed);
    }

    /**
     * 获取总接受字节数
     * @return
     */
    public double getTestBytesTranfered() {
        double bytesTranfered = 0;
        for (int i = 0; i < curTestThreads.length; i++) {
            bytesTranfered += curTestThreads[i].bytesTranfered;
        }
        return (int) Math.round(bytesTranfered);
    }

    public void stopTestSpeed() {
        for (int i = 0; i < curTestThreads.length; i++) {
            if (curTestThreads[i].isAlive()) {
                curTestThreads[i].isStop = true;
            }
        }
    }

    public void startTest(int threadCount) {
        curTestThreads = new InnerTestSpeedThread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            curTestThreads[i] = new InnerTestSpeedThread();
            curTestThreads[i].start();
        }
    }

    class InnerTestSpeedThread extends Thread {
        protected volatile StringBuilder logMsg = new StringBuilder();
        protected volatile boolean failed = false;
        protected volatile String failedMsg;

        boolean isDownLoadTest = true;
        volatile boolean isStop = false;

        public long bytesTranfered;
        public volatile double curSpeed;
        public long takeTime;

        /**
         * 
         */
        public InnerTestSpeedThread() {
            this.setDaemon(true);
        }

        /**
         * 
         */
        private void testUdpDownLoadSpeed() {
            DatagramChannel channel = null;
            Selector selector = null;

            try {

                channel = DatagramChannel.open();
                channel.configureBlocking(false);
                channel.socket().setReceiveBufferSize(1024 * 1024 * 10);
                SocketAddress sa = new InetSocketAddress(serverIp, serverPort);

                //
                long currTime = System.currentTimeMillis();
                if (startTime != 0 && currTime < startTime) {
                    startTime = currTime;
                } else if (startTime == 0) {
                    startTime = currTime;
                }

                channel.connect(sa);
                selector = Selector.open();
                channel.register(selector, SelectionKey.OP_READ);
                channel.write(Charset.defaultCharset().encode(
                        TEST_UDP_HEADER + " -thread[" + clientName + "-"
                                + Thread.currentThread().getName() + "]"));

                //
                ByteBuffer byteBuffer = ByteBuffer.allocate(65535);

                //
                while (!isStop) {
                    try {
                        int eventsCount = selector.select();
                        if (eventsCount > 0) {
                            Set<SelectionKey> selectedKeys = selector
                                    .selectedKeys();
                            Iterator<SelectionKey> iterator = selectedKeys
                                    .iterator();
                            while (iterator.hasNext()) {
                                SelectionKey sk = (SelectionKey) iterator
                                        .next();
                                iterator.remove();
                                if (sk.isReadable()) {
                                    DatagramChannel datagramChannel = (DatagramChannel) sk
                                            .channel();
                                    if (datagramChannel.isOpen()) {
                                        datagramChannel.read(byteBuffer);
                                        byteBuffer.flip();

                                        String msg = Charset.defaultCharset()
                                                .decode(byteBuffer).toString();

                                        if (msg.startsWith(TEST_UDP_HEADER)) {
                                            logger
                                                    .info("Thread -name "
                                                            + Thread
                                                                    .currentThread()
                                                                    .getName()
                                                            + " start open udp-server socket channel ...");
                                        }

                                        bytesTranfered += msg.getBytes().length;
                                        curSpeed = (bytesTranfered * 8 * 1000 / 1024 + 0.0D)
                                                / (System.currentTimeMillis() - startTime);

                                        if (endTime < System
                                                .currentTimeMillis()) {
                                            endTime = System
                                                    .currentTimeMillis();
                                        }

                                        byteBuffer.clear();
                                    }

                                }

                            }
                        }
                    } catch (Exception e) {
                        logger.error("udp-client err ", e);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                try {
                    channel.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }

        /**
         * @see java.lang.Thread#run()
         */
        public void run() {
            testUdpDownLoadSpeed();
        }
    }

    public static void main(String[] args) {
        // String serverIp = args[0];
        // Integer port = Integer.valueOf(args[1]);
        // Integer threadCount = Integer.valueOf(args[2]);
        // Integer testSec = Integer.valueOf(args[3]);
        // Float peakPercentage = Float.valueOf(args[4]);
        // String serverIp = "114.80.142.114";

        String clientName = "mao1";
        String serverIp = "218.1.60.50";
        Integer port = 4445;
        Integer threadCount = 3;
        Integer testSec = 60;
        Float peakPercentage = 0.7F;

        UDPClient tester = new UDPClient(clientName, serverIp, port);
        tester.startTest(threadCount);

        double curSpeed = 0, maxDownSpeed = 0, downSpeed = 0;

        for (int i = 0; i < testSec; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            curSpeed = Math.round(tester.getTestSpeed());

            maxDownSpeed = Math.round(Math.max(maxDownSpeed, curSpeed));
            downSpeed = Math.round(maxDownSpeed * peakPercentage + curSpeed
                    * (1 - peakPercentage));

            logger.info("bytesTranfered " + tester.getTestBytesTranfered()
                    + " curSpeed [" + curSpeed + "] kbps ,maxDownSpeed ["
                    + maxDownSpeed + "] kbps,downSpeed [" + downSpeed
                    + "] kbps");

        }

        tester.stopTestSpeed();
    }
}
