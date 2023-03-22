package com.mySite.sbb.domain.question.controller;

import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.SiteUser.UserService;
import com.mySite.sbb.domain.answer.AnswerForm;
import com.mySite.sbb.domain.question.QuestionForm;
import com.mySite.sbb.domain.question.entity.Question;
import com.mySite.sbb.domain.question.repository.QuestionRepository;
import com.mySite.sbb.domain.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable ("id")Integer id, AnswerForm answerForm){
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @GetMapping("list")
    public String list(Model model, @RequestParam(value="page",defaultValue = "0") int page){
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging",paging);
        return "question_list";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm){
        return "question_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        SiteUser user= this.userService.getUser(principal.getName());
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(),questionForm.getContent(),user);
        return "redirect:/question/list";
    }
}
