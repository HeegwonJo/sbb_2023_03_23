package com.mySite.sbb.domain.question.entity;

import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime;
    private LocalDateTime modifyDate;


    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList= new ArrayList<>();

    public void addAnswer(Answer a) {
        a.setQuestion(this);
        answerList.add(a);
    }
    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    private Integer viewCount;


}
