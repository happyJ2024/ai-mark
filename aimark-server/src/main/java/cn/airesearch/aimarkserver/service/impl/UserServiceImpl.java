package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.pojo.modelvo.UserInfo;
import cn.airesearch.aimarkserver.service.UserService;
import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserInfo getUserInfo(String userName) {
        if (userMap.containsKey(userName)) {
            return userMap.get(userName);
        }
        return null;
    }

    private static HashMap<String, UserInfo> userMap = new HashMap<>();

    static {
        File f = new File("user.txt");
        if (f.exists()) {
            List<String> lines = FileUtil.readLines(f, "UTF-8");
            for (String line : lines
            ) {
                if (StringUtils.isEmpty(line)) continue;

                String[] valueArray = line.split(" ");
                if (valueArray.length >= 2) {
                    UserInfo info = new UserInfo();
                    info.setUserName(valueArray[0]);
                    info.setPassword(valueArray[1]);
                    userMap.put(info.getUserName(), info);
                }
            }
        }
        System.out.println("UserList:");
        for (String u :
                userMap.keySet()) {
            System.out.println(u);
        }
    }
}
