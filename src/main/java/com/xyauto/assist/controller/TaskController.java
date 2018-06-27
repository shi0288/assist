package com.xyauto.assist.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.mcp.fastcloud.util.Result;
import com.mcp.fastcloud.util.enums.ResultCode;
import com.mcp.validate.annotation.Check;
import com.xyauto.assist.cloud.DealerSeverCloud;
import com.xyauto.assist.entity.*;
import com.xyauto.assist.mapper.broker.BrokerMapper;
import com.xyauto.assist.mapper.log.DealerTaskMonthFinishedMapper;
import com.xyauto.assist.service.BrokerTaskFinishedService;
import com.xyauto.assist.service.TaskExtService;
import com.xyauto.assist.service.TaskService;
import com.xyauto.assist.util.*;
import com.xyauto.assist.util.constant.CommonCons;
import com.xyauto.assist.util.constant.JsonSchemaCons;
import com.xyauto.assist.util.mq.TaskApplySender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.*;

import static com.xyauto.assist.util.constant.ErrorCons.*;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shiqm on 2018-01-18.
 */
@RestController
@RequestMapping("task")
public class TaskController extends BaseController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskExtService taskExtService;

    @Autowired
    private BrokerTaskFinishedService brokerTaskFinishedService;

    @Autowired
    private DealerSeverCloud dealerSeverCloud;

    @Autowired
    private Cache cache;

    @Autowired
    private BrokerMapper brokerMapper;

    @Autowired
    private TaskApplySender taskApplySender;

    @Autowired
    private DealerTaskMonthFinishedMapper dealerTaskMonthFinishedMapper;

    /**
     * 创建任务
     */
    @RequestMapping("add")
    public Result add(
            @Check String plugin,
            @Check(defaultValue = "{}") String apply_condition,
            @Check(name = "is_target", pattern = "^[123]$") Short is_target,
            @Check(numOrLetter = true) String apply,
            @Check(required = false, defaultValue = "0") BigDecimal finishPercent,
            @Check(required = false, number = true, length = 10) Integer start_at,
            @Check(required = false, number = true, length = 10) Integer end_at,
            @Check String name,
            @Check String description,
            @Check(required = false, defaultValue = "0") Short type,
            @Check(required = false, defaultValue = "0") Integer times,
            @Check(required = false, defaultValue = "") String apply_other,
            @Check(required = false) String icon,
            @Check(required = false) String link,
            @Check(required = false) String link_des,
            @Check(required = false) Short sort,
            @Check(required = false) String extend
    ) {
        //任务插件是否存在
        if (!PluginCache.containsKey(plugin)) {
            return new Result(EXECUTION_NO_PLUGIN);
        }
        //条件是否符合插件格式
        if (!JsonSchemaValidator.validatorSchema(JsonSchemaCons.get(plugin), apply_condition)) {
            return new Result(EXECUTION_CONDITION_ERROR);
        }
        //任务标识是否重复
        if (!taskService.isRepeat(apply)) {
            return new Result(TASK_ONLY_APPLY);
        }
        //组装任务
        Task task = new Task();
        task.setPlugin(plugin);
        task.setApplyCondition(apply_condition);
        task.setIsTarget(is_target);
        task.setApply(apply);
        task.setStartAt(start_at);
        task.setEndAt(end_at);
        task.setFinishPercent(finishPercent);
        task.setType(type);
        task.setTimes(times);
        task.setApplyOthers(apply_other);
        //判断是否符合插件内部条件
        taskService.checkin(plugin, task);
        //组装任务扩展
        TaskExt taskExt = new TaskExt();
        taskExt.setTaskId(task.getId());
        taskExt.setName(name);
        taskExt.setDescription(description);
        taskExt.setIcon(icon);
        taskExt.setLink(link);
        taskExt.setLinkDes(link_des);
        taskExt.setSort(sort);
        taskExt.setExtend(extend);
        taskExt.setExtend(extend);
        try {
            taskService.add(task, taskExt);
        } catch (Exception e) {
            return new Result(TASK_ERROR);
        }
        return new Result();
    }

    /**
     * 执行任务
     */
    @RequestMapping("/apply/{apply}")
    public Result execute(
            HttpServletRequest request,
            @PathVariable(name = "apply") @Check String apply
    ) {
        List<Task> taskList = taskService.checkout(apply, request);
        taskApplySender.send(request, taskList, apply);
        return new Result();
    }


    /**
     * 删除积分
     */
    @RequestMapping("/revoke/{apply}")
    public Result revoke(
            @PathVariable(name = "apply") @Check String apply,
            @Check String broker_id,
            @Check String target_id,
            @Check(required = false) String desc
    ) {
        if (StringUtils.isEmpty(desc)) {
            desc = "违规扣除积分";
        }
        taskService.revokeExecution(apply, broker_id, target_id, desc);
        return new Result();
    }


    /**
     * 获取任务列表
     */
    @RequestMapping(value = "list")
    public Result list(
            @Check(value = "page", required = false, defaultValue = "1") Integer page,
            @Check(value = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        Page pageInfo = taskService.list(page, limit);
        return new Result(pageInfo.toPageInfo());
    }

    /**
     * 关闭任务
     */
    @RequestMapping(value = "close")
    public Result close(
            @Check(value = "id") Long id
    ) {
        if (taskService.update(id, CommonCons.StatusFlag.CLOSE) == 1) {
            return new Result();
        }
        return new Result(ERROR);
    }

    /**
     * 开放任务
     */
    @RequestMapping(value = "open")
    public Result open(
            @Check(value = "id") Long id
    ) {
        if (taskService.update(id, CommonCons.StatusFlag.OPEN) == 1) {
            return new Result();
        }
        return new Result(ERROR);
    }

    /**
     * 获取所有计算插件
     */
    @RequestMapping(value = "plugin/list")
    public Result plugin_list() {
        return new Result(JSON.parse(PluginCache.string()));
    }

    /**
     * 获取任务扩展信息
     */
    @RequestMapping(value = "ext/list")
    public Result extList(
            @Check(value = "uid", number = true) String uid,
            @Check(value = "time", required = false) String time
    ) {
        JSONObject jsonObject = new JSONObject();
        Date date = null;
        if (StringUtils.isEmpty(time)) {
            date = new Date();
        } else {
            date = DateUtil.StringToDate(time);
        }
        BrokerTaskFinished brokerTaskFinished = brokerTaskFinishedService.getBrokerDayFinishPercent(Long.valueOf(uid), date);
        if (brokerTaskFinished != null) {
            jsonObject.put("progress", brokerTaskFinished.getProgress());
        } else {
            jsonObject.put("progress", BigDecimal.valueOf(0));
        }
        if (brokerTaskFinished.getProgress().compareTo(BigDecimal.valueOf(1)) == 0) {
            jsonObject.put("tasks", taskExtService.listFinishd(uid, date, false));
        } else {
            jsonObject.put("tasks", taskExtService.list(uid, date));
        }
        return new Result(jsonObject);
    }

    /**
     * 获取任务扩展信息
     */
    @RequestMapping(value = "op/list")
    public Result opList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tasks", taskExtService.list());
        return new Result(jsonObject);
    }

    /**
     * op获取任务信息
     */
    @RequestMapping(value = "op/ext/list")
    public Result opExtList(
            @Check(value = "uid", number = true) String uid,
            @Check(value = "time") String time
    ) {
        JSONObject jsonObject = new JSONObject();
        Date date = null;
        try {
            date = DateUtil.StringToDate(time);
        } catch (Exception e) {
            return new Result(TASK_TIME_FORMAT_ERROR);
        }
        Broker broker = brokerMapper.selectByPrimaryKey(Long.valueOf(uid));
        jsonObject.put("broker_id", broker.getBrokerId());
        jsonObject.put("name", broker.getName());
        BrokerTaskFinished brokerTaskFinished = brokerTaskFinishedService.getBrokerDayFinishPercent(Long.valueOf(uid), date);
        if (brokerTaskFinished != null) {
            jsonObject.put("progress", brokerTaskFinished.getProgress());
        } else {
            jsonObject.put("progress", BigDecimal.valueOf(0));
        }
        if (brokerTaskFinished.getProgress().compareTo(BigDecimal.valueOf(1)) == 0) {
            jsonObject.put("tasks", taskExtService.listFinishd(uid, date, true));
        } else {
            jsonObject.put("tasks", taskExtService.list(uid, date, true));
        }
        jsonObject.put("days", taskService.getScheduleDays(Long.valueOf(uid)));
        return new Result(jsonObject);
    }

    /**
     * op获取任务信息
     */
    @RequestMapping(value = "op/dealer/list")
    public Result opDealerList(
            @Check(value = "dealer_id") Long dealer_id,
            @Check(value = "time") String time
    ) {
        JSONObject jsonObject = new JSONObject();
        Date date = null;
        try {
            date = DateUtil.StringToDate(time);
        } catch (Exception e) {
            return new Result(TASK_TIME_FORMAT_ERROR);
        }
        //时间不变，总显示当期最新得分
        DealerTaskMonthFinished dealerTaskMonthFinished = brokerTaskFinishedService.getNewDealerMonthFinishPercent(dealer_id, new Date());
        if (dealerTaskMonthFinished != null) {
            jsonObject.put("progress", dealerTaskMonthFinished.getProgress());
            jsonObject.put("score", dealerTaskMonthFinished.getProgress());
            Date preDate = new Date();
            //季度相加
            while (true) {
                preDate = DateUtil.getPreSeasonFinalDay(preDate);
                if (preDate == null) {
                    break;
                }
                Example example = new Example(DealerTaskMonthFinished.class);
                example.createCriteria()
                        .andEqualTo("dealerId", dealer_id)
                        .andEqualTo("calTime", preDate);
                DealerTaskMonthFinished temp = null;
                try {
                    temp = dealerTaskMonthFinishedMapper.selectOneByExample(example);
                    jsonObject.put("progress", jsonObject.getBigDecimal("progress").add(temp.getProgress()));
                    jsonObject.put("score", jsonObject.getBigDecimal("score").add(temp.getProgress()));
                } catch (Exception e) {
                }
            }
        } else {
            jsonObject.put("progress", BigDecimal.valueOf(0));
            jsonObject.put("score", BigDecimal.valueOf(0));
        }
        jsonObject.put("days", taskService.getScheduleDaysByDealerId(dealer_id));
        jsonObject.put("list", taskService.getBrokerTaskInfoByDealerId(dealer_id, date));
        return new Result(jsonObject);
    }


    /**
     * 经销商车顾问详情
     */
    @RequestMapping(value = "op/dealer/list/brokers")
    public Result opDealerListBrokers(
            @Check(value = "dealer_id") Long dealer_id,
            @Check(value = "time") String time
    ) {
        Date date = null;
        try {
            date = DateUtil.StringToDate(time);
        } catch (Exception e) {
            return new Result(TASK_TIME_FORMAT_ERROR);
        }
        Date today = new Date();
        int curYear = DateUtil.getYear(today);
        int curMonth = DateUtil.getMonth(today);
        int tempYear = DateUtil.getYear(date);
        int tempMonth = DateUtil.getMonth(date);
        Date queryTime = null;
        if (curYear == tempYear && curMonth == tempMonth) {
            //当期月份需要显示当天
            queryTime = today;
        } else {
            //往期月份显示月底当天
            Date endTime = DateUtil.getDateStart(date, DateStyle.FIELD_TYPE.MONTH, 1);
            queryTime = DateUtil.addDay(endTime, -1);
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = taskService.getBrokerTaskMonthInfoByDealerId(dealer_id, queryTime);
        jsonArray.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    JSONObject a = (JSONObject) o1;
                    JSONObject b = (JSONObject) o2;
                    BrokerTaskMonthFinished a1 = (BrokerTaskMonthFinished) a.get("info");
                    BrokerTaskMonthFinished b1 = (BrokerTaskMonthFinished) b.get("info");
                    return -a1.getProgress().compareTo(b1.getProgress());
                } catch (Exception e) {
                }
                return 0;
            }
        });
        jsonObject.put("list", jsonArray);
        return new Result(jsonObject);
    }

    /**
     * 经销商车顾问或车顾问月完成度详情
     */
    @RequestMapping(value = "op/month/finish/list")
    public Result opMonthFinishList(
            @Check(value = "dealer_id", required = false) Long dealer_id,
            @Check(value = "broker_id", required = false) Long broker_id,
            @Check(value = "time") String time
    ) {
        Date date = null;
        try {
            date = DateUtil.StringToDate(time);
        } catch (Exception e) {
            return new Result(TASK_TIME_FORMAT_ERROR);
        }
        Date today = new Date();
        int curYear = DateUtil.getYear(today);
        int curMonth = DateUtil.getMonth(today);
        int tempYear = DateUtil.getYear(date);
        int tempMonth = DateUtil.getMonth(date);
        Date startTime = null;
        Date endTime = null;
        boolean plugsToday = false;
        if (curYear == tempYear && curMonth == tempMonth) {
            //当期月份需要显示当天
            startTime = DateUtil.getDateStart(date, DateStyle.FIELD_TYPE.MONTH, 0);
            endTime = today;
            plugsToday = true;
        } else {
            //往期月份显示月底当天
            startTime = DateUtil.getDateStart(date, DateStyle.FIELD_TYPE.MONTH, 0);
            endTime = DateUtil.getDateStart(date, DateStyle.FIELD_TYPE.MONTH, 1);
        }
        List list = null;
        if (dealer_id != null) {
            list = brokerTaskFinishedService.getDealerMonthFinishPercentByTime(dealer_id, startTime, endTime);
            if (plugsToday) {
                list.add(brokerTaskFinishedService.getNewDealerMonthFinishPercent(dealer_id, today));
            }
        } else if (broker_id != null) {
            list = brokerTaskFinishedService.getBrokerMonthFinishPercentByTime(broker_id, startTime, endTime);
            if (plugsToday) {
                list.add(brokerTaskFinishedService.getBrokerMonthFinishPercent(broker_id, today));
            }
        }
        return new Result(list);
    }

    /**
     * 获取经纪人日任务完成度
     */
    @RequestMapping(value = "export/broker/day")
    public Result exportBrokerDay(
            @Check(value = "uid") Set<String> uid,
            @Check(value = "time", required = false) String time
    ) {
        //todo 判断如果不再排期内则返回0
        Date day = null;
        if (StringUtils.isEmpty(time)) {
            day = new Date();
        } else {
            day = DateUtil.StringToDate(time);
        }
        Result result = new Result();
        JSONArray items = new JSONArray();
        for (String temp : uid) {
            BrokerTaskFinished brokerTaskFinished = brokerTaskFinishedService.getBrokerDayFinishPercent(Long.valueOf(temp), day);
            if (brokerTaskFinished != null) {
                items.add(brokerTaskFinished);
            }
        }
        result.setData(items);
        return result;
    }

    /**
     * 获取经纪人月任务完成度
     */
    @RequestMapping(value = "export/broker/month")
    public Result exportBrokerMonth(
            @Check(value = "uid", number = true) String uid,
            @Check(value = "time", required = false) String time
    ) {
        Date day = null;
        if (StringUtils.isEmpty(time)) {
            day = new Date();
        } else {
            day = DateUtil.StringToDate(time);
        }
        return new Result(brokerTaskFinishedService.getBrokerMonthFinishPercent(Long.valueOf(uid), day));
    }

    /**
     * 获取经经销商完成度
     */
    @RequestMapping(value = "export/dealer/month")
    public Result exportDealerMonth(
            @Check(value = "dealer_id") Long dealerId,
            @Check(value = "time", required = false) String time
    ) {
        Date day = null;
        if (StringUtils.isEmpty(time)) {
            day = new Date();
        } else {
            day = DateUtil.StringToDate(time);
        }
        return new Result(brokerTaskFinishedService.getNewDealerMonthFinishPercent(dealerId, day));
    }

    /**
     * 获取经经销商排期
     */
    @RequestMapping(value = "export/dealer/Schedule/list")
    public Result listForDealerSchedule(
            @Check(value = "dealer_id") Long dealerId,
            @Check(value = "date", required = false) String date
    ) {
        if (StringUtils.isEmpty(date)) {
            return dealerSeverCloud.dealerScheduleList(dealerId);
        }
        return dealerSeverCloud.dealerScheduleList(dealerId, date);
    }

    /**
     * 获取积分任务
     */
    @RequestMapping(value = "list/point")
    public Result listForPoint() {
        return new Result(taskExtService.allForPoint());
    }

