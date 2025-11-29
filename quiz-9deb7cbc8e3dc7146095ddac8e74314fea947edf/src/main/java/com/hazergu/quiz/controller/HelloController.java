package com.hazergu.quiz.controller;

import com.hazergu.quiz.model.Result;
import com.hazergu.quiz.model.SimpleUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
//    public String hello() {
//        System.out.println("hello");
//        return "Hello World";
//    }
    public Result hello(){
        System.out.println("hello");
        return Result.success();
    }

    @RequestMapping("/simpleParam")
//    public String getParam(String name, Integer age) {
//        System.out.println(name + ":" + age);
//        return "ok";
//   }
    public Result simpleParam(String name, Integer age){
        System.out.println("name:"+name+",age:"+age);
        return Result.success("simpleParam");
    }

   @RequestMapping("/simpleUser")
//    public Object getUser(SimpleUser user) {
//        System.out.println(user);
//        SimpleUser simpleUser = new SimpleUser();
//        simpleUser.setAge(user.getAge());
//        simpleUser.setName(user.getName());
//        return simpleUser;
//   }
    public Result getUser(SimpleUser user){
       SimpleUser simpleUser = new SimpleUser();
       simpleUser.setAge(user.getAge());
       simpleUser.setName(user.getName());
       return Result.success(simpleUser);
   }
}
