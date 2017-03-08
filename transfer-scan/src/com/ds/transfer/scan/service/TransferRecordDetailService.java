package com.ds.transfer.scan.service;

import com.ds.transfer.scan.entity.TransferRecordDetailEntity;

public interface TransferRecordDetailService {

	TransferRecordDetailEntity update(TransferRecordDetailEntity record, int status, String remark);

}
