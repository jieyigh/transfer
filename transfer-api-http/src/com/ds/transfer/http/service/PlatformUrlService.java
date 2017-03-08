package com.ds.transfer.http.service;

import java.util.List;

public interface PlatformUrlService {

	public List<String> queryPlatformURL(String platformType);

	public boolean modPlatformURL(String platformType, String platformUrl, Integer siteId);

	public String queryCurrentPlatformURL(String platformType, Integer siteId);

}
