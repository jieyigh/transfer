package com.ds.transfer.http.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ds.transfer.http.entity.TransferRecordDetailEntity;
import com.ds.transfer.http.entity.TransferRecordDetailEntityExample;

public interface TransferRecordDetailEntityMapper {
	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int countByExample(TransferRecordDetailEntityExample example);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int deleteByExample(TransferRecordDetailEntityExample example);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int insert(TransferRecordDetailEntity record);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int insertSelective(TransferRecordDetailEntity record);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	List<TransferRecordDetailEntity> selectByExample(TransferRecordDetailEntityExample example);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	TransferRecordDetailEntity selectByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int updateByExampleSelective(@Param("record") TransferRecordDetailEntity record, @Param("example") TransferRecordDetailEntityExample example);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int updateByExample(@Param("record") TransferRecordDetailEntity record, @Param("example") TransferRecordDetailEntityExample example);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int updateByPrimaryKeySelective(TransferRecordDetailEntity record);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table transfer_record_detail
	 *
	 * @mbggenerated Fri Dec 04 21:01:04 CST 2015
	 */
	int updateByPrimaryKey(TransferRecordDetailEntity record);

	/** 分页查询 */
	List<TransferRecordDetailEntity> queryRecord(TransferRecordDetailEntityExample example);

	/** 查询记录数 */
	Long queryRecordCount(TransferRecordDetailEntityExample example);
}