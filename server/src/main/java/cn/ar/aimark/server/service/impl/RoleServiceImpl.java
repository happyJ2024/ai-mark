package cn.ar.aimark.server.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.ar.aimark.server.mapper.mapper.auto.RoleMapper;
import cn.ar.aimark.server.mapper.model.auto.Role;
import cn.ar.aimark.server.service.RoleService;
@Service
public class RoleServiceImpl implements RoleService{

    @Resource
    private RoleMapper roleMapper;

    @Override
    public int deleteByPrimaryKey(Integer roleId) {
        return roleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int insert(Role record) {
        return roleMapper.insert(record);
    }

    @Override
    public Role selectByPrimaryKey(Integer roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public int updateByPrimaryKey(Role record) {
        return roleMapper.updateByPrimaryKey(record);
    }

}
