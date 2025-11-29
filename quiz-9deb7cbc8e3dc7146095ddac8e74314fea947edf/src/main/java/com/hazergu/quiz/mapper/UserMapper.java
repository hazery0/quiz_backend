package com.hazergu.quiz.mapper;

import com.hazergu.quiz.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    public List<User> list();

    @Insert("insert into user(userName, userPassword,isDelete,userRole,createTime, updateTime)" +
            "values(#{userName}, #{userPassword}, #{isDelete}, #{userRole}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int saveUser(User user);

    @Select("SELECT COUNT(*) FROM user WHERE userName = #{username} AND isDelete = 0")
    public int existsByName(@Param("username") String username);

    //soft delete
    //根据id删除用户；
    @Update("UPDATE user SET isDelete = 1, updateTime = NOW() WHERE id = #{id} AND isDelete = 0")
    public int deleteUserById(@Param("id") Long id);

    //根据username删除用户；
    @Update("UPDATE user SET isDelete = 1, updateTime = NOW() WHERE userName = #{username} AND isDelete = 0")
    public int deleteByUsername(@Param("username") String username);

    //hard delete
    @Delete("DELETE FROM user WHERE id = #{id}")
    public int hardDeleteUserById(@Param("id") Long id);

    @Delete("DELETE FROM user WHERE userName = #{username}")
    public int hardDeleteByUsername(@Param("username") String username);

    //search by page
    @Select("SELECT COUNT(*) FROM user WHERE isDelete=0")
    public int count();

    @Select("SELECT * FROM user WHERE isDelete=0 limit #{start},#{pageSize}")
    public List<User> page(Integer start, Integer pageSize);

    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0")
    List<User> findByName(String keyword);

    @Select("select * from user where userName=#{username} AND userPassword=#{password}")
    public User getByNameAndPassword(String username, String password);
}
