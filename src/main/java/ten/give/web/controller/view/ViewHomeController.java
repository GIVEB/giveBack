package ten.give.web.controller.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ten.give.domain.entity.user.User;
import ten.give.web.service.FollowService;
import ten.give.web.service.LoginService;
import ten.give.web.service.UserService;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class ViewHomeController {

    private final LoginService loginService;
    private final UserService userService;
    private final FollowService followService;

    @GetMapping
    public String loginHome(Model model) {
        return "loginHome";
    }

}
