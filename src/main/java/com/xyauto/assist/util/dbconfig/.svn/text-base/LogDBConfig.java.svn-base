package com.xyauto.assist.util.dbconfig;

import com.xyauto.assist.util.interceptor.EntityInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * Created by shiqm on 2018-03-03.
 */

@Configuration
@MapperScan(basePackages = "com.xyauto.assist.mapper.log", sqlSessionTemplateRef = "logSessionTemplate")
public class LogDBConfig {


    @Bean("logDataSource")
    @ConfigurationProperties("spring.datasource.log")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("logSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("logDataSource") DataSource dataSource, @Qualifier("entityInterceptor") EntityInterceptor entityInterceptor) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(new Interceptor[]{entityInterceptor});
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/log/*.xml"));
        return bean.getObject();
    }

    @Bean("logTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("logDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("logSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("logSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }



}
