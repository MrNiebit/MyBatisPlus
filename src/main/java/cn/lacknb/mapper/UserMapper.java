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
