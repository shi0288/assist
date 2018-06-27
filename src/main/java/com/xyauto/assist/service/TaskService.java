package com.xyauto.assist.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mcp.fastcloud.util.Result;
import com.mcp.fastcloud.util.SpringIocUtil;
import com.mcp.fastcloud.util.exception.base.ErrorException;
import com.xyauto.assist.cloud.BrokerServerCloud;
import com.xyauto.assist.cloud.DealerSeverCloud;
import com.xyauto.assist.entity.*;
import com.xyauto.assist.mapper.broker.ExecutionMapper;
import com.xyauto.assist.mapper.broker.TaskExtMapper;
import com.xyauto.assist.mapper.broker.TaskMapper;
import com.xyauto.assist.util.DateStyle;
import com.xyauto.assist.util.DateUtil;
import com.xyauto.assist.util.FinishedResult;
import com.xyauto.assist.util.PluginAbs;
import com.xyauto.assist.util.constant.CommonCons;
import com.xyauto.assist.util.constant.ErrorCons;
import com.xyauto.assist.util.constant.TaskPluginCons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by shiqm on 2018-01-18.
 */

@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskExtMapper taskExtMapper;

    @Autowired
    private TaskExtService taskExtService;

    @Autowired
    private BrokerTaskFinishedService brokerTaskFinishedService;

    @Autowired
    private DealerSeverCloud dealerSeverCloud;

    @Autowired
    private BrokerServerCloud brokerServerCloud;


    @Autowired
    private ExecutionMapper executionMapper;

    public boolean isRepeat(String apply) {
        Task query = new Task();
        query.setApply(apply);
        if (taskMapper.selectCount(query) == 0) {
            return true;
        }
        return false;
    }


    @Transactional("brokerTransactionManager")
    public void add(Task task, TaskExt taskExt) {
        taskMapper.insertSelective(task);
        taskExt.setTaskId(task.getId());
        taskExtMapper.insertSelective(taskExt);
    }

    public void checkin(String plugin, Task task) {
        PluginAbs pluginAbs = (PluginAbs) SpringIocUtil.getBean(plugin);
        pluginAbs.valid(task);
    }

    public List<Task> checkout(String apply, HttpServletRequest request) {
        //判断uid
        if (request.getParameter("uid") == null) {
            throw new ErrorException(ErrorCons.TASK_NO_USER);
        }
        List<Task> list = new ArrayList();
        //判断存在
        Task task = new Task();
        task.setApply(apply);
        task = taskMapper.selectOne(task);
        if (checkToRun(task, request)) {
            list.add(task);
        }
        List<Task> otherTaskList = taskMapper.otherList(apply);
        for (int i = 0; i < otherTaskList.size(); i++) {
            task = otherTaskList.get(i);
            if (checkToRun(task, request)) {
                list.add(task);
            }
        }
        return list;
    }


    public boolean checkToRun(Task task, HttpServletRequest request) {
        if (task == null) {
            throw new ErrorException(ErrorCons.TASK_NO_APPLY);
        }
        //用户目标一次性  目标一次性 ,判断target_id是否存在
        if (task.getIsTarget().compareTo(CommonCons.TargetFlag.USER) == 1 && StringUtils.isEmpty(request.getParameter("target_id"))) {
            return false;
//            throw new ErrorException(ErrorCons.TASK_NO_TARGET_ID);
        }
        //判断任务
        if (task.getStatus().compareTo(CommonCons.StatusFlag.CLOSE) == 0) {
            return false;
//            throw new ErrorException(ErrorCons.TASK_CLOSE);
        }
        //判断时间
        if (task.getStartAt() > 0) {
            Integer curTimestamp = DateUtil.getTimestamp(new Date());
            if (task.getStartAt().intValue() >= curTimestamp.intValue()) {
                return false;
//                throw new ErrorException(ErrorCons.TASK_TIME_START);
            }
        }
        if (task.getEndAt() > 0) {
            Integer curTimestamp = DateUtil.getTimestamp(new Date());
            if (task.getEndAt().intValue() <= curTimestamp.intValue()) {
                return false;
//                throw new ErrorException(ErrorCons.TASK_TIME_END);
            }
        }
        return true;
    }


    public Page<Map> list(int page, int limit) {
        PageHelper.startPage(page, limit).setOrderBy("create_time desc");
        Page pageInfo = taskMapper.list();
        return pageInfo;
    }

    public int update(Long id, Short status) {
        Task task = new Task();
        task.setId(id);
        task.setStatus(status);
        return taskMapper.updateByPrimaryKeySelective(task);
    }


    public void revokeExecution(String apply, String broker_id, String target_id, String desc) {
        try {
            Task task = new Task();
            task.setApply(apply);
            task = taskMapper.selectOne(task);
            if (task == null) {
                throw new ErrorException(ErrorCons.TASK_NO_APPLY);
            }
            Long task_id = task.getId();
            Execution execution = new Execution();
            execution.setTaskId(task_id);
            execution.setUid(broker_id);
            execution.setTargetId(target_id);
            execution.setIsDeleted(Short.valueOf("0"));
            execution = executionMapper.selectOne(execution);
            if (execution == null) {
                throw new ErrorException(ErrorCons.TASK_NO_EXECUTION);
            }
            JSONObject tagObj = execution.getTag();
            BigDecimal point = tagObj.getBigDecimal("point");
            Result result = brokerServerCloud.addIntegral(Long.valueOf(broker_id), desc, -point.longValue());
            if (result.getCode() == 10000) {
                Execution temp = new Execution();
                temp.setId(execution.getId());
                temp.setIsDeleted(Short.valueOf("1"));
                executionMapper.updateByPrimaryKeySelective(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(ErrorCons.ERROR);
        }

    }


    public JSONArray getBrokerTaskInfoByDealerId(Long dealerId, Date currentTime) {
        List<Broker> list = taskMapper.getBrokersByDealerId(dealerId, DateUtil.getDateStart(currentTime, DateStyle.FIELD_TYPE.DAY, 0));
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject temp = new JSONObject();
            Broker broker = list.get(i);
            temp.put("name", broker.getName());
            temp.put("broker_id", broker.getBrokerId());
            BrokerTaskFinished brokerTaskFinished = brokerTaskFinishedService.getBrokerDayFinishPercent(broker.getBrokerId(), currentTime);
            if (brokerTaskFinished == null) {
                continue;
            }
            BigDecimal progress = brokerTaskFinished.getProgress();
            if (progress.compareTo(BigDecimal.valueOf(0)) == 0) {
                continue;
            }
            temp.put("progress", progress);
            jsonArray.add(temp);
        }
        return jsonArray;
    }

    public JSONArray getBrokerTaskMonthInfoByDealerId(Long dealerId, Date currentTime) {
        List<Broker> list = taskMapper.getBrokersByDealerId(dealerId, DateUtil.getDateStart(currentTime, DateStyle.FIELD_TYPE.DAY, 0));
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject temp = new JSONObject();
            Broker broker = list.get(i);
            temp.put("name", broker.getName());
            temp.put("broker_id", broker.getBrokerId());
            temp.put("info", brokerTaskFinishedService.getBrokerMonthFinishPercent(broker.getBrokerId(), dealerId, currentTime));
            jsonArray.add(temp);
        }
        return jsonArray;
    }


    /**
     * 获取任务经纪人日任务完成度
     *
     * @param uid
     * @param currentTime
     * @return
     */
    public FinishedResult getBrokerFinishPercent(String uid, Date currentTime) {
        FinishedResult finishedResult = new FinishedResult();
        Task task = new Task();
        task.setStatus(CommonCons.StatusFlag.OPEN);
        task.setType(Short.valueOf("0"));
        List<Task> list = taskMapper.select(task);
        BigDecimal baseNumber = new BigDecimal(0);
        BigDecimal countNumber = new BigDecimal(0);
        Execution execution = new Execution();
        execution.setUid(uid);
        for (int i = 0; i < list.size(); i++) {
            Task temp = list.get(i);
            execution.setTaskId(temp.getId());
            int count = taskExtService.getInterValCount(execution, DateStyle.FIELD_TYPE.DAY, currentTime);
            //今日所得分
            BigDecimal myPercent = temp.getFinishPercent().multiply(BigDecimal.valueOf(count));
            //加入分子日得分
            baseNumber = baseNumber.add(myPercent);
            //分类并记录
            if (temp.getStartAt() != 0 || temp.getEndAt() != 0) {
                finishedResult.add(FinishedResult.TEMPORARY_PERCENT, myPercent);
            } else {
                finishedResult.add(temp.getPlugin(), myPercent);
            }
            //如果是日任务，并且不是临时任务
            if (temp.getPlugin().equals(TaskPluginCons.INTERVAL_DAY) && (temp.getStartAt() == 0 && temp.getEndAt() == 0)) {
                Integer limit_times = JSON.parseObject(temp.getApplyCondition()).getInteger("limit_times");
                //上限分值
                BigDecimal countPercent = temp.getFinishPercent().multiply(BigDecimal.valueOf(limit_times));
                //算入分母总得分
                countNumber = countNumber.add(countPercent);
                //记录日任务上线
                finishedResult.add(FinishedResult.DAY_LIMIT, countPercent);
            } else {
                //算入分母总得分
                countNumber = countNumber.add(myPercent);
            }
        }
        if (countNumber.compareTo(BigDecimal.valueOf(0)) == 0) {
            finishedResult.setProgress(BigDecimal.valueOf(0));
        } else {
            finishedResult.setProgress(baseNumber.divide(countNumber, 2, BigDecimal.ROUND_HALF_UP));
        }
        return finishedResult;

    }


    /**
     * 获取经纪人当季排期天数
     *
     * @param brokerId
     * @return
     */
    public long getScheduleDays(long brokerId) {
        Broker broker = this.getBrokerById(brokerId);
        if (broker == null) {
            return 0;
        }
        Result result = dealerSeverCloud.dealerScheduleList(broker.getDealerId());
        JSONArray items = JSONArray.parseArray(result.getData().toString());
        items.sort((Object o1, Object o2) -> {
            JSONObject json1 = (JSONObject) o1;
            JSONObject json2 = (JSONObject) o2;
            return json1.getLong("beginTime").compareTo(json2.getLong("beginTime"));
        });
        long interval = 0;
        long sTime = DateUtil.getFirstDateOfSeason(new Date()).getTime();
        long eTime = DateUtil.getLastDateOfMonth(new Date()).getTime();
        for (int i = 0; i < items.size(); i++) {
            JSONObject obj = items.getJSONObject(i);
            long beginTime = obj.getLongValue("beginTime");
            long endTime = obj.getLongValue("endTime");
            if (sTime > endTime) {
                continue;
            }
            if (eTime < beginTime) {
                break;
            }
            if (sTime <= beginTime && eTime <= endTime) {
                interval += (eTime - beginTime);
                break;
            }
            if (sTime <= beginTime && eTime > endTime) {
                interval += (endTime - beginTime);
            }
            if (sTime > beginTime && eTime <= endTime) {
                interval += (eTime - sTime);
                break;
            }
            if (sTime > beginTime && eTime > endTime) {
                interval += (endTime - sTime);
            }
        }
        long days = interval / 1000 / 60 / 60 / 24;
        return days;
    }


    /**
     * 获取经纪人当季排期天数
     *
     * @return
     */
    public long getScheduleDaysByDealerId(long dealerId) {
        Result result = dealerSeverCloud.dealerScheduleList(dealerId);
        JSONArray items = JSONArray.parseArray(result.getData().toString());
        items.sort((Object o1, Object o2) -> {
            JSONObject json1 = (JSONObject) o1;
            JSONObject json2 = (JSONObject) o2;
            return json1.getLong("beginTime").compareTo(json2.getLong("beginTime"));
        });
        long interval = 0;
        long sTime = DateUtil.getFirstDateOfSeason(new Date()).getTime();
        long eTime = DateUtil.getLastDateOfMonth(new Date()).getTime();
        for (int i = 0; i < items.size(); i++) {
            JSONObject obj = items.getJSONObject(i);
            long beginTime = obj.getLongValue("beginTime");
            long endTime = obj.getLongValue("endTime");
            if (sTime > endTime) {
                continue;
            }
            if (eTime < beginTime) {
                break;
            }
            if (sTime <= beginTime && eTime <= endTime) {
                interval += (eTime - beginTime);
                break;
            }
            if (sTime <= beginTime && eTime > endTime) {
                interval += (endTime - beginTime);
            }
            if (sTime > beginTime && eTime <= endTime) {
                interval += (eTime - sTime);
                break;
            }
            if (sTime > beginTime && eTime > endTime) {
                interval += (endTime - sTime);
            }
        }
        long days = interval / 1000 / 60 / 60 / 24;
        return days;
    }


    public List<Broker> getBrokerIds(Date date) {
        return taskMapper.getBrokerIds(DateUtil.getDateStart(date, DateStyle.FIELD_TYPE.DAY, 0));
    }

    public List<Long> getDealerIds() {
        return taskMapper.getDealerIds();
    }

    public List<Long> getBrokerIdsByDealerId(Long dealerId, Date date) {
        return taskMapper.getBrokerIdsByDealerId(dealerId, DateUtil.getDateStart(date, DateStyle.FIELD_TYPE.DAY, 0));
    }

    public Broker getBrokerById(Long brokerId) {
        return taskMapper.getBrokerById(brokerId);
    }


    public int getBrokerIdsCount(Date date) {
        return taskMapper.getBrokerIdsCount(DateUtil.getDateStart(date, DateStyle.FIELD_TYPE.DAY, 0));
    }

    public int getDealerIdsCount() {
        return taskMapper.getDealerIdsCount();
    }


}
