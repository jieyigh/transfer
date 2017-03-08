package com.ds.transfer.http.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ds.transfer.http.entity.PlatformUrl;
import com.ds.transfer.http.entity.PlatformUrlExample;
import com.ds.transfer.http.mapper.PlatformUrlMapper;
import com.ds.transfer.http.service.PlatformUrlService;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.ApiInfoEntityExample;
import com.ds.transfer.record.mapper.ApiInfoEntityMapper;

@Service("platformUrlServiceImpl")
public class PlatformUrlServiceImpl implements PlatformUrlService {
	private Logger logger = LoggerFactory.getLogger(PlatformUrlServiceImpl.class);

	@Resource
	private PlatformUrlMapper platformUrlMapper;

	@Resource
	private ApiInfoEntityMapper apiInfoEntityMapper;

	@Override
	public List<String> queryPlatformURL(String platformType) {
		List<String> platformUrl = new ArrayList<String>();
		PlatformUrlExample e = new PlatformUrlExample();
		e.createCriteria().andPlatformTypeEqualTo(platformType);
		List<PlatformUrl> list = platformUrlMapper.selectByExample(e);
		for (PlatformUrl p : list) {
			platformUrl.add(p.getPlatformUrl());

		}
		return platformUrl;
	}

	@Override
	public String queryCurrentPlatformURL(String platformType, Integer siteId) {
		ApiInfoEntityExample e = new ApiInfoEntityExample();
		e.createCriteria().andLiveNameEqualTo(platformType).andSiteIdEqualTo(siteId);
		List<ApiInfoEntity> apiInfoList = apiInfoEntityMapper.selectByExample(e);
		String currentUrl = apiInfoList.get(0).getBbinUrl();
		return currentUrl;
	}

	@Override
	public boolean modPlatformURL(String platformType, String platformUrl, Integer siteId) {
		try {
			Pattern pattern = Pattern.compile("\\.(\\w*\\.com)");
			ApiInfoEntityExample e = new ApiInfoEntityExample();
			e.createCriteria().andLiveNameEqualTo(platformType).andSiteIdEqualTo(siteId);
			List<ApiInfoEntity> list = apiInfoEntityMapper.selectByExample(e);
			for (ApiInfoEntity api : list) {
				String oldUrl = api.getBbinUrl();
				Matcher matcher = pattern.matcher(oldUrl);
				if (matcher.find()) {
					String newUrl = oldUrl.replaceAll(matcher.group(1), platformUrl);
					logger.info("modPlatformURL old url={} , new url={}", oldUrl, newUrl);
					api.setBbinUrl(newUrl);
				}
				apiInfoEntityMapper.updateByPrimaryKey(api);
			}
			return true;
		} catch (Exception e) {
			logger.error("modPlatformURL error ", e);
		}
		return false;
	}

}
