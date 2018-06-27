package com.xyauto.assist.util;

import com.xyauto.assist.util.constant.TaskPluginCons;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by shiqm on 2018-03-03.
 */
public class FinishedResult extends HashMap<String, BigDecimal> {

    public static final String DAY_PERCENT = "Day_Percent";
    public static final String DAY_LIMIT = "Day_Limit";
    public static final String WEEK_PERCENT = "Week_Percent";
    public static final String MONTH_PERCENT = "Month_Percent";
    public static final String ONCE_PERCENT = "Once_Percent";
    public static final String TEMPORARY_PERCENT = "Temporary_Percent";


    public BigDecimal getDayPercent() {
        return this.get(DAY_PERCENT);
    }

    public BigDecimal getDayLimit() {
        return this.get(DAY_LIMIT);
    }

    public BigDecimal getWeekPercent() {
        return this.get(WEEK_PERCENT);
    }

    public BigDecimal getMonthPercent() {
        return this.get(MONTH_PERCENT);
    }

    public BigDecimal getOncePercent() {
        return this.get(ONCE_PERCENT);
    }

    public BigDecimal getTemporaryPercent() {
        return this.get(TEMPORARY_PERCENT);
    }


    private BigDecimal progress;

    public void add(String plugin, BigDecimal value) {
        switch (plugin) {
            case TaskPluginCons.ONCE:
                this.put(ONCE_PERCENT, value);
                break;
            case TaskPluginCons.INTERVAL_DAY:
                this.put(DAY_PERCENT, value);
                break;
            case TaskPluginCons.INTERVAL_WEEK:
                this.put(WEEK_PERCENT, value);
                break;
            case TaskPluginCons.INTERVAL_MONTH:
                this.put(MONTH_PERCENT, value);
                break;
            default:
                this.put(plugin,value);
        }
    }

    @Override
    public BigDecimal put(String key, BigDecimal value) {
        BigDecimal oldValue = super.get(key);
        if (oldValue != null) {
            value = oldValue.add(value);
        }
        return super.put(key, value);
    }

    @Override
    public BigDecimal get(Object key) {
        BigDecimal oldValue = super.get(key);
        if (oldValue != null) {
            return oldValue;
        }
        return BigDecimal.valueOf(0);
    }

    public BigDecimal getProgress() {
        return progress;
    }

    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

}
