package ten.give.web.controller.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ten.give.web.form.DonorAddForm;
import ten.give.web.service.DonorCardService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/view/donorcards")
public class ViewDonorCardController {

    @Value("${admin.id}")
    private Long admin;

    private final DonorCardService cardService;

    @GetMapping
    public String donorcardView(Authentication authentication, Model model,@CookieValue(value = "jwt",required = false) String jwt) {

        // 관리자 권한인 2L 이 아니면 , 수정 필요
        /*if (Long.valueOf(authentication.getName()) != admin){
            return "redirect:/";
        }*/

        log.info("jwt : {}", jwt);

        model.addAttribute("cardList", cardService.getCardList());

        return "cardListView";
    }

    @GetMapping("donorcardList")
    public String cardList(){
        return "cardList";
    }


    @GetMapping("/list")
    public String userCardListView(Authentication authentication, Model model) {

        if (authentication.getName() == null){
            return "redirect:/";
        }

        model.addAttribute("cardList", cardService.getCardListByUserId(Long.valueOf(authentication.getName())));

        return "cardListView";
    }

    @GetMapping("/addDonorcard")
    public String addForm(Authentication authentication,@ModelAttribute DonorAddForm form) {

        if (authentication.getName() == null){
            return "home";
        }

        return "addDonorcardForm";
    }

    @PostMapping("/addDonorcard")
    public String addCard(Authentication authentication,@ModelAttribute DonorAddForm form){
        return "redirect:/";
    }

}