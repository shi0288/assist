package com.xyauto.assist.util.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * Created by shiqm on 2017-07-14.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Component
public class EntityInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
        if (invocation.getArgs()[0] instanceof MappedStatement) {
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            ms.getConfiguration().setCacheEnabled(false);
            SqlCommandType sqlCommandType = ms.getSqlCommandType();
            Object parameter = invocation.getArgs()[1];
            Class<?> clazz = parameter.getClass();
            //保存时赋值创建时间
            if (sqlCommandType.equals(SqlCommandType.INSERT)) {
                try {
                    Field createdAtField = clazz.getDeclaredField("createTime");
                    createdAtField.setAccessible(true);
                    if (createdAtField.get(parameter) == null) {
                        createdAtField.set(parameter, new Date());
                    }
                } catch (NoSuchFieldException e) {
                }
            }
            //更新时和保存时都赋值updateAt
            if (sqlCommandType.equals(SqlCommandType.UPDATE) || sqlCommandType.equals(SqlCommandType.INSERT)) {
                try {
                    Field updateAtField = clazz.getDeclaredField("updateTime");
                    updateAtField.setAccessible(true);
                    updateAtField.set(parameter, new Date());
                } catch (NoSuchFieldException e) {
                }
            }
        }
        }catch(Exception e){}
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }


}
