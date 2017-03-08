package com.ds.transfer.record.service;

import com.ds.transfer.record.entity.TransferRecordEntity;

/**
 * 转账记录
 * 
 * @author jackson
 *
 */
public interface TransferRecordService {

	TransferRecordEntity insert(Integer siteId, Integer liveId, Long transRecordId, String password, String username, String credit, String billno, String moneyTransferType, String liveType, String moneyTransferRemark, TransferRecordEntity record);

	TransferRecordEntity update(Integer transferStatus, String remark, TransferRecordEntity record);

}
