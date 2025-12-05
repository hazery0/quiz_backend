package com.hazergu.quiz.service.impl;

import com.hazergu.quiz.mapper.UserMapper;
import com.hazergu.quiz.model.PageBean;
import com.hazergu.quiz.model.Result;
import com.hazergu.quiz.model.User;
import com.hazergu.quiz.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public Result saveUser(String username, String password, String checkpassword) {
        if(StringUtils.isAnyBlank(username, password, checkpassword)){
            return Result.error(null, "用户名或密码为空");
        }

        if (!password.equals(checkpassword)) {
            return Result.error(null, "两次输入的密码不一致");
        }

        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if(!matcher.matches()){
            return Result.error(null, "用户名包含特殊字符");
        }

        //check if username exist
        int userExist = userMapper.existsByName(username);
        if (userExist > 0) {
            return Result.error(0, "Username already exist");
        }

        final String SALT = "com.quiz";
        String encrptedPassword = DigestUtils.md5DigestAsHex((SALT+password).getBytes());

        User user = new User();
        user.setUserName(username);
        user.setUserPassword(encrptedPassword);
        /**
         * 注册默认为普通用户，userRole为0
         */
        user.setUserRole(0);
        user.setIsDelete(0);

        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        //4.插入到数据库；
        int result = userMapper.saveUser(user);

        if (result > 0)
            return Result.success("新增用户成功");
        else
            return Result.error(null, "注册用户失败");

    }

    @Override
    public boolean deleteUserById(Long id) {
        int result = userMapper.deleteUserById(id);
        return result > 0;
    }

    @Override
    public boolean deleteUserByName(String username) {
        int result = userMapper.deleteByUsername(username);
        return result > 0;
    }

    @Override
    public PageBean page(Integer page, Integer pageSize){
        //获取总的记录数；
        Integer total=userMapper.count();

        //获取分页查询结果列表；
        Integer start = (page-1)*pageSize;
        List<User> userList=userMapper.page(start, pageSize);

        //封装PageBean对象；
        PageBean pageBean = new PageBean(0,null);
        pageBean.setTotal(total);
        pageBean.setRow(userList);

        return pageBean;
    }

    @Override
    public List<User> findByName(String keyword){
        List<User> userList=userMapper.findByName(keyword);
        for(User user:userList){
            user.setUserPassword("*******");
        }
        return userList;
    }

    @Override
    public User login(String username, String password){
        //对密码进行加密;
        final String SALT = "com.quiz";
        String encrptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        return userMapper.getByNameAndPassword(username,encrptedPassword);
    }

    @Override
    public Result addUser(String username, String password, Integer userRole) {
        if(StringUtils.isAnyBlank(username, password)) {
            return Result.error(null, "用户名或密码为空");
        }

        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if(!matcher.matches()) {
            return Result.error(null, "用户名包含特殊字符");
        }

        //check if username exist
        int userExist = userMapper.existsByName(username);
        if (userExist > 0) {
            return Result.error(0, "Username already exist");
        }

        final String SALT = "com.quiz";
        String encrptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        User user = new User();
        user.setUserName(username);
        user.setUserPassword(encrptedPassword);
        user.setUserRole(userRole); // 0-普通用户，1-管理员
        user.setIsDelete(0);

        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        //插入到数据库
        int result = userMapper.saveUser(user);

        if (result > 0)
            return Result.success("新增用户成功");
        else
            return Result.error(null, "新增用户失败");
    }

    @Override
    public Result resetPassword(Long userId) {
        // 重置密码为123456
        final String SALT = "com.quiz";
        String defaultPassword = "123456";
        String encrptedPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        
        int result = userMapper.resetPassword(userId, encrptedPassword);
        
        if (result > 0) {
            return Result.success("密码重置成功");
        } else {
            return Result.error(null, "密码重置失败，用户不存在或已被删除");
        }
    }

    // UserServiceImpl.java
    @Override
    public Result toggleAdmin(Long userId, Integer userRole) {
        if (userId == null) {
            return Result.error(null, "用户ID不能为空");
        }

        if (userRole == null || (userRole != 0 && userRole != 1)) {
            return Result.error(null, "用户角色参数错误");
        }

        // 检查用户是否存在
        User user = userMapper.getById(userId);
        if (user == null) {
            return Result.error(null, "用户不存在或已被删除");
        }

        // 更新用户角色
        int result = userMapper.updateUserRole(userId, userRole);

        if (result > 0) {
            String roleName = userRole == 1 ? "管理员" : "普通用户";
            return Result.success("用户角色已更新为" + roleName);
        } else {
            return Result.error(null, "更新用户角色失败");
        }
    }
}
