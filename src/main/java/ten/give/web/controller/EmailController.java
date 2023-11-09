package ten.give.web.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ten.give.web.form.EmailResultForm;
import ten.give.web.form.SendEmailForm;
import ten.give.web.service.EmailService;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "EmailController")
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @ApiOperation(
            value = "Send E-mail",
            notes = "E-mail 로 인증 번호 보내기 <br>" +
                    "[ EX ] URL : http://localhost:8080/email/sendmail")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "email",
                            value = "인증 token 을 전송할 e-mail 주소",
                            required = true,
                            dataType = "json",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @PostMapping("/sendemail")
    public EmailResultForm sendEmail(@RequestBody SendEmailForm form){
        return emailService.sendEmail(form.getToEmail());

    }


}
