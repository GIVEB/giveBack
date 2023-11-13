package ten.give.web.controller.view;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ten.give.domain.exception.form.ResultForm;
import ten.give.web.form.DonorAddForm;
import ten.give.web.form.DonorCardInfoForm;
import ten.give.web.form.DonorUpdateForm;
import ten.give.web.service.DonorCardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/view/donorcards")
public class ViewDonorCardController {

    @Value("${admin.id}")
    private Long admin;

    private final DonorCardService cardService;

    @GetMapping
    public String donorcardView(Authentication authentication, Model model) {

        // 관리자 권한인 2L 이 아니면 , 수정 필요
        /*if (Long.valueOf(authentication.getName()) != admin){
            return "redirect:/";
        }*/

        model.addAttribute("cardList", cardService.getCardList());

        return "cardListView";
    }


    @GetMapping("/list")
    public String userCardListView(Authentication authentication, Model model) {

        if (authentication.getName() == null){
            return "redirect:/";
        }

        model.addAttribute("cardList", cardService.getCardListByUserId(Long.valueOf(authentication.getName())));

        return "cardListView";
    }

}