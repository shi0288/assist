package com.xyauto.assist.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mcp.fastcloud.annotation.Log;
import com.mcp.fastcloud.util.Result;
import com.mcp.fastcloud.util.enums.ResultCode;
import com.xyauto.assist.cloud.DealerSeverCloud;
import com.xyauto.assist.cloud.XyhPlatFormServerCloud;
import com.xyauto.assist.entity.Broker;
import com.xyauto.assist.entity.BrokerTaskFinished;
import com.xyauto.assist.entity.BrokerTaskMonthFinished;
import com.xyauto.assist.entity.DealerTaskMonthFinished;
import com.xyauto.assist.mapper.broker.BrokerMapper;
import com.xyauto.assist.mapper.broker.TaskMapper;
import com.xyauto.assist.util.ArithUtil;
import com.xyauto.assist.util.DateStyle;
import com.xyauto.assist.util.DateUtil;
import com.xyauto.assist.util.FinishedResult;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.xyauto.assist.mapper.log.*;
import com.xyauto.assist.util.Cache;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.util.Lists;

/**
 * Created by shiqm on 2018-03-03.
 */
@Service
public class BrokerTaskFinishedService {

    @Autowired
    private BrokerTaskFinishedMapper brokerTaskFinishedMapper;
    @Autowired
    private BrokerTaskMonthFinishedMapper brokerTaskMonthFinishedMapper;
    @Autowired
    private DealerTaskMonthFinishedMapper dealerTaskMonthFinishedMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private XyhPlatFormServerCloud xyhPlatFormServerCloud;
    @Autowired
    private DealerSeverCloud dealerSeverCloud;
    @Autowired
    private Cache cache;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private BrokerMapper brokerMapper;
    @Log
    protected Logger logger;

    /**
     * 指定经纪人某天的日完成度
     */
    public BrokerTaskFinished getBrokerDayFinishPercent(Long broker_id, Date currentTime) {
        Broker broker = taskService.getBrokerById(broker_id);
        if (broker == null) {
            return null;
        }
        return this.getBrokerDayFinishPercent(broker_id, broker.getDealerId(), currentTime);
    }

    /**
     * 查询经纪人是否在排期内
     */
    public boolean isBrokerSchedule(Long broker_id, Long dealer_id, Date time) {
        String key = "broker_task_dealer_schedule_" + dealer_id + "_" + DateUtil.DateToString(time, "yyyy_MM_dd");
        if (cache.exists(key)) {
            return Boolean.valueOf(cache.get(key));
        }
        boolean ret = false;
        try {
            Result result = xyhPlatFormServerCloud.isBrokerSchedule(broker_id, DateUtil.DateToString(time, "yyyy-MM-dd 23:59:59"));
            if (result.getCode() == 10000 && Boolean.valueOf(result.getData().toString())) {
                ret = true;
                cache.set(key, String.valueOf(ret), 60 * 60L);
                return ret;
            }
        } catch (Exception e) {
            Result result = xyhPlatFormServerCloud.isBrokerSchedule(broker_id, DateUtil.DateToString(time, "yyyy-MM-dd 23:59:59"));
            if (result.getCode() == 10000 && Boolean.valueOf(result.getData().toString())) {
                ret = true;
                cache.set(key, String.valueOf(ret), 60 * 60L);
                return ret;
            }
        }
        return ret;
    }

    /**
     * 查询经纪人是否在排期内
     */
    public boolean isBrokerSchedule(Long broker_id, Date time) {
        boolean ret = false;
        try {
            Result result = xyhPlatFormServerCloud.isBrokerSchedule(broker_id, DateUtil.DateToString(time, "yyyy-MM-dd 23:59:59"));
            if (result.getCode() == 10000 && Boolean.valueOf(result.getData().toString())) {
                ret = true;
            }
        } catch (Exception e) {
            try {
                Result result = xyhPlatFormServerCloud.isBrokerSchedule(broker_id, DateUtil.DateToString(time, "yyyy-MM-dd 23:59:59"));
                if (result.getCode() == 10000 && Boolean.valueOf(result.getData().toString())) {
                    ret = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 指定经纪人某天的日完成度
     */
    public BrokerTaskFinished getBrokerDayFinishPercent(Long broker_id, Long dealer_id, Date currentTime) {
        if (!this.isBrokerSchedule(broker_id, dealer_id, currentTime)) {
            return null;
        }
        Example example = new Example(BrokerTaskFinished.class);
        example.createCriteria()
                .andEqualTo("brokerId", broker_id)
                .andEqualTo("calTime", currentTime);
        BrokerTaskFinished brokerTaskFinished = null;
        try {
            brokerTaskFinished = brokerTaskFinishedMapper.selectOneByExample(example);
        } catch (Exception e) {
            return null;
        }
        if (brokerTaskFinished == null) {
            FinishedResult finishedResult = taskService.getBrokerFinishPercent(broker_id.toString(), currentTime);
            brokerTaskFinished = new BrokerTaskFinished();
            brokerTaskFinished.setBrokerId(broker_id);
            brokerTaskFinished.setDealerId(dealer_id);
            brokerTaskFinished.setCalTime(currentTime);
            brokerTaskFinished.setProgress(finishedResult.getProgress());
            brokerTaskFinished.setDayPercent(finishedResult.getDayPercent());
            brokerTaskFinished.setDayLimit(finishedResult.getDayLimit());
            brokerTaskFinished.setWeekPercent(finishedResult.getWeekPercent());
            brokerTaskFinished.setMonthPercent(finishedResult.getMonthPercent());
            brokerTaskFinished.setOncePercent(finishedResult.getOncePercent());
            brokerTaskFinished.setTemporaryPercent(finishedResult.getTemporaryPercent());
        }
        return brokerTaskFinished;
    }

    /**
     * 指定经纪人某天的月完成度
     */
    public BrokerTaskMonthFinished getBrokerMonthFinishPercent(Long broker_id, Date currentTime) {
        Broker broker = taskService.getBrokerById(broker_id);
        if (broker == null) {
            return null;
        }
        return this.getBrokerMonthFinishPercent(broker_id, broker.getDealerId(), currentTime);
    }

    public BrokerTaskMonthFinished getBrokerMonthFinishPercent(Long broker_id, Long dealer_id, Date currentTime) {
        return this.getBrokerMonthFinishPercent(null, broker_id, dealer_id, currentTime);
    }

    /**
     * 指定经纪人某天的月完成度
     */
    public BrokerTaskMonthFinished getBrokerMonthFinishPercent(BrokerTaskFinished brokerTaskFinished, Long broker_id, Long dealer_id, Date currentTime) {
        if (!this.isBrokerSchedule(broker_id, dealer_id, currentTime)) {
            return null;
        }
        Example example = new Example(BrokerTaskMonthFinished.class);
        example.createCriteria()
                .andEqualTo("brokerId", broker_id)
                .andEqualTo("calTime", currentTime);
        BrokerTaskMonthFinished brokerTaskMonthFinished = null;
        try {
            brokerTaskMonthFinished = brokerTaskMonthFinishedMapper.selectOneByExample(example);
        } catch (Exception e) {
            return null;
        }
        if (brokerTaskMonthFinished == null) {
            if (brokerTaskFinished == null) {
                brokerTaskFinished = this.getBrokerDayFinishPercent(broker_id, dealer_id, currentTime);
            }
            PageHelper.startPage(1, 1, false).setOrderBy("id desc");
            example = new Example(BrokerTaskMonthFinished.class);
            example.createCriteria()
                    .andEqualTo("brokerId", broker_id)
                    .andGreaterThanOrEqualTo("createTime", DateUtil.getDateStart(currentTime, DateStyle.FIELD_TYPE.MONTH, 0))
                    .andLessThan("createTime", DateUtil.getDateStart(currentTime, DateStyle.FIELD_TYPE.MONTH, 1));
            brokerTaskMonthFinished = brokerTaskMonthFinishedMapper.selectOneByExample(example);
            if (brokerTaskMonthFinished == null) {
                brokerTaskMonthFinished = new BrokerTaskMonthFinished();
                brokerTaskMonthFinished.setDealerId(dealer_id);
                brokerTaskMonthFinished.setBrokerId(broker_id);
                brokerTaskMonthFinished.setExamineDayNumber(1);
            } else {
                brokerTaskMonthFinished.setCreateTime(null);
                brokerTaskMonthFinished.setUpdateTime(null);
                brokerTaskMonthFinished.setExamineDayNumber(brokerTaskMonthFinished.getExamineDayNumber() + 1);
            }
            brokerTaskMonthFinished.setDayPercent(ArithUtil.add(brokerTaskFinished.getDayPercent(), brokerTaskMonthFinished.getDayPercent()));
            brokerTaskMonthFinished.setWeekPercent(ArithUtil.add(brokerTaskFinished.getWeekPercent(), brokerTaskMonthFinished.getWeekPercent()));
            brokerTaskMonthFinished.setMonthPercent(ArithUtil.add(brokerTaskFinished.getMonthPercent(), brokerTaskMonthFinished.getMonthPercent()));
            brokerTaskMonthFinished.setOncePercent(ArithUtil.add(brokerTaskFinished.getOncePercent(), brokerTaskMonthFinished.getOncePercent()));
            brokerTaskMonthFinished.setTemporaryPercent(ArithUtil.add(brokerTaskFinished.getTemporaryPercent(), brokerTaskMonthFinished.getTemporaryPercent()));
            brokerTaskMonthFinished.setHadPercent(ArithUtil.add(
                    brokerTaskMonthFinished.getHadPercent(),
                    brokerTaskFinished.getDayPercent(),
                    brokerTaskFinished.getWeekPercent(),
                    brokerTaskFinished.getMonthPercent(),
                    brokerTaskFinished.getTemporaryPercent(),
                    brokerTaskFinished.getOncePercent()
            ));
            brokerTaskMonthFinished.setCalTime(currentTime);
            brokerTaskMonthFinished.setDayLimit(brokerTaskFinished.getDayLimit());

            //顾问月任务完成度=本月顾问得分÷(日任务分上限×已考核天数+本月已完成周任务分+本月已完成月任务分+本月已完成临时任务分+本月已完成一次性任务分)
            brokerTaskMonthFinished.setProgress(
                    brokerTaskMonthFinished.getHadPercent().divide(
                            ArithUtil.add(
                                    brokerTaskMonthFinished.getDayLimit().multiply(BigDecimal.valueOf(brokerTaskMonthFinished.getExamineDayNumber())),
                                    brokerTaskMonthFinished.getWeekPercent(),
                                    brokerTaskMonthFinished.getMonthPercent(),
                                    brokerTaskMonthFinished.getTemporaryPercent(),
                                    brokerTaskMonthFinished.getTemporaryPercent()
                            ),
                            2, BigDecimal.ROUND_HALF_UP));
        }
        return brokerTaskMonthFinished;
    }

    public List<DealerTaskMonthFinished> getDealerMonthFinishPercentByTime(Long dealerId, Date startTime, Date endTime) {
        Example example = new Example(DealerTaskMonthFinished.class);
        example.createCriteria()
                .andEqualTo("dealerId", dealerId)
                .andGreaterThanOrEqualTo("calTime", startTime)
                .andLessThan("calTime", endTime);
        return dealerTaskMonthFinishedMapper.selectByExample(example);
    }

    public List<BrokerTaskMonthFinished> getBrokerMonthFinishPercentByTime(Long brokerId, Date startTime, Date endTime) {
        Example example = new Example(BrokerTaskMonthFinished.class);
        example.createCriteria()
                .andEqualTo("brokerId", brokerId)
                .andGreaterThanOrEqualTo("calTime", startTime)
                .andLessThan("calTime", endTime);
        return brokerTaskMonthFinishedMapper.selectByExample(example);
    }

    /**
     * 指定经销商某天的月完成度
     */
    @Deprecated
    public DealerTaskMonthFinished getDealerMonthFinishPercent(Long dealerId, Date time) {
        if (!this.isDealerSchedule(dealerId, time)) {
            return null;
        }
        Example example = new Example(DealerTaskMonthFinished.class);
        example.createCriteria()
                .andEqualTo("dealerId", dealerId)
                .andEqualTo("calTime", time);
        DealerTaskMonthFinished dealerTaskMonthFinished = null;
        try {
            dealerTaskMonthFinished = dealerTaskMonthFinishedMapper.selectOneByExample(example);
        } catch (Exception e) {
            return null;
        }
        if (dealerTaskMonthFinished == null) {
            PageHelper.startPage(1, 1, false).setOrderBy("id desc");
            example = new Example(DealerTaskMonthFinished.class);
            example.createCriteria()
                    .andEqualTo("dealerId", dealerId)
                    .andGreaterThanOrEqualTo("createTime", DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 0))
                    .andLessThan("createTime", DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 1));
            dealerTaskMonthFinished = dealerTaskMonthFinishedMapper.selectOneByExample(example);
            if (dealerTaskMonthFinished == null) {
                dealerTaskMonthFinished = new DealerTaskMonthFinished();
                dealerTaskMonthFinished.setDealerId(dealerId);
                dealerTaskMonthFinished.setValidBrokerNumber(0);
                dealerTaskMonthFinished.setExamineDayNumber(1);
            } else {
                dealerTaskMonthFinished.setCreateTime(null);
                dealerTaskMonthFinished.setUpdateTime(null);
                dealerTaskMonthFinished.setExamineDayNumber(dealerTaskMonthFinished.getExamineDayNumber() + 1);
            }
            dealerTaskMonthFinished.setCalTime(time);
            List<Long> brokerIds = taskService.getBrokerIdsByDealerId(dealerId, time);
            BigDecimal percent = BigDecimal.valueOf(0);
            List<Long> validBrokerIds = new ArrayList<>();

            for (int i = 0; i < brokerIds.size(); i++) {
                BrokerTaskMonthFinished brokerTaskMonthFinished = this.getBrokerMonthFinishPercent(brokerIds.get(i), dealerId, time);
                if (brokerTaskMonthFinished == null) {
                    continue;
                }
                //经纪人月完成度
                if (brokerTaskMonthFinished.getProgress().compareTo(percent) > 0) {
                    //取当天月任务完成度大约50%的车顾问
                    if (brokerTaskMonthFinished.getProgress().compareTo(BigDecimal.valueOf(0.5)) > 0) {
                        validBrokerIds.add(brokerTaskMonthFinished.getBrokerId());
                    }
                }
            }
            //有效车顾问数量
            dealerTaskMonthFinished.setValidBrokerNumber(validBrokerIds.size());
            //有效顾问的日任务完成度最高的N个之和
//            PageHelper.startPage(1, dealerTaskMonthFinished.getExamineDayNumber(), false);
            BigDecimal sumPercent = BigDecimal.valueOf(0);
            if (validBrokerIds.size() > 0) {
                sumPercent = brokerTaskFinishedMapper.getDayProgressSum(
                        DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 0),
                        DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 1),
                        validBrokerIds, dealerTaskMonthFinished.getExamineDayNumber());
            }
            dealerTaskMonthFinished.setHadPercent(sumPercent);
            try {
                Result result = dealerSeverCloud.dealerScheduleList(dealerId);
                JSONArray items = JSONArray.parseArray(result.getData().toString());
                for (int i = 0; i < items.size(); i++) {
                    JSONObject obj = items.getJSONObject(i);
                    long beginTime = obj.getLongValue("beginTime");
                    long endTime = obj.getLongValue("endTime");
                    if (time.getTime() >= beginTime && time.getTime() <= endTime) {
                        dealerTaskMonthFinished.setScheduleStartTime(new Date(beginTime));
                        dealerTaskMonthFinished.setScheduleEndTime(new Date(endTime));
                        dealerTaskMonthFinished.setExamineStartTime(new Date(obj.getLongValue("taskStartTime")));
                    }
                }
            } catch (Exception e) {
            }
            dealerTaskMonthFinished.setProgress(
                    dealerTaskMonthFinished.getHadPercent().divide(BigDecimal.valueOf(dealerTaskMonthFinished.getExamineDayNumber()), 2, BigDecimal.ROUND_HALF_UP));
        }
        return dealerTaskMonthFinished;
    }

    /**
     * 指定经销商某天的月完成度
     */
    public DealerTaskMonthFinished getNewDealerMonthFinishPercent(Long dealerId, Date time) {
        if (!this.isDealerSchedule(dealerId, time)) {
            return null;
        }
        Example example = new Example(DealerTaskMonthFinished.class);
        example.createCriteria()
                .andEqualTo("dealerId", dealerId)
                .andEqualTo("calTime", time);
        DealerTaskMonthFinished dealerTaskMonthFinished = null;
        try {
            dealerTaskMonthFinished = dealerTaskMonthFinishedMapper.selectOneByExample(example);
        } catch (Exception e) {
            return null;
        }
        if (dealerTaskMonthFinished == null) {
            PageHelper.startPage(1, 1, false).setOrderBy("id desc");
            example = new Example(DealerTaskMonthFinished.class);
            example.createCriteria()
                    .andEqualTo("dealerId", dealerId)
                    .andGreaterThanOrEqualTo("calTime", DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 0))
//                    .andLessThan("calTime", DateUtil.getDateStart(DateUtil.getLastDateOfSeason(time), DateStyle.FIELD_TYPE.DAY, 1));
                    .andLessThan("calTime", DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.DAY, 1));
            dealerTaskMonthFinished = dealerTaskMonthFinishedMapper.selectOneByExample(example);
            if (dealerTaskMonthFinished == null) {
                dealerTaskMonthFinished = new DealerTaskMonthFinished();
                dealerTaskMonthFinished.setDealerId(dealerId);
                dealerTaskMonthFinished.setCalTime(time);
                dealerTaskMonthFinished.setProgress(BigDecimal.valueOf(0));
                dealerTaskMonthFinished.setExamineDayNumber(1);
            } else {
                dealerTaskMonthFinished.setCalTime(time);
                dealerTaskMonthFinished.setCreateTime(null);
                dealerTaskMonthFinished.setUpdateTime(null);
                dealerTaskMonthFinished.setExamineDayNumber(dealerTaskMonthFinished.getExamineDayNumber() + 1);
            }
            List<Long> brokerIds = taskService.getBrokerIdsByDealerId(dealerId, time);
            int sumCount = 0;
            boolean queryWeek = false;
            boolean isWeek = false;
            boolean queryMonth = false;
            boolean isMonth = false;
            for (int i = 0; i < brokerIds.size(); i++) {
                BrokerTaskFinished brokerTaskFinished = this.getBrokerDayFinishPercent(brokerIds.get(i), dealerId, time);
                if (brokerTaskFinished == null) {
                    continue;
                }
                if (brokerTaskFinished.getProgress().compareTo(BigDecimal.valueOf(1)) == 0) {
                    sumCount++;
                }
                if (brokerTaskFinished.getWeekPercent().compareTo(BigDecimal.valueOf(0)) > 0) {
                    if (!queryWeek) {
                        queryWeek = true;
                        if (brokerTaskFinishedMapper.isWeek(
                                DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.WEEK, 0),
                                DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.WEEK, 1),
                                dealerId) == 0) {
                            isWeek = true;
                        }
                    }
                }
                if (brokerTaskFinished.getMonthPercent().compareTo(BigDecimal.valueOf(0)) > 0) {
                    if (!queryMonth) {
                        queryMonth = true;
                        if (brokerTaskFinishedMapper.isMonth(
                                DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 0),
                                DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 1),
                                dealerId) == 0) {
                            isMonth = true;
                        }
                    }
                }
            }
            dealerTaskMonthFinished.setHadPercent(BigDecimal.valueOf(0));
            BigDecimal progress = BigDecimal.valueOf(0);
            if (sumCount >= 3) {
                progress = BigDecimal.valueOf(1);
            } else if (sumCount == 2) {
                progress = BigDecimal.valueOf(0.8);
            } else if (sumCount == 1) {
                progress = BigDecimal.valueOf(0.5);
            }
            if (isWeek) {
                progress = progress.add(BigDecimal.valueOf(2));
                dealerTaskMonthFinished.setHadPercent(dealerTaskMonthFinished.getHadPercent().add(BigDecimal.valueOf(1)));
            }
            if (isMonth) {
                progress = progress.add(BigDecimal.valueOf(5));
                dealerTaskMonthFinished.setHadPercent(dealerTaskMonthFinished.getHadPercent().add(BigDecimal.valueOf(2)));
            }
            try {
                Result result = dealerSeverCloud.dealerScheduleList(dealerId);
                JSONArray items = JSONArray.parseArray(result.getData().toString());
                for (int i = 0; i < items.size(); i++) {
                    JSONObject obj = items.getJSONObject(i);
                    long beginTime = obj.getLongValue("beginTime");
                    long endTime = obj.getLongValue("endTime");
                    if (time.getTime() >= beginTime && time.getTime() <= endTime) {
                        dealerTaskMonthFinished.setScheduleStartTime(new Date(beginTime));
                        dealerTaskMonthFinished.setScheduleEndTime(new Date(endTime));
                        dealerTaskMonthFinished.setExamineStartTime(new Date(obj.getLongValue("taskStartTime")));
                    }
                }
            } catch (Exception e) {
            }
            dealerTaskMonthFinished.setProgress(dealerTaskMonthFinished.getProgress().add(progress));
        }
        return dealerTaskMonthFinished;
    }


    public void saveBrokerFinishd(Long dealerId, Date time) {
        List<Long> list = taskService.getBrokerIdsByDealerId(dealerId, time);
        for (int i = 0; i < list.size(); i++) {
            Long brokerId = list.get(i);
            Broker broker = brokerMapper.selectByPrimaryKey(brokerId);
            BrokerTaskFinished brokerTaskFinished = this.getBrokerDayFinishPercent(broker.getBrokerId(), broker.getDealerId(), time);
            if (brokerTaskFinished == null) {
                continue;
            }
            brokerTaskFinishedMapper.insertSelective(brokerTaskFinished);
            BrokerTaskMonthFinished brokerTaskMonthFinished = this.getBrokerMonthFinishPercent(brokerTaskFinished, broker.getBrokerId(), broker.getDealerId(), time);
            brokerTaskMonthFinishedMapper.insertSelective(brokerTaskMonthFinished);
        }
    }


    public void saveDealerFinishd(Long dealerId, Date time) {
        DealerTaskMonthFinished dealerTaskMonthFinished = this.getNewDealerMonthFinishPercent(dealerId, time);
        if (dealerTaskMonthFinished != null) {
            dealerTaskMonthFinishedMapper.insertSelective(dealerTaskMonthFinished);
        }
    }


    /**
     * 保存经纪人完成度
     */
    public void saveTaskFinished(Date time, int page) {
        PageHelper.startPage(page, 300, false).setOrderBy("broker_id asc");
        List<Broker> list = taskService.getBrokerIds(time);
        for (int i = 0; i < list.size(); i++) {
            Broker broker = list.get(i);
            BrokerTaskFinished brokerTaskFinished = this.getBrokerDayFinishPercent(broker.getBrokerId(), broker.getDealerId(), time);
            if (brokerTaskFinished == null) {
                continue;
            }
            brokerTaskFinishedMapper.insertSelective(brokerTaskFinished);
            BrokerTaskMonthFinished brokerTaskMonthFinished = this.getBrokerMonthFinishPercent(brokerTaskFinished, broker.getBrokerId(), broker.getDealerId(), time);
            brokerTaskMonthFinishedMapper.insertSelective(brokerTaskMonthFinished);
        }
    }

    /**
     * 更新经纪人完成度
     */
    @Async
    public void updateTaskFinished(Date time, Long broker_id) {
        Broker broker = brokerMapper.selectByPrimaryKey(broker_id);
        Example example = new Example(BrokerTaskFinished.class);
        example.createCriteria()
                .andEqualTo("brokerId", broker_id)
                .andEqualTo("calTime", time);
        brokerTaskFinishedMapper.deleteByExample(example);
        BrokerTaskFinished brokerTaskFinished = this.getBrokerDayFinishPercent(broker.getBrokerId(), broker.getDealerId(), time);
        if (brokerTaskFinished == null) {
            return;
        }
        brokerTaskFinishedMapper.insertSelective(brokerTaskFinished);
        example = new Example(DealerTaskMonthFinished.class);
        example.createCriteria()
                .andEqualTo("dealerId", broker.getDealerId())
                .andEqualTo("calTime", time);
        dealerTaskMonthFinishedMapper.deleteByExample(example);
        DealerTaskMonthFinished dealerTaskMonthFinished = this.getNewDealerMonthFinishPercent(broker.getDealerId(), time);
        if (dealerTaskMonthFinished == null) {
        }
        dealerTaskMonthFinishedMapper.insertSelective(dealerTaskMonthFinished);
    }

    /**
     * 查询经销商是否在排期内
     */
    public boolean isDealerSchedule(Long dealerId, Date time) {
        PageHelper.startPage(1, 1, false);
        List<Long> brokers = taskService.getBrokerIdsByDealerId(dealerId, time);
        if (brokers == null || brokers.size() == 0) {
            return false;
        }
        return isBrokerSchedule(brokers.get(0), dealerId, time);
    }

    /**
     * 保存经销商完成度
     */
    public void saveDealerTaskFinished(Date time, int page) {
        PageHelper.startPage(page, 300, false).setOrderBy("dealer_id asc");
        List<Long> list = taskService.getDealerIds();
        for (int i = 0; i < list.size(); i++) {
            Long dealerId = list.get(i);
            DealerTaskMonthFinished dealerTaskMonthFinished = this.getNewDealerMonthFinishPercent(dealerId, time);
            if (dealerTaskMonthFinished == null) {
                continue;
            }
            dealerTaskMonthFinishedMapper.insertSelective(dealerTaskMonthFinished);
        }
    }


//    static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1000);

//    /**
//     * 临时补充所有在排期内的经纪人的所有任务分
//     */
//    public void tmpAddTaskFinish(String time, int week, int page) {
//        PageHelper.startPage(page, 300, false).setOrderBy("broker_id asc");
//        List<Broker> list = taskService.getBrokerIds();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = null;
//        try {
//            date = simpleDateFormat.parse(time);
//        } catch (Exception e) {
//
//        }
//
//
//        for (int i = 0; i < list.size(); i++) {
//            Broker broker = list.get(i);
//            if (this.isBrokerSchedule(broker.getBrokerId(), broker.getDealerId(), date) == true) {
//                fixedThreadPool.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        taskMapper.tmpAdd(broker.getBrokerId(), 111, time + " 08:00:00", time + " 08:00:00");
//                        taskMapper.tmpAdd(broker.getBrokerId(), 123, time + " 08:00:00", time + " 08:00:00");
//                        taskMapper.tmpAdd(broker.getBrokerId(), 125, time + " 08:00:00", time + " 08:00:00");
//                        taskMapper.tmpAdd(broker.getBrokerId(), 125, time + " 08:00:00", time + " 08:00:00");
//                        taskMapper.tmpAdd(broker.getBrokerId(), 125, time + " 08:00:00", time + " 08:00:00");
//                        taskMapper.tmpAdd(broker.getBrokerId(), 125, time + " 08:00:00", time + " 08:00:00");
//                        taskMapper.tmpAdd(broker.getBrokerId(), 125, time + " 08:00:00", time + " 08:00:00");
//                        if (week == 1) {
//                            for (int k = 0; k < 50; k++) {
//                                taskMapper.tmpAdd(broker.getBrokerId(), 129, time + " 08:00:00", time + " 08:00:00");
//                            }
//                        }
//                    }
//                });
//            }
//        }
//    }


    public void tmpAddTaskDayFinish(String time, int week, int page) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (Exception e) {

        }
        PageHelper.startPage(page, 300, false).setOrderBy("broker_id asc");
        List<Broker> list = taskService.getBrokerIds(date);
        for (int i = 0; i < list.size(); i++) {
            Broker broker = list.get(i);
            if (!this.isBrokerSchedule(broker.getBrokerId(), broker.getDealerId(), date)) {
                continue;
            }
            BrokerTaskFinished brokerTaskFinished = new BrokerTaskFinished();
            brokerTaskFinished.setBrokerId(broker.getBrokerId());
            brokerTaskFinished.setDealerId(broker.getDealerId());
            brokerTaskFinished.setDayLimit(BigDecimal.valueOf(100));
            brokerTaskFinished.setProgress(BigDecimal.valueOf(1));
            brokerTaskFinished.setDayPercent(BigDecimal.valueOf(100));
            brokerTaskFinished.setCalTime(date);
            if (week != 0) {
                brokerTaskFinished.setWeekPercent(BigDecimal.valueOf(100));
            }
            brokerTaskFinishedMapper.insertSelective(brokerTaskFinished);
        }
    }


    public void tmpAddTaskMonthFinish(String time, int page) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (Exception e) {
        }
        PageHelper.startPage(page, 300, false).setOrderBy("broker_id asc");
        List<Broker> list = taskService.getBrokerIds(date);
        for (int i = 0; i < list.size(); i++) {
            Broker broker = list.get(i);
            BrokerTaskMonthFinished brokerTaskMonthFinished = this.getBrokerMonthFinishPercent(null, broker.getBrokerId(), broker.getDealerId(), date);
            if (brokerTaskMonthFinished != null) {
                brokerTaskMonthFinishedMapper.insertSelective(brokerTaskMonthFinished);
            }
        }
    }


    public void tmpAddDealerTaskMonthFinish(String time, int page) {
        PageHelper.startPage(page, 300, false).setOrderBy("dealer_id asc");
        List<Long> list = taskService.getDealerIds();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (Exception e) {
        }
        for (int i = 0; i < list.size(); i++) {
            Long dealerId = list.get(i);
            DealerTaskMonthFinished dealerTaskMonthFinished = this.getNewDealerMonthFinishPercent(dealerId, date);
            if (dealerTaskMonthFinished != null) {
                dealerTaskMonthFinishedMapper.insertSelective(dealerTaskMonthFinished);
            }
        }
    }


}
