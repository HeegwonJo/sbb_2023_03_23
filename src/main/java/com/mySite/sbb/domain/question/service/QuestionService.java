package com.mySite.sbb.domain.question.service;

import com.mySite.sbb.DataNotFoundException;
import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.answer.entity.Answer;
import com.mySite.sbb.domain.comment.Comment;
import com.mySite.sbb.domain.question.entity.Question;
import com.mySite.sbb.domain.question.repository.QuestionRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createTime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec=search(keyword);
        return this.questionRepository.findAll(spec,pageable);
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public Page<Question> getMyQuestion(int page ,SiteUser author){
        Pageable pageable = PageRequest.of(page,10);
        Page<Question> MyQuestionList =this.questionRepository.findByAuthor(author,pageable);
        return MyQuestionList;
    }

    public Question create(String subject, String content, SiteUser author) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateTime(LocalDateTime.now());
        question.setAuthor(author);
        question.setViewCount(0);
        this.questionRepository.save(question);
        return question;
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    public void Count(Question question){
        question.setViewCount(question.getViewCount()+1);
        this.questionRepository.save(question);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                Join<Answer,Comment> c = a.join("commentList", JoinType.LEFT);
                Join<Comment,SiteUser> u3 = c.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"), //답변 작성자
                        cb.like(c.get("content"),"%"+kw+"%"), //댓글 내용
                        cb.like(u3.get("username"), "%" + kw + "%")); //댓글 작성자
            }
        };
    }

}
