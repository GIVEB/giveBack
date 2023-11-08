package ten.give.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Password Edit form")
public class PasswordEditForm {

    @ApiModelProperty(value = "password", required = true, example = "123dfasdf")
    private String editPassword;

}
