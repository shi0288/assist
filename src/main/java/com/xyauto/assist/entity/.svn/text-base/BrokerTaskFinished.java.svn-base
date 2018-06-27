package com.xyauto.assist.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by shiqm on 2018-03-03.
 */
@Table(name = "broker_task_day_finished")
public class BrokerTaskFinished {


    @Column(name = "broker_id")
    private Long brokerId;

    @Column(name = "dealer_id")
    private Long dealerId;

    @Column(name = "progress")
    private BigDecimal progress;

    @Column(name = "day_percent")
    private BigDecimal dayPercent;

    @Column(name = "day_limit")
    private BigDecimal dayLimit;

    @Column(name = "week_percent")
    private BigDecimal weekPercent;

    @Column(name = "month_percent")
    private BigDecimal monthPercent;

    @Column(name = "once_percent")
    private BigDecimal oncePercent;

    @Column(name = "temporary_percent")
    private BigDecimal temporaryPercent;

    @Column(name = "cal_time")
    private Date calTime;

    @Column(name = "is_deleted")
    private Long isDeleted;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;


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

    public BigDecimal getProgress() {
        if (progress != null) {
            return progress.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return progress;
    }


    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

    public BigDecimal getDayPercent() {
        return dayPercent;
    }

    public void setDayPercent(BigDecimal dayPercent) {
        this.dayPercent = dayPercent;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public BigDecimal getWeekPercent() {
        return weekPercent;
    }

    public void setWeekPercent(BigDecimal weekPercent) {
        this.weekPercent = weekPercent;
    }

    public BigDecimal getMonthPercent() {
        return monthPercent;
    }

    public void setMonthPercent(BigDecimal monthPercent) {
        this.monthPercent = monthPercent;
    }

    public BigDecimal getOncePercent() {
        return oncePercent;
    }

    public void setOncePercent(BigDecimal oncePercent) {
        this.oncePercent = oncePercent;
    }

    public BigDecimal getTemporaryPercent() {
        return temporaryPercent;
    }

    public void setTemporaryPercent(BigDecimal temporaryPercent) {
        this.temporaryPercent = temporaryPercent;
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
}
