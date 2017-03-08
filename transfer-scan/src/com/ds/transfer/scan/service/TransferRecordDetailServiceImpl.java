package com.ds.transfer.scan.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.transfer.common.util.DateUtil;
import com.ds.transfer.scan.entity.TransferRecordDetailEntity;
import com.ds.transfer.scan.mapper.TransferRecordDetailEntityMapper;

@Service("transferRecordDetailServiceImpl")
public class TransferRecordDetailServiceImpl implements TransferRecordDetailService {

	@Resource
	private TransferRecordDetailEntityMapper transferRecordDetailEntityMapper;

	@Override
	@Transactional
	public TransferRecordDetailEntity update(TransferRecordDetailEntity record, int status, String remark) {
		record.setVersion(record.getVersion() + 1);
		record.setUpdateTime(DateUtil.getCurrentTime());
		record.setStatus(status);
		record.setRemark(remark);
		this.transferRecordDetailEntityMapper.updateByPrimaryKey(record);
		return record;
	}

}
