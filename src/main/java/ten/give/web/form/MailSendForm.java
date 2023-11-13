package ten.give.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "로그인 입력 form")
public class MailSendForm {

    @ApiModelProperty(value = "보낼 email", required = true, example = "test@test.com")
    private String toEmail;
    @ApiModelProperty(value = "제목", required = true, example = "xxx 님 인증 Mail 입니다.")
    private String subject;
    @ApiModelProperty(value = "내용", required = true, example = "xxx 님 인증 번호는 [xxxx] 입니다.")
    private String body;

}
