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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 
 * @author Lee
 * 
 */
public class UDPSpecialClient {
    /**
     * 
     */
    private static final Logger logger = Logger.getLogger(UDPSpecialClient.class);

    public static final String TEST_UDP_HEADER = "0000000100000001";
    private volatile InnerTestSpeedThread[] curTestThreads = new InnerTestSpeedThread[0];
    private static final byte[] uploadByts = new byte[1024 * 10];

    protected volatile int failedThreadCount = 0;
    private final ThreadPoolExecutor threadPoolExecutor;

    private String serverIp;
    private int serverPort;

    private volatile Long startTime = 0L;
    private volatile Long endTime = 0L;

    /**
     * 
     * @param serverIP
     * @param serverPort
     */
    public UDPSpecialClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        threadPoolExecutor = new ThreadPoolExecutor(5, 8, 90, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());

    }

    /**
     * test result
     * @return test result unit?? xx kbit/s???
     */
    public int getTestSpeed() {
        double curSpeed = 0;
        double bytesTranfered = 0;
        for (int i = 0; i < curTestThreads.length; i++) {
            bytesTranfered += curTestThreads[i].bytesTranfered;
        }

        curSpeed = (bytesTranfered * 8 * 1000 / 1024 + 0.0D + 0.0D)
                / (endTime - startTime);

        return (int) Math.round(curSpeed);
    }

    /**
     * 
     * @return double
     */
    public double getTestTotalTranferedBytes() {
        double bytesTranfered = 0;
        for (int i = 0; i < curTestThreads.length; i++) {
            bytesTranfered += curTestThreads[i].bytesTranfered;
        }
        
        return (int) Math.round(bytesTranfered);
    }

    public void stopTestSpeed() {
        for (int i = 0; i < curTestThreads.length; i++) {
            if (!curTestThreads[i].isStop) {
                curTestThreads[i].isStop = true;
            }
        }

        threadPoolExecutor.shutdown();
    }

    public void startTest(int threadCount) {
        curTestThreads = new InnerTestSpeedThread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            curTestThreads[i] = new InnerTestSpeedThread();
            threadPoolExecutor.execute(curTestThreads[i]);
        }

        //
        new WatchThread(threadPoolExecutor).start();
    }

    class InnerTestSpeedThread implements Runnable {
        protected volatile StringBuilder logMsg = new StringBuilder();
        protected volatile boolean failed = false;
        protected volatile String failedMsg;
        boolean isDownLoadTest = true;
        public volatile boolean isStop = false;
        public long bytesTranfered;
        public volatile double curSpeed;

        /**
         * 
         */
        private void testUdpDownLoadSpeed() {
            DatagramChannel channel = null;
            Selector selector = null;

            try {
                channel = DatagramChannel.open();
                channel.configureBlocking(false);
                channel.socket().setReceiveBufferSize(1024 * 512);
                SocketAddress sa = new InetSocketAddress(serverIp, serverPort);

                // long startTime = System.currentTimeMillis();
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
                        TEST_UDP_HEADER + " -thread["
                                + Thread.currentThread().getName() + "]"));

                //
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 128);

                while (!isStop && !Thread.interrupted()) {
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
                                    
                                    if (endTime < System.currentTimeMillis()) {
                                        endTime = System.currentTimeMillis();
                                    }

                                    byteBuffer.clear();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
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

    /**
     * 
     * @author Mr.li
     * 
     */
    class WatchThread extends Thread {

        private ThreadPoolExecutor threadPoolExecutor;

        /**
         * 
         * @param threadPoolExecutor
         */
        public WatchThread(ThreadPoolExecutor threadPoolExecutor) {
            this.threadPoolExecutor = threadPoolExecutor;
            this.setDaemon(true);
        }

        /**
         * 
         */
        public void run() {
            boolean isStop = false;
            while (!isStop) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                logger.info("ThreadPool - completed count "
                        + threadPoolExecutor.getCompletedTaskCount());
            }

        }

    }

    public static void main(String[] args) {
        // String serverIp = args[0];
        // Integer port = Integer.valueOf(args[1]);
        // Integer threadCount = Integer.valueOf(args[2]);
        // Integer testSec = Integer.valueOf(args[3]);
        // Float peakPercentage = Float.valueOf(args[4]);

        String serverIp = "192.168.8.155";
        Integer port = 4444;
        Integer threadCount = 10000;
        Integer testSec = 10;
        Float peakPercentage = 0.7F;

        UDPSpecialClient tester = new UDPSpecialClient(serverIp, port);
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

            logger.info("curSpeed [" + curSpeed + "] kbps ,maxDownSpeed ["
                    + maxDownSpeed + "] kbps,downSpeed [" + downSpeed
                    + "] kbps");
        }

        tester.stopTestSpeed();
    }
}
