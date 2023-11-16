package ten.give.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ten.give.common.enums.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "회원 가입 시 입력 form")
public class JoinForm {

    @Email
    @ApiModelProperty(value = "ID 용 Email 주소", example = "test@test.com")
    private String email;

    @ApiModelProperty(value = "사용자 이름", example = "김실험")
    private String name;

    @ApiModelProperty(value = "사용자 비밀번호", example = "test!")
    private String password;

    @ApiModelProperty(value = "생년", example = "2023")
    private String birthYear;


    @ApiModelProperty(value = "생월", example = "01")
    private String birthMonth;


    @ApiModelProperty(value = "생일", example = "11")
    private String birthDay;


    @ApiModelProperty(value = "핸드폰번호", example = "01012345678")
    private String phone;


    @ApiModelProperty(value = "주소", example = "청주시 흥덕구 증안로 21")
    private String address;

    @ApiModelProperty(value = "주소 상세", example = "xx 아파트, xxx동 xxx호")
    private String addressDetail;

    @ApiModelProperty(value = "성별 " , example = "남자, 여자")
    private Gender gender;

}
