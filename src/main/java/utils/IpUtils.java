/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package utils;

import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author wuzh
 */
public class IpUtils {

    /**
     * @param bytes
     * @param off
     * @return int
     */
    public static int bytesToInt(byte[] bytes, int off) {
        int addr = bytes[3 + off] & 0xFF;
        addr |= ((bytes[2 + off] << 8) & 0xFF00);
        addr |= ((bytes[1 + off] << 16) & 0xFF0000);
        addr |= ((bytes[0 + off] << 24) & 0xFF000000);
        return addr;
    }

    /**
     * @param ip1ip2
     * @return int
     */
    public static int[] getIPScope2(String ip1ip2) {
        String[] temp = ip1ip2.trim().split("-");
        int[] result = new int[2];
        try {
            result[0] = bytesToInt((InetAddress.getByName(temp[0].trim()))
                    .getAddress(), 0);
            result[1] = bytesToInt((InetAddress.getByName(temp[1].trim()))
                    .getAddress(), 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param ipAndMask
     * @return
     */
    public static int[] getIPScope(String ipAndMask) {

        String[] temp = ipAndMask.split("/");
        String tempIP = getCompleteIP(temp[0]);
        int theIP;
        try {
            theIP = bytesToInt((InetAddress.getByName(tempIP)).getAddress(), 0);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("invalid ip express "
                    + ipAndMask);
        }

        int netMaskBit = Integer.valueOf(temp[1].trim());
        if (netMaskBit == 0 || netMaskBit == 32) {
            throw new IllegalArgumentException("invalid ip express "
                    + ipAndMask);
        }
        int netIP = theIP & (0xFFFFFFFF << (32 - netMaskBit));
        int hostScope = (0xFFFFFFFF >>> netMaskBit);
        return new int[] { netIP, netIP + hostScope };

    }

    /**
     * @param ip ip
     * @param mask mask
     * @return
     */
    public static int[] getIPScope(String ip, String mask) {

        int theIP;
        int theMask = 0, ipcount = 0;
        try {
            theIP = IpUtils.bytesToInt(
                    (InetAddress.getByName(ip)).getAddress(), 0);
            if (null == mask || "".equals(mask)) {
                System.out.println("----ip:" + ip);
                return new int[] { theIP, theIP };
            }
            theMask = IpUtils.bytesToInt((InetAddress.getByName(mask))
                    .getAddress(), 0);
            ipcount = IpUtils.bytesToInt(InetAddress.getByName(
                    "255.255.255.255").getAddress(), 0)
                    - theMask;
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("invalid ip express  ip:" + ip
                    + "  mask:" + mask);
        }
        int netIP = theIP & theMask;
        int hostScope = netIP + ipcount;
        // System.out.println("netIP:" + netIP + " theMask:" + theMask);
        return new int[] { netIP, hostScope };

    }

    private static String getCompleteIP(String ip) {
        int pos = 0;
        int count = 0;
        while ((pos = ip.indexOf('.', pos + 1)) > 0) {
            count++;
        }
        count = 3 - count;
        switch (count) {
        case 3: {
            return ip + ".0.0.0";
        }
        case 2: {
            return ip + ".0.0";
        }
        case 1: {
            return ip;
        }
        default: {
            return ip;
        }
        }

    }

    /**
     * @param addr
     * @return String
     */
    public static final String str_addr(int addr) {
        return new StringBuilder().append(((addr >> 24) & 0xff)).append('.')
                .append((addr >> 16) & 0xff).append('.').append(
                        (addr >> 8) & 0xff).append('.').append((addr & 0xff))
                .toString();
    }

    /**
     * @param i
     * @return String
     */
    public static String intToStrBytes(int i) {
        byte[] byts = Integer.valueOf(i).toString().getBytes();
        byte[] bytes8 = { '0', '0', '0', '0', '0', '0', '0', '0' };
        int pos = 7;
        for (int k = byts.length - 1; k >= 0; k--) {
            bytes8[pos] = byts[k];
            pos--;
        }
        return new String(bytes8);
    }

    /**
     * @param bytes
     * @param off
     * @return int
     */
    public static int bytesToUnsignedInt(byte[] bytes, int off) {
        long addr = bytes[3 + off] & 0xFF;
        addr |= ((bytes[2 + off] << 8) & 0xFF00);
        addr |= ((bytes[1 + off] << 16) & 0xFF0000);
        addr |= ((bytes[0 + off] << 24) & 0xFF000000);
        return (int) addr;
    }

    /**
     * @param readBuffer
     * @return long
     */
    public static long bytesToLong(byte[] readBuffer) {
        return (((long) (readBuffer[0] & 255) << 24)
                + ((long) (readBuffer[1] & 255) << 16)
                + ((long) (readBuffer[2] & 255) << 8) + ((long) (readBuffer[3] & 255)));
    }

    /**
     * @param bytes
     * @return int
     */
    public static int bytesToUnsignedShort(byte[] bytes) {
        int addr = bytes[1] & 0xFF;
        addr |= (((bytes[0] & 255) << 8) & 0xFF00);

        return addr;
    }

    public static void setAlpha(BufferedImage buffedImg, int alpah) {
        int w = buffedImg.getWidth();
        int h = buffedImg.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int value = buffedImg.getRGB(x, y);
                value = (alpah << 24) | (value & 0x00ffffff);
                buffedImg.setRGB(x, y, value);
            }

        }
    }

    /**
     *
     * @param ip
     * @return boolean
     */
    public static boolean checkLanIp(String ip) {
        if (null == ip) {
            return false;
        }
        try {
            if (("127.0.0.1").equals(ip) || ip.startsWith("10.")
                    || ip.startsWith("192.168.")) {
                return true;
            } else if (ip.startsWith("172.")) {
                String[] arr = ip.split("\\.");
                if (Integer.parseInt(arr[1]) >= 16
                        && Integer.parseInt(arr[1]) <= 31) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param buffedImg
     * @param inc
     * @return
     */
    public static boolean changeAlpha(BufferedImage buffedImg, boolean inc) {
        int w = buffedImg.getWidth();
        int h = buffedImg.getHeight();
        boolean chnaged = false;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int value = buffedImg.getRGB(x, y);
                int a = (value >> 24) & 0xff;
                if (inc) {
                    if (a <= 235) {
                        chnaged = true;
                        a += 20;
                    } else if (a < 255) {
                        chnaged = true;
                        a = 255;
                    }
                } else {// dec
                    if (a >= 20) {
                        chnaged = true;
                        a -= 20;
                    } else if (a > 0) {
                        chnaged = true;
                        a = 0;
                    }
                }

                if (chnaged) {
                    value = ((a) << 24) | (value & 0x00ffffff);
                    buffedImg.setRGB(x, y, value);
                }

            }
        }
        return !chnaged;
    }

    /**
     * @param myShap
     * @param buffedImg
     */
    public static void revertImg(Shape myShap, BufferedImage buffedImg) {
        int w = buffedImg.getWidth();
        int h = buffedImg.getHeight();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int value = buffedImg.getRGB(x, y);
                if (myShap.contains(x, y)) {
                    int oppValue = ~value | 0xff000000;
                    // int color = value & 0x00ffffff;
                    buffedImg.setRGB(x, y, oppValue);
                }
            }
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        long theIP = 3556909056L;

        System.out.println("ip:" + IpUtils.str_addr((int) theIP) + "  int "
                + (int) theIP);
        String ipscope = "61.178.233.36-61.178.233.36";

        int[] ips = getIPScope2(ipscope);
        System.out.println(ipscope + " " + (ips[0]) + "-" + (ips[1]));
        int ipPlus = ips[1] + 1;
        System.out.println(str_addr(ipPlus));
        ipscope = "118.120.0.0-118.123.255.255";
        ips = getIPScope2(ipscope);
        System.out.println(ipscope + " " + (ips[0]) + "-" + (ips[1]));

        int[] arr1 = getIPScope("202.112.14.137/27");
        System.out.println("----- :" + (arr1[0]) + " - :" + (arr1[1]));
        int[] arr2 = getIPScope("202.112.14.137", "255.255.255.224");
        System.out.println("===== :" + (arr2[0]) + " - :" + (arr2[1]));

        System.out.println("ip:" + IpUtils.str_addr(arr2[0]) + " - "
                + IpUtils.str_addr(arr2[1]));

        int[] arr3 = getIPScope("116.228.37.0/26");
        System.out.println("----- :" + (arr3[0]) + " - :" + (arr3[1]));
        int[] arr4 = getIPScope("116.228.9.206", null);
        System.out.println("===== :" + (arr4[0]) + " - :" + (arr4[1]));
        System.out.println("ip:" + IpUtils.str_addr(1346374026) + " - "
                + IpUtils.str_addr(1978423790));

        int userip = IpUtils.bytesToInt(InetAddress.getByName("61.170.158.224")
                .getAddress(), 0);
        System.out.println("-----userip::" + userip);
        //String ipstr = "172.17.20.0|172.17.24.0|192.168.8.0|192.168.40.0|172.16.0.0|172.17.28.0|172.19.50.0|128.0.0.0|172.17.40.0|172.18.0.0|172.19.0.0";
        //String maskstr = "255.255.252.0|255.255.252.0|255.255.252.0|255.255.252.0|255.255.0.0|255.255.252.0|255.255.255.0|128.0.0.0|255.255.252.0|255.255.0.0|255.255.0.0";
        String ipstr = "0.0.0.0|61.0.0.0|144.16.0.0";
        String maskstr ="0.0.0.0|255.0.0.0|255.255.0.0";
        String[] iparr = ipstr.split("\\|");
        String[] maskarr = maskstr.split("\\|");
        String mask = null;
        for (int i = 0; i < iparr.length; i++) {
            try {
                if (i >= maskarr.length) {
                    mask = null;
                } else {
                    mask = maskarr[i];
                }
                if (iparr[i].equals(mask)) {
                    continue;
                }
                int[] scp = IpUtils.getIPScope(iparr[i], mask);
                System.out.println(scp[0] + "******" + scp[1]);
            } catch (Exception ex) {
                continue;
            }
        }

    }
}
