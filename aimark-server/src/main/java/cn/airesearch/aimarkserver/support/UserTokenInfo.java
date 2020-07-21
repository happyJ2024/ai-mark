package cn.airesearch.aimarkserver.support;

import lombok.Data;

import java.util.Date;

@Data
public class UserTokenInfo {

    String userId;
    String userName;
    String accessToken;
    String refreshToken;
    Date issuedAt;
    Date expiresAt;

    boolean isValid;
}
