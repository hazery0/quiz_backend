package com.hazergu.quiz.controller;

import com.hazergu.quiz.mapper.UserMapper;
import com.hazergu.quiz.model.PageBean;
import com.hazergu.quiz.model.Result;
import com.hazergu.quiz.model.User;
import com.hazergu.quiz.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import com.hazergu.quiz.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public Result register(String username, String password, String checkpassword) {
        Result result = userService.saveUser(username, password, checkpassword);
        return result;
    }

    @GetMapping("/deleteById")
    public Result deleteUserById(Long id) {
        boolean success = userService.deleteUserById(id);
        if (success) {
            return Result.success("用户已删除");
        }
        return Result.error(0, "用户不存在或已被删除");
    }

    @GetMapping("/deleteByName")
    public Result deleteUser(String username) {
        boolean success = userService.deleteUserByName(username);
        if (success) {
            return Result.success("用户已删除");
        }
        return Result.error(0, "用户不存在或已被删除");
    }

    @GetMapping("/users")
    public Result getPage(@RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="5")Integer pageSize){
        PageBean pageBean=userService.page(page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/findUser")
    public Result getUser(String keyword){
        List<User> users=userService.findByName(keyword);
        return Result.success(users);
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> loginData){

        String username = loginData.get("username");
        String password = loginData.get("password");

        if (StringUtils.isAnyBlank(username, password)) {
            return Result.error(0,"用户名或密码为空");
        }
        User userResult = userService.login(username, password);
        if(userResult!=null){
            Claims claims = Jwts.claims();
            claims.put("id", userResult.getId());
            claims.put("username", userResult.getUserName());

            String token = JwtUtil.generateTokenWithClaims(claims);
            Result result = Result.success("用户登录成功");
            result.setData(token);
            return result;
        }else{
            return Result.error(0,"用户登录失败");
        }
    }
}
