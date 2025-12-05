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

    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> registerData) {
        String username = registerData.get("username");
        String password = registerData.get("password");
        String checkpassword = registerData.getOrDefault("checkpassword", registerData.get("confirmPassword"));
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

    @PostMapping("/addUser")
    public Result addUser(@RequestBody Map<String, Object> userData) {
        String username = (String) userData.get("username");
        String password = (String) userData.get("password");
        Integer userRole = (Integer) userData.get("userRole");
        
        Result result = userService.addUser(username, password, userRole);
        return result;
    }

    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody Map<String, Object> resetData) {
        Long userId = Long.valueOf(resetData.get("userId").toString());
        Result result = userService.resetPassword(userId);
        return result;
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
            claims.put("userRole", userResult.getUserRole());

            String token = JwtUtil.generateTokenWithClaims(claims);
            Result result = Result.success("用户登录成功");
            result.setData(token);
            return result;
        }else{
            return Result.error(0,"用户登录失败");
        }
    }

    @PostMapping("/loginAdmin")
    public Result loginAdmin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (StringUtils.isAnyBlank(username, password)) {
            return Result.error(0,"用户名或密码为空");
        }
        User userResult = userService.login(username, password);
        if(userResult!=null){
            // 检查是否为管理员
            if(userResult.getUserRole() != 1) {
                return Result.error(0,"只有管理员才能登录管理端");
            }
            Claims claims = Jwts.claims();
            claims.put("id", userResult.getId());
            claims.put("username", userResult.getUserName());
            claims.put("userRole", userResult.getUserRole());

            String token = JwtUtil.generateTokenWithClaims(claims);
            Result result = Result.success("管理员登录成功");
            result.setData(token);
            return result;
        }else{
            return Result.error(0,"管理员登录失败");
        }
    }

    @PostMapping("/toggleAdmin")
    public Result toggleAdmin(@RequestBody Map<String, Object> toggleData) {
        Long userId = null;
        try {
            userId = Long.valueOf(toggleData.get("id").toString());
        } catch (Exception e) {
            return Result.error(0, "用户ID格式错误");
        }

        Boolean isAdmin = (Boolean) toggleData.get("isAdmin");
        if (isAdmin == null) {
            return Result.error(0, "isAdmin参数不能为空");
        }

        Integer userRole = isAdmin ? 1 : 0;  // true->管理员(1), false->普通用户(0)

        Result result = userService.toggleAdmin(userId, userRole);
        return result;
    }
}
