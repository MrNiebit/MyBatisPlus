

# CRUD扩展

项目代码：

## 所需的依赖


```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.2</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>


        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.21</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>


        <!-- 这个 工具 不适合用在生产条件下。 -->
        <!-- https://mvnrepository.com/artifact/p6spy/p6spy -->
        <dependency>
            <groupId>p6spy</groupId>
            <artifactId>p6spy</artifactId>
            <version>3.8.7</version>
        </dependency>

        <!-- 代码生成器 -->

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.3.2</version>
        </dependency>

        <!--模板引擎-->
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
            <version>2.2</version>
        </dependency>

    </dependencies>
```

## 创建mapper


```java

package cn.lacknb.mapper;

import cn.lacknb.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;


/**
 * @Mapper 注解，如果启动类没有添加MapperScan 扫描对应的包，可以添加这个注解。如果不想用这个注解，就在启动类上添加MapperScan
 *          不建议使用这个注解，如果有多个mapper，就很麻烦了。直接在启动类中使用MapperScan("cn.lacknb.mapper")
 * @Repository 这个注解，是将该类放入容器中，可以避免 使用@Autowired 时，idea不提示报错。
 */
@Repository
public interface UserMapper extends BaseMapper<User> {



}

```

## 插入

```java
    /**
     * 测试插入
     */
    @Test
    public void insert () {
        User user = new User();
        user.setName("hello");
        user.setAge(111);
        user.setEmail("12312@qq.com");
        int result = userMapper.insert(user);
        System.out.println(user);
        System.out.println(result);
    }
```


这里没有 设置id，通过日志可以看到 mybatis-plus会自动生成一个id

```
Creating a new SqlSession
SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3a1b36a1] was not registered for synchronization because synchronization is not active
JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@739831a4] will not be managed by Spring
==>  Preparing: INSERT INTO user ( id, name, age, email ) VALUES ( ?, ?, ?, ? ) 
==> Parameters: 1282194649338966017(Long), hello(String), 111(Integer), 12312@qq.com(String)
<==    Updates: 1
Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3a1b36a1]
User(id=1282194649338966017, name=hello, age=111, email=12312@qq.com)
```

生成的id值：全局的唯一id
数据库中的主键 （UUID, 自增id，雪花算法，redis，zookeeper）   

### 主键生成策略
分布式系统唯一id生成

**雪花算法：**

snowflake是Twitter开源的分布式ID生成算法，结果是一个long型的ID。且核心思想是：使用41bit作为毫秒数，
10 bit作为机器的ID（5bit是数据中心，5bit的机器ID），12bit作为毫秒内的流水号（意味着每个节点在每毫秒可
以产生4096个ID），最后还有一个符号位，永远是0。

可以保证几乎全球唯一!

默认是IdType.ID_WORKER
```java
@Data
public class User {

    @TableId(type = IdType.ID_WORKER)
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

**配置主键id自增：1. 数据库要设置自增。2. 实体类要设置`IdType.AUTO`**
```java
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

其中IdType的其他参数
```java
public enum IdType {
    AUTO(0),  // 数据库id自增
    NONE(1),  // 未设置主键
    INPUT(2),  // 手动输入
    ASSIGN_ID(3),   // 全局唯一id，雪花算法，3.3.0版本之后引入。  为Long整型
    ASSIGN_UUID(4),  // 随机UUID，不带下划线  352793d17819ab76909789061cd2ab89  为字符串类型
    /** @deprecated */
    @Deprecated
    ID_WORKER(3),  // 默认的全局唯一id, 雪花算法    3.3.0版本之后 舍弃
    /** @deprecated */
    @Deprecated
    ID_WORKER_STR(3), 
    /** @deprecated */
    @Deprecated
    UUID(4);  // ID_WORDER字符串表示法

}
```


## 更新操作

```java
    /**
     * 测试更新
     */
    @Test
    public void update () {
        User user = new User();
        user.setId(1282300585567182850L);
        user.setName("更新 测试");
        // 通过条件自动拼接sql
        int row = userMapper.updateById(user);
        System.out.println(row);
    }
```


