package com.ds.transfer.money.entity;

public class TransferMoneyKeyEntity {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transfer_money_key.id
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transfer_money_key.type
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transfer_money_key.live_id
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    private Integer liveId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transfer_money_key.from_key_type
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    private Integer fromKeyType;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transfer_money_key.id
     *
     * @return the value of transfer_money_key.id
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transfer_money_key.id
     *
     * @param id the value for transfer_money_key.id
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transfer_money_key.type
     *
     * @return the value of transfer_money_key.type
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transfer_money_key.type
     *
     * @param type the value for transfer_money_key.type
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transfer_money_key.live_id
     *
     * @return the value of transfer_money_key.live_id
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    public Integer getLiveId() {
        return liveId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transfer_money_key.live_id
     *
     * @param liveId the value for transfer_money_key.live_id
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transfer_money_key.from_key_type
     *
     * @return the value of transfer_money_key.from_key_type
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    public Integer getFromKeyType() {
        return fromKeyType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transfer_money_key.from_key_type
     *
     * @param fromKeyType the value for transfer_money_key.from_key_type
     *
     * @mbggenerated Tue Nov 24 20:39:37 CST 2015
     */
    public void setFromKeyType(Integer fromKeyType) {
        this.fromKeyType = fromKeyType;
    }
}