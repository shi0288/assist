package com.xyauto.assist.mapper.broker;

import com.github.pagehelper.Page;
import com.xyauto.assist.entity.Broker;
import com.xyauto.assist.entity.Task;
import com.xyauto.assist.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * 获取所有任务信息
     * @return
     */
    Page list();


    /**
     * 获取经销商下的经纪人ID
     * @param dealerId
     * @return
     */
    List<Long> getBrokerIdsByDealerId(@Param("dealer_id")Long dealerId,@Param("delete_time")Date deleteTime);

    /**
     * 获取经销商下的经纪人ID和名称
     * @param dealerId
     * @return
     */
    List<Broker> getBrokersByDealerId(@Param("dealer_id")Long dealerId,@Param("delete_time")Date deleteTime);


    /**
     * 获取所有经纪人ID
     * @return
     */
    List<Broker> getBrokerIds(@Param("delete_time")Date deleteTime);

    /**
     * 获取所有经纪人IDCount
     * @return
     */
    int getBrokerIdsCount(@Param("delete_time")Date deleteTime);


    /**
     * 获取所有经销商ID
     * @return
     */
    List<Long> getDealerIds();



    /**
     * 获取所有经销商IDCount
     * @return
     */
    int getDealerIdsCount();


    Broker getBrokerById(@Param("broker_id")Long brokerId);


    List<Task> otherList(@Param("apply")String apply);

    public void tmpAdd(@Param("brokerId")Long brokerId, @Param("taskId")int taskId, @Param("createTime")String createTime, @Param("updateTime")String updateTime);








}