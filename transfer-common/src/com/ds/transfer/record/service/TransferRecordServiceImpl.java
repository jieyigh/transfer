package com.ds.transfer.record.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.transfer.common.constants.SysConstants;
import com.ds.transfer.common.util.DateUtil;
import com.ds.transfer.common.util.EncryptUtils;
import com.ds.transfer.record.entity.TransferRecordEntity;
import com.ds.transfer.record.mapper.TransferRecordEntityMapper;

@Service
public class TransferRecordServiceImpl implements TransferRecordService {

	@Autowired
	private TransferRecordEntityMapper transferRecordMapper;

	/**
	 * 插入转账记录
	 * lastModify by :guang
	 * lastModifyTime ：2015-12-19
	 */
	@Override
	@Transactional
	public TransferRecordEntity insert(Integer siteId, Integer liveId, Long transRecordId, String password, String username, String credit, String billno, String moneyTransferType, String liveType, String moneyTransferRemark, TransferRecordEntity record) {
		record.setUsername(username);//用户名
		record.setPassword(password);//密码
		record.setTransferMoney(credit + "");
		record.setTransBillno(billno);//唯一转账编码
		record.setTransStatus(SysConstants.Record.TRANS_START);//转账状态  0表示开始转账
		record.setTransType(moneyTransferType);//转账类型
		record.setRemark(moneyTransferRemark);
		record.setCreateTime(DateUtil.getCurrentTime());
		record.setLiveType(liveType);//center 表示钱包
		record.setLiveId(liveId);
		record.setSiteId(siteId);//网站编号
		record.setTransRecordId(transRecordId);
		record.setFinger(EncryptUtils.encrypt(record.getUsername(),record.getTransferMoney(),record.getTransBillno(),record.getTransStatus().toString()));
		
		this.transferRecordMapper.insert(record);
		return record;
	}

	/**
	 * 更新转账记录
	 */
	@Override
	@Transactional
	public TransferRecordEntity update(Integer transferStatus, String remark, TransferRecordEntity record) {
		record.setTransStatus(transferStatus);
		record.setUpdateTime(DateUtil.getCurrentTime());
		record.setRemark(remark);
		record.setFinger(EncryptUtils.encrypt(record.getUsername(),record.getTransferMoney(),record.getTransBillno(),record.getTransStatus().toString()));
		this.transferRecordMapper.updateByPrimaryKey(record);
		return record;
	}
	
}
