package socket.nio;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;

import com.sun.org.apache.bcel.internal.generic.InstructionConstants.Clinit;

/**
 * 
 * @author Mr.li
 * 
 */
public class UDPServer extends Thread {

    /**
     * 
     */
    private static final Logger logger = Logger.getLogger(UDPServer.class);

    private static final int GLOBAL_BUFFER_SIZE = 1024;
    
    private static final int GLOBAL_REST_TIME = 1;

    private final ThreadPoolExecutor threadPoolExecutor;
    private static final byte[] downDataByts = new byte[GLOBAL_BUFFER_SIZE];

    //
    private AtomicLong sendUdpPkgCount = new AtomicLong(0);
    private AtomicLong beginTestTime = new AtomicLong(0);
    private AtomicLong endTestTime = new AtomicLong(0);
    private AtomicInteger globalRestTime = new AtomicInteger(1);

    //
    private Map<String, Long> udpPkgCountMap = new ConcurrentHashMap<String, Long>();
    private Map<String, Long> beginTestTimeMap = new ConcurrentHashMap<String, Long>();
    private Map<String, Long> endTestTimeMap = new ConcurrentHashMap<String, Long>();
    private Map<String, Integer> globalRestTimeMap = new ConcurrentHashMap<String, Integer>();

    //
    private Map<String, Integer> udpClientMap = new ConcurrentHashMap<String, Integer>();

    static {
        for (int i = 0; i < downDataByts.length; i++) {
            downDataByts[i] = 'A';
        }
    }

    /**
     * 
     */
    public UDPServer() {
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 90,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        new WatchThread().start();
    }

