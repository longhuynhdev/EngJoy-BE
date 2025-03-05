package com.suika.englishlearning.service;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.mapper.QuestionMapper;
import com.suika.englishlearning.model.Answer;
import com.suika.englishlearning.model.Question;
import com.suika.englishlearning.model.dto.AnswerDto;
import com.suika.englishlearning.model.dto.question.QuestionDto;
import com.suika.englishlearning.repository.AnswerRepository;
import com.suika.englishlearning.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionMapper = questionMapper;
    }

    public List<QuestionDto> getAllQuestion() {
        return questionMapper.toDtoList(questionRepository.findAll());
    }

    public QuestionDto getQuestionById(int id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        return questionMapper.toDto(question);
    }

    public List<QuestionDto> createQuestions(List<QuestionDto> questionDtos) {
        List<Question> questions = questionMapper.toEntityList(questionDtos);

        for (Question question : questions) {
            List<Answer> answers = question.getAnswers();
            if (answers != null) {
                answerRepository.saveAll(answers);
            }
        }

        return questionMapper.toDtoList(questionRepository.saveAll(questions));
    }

    public QuestionDto editQuestion(Integer id, QuestionDto questionDto) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        boolean isChanged = false;

        if (!existingQuestion.getQuestion().equals(questionDto.getQuestion())) {
            existingQuestion.setQuestion(questionDto.getQuestion());
            isChanged = true;
        }

        List<Answer> existingAnswers = existingQuestion.getAnswers();
        List<AnswerDto> newAnswerDtos = questionDto.getAnswers();

        if (existingAnswers.size() != newAnswerDtos.size()) {
            existingQuestion.setAnswers(questionMapper.toAnswerEntityList(newAnswerDtos));
            isChanged = true;
        } else {
            for (int i = 0; i < existingAnswers.size(); i++) {
                Answer existingAnswer = existingAnswers.get(i);
                AnswerDto newAnswerDto = newAnswerDtos.get(i);

                if (!existingAnswer.getAnswer().equals(newAnswerDto.getAnswer()) ||
                        !existingAnswer.getExplanation().equals(newAnswerDto.getExplanation()) ||
                        existingAnswer.isCorrect() != newAnswerDto.isCorrect()) {
                    existingAnswer.setAnswer(newAnswerDto.getAnswer());
                    existingAnswer.setExplanation(newAnswerDto.getExplanation());
                    existingAnswer.setCorrect(newAnswerDto.isCorrect());
                    isChanged = true;
                }
            }
        }

        if (isChanged) {
            questionRepository.save(existingQuestion);
        }

        return questionMapper.toDto(existingQuestion);
    }
    
    public void DeleteQuestion(Integer id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        questionRepository.delete(question);
    }
}
