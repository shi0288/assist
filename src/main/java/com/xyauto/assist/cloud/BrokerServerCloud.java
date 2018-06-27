package com.xyauto.assist.cloud;

import com.mcp.fastcloud.annotation.ServerName;
import com.mcp.fastcloud.util.Result;
import feign.Param;
import feign.RequestLine;

/**
 * Created by shiqm on 2018-06-11.
 */

@ServerName("QCDQ-INTERACT-BROKER-SERVER")
public interface BrokerServerCloud {


    @RequestLine("GET /broker/integral/addintegral?broker_id={broker_id}&desc={desc}&integral={integral}")
    Result addIntegral(@Param("broker_id") Long broker_id, @Param("desc") String desc,@Param("integral") Long integral);



}
