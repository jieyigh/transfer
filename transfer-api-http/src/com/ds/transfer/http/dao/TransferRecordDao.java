package com.ds.transfer.http.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ds.transfer.http.vo.ds.TotalBalanceParam;

public interface TransferRecordDao {

	List<String> totalBalanceBySiteId(@Param("param") TotalBalanceParam param);

}
