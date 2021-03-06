package com.ds.transfer.record.mapper;

import com.ds.transfer.record.entity.BbinApiUserEntity;
import com.ds.transfer.record.entity.BbinApiUserEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BbinApiUserEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int countByExample(BbinApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int deleteByExample(BbinApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int insert(BbinApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int insertSelective(BbinApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    List<BbinApiUserEntity> selectByExample(BbinApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    BbinApiUserEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int updateByExampleSelective(@Param("record") BbinApiUserEntity record, @Param("example") BbinApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int updateByExample(@Param("record") BbinApiUserEntity record, @Param("example") BbinApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int updateByPrimaryKeySelective(BbinApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bbin_api_user
     *
     * @mbggenerated Sat Oct 31 16:35:21 CST 2015
     */
    int updateByPrimaryKey(BbinApiUserEntity record);
}