package com.xyauto.assist.cloud;

import com.mcp.fastcloud.annotation.ServerName;
import com.mcp.fastcloud.util.Result;
import feign.Param;
import feign.RequestLine;

/**
 * Created by shiqm on 2018-03-06.
 */

@ServerName("QCDQ-DEALER-SERVER-NEW")
public interface DealerSeverCloud {

    @RequestLine("GET /dealerrecord/schedulelist/{id}")
    Result dealerScheduleList(@Param("id") Long dealerId);

    @RequestLine("GET /dealerrecord/schedulelist/{id}?date={date}")
    Result dealerScheduleList(@Param("id") Long dealerId,@Param("date") String date);


}
