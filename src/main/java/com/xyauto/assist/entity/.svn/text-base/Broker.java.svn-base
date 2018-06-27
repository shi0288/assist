package com.xyauto.assist.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by shiqm on 2018-03-01.
 */

@Table(name = "broker")
public class Broker {

    @Id
    @Column(name = "broker_id")
    private Long brokerId;

    @Column(name = "dealer_id")
    private Long dealerId;

    @Column(name = "user_token")
    private String userToken;

    private String name;

    @Column(name="is_deleted")
    private int isDeleted;

    @Column(name = "delete_time")
    private Date deleteTime;


    public Long getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Long brokerId) {
        this.brokerId = brokerId;
    }

    public Long getDealerId() {
        return dealerId;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
}
