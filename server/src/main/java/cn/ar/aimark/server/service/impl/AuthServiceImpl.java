package cn.ar.aimark.server.service.impl;

import cn.ar.aimark.server.config.context.RuntimeContext;
import cn.ar.aimark.server.config.context.UserContext;
import cn.ar.aimark.server.mapper.model.auto.Role;
import cn.ar.aimark.server.mapper.model.auto.User;
import cn.ar.aimark.server.mapper.model.auto.UserLoginHistory;
import cn.ar.aimark.server.pojo.CommonResp;
import cn.ar.aimark.server.pojo.enums.AppCode;
import cn.ar.aimark.server.pojo.enums.ResponseCode;
import cn.ar.aimark.server.pojo.enums.RoleType;
import cn.ar.aimark.server.pojo.enums.UserStatus;
import cn.ar.aimark.server.pojo.param.LoginRequest;
import cn.ar.aimark.server.pojo.param.RefreshTokenRequest;
import cn.ar.aimark.server.pojo.param.RegisterUserRequest;
import cn.ar.aimark.server.service.*;

import cn.asr.appframework.PasswordUtils;
import cn.asr.appframework.utility.date.DateUtils;
import cn.asr.appframework.utility.jwt.JWTTokenUtils;
import cn.asr.appframework.utility.jwt.TokenData;
import cn.asr.appframework.utility.lang.ListExtUtils;
import cn.asr.appframework.utility.lang.StringExtUtils;
import cn.asr.appframework.utility.log.Log;
import cn.asr.appframework.utility.log.LoggerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.FutureTask;


@Service
public class AuthServiceImpl implements AuthService {

    private Log logger = LoggerWrapper.getLogger(String.valueOf(AuthServiceImpl.class));

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserLoginHistoryService userLoginHistoryService;

    @Override
    public CommonResp<TokenData> attemptAuthentication(LoginRequest request) {
        CommonResp<TokenData> resp = doLogin(request);

        if (resp.data != null && StringExtUtils.hasText(resp.data.getUserId())) {
            UserLoginHistory history = new UserLoginHistory();
            history.setUserId(Integer.parseInt(resp.data.getUserId()));
            history.setIP(UserContext.getIP());
            history.setLastLoginDateTime(DateUtils.getCurrentDate());
            history.setLoginSuccess(resp.respCode == ResponseCode.Success);
            history.setLoginFailedCount(1);
            FutureTask<Integer> futureTask = new FutureTask<Integer>(() -> {
                UserLoginHistory existHistoyRecord = userLoginHistoryService.selectByPrimaryKey(history.getUserId(), history.getIP());
                if (existHistoyRecord != null) {
                    existHistoyRecord.setLastLoginDateTime(history.getLastLoginDateTime());
                    existHistoyRecord.setLoginSuccess(history.getLoginSuccess());
                    if (history.getLoginSuccess() == false) {
                        existHistoyRecord.setLoginFailedCount(existHistoyRecord.getLoginFailedCount() + 1);
                    }
                    return userLoginHistoryService.updateByPrimaryKey(existHistoyRecord);
                } else {
                    return userLoginHistoryService.insert(history);
                }
            });
            RuntimeContext.getExecutorService().submit(futureTask);
        }

        return resp;
    }

    public CommonResp<TokenData> doLogin(LoginRequest request) {
        CommonResp resp = new CommonResp<TokenData>();
        try {

            String userName = request.userName;
            String password = request.password;
            AppCode appCode = request.appCode;
            logger.error("登录验证:" + request);


            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
                resp.fail("用户名或者密码不能为空！");
                return resp;
            }
            userName = userName.trim();

            User user = userService.findUserByLoginName(userName);
            logger.info("获取用户信息！", user);
            if (user == null || user.getDeleteFlag()) {
                resp.fail("用户不存在");
                return resp;
            }
            TokenData data = new TokenData();
            data.setUserId(user.getId().toString());
            resp.data = data;

            if (user.getStatus() == UserStatus.FREEZE.getEnumValue()) {
                resp.fail("用户已经被停用");
                return resp;
            }
            if (user.getStatus() == UserStatus.PENDING_VALIDATE.getEnumValue()) {
                resp.fail("用户待验证");
                return resp;
            }


            String passwordInStore = user.getPasswordHash();

            if (PasswordUtils.verify(password, passwordInStore) == false) {
                resp.fail("用户名或者密码错误");
                return resp;
            }

            //检查用户角色, 是否允许登录对应的系统
            List<Role> userRoles = userService.getRoles(user.getId());
            RoleType checkRole;
            switch (appCode) {
                case WEBAPP:
                    checkRole = RoleType.USER;
                    break;
                case SYS:
                    checkRole = RoleType.SUPERADMIN;
                    break;
                default:
                    resp.fail("未知的应用标识");
                    return resp;
            }
            boolean hasUserRole = ListExtUtils.contains(userRoles, (obj) -> {
                return obj.getRoleId().equals(checkRole.getEnumValue());
            });
            if (hasUserRole == false) {
                resp.fail("用户角色不是" + checkRole + ", 不能登录");
                return resp;
            }


            TokenData tokenData = JWTTokenUtils.grantToken(user.getId().toString(), user.getName(), null);
            tokenService.updateToken(tokenData.getUserId(), tokenData.getAccessToken(), tokenData.getRefreshToken());
            resp.success("success", tokenData);
            logger.info("登录成功！" + user.getId());
            return resp;

        } catch (Exception ex) {
            logger.error("登录验证出现异常:", ex);
            resp.fail("登录失败", ex);
        }

        return resp;
    }

    @Override
    public CommonResp registerUser(RegisterUserRequest req) {
        CommonResp resp = new CommonResp();
        User findExistUser = userService.findUserByLoginName(req.getLoginName());
        if (findExistUser != null) {
            resp.fail("用户名已经存在");
            return resp;
        }

        User newUser = new User();
        newUser.setLoginName(req.getLoginName());
        newUser.setName(req.getName());
        newUser.setPasswordHash(PasswordUtils.encodePassword(req.getPassword()));
        newUser.setEmail(req.getEmail());
        newUser.setMobile(req.getMobile());
        newUser.setStatus(UserStatus.NORMAL.getEnumValue());
        int affectRowCount = userService.addUser(newUser);
        resp.success("创建新用户成功", newUser.getId());

        return resp;

    }

    @Override
    public CommonResp<TokenData> refreshToken(RefreshTokenRequest request) {
        CommonResp resp = new CommonResp<TokenData>();
        resp.fail("请求信息无效");

        if (request == null || StringExtUtils.hasText(request.accessToken) == false || StringExtUtils.hasText(request.refreshToken) == false) {
            return resp;
        }


        TokenData decodedJWT = JWTTokenUtils.decodeToken(request.accessToken);
        if (decodedJWT != null) {
            String userID = decodedJWT.getUserId();
            String userName = decodedJWT.getUserName();
            if (tokenService.checkRefreshToken(userID, request.refreshToken)) {
                TokenData tokenData = JWTTokenUtils.grantToken(userID, userName, null);
                tokenService.updateToken(userID, tokenData.getAccessToken(), tokenData.getRefreshToken());
                resp.success("success", tokenData);

            }
        }


        return resp;
    }
}
