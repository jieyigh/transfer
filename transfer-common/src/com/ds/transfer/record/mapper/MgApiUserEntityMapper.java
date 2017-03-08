package com.ds.transfer.record.mapper;

import com.ds.transfer.record.entity.MgApiUserEntity;
import com.ds.transfer.record.entity.MgApiUserEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MgApiUserEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int countByExample(MgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int deleteByExample(MgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int insert(MgApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int insertSelective(MgApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    List<MgApiUserEntity> selectByExample(MgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    MgApiUserEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int updateByExampleSelective(@Param("record") MgApiUserEntity record, @Param("example") MgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int updateByExample(@Param("record") MgApiUserEntity record, @Param("example") MgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int updateByPrimaryKeySelective(MgApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mg_api_user
     *
     * @mbggenerated Sat Jul 23 17:05:13 CST 2016
     */
    int updateByPrimaryKey(MgApiUserEntity record);
}