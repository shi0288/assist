package com.xyauto.assist.mapper.broker;

import com.xyauto.assist.entity.TaskExt;
import com.xyauto.assist.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskExtMapper extends BaseMapper<TaskExt> {

    List<TaskExt> all();

    List<TaskExt> allForPoint();

}