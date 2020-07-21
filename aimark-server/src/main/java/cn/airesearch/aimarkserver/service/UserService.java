package cn.airesearch.aimarkserver.service;

import cn.airesearch.aimarkserver.pojo.modelvo.UserInfo;

public interface UserService {
    UserInfo getUserInfo(String userName);
}
