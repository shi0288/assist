package com.xyauto.assist.util.task;

import com.xyauto.assist.service.BrokerTaskFinishedService;
import com.xyauto.assist.service.TaskService;
import com.xyauto.assist.util.Cache;
import com.xyauto.assist.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by shiqm on 2018-03-06.
 */

@Component
public class BrokerTaskFinishedTask {


    @Autowired
    private BrokerTaskFinishedService brokerTaskFinishedService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private Cache cache;


    @Scheduled(cron = "0 0 1 * * ? ")
    public void saveBrokerTaskFinished() {
        String keyLock = "broker:sync:assist:broker:day:finished";
        boolean flag = cache.setnx(keyLock, String.valueOf(System.currentTimeMillis()), 300L);
        if (flag) {
            Date time=DateUtil.addDay(new Date(), -1);
            int count = taskService.getBrokerIdsCount(time);
            int pageNum = count / 300;
            if (count % 300 == 0) {
            } else {
                pageNum++;
            }
            for (int i = 1; i <= pageNum; i++) {
                brokerTaskFinishedService.saveTaskFinished(time, i);
            }
            count = taskService.getDealerIdsCount();
            pageNum = count / 300;
            if (count % 300 == 0) {
            } else {
                pageNum++;
            }
            for (int i = 1; i <= pageNum; i++) {
                brokerTaskFinishedService.saveDealerTaskFinished(time, i);
            }
        }
    }


//    @Scheduled(cron = "0 0 1 * * ? ")
//    public void saveDealerTaskFinished() {
//        String keyLock = "broker:sync:assist:dealer:month:finished";
//        boolean flag = cache.setnx(keyLock, String.valueOf(System.currentTimeMillis()), 300L);
//        if (flag) {
//            int count = taskService.getDealerIdsCount();
//            int pageNum = count / 300;
//            if (count % 300 == 0) {
//            } else {
//                pageNum++;
//            }
//            for (int i = 1; i <= pageNum; i++) {
//                brokerTaskFinishedService.saveDealerTaskFinished(DateUtil.addDay(new Date(), -1), i);
//            }
//        }
//    }





}
