package com.ds.transfer.http.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.http.entity.TransferRecordDetailEntity;

/**
 * 记录详情插入
 * 
 * @author jackson
 *
 */
public interface TransferRecordDetailService {

	TransferRecordDetailEntity insert(@Param("record") TransferRecordDetailEntity record, @Param("param") TransferParam param);

	TransferRecordDetailEntity update(TransferRecordDetailEntity record, int status);

	TransferRecordDetailEntity query(TransferRecordDetailEntity record);

	String queryRecordByPage(Map<String, Object> resultMap);

	String queryRecordDetail(Long transferRecordId);

}
