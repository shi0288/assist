package com.xyauto.assist.plugin;

import com.mcp.fastcloud.util.exception.base.ErrorException;
import com.xyauto.assist.entity.Execution;
import com.xyauto.assist.entity.Task;
import com.xyauto.assist.mapper.broker.ExecutionMapper;
import com.xyauto.assist.util.PluginAbs;
import com.xyauto.assist.util.annotation.AutoRelease;
import com.xyauto.assist.util.constant.CommonCons;
import com.xyauto.assist.util.constant.ErrorCons;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shiqm on 2018-01-19.
 */

@AutoRelease("一次性任务")
public class DisposablePlugin extends PluginAbs {

    @Autowired
    private ExecutionMapper executionMapper;

    @Override
    public void business(Task task, Execution execution) {
        if (executionMapper.selectCount(execution) > 0) {
            throw new ErrorException(ErrorCons.EXECUTION_MAX_TIME);
        }
    }

    @Override
    public void valid(Task task) {
        if (task.getIsTarget().compareTo(CommonCons.TargetFlag.USER) != 0) {
            throw new ErrorException(ErrorCons.TASK_ONE_TIME_ERROR);
        }
    }





}
