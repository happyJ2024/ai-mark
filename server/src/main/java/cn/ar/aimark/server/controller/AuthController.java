package cn.ar.aimark.server.controller;

import cn.ar.aimark.server.pojo.param.RefreshTokenRequest;
import cn.ar.aimark.server.pojo.param.RegisterUserRequest;
import cn.ar.aimark.server.service.AuthService;
import cn.ar.aimark.server.pojo.param.LoginRequest;
import cn.ar.aimark.server.pojo.CommonResp;
import cn.asr.appframework.utility.jwt.TokenData;
import cn.asr.appframework.utility.jwt.annotation.AnonymousAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth for user
 *
 * @author yunjian.bian
 */
@RestController
@RequestMapping(value = "/auth")
@Api(tags = "权限验证接口")
public class AuthController {

    @Autowired
    public AuthService authService;


    @ApiOperation(value = "登录", response = TokenData.class)
    @AnonymousAnnotation
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public CommonResp<TokenData> login(@RequestBody @ApiParam(value = "登录请求", required = true) LoginRequest request) {
        CommonResp resp = authService.attemptAuthentication(request);

        return resp;
    }

    @ApiOperation(value = "刷新身份验证信息", response = TokenData.class)
    @AnonymousAnnotation
    @RequestMapping(value = {"/refreshToken"}, method = RequestMethod.POST)
    public CommonResp<TokenData> refreshToken(@RequestBody @ApiParam(value = "刷新身份验证信息请求", required = true) RefreshTokenRequest request) {
        CommonResp resp = authService.refreshToken(request);
        return resp;
    }

    @ApiOperation(value = "注册新用户", response = Integer.class)
    @AnonymousAnnotation
    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public CommonResp<Integer> register(@RequestBody RegisterUserRequest req) {
        CommonResp resp = authService.registerUser(req);
        return resp;

    }

}
