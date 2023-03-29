package com.mySite.sbb.domain.question.repository;

import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.answer.entity.Answer;
import com.mySite.sbb.domain.question.entity.Question;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    @Transactional
    // @Modifying // 만약 아래 쿼리가 SELECT가 아니라면 이걸 붙여야 한다.
    @Modifying
    // nativeQuery = true 여야 MySQL 쿼리문법 사용 가능
    @Query(value = "ALTER TABLE question AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();
    Question findBySubject (String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String s);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question>spec,Pageable pageable);

    @Query("select q "
            + "from Question q "
            + "join SiteUser u on q.author=u "
            + "where u.username = :username "
            + "order by q.createTime desc ")
    List<Question> findByAuthor(@Param("username") String username, Pageable pageable);
}
