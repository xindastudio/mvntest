package unittest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


/**
 * @author wuliwei
 *
 */
public class TestFtp {
	private static final String ACTIVE = "active";
	private static final String ACTIVE_HISTORY = "activehistory";
	private static final String STATUS = "status";
	private static final String STATUS_HISTORY = "statushistory";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "192.168.202.5", username = "huawei", password = "huawei", flag = STATUS;
		queryFiles(host, username, password, flag);
	}
	
    private static List<String> queryFiles(String host, String username, String password, String flag) {
    	String active = "active/", activehistory = "activehistory/", status = "status/", statushistory = "statushistory/";
    	
        FTPClient ftpClient = getFTPClient(host, username, password);
        boolean suc = false;
        List<String> fileCol = new ArrayList<String>();

		if (ftpClient != null) {
			try {
				System.out.println("current dir is " + ftpClient.printWorkingDirectory());
			} catch (Exception e) {
				e.printStackTrace();
			}
            try {
                if (ACTIVE.equals(flag)) {
                    suc = ftpClient.changeWorkingDirectory(active);
                }
                if (ACTIVE_HISTORY.equals(flag)) {
                    suc = ftpClient.changeWorkingDirectory(activehistory);
                }
                if (STATUS.equals(flag)) {
                    suc = ftpClient.changeWorkingDirectory(status);
                }
                if (STATUS_HISTORY.equals(flag)) {
                    suc = ftpClient.changeWorkingDirectory(statushistory);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
				System.out.println("current dir is " + ftpClient.printWorkingDirectory());
			} catch (Exception e) {
				e.printStackTrace();
			}
            // 打开状态目录失败
            if (!suc) {
                System.out.println(ftpClient.getRemoteAddress() + " can't to ftp " + flag + " dir " + " " + ftpClient.getReplyString());
            }

            // 打开状态目录成功
            try {
            	ftpClient.setControlEncoding("GBK");
                FTPFile[] files = ftpClient.listFiles();

                System.out.println(host + " " + flag + " file count:" + files.length);

                // 循环所有文件
                for (FTPFile file : files) {
                	System.out.println(file.getName());
                    if (!file.getName().endsWith(".txt")) {
                        continue;
                    }
                    String path = "../" + statushistory + file.getName();
                    //HWADS20110111110356.txt
                    if (ftpClient.rename(file.getName(), path)) {
                    	System.out.println("success rename to " + path);
                    } else {
                    	System.out.println("fail rename to " + path + ", reason : " + ftpClient.getReplyString());
                    }
                    fileCol.add(file.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileCol;
    }

    /**
     * 获取ftpclient,如果不能得到（连接不了）则返回NULL
     * @param channelConf EMSFtpChannelCfg
     * @return ftpclient
     */
    private static FTPClient getFTPClient(String host, String username, String password) {
        FTPClient ftpclt = new FTPClient();

        try {
            ftpclt.connect(host);
            ftpclt.login(username, password);
            if (!FTPReply.isPositiveCompletion(ftpclt.getReplyCode())) {
                System.out.println("ftp server " + host + " refused connection.");
                closeFTPClient(ftpclt);
                return null;
            }
            
            try{
				ftpclt.setSoTimeout(1000 * 60 * 3);
			} catch (Exception e) {
				e.printStackTrace();
			}
            return ftpclt;
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return null;
    }

    private static void closeFTPClient(FTPClient ftpclt) {
        if (ftpclt == null) {
            return;
        }
        try {
            ftpclt.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
