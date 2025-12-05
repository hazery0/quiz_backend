package com.hazergu.quiz.service;

import com.hazergu.quiz.model.PageBean;
import com.hazergu.quiz.model.Result;
import com.hazergu.quiz.model.User;

import java.util.List;

public interface UserService {

    //add user to mySQL
    public Result saveUser(String username, String password, String checkpassword);
    //delete user by id
    public boolean deleteUserById(Long id);
    //delete user by username
    public boolean deleteUserByName(String username);
    //search user by page
    public PageBean page(Integer page, Integer pageSize);
    //search user by keywords
    public List<User> findByName(String keyword);
    //login
    public User login(String username, String password);
    
    //add user by admin
    public Result addUser(String username, String password, Integer userRole);
    
    //reset user password
    public Result resetPassword(Long userId);

    //set Admin
    public Result toggleAdmin(Long userId, Integer userRole);
}