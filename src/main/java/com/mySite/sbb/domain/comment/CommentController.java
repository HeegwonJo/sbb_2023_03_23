package com.mySite.sbb.domain.comment;

import com.mySite.sbb.domain.SiteUser.SiteUser;
import com.mySite.sbb.domain.SiteUser.UserService;
import com.mySite.sbb.domain.answer.entity.Answer;
import com.mySite.sbb.domain.answer.entity.AnswerForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;
    private final CommentService commentService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal, RedirectAttributes re) {
        SiteUser user = this.userService.getUser(principal.getName());
        Answer answer = this.answerService.getAnswer(id);
        Question question =answer.getQuestion();

        if (bindingResult.hasErrors()) {
            model.addAttribute("answer", answer);
            return "question_detail";
        }
        int page = question.getAnswerList().size() / 10;
        re.addAttribute("answerPage", page);
        Comment comment = this.commentService.create(answer,commentForm.getContent(),user);
        model.addAttribute("comment",comment);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
}
