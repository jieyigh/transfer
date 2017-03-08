package com.ds.transfer.http.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.transfer.common.constants.SysConstants;
import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.util.DateUtil;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.StringsUtil;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.http.entity.TransferRecordDetailEntity;
import com.ds.transfer.http.entity.TransferRecordDetailEntityExample;
import com.ds.transfer.http.entity.TransferRecordDetailEntityExample.Criteria;
import com.ds.transfer.http.entity.TransferStatus;
import com.ds.transfer.http.mapper.TransferRecordDetailEntityMapper;
import com.ds.transfer.http.service.TransferRecordDetailService;
import com.ds.transfer.record.entity.TransferRecordEntity;
import com.ds.transfer.record.entity.TransferRecordEntityExample;
import com.ds.transfer.record.mapper.TransferRecordEntityMapper;

@Service("transferRecordDetailServiceImpl")
public class TransferRecordDetailServiceImpl extends CommonTransferService implements TransferRecordDetailService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private TransferRecordDetailEntityMapper transferRecordDetailEntityMapper;

	@Resource
	private TransferRecordEntityMapper transferRecordEntityMapper;

	@Override
	@Transactional
	public TransferRecordDetailEntity insert(TransferRecordDetailEntity record, TransferParam param) {
		record.setBillno(param.getBillno());
		record.setCredit(param.getCredit());
		record.setLiveId(param.getLiveId() + "");
		record.setStatus(TransferStatus.START.value());
		record.setType(param.getType());
		record.setUsername(param.getUsername());
		record.setVersion(1);
		record.setCreateTime(DateUtil.getCurrentTime());
		record.setSiteId(param.getEntity().getSiteId());
		this.transferRecordDetailEntityMapper.insert(record);
		return record;
	}

	@Override
	@Transactional
	public TransferRecordDetailEntity update(TransferRecordDetailEntity record, int status) {
		record.setVersion(record.getVersion() + 1);
		record.setUpdateTime(DateUtil.getCurrentTime());
		record.setStatus(status);
		this.transferRecordDetailEntityMapper.updateByPrimaryKey(record);
		return record;
	}

	@Override
	public TransferRecordDetailEntity query(TransferRecordDetailEntity record) {
		TransferRecordDetailEntityExample example = new TransferRecordDetailEntityExample();
		Criteria createCriteria = example.createCriteria();
		if (!StringsUtil.isNull(record.getUsername())) {
			createCriteria.andUsernameEqualTo(record.getUsername());
		}
		if (record.getSiteId() != null) {
			createCriteria.andSiteIdEqualTo(record.getSiteId());
		}
		if (!StringsUtil.isNull(record.getBillno())) {
			createCriteria.andBillnoEqualTo(record.getBillno());
		}
		List<TransferRecordDetailEntity> selectByExample = this.transferRecordDetailEntityMapper.selectByExample(example);
		if (selectByExample == null) {
			return null;
		}
		logger.info("query record detail size = {}", selectByExample.size());
		return selectByExample.size() <= 0 ? null : selectByExample.get(0);
	}

	@Override
	public String queryRecordByPage(Map<String, Object> resultMap) {
		TransferRecordDetailEntityExample example = new TransferRecordDetailEntityExample();
		Criteria createCriteria = example.createCriteria();
		Criteria createCriteria2 = example.createCriteria();
		//	username, siteId, billno, page, pageLimit, ip);

		if (!StringsUtil.isNull(resultMap.get("username") + "")) {
			createCriteria.andUsernameEqualTo(resultMap.get("username") + "");
			createCriteria2.andUsernameEqualTo(resultMap.get("username") + "");
		}
		if (!StringsUtil.isNull(resultMap.get("siteId") + "")) {
			createCriteria.andSiteIdEqualTo(Integer.valueOf(resultMap.get("siteId") + ""));
			createCriteria2.andSiteIdEqualTo(Integer.valueOf(resultMap.get("siteId") + ""));
		}
		if (!StringsUtil.isNull(resultMap.get("billno") + "")) {
			createCriteria.andBillnoEqualTo(resultMap.get("billno") + "");
			createCriteria2.andBillnoEqualTo(resultMap.get("billno") + "");
		}
		if (!StringsUtil.isNull(resultMap.get("status") + "")) {
			createCriteria.andStatusEqualTo(Integer.valueOf(resultMap.get("status") + ""));
			createCriteria2.andStatusEqualTo(Integer.valueOf(resultMap.get("status") + ""));
		}
		if (!StringsUtil.isNull(resultMap.get("startTime") + "")) {
			createCriteria.andCreateTimeGreaterThanOrEqualTo(resultMap.get("startTime") + "");
			createCriteria2.andCreateTimeGreaterThanOrEqualTo(resultMap.get("startTime") + "");
		}
		if (!StringsUtil.isNull(resultMap.get("endTime") + "")) {
			createCriteria.andCreateTimeLessThanOrEqualTo(resultMap.get("endTime") + "");
			createCriteria2.andCreateTimeLessThanOrEqualTo(resultMap.get("endTime") + "");
		}
		String fromLive = StringsUtil.isNull(resultMap.get("fromLive") + "") ? "" : resultMap.get("fromLive") + "";
		String toLive = StringsUtil.isNull(resultMap.get("toLive") + "") ? "" : resultMap.get("toLive") + "";
		if (StringsUtil.isNull(fromLive)) { //任何来源
			if (!StringsUtil.isNull(toLive)) { //去向
				//全部转入DS主账户
				if(SysConstants.LiveId.DS.equals(toLive)){
					createCriteria.andLiveIdNotLike("_");
					createCriteria2.andLiveIdNotLike("_");
				}else {
					createCriteria.andLiveIdEqualTo(toLive);
					createCriteria2.andLiveIdEqualTo(toLive);
				}
			}
		} else {
			if (StringsUtil.isNull(toLive)) {
				if(SysConstants.LiveId.DS.equals(fromLive)){
					createCriteria.andLiveIdNotEqualTo("_");
					createCriteria2.andLiveIdNotEqualTo("_");
				}else {
					createCriteria.andLiveIdEqualTo(fromLive);
					createCriteria2.andLiveIdEqualTo(fromLive);
				}
			} else {
				if (SysConstants.LiveId.DS.equals(fromLive)) { //DS
					createCriteria.andLiveIdEqualTo(toLive);
					createCriteria.andTypeEqualTo(IN);

					createCriteria2.andLiveIdEqualTo(toLive);
					createCriteria2.andTypeEqualTo(IN);
				}else if (SysConstants.LiveId.TOTAL.equals(fromLive)){
					createCriteria.andLiveIdEqualTo(fromLive);
					createCriteria2.andLiveIdEqualTo(fromLive);
				} else { // !DS
					if (SysConstants.LiveId.DS.equals(toLive)) { //DS
						createCriteria.andLiveIdEqualTo(fromLive);
						createCriteria.andTypeEqualTo(OUT);

						createCriteria2.andLiveIdEqualTo(fromLive);
						createCriteria2.andTypeEqualTo(OUT);
					} else {
						createCriteria.andLiveIdEqualTo(fromLive + "_" + toLive);
						createCriteria.andTypeEqualTo(IN);

						createCriteria2.andLiveIdEqualTo(toLive + "_" + fromLive);
						createCriteria2.andTypeEqualTo(OUT);
					}
				}
			}
		}
		example.or(createCriteria2);
		Long total = queryRecordCount(example);
		List<TransferRecordDetailEntity> data = queryRecordData(example, resultMap);
		resultMap.clear();
		resultMap.put(STATUS, SUCCESS);
		resultMap.put(MESSAGE, "success");
		resultMap.put("data", data);
		resultMap.put("total", total);
		return JSONUtils.map2Json(resultMap);
	}

	private Long queryRecordCount(TransferRecordDetailEntityExample example) {
		Long total = this.transferRecordDetailEntityMapper.queryRecordCount(example);
		logger.info("total = {}", total);
		return total == null ? 0L : total;
	}

	private List<TransferRecordDetailEntity> queryRecordData(TransferRecordDetailEntityExample example, Map<String, Object> resultMap) {
		Integer page = Integer.valueOf(resultMap.get("page") + "");
		Integer pageLimit = Integer.valueOf(resultMap.get("pageLimit") + "");
		example.setPage((page - 1) * pageLimit);
		example.setPageLimit(pageLimit);
		List<TransferRecordDetailEntity> data = this.transferRecordDetailEntityMapper.queryRecord(example);
		return data;
	}

	@Override
	public String queryRecordDetail(Long transferRecordId) {
		TransferRecordEntityExample example = new TransferRecordEntityExample();
		com.ds.transfer.record.entity.TransferRecordEntityExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andTransRecordIdEqualTo(transferRecordId);
		List<TransferRecordEntity> list = this.transferRecordEntityMapper.selectByExample(example);
		Map<String, Object> resultMap = success(JSONUtils.bean2Json(list));
		return JSONUtils.map2Json(resultMap);
	}
}
