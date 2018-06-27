package com.xyauto.assist.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by shiqm on 2018-03-03.
 */
@Table(name = "dealer_task_month_finished")
public class DealerTaskMonthFinished {

    @Column(name = "dealer_id")
    private Long dealerId;

    @Column(name = "progress")
    private BigDecimal progress;

    @Column(name = "had_percent")
    private BigDecimal hadPercent;

    @Column(name = "examine_day_number")
    private Integer examineDayNumber;

    @Column(name = "valid_broker_number")
    private Integer validBrokerNumber;

    @Column(name = "examine_start_time")
    private Date examineStartTime;

    @Column(name = "schedule_start_time")
    private Date scheduleStartTime;

    @Column(name = "schedule_end_time")
    private Date scheduleEndTime;

    @Column(name = "cal_time")
    private Date calTime;

    @JSONField(serialize=false)
    @Column(name = "is_deleted")
    private Long isDeleted;

    @JSONField(serialize=false)
    @Column(name = "create_time")
    private Date createTime;

    @JSONField(serialize=false)
    @Column(name = "update_time")
    private Date updateTime;

    @Transient
    private BigDecimal score;


    public Long getDealerId() {
        return dealerId;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public BigDecimal getProgress() {
        if (progress != null) {
            return progress.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return progress;
    }

    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

    public BigDecimal getHadPercent() {
        return hadPercent;
    }

    public void setHadPercent(BigDecimal hadPercent) {
        this.hadPercent = hadPercent;
    }

    public Integer getExamineDayNumber() {
        return examineDayNumber;
    }

    public void setExamineDayNumber(Integer examineDayNumber) {
        this.examineDayNumber = examineDayNumber;
    }

    public Date getCalTime() {
        return calTime;
    }

    public void setCalTime(Date calTime) {
        this.calTime = calTime;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public Integer getValidBrokerNumber() {
        return validBrokerNumber;
    }

    public void setValidBrokerNumber(Integer validBrokerNumber) {
        this.validBrokerNumber = validBrokerNumber;
    }

    public Date getExamineStartTime() {
        return examineStartTime;
    }

    public void setExamineStartTime(Date examineStartTime) {
        this.examineStartTime = examineStartTime;
    }

    public Date getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(Date scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public Date getScheduleEndTime() {
        return scheduleEndTime;
    }

    public void setScheduleEndTime(Date scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }

    public BigDecimal getScore() {
        if (progress != null) {
            return progress.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return progress;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}



