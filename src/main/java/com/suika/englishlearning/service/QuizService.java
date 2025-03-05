package com.suika.englishlearning.service;

import com.suika.englishlearning.exception.DuplicateResourceException;
import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.mapper.QuestionMapper;
import com.suika.englishlearning.mapper.QuizMapper;
import com.suika.englishlearning.mapper.QuizResultMapper;
import com.suika.englishlearning.model.*;
import com.suika.englishlearning.model.dto.question.QuestionDto;
import com.suika.englishlearning.model.dto.question.QuestionResultDto;
import com.suika.englishlearning.model.dto.quiz.*;
import com.suika.englishlearning.repository.*;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizResultRepository quizResultRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DifficultyRepository difficultyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizMapper quizMapper;
    private final QuestionMapper questionMapper;
    private final QuizResultMapper quizResultMapper;

    private QuizService(QuizRepository quizRepository, UserRepository userRepository,
                        CategoryRepository categoryRepository, DifficultyRepository difficultyRepository,
                        QuizResultRepository quizResultRepository, QuestionRepository questionRepository, QuestionService questionService, AnswerRepository answerRepository,
                        QuestionMapper questionMapper, QuizResultMapper quizResultMapper, QuizMapper quizMapper)
    {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.difficultyRepository = difficultyRepository;
        this.quizResultRepository = quizResultRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizMapper = quizMapper;
        this.questionMapper = questionMapper;
        this.quizResultMapper = quizResultMapper;
    }

    public List<QuizDetailsDto> getQuizzes()
    {
        List<Quiz> quizzes = quizRepository.findAll();
        List<QuizDetailsDto> quizDetailsDto = new ArrayList<>();
        for (Quiz quiz : quizzes)
        {
            quizDetailsDto.add(quizMapper.toDtoDetails(quiz));
        }
        return quizDetailsDto;
    }


    public QuizDetailsDto getQuizById(Integer id)
    {
        return quizMapper.toDtoDetails(quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found")));
    }

    public String createQuiz(AddQuizRequestDto addQuizRequestDto, String userName) {
        UserEntity author = userRepository.findByEmail(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (author.getRole().getName().equals("USER"))
            throw new RuntimeException("User cannot create quizzes");

        if (addQuizRequestDto.getDuration() <= 0 || addQuizRequestDto.getPoints() <= 0) {
            throw new IllegalArgumentException("Duration and points must be greater than 0");
        }

        List<Question> questions = questionMapper.toEntityList(addQuizRequestDto.getQuestions());
        for (Question question : questions) {
            List<Answer> answers = question.getAnswers();
            if (answers != null) {
                answerRepository.saveAll(answers);
            }
        }
        questionRepository.saveAll(questions);

        Quiz quiz = quizMapper.toEntity(addQuizRequestDto, author);
        quiz.setQuestions(questions);

        quizRepository.save(quiz);

        return "Quiz created";
    }

    public QuizResponseDto updateQuiz(Integer id, EditQuizRequestDto quizRequestDto)
    {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        // Check request validity
        if (quizRequestDto.getTitle() == null || quizRequestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty or null");
        }
        if (quizRequestDto.getDuration() <= 0 || quizRequestDto.getPoints() <= 0) {
            throw new IllegalArgumentException("Duration and points must be greater than 0");
        }

        quiz.setTitle(quizRequestDto.getTitle());

        if (quizRequestDto.getShortDescription() != null && !quizRequestDto.getShortDescription().isEmpty()) {
            quiz.setShortDescription(quizRequestDto.getShortDescription());
        }

        quiz.setDuration(quizRequestDto.getDuration());
        quiz.setPoints(quizRequestDto.getPoints());

        if (quizRequestDto.getDate() != null) {
            quiz.setDate(quizRequestDto.getDate());
        }

        if (quizRequestDto.getCategories() != null && !quizRequestDto.getCategories().isEmpty()) {
            List<Category> categories = quizRequestDto.getCategories().stream()
                    .map(name -> categoryRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + name)))
                    .collect(Collectors.toList());
            quiz.setCategories(categories);
        }

        if (quizRequestDto.getDifficulties() != null && !quizRequestDto.getDifficulties().isEmpty()) {
            List<Difficulty> difficulties = quizRequestDto.getDifficulties().stream()
                    .map(name -> difficultyRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Difficulty not found: " + name)))
                    .collect(Collectors.toList());
            quiz.setDifficulties(difficulties);
        }

        return quizMapper.toDto(quizRepository.save(quiz));
    }

    public String deleteQuiz(Integer id)
    {
        if (!quizRepository.existsById(id))
        {
            throw new ResourceNotFoundException("Quiz not found");
        }
        quizRepository.deleteById(id);

        return "Quiz deleted";
    }

    public String assignQuestionsToQuiz(Integer id, List<Integer> questionIds)
    {
        if (questionIds == null || questionIds.isEmpty()) {
            throw new IllegalArgumentException("Question list cannot be empty");
        }

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        List<Question> questions = quiz.getQuestions();

        for (Integer questionId : questionIds) {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
            if (questions.contains(question))
            {
                throw new DuplicateResourceException("Question already assigned");
            }

            questions.add(question);
        }

        quiz.setQuestions(questions);
        quizRepository.save(quiz);

        return "Questions assigned";
    }

    public String removeQuestionsFromQuiz(Integer id, List<Integer> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) {
            throw new IllegalArgumentException("Question list cannot be empty");
        }

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        List<Question> questions = quiz.getQuestions();

        for (Integer questionId : questionIds) {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
            if (!questions.contains(question))
            {
                throw new IllegalArgumentException("Question not in the quiz");
            }

            questions.remove(question);
        }

        quiz.setQuestions(questions);
        quizRepository.save(quiz);

        return "Questions removed";
    }

    public List<QuestionDto> getQuestions(Integer id)
    {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        List<Question> questions = quiz.getQuestions();

        return questionMapper.toDtoList(questions);
    }

    public List<QuizResponseDto> filterQuizzes(List<String> categories, List<String> difficulties)
    {
        // Validate categories
        if (categories != null && !categories.isEmpty()) {
            for (String category : categories) {
                if (!categoryRepository.existsByName(category))
                    throw new IllegalArgumentException("Category not existing: " + category);
            }
        }

        // Validate difficulties
        if (difficulties != null && !difficulties.isEmpty()) {
            for (String difficulty : difficulties) {
                if (!difficultyRepository.existsByName(difficulty)) {
                    throw new IllegalArgumentException("Difficulty not existing: " + difficulty);
                }
            }
        }

        List<Quiz> filteredQuizzes;

        if ((categories == null || categories.isEmpty()) && (difficulties == null || difficulties.isEmpty())) {
            // Return all quizzes if no filters are provided
            filteredQuizzes = quizRepository.findAll();

        } else if (categories != null && difficulties != null) {
            // Filter by both categories and difficulties
            filteredQuizzes = quizRepository.findByCategoriesAndDifficulties(categories, difficulties)
                    .stream()
                    .flatMap(Optional::stream) // Extract non-empty Optional values
                    .collect(Collectors.toList());

        } else if (categories != null) {
            // Filter by categories only
            filteredQuizzes = quizRepository.findByCategories(categories)
                    .stream()
                    .flatMap(Optional::stream) // Extract non-empty Optional values
                    .collect(Collectors.toList());

        } else {
            // Filter by difficulties only
            filteredQuizzes = quizRepository.findByDifficulties(difficulties)
                    .stream()
                    .flatMap(Optional::stream) // Extract non-empty Optional values
                    .collect(Collectors.toList());
        }

        // Map quizzes to DTOs
        return filteredQuizzes.stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }

    public Pair<String, Integer> processQuizAttempt(QuizAttemptRequestDto quizAttemptDto) {
        // Fetch quiz and user
        Quiz quiz = quizRepository.findById(quizAttemptDto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        UserEntity user = userRepository.findByEmail(quizAttemptDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate the questions and answers
        List<QuestionResult> questionResults = new ArrayList<>();
        for (QuestionResultDto questionResultDto : quizAttemptDto.getQuestionResults()) {
            Question question = quiz.getQuestions().stream()
                    .filter(q -> q.getId() == questionResultDto.getQuestionId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));

            Answer selectedAnswer = question.getAnswers().stream()
                    .filter(a -> a.getId() == questionResultDto.getAnswerId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid answer ID"));

            // Add to question results
            QuestionResult questionResult = new QuestionResult();
            questionResult.setQuestion(question);
            questionResult.setSelectedAnswer(selectedAnswer);

            questionResults.add(questionResult);
        }

        // Create and save QuizResult
        QuizResult quizResult = new QuizResult();
        quizResult.setQuiz(quiz);
        quizResult.setUser(user);
        quizResult.setCompletedAt(quizAttemptDto.getCompletedAt());
        quizResult.setQuestionResults(questionResults);

        quizResultRepository.save(quizResult);

        return new Pair<>("Quiz attempt submitted successfully", quizResult.getScore());
    }

    public List<QuizAttemptResponseDto> getQuizAttemptsByQuizId(int quizId, String username) {
        // Fetch quiz
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        // Fetch user
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Fetch quiz attempts for the user
        List<QuizResult> results = quizResultRepository.findByQuizAndUser(quiz, user)
                .stream()
                .flatMap(Optional::stream) // Extract non-empty Optional values
                .collect(Collectors.toList());

        // Map results to DTOs
        return quizResultMapper.toDtoList(results);
    }

    public List<QuizAttemptResponseDto> getAllQuizAttempts(String username) {
        // Fetch user
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Fetch quiz attempts for the user
        List<QuizResult> results = quizResultRepository.findByUser(user)
                .stream()
                .flatMap(Optional::stream) // Extract non-empty Optional values
                .collect(Collectors.toList());

        // Map results to DTOs
        return quizResultMapper.toDtoList(results);
    }
}
