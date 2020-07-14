package cn.lacknb.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class User {


    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Integer age;
    private String email;

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

    /*
    * @Version 乐观锁Version注解
    * */

    @Version
    private Integer version;


   /*
   * 逻辑删除
   * */

    @TableLogic
    private Integer deleted;
}