package com.hazergu.quiz.service.impl;

import com.hazergu.quiz.mapper.QuestionMapper;
import com.hazergu.quiz.mapper.UserMapper;
import com.hazergu.quiz.model.*;
import com.hazergu.quiz.service.QuestionService;
import com.hazergu.quiz.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int insertQuestion(QSBean qsBean) {
        // 添加详细的空值检查
        if (qsBean == null) {
            throw new IllegalArgumentException("Question data cannot be null");
        }

        String ans = qsBean.getAnswer();
        // 详细的空值和空字符串检查
        if (ans == null) {
            throw new IllegalArgumentException("Answer cannot be null");
        }

        if (ans.trim().isEmpty()) {
            throw new IllegalArgumentException("Answer cannot be empty");
        }

        // 现在可以安全地调用 toLowerCase()
        String lowerAns = ans.toLowerCase();
        if (!List.of("a", "b", "c", "d").contains(lowerAns)) {
            throw new IllegalArgumentException("Answer must be one of: a, b, c, or d");
        }

        // 检查其他必要字段
        if (qsBean.getQuestion() == null || qsBean.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty");
        }

        Question q = new Question();
        q.setQuestionText(qsBean.getQuestion());

        // 设置选项，处理可能的空值
        q.setAnswer1Text(qsBean.getOptiona() != null ? qsBean.getOptiona() : "");
        q.setAnswer1Correct("a".equals(lowerAns));

        q.setAnswer2Text(qsBean.getOptionb() != null ? qsBean.getOptionb() : "");
        q.setAnswer2Correct("b".equals(lowerAns));

        q.setAnswer3Text(qsBean.getOptionc() != null ? qsBean.getOptionc() : "");
        q.setAnswer3Correct("c".equals(lowerAns));

        q.setAnswer4Text(qsBean.getOptiond() != null ? qsBean.getOptiond() : "");
        q.setAnswer4Correct("d".equals(lowerAns));

        q.setIsDelete(0);
        q.setCreateTime(new Date());
        q.setUpdateTime(new Date());

        int result = questionMapper.insertQuestion(q);
        return result;
    }

    @Override
    public boolean delQuestion(Integer id){
        int result = questionMapper.delQuestionById(id);
        return result>0;
    }

    @Override
    public List<QSBeanOut> getQuestions() {
        List<Question> questionList = questionMapper.getQuestions();
        List<QSBeanOut> qsBeanOutList = questionList.stream().map(q -> {
            QSBeanOut out = new QSBeanOut();
            out.setQuestion(q.getQuestionText());
            out.setAnswers(List.of(
                    new AnsBean(q.getAnswer1Text(), q.getAnswer1Correct()),
                    new AnsBean(q.getAnswer2Text(), q.getAnswer2Correct()),
                    new AnsBean(q.getAnswer3Text(), q.getAnswer3Correct()),
                    new AnsBean(q.getAnswer4Text(), q.getAnswer4Correct())
            ));
            return out;
        }).collect(Collectors.toList());
        return qsBeanOutList;
    }

    @Override
    public QSBeanPage page(Integer page, Integer pageSize){
        // 获取总的记录数
        Integer total = questionMapper.count();

        // 获取分页查询结果列表
        Integer start = (page - 1) * pageSize;
        List<Question> questionList = questionMapper.page(start, pageSize);

        // 修复bug: 将 List<QSBeanOutManage> 正确赋值给 List 变量
        List<QSBeanOutManage> qsBeanOutManageList = Tools.convertToQSBeanManageList(questionList);

        // 封装PageBean对象
        QSBeanPage qsBeanPage = new QSBeanPage();
        qsBeanPage.setTotal(total);
        // 修复bug: 传入正确的 List 对象
        qsBeanPage.setQsBeanList(qsBeanOutManageList);

        return qsBeanPage;
    }

    public List<QSBeanOutManage> findByName(String keyword){  // 修复返回类型
        List<Question> questionList = questionMapper.findByName(keyword);  // 修复变量名

        List<QSBeanOutManage> qsBeanOutManageList = Tools.convertToQSBeanManageList(questionList);  // 修复变量名
        return qsBeanOutManageList;
    }
}