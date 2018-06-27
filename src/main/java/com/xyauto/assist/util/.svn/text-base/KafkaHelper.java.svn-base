package com.xyauto.assist.util;

import com.mcp.fastcloud.annotation.Log;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Created by shiqm on 2018-03-07.
 */

@Component
public class KafkaHelper {


    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Log
    private Logger logger;


    public void send(String topic, String content) {
        ListenableFuture future = kafkaTemplate.send(topic, content);
        future.addCallback(
                o -> {
                    logger.info("send-消息发送成功：" + content);
                },
                throwable -> logger.error("消息发送失败：" + content));
    }


    public void send(String topic, String key, String content) {
        ListenableFuture future = kafkaTemplate.send(topic, key, content);
        future.addCallback(
                o -> {
                    logger.info("send-消息发送成功：" + content);
                },
                throwable -> logger.error("消息发送失败：" + content));
    }


}
