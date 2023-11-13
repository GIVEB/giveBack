package ten.give.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "send Email Form")
public class SendEmailForm {

    @ApiModelProperty(value = "email Address", required = true, example = "test@test.com")
    private String toEmail;

}
