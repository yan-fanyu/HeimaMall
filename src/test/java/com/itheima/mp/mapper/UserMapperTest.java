package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Update;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        //user.setId(5L);
        user.setUsername("Hellen");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(1000);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testQueryByIds2() {
        List<User> users = userMapper.queryUserByIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }

    // 使用 QueryWrapper 进行条件查询
    @Test
    void testQuerySelectUser() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 使用 UpdateWrapper 进行条件更新
    @Test
    void testUpdateWrapper() {
        // 1 更新的数据
        User user = new User();
        user.setBalance(2000);
        // 2 更新的条件
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>().eq("username", "jack");
        // 3 执行更新
        userMapper.update(user, updateWrapper);
    }

    // 案例三 更新多个用户的余额减200
    @Test
    void testUpdateWrapper2(){
        List<Long> ids = List.of(1L, 2L, 4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200")
                .in("id", ids);
        userMapper.update(null, wrapper);
    }

    // 推荐使用 Lambda
    @Test
    void testLambdaQueryWrapper(){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 使用自定义 SQl
    @Test
    void testCustomerSqlUpdate(){
        List<Long> ids = List.of(1L, 2L, 4L);
        int amount = 200;
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>().in(User::getId, ids);
        // UpdateWrapper<User> wrapper1 = new UpdateWrapper<User>().in(ids);
        userMapper.updateBalanceByIds(wrapper, amount);
    }
}