## 自动填充

创建时间、修改时间！这些个操作 都是自动化完成的，我们不希望手动更新
阿里巴巴开发手册：所有的数据库表：gmt_create、gmt_modified 几乎所有的表都要配置上！而且需要自动化

实现方式

> 方式一：数据库级别
1. 在表中新增字段 create_time, update_time
2. 设置表中字段进行 新增数据，自动添加 创建时间和更新时间。还有 每当更新数据时，自动改变更新时间为当前时间戳
相关sql
- 创建字段时，需要写的sql
```sql
alter table user add create_time datetime default current_timestamp null;

```

- 修改字段 时 的sql
```sql
alter table user modify update_time datetime default CURRENT_TIMESTAMP on update current_timestamp null;

```

> 方式二：代码级别

1. 删除数据库的默认值、更新操作
2. 给实体类中的属性使用注解 @TableField
```java

    /*
    * 字段添加填充内容
    * */

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /*
    * 字段添加、修改时 填充内容。
    * */

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
```
3. 编写处理器来处理这个注解即可。需要实现MetaObjectHandler这个类，实现 插入 和  修改时 这两个方法。
```java
package cn.lacknb.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时 填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入数据时，填充策略");
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 更新时 填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("修改数据时 的填充策略");
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}

```

## 乐观锁处理理解。
> 乐观锁：顾名思义十分乐观，它总是认为不会出现问题，无论干什么都不去上锁！如果出现问了问题，再次更新值测试
> 悲观锁：顾名思义十分悲观，它总是认为会出问题，无论干什么都会上锁，再去进行操作。

当要更新一条记录的时候，希望这条记录没有被别人更新


乐观锁的实现机制：
- 取出记录时，获取当前的version
- 更新时，带上这个version
- 执行更新时，set version = newVersion where version = oldVersion
- 如果version不对，就会更新失败

```sql
-- A线程
update user set name = "kuangshen", version = version + 1 where id = 2 and version = 1

-- B线程 抢先完成，这个时候version = 2，会导致A修改失败。
update user set name = "kuangshen", version = version + 1 where id = 2 and version = 1
```

### MyBatisPlus的乐观锁插件

1. 给数据库中增加version字段。且默认 值 为 1

2. 我们实体类加对应的字段。并加上version注解
```java
    @Version
    private Integer version;  // 乐观锁的注解
```

3. 注册组件
```java
package cn.lacknb.config;


import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

}
 
```

测试一下乐观锁

```java
    /**
     * 乐观锁 测试 111
     */
    @Test
    public void testLock () {
        // 查询用户信息
        User user = userMapper.selectById(1L);
        // 修改用户信息
        user.setName("lock test ");
        user.setEmail("asdasd@qqq.com");
        // 执行更新操作
        userMapper.updateById(user);
    }

    /**
     * 乐观锁 测试 222
     * 结果：第二次插入 失败。第一次插入成功
     */
    @Test
    public void testLock2 () {
        User user = userMapper.selectById(1L);
        user.setName("lock test 111");
        user.setEmail("asdasd@qqq.com");
        User user1 = userMapper.selectById(1L);;
        user1.setName("lock test 222");
        user1.setEmail("qqqq@sada.com");
        userMapper.updateById(user);

        /*
        * 如果没有乐观锁，下面的插入 会 覆盖上面的插入。
        * 自旋锁 会多次尝试提交。
        * */

        userMapper.updateById(user1);

    }
```


## 查询操作

分页在网站是哟个的十分之多

1. 原始的limit进行分页
2. pageHelper第三方插件
3. MyBatisPlus其实内置了分页插件

> 如何使用

1. 配置拦截器组件。

 ```java
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
```

分页测试

```java
    /**
     * 分页查询
     */
    @Test
    public void selectPage () {
        // 第1页，每页显示5个
        Page<User> userPage = new Page<>(1, 5);
        userMapper.selectPage(userPage, null);
        userPage.getRecords().forEach(System.out::println);
    }

```


