package cn.lacknb.test.service.impl;

import cn.lacknb.test.entity.User;
import cn.lacknb.test.mapper.UserMapper;
import cn.lacknb.test.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gitsilence
 * @since 2020-07-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
