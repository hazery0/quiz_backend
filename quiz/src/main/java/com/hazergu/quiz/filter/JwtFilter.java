package com.hazergu.quiz.filter;

import com.alibaba.fastjson.JSONObject;
import com.hazergu.quiz.model.Result;
import com.hazergu.quiz.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class JwtFilter implements Filter {

    //初始化，只调用一次；
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("JwtFilter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("JwtFilter拦截到请求");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        //1、获取请求url;
        String url = request.getRequestURL().toString();

        //2、判断url中是否包含需要放行的路径（登录、注册等）
        if (url.contains("login") || url.contains("register") || url.contains("Register")) {
            System.out.println("放行请求: " + url);
            chain.doFilter(request, response);
            return;
        }

        //3、获取请求头中的令牌(token)
        String token = request.getHeader("token");
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasLength(token) && StringUtils.hasLength(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        //4、判断令牌是否存在，如果不存在，返回未登录信息。
        if (!StringUtils.hasLength(token)) {
            System.out.println("未找到token，返回未登录");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Result result = Result.error(0, "NOT_LOGIN");
            String noLogin = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(noLogin);
            return;
        }

        //5、解析token，如果解析失败，返回未登录信息。
        try {
            // 解析token，获取Claims
            io.jsonwebtoken.Claims claims = JwtUtil.parseTokenReturnClaims(token);
            System.out.println("token验证成功");
            
            // 获取用户角色
            Integer userRole = (Integer) claims.get("userRole");
            System.out.println("用户角色: " + userRole);
            
            // 判断是否需要管理员权限
            if (url.contains("/addUser") || url.contains("/resetPassword") || 
                url.contains("/deleteById") || url.contains("/deleteByName") ||
                url.contains("/users") || url.contains("/findUser")) {
                // 管理端API，需要管理员权限
                if (userRole == null || userRole != 1) {
                    System.out.println("没有管理员权限，拒绝访问");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    Result result = Result.error(0, "NO_ADMIN_PERMISSION");
                    String noPermission = JSONObject.toJSONString(result);
                    response.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write(noPermission);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("token解析失败");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Result result = Result.error(0, "NOT_LOGIN");
            String noLogin = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(noLogin);
            return;
        }

        //6、满足所有条件，放行。
        chain.doFilter(req, res);
    }
}
