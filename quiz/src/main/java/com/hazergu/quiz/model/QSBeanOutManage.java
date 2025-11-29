package com.hazergu.quiz.model;

import java.util.List;

public class QSBeanOutManage {

    private Integer id;
    private String questionText;
    private List<String> options;
    private String answer;

    //添加无参构造函数，否则 Tools.java 中 new QSBeanOutManage() 会报错
    public QSBeanOutManage() {
    }

    @Override
    public String toString() {
        return "QSBeanOutManage{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", options=" + options +
                ", answer='" + answer + '\'' +
                '}';
    }

    public QSBeanOutManage(Integer id, String questionText, List<String> options, String answer) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}