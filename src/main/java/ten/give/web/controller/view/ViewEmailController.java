package ten.give.web.controller.view;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ten.give.web.form.EmailResultForm;
import ten.give.web.form.SendEmailForm;
import ten.give.web.service.EmailService;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/view/email")
public class ViewEmailController {

    private final EmailService emailService;


    @PostMapping("/sendemail")
    public String sendEmail(@ModelAttribute SendEmailForm form, Model model){
        model.addAttribute("token",emailService.sendEmail(form.getToEmail()));
        log.info("auth name : {} , email : {}" , form.getName(),form.getToEmail());
        model.addAttribute("form",form);
        return "authenticationView";
    }



}
