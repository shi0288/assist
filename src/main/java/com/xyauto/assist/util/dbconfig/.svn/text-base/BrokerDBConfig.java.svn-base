package com.xyauto.assist.util.dbconfig;

import com.xyauto.assist.util.handle.JsonTypeHandle;
import com.xyauto.assist.util.interceptor.EntityInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
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
 * Created by shiqm on 2018-02-08.
 */

@Configuration
@MapperScan(basePackages = "com.xyauto.assist.mapper.broker", sqlSessionTemplateRef = "brokerSessionTemplate")
public class BrokerDBConfig {

    @Bean("brokerDataSource")
    @ConfigurationProperties("spring.datasource.broker")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("brokerSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("brokerDataSource") DataSource dataSource, @Qualifier("entityInterceptor") EntityInterceptor entityInterceptor) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(new Interceptor[]{entityInterceptor});
        bean.setTypeHandlers(new TypeHandler[]{new JsonTypeHandle()});
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/broker/*.xml"));
        return bean.getObject();
    }

    @Bean("brokerTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("brokerDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("brokerSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("brokerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
