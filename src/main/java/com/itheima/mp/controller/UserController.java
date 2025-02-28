package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("用户管理接口")
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    // 推荐使用构造函数注入，不推荐使用 @Autowired
    // lombok 中 @RequiredArgsConstructor 可以帮助我们创建构造函数
    private final IUserService userService;

    @ApiOperation("新增用户接口")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userDTO){
        // 1 把 DTO 对象拷贝到 PO
        User user = BeanUtil.copyProperties(userDTO, User.class);
        // 2 执行新增操作
        userService.save(user);
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUserById(@ApiParam("用户id") @PathVariable("id") Long id){
        userService.removeById(id);
    }

    @ApiOperation("根据id查询用户接口")
    @GetMapping("{id}")
    public UserVO queryUserById(@ApiParam("用户id") @PathVariable("id") Long id){
        // 1 查询用户 PO
        User user = userService.getById(id);
        // 2 把 PO 拷贝到 VO
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @ApiOperation("根据id批量查询用户接口")
    @GetMapping
    public List<UserVO> queryUserByIds(@ApiParam("用户id集合") @RequestParam("ids") List<Long> ids){
        // 1 查询用户 PO
        List<User> users = userService.listByIds(ids);
        // 2 把 PO 拷贝到 VO
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @ApiOperation("扣减用户余额")
    @GetMapping("/{id}/deduction/{money}")
    public void deductBalance(
            @ApiParam("用户id") @PathVariable("id") Long id,
            @ApiParam("扣减的金额") @PathVariable("money") Integer money){
        userService.deductBalance(id, money);
    }


    @ApiOperation("自定义复杂查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUserByIds(UserQuery query){
        // 1 查询用户 PO
        List<User> users = userService.queryUsers(
                query.getName(), query.getStatus(), query.getMaxBalance(), query.getMinBalance());
        // 2 把 PO 拷贝到 VO
        return BeanUtil.copyToList(users, UserVO.class);
    }
}
