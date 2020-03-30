package cn.ar.aimark.server.service.impl;

import cn.ar.aimark.server.mapper.mapper.auto.TokenMapper;
import cn.ar.aimark.server.mapper.model.auto.Token;
import cn.ar.aimark.server.service.TokenService;
import cn.asr.appframework.utility.lang.StringExtUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public boolean checkRefreshToken(String userID, String refreshToken) {
        Token token = tokenMapper.selectByPrimaryKey(userID);
        if (token == null || token.getDeleteFlag()) {
            return false;
        }
        return StringExtUtils.equals(token.getRefreshToken(), (refreshToken));
    }

    @Override
    public boolean updateToken(String userID, String accessToken, String refreshToken) {
        Token token = tokenMapper.selectByPrimaryKey(userID);
        if (token == null) {
            //insert
            token = new Token();
            token.setUserId(userID);
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);
            tokenMapper.insert(token);
        } else {
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);
            tokenMapper.updateByPrimaryKey(token);
        }
        return true;
    }

    @Override
    public List<Token> selectAll() {
        return tokenMapper.selectAll();
    }

    @Override
    public PageInfo<Token> selectAllwithPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(tokenMapper.selectAll());
    }
}
