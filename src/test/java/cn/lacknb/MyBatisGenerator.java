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
        dsc.setPassword("123456");
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
