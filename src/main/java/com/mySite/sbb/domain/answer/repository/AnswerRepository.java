package com.mySite.sbb.domain.answer.repository;

import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.answer.entity.Answer;
import com.mySite.sbb.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnswerRepository extends JpaRepository <Answer, Integer> {
    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();

    Page<Answer> findByQuestion (Question question,Pageable pageable);

    Page<Answer> findByAuthor (SiteUser author, Pageable pageable);

}
