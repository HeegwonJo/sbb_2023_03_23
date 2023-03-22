package com.mySite.sbb.domain.answer.controller;

import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.SiteUser.UserService;
import com.mySite.sbb.domain.answer.AnswerForm;
import com.mySite.sbb.domain.answer.service.AnswerService;
import com.mySite.sbb.domain.question.entity.Question;
import com.mySite.sbb.domain.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal){
        Question question = this.questionService.getQuestion(id);
        SiteUser user= this.userService.getUser(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("question",question);
            return "question_detail";
        }
        this.answerService.createAnswer(question,answerForm.getContent(), user);
        return String.format("redirect:/question/detail/%s",id);
    }
}
