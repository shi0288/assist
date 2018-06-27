//package com.xyauto.assist.controller;
//
//import com.mcp.fastcloud.annotation.Log;
//import com.xyauto.assist.entity.Broker;
//import com.xyauto.assist.entity.BrokerTaskFinished;
//import com.xyauto.assist.entity.BrokerTaskMonthFinished;
//import com.xyauto.assist.entity.DealerTaskMonthFinished;
//import com.xyauto.assist.mapper.broker.TaskExtMapper;
//import com.xyauto.assist.mapper.log.BrokerTaskFinishedMapper;
//import com.xyauto.assist.mapper.log.BrokerTaskMonthFinishedMapper;
//import com.xyauto.assist.mapper.log.DealerTaskMonthFinishedMapper;
//import com.xyauto.assist.plugin.DisposablePlugin;
//import com.xyauto.assist.mapper.broker.ExecutionMapper;
//import com.xyauto.assist.mapper.broker.TaskMapper;
//import com.xyauto.assist.service.BrokerTaskFinishedService;
//import com.xyauto.assist.service.TaskService;
//import com.xyauto.assist.util.Cache;
//import com.xyauto.assist.util.DateUtil;
//import com.xyauto.assist.util.KafkaHelper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.util.concurrent.ListenableFuture;
//
//import java.util.*;
//
//
///**
// * Created by shiqm on 2018-01-18.
// */
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class TestControllerTest {
//
//
//    @Autowired
//    private TaskMapper taskMapper;
//
//    @Autowired
//    private TaskService taskService;
//
//    @Autowired
//    private TaskExtMapper taskExtMapper;
//
//    @Autowired
//    private ExecutionMapper executionMapper;
//    @Log
//    private Logger logger;
//    @Autowired
//    private Cache cache;
//    @Autowired
//    private DisposablePlugin disposableApply;
//
//    @Autowired
//    private BrokerTaskFinishedService brokerTaskFinishedService;
//
//    @Autowired
//    private BrokerTaskMonthFinishedMapper brokerTaskMonthFinishedMapper;
//
//    @Autowired
//    private KafkaTemplate kafkaTemplate;
//
//    @Autowired
//    private BrokerTaskFinishedMapper brokerTaskFinishedMapper;
//    @Autowired
//    private DealerTaskMonthFinishedMapper dealerTaskMonthFinishedMapper;
//
//
//    @Test
//    public void test1() throws Exception {
//
//
//        Long dealer_id = 100208720L;
//        Set<Long> set = new HashSet<>();
//        set.add(8763L);
//        set.add(96795L);
//        set.add(116850L);
//        set.add(122405L);
//        List<Broker> list = new ArrayList<>();
//        set.forEach(it -> {
//            Broker broker = new Broker();
//            broker.setBrokerId(it);
//            broker.setDealerId(dealer_id);
//            list.add(broker);
//        });
//
////        for (int i = 0; i < list.size(); i++) {
////            Broker broker = list.get(i);
////            for (int m = 1; m < 19; m++) {
////                String time="2018-03-"+m+" 10:00:00";
////                BrokerTaskFinished brokerTaskFinished = brokerTaskFinishedService.getBrokerDayFinishPercent(broker.getBrokerId(), broker.getDealerId(), DateUtil.StringToDate(time));
////                if (brokerTaskFinished == null) {
////                    continue;
////                }
////                brokerTaskFinishedMapper.insertSelective(brokerTaskFinished);
////            }
////        }
//
////
//        for (int i = 0; i < list.size(); i++) {
//            Broker broker = list.get(i);
//            for (int m = 1; m < 19; m++) {
//                String time = "2018-03-" + m + " 10:00:00";
//                BrokerTaskMonthFinished brokerTaskMonthFinished = brokerTaskFinishedService.getBrokerMonthFinishPercent(broker.getBrokerId(), broker.getDealerId(), DateUtil.StringToDate(time));
//                brokerTaskMonthFinishedMapper.insertSelective(brokerTaskMonthFinished);
//            }
//        }
//
//
//
//
//            for (int m = 1; m < 19; m++) {
//                String time = "2018-03-" + m + " 10:00:00";
//                DealerTaskMonthFinished dealerTaskMonthFinished = brokerTaskFinishedService.getDealerMonthFinishPercent(dealer_id, DateUtil.StringToDate(time));
//                dealerTaskMonthFinishedMapper.insertSelective(dealerTaskMonthFinished);
//            }
//
//
//
//
////
////        int count = taskService.getBrokerIdsCount();
////        int pageNum = count / 300;
////        if (count % 300 == 0) {
////        } else {
////            pageNum++;
////        }
////        for (int i = 1; i <= pageNum; i++) {
////            brokerTaskFinishedService.saveTaskFinished(DateUtil.addDay(new Date(), -1), i);
////        }
//
////        System.out.println( kafkaTemplate.partitionsFor("interact_task_add_point_topic"));
//
////        ListenableFuture future = kafkaTemplate.send("broker_partition_three_topic", 2, "bbb");
////        future.addCallback(
////                o -> {
////                    logger.info("send-消息发送成功：" + "aaaa" + " " + "bbb");
////                },
////                throwable -> logger.error("消息发送失败：" + "bbb" ));
////        Thread.sleep(5000);
//
//
////        int i=0;
////        while (true){
////            String topic = "broker_task_point";
////            String brokerId = "333";
////            JSONObject data = new JSONObject();
////            data.put("aa","bb");
////            ListenableFuture future = kafkaTemplate.send(topic, brokerId,data.toString());
////            future.addCallback(o -> System.out.println("send-消息发送成功：" + data), throwable -> System.out.println("消息发送失败：" + data));
////            i++;
////            if(i>100){
////                break;
////            }
////        }
//
//
////        brokerTaskFinishedService.saveTaskFinished(new Date(),1);
//
//
////        PageHelper.startPage(1, 1, false).setOrderBy("id desc");
////        Example example = new Example(BrokerTaskMonthFinished.class);
////        example.createCriteria()
////                .andEqualTo("brokerId",1)
////                .andGreaterThanOrEqualTo("createTime", DateUtil.getDateStart(new Date(), DateStyle.FIELD_TYPE.MONTH,0))
////                .andLessThan("createTime", new Date());
////        brokerTaskMonthFinishedMapper.selectOneByExample(example);
//
////
////        System.out.println(taskExtMapper.all());
//
////        PageHelper.startPage(1, 5);
////        Page page = taskMapper.list();
////        System.out.println(page);
////        for (int i = 0; i < page.size(); i++) {
////            System.out.println(page.get(i));
////        }
//
//
////        Set< EntityColumn > columnList = EntityHelper.getColumns(Execution.class);
////        Iterator<EntityColumn> iterator=columnList.iterator();
////        while (iterator.hasNext()){
////            EntityColumn entityColumn=iterator.next();
////            System.out.println(entityColumn.getColumn());
////        }
////        System.out.println(columnList);
////        System.out.println(columnList);
////        System.out.println(columnList);
////        System.out.println(columnList);
////        System.out.println(columnList);
////        System.out.println(columnList);
////        System.out.println(columnList);
////        System.out.println(columnList);
//
////        Execution execution = new Execution();
//////        execution.setTaskId(111L);
//////        execution.setTargetId("234");
//////        execution.setUid("234");
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("aa", "dd");
////        jsonObject.put("fff", 5);
////        execution.setTag(jsonObject);
////        executionMapper.insertSelective(execution);
////        System.out.println(executionMapper.selectByPrimaryKey(22L).getTag().get("aa"));
//
//
////        logger.info("1111111111111");
////        disposableApply.business(null,null);
////        System.out.println(PluginCache.string());
//        // System.out.println(cache.get("sss"));
////        System.out.println(executionMapper.getById(1L));
////        System.out.println(executionMapper.getById(1L).getTargetId());
//
//
////        Execution execution=new Execution();
////        execution.setUid("1");
////        execution.setTargetId("dd");
////        System.out.println(executionMapper.queryCount(execution,new Date(),new Date()));
//    }
//
//}