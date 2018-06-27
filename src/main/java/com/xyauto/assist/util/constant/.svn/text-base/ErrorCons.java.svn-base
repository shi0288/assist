package com.xyauto.assist.util.constant;

import com.mcp.fastcloud.util.Code;

/**
 * Created by shiqm on 2018-01-19.
 */
public enum ErrorCons implements Code {

    TASK_ERROR(20001, "任务创建失败"),
    TASK_NO_APPLY(20002, "任务标识不存在"),
    TASK_CLOSE(20003, "任务状态关闭"),
    TASK_TIME_START(20004, "任务时间还未开始"),
    TASK_TIME_END(20005, "任务已过期"),
    TASK_ONLY_APPLY(20006, "apply标识已经存在"),
    TASK_NO_TARGET_ID(20007, "target_id不能为空"),
    TASK_NO_USER(20008, "用户ID不能为空"),
    TASK_ONE_TIME_ERROR(20009, "一次性任务只允许用户唯一"),
    TASK_TIME_FORMAT_ERROR(20010, "时间格式错误"),
    TASK_NO_EXECUTION(20011, "没有执行记录"),
    EXECUTION_REQUEST(21001, "缺少任务参数"),
    EXECUTION_NO_PLUGIN(21002, "没有找到合适的执行组件"),
    EXECUTION_CONDITION_ERROR(21003, "执行条件和计算插件不符"),
    EXECUTION_MAX_TIME(21004, "已经满足限制次数"),
    EXECUTION_COULD_NOT(21005, "未达到执行条件"),
    EXECUTION_NOT_IN_SHOUDER(21006, "未达到执行条件"),
    ERROR(9999, "操作失败");

    ErrorCons(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    @Override
    public String toString() {
        return "ErrorCons{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
