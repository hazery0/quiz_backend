package com.hazergu.quiz.service;

import com.hazergu.quiz.model.*;

import java.util.List;

public interface QuestionService {
    //insert
    public int insertQuestion(QSBean qsBean);
    //delete
    public boolean delQuestion(Integer id);
    //get 5 new question
    public List<QSBeanOut> getQuestions();
    //
    public QSBeanPage page(Integer page, Integer pageSize);
    //
    public List<QSBeanOutManage> findByName(String keyword);
    
    //update question
    public int updateQuestion(QSBean qsBean);

}