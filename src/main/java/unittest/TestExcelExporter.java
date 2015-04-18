package unittest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.com.common.util.excel.DataProvider;
import cn.com.common.util.excel.impl.ExcelExporter4Xml;

/**
 * @author wuliwei
 * 
 */
public class TestExcelExporter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test4Xml();
	}

	private static void test4Xml() {
		File file = null;
		FileOutputStream fos = null;
		try {
			file = new File("W:/test.zip");
			if (!file.exists() || !file.isFile()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			ExcelExporter4Xml ee = new ExcelExporter4Xml();
			ee.setCharsetName("UTF-8");
			ee.exportZip(new DataProvider() {
				private int rowCount = 110000;
				private int colCount = 12;
				private int curRow = 0;

				@Override
				public Object[] getHeadRow() {
					String[] head = new String[colCount];
					for (int i = 0; i < colCount; i++) {
						head[i] = "行_" + i;
					}
					return head;
				}

				@Override
				public List<Object[]> getNextRows() {
					if (curRow < rowCount) {
						String[] row = new String[colCount];
						for (int i = 0; i < colCount; i++) {
							row[i] = curRow + "_" + i;
						}
						curRow++;
						ArrayList<Object[]> l = new ArrayList<Object[]>();
						l.add(row);
						return l;
					}
					return null;
				}

			}, fos, "测试.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
