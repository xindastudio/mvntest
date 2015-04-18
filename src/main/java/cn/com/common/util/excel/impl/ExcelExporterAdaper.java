package cn.com.common.util.excel.impl;

import java.io.OutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.log4j.Logger;

import cn.com.common.util.excel.DataProvider;
import cn.com.common.util.excel.ExcelExporter;

/**
 * Excel导出工具适配器
 * 
 * @author wuliwei
 * 
 */
public class ExcelExporterAdaper implements ExcelExporter {
	private static final Logger logger = Logger
			.getLogger(ExcelExporterAdaper.class);
	protected int maxRowPerSheet = 50000;
	protected String charsetName = "UTF-8";

	/**
	 * 设置每个工作表最大行数
	 * 
	 * @param pMaxRowPerSheet
	 *            工作表最大行数，默认是50000
	 */
	public void setMaxRowPerSheet(int pMaxRowPerSheet) {
		maxRowPerSheet = pMaxRowPerSheet;
	}

	/**
	 * 设置编码
	 * 
	 * @param pCharsetName
	 *            编码，默认是UTF-8
	 */
	public void setCharsetName(String pCharsetName) {
		charsetName = pCharsetName;
	}

	/**
	 * 根据参数中提供的数据源和输出流，将数据源中的数据输出到输出流，<br/>
	 * 导出完成后，方法体内自行关闭输出流
	 * 
	 * @param pDp
	 *            数据源
	 * @param pOs
	 *            目标输出流
	 * @return 导出操作结果：成功为true，失败为false
	 */
	public boolean export(DataProvider pDp, OutputStream pOs) {
		throw new UnsupportedOperationException(
				"cn.com.common.utils.excel.impl.ExcelExporterAdaper.export(DataProvider, OutputStream) operation unsupported");
	}

	/**
	 * 根据参数中提供的数据源和输出流，以ZIP压缩的形式将数据源中的数据输出到输出流，<br/>
	 * 导出完成后，方法体内自行关闭输出流
	 * 
	 * @param pDp
	 *            数据源
	 * @param pOs
	 *            目标输出流
	 * @param name
	 *            ZIP压缩文档中的条目名
	 * @return 导出操作结果：成功为true，失败为false
	 */
	public boolean exportZip(DataProvider pDp, OutputStream pOs, String name) {
		ZipArchiveOutputStream zos = null;
		ZipArchiveEntry ze = null;
		try {
			zos = new ZipArchiveOutputStream(pOs);
			ze = new ZipArchiveEntry(name);
			zos.setEncoding("GBK");
			zos.putArchiveEntry(ze);
			export(pDp, zos);
			zos.closeArchiveEntry();
			zos.flush();
		} catch (Exception e) {
			logger.error("export zip error : ", e);
		} finally {
			close(zos);
			close(pOs);
		}
		return false;
	}

	/**
	 * 输出操作
	 * 
	 * @param pOs
	 *            目标输出流
	 * @param str
	 *            输出内容
	 * @throws Exception
	 */
	protected void write(OutputStream pOs, String str) throws Exception {
		byte[] b = str.getBytes(charsetName);
		pOs.write(b, 0, b.length);
	}

	protected static void close(OutputStream pOs) {
		try {
			if (null == pOs) {
				return;
			}
			pOs.close();
		} catch (Exception e) {
			logger.error("outputstream close error : ", e);
		}
	}

}
