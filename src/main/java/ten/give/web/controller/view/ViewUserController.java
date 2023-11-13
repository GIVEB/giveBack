package ten.give.web.controller.view;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ten.give.domain.entity.user.User;
import ten.give.domain.exception.NoSuchTargetException;
import ten.give.domain.exception.form.ResultForm;
import ten.give.web.form.*;
import ten.give.web.service.FollowService;
import ten.give.web.service.LoginService;
import ten.give.web.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/view/users")
public class ViewUserController {

    private final LoginService loginService;
    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm form) {
        return "loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult) {

        log.info("loginId : {} , loginPassword : {} " , form.getLoginEmail(), form.getLoginPassword());

        if (bindingResult.hasErrors()) {
            log.info("in binding Error");
            return "redirect:/view/users/login";
        }

        String token = loginService.login(form.getLoginEmail(), form.getLoginPassword());

        if (token == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "redirect:/view/users/login";
        }
        //로그인 성공 처리 TODO
        return "redirect:/";
    }

    @ApiOperation(
            value = "Join",
            notes = "회원 가입" +
                    "[ EX ] URL : http://localhost:8080/users/join")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "JoinForm",
                            value = "회원 가입 form : email; name; password; birthYear; birthMonth; birthDay; phone; <br>" +
                                    "address; addressDetail; gender;",
                            required = true,
                            dataType = "JoinForm",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공"),
    })
    @PostMapping("/join")
    public ResultForm join(@Valid @RequestBody JoinForm form){
        return userService.joinUser(form);
    }

    @ApiOperation(
            value = "withdrawal",
            notes = "회원 탈퇴 <br>" +
                    "<br> 로그인이 선행 되어 있어야 한다. " +
                    "[ EX ] URL : http://localhost:8080/users/withdrawal")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "authentication",
                            value = "로그인 회원 정보",
                            required = true,
                            dataType = "Authentication",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공"),
    })
    @PostMapping("/withdrawal")
    public ResultForm withdrawal(Authentication authentication){
        return userService.withdrawal(Long.valueOf(authentication.getName()));
    }

    @ApiOperation(
            value = "User information",
            notes = "회원 정보 조회 <br>" +
                    "<br> 로그인이 선행 되어 있어야 한다. " +
                    "[ EX ] URL : http://localhost:8080/users/info")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "authentication",
                            value = "로그인 회원 정보",
                            required = true,
                            dataType = "Authentication",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공"),
    })
    @GetMapping("/info")
    public UserInfoForm showUserInfo(Authentication authentication){

        Optional<User> userByEmail = userService.getuserByAccountId(Long.valueOf(authentication.getName()));
        Long totalDonationCount = userService.getTotalDonationCount();

        if (userByEmail.isEmpty()){
            throw new NoSuchTargetException("존재 하지 않는 User 입니다.");
        }

        Long followingCount = followService.getFollowingCount(Long.valueOf(authentication.getName()));
        Long followerCount = followService.getFollowerCount(Long.valueOf(authentication.getName()));

        UserInfoForm userInfoForm = userByEmail.get().userTransferToUserInfo(totalDonationCount,followingCount,followerCount);

        return userInfoForm;

    }

    @ApiOperation(
            value = "User information edit",
            notes = "회원 정보 수정 <br>" +
                    "<br> 로그인이 선행 되어 있어야 한다. " +
                    "[ EX ] URL : http://localhost:8080/users/edit")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "authentication",
                            value = "로그인 회원 정보",
                            required = true,
                            dataType = "Authentication",
                            paramType = "body",
                            defaultValue = "None"
                    ),
                    @ApiImplicitParam(
                            name = "updateParam",
                            value = "로그인 회원 정보 수정 시 사용자가 입력하는 값",
                            required = true,
                            dataType = "UserInfoForm",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공"),
    })
    @PostMapping("/edit")
    public UserInfoForm editUserInfo(Authentication authentication, @RequestBody UserInfoForm updateParam){

        Long userId = Long.valueOf(authentication.getName());

        Optional<User> userByEmail = userService.getuserByAccountId(userId);

        if (userByEmail.isEmpty()){
            throw new NoSuchTargetException("존재 하지 않는 User 입니다.");
        }

        UserInfoForm userInfoForm = userService.editUserInfo(userId, updateParam);

        return userInfoForm;
    }


    @ApiOperation(
            value = "find email",
            notes = "email 찾기 <br>" +
                    "로그인 되어 있지 않아도 됨" +
                    "[ EX ] URL : http://localhost:8080/users/findemail")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "name",
                            value = "사용자 이름",
                            required = true,
                            dataType = "String",
                            paramType = "body",
                            defaultValue = "None"
                    ),
                    @ApiImplicitParam(
                            name = "phone number",
                            value = "사용자 전화번호",
                            required = true,
                            dataType = "String",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @PostMapping("/findemail")
    public ResultForm findEmail(@RequestBody FindEmailForm form){
        return userService.findEmail(form.getName(),form.getPhoneNumber());
    }

    @ApiOperation(
            value = "find password",
            notes = "password 찾기 <br>" +
                    "로그인이 되어 있지 않아도 된다." +
                    "[ EX ] URL : http://localhost:8080/users/findpassword")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "name",
                            value = "사용자 이름",
                            required = true,
                            dataType = "String",
                            paramType = "body",
                            defaultValue = "None"
                    ),
                    @ApiImplicitParam(
                            name = "phone number",
                            value = "사용자 전화번호",
                            required = true,
                            dataType = "String",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @PostMapping("/findpassword")
    public ResultForm findPassword(@RequestBody FindPasswordForm form){
        return userService.findPassword(form.getName(),form.getEmail());
    }

    @ApiOperation(
            value = "edit password",
            notes = "password 수정 <br>" +
                    "[ EX ] URL : http://localhost:8080/users/editpassword")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "password",
                            value = "수정 될 비밀번호",
                            required = true,
                            dataType = "String",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @PostMapping("/editpassword")
    public ResultForm editPassword(@RequestBody PasswordEditForm editPassword, Authentication authentication){
        log.info("password : {} ",editPassword.getEditPassword());
        return userService.editPassword(Long.valueOf(authentication.getName()),editPassword.getEditPassword());
    }



    /*// coolSMS 구현 로직 연결
    @GetMapping("/sendemail")
    public void sendSMS(@RequestParam(value="to") String to, HttpServletRequest request, HttpServletResponse response) throws CoolsmsException, IOException {


        request.setAttribute("smsToken","testToken");
        String redirect_uri="/users/test";

        response.sendRedirect(redirect_uri);

    }*/

    /*@GetMapping("/test")
    public String test(String smsToken, HttpServletRequest request){
        log.info("in /test uri");
        log.info("{}" , request.getAttribute("smsToken"));
        return smsToken;
    }*/

}
