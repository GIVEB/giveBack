package ten.give.web.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/donorcards")
@Api(tags = "DonorCardController")
public class DonorCardController {

    private final DonorCardService cardService;

    @ApiOperation(
            value = "다중 헌혈증 조회하기",
            notes = "모든 헌혈증 조회하기 <br>" +
                    "권한 : Admin Only <br>" +
                    "[ Ex ] URL : http://localhost:8080/donorcards")
    @ApiResponses({
            @ApiResponse(code=200, message="성공")
    })
    @GetMapping
    public Object getList(Authentication authentication){
        // 관리자 권한인 2L 이 아니면 , 수정 필요
        if (Long.valueOf(authentication.getName()) != 2L){
            return new ResultForm(false,"허용되지 않은 접근입니다 [getList]");
        }
        Map<String,List<DonorCardInfoForm>> result = cardService.getCardList();
        return result;
    }

    @ApiOperation(
            value = "card_id 로 헌혈증 단일 조회하기",
            notes = "card_id로 헌혈증 단일 조회하기<br>" +
                    "[ EX ] URL : http://localhost:8080/donorcards/2 <br>" +
                    "1 : red box 2 ~ : user_id " )
    @ApiImplicitParam(
            name = "cardId",
            value = "카드 Generate 번호",
            required = true,
            dataType = "Long",
            paramType = "path",
            defaultValue = "None"
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공"),
            @ApiResponse(code=400, message="존재하지 않습니다.")
    })
    @GetMapping("/{cardId}")
    public DonorCardInfoForm getCard(@PathVariable Long cardId){
        return cardService.getCard(cardId);
    }

    @ApiOperation(
            value = "헌혈증 등록하기",
            notes = "헌혈증 정보 등록하기 <br>" +
                    "[ EX ] URL : http://localhost:8080/donorcards")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "DonorCardform",
                            value = "헌혈증 등록 정보",
                            required = true,
                            dataType = "DonorAddForm",
                            paramType = "body",
                            defaultValue = "None"
                    ),
                    @ApiImplicitParam(
                            name = "Authentication",
                            value = "로그인 유저 Token",
                            required = true,
                            dataType = "Authentication",
                            paramType = "Header",
                            defaultValue = "None"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공")
    })
    @PostMapping
    public DonorCardInfoForm addCard(@RequestBody DonorAddForm form, Authentication authentication){
        return cardService.addCard(form,authentication);
    }

    @ApiOperation(
                value = "헌혈증 삭제하기",
            notes = "card_id 로 헌혈증 삭제하기<br>" +
                    " [ EX ] URL : http://localhost:8080/donorcards/2 ")
    @ApiImplicitParam(
            name = "card_id",
            value = "헌혈증 ID",
            required = true,
            dataType = "Long",
            paramType = "path",
            defaultValue = "None"
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공")
    })
    @DeleteMapping("/{cardId}")
    public ResultForm deleteCard(@PathVariable Long cardId){
       return cardService.deleteCard(cardId);
    }

    @ApiOperation(
            value = "헌혈증 정보 수정하기",
            notes = "헌혈증 정보 수정하기<br>" +
                    "[ EX ] URL : http://localhost:8080/donorcards/3")
    @ApiImplicitParams(
            value = {
                @ApiImplicitParam(
                        name = "cardId",
                        value = "헌혈증 ID",
                        required = true,
                        dataType = "Long",
                        paramType = "path",
                        defaultValue = "None"
                ),
                    @ApiImplicitParam(
                            name = "DonorUpdateForm",
                            value = "헌혈증 수정 form",
                            required = true,
                            dataType = "DonorUpdateForm",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공"),
            @ApiResponse(code = 400,message = "존재 하지 않습니다.")
    })
    @PatchMapping("/{cardId}")
    public DonorCardInfoForm updateCard(@PathVariable Long cardId, @RequestBody DonorUpdateForm form){
        return cardService.updateCard(cardId,form);
    }

    @ApiOperation(
            value = "헌혈증 수량 보기",
            notes = " '소유자 ID' 로 헌혈증 수량보기<br>" +
                    "[ EX ] URL : http://localhost:8080/donorcards/count/3")
    @ApiImplicitParam(
            name = "user_id",
            value = "소유자 id",
            required = true,
            dataType = "Long",
            paramType = "path",
            defaultValue = "None"
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공")
    })
    @GetMapping("/count/{userId}")
    public Map<String,Integer> cardCount(@PathVariable Long userId){

        Map<String, Integer> result = new HashMap<>();
        Integer cardCount = cardService.getCardCount(userId);
        log.info("count : {}",cardCount);
        result.put("count",cardCount);

        return result;
    }

    @ApiOperation(
            value = " 헌혈증 목록 보기 [ 소유자 ID 이용 / 관리자 권한 ]",
            notes = " '소유자 ID' 헌혈증 목록 보기<br>" +
                    "[ EX ] URL : http://localhost:8080/donorcards/list/3")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "user_id",
                            value = "소유자 id",
                            required = true,
                            dataType = "Long",
                            paramType = "path",
                            defaultValue = "None"
                    ),
                    @ApiImplicitParam(
                            name = "authentication",
                            value = "로그인 유저 Token",
                            required = true,
                            dataType = "Authentication",
                            paramType = "path",
                            defaultValue = "None"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공")
    })
    @GetMapping("/list/{userId}")
    public Map<String,List<DonorCardInfoForm>> getCardListByUserId(@PathVariable Long userId, Authentication authentication){

        if (Long.valueOf(authentication.getName()) != 1L){
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }

        Map<String,List<DonorCardInfoForm>> result = cardService.getCardListByUserId(userId);
        return result;
    }

    @ApiOperation(
            value = " 로그인 사용자 헌혈증 목록 보기 [ 사용자 권한 ]",
            notes = "로그인 사용자 헌혈증 목록 보기<br>" +
                    "[ EX ] URL : http://localhost:8080/donorcards/list")
    @ApiImplicitParam(
            name = "authentication",
            value = "로그인 유저 Token",
            required = true,
            dataType = "Authentication",
            defaultValue = "None"
    )
    @ApiResponses({
            @ApiResponse(code=200, message="성공")
    })
    @GetMapping("/list")
    public Map<String,List<DonorCardInfoForm>> getCardListByUserId(Authentication authentication){

        Map<String,List<DonorCardInfoForm>> result = cardService.getCardListByUserId(Long.valueOf(authentication.getName()));
        return result;

    }

}
