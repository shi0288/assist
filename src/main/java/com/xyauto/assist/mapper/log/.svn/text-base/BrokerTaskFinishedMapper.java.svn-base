package com.xyauto.assist.mapper.log;

import com.xyauto.assist.entity.BrokerTaskFinished;
import com.xyauto.assist.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by shiqm on 2018-03-03.
 */

@Mapper
public interface BrokerTaskFinishedMapper extends BaseMapper<BrokerTaskFinished> {


    BigDecimal getDayProgressSum(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("ids") List brokerIds, @Param("limit") Integer limit);


    BigDecimal getDealerMonthProgress(@Param("calTime") Date calTime, @Param("dealerId") Long dealerId);

    int isWeek(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dealerId") Long dealerId);

    int isMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dealerId") Long dealerId);

}
