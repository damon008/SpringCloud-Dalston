package com.leinao.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.github.pagehelper.PageHelper;

/**
*
*
* created by wangshoufa
* 2018年5月23日 下午7:39:37
*
*/
@Component
@Configuration
@EnableTransactionManagement
@MapperScan("com.leinao.*.dao")
public class MybaitsConfig {

    @Autowired
    private Environment env;

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("jdbc.driverClassName"));
        props.put("url", env.getProperty("jdbc.url"));
        props.put("username", env.getProperty("jdbc.username"));
        props.put("password", env.getProperty("jdbc.password"));
        return DruidDataSourceFactory.createDataSource(props);
    }

    /**
     * 创建数据源(数据源的名称：方法名可以取为XXXDataSource(),XXX为数据库名称,默认该名称也就是数据源的名称)
     *
     * @return
     * @throws Exception
     */
    /*@Bean(name = "dbEventDataSource")
   
    public DataSource dbEventDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("dbEvent.jdbc.driverClassName"));
        props.put("url", env.getProperty("dbEvent.jdbc.url"));
        props.put("username", env.getProperty("dbEvent.jdbc.username"));
        props.put("password", env.getProperty("dbEvent.jdbc.password"));
        return DruidDataSourceFactory.createDataSource(props);
    }*/

    //该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
    /*@Primary 
    @Bean(name = "weiBoDataSource")
    public DataSource weiBoDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("weiBo.jdbc.driverClassName"));
        props.put("url", env.getProperty("weiBo.jdbc.url"));
        props.put("username", env.getProperty("weiBo.jdbc.username"));
        props.put("password", env.getProperty("weiBo.jdbc.password"));
        return DruidDataSourceFactory.createDataSource(props);
    }
    
    @Bean(name = "payDataSource")
    public DataSource payDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("pay.jdbc.driverClassName"));
        props.put("url", env.getProperty("pay.jdbc.url"));
        props.put("username", env.getProperty("pay.jdbc.username"));
        props.put("password", env.getProperty("pay.jdbc.password"));
        return DruidDataSourceFactory.createDataSource(props);
    }*/

    /**
     * Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     * @param dbEventDataSource
     * @param weiBoDataSource
     * @param payDataSource
     * @return
     */
    /*@Bean(name = "dataSource")
    public DynamicDataSource dataSource(@Qualifier("dbEventDataSource") DataSource dbEventDataSource,
    									@Qualifier("payDataSource") DataSource payDataSource,
                                        @Qualifier("weiBoDataSource") DataSource weiBoDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseType.DBEVENT, dbEventDataSource);
        targetDataSources.put(DatabaseType.WEIBO, weiBoDataSource);
        targetDataSources.put(DatabaseType.PAY, payDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        // 该方法是AbstractRoutingDataSource的方法
        dataSource.setTargetDataSources(targetDataSources);
        // 默认的datasource设置为myTestDbDataSource
        dataSource.setDefaultTargetDataSource(weiBoDataSource);

        return dataSource;
    }*/

    @Bean
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {

		SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
		// 指定数据源(这个必须有，否则报错)
		fb.setDataSource(dataSource);
		// 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
		fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));// 指定基包
		fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));// 指定xml文件位置

		// 分页插件
		PageHelper pageHelper = new PageHelper();
		Properties props = new Properties();
		// 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页
        //禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
		props.setProperty("reasonable", "true");
		//指定数据库
		props.setProperty("dialect", "mysql");
		//支持通过Mapper接口参数来传递分页参数
		props.setProperty("supportMethodsArguments", "true");
		//总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page
		props.setProperty("returnPageInfo", "check");
		props.setProperty("params", "count=countSql");
		pageHelper.setProperties(props);
		// 添加插件
		fb.setPlugins(new Interceptor[] { pageHelper });

		try {
			return fb.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * 配置事务管理器
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /*
     * 事务管理器
    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }*/
}

