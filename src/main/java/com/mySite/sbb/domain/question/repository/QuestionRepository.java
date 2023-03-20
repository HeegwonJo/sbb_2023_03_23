package com.mySite.sbb.domain.question.repository;

import com.mySite.sbb.domain.answer.entity.Answer;
import com.mySite.sbb.domain.question.entity.Question;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    Question findBySubject (String subject);
}
