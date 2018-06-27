package com.xyauto.assist.service;

import com.alibaba.fastjson.JSONObject;
import com.mcp.fastcloud.annotation.Log;
import com.mcp.fastcloud.util.SpringIocUtil;
import com.mcp.fastcloud.util.exception.base.BaseException;
import com.xyauto.assist.entity.Broker;
import com.xyauto.assist.entity.Execution;
import com.xyauto.assist.entity.Task;
import com.xyauto.assist.entity.TaskExt;
import com.xyauto.assist.mapper.broker.BrokerMapper;
import com.xyauto.assist.mapper.broker.ExecutionMapper;
import com.xyauto.assist.util.*;
import com.xyauto.assist.util.constant.MqCons;
import com.xyauto.assist.util.mq.TaskApplySender;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by shiqm on 2018-01-19.
 */

@Service
@RabbitListener(queues = MqCons.BROKER_TASK_DIRECT_EXCHANGE_FLAG.WAIT_QUEUE, containerFactory = "taskContainerFactory")
public class ExecutionService {

    @Autowired
    private ExecutionMapper executionMapper;

    @Autowired
    private TaskExtService taskExtService;

    @Autowired
    private KafkaHelper kafkaHelper;

    @Autowired
    private BrokerMapper brokerMapper;

    @Autowired
    private TaskApplySender taskApplySender;

    @Autowired
    private BrokerTaskFinishedService brokerTaskFinishedService;

    @Autowired
    private Cache cache;

    @Log
    private Logger logger;

    @RabbitHandler
    public void process(Map params) {
        List<Task> taskList = (List<Task>) params.get("list");
        //处理
        String uid = (String) params.get("uid");
        try {
            Long.valueOf(uid);
        } catch (Exception e) {
            Broker query = new Broker();
            query.setUserToken(uid);
            query.setIsDeleted(0);
            List<Broker> list = brokerMapper.select(query);
            if (list.size() == 1) {
                uid = list.get(0).getBrokerId().toString();
                params.put("uid", uid);
            } else {
                logger.warn(uid + "不存在用户");
                return;
            }
        }
        String key = "broker:task:apply:async:" + uid;
        if (cache.setnx(key, String.valueOf(System.currentTimeMillis()), 60L)) {
            //判断用户是否离职
            boolean isBroker = true;
            try {
                Broker broker = brokerMapper.selectByPrimaryKey(Long.valueOf(uid));
                if (broker == null) {
                    logger.warn(uid + "不存在用户");
                    isBroker = false;
                }
                if (broker != null && broker.getIsDeleted() == 1) {
                    logger.warn(uid + "用户已经离职");
                    isBroker = false;
                }
            } catch (Exception e) {
                logger.warn(uid + "获取用户信息出错");
                isBroker = false;
            }
            if (isBroker) {
                for (int i = 0; i < taskList.size(); i++) {
                    Task task = taskList.get(i);
                    //记录日志
                    try {
                        if (task.getApply().equals(params.get("apply"))) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("brokerId", uid);
                            Broker broker = brokerMapper.selectByPrimaryKey(Long.valueOf(uid));
                            jsonObject.put("dealerId", broker.getDealerId());
                            jsonObject.put("targetId", task.getId());
                            TaskExt taskExt = taskExtService.get(task.getId());
                            jsonObject.put("message", taskExt.getName());
                            JSONObject extra = new JSONObject();
                            extra.put("type", task.getApply());
                            extra.put("description", taskExt.getDescription());
                            jsonObject.put("extra", extra);
                            kafkaHelper.send("interact_broker_operation_topic", "broker_task_finish", jsonObject.toString());
                        }
                    } catch (Exception e) {
                    }

                    try {
                        this.execute(task, params);
                    } catch (Exception e) {
                        if (BaseException.class.isAssignableFrom(e.getClass())) {
                        } else {
                            e.printStackTrace();
                        }
                    }
                }
            }
            cache.remove(key);
        } else {
            taskApplySender.send(params);
        }
    }

    public void execute(Task task, Map params) {
        String plugin = task.getPlugin();
        PluginAbs pluginAbs = (PluginAbs) SpringIocUtil.getBean(plugin);
        Execution execution = pluginAbs.create(task, params);
        //判断日期限制
        boolean resetProgress = false;
        try {
            JSONObject condition = JSONObject.parseObject(task.getApplyCondition());
            if (condition.containsKey("pre_day")) {
                int pre_day = condition.getInteger("pre_day");
                Date curDay = new Date();
                int day = DateUtil.getDay(curDay);
                if (day <= pre_day) {
                    Date startDay = DateUtil.getDateStart(curDay, DateStyle.FIELD_TYPE.MONTH, 0);
                    execution.setCreateTime(DateUtil.addDay(startDay, -1));
                    resetProgress=true;
                }
            }
        } catch (Exception e) {
            logger.warn(execution.getUid() + "判断日期限制出错");
            e.printStackTrace();
        }
        executionMapper.insertSelective(execution);
        //重新计算
        try {
            if(resetProgress){
                brokerTaskFinishedService.updateTaskFinished(execution.getCreateTime(),Long.valueOf(execution.getUid()));
            }
        }catch (Exception e) {
            logger.warn(execution.getUid() + "更新完成度出错");
            e.printStackTrace();
        }
        //添加积分&记录日志
        TaskExt taskExt = null;
        if (execution.getTag().size() > 0 && execution.getTag().containsKey("point")) {
//            String topic = "broker_task_point_topic";
            String topic = "interact_task_add_point_topic";
            taskExt = taskExtService.get(task.getId());
            execution.getTag().put("description", taskExt.getName());
            execution.getTag().put("broker_id", execution.getUid());
            kafkaHelper.send(topic, execution.getTag().toString());
        }
    }


}
