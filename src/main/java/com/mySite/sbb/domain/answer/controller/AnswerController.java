package com.mySite.sbb.domain.answer.controller;

import com.mySite.sbb.domain.answer.service.AnswerService;
import com.mySite.sbb.domain.question.entity.Question;
import com.mySite.sbb.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content){
        Question question = this.questionService.getQuestion(id);
        //TODO: 답변 저장기능 만들어야함
        this.answerService.createAnswer(question, content);
        return String.format("redirect:/question/detail/%s",id);
    }
}
