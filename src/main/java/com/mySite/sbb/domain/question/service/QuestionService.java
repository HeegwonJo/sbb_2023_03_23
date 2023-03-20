package com.mySite.sbb.domain.question.service;

import com.mySite.sbb.DataNotFoundException;
import com.mySite.sbb.domain.question.entity.Question;
import com.mySite.sbb.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id){
        Optional<Question> question =this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        }else{
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content) {
        Question question= new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateTime(LocalDateTime.now());
        this.questionRepository.save(question);
    }
}