## 删除操作

```java
    /**
     * 删除操作
     */
    @Test
    public void deleteById () {
        // 单个删除
//        userMapper.deleteById(2);

        /*
        * 批量删除
        * DELETE FROM user WHERE id IN ( ? , ? )
         * */

//        userMapper.deleteBatchIds(Arrays.asList(4, 5));

        /*
        * 通过条件删除
        * DELETE FROM user WHERE name = ?
        * */

        Map<String, Object> map = new HashMap<>();
        map.put("name", "hello");
        userMapper.deleteByMap(map);
    }
```

## 逻辑删除

> 物理删除：从数据库中直接删除
> 逻辑删除：在数据库中没有被移除，而是通过一个变量来让他失效！ delete = 0 => deleted = 1

管理员可以查看被删除的记录！防止数据的丢失，类似于回收站。

### 测试一下

1. 在数据库表中增加一个`deleted`字段
2. 实体类中增加属性
```java
   /*
   * 逻辑删除
   * */

    @TableLogic
    private Integer deleted;
```
3. 配置application.yml
```yml
mybatis-plus:
  # 配置逻辑删除
  global-config:
    db-config:
      logic-delete-value: 1  # 对于已经删除的的值为1
      logic-not-delete-value: 0  # 没删除的 值为0
```
> 对于旧版本的MyBatisPlus 需要配置 注册 逻辑删除插件LogicSqlInjecctor，新版只需要配置上述步骤即可。


## 性能分析插件

作用：
- 性能分析插件，用于输出每条SQL语句及其执行时间


我们在平时的开发中，会遇到一些慢sql，测试！druid。。
MyBatisPlus也提供性能分析插件，如果超过这个时间就停止运行。

1. 导入插件
2. 测试使用

### 新版本将这个插件启用了，可以通过p6spyl来查看详细的sql日志以及执行效率

