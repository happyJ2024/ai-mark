package cn.ar.aimark.server.service;

import cn.ar.aimark.server.pojo.CommonResp;
import cn.ar.aimark.server.pojo.param.LoginRequest;
import cn.ar.aimark.server.pojo.param.RefreshTokenRequest;
import cn.ar.aimark.server.pojo.param.RegisterUserRequest;
import cn.asr.appframework.utility.jwt.TokenData;


public interface AuthService {
    CommonResp<TokenData> attemptAuthentication(LoginRequest request);

    CommonResp<Integer> registerUser(RegisterUserRequest req);

    CommonResp<TokenData> refreshToken(RefreshTokenRequest request);
}
