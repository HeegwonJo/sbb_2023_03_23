package com.mySite.sbb.domain.answer.entity;

import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.comment.Comment;
import com.mySite.sbb.domain.question.entity.Question;
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
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime;
    private LocalDateTime modifyDate;


    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;
    @ManyToMany
    Set<SiteUser> voter;
    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;
}


