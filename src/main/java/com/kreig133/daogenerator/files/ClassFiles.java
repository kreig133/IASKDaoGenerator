package com.kreig133.daogenerator.files;

/**
 * Перечень необходимых в проекте классов
 * 
 * @author mfayzullin
 * 
 */
public enum ClassFiles {

	SB_CACHE_KEY("SbCacheKey.txt", "com.aplana.sbrf.deposit.common.server.cache.SbCacheKey"),
	SB_EHCACHE_CACHE("SbEhcacheCache.txt", "com.aplana.sbrf.deposit.common.server.cache.SbEhcacheCache");

	private String fileName;
	private String className;

	ClassFiles(String fileName, String className) {
		this.fileName = fileName;
		this.className = className;
	}

	public String getFileName() {
		return fileName;
	}

	public String getClassName() {
		return className;
	}

}
