package ten.give.web.controller.view;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/join1")
    public String joinForm1(@ModelAttribute JoinForm form){
        return "joinForm1";
    }

    @GetMapping("/join2")
    public String joinForm2(@ModelAttribute JoinForm form){
        return "joinForm2";
    }

    @GetMapping("/joinComplete")
    public String joinCompleView(){
        return "joinCompleView";
    }

    @PostMapping("/join")
    public ResultForm join(@Valid @ModelAttribute JoinForm form){
        return userService.joinUser(form);
    }


    @PostMapping("/withdrawal")
    public String withdrawal(Authentication authentication){
        userService.withdrawal(Long.valueOf(authentication.getName()));
        return "redirect:/";
    }


    @GetMapping("/findemail")
    public String findEmailForm(@ModelAttribute FindEmailForm form){
        return "findEmail";
    }


    @PostMapping("/findemail")
    public String findEmail(@ModelAttribute FindEmailForm form, Model model){
        ResultForm email = userService.findEmail(form.getName(), form.getPhoneNumber());
        String startEmail = starEditEmail(email.getBody());
        email.setBody(startEmail);
        model.addAttribute("email",email);
        return "findEmailResult";
    }

    private String starEditEmail(String email) {
        String[] split = email.split("@");
        int len = split[0].length();
        int subTargetIndex = len / 2;
        int starts = subTargetIndex;

        if (len % 2 != 0){
            starts +=1;
        }

        return split[0].replace(split[0].substring(subTargetIndex),"*".repeat(starts))+"@"+split[1];
    }

    @GetMapping("/findpassword")
    public String findPasswordForm(@ModelAttribute FindPasswordForm form){
        return "findPassword";
    }


    @PostMapping("/findpassword")
    public String findPassword(@ModelAttribute SendEmailForm form, Model model){
        log.info("name : {} , email : {}" , form.getName(), form.getToEmail());
        model.addAttribute("newPassword",userService.findPassword(form.getName(),form.getToEmail()));
        return "findPasswordResult";
    }

    // 손봐야 함
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
