package ten.give.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@ApiModel(value = "send Email Form")
public class SendEmailForm {

    @ApiModelProperty(value = "name" , required = false, example = "신영운")
    private String name;

    @Email
    @ApiModelProperty(value = "email Address", required = true, example = "test@test.com")
    private String toEmail;

}
