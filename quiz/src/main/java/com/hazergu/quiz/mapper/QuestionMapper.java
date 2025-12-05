package com.hazergu.quiz.mapper;

import com.hazergu.quiz.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO questions (questionText, answer1Text, answer1Correct, answer2Text, answer2Correct, " +
            "answer3Text, answer3Correct, answer4Text, answer4Correct, isDelete, createTime, updateTime) " +
            "VALUES (#{questionText}, #{answer1Text}, #{answer1Correct}, #{answer2Text}, #{answer2Correct}, " +
            "#{answer3Text}, #{answer3Correct}, #{answer4Text}, #{answer4Correct}, #{isDelete}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertQuestion(Question question);

    //根据id删除题目；
    @Update("UPDATE questions SET isDelete = 1 WHERE id = #{id} AND isDelete = 0")
    public int delQuestionById(Integer id);

    @Select("SELECT * FROM questions WHERE isDelete = 0 ORDER BY RAND() LIMIT 5")
    public List<Question> getQuestions();

    @Select("SELECT COUNT(*) FROM questions WHERE isDelete=0")
    public int count();

    @Select("SELECT * FROM questions WHERE isDelete=0 limit #{start},#{pageSize}")
    public List<Question> page(Integer start, Integer pageSize);

    @Select("SELECT * FROM questions WHERE questionText LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0")
    List<Question> findByName(String keyword);

    @Update("UPDATE questions SET questionText = #{questionText}, answer1Text = #{answer1Text}, answer1Correct = #{answer1Correct}, " +
            "answer2Text = #{answer2Text}, answer2Correct = #{answer2Correct}, answer3Text = #{answer3Text}, " +
            "answer3Correct = #{answer3Correct}, answer4Text = #{answer4Text}, answer4Correct = #{answer4Correct}, " +
            "updateTime = NOW() WHERE id = #{id} AND isDelete = 0")
    int updateQuestion(Question question);

}

