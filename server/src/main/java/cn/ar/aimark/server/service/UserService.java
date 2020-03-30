package cn.ar.aimark.server.service;

import cn.ar.aimark.server.mapper.model.auto.Role;
import cn.ar.aimark.server.mapper.model.auto.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    User findUserByLoginName(String loginName);

    User findUserById(String userId);

    int addUser(User newUser);

    int updateDeleteFlagById(Integer id, Boolean deleteFlag);

    PageInfo<User> selectAllwithPage(int page, int pageSize);

    List<User> selectAll();

    List<Role> getRoles(Integer id);
}
