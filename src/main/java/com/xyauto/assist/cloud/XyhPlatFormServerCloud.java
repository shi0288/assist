package com.xyauto.assist.cloud;

import com.mcp.fastcloud.annotation.ServerName;
import com.mcp.fastcloud.util.Result;
import feign.Param;
import feign.RequestLine;

/**
 * Created by shiqm on 2018-03-06.
 */

@ServerName("XYH-PLATFORM-SERVER")
public interface XyhPlatFormServerCloud {


    @RequestLine("GET /api/dealer/record/isaccountactive?accountId={accountId}&time={time}")
    Result isBrokerSchedule(@Param("accountId") Long brokerId,@Param("time") String time);



}
