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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ten.give.web.form.EmailResultForm;
import ten.give.web.form.SendEmailForm;
import ten.give.web.service.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/view/email")
public class ViewEmailController {

    private final EmailService emailService;

    @PostMapping("/sendemail")
    public String sendEmail(@ModelAttribute SendEmailForm form, Model model){

        model.addAttribute("token",emailService.sendEmail(form.getToEmail()));
        model.addAttribute("form",form);
        return "authenticationView";
    }

    @PostMapping("/sendmail")
    public String sendmail(@RequestParam String email, RedirectAttributes redirectAttributes,HttpServletRequest request){
        log.info("sendMail Test : {} " , email);
        redirectAttributes.addAttribute("email",email);
        EmailResultForm tokenForm = emailService.sendEmail(email);
        redirectAttributes.addFlashAttribute("tokenForm",tokenForm);
        return "redirect:/view/users/join2";
    }


}
