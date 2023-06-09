package com.mySite.sbb.domain.question.controller;

import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.SiteUser.UserService;
import com.mySite.sbb.domain.answer.entity.AnswerForm;
import com.mySite.sbb.domain.answer.entity.Answer;
import com.mySite.sbb.domain.answer.service.AnswerService;
import com.mySite.sbb.domain.comment.CommentForm;
import com.mySite.sbb.domain.question.entity.QuestionForm;
import com.mySite.sbb.domain.question.entity.Question;
import com.mySite.sbb.domain.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable ("id")Integer id, AnswerForm answerForm , CommentForm commentForm, @RequestParam(value="answerPage",defaultValue="0")int answerPage){
        Question question = this.questionService.getQuestion(id);
        Page<Answer> answerPaging= this.answerService.getList(question,answerPage);
        questionService.Count(question);
        model.addAttribute("question",question);
        model.addAttribute("answerPaging",answerPaging);
        return "question_detail";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page",defaultValue = "0") int page,
                       @RequestParam(value="keyword",defaultValue = "") String keyword){
        Page<Question> paging = this.questionService.getList(page,keyword);
        model.addAttribute("paging",paging);
        model.addAttribute("keyword",keyword);
        return "question_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("list/myQuestion")
    public String getMyList(Model model, Principal principal, @RequestParam(value="page",defaultValue = "0")int page){
        String username = principal.getName();
        SiteUser user = this.userService.getUser(username);
        Page<Question> myQuestion =this.questionService.getMyQuestion(page,user);
        model.addAttribute("paging",myQuestion);
        return"myQuestion_List";
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

}
