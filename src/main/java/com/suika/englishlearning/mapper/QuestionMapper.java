package com.suika.englishlearning.mapper;

import com.suika.englishlearning.model.Answer;
import com.suika.englishlearning.model.Question;
import com.suika.englishlearning.model.dto.AnswerDto;
import com.suika.englishlearning.model.dto.question.QuestionDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionMapper implements Mapper<Question, QuestionDto> {

    @Override
    public QuestionDto toDto(Question question) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        questionDto.setQuestion(question.getQuestion());

        List<AnswerDto>  answerDtos = new ArrayList<>();
        for(Answer answer : question.getAnswers()) {
            AnswerDto answerDto = new AnswerDto();
            answerDto.setAnswerId(answer.getAnswerId());
            answerDto.setAnswer(answer.getAnswer());
            answerDto.setExplanation(answer.getExplanation());
            answerDto.setCorrect(answer.isCorrect());
            answerDtos.add(answerDto);
        }
        questionDto.setAnswers(answerDtos);

        return questionDto;
    }

    @Override
    public Question toEntity(QuestionDto questionDto) {
        Question question = new Question();
        question.setQuestion(questionDto.getQuestion());

        List<Answer> answers = new ArrayList<>();
        for(AnswerDto answerDto : questionDto.getAnswers()) {
            Answer answer = new Answer();
            answer.setAnswerId(answerDto.getAnswerId());
            answer.setAnswer(answerDto.getAnswer());
            answer.setExplanation(answerDto.getExplanation());
            answer.setCorrect(answerDto.isCorrect());
            answers.add(answer);
        }
        question.setAnswers(answers);
        return question;
    }

    @Override
    public List<QuestionDto> toDtoList(List<Question> questions) {
        return questions.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Question> toEntityList(List<QuestionDto> questionDtos) {
        return questionDtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<Answer> toAnswerEntityList(List<AnswerDto> answerDtos) {
        return answerDtos.stream().map(this::toAnswerEntity).collect(Collectors.toList());
    }

    public Answer toAnswerEntity(AnswerDto answerDto) {
        Answer answer = new Answer();
        answer.setAnswerId(answerDto.getAnswerId());
        answer.setAnswer(answerDto.getAnswer());
        answer.setExplanation(answerDto.getExplanation());
        answer.setCorrect(answerDto.isCorrect());
        return answer;
    }
}
