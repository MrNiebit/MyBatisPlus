package cn.lacknb;

import cn.lacknb.mapper.UserMapper;
import cn.lacknb.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.sql.Wrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {

        // 参数是一个wrapper，条件构造器，这里先不用，直接填null即可。
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }


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

    /**
     * 测试更新
     */
    @Test
    public void update () {
        User user = new User();
        user.setId(1282330408502951938L);
        user.setName("修改时间同步 Hello");
        // 通过条件自动拼接sql
        int row = userMapper.updateById(user);
        System.out.println(row);
    }


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
        User user = userMapper.selectById(1L);  // version 5
        user.setName("lock test 111");
        user.setEmail("asdasd@qqq.com");
        User user1 = userMapper.selectById(1L);  // version 5
        user1.setName("lock test 222");
        user1.setEmail("qqqq@sada.com");
        userMapper.updateById(user);  // 数据库中的version 改变为 6

        /*
        * 如果没有乐观锁，下面的插入 会 覆盖上面的插入。
        * 自旋锁 会多次尝试提交。
        * */

        userMapper.updateById(user1);  // 数据库中的version 不为5，无法更改。

    }


    /**
     * 批量查询
     * SELECT id,name,age,email,create_time,update_time,version FROM user WHERE id IN ( ? , ? , ? , ? )
     * selectBatch()
     */
    @Test
    public void select () {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3, 4));
        users.forEach(System.out::println);
    }


    /**
     * 条件查询 通过Map
     * SELECT id,name,age,email,create_time,update_time,version FROM user WHERE name = ?
     */
    @Test
    public void selectMany () {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "tom");
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }


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


    /**
     * 测试逻辑删除
     *  执行删除时 只是修改deleted的值 UPDATE user SET deleted=1 WHERE id=? AND deleted=0
     *  执行查询时  SELECT id,name,age,email,create_time,update_time,version,deleted FROM user WHERE deleted=0
     */
    @Test
    public void testLogic () {
//        userMapper.deleteById(1);
        userMapper.selectList(null).forEach(System.out::println);
    }


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

}
