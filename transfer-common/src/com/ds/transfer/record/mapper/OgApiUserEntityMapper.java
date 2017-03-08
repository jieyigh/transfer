package com.ds.transfer.record.mapper;

import com.ds.transfer.record.entity.OgApiUserEntity;
import com.ds.transfer.record.entity.OgApiUserEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OgApiUserEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int countByExample(OgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int deleteByExample(OgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int insert(OgApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int insertSelective(OgApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    List<OgApiUserEntity> selectByExample(OgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    OgApiUserEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int updateByExampleSelective(@Param("record") OgApiUserEntity record, @Param("example") OgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int updateByExample(@Param("record") OgApiUserEntity record, @Param("example") OgApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int updateByPrimaryKeySelective(OgApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table og_api_user
     *
     * @mbggenerated Thu Nov 26 17:10:08 CST 2015
     */
    int updateByPrimaryKey(OgApiUserEntity record);
}