package com.mySite.sbb.domain.answer.service;

import com.mySite.sbb.domain.answer.entity.Answer;
import com.mySite.sbb.domain.answer.repository.AnswerRepository;
import com.mySite.sbb.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void createAnswer(Question question, String content) {
        Answer answer = new Answer();
        answer.setContent (content);
        answer.setCreateTime(LocalDateTime.now());
        answer.setQuestion(question);
        this.answerRepository.save(answer);
    }

}
