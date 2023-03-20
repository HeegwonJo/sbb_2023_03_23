package com.mySite.sbb.domain.answer.repository;

import com.mySite.sbb.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository <Answer, Integer> {
}
