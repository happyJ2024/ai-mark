package cn.airesearch.aimarkserver.support;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class TokenUtil {

    @Value("${app.token.expire-minutes}")
    public static int TOKEN_EXPIRE_MINUTES = 300;
    @Value("${app.token.sign-key}")
    public static String TOKEN_SIGN_KEY = "ezml-sign-key";

    public static final String Bearer_Prefix = "Bearer ";
    public static final String Authorization_Header = "Authorization";


    public static String generateAccessToken(Date issueAt, Date expireAt, String[] audienceArray) {

        String accessToken = "";
        JWTCreator.Builder builder = JWT.create();
        if (audienceArray != null && audienceArray.length > 0) {
            builder.withAudience(audienceArray);
        }

        accessToken = builder.withIssuedAt(issueAt)
                .withExpiresAt(expireAt)
                .sign(Algorithm.HMAC256(TokenUtil.TOKEN_SIGN_KEY));

        return accessToken;

    }

    public static String generateRefreshToken(Date issueAt, Date expireAt, String[] audienceArray) {
        JWTCreator.Builder builder = JWT.create();
        if (audienceArray != null && audienceArray.length > 0) {
            builder.withAudience(audienceArray);
        }

        return builder.withIssuedAt(issueAt)
                .withExpiresAt(expireAt)
                .sign(Algorithm.HMAC256(TokenUtil.TOKEN_SIGN_KEY));
    }

    public static boolean verifyPassword(String password, String passwordBase64) {
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            String encodeString = encoder.encodeToString(password.getBytes("UTF-8"));
            return passwordBase64.equals(encodeString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UserTokenInfo decodeToken(String token) {
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        ;
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(TokenUtil.TOKEN_SIGN_KEY)).build();
        try {
            jwtVerifier.verify(token);


            DecodedJWT decodedJwt = JWT.decode(token);
            Date expireAt = decodedJwt.getExpiresAt();
            userTokenInfo.setExpiresAt(expireAt);
            Date issuedAt = decodedJwt.getIssuedAt();
            userTokenInfo.setIssuedAt(issuedAt);

            List<String> audienceList = decodedJwt.getAudience();
            if (audienceList.size() > 0) {
                String userId = audienceList.get(0);
                if (StringUtils.isEmpty(userId) == false) {
                    userTokenInfo.setUserId(userId);
                }
                if (audienceList.size() > 1) {
                    String userName = audienceList.get(1);
                    if (StringUtils.isEmpty(userName) == false) {
                        userTokenInfo.setUserName(userName);
                    }
                }
            }

            if (expireAt != null) {
                if (expireAt.getTime() < (new Date().getTime())) {
                    System.out.println("token已经过期");
                    userTokenInfo.setValid(false);
                }
            }


            if (StringUtils.isEmpty(userTokenInfo.getUserId()) == false) {
                System.out.println("token验证有效");
                userTokenInfo.setValid(true);
            } else {
                System.out.println("token解析信息不全");
                userTokenInfo.setValid(false);
            }

        } catch (JWTVerificationException e) {
            System.out.println("token密钥验证无效");
            userTokenInfo.setValid(false);
        }

        return userTokenInfo;
    }

}
