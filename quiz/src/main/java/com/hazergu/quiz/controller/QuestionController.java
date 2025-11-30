package com.hazergu.quiz.controller;

import com.hazergu.quiz.model.*;
import com.hazergu.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/addQuestion")
    public Result addQuestion(@RequestBody QSBean question) {
        try {
            // 详细的调试信息
            System.out.println("=== 调试信息：接收到的 QSBean 对象 ===");
            System.out.println("Question 对象是否为 null: " + (question == null));
            if (question != null) {
                System.out.println("question 字段: " + question.getQuestion());
                System.out.println("optiona 字段: " + question.getOptiona());
                System.out.println("optionb 字段: " + question.getOptionb());
                System.out.println("optionc 字段: " + question.getOptionc());
                System.out.println("optiond 字段: " + question.getOptiond());
                System.out.println("answer 字段: " + question.getAnswer());
                System.out.println("answer 字段是否为 null: " + (question.getAnswer() == null));
            }
            System.out.println("===================================");

            int result = questionService.insertQuestion(question);
            if (result > 0){
                return Result.success("插入新问题成功");
            }else{
                return Result.error(0, "插入失败");
            }
        } catch (IllegalArgumentException e) {
            return Result.error(0, "参数错误: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(0, "服务器内部错误: " + e.getMessage());
        }
    }

    @GetMapping("/delQuestion")
    public Result deleteQuestion(Integer id) {
        boolean success = questionService.delQuestion(id);
        if (success) {
            return Result.success("用户成功删除");
        }
        return Result.error(0, "用户不存在或已被删除");
    }

    @CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
    @GetMapping("/getQuestion")
    public Result getQuestion() {
        List<QSBeanOut> qsBeanOut = questionService.getQuestions();
        return Result.success(qsBeanOut);
    }

    @GetMapping("/questions")
    public Result getPage(@RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="5")Integer pageSize){
        QSBeanPage qsBeanPage=questionService.page(page, pageSize);
        return Result.success(qsBeanPage);
    }

    @GetMapping("/findQuestion")
    public Result getUser(@RequestParam(required = false) String keyword){
        List<QSBeanOutManage> qsBeanOut=questionService.findByName(keyword);
        return Result.success(qsBeanOut);
    }
}
