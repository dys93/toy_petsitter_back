package com.toy.toy_petsitter_back.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan("com.toy.toy_petsitter_back.repository")
public class DbConfig {

    // mybatis 설정 파일을 따로 작성해서 임포트
    @Value("${spring.datasource.mapper-locations}")
    String mPath;
    @Value("${mybatis.config-location}")
    String mybatisConfigPath;

    //*연결을 위한 설정//
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource DataSource() {
        return DataSourceBuilder.create().build();
    }

    //*MyBatis와 Mysql 연결//
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources(mPath));

        Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(mybatisConfigPath);
        sessionFactory.setConfigLocation(myBatisConfig);

        return sessionFactory.getObject();
    }

}
