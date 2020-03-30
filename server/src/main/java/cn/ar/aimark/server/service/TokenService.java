package cn.ar.aimark.server.service;

import cn.ar.aimark.server.mapper.model.auto.Token;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TokenService {
    boolean checkRefreshToken(String userID, String refreshToken);

    boolean updateToken(String userID, String accessToken, String refreshToken);

    List<Token> selectAll();

    PageInfo<Token> selectAllwithPage(int page, int pageSize);
}
