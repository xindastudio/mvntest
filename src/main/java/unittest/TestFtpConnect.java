package unittest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * @author wuliwei
 *
 */
public class TestFtpConnect {
	private static int timeout = 5 * 1000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "211.136.99.8";
		int port = 6162;
		String userName = "virtual";
		String password = "virtual";
		String path = "mbhx/test";
		
		Socket dataSocket = passiveConnect(host, port, userName, password, path);
		if (null == dataSocket) {
			System.out.println("passive connect fail...");
			dataSocket = activeConnect(host, port, userName, password, path);
		}
		if (null == dataSocket) {
			System.out.println("active connect fail...");
		} else {
			InputStream is = null;
			try {
				is = dataSocket.getInputStream();
				byte[] b = new byte[1024];
				int len = 0;
				long size = 0;
				while (-1 != (len = is.read(b))) {
					size += len;
				}
				System.out.println("size : " + size);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try { dataSocket.close(); } catch (IOException e) { }
			}
		}
	}
	
	private static Socket passiveConnect(String host, int port, String userName, String password, String path) {
		Socket cmdSocket = null;
		PrintWriter cmdWriter = null;
		ServerSocket serverSocket = null;
		Socket dataSocket = null;
		try {
			cmdSocket = new Socket();
			cmdSocket.connect(new InetSocketAddress(host, port), timeout);
			cmdWriter = new PrintWriter(cmdSocket.getOutputStream());
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.setSoTimeout(timeout);
			serverSocket.bind(new InetSocketAddress(cmdSocket.getLocalPort() + 1), 1);
			
			byte[] address = serverSocket.getInetAddress().getAddress();
            StringBuilder cmd = new StringBuilder("PORT ");
            for (int i = 0; i < 4; i++) {
            	cmd.append(address[i] & 0xFF).append(',');
            }
            cmd.append((serverSocket.getLocalPort() / 256) & 0xFF).append(',').append(serverSocket.getLocalPort() & 0xFF);
            String[] cmds = new String[] {
            		"USER " + userName, 
            		"PASS " + password, 
            		cmd.toString(), 
            		"RETR " + path
            };
            for (String c : cmds) {
            	cmdWriter.println(c);
                cmdWriter.flush();
            }
            
            dataSocket = serverSocket.accept();

            cmdWriter.println("QUIT ");
            
            return dataSocket;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != cmdSocket) {
				try { cmdSocket.close(); } catch (Exception e) { }
			}
			if (null != serverSocket) {
				try { serverSocket.close(); } catch (Exception e) { }
			}
		}
		return null;
	}
	
	private static Socket activeConnect(String host, int port, String userName, String password, String path) {
		Socket cmdSocket = null;
		PrintWriter cmdWriter = null;
		LineNumberReader cmdReader = null;
        Socket dataSocket = null;
        try {
        	cmdSocket = new Socket();
        	cmdSocket.connect(new InetSocketAddress(host, port), timeout);
        	cmdWriter = new PrintWriter(cmdSocket.getOutputStream());
        	cmdReader = new LineNumberReader(new InputStreamReader(cmdSocket.getInputStream()));
        	
            String[] cmds = new String[] {
            		"USER " + userName, 
            		"PASS " + password, 
            		"PASV", 
            		"RETR " + path
            };
            for (String c : cmds) {
            	cmdWriter.println(c);
                cmdWriter.flush();
            }
            String rtnInfo = null;
            int dataPort = -1;
            while ((rtnInfo = cmdReader.readLine()) != null) {
            	System.out.println(rtnInfo);
            	if (rtnInfo.startsWith("227")) {
            		int startIndex = rtnInfo.indexOf('(') + 1, endIndex = rtnInfo.indexOf(')');
            		String[] addr = rtnInfo.substring(startIndex, endIndex).split(",");
            		dataPort = 256 * Integer.valueOf(addr[4]) + Integer.valueOf(addr[5]);
            		break;
            	}
            }
            
            System.out.println("data port : " + dataPort);
        	dataSocket = new Socket();
            dataSocket.connect(new InetSocketAddress(host, dataPort), timeout);
            
            cmdWriter.println("QUIT ");
            
            return dataSocket;
        } catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != cmdSocket) {
				try { cmdSocket.close(); } catch (Exception e) { }
			}
        }
		return null;
	}

}
