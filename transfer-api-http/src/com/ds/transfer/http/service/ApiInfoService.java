package com.ds.transfer.http.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.ApiInfoEntityExample;
import com.ds.transfer.record.entity.ApiInfoEntityExample.Criteria;
import com.ds.transfer.record.mapper.ApiInfoEntityMapper;

/**
 * 扫描api_info 这张表
 * @author jackson
 *
 */
@Service
public class ApiInfoService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ApiInfoEntityMapper apiInfoMapper;

	/**
	 * agentInfoMap ====== key:projectAgent value:entity
	 */
	private Map<String, ApiInfoEntity> agentInfoMap = new HashMap<String, ApiInfoEntity>();
	/**
	 * by guangguang 为了兼容新的api模式故定义此变量，此变量以siteId+isDemo+LiveId，三者为key
	 * siteIdLiveIdMap ======== key：siteId+isDemo+LiveId value:entity
	 */
	private Map<String, ApiInfoEntity> siteIdLiveIdMap = new HashMap<String, ApiInfoEntity>();

	List<ApiInfoEntity> list = new ArrayList<ApiInfoEntity>();

	@PostConstruct
	public List<ApiInfoEntity> selectApiInfo() {
		list.clear();
		logger.info("初始化 配置信息...................." + new Date());
		ApiInfoEntityExample apiInfoExample = new ApiInfoEntityExample();
		apiInfoExample.createCriteria();
		list = apiInfoMapper.selectByExample(apiInfoExample);
		return list;
	}

	@PostConstruct
	@Scheduled(cron = "${spring.schedule}")
	public void job() {
//		agentInfoMap.clear();
//		siteIdLiveIdMap.clear();
		logger.info("初始化 配置信息...................." + new Date());
		ApiInfoEntityExample apiInfoExample = new ApiInfoEntityExample();
		apiInfoExample.createCriteria();
		list = apiInfoMapper.selectByExampleWithBLOBs(apiInfoExample);

		for (int i = 0; i < list.size(); i++) {
			ApiInfoEntity apiInfo = list.get(i);
			agentInfoMap.put(apiInfo.getProjectAgent(), apiInfo);//通过代理获取信息
			String siteIdLiveIdMapKey = apiInfo.getSiteId().toString() + "" + apiInfo.getIsDemo() + "" + apiInfo.getLiveId();//不用理会空值问题
			siteIdLiveIdMap.put(siteIdLiveIdMapKey, apiInfo);
		}
	}

	/**
	 * 根据网站id查询apiinfo信息(除了ds的),目前是为了查询每个网站接入的apiinfo信息
	 * @param siteId
	 * @return
	 */
	public List<ApiInfoEntity> queryApiInfoBySiteId(String siteId) {
		ApiInfoEntityExample example = new ApiInfoEntityExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andSiteIdEqualTo(Integer.valueOf(siteId));
		createCriteria.andLiveStatusEqualTo(50);
		createCriteria.andIsDemoEqualTo(1);
		createCriteria.andLiveIdNotEqualTo(12);//DS
		createCriteria.andLiveIdNotEqualTo(99999);//资金归集
		List<ApiInfoEntity> apiList = this.apiInfoMapper.selectByExample(example);
		return apiList;
	}

	public ApiInfoEntityMapper getApiInfoMapper() {
		return apiInfoMapper;
	}

	public void setApiInfoMapper(ApiInfoEntityMapper apiInfoMapper) {
		this.apiInfoMapper = apiInfoMapper;
	}

	public Map<String, ApiInfoEntity> getSiteIdLiveIdMap() {
		return siteIdLiveIdMap;
	}

	public void setSiteIdLiveIdMap(Map<String, ApiInfoEntity> siteIdLiveIdMap) {
		this.siteIdLiveIdMap = siteIdLiveIdMap;
	}

}
