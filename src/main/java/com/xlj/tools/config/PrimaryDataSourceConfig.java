package com.xlj.tools.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author XLJ
 * @description: 默认数据源配置
 * @date 2020/9/22 15:01
 */
@Configuration
@MapperScan(basePackages = "com.xlj.datasource.dao.one", sqlSessionFactoryRef = "PrimarySqlSessionFactory")    // 配置mybatis的接口类放的地方
public class PrimaryDataSourceConfig {

    @Primary    // 表示这个数据源是默认数据源
    @Bean(name = "PrimaryDataSource")   // 将这个对象放入Spring容器中
    @ConfigurationProperties(prefix = "spring.datasource.primary")  // 读取application.properties中的配置参数映射成为一个对象
    public DataSource getPrimaryDateSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "PrimarySqlSessionFactory")
    // @Qualifier表示查找Spring容器中名字为test1DataSource的对象
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("PrimaryDataSource") DataSource datasource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        return bean.getObject();
    }

    @Primary
    @Bean(name = "PrimarySqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("PrimarySqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}

