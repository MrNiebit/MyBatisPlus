package cn.lacknb.config;


import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


/**
 * 注册组件
 * 开启事务注解 也可以加在这
 * 扫描包也可以加在这
 */
@Configuration
@MapperScan("cn.lacknb.mapper")
@EnableTransactionManagement
public class MyBatisPlusConfig {


    /**
     * 乐观锁插件
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor () {
        return new OptimisticLockerInterceptor();
    }


    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor () {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作，true调回首页、false继续请求 默认为false
        paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认500条， -1 代表不受限制
        paginationInterceptor.setLimit(2);
        // 开启count的join优化，只针对部分left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }



}
