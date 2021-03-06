package com.ds.transfer.record.mapper;

import com.ds.transfer.record.entity.H8ApiUserEntity;
import com.ds.transfer.record.entity.H8ApiUserEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface H8ApiUserEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int countByExample(H8ApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int deleteByExample(H8ApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int insert(H8ApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int insertSelective(H8ApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    List<H8ApiUserEntity> selectByExample(H8ApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    H8ApiUserEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int updateByExampleSelective(@Param("record") H8ApiUserEntity record, @Param("example") H8ApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int updateByExample(@Param("record") H8ApiUserEntity record, @Param("example") H8ApiUserEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int updateByPrimaryKeySelective(H8ApiUserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table h8_api_user
     *
     * @mbggenerated Sat Oct 31 16:36:35 CST 2015
     */
    int updateByPrimaryKey(H8ApiUserEntity record);
}