package com.xyauto.assist.plugin;

import com.xyauto.assist.entity.Execution;
import com.xyauto.assist.entity.Task;
import com.xyauto.assist.util.DateStyle;
import com.xyauto.assist.util.PluginAbs;
import com.xyauto.assist.util.annotation.AutoRelease;


/**
 * Created by shiqm on 2018-01-19.
 */

@AutoRelease("日任务")
public class IntervalDayPlugin extends PluginAbs {


    @Override
    public void business(Task task, Execution execution) {
        interval(task, execution, DateStyle.FIELD_TYPE.DAY);
    }

}
