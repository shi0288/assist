package com.xyauto.assist.mapper.broker;

import com.xyauto.assist.entity.Execution;
import com.xyauto.assist.entity.ExecutionExt;
import com.xyauto.assist.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface ExecutionMapper extends BaseMapper<Execution> {

    /**
     * 获取时间区间的执行记录
     * @param execution
     * @param startTime
     * @param endTime
     * @return
     */
    int queryCount(@Param(value = "execution") Execution execution, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime);



    /**
     * 获取时间区间的执行扩展记录
     * @param executionExt
     * @param startTime
     * @param endTime
     * @return
     */
    int queryExtCount(@Param(value = "executionExt") ExecutionExt executionExt, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime);


}