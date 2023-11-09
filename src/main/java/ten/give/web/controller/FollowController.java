package ten.give.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ten.give.domain.entity.user.Follow;
import ten.give.domain.exception.form.ResultForm;
import ten.give.web.service.FollowService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "EmailController")
@RequestMapping("/")
public class FollowController {

    private final FollowService followService;

    @ApiOperation(
            value = "Follow",
            notes = "Follow 하기 <br>" +
                    "타인을 Follow 합니다." +
                    "[ EX ] URL : http://localhost:8080/follow/3")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "authentication",
                            value = "Follow 할 ID",
                            required = true,
                            dataType = "Authentication",
                            paramType = "head",
                            defaultValue = "None"
                    ),
                    @ApiImplicitParam(
                            name = "toId",
                            value = "Follow 할 ID",
                            required = true,
                            dataType = "Long",
                            paramType = "path",
                            defaultValue = "None"
                    )
            }
    )
    @PostMapping("/follow/{toId}")
    public ResultForm follow(@PathVariable Long toId, Authentication authentication){
        return followService.follow(Long.valueOf(authentication.getName()),toId);
    }

    @ApiOperation(
            value = "unFollow",
            notes = "unFollow 하기 <br>" +
                    "타인은 unFollow 합니다." +
                    "[ EX ] URL : http://localhost:8080/unfollow/3")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "authentication",
                            value = "로그인 사용자 Token",
                            required = true,
                            dataType = "Authentication",
                            paramType = "head",
                            defaultValue = "None"
                    ),
                    @ApiImplicitParam(
                            name = "toId",
                            value = "unFollow 할 ID",
                            required = true,
                            dataType = "Long",
                            paramType = "path",
                            defaultValue = "None"
                    )
            }
    )
    @PostMapping("/unfollow/{followId}")
    public ResultForm unfollow(@PathVariable Long followId, Authentication authentication){
        return followService.unfollow(Long.valueOf(authentication.getName()),followId);
    }

    @ApiOperation(
            value = "show Following",
            notes = "Following 목록 보기 <br>" +
                    "[ EX ] URL : http://localhost:8080/followings")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "authentication",
                            value = "로그인 사용자 정보",
                            required = true,
                            dataType = "Authentication",
                            paramType = "head",
                            defaultValue = "None"
                    )
            }
    )
    @GetMapping("/followings")
    public List<Follow> followings(Authentication authentication){
        return followService.getFollowing(Long.valueOf(authentication.getName()));
    }

    @ApiOperation(
            value = "show Followers",
            notes = "Followers 인원 보기 <br>" +
                    "[ EX ] URL : http://localhost:8080/Followers")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "authentication",
                            value = "로그인 ID",
                            required = true,
                            dataType = "Authentication",
                            paramType = "body",
                            defaultValue = "None"
                    )
            }
    )
    @GetMapping("/followers")
    public List<Follow> Followers(Authentication authentication){
        return followService.getFollower(Long.valueOf(authentication.getName()));
    }


}