//    @RequestMapping(value = "/broker/daypercent/append")
//    public Result append(
//            @Check(value = "time", required = false) String time
//    ) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = null;
//        try {
//            date = simpleDateFormat.parse(time);
//        } catch (Exception e) {
//            return new Result(ResultCode.ERROR_PARAMS);
//        }
//        int count = taskService.getBrokerIdsCount();
//        int pageNum = count / 300;
//        if (count % 300 == 0) {
//        } else {
//            pageNum++;
//        }
//        for (int i = 1; i <= pageNum; i++) {
//            brokerTaskFinishedService.saveTaskFinished(date, i);
//        }
//        return new Result(ResultCode.OK);
//    }


//    @RequestMapping(value = "/broker/daytask/append")
//    public Result appendtask(
//            @Check(value = "time", required = false) String time,
//            @Check(value = "week", required = false, defaultValue="0") int week
//    ) {
//
//        int count = taskService.getBrokerIdsCount();
//        int pageNum = count / 300;
//        if (count % 300 == 0) {
//        } else {
//            pageNum++;
//        }
//        for (int i = 1; i <= pageNum; i++) {
//            brokerTaskFinishedService.tmpAddTaskFinish(time, week, i);
//        }
//        return new Result(ResultCode.OK);
//    }

    static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);


    @RequestMapping(value = "/broker/dayFinished/append")
    public Result appendDayFinished(
            @Check(value = "time", required = false) String time,
            @Check(value = "week", required = false, defaultValue = "0") int week
    ) {

        int count = taskService.getBrokerIdsCount(DateUtil.StringToDate(time));
        int pageNum = count / 300;
        if (count % 300 == 0) {
        } else {
            pageNum++;
        }
        for (int i = 1; i <= pageNum; i++) {
            int a = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    brokerTaskFinishedService.tmpAddTaskDayFinish(time, week, a);
                }
            });
        }
        return new Result(ResultCode.OK);
    }


    @RequestMapping(value = "/broker/monthFinished/append")
    public Result appendMonthFinished(
            @Check(value = "time", required = false) String time
    ) {
        int count = taskService.getBrokerIdsCount(DateUtil.StringToDate(time));
        int pageNum = count / 300;
        if (count % 300 == 0) {
        } else {
            pageNum++;
        }
        for (int i = 1; i <= pageNum; i++) {
            int a = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    brokerTaskFinishedService.tmpAddTaskMonthFinish(time, a);
                }
            });
        }
        return new Result(ResultCode.OK);
    }


    @RequestMapping(value = "/dealer/monthFinished/append")
    public Result appendDealerMonthFinished(
            @Check(value = "time", required = false) String time
    ) {
        int count = taskService.getDealerIdsCount();
        int pageNum = count / 300;
        if (count % 300 == 0) {
        } else {
            pageNum++;
        }
        for (int i = 1; i <= pageNum; i++) {
            int a = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    brokerTaskFinishedService.tmpAddDealerTaskMonthFinish(time, a);
                }
            });
        }
        return new Result(ResultCode.OK);
    }


    @RequestMapping(value = "/broker/assist/month/finish")
    public Result brokerAssistMonthFinish(
            @Check(value = "dealer_id") Long dealerId,
            @Check(value = "date", required = false) String date
    ) {
        Date day = null;
        if (StringUtils.isEmpty(date)) {
            day = new Date();
        } else {
            day = DateUtil.StringToDate(date);
        }
        brokerTaskFinishedService.saveBrokerFinishd(dealerId, day);
        return new Result(ResultCode.OK);
    }


    @RequestMapping(value = "/dealer/assist/month/finish")
    public Result dealerAssistMonthFinish(
            @Check(value = "dealer_id") Long dealerId,
            @Check(value = "date", required = false) String date
    ) {
        Date day = null;
        if (StringUtils.isEmpty(date)) {
            day = new Date();
        } else {
            day = DateUtil.StringToDate(date);
        }
        brokerTaskFinishedService.saveDealerFinishd(dealerId, day);
        return new Result(ResultCode.OK);
    }


}
