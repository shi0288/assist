package com.xyauto.assist.util.mq;

import com.xyauto.assist.entity.Task;
import com.xyauto.assist.util.constant.MqCons;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shiqm on 2017-08-01.
 */

@Component
public class TaskApplySender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(HttpServletRequest httpServletRequest, List<Task> list,String apply) {
        Map sendData = new HashMap();
        sendData.put("list", list);
        sendData.put("time", System.currentTimeMillis());
        sendData.put("apply", apply);
        Map params = httpServletRequest.getParameterMap();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            String[] values = (String[]) params.get(key);
            for (String value : values) {
                sendData.put(key, value);
            }
        }
        rabbitTemplate.convertAndSend(MqCons.BROKER_TASK_DIRECT_EXCHANGE, MqCons.BROKER_TASK_DIRECT_EXCHANGE_FLAG.ROUTE_KEY, sendData);
    }

    public void send(Map sendData) {
        rabbitTemplate.convertAndSend(MqCons.BROKER_TASK_DIRECT_EXCHANGE, MqCons.BROKER_TASK_DIRECT_EXCHANGE_FLAG.ROUTE_KEY, sendData);
    }



}
