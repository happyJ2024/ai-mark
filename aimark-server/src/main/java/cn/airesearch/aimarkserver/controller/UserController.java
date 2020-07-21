package cn.airesearch.aimarkserver.controller;

import cn.airesearch.aimarkserver.constant.SysErrorCode;
import cn.airesearch.aimarkserver.pojo.modelvo.UserInfo;
import cn.airesearch.aimarkserver.pojo.requestvo.LoginReq;
import cn.airesearch.aimarkserver.service.UserService;
import cn.airesearch.aimarkserver.support.TokenUtil;
import cn.airesearch.aimarkserver.support.UserTokenInfo;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ZhangXi
 */
@Slf4j
@RestController
public class UserController {

    private final UserService UserService;

    @Autowired
    public UserController(UserService UserService) {
        this.UserService = UserService;
    }

    @PostMapping(value = "/api/login")
    public BaseResponse<UserTokenInfo> ocr(@RequestBody @Validated LoginReq req) {
        BaseResponse<UserTokenInfo> resp = new BaseResponse<>();
        UserInfo userInfo = UserService.getUserInfo(req.getUserName());
        if (userInfo != null && userInfo.getPassword().equals(req.getPassword())) {

            UserTokenInfo userTokenInfo = new UserTokenInfo();
            Date issueAt = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, TokenUtil.TOKEN_EXPIRE_MINUTES);
            Date expireAt = calendar.getTime();
            String[] audienceArray = {userInfo.getUserName()};
            String accessToken = TokenUtil.generateAccessToken(issueAt, expireAt, audienceArray);
            userTokenInfo.setUserName(req.getUserName());
            userTokenInfo.setExpiresAt(expireAt);
            userTokenInfo.setIssuedAt(issueAt);
            userTokenInfo.setValid(true);
            userTokenInfo.setAccessToken(accessToken);
            resp.success("success", userTokenInfo);
            return resp;
        }

        resp.fail("login failed", SysErrorCode.FAILURE);
        return resp;
    }


}
