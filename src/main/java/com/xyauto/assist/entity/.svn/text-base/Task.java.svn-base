package com.xyauto.assist.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "broker_task")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plugin;

    @Column(name = "apply_condition")
    private String applyCondition;

    private String apply;

    @Column(name = "apply_others")
    private String applyOthers;

    @Column(name = "apply_out")
    private String applyOut;

    @Column(name = "finish_percent")
    private BigDecimal finishPercent;

    private Short status;

    private Integer times;

    private Short type;

    @Column(name = "is_target")
    private Short isTarget;

    @Column(name = "start_at")
    private Integer startAt;

    @Column(name = "end_at")
    private Integer endAt;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "is_deleted")
    private Short isDeleted;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", plugin='" + plugin + '\'' +
                ", applyCondition='" + applyCondition + '\'' +
                ", apply='" + apply + '\'' +
                ", finishPercent=" + finishPercent +
                ", status=" + status +
                ", isTarget=" + isTarget +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted=" + isDeleted +
                '}';
    }

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return plugin
     */
    public String getPlugin() {
        return plugin;
    }

    /**
     * @param plugin
     */
    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    /**
     * @return apply
     */
    public String getApply() {
        return apply;
    }

    /**
     * @param apply
     */
    public void setApply(String apply) {
        this.apply = apply;
    }

    public Short getIsTarget() {
        return isTarget;
    }

    public void setIsTarget(Short isTarget) {
        this.isTarget = isTarget;
    }

    /**
     * @return status
     */
    public Short getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Short status) {
        this.status = status;
    }


    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public Integer getEndAt() {
        return endAt;
    }

    public void setEndAt(Integer endAt) {
        this.endAt = endAt;
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

    public Short getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Short isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @return apply_condition
     */
    public String getApplyCondition() {
        return applyCondition;
    }

    /**
     * @param applyCondition
     */
    public void setApplyCondition(String applyCondition) {
        this.applyCondition = applyCondition;
    }

    public BigDecimal getFinishPercent() {
        return finishPercent;
    }

    public void setFinishPercent(BigDecimal finishPercent) {
        this.finishPercent = finishPercent;
    }

    public String getApplyOthers() {
        return applyOthers;
    }

    public void setApplyOthers(String applyOthers) {
        this.applyOthers = applyOthers;
    }

    public String getApplyOut() {
        return applyOut;
    }

    public void setApplyOut(String applyOut) {
        this.applyOut = applyOut;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}