package com.xyauto.assist.util;

import com.mcp.fastcloud.annotation.Log;
import com.mcp.fastcloud.util.Result;
import com.mcp.fastcloud.util.enums.ResultCode;
import com.mcp.fastcloud.util.exception.base.BaseException;
import com.mcp.validate.BindResult;
import com.mcp.validate.exception.ValidateException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shiqm on 2018-01-19.
 */
public class BaseController {

    @Log
    protected Logger logger;

    @ExceptionHandler({Exception.class})
    public Object handleException(HttpServletRequest req, Exception ex) {
        Throwable e = ExceptionUtils.getRootCause(ex);
        if (null != e) {
            ex = (Exception)e;
        }
        Result result;
        if (BaseException.class.isAssignableFrom(ex.getClass())) {
            BaseException baseException = (BaseException)ex;
            switch(baseException.getLevel()) {
                case INFO:
                    this.logger.info(baseException.getCode().toString());
                    break;
                case WARN:
                    this.logger.warn(baseException.getCode().toString());
                    break;
                case DEBUG:
                    this.logger.debug(baseException.getCode().toString());
                    break;
                case ERROR:
                    this.logger.error(baseException.getCode().toString());
                    break;
                default:
                    this.logger.error(baseException.getCode().toString());
            }

            result = new Result(baseException.getCode());
        } else if (ex.getClass().isAssignableFrom(MissingServletRequestParameterException.class)) {
            MissingServletRequestParameterException msrpException = (MissingServletRequestParameterException)ex;
            this.logger.warn("请求:" + req.getRequestURI() + " 缺少参数：" + msrpException.getParameterName());
            result = new Result(ResultCode.ERROR_PARAMS);
        } else if (ex.getClass().isAssignableFrom(ValidateException.class)) {
            ValidateException validateException= (ValidateException) ex;
            BindResult bindResult = validateException.getBindResult();
            result = new Result(9999, bindResult.getMessage());
        } else if (ex.getClass().isAssignableFrom(NumberFormatException.class)) {
            this.logger.warn("请求:" + req.getRequestURI() + " 参数类型错误" );
            result = new Result(9999,"传入的数据类型有误");
        } else {
            this.logger.error(ExceptionUtils.getStackTrace(ex));
            result = new Result(ResultCode.OVER);
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }


}
