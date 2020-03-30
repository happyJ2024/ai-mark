package cn.ar.aimark.server.service.impl;

import cn.ar.aimark.server.mapper.mapper.auto.UserMapper;
import cn.ar.aimark.server.mapper.model.auto.Role;
import cn.ar.aimark.server.mapper.model.auto.User;
import cn.ar.aimark.server.mapper.model.auto.Userrole;
import cn.ar.aimark.server.service.UserService;
import cn.ar.aimark.server.service.UserroleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserroleService userroleService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserByLoginName(String loginName) {

        return userMapper.selectByLoginName(loginName);
    }

    @Override
    public User findUserById(String userId) {

        return userMapper.selectByPrimaryKey(Integer.valueOf(userId));
    }

    @Override
    public int addUser(User newUser) {

        return userMapper.insert(newUser);
    }


    @Override
    public int updateDeleteFlagById(Integer id, Boolean deleteFlag) {
        return userMapper.updateDeleteFlagById(id, deleteFlag);
    }

    @Override
    public PageInfo<User> selectAllwithPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(userMapper.selectAll());
    }

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<Role> getRoles(Integer userId) {
        List<Role> roleList = new ArrayList<>();
        List<Userrole> userRoleList = userroleService.selectByUserId(userId);
        if (userRoleList != null) {
            userRoleList.forEach((c) -> {
                Role newRole = new Role();
                newRole.setRoleId(c.getRoleId());
                roleList.add(newRole);
            });
        }

        return roleList;
    }
}
