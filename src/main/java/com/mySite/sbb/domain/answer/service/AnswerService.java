package com.mySite.sbb.domain.answer.service;

import com.mySite.sbb.DataNotFoundException;
import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.answer.entity.Answer;
import com.mySite.sbb.domain.answer.repository.AnswerRepository;
import com.mySite.sbb.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void createAnswer(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent (content);
        answer.setCreateTime(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }
}
