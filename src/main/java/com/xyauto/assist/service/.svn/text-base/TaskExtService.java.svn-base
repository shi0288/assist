package com.xyauto.assist.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyauto.assist.entity.Execution;
import com.xyauto.assist.entity.TaskExt;
import com.xyauto.assist.mapper.broker.ExecutionMapper;
import com.xyauto.assist.mapper.broker.TaskExtMapper;
import com.xyauto.assist.util.DateStyle;
import com.xyauto.assist.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shiqm on 2018-02-24.
 */
@Service
public class TaskExtService {

    @Autowired
    private TaskExtMapper taskExtMapper;

    @Autowired
    private ExecutionMapper executionMapper;


    public List<TaskExt> allForPoint() {
        return taskExtMapper.allForPoint();
    }


    public TaskExt get(Long taskId) {
        TaskExt taskExt = new TaskExt();
        taskExt.setTaskId(taskId);
        return taskExtMapper.selectOne(taskExt);
    }

    public JSONArray list(String uid, Date time) {
        return this.list(uid, time, false);
    }

    public JSONArray list(String uid, Date time, boolean isMonth) {
        List onceList = new ArrayList();
        List dayList = new ArrayList();
        List weekList = new ArrayList();
        List monthList = new ArrayList();
        List<TaskExt> list = taskExtMapper.all();
        for (int i = 0; i < list.size(); i++) {
            TaskExt taskExt = list.get(i);
            //如果设置了开启时间，且时间大于等于当期当前时间
            if (taskExt.getTask().getStartAt().compareTo(0) != 0 && taskExt.getTask().getStartAt().compareTo((int) (time.getTime() / 1000)) > -1) {
                continue;
            }
            //如果设置了结束时间，且时间小于当期当前时间
            if (taskExt.getTask().getEndAt().compareTo(0) != 0 && taskExt.getTask().getEndAt().compareTo((int) (time.getTime() / 1000)) == -1) {
                continue;
            }
            Execution execution = new Execution();
            execution.setTaskId(taskExt.getTaskId());
            execution.setUid(uid);
            switch (taskExt.getTask().getPlugin()) {
                case "disposablePlugin":
                    taskExt.setLimitTimes(1);
                    taskExt.setFinishedTimes(getInterValCount(execution, time));
                    if (taskExt.getFinishedTimes() >= taskExt.getLimitTimes()) {
                        taskExt.setLinkDes("已完成");
                    }
                    onceList.add(taskExt);
                    break;
                case "intervalDayPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    taskExt.setFinishedTimes(getInterValCount(execution, DateStyle.FIELD_TYPE.DAY, time));
                    if (taskExt.getFinishedTimes() >= taskExt.getLimitTimes()) {
                        taskExt.setLinkDes("已完成");
                    }
                    dayList.add(taskExt);
                    break;
                case "intervalWeekPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    taskExt.setFinishedTimes(getInterValCount(execution, DateStyle.FIELD_TYPE.WEEK, time));
                    if (taskExt.getFinishedTimes() >= taskExt.getLimitTimes()) {
                        taskExt.setLinkDes("已完成");
                    }
                    weekList.add(taskExt);
                    break;
                case "intervalMonthPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    taskExt.setFinishedTimes(getInterValCount(execution, DateStyle.FIELD_TYPE.MONTH, time));
                    if (taskExt.getFinishedTimes() >= taskExt.getLimitTimes()) {
                        taskExt.setLinkDes("已完成");
                    }
                    monthList.add(taskExt);
                    break;
            }
        }
        JSONArray jsonArray = new JSONArray();

        if (onceList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "单次任务");
            jsonObject.put("name", "单次任务");
            jsonObject.put("deadline", "");
            jsonObject.put("list", onceList);
            jsonArray.add(jsonObject);
        }
        if (dayList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本日任务");
            jsonObject.put("name", "本日任务");
            jsonObject.put("deadline", DateUtil.DateToString(time, "MM月dd日"));
            jsonObject.put("list", dayList);
            jsonArray.add(jsonObject);
        }
        if (weekList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本周任务 " + DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.WEEK, 1, "(截止至MM月dd日)"));
            jsonObject.put("name", "本周任务");
            if (isMonth) {
                Date weekEnd = DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.WEEK, 1);
                Date monthEnd = DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 1);
                if (monthEnd.getTime() <= weekEnd.getTime()) {
                    jsonObject.put("deadline",
                            DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getWeek(time).getNumber() + 1), "MM月dd日-") +
                                    DateUtil.DateToString(DateUtil.addDay(monthEnd, -1), "MM月dd日"));
                    jsonObject.put("title", "本周任务 " + DateUtil.DateToString(DateUtil.addDay(monthEnd, -1), "(截止至MM月dd日)"));
                } else {
                    jsonObject.put("deadline",
                            DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getWeek(time).getNumber() + 1), "MM月dd日-") +
                                    DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.WEEK, 1, "MM月dd日"));
                }
            } else {
                jsonObject.put("deadline",
                        DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getWeek(time).getNumber() + 1), "MM月dd日-") +
                                DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.WEEK, 1, "MM月dd日"));
            }
            jsonObject.put("list", weekList);
            jsonArray.add(jsonObject);
        }
        if (monthList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本月任务 " + DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.MONTH, 1, "(截止至MM月dd日)"));
            jsonObject.put("name", "本月任务");
            jsonObject.put("deadline",
                    DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getDay(time) + 1), "MM月dd日-") +
                            DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.MONTH, 1, "MM月dd日"));
            jsonObject.put("list", monthList);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    public JSONArray listFinishd(String uid, Date time, boolean isMonth) {
        List onceList = new ArrayList();
        List dayList = new ArrayList();
        List weekList = new ArrayList();
        List monthList = new ArrayList();
        List<TaskExt> list = taskExtMapper.all();
        for (int i = 0; i < list.size(); i++) {
            TaskExt taskExt = list.get(i);
            //如果设置了开启时间，且时间大于等于当期当前时间
            if (taskExt.getTask().getStartAt().compareTo(0) != 0 && taskExt.getTask().getStartAt().compareTo((int) (time.getTime() / 1000)) > -1) {
                continue;
            }
            //如果设置了结束时间，且时间小于当期当前时间
            if (taskExt.getTask().getEndAt().compareTo(0) != 0 && taskExt.getTask().getEndAt().compareTo((int) (time.getTime() / 1000)) == -1) {
                continue;
            }
            Execution execution = new Execution();
            execution.setTaskId(taskExt.getTaskId());
            execution.setUid(uid);
            switch (taskExt.getTask().getPlugin()) {
                case "disposablePlugin":
                    taskExt.setLimitTimes(1);
                    taskExt.setFinishedTimes(taskExt.getLimitTimes());
                    taskExt.setLinkDes("已完成");
                    onceList.add(taskExt);
                    break;
                case "intervalDayPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    taskExt.setFinishedTimes(taskExt.getLimitTimes());
                    taskExt.setLinkDes("已完成");
                    dayList.add(taskExt);
                    break;
                case "intervalWeekPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    //todo
                    String timeStr = DateUtil.DateToString(time, "yyyy-MM-dd");
                    if (timeStr.equals("2018-04-02")) {
                        taskExt.setFinishedTimes(taskExt.getLimitTimes());
                        taskExt.setLinkDes("已完成");
                        weekList.add(taskExt);
                    } else {
                        taskExt.setFinishedTimes(getInterValCount(execution, DateStyle.FIELD_TYPE.WEEK, time));
                        if (taskExt.getFinishedTimes() >= taskExt.getLimitTimes()) {
                            taskExt.setLinkDes("已完成");
                        }
                        weekList.add(taskExt);
                    }
                    break;
                case "intervalMonthPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    taskExt.setFinishedTimes(getInterValCount(execution, DateStyle.FIELD_TYPE.MONTH, time));
                    if (taskExt.getFinishedTimes() >= taskExt.getLimitTimes()) {
                        taskExt.setLinkDes("已完成");
                    }
                    monthList.add(taskExt);
                    break;
            }
        }
        JSONArray jsonArray = new JSONArray();
        if (onceList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "单次任务");
            jsonObject.put("name", "单次任务");
            jsonObject.put("deadline", "");
            jsonObject.put("list", onceList);
            jsonArray.add(jsonObject);
        }
        if (dayList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本日任务");
            jsonObject.put("name", "本日任务");
            jsonObject.put("deadline", DateUtil.DateToString(time, "MM月dd日"));
            jsonObject.put("list", dayList);
            jsonArray.add(jsonObject);
        }
        if (weekList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本周任务 " + DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.WEEK, 1, "(截止至MM月dd日)"));
            jsonObject.put("name", "本周任务");
            if (isMonth) {
                Date weekEnd = DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.WEEK, 1);
                Date monthEnd = DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 1);
                if (monthEnd.getTime() <= weekEnd.getTime()) {
                    jsonObject.put("deadline",
                            DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getWeek(time).getNumber() + 1), "MM月dd日-") +
                                    DateUtil.DateToString(DateUtil.addDay(monthEnd, -1), "MM月dd日"));
                    jsonObject.put("title", "本周任务 " + DateUtil.DateToString(DateUtil.addDay(monthEnd, -1), "(截止至MM月dd日)"));
                } else {
                    jsonObject.put("deadline",
                            DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getWeek(time).getNumber() + 1), "MM月dd日-") +
                                    DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.WEEK, 1, "MM月dd日"));
                }
            } else {
                jsonObject.put("deadline",
                        DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getWeek(time).getNumber() + 1), "MM月dd日-") +
                                DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.WEEK, 1, "MM月dd日"));
            }
            jsonObject.put("list", weekList);
            jsonArray.add(jsonObject);
        }
        if (monthList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本月任务 " + DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.MONTH, 1, "(截止至MM月dd日)"));
            jsonObject.put("name", "本月任务");
            jsonObject.put("deadline",
                    DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getDay(time) + 1), "MM月dd日-") +
                            DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.MONTH, 1, "MM月dd日"));
            jsonObject.put("list", monthList);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    public JSONArray list() {
        List onceList = new ArrayList();
        List dayList = new ArrayList();
        List weekList = new ArrayList();
        List monthList = new ArrayList();
        List<TaskExt> list = taskExtMapper.all();
        for (int i = 0; i < list.size(); i++) {
            TaskExt taskExt = list.get(i);
            switch (taskExt.getTask().getPlugin()) {
                case "disposablePlugin":
                    taskExt.setLimitTimes(1);
                    onceList.add(taskExt);
                    break;
                case "intervalDayPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    dayList.add(taskExt);
                    break;
                case "intervalWeekPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    weekList.add(taskExt);
                    break;
                case "intervalMonthPlugin":
                    taskExt.setLimitTimes(JSON.parseObject(taskExt.getTask().getApplyCondition()).getInteger("limit_times"));
                    monthList.add(taskExt);
                    break;
            }
        }
        JSONArray jsonArray = new JSONArray();
        Date time = new Date();
        if (onceList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "单次任务");
            jsonObject.put("name", "单次任务");
            jsonObject.put("deadline", "");
            jsonObject.put("list", onceList);
            jsonArray.add(jsonObject);
        }
        if (dayList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本日任务");
            jsonObject.put("name", "本日任务");
            jsonObject.put("deadline", DateUtil.DateToString(time, "MM月dd日"));
            jsonObject.put("list", dayList);
            jsonArray.add(jsonObject);
        }
        if (weekList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本周任务 " + DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.WEEK, 1, "(截止至MM月dd日)"));
            jsonObject.put("name", "本周任务");
            Date weekEnd = DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.WEEK, 1);
            Date monthEnd = DateUtil.getDateStart(time, DateStyle.FIELD_TYPE.MONTH, 1);
            if (monthEnd.getTime() <= weekEnd.getTime()) {
                jsonObject.put("deadline",
                        DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getWeek(time).getNumber() + 1), "MM月dd日-") +
                                DateUtil.DateToString(DateUtil.addDay(monthEnd, -1), "MM月dd日"));
                jsonObject.put("title", "本周任务 " + DateUtil.DateToString(DateUtil.addDay(monthEnd, -1), "(截止至MM月dd日)"));
            } else {
                jsonObject.put("deadline",
                        DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getWeek(time).getNumber() + 1), "MM月dd日-") +
                                DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.WEEK, 1, "MM月dd日"));
            }
            jsonObject.put("list", weekList);
            jsonArray.add(jsonObject);
        }
        if (monthList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "本月任务 " + DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.MONTH, 1, "(截止至MM月dd日)"));
            jsonObject.put("name", "本月任务");
            jsonObject.put("deadline",
                    DateUtil.DateToString(DateUtil.addDay(time, -DateUtil.getDay(time) + 1), "MM月dd日-") +
                            DateUtil.getDateEnd(time, DateStyle.FIELD_TYPE.MONTH, 1, "MM月dd日"));
            jsonObject.put("list", monthList);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    public Integer getInterValCount(Execution execution, int timeUnit) {
        Date currentTime = new Date();
        Date startTime = DateUtil.getDateStart(currentTime, timeUnit, 0);
        Date endTime = DateUtil.getDateStart(currentTime, timeUnit, 1);
        return executionMapper.queryCount(execution, startTime, endTime);
    }

    public Integer getInterValCount(Execution execution, int timeUnit, Date currentTime) {
        Date startTime = DateUtil.getDateStart(currentTime, timeUnit, 0);
        Date endTime = DateUtil.getDateStart(currentTime, timeUnit, 1);
        return executionMapper.queryCount(execution, startTime, endTime);
    }

    public Integer getInterValCount(Execution execution, Date currentTime) {
        Date endTime = DateUtil.getDateStart(currentTime, DateStyle.FIELD_TYPE.DAY, 1);
        return executionMapper.queryCount(execution, null, endTime);
    }


}