    public void run() {
        Selector selector = null;
        try {
            DatagramChannel channel = DatagramChannel.open();
            DatagramSocket socket = channel.socket();
            channel.configureBlocking(false);
            socket.bind(new InetSocketAddress(4445));

            channel.socket().setReceiveBufferSize(1024 * 1024);
            channel.socket().setSendBufferSize(1024 * 1024);

            logger.info("udp-server send buffer ["
                    + channel.socket().getSendBufferSize()
                    + "] receive buffers ["
                    + channel.socket().getReceiveBufferSize() + "]");

            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

        } catch (Exception e) {
            logger.error("udp-server start listener ", e);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(65536);

        while (true) {
            try {
                int eventsCount = selector.select();
                if (eventsCount > 0) {
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey sk = (SelectionKey) iterator.next();
                        iterator.remove();

                        if (sk.isReadable()) {
                            DatagramChannel datagramChannel = (DatagramChannel) sk
                                    .channel();
                            SocketAddress sa = datagramChannel
                                    .receive(byteBuffer);
                            byteBuffer.flip();

                            CharBuffer charBuffer = Charset.defaultCharset()
                                    .decode(byteBuffer);
                            String message = charBuffer.toString();
                            if (null != message
                                    && message
                                            .startsWith(UDPClient.TEST_UDP_HEADER)) {
                                threadPoolExecutor
                                        .execute(new InnerUdpServerWorkThread(
                                                datagramChannel, sa, message));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("udp-server err ", e);
            }
        }

    }

    /**
     * 
     * @author Mr.li
     * 
     */
    class InnerUdpServerWorkThread implements Runnable {

        private DatagramChannel datagramChannel;
        private SocketAddress socketAddress;
        private String clientSendMsg;

        // 开始-调度时间
        private volatile long beginningScheduledTime;

        //
        private int period = 60;

        /**
         * 
         * @param datagramChannel
         * @param socketAddress
         * @param clientSendMsg
         */
        public InnerUdpServerWorkThread(DatagramChannel datagramChannel,
                SocketAddress socketAddress, String clientSendMsg) {
            this.datagramChannel = datagramChannel;
            this.socketAddress = socketAddress;
            this.clientSendMsg = clientSendMsg;
        }

        /**
         * @see java.lang.Thread#run()
         */
        public void run() {
            // 设置开始调度时间
            beginningScheduledTime = System.currentTimeMillis();

            int beginIndex = clientSendMsg.indexOf("[") + 1;
            int endIndex = clientSendMsg.lastIndexOf("]");
            String client = clientSendMsg.substring(beginIndex, endIndex)
                    .toString().split("-")[0];

            long currTime = System.currentTimeMillis();
            if (udpClientMap.containsKey(client)) {
                udpClientMap.put(client, udpClientMap.get(client) + 1);

                long startTime = beginTestTimeMap.get(client);
                if (startTime != 0 && currTime < startTime) {
                    beginTestTimeMap.put(client, currTime);
                } else if (startTime == 0) {
                    beginTestTimeMap.put(client, currTime);
                }
            } else {
                udpClientMap.put(client, 1);
                beginTestTimeMap.put(client, currTime);
                //
                globalRestTimeMap.put(client, GLOBAL_REST_TIME);
            }

            // long currTime = System.currentTimeMillis();
            // long startTime = beginTestTime.get();
            // if (startTime != 0 && currTime < startTime) {
            // beginTestTime.set(currTime);
            // } else if (startTime == 0) {
            // beginTestTime.set(currTime);
            // }

            long i = 0;
            long count = 0;
            while (beforeTheDeadline()) {

                ByteBuffer buffer = ByteBuffer.wrap(downDataByts);
                int bytes = 0;
                if (null != datagramChannel && datagramChannel.isOpen()
                        && socketAddress != null) {
                    try {
                        bytes = datagramChannel.send(buffer, socketAddress);
                        if (i % 5 == 0) {
                            // 
                            Thread.sleep(globalRestTimeMap.get(client), 10);
                        }
                    } catch (IOException e) {
                        logger.error("udp-server inner thread io err", e);
                    } catch (Exception e) {
                        logger.error("udp-server inner thread err", e);
                    }

                    if (bytes > 0) {
                        logger.debug("send msg to -thread "
                                + clientSendMsg.subSequence(beginIndex,
                                        endIndex) + " bytes " + bytes);
                        //
                        // sendUdpPkgCount.getAndAdd(1);
                        // long endTime = endTestTime.get();
                        // if (endTime < System.currentTimeMillis()) {
                        // endTestTime.set(System.currentTimeMillis());
                        // }

                        if (udpPkgCountMap.containsKey(client)) {
                            udpPkgCountMap.put(client, udpPkgCountMap
                                    .get(client) + 1);
                            long endTime = endTestTimeMap.get(client);

                            if (endTime < System.currentTimeMillis()) {
                                endTestTimeMap.put(client, System
                                        .currentTimeMillis());
                            }
                        } else {
                            udpPkgCountMap.put(client, 1L);
                            endTestTimeMap.put(client, System
                                    .currentTimeMillis());
                        }

                        ++count;
                    }
                    ++i;
                }

            }

            logger.info("Thread -name " + Thread.currentThread().getName()
                    + " send udp package total count [" + i
                    + "] success-count [" + count + "]");
        }

        /**
         * 
         * @return boolean
         */
        public boolean beforeTheDeadline() {
            long curTime = System.currentTimeMillis();
            long nextScheudleTime = beginningScheduledTime + this.period
                    * 1000L;
            return (nextScheudleTime > curTime);
        }
    }

    /**
     * 
     * @author Mr.li
     * 
     */
    class WatchThread extends Thread {

        /**
         * 
         */
        public WatchThread() {
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

                for (Entry<String, Integer> entry : udpClientMap.entrySet()) {
                    String client = entry.getKey();
                    System.out.println("-name " + client);
                    
                    if (beginTestTimeMap.containsKey(client)
                            && endTestTimeMap.containsKey(client)
                            && udpPkgCountMap.containsKey(client)) {

                        double curSpeed = (udpPkgCountMap.get(client)
                                * UDPServer.GLOBAL_BUFFER_SIZE * 8 * 1000
                                / 1024 + 0.0D)
                                / (endTestTimeMap.get(client) - beginTestTimeMap
                                        .get(client));

                        double commonspeed = 40000;
                        if (client.equals("mao")) {
                            commonspeed = 40000;
                        }
                        if (client.equals("mao1")) {
                            commonspeed = 20000;
                        }

                        if (curSpeed > commonspeed) {
                            globalRestTimeMap.put(client, globalRestTimeMap
                                    .get(client) + 1);
                        } else if (curSpeed > 0 && curSpeed < commonspeed) {
                            globalRestTimeMap.put(client, globalRestTimeMap
                                    .get(client) - 1);
                        }

                        logger.info("client -name " + client + " -rest "
                                + globalRestTimeMap.get(client)
                                + " on server realtime curspeed " + curSpeed
                                + " kbps");
                    }

                }

                // double curSpeed = (sendUdpPkgCount.get()
                // * UDPServer.GLOBAL_BUFFER_SIZE * 8 * 1000 / 1024 + 0.0D)
                // / (endTestTime.get() - beginTestTime.get());
                //
                // double commonspeed = 40000;
                // if (curSpeed > commonspeed) {
                // globalRestTime.set(globalRestTime.addAndGet(1));
                // } else if (curSpeed > 0 && curSpeed < commonspeed) {
                // globalRestTime.set(globalRestTime.decrementAndGet());
                // }
                //
                // logger.info("udp-server realtime curspeed " + curSpeed
                // + " kbps");

            }
        }
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        new UDPServer().start();
    }

}