参考官网介绍：[https://mp.baomidou.com/guide/p6spy.html#%E6%89%A7%E8%A1%8C-sql-%E5%88%86%E6%9E%90%E6%89%93%E5%8D%B0](https://mp.baomidou.com/guide/p6spy.html#%E6%89%A7%E8%A1%8C-sql-%E5%88%86%E6%9E%90%E6%89%93%E5%8D%B0)
这个工具不适合在 生产环境下使用，只适合开发环境中使用，便于查看sql语句的执行效率

1. 导入依赖
```xml
<!-- 这个 工具 不适合用在生产条件下。 -->
<!-- https://mvnrepository.com/artifact/p6spy/p6spy -->
<dependency>
    <groupId>p6spy</groupId>
    <artifactId>p6spy</artifactId>
    <version>3.8.7</version>
</dependency>

2. 创建spy.properties文件
```properties
#3.2.1以上使用
modulelist=com.baomidou.mybatisplus.extension.p6spy.MybatisPlusLogFactory,com.p6spy.engine.outage.P6OutageFactory
#3.2.1以下使用或者不配置
#modulelist=com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory
# 自定义日志打印
logMessageFormat=com.baomidou.mybatisplus.extension.p6spy.P6SpyLogger
#日志输出到控制台
appender=com.baomidou.mybatisplus.extension.p6spy.StdoutLogger
# 使用日志系统记录 sql
#appender=com.p6spy.engine.spy.appender.Slf4JLogger
# 设置 p6spy driver 代理
deregisterdrivers=true
# 取消JDBC URL前缀
useprefix=true
# 配置记录 Log 例外,可去掉的结果集有error,info,batch,debug,statement,commit,rollback,result,resultset.
excludecategories=info,debug,result,commit,resultset
# 日期格式
dateformat=yyyy-MM-dd HH:mm:ss
# 实际驱动可多个
#driverlist=org.h2.Driver
# 是否开启慢SQL记录
outagedetection=true

# 慢SQL记录标准 2 秒
outagedetectioninterval=2
```

3. 在主配置文件中配置数据源
```yml
spring:
  datasource:
#    schema: classpath:schema.sql
#    data: classpath:data.sql
    type: com.alibaba.druid.pool.DruidDataSource
#    url: jdbc:mysql:///mybatisplus?useSSL=false&CharacterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT

    driver-class-name: com.p6spy.engine.spy.P6SpyDriver
    url: jdbc:p6spy:mysql:///mybatisplus?useSSL=false&CharacterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT
    username: root
    password: 123456
#    driver-class-name: com.mysql.cj.jdbc.Driver
```
最后直接使用 看看效果。

## 条件构造器

写一些复杂的sql可以使用Wrapper来替代。

```java
    /**
     * 使用Wrapper 条件查询器
     */
    @Test
    public void wrapperTest () {

        /*
        * SELECT id,name,age,email,create_time,update_time,version,deleted
        * FROM user WHERE deleted=0 AND (name IS NOT NULL AND age >= 28)
        * ge 是大于等于，le 是小于等于
        * */

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("name");
        wrapper.ge("age", 28);
        userMapper.selectList(wrapper).forEach(System.out::println);

    }
```

## 代码自动生成器


AutoGenerator 是 MyBatis-Plus 的代码生成器，通过 AutoGenerator 
可以快速生成 Entity、Mapper、Mapper XML、Service、Controller 等各个模块的代码，极大的提升了开发效率。

### 添加依赖
MyBatis-Plus 从 3.0.3 之后移除了代码生成器与模板引擎的默认依赖，需要手动添加相关依赖：

添加 代码生成器 依赖
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.3.2</version>
</dependency>

<!--模板引擎-->
<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity-engine-core</artifactId>
    <version>2.2</version>
</dependency>
```

其他依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

测试类

```java
package cn.lacknb;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

public class MyBatisGenerator {

    private static final String MODULE_NAME = "\\01-mybatisplus-demo";

    public static void main(String[] args) {
        // 需要构建一个代码自动生成器 对象
        AutoGenerator generator = new AutoGenerator();
        // 配置策略

        // 1. 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        // 获取项目目录
        String projectPath = System.getProperty("user.dir");
        System.out.println("project: " + projectPath);
        globalConfig.setOutputDir(projectPath + MODULE_NAME + "\\src\\main\\java\\");
        globalConfig.setAuthor("gitsilence");
        // 是否自动打开文件目录
        globalConfig.setOpen(false);
        // 是否覆盖
        globalConfig.setFileOverride(false);
        globalConfig.setServiceName("%sService");
        // 设置主键自增
        globalConfig.setIdType(IdType.ASSIGN_ID);
        globalConfig.setSwagger2(true);
        // 自动配置swagger文档
        globalConfig.setDateType(DateType.ONLY_DATE);
        generator.setGlobalConfig(globalConfig);

        // 2. 设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql:///mybatisplus?useSSL=false&CharacterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("niebaohua");
        dsc.setDbType(DbType.MYSQL);
        generator.setDataSource(dsc);

        // 3. 包的配置
        PackageConfig pc = new PackageConfig();
        // 设置包模块
        pc.setModuleName("test");
        pc.setParent("cn.lacknb");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");

        generator.setPackageInfo(pc);

        // 4. 策略配置

        StrategyConfig sc = new StrategyConfig();
        // 设置映射的表
        sc.setInclude("user");
        // 表名
        sc.setNaming(NamingStrategy.underline_to_camel);
        // 字段名
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
//        sc.setSuperEntityClass()
        // 是否开启 lombok
        sc.setEntityLombokModel(true);

        // 逻辑删除
        sc.setLogicDeleteFieldName("deleted");
        // 设置乐观锁
        sc.setVersionFieldName("version");
        // 自动填充配置
        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
        TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        tableFills.add(gmtModified);
        sc.setTableFillList(tableFills);

        // 开启 RestController驼峰命名
        sc.setRestControllerStyle(true);
        // controller中的字段 下划线。
        sc.setControllerMappingHyphenStyle(true);

        generator.setStrategy(sc);

        generator.execute();

    }

}

```