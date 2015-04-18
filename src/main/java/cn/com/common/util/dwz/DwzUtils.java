package cn.com.common.util.dwz;

import com.danga.MemCached.MemCachedClient;

/**
 * 短网址生成器
 * 
 * @author wuxufeng
 * 
 */
public class DwzUtils {
	private MemCachedClient mcc = null;

	public DwzUtils() {
		mcc = new MemCachedClient("DwzUtils-MemCachedClient");
	}

}
