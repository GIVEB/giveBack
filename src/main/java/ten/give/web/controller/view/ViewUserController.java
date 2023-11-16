package ten.give.web.controller.view;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import ten.give.domain.entity.user.User;
import ten.give.domain.exception.NoSuchTargetException;
import ten.give.domain.exception.form.ResultForm;
import ten.give.web.form.*;
import ten.give.web.service.FollowService;
import ten.give.web.service.LoginService;
import ten.give.web.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/view/users")
public class ViewUserController {

    @Value("${email.expireTime}")
    private int expireTime;

    private final LoginService loginService;
    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm form, Model model) {
        model.addAttribute("form",form);
        return "loginForm";
    }

    @ResponseBody
    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm form) {
        return loginService.login(form.getLoginEmail(),form.getLoginPassword());
    }

    //@PostMapping("/login")
    public String login(@Validated @ModelAttribute("form") LoginForm form, BindingResult bindingResult, Model model, HttpServletResponse response) throws UnsupportedEncodingException {

        if (bindingResult.hasErrors()) {
            return "loginForm";
        }

        String token = loginService.login(form.getLoginEmail(), form.getLoginPassword());

        if (token == null) {
            bindingResult.reject("loginFail", "존재하지 않는 유저입니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "loginForm";
        }

        model.addAttribute("loginUser", userService.getUserByEmail(form.getLoginEmail()).get());
        model.addAttribute("token", token);

        String jwt = "Bearer " + token;
        jwt = URLEncoder.encode(jwt, "utf-8");
        log.info("token : {}" ,token);
        log.info("jwt : {} " , jwt);
        Cookie cookie = new Cookie("jwt",jwt);
        cookie.setMaxAge(expireTime * 1000);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        response.addCookie(cookie);

        return "loginHome";
    }

    @GetMapping("/join1")
    public String joinForm1(@ModelAttribute JoinForm form){
        return "joinForm1";
    }

    @GetMapping("/join2")
    public String joinForm2(@ModelAttribute JoinForm form,@RequestParam(required = false) Boolean duple,Model model,HttpServletRequest request){

        model.addAttribute("form",form);

        log.info("duple : {}" , duple);

        model.addAttribute("duple", duple);

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if(flashMap == null) {
            model.addAttribute("tokenForm",null);
            return "joinForm2";
        }
        String email = (String)flashMap.get("email");
        if(email != null){
            model.addAttribute("form",form);
        }
        EmailResultForm tokenForm = (EmailResultForm)flashMap.get("tokenForm");
        if(tokenForm != null){
            model.addAttribute("tokenForm",tokenForm);
        }
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
        model.addAttribute("newPassword",userService.findPassword(form.getName(),form.getToEmail()));
        return "findPasswordResult";
    }

    @GetMapping("/userCheck")
    public String userCheck(@RequestParam String email, RedirectAttributes redirectAttributes, HttpServletRequest request){

        Optional<User> userByEmail = userService.getUserByEmail(email);

        redirectAttributes.addAttribute("email",email);

        if(!userByEmail.isEmpty()){
            redirectAttributes.addAttribute("duple",true);
            return "redirect:/view/users/join2";
        }

        redirectAttributes.addAttribute("duple",false);
        return "redirect:/view/users/join2";
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
