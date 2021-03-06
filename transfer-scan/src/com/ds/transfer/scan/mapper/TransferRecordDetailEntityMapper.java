package com.ds.transfer.scan.mapper;

import com.ds.transfer.scan.entity.TransferRecordDetailEntity;
import com.ds.transfer.scan.entity.TransferRecordDetailEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransferRecordDetailEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int countByExample(TransferRecordDetailEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int deleteByExample(TransferRecordDetailEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int insert(TransferRecordDetailEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int insertSelective(TransferRecordDetailEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    List<TransferRecordDetailEntity> selectByExample(TransferRecordDetailEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    TransferRecordDetailEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int updateByExampleSelective(@Param("record") TransferRecordDetailEntity record, @Param("example") TransferRecordDetailEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int updateByExample(@Param("record") TransferRecordDetailEntity record, @Param("example") TransferRecordDetailEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int updateByPrimaryKeySelective(TransferRecordDetailEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record_detail
     *
     * @mbggenerated Fri Dec 04 21:00:30 CST 2015
     */
    int updateByPrimaryKey(TransferRecordDetailEntity record);
}