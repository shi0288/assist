package com.xyauto.assist.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mcp.fastcloud.annotation.Log;
import com.mcp.fastcloud.util.exception.base.ErrorException;
import com.xyauto.assist.entity.*;
import com.xyauto.assist.mapper.broker.ExecutionExtMapper;
import com.xyauto.assist.mapper.broker.ExecutionMapper;
import com.xyauto.assist.mapper.broker.TaskMapper;
import com.xyauto.assist.service.BrokerTaskFinishedService;
import com.xyauto.assist.util.constant.CommonCons;
import com.xyauto.assist.util.constant.ErrorCons;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


/**
 * Created by shiqm on 2018-01-19.
 */
public abstract class PluginAbs {

    @Autowired
    protected ExecutionMapper executionMapper;

    @Autowired
    private ExecutionExtMapper executionExtMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private BrokerTaskFinishedService brokerTaskFinishedService;

    @Log
    protected Logger logger;


    public void valid(Task task) {
    }

    public abstract void business(Task task, Execution execution);


    public Execution create(Task task, Map params) {
        Execution execution = new Execution();
        String uid = (String) params.get("uid");
        execution.setUid(uid);
        //吃米线任务不再排期的不做任务
        if (task.getType() == 0 && !brokerTaskFinishedService.isBrokerSchedule(Long.valueOf(uid), new Date())) {
            throw new ErrorException(ErrorCons.EXECUTION_NOT_IN_SHOUDER);
        }
        //组织任务条件
        execution.setTaskId(task.getId());
        if (task.getIsTarget().compareTo(CommonCons.TargetFlag.USER) == 0) {
            //用户
        } else if (task.getIsTarget().compareTo(CommonCons.TargetFlag.USER_TARGET) == 0) {
            //用户&目标
            execution.setTargetId(params.get("target_id").toString());
        } else if (task.getIsTarget().compareTo(CommonCons.TargetFlag.TARGET) == 0) {
            //目标
            execution.setTargetId(params.get("target_id").toString());
        }
        //判断是否允许执行
        business(task, execution);
        //加入积分信息
        addPointTag(task, execution, params);
        //加入任务分
        addPercent(task, execution);
        //组织留存数据
        if (execution.getUid() == null) {
            execution.setUid(uid);
        }
        if (execution.getTargetId() == null) {
            execution.setTargetId(params.get("target_id") == null ? null : params.get("target_id").toString());
        }
        return execution;
    }


    protected void interval(Task task, Execution execution, int timeUnit) {
        if (execution.getTag() == null) {
            execution.setTag(new JSONObject());
        }
        //判断次数
        Date currentTime = new Date();
        Date startTime = DateUtil.getDateStart(currentTime, timeUnit, 0);
        Date endTime = DateUtil.getDateStart(currentTime, timeUnit, 1);
        JSONObject condition = JSONObject.parseObject(task.getApplyCondition());
        if (condition.containsKey("pre_day")) {
            int pre_day = condition.getInteger("pre_day");
            Date curDay = new Date();
            int day = DateUtil.getDay(curDay);
            if (day <= pre_day) {
                endTime = DateUtil.getDateStart(startTime, DateStyle.FIELD_TYPE.DAY, pre_day);
                startTime = DateUtil.getDateStart(DateUtil.addMonth(endTime, -1), DateStyle.FIELD_TYPE.DAY, 0);
            }else{
                endTime = DateUtil.getDateStart(endTime, DateStyle.FIELD_TYPE.DAY, pre_day);
                startTime = DateUtil.getDateStart(DateUtil.addMonth(endTime, -1), DateStyle.FIELD_TYPE.DAY, 0);
            }
        }
        int limitTimes = condition.getIntValue("limit_times");
        //当期次数
        Execution userQuery = new Execution();
        userQuery.setTaskId(execution.getTaskId());
        userQuery.setUid(execution.getUid());
        int userCount = executionMapper.queryCount(userQuery, startTime, endTime);
        if (limitTimes > 0) {
            if (userCount >= limitTimes) {
                throw new ErrorException(ErrorCons.EXECUTION_MAX_TIME);
            }
        }
        //记录任务分子次数
        if (task.getTimes() > 0) {
            //todo 暂不做去重处理
            ExecutionExt executionExt = new ExecutionExt();
            executionExt.setUid(execution.getUid());
            executionExt.setTaskId(execution.getTaskId());
            int count = executionMapper.queryExtCount(executionExt, startTime, endTime);
            //如果当期记录数大于或者等于当前次数，那就已经加过了
            if (count >= task.getTimes()) {
                throw new ErrorException(ErrorCons.EXECUTION_MAX_TIME);
            }
            executionExt.setTargetId(execution.getTargetId());
            executionExtMapper.insertSelective(executionExt);
            if ((count + 1) == task.getTimes()) {
                //达到次数  可以加积分
            } else {
                throw new ErrorException(ErrorCons.EXECUTION_COULD_NOT);
            }
        }
        if (task.getIsTarget().compareTo(CommonCons.TargetFlag.USER) == 0) {
            //用户前辈已经校验过
        } else if (task.getIsTarget().compareTo(CommonCons.TargetFlag.USER_TARGET) == 0) {
            //用户&目标
            int count = executionMapper.queryCount(execution, startTime, endTime);
            if (count > 0) {
                throw new ErrorException(ErrorCons.EXECUTION_MAX_TIME);
            }
        } else if (task.getIsTarget().compareTo(CommonCons.TargetFlag.TARGET) == 0) {
            //目标
            execution.setUid(null);
            int count = executionMapper.queryCount(execution, startTime, endTime);
            if (count > 0) {
                throw new ErrorException(ErrorCons.EXECUTION_MAX_TIME);
            }
        }
        JSONObject tag = execution.getTag();
        tag.put("max", limitTimes);
        tag.put("current", userCount + 1);
        if (execution.getUid() != null) {
            addPreTag(execution, timeUnit);
        }
    }

    protected void addPreTag(Execution execution, int timeUnit) {
        Date currentTime = new Date();
        Date startTime = DateUtil.getDateStart(currentTime, timeUnit, -1);
        Date endTime = DateUtil.getDateStart(currentTime, timeUnit, 0);
        PageHelper.startPage(1, 1, false).setOrderBy("id desc");
        Example example = new Example(Execution.class);
        example.createCriteria()
                .andEqualTo("taskId", execution.getTaskId())
                .andEqualTo("uid", execution.getUid())
                .andGreaterThanOrEqualTo("createTime", startTime)
                .andLessThan("createTime", endTime);
        Execution preExecution = executionMapper.selectOneByExample(example);
        JSONObject tag = execution.getTag();
        int unitTimes = 0;
        try {
            unitTimes = preExecution.getTag().getIntValue("unit_times");
        } catch (Exception e) {
        }
        tag.put("unit_times", unitTimes + 1);
    }

    private void addPercent(Task task, Execution execution) {
        if (task.getFinishPercent().compareTo(BigDecimal.valueOf(0)) == 0) {
            execution.setPercent(BigDecimal.valueOf(0));
            return;
        }
        execution.setPercent(task.getFinishPercent());
    }


    protected void addPointTag(Task task, Execution execution, Map params) {
        if (execution.getTag() == null) {
            execution.setTag(new JSONObject());
        }
        if (task.getType() == 0) {
            return;
        }
        JSONObject condition = JSONObject.parseObject(task.getApplyCondition());
        if (condition.containsKey("point")) {
            //固定积分直接拿值
            BigDecimal point = condition.getBigDecimal("point");
            execution.getTag().put("point", point);
        } else if (condition.containsKey("points")) {
            //数组指定积分获取数组内值
            JSONArray items = condition.getJSONArray("points");
            BigDecimal point = items.getBigDecimal(execution.getTag().getIntValue("current") - 1);
            execution.getTag().put("point", point);
        } else if (condition.containsKey("appoint")) {
            //标识置顶积分通过标示拿积分
            String sign = params.get("sign").toString();
            if (!StringUtils.isEmpty(sign)) {
                JSONArray items = condition.getJSONArray("appoint");
                for (int i = 0; i < items.size(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    if (item.containsKey(sign)) {
                        BigDecimal point = item.getBigDecimal("point");
                        execution.getTag().put("point", point);
                    }
                }
            }
        } else if (condition.containsKey("appoints")) {
            //数组指定积分获取数组内值
            int index = execution.getTag().getIntValue("current");
            JSONArray items = condition.getJSONArray("appoints");
            BigDecimal point = null;
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                int start = item.getIntValue("start");
                int end = item.getIntValue("end");
                if ((index >= start && index <= end) || end == 0) {
                    point = item.getBigDecimal("point");
                    break;
                }

            }
            execution.getTag().put("point", point);
        } else if (condition.containsKey("limit_appoints")) {
            //数组指定积分获取数组内值
            int index = execution.getTag().getIntValue("unit_times");
            JSONArray items = condition.getJSONArray("limit_appoints");
            BigDecimal point = null;
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                int start = item.getIntValue("start");
                int end = item.getIntValue("end");
                if ((index >= start && index <= end) || end == 0) {
                    point = item.getBigDecimal("point");
                    break;
                }

            }
            execution.getTag().put("point", point);
        }
    }

}
