package cn.ar.aimark.server.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.ar.aimark.server.mapper.mapper.auto.RoleactionMapper;
import cn.ar.aimark.server.mapper.model.auto.Roleaction;
import cn.ar.aimark.server.service.RoleactionService;

@Service
public class RoleactionServiceImpl implements RoleactionService {

    @Resource
    private RoleactionMapper roleactionMapper;

    @Override
    public int deleteByPrimaryKey(Integer roleId, Integer actionId) {
        return roleactionMapper.deleteByPrimaryKey(roleId, actionId);
    }

    @Override
    public int insert(Roleaction record) {
        return roleactionMapper.insert(record);
    }

}

