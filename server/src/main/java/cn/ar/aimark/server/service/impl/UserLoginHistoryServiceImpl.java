package cn.ar.aimark.server.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.ar.aimark.server.mapper.model.auto.UserLoginHistory;
import cn.ar.aimark.server.mapper.mapper.auto.UserLoginHistoryMapper;
import cn.ar.aimark.server.service.UserLoginHistoryService;

@Service
public class UserLoginHistoryServiceImpl implements UserLoginHistoryService {

    @Resource
    private UserLoginHistoryMapper userLoginHistoryMapper;

    @Override
    public int deleteByPrimaryKey(Integer userId, String IP) {
        return userLoginHistoryMapper.deleteByPrimaryKey(userId, IP);
    }

    @Override
    public int insert(UserLoginHistory record) {
        return userLoginHistoryMapper.insert(record);
    }

    @Override
    public UserLoginHistory selectByPrimaryKey(Integer userId, String IP) {
        return userLoginHistoryMapper.selectByPrimaryKey(userId, IP);
    }

    @Override
    public int updateByPrimaryKey(UserLoginHistory record) {
        return userLoginHistoryMapper.updateByPrimaryKey(record);
    }

}


