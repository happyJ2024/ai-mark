package cn.ar.aimark.server.config.interceptor;

import cn.ar.aimark.server.constant.ApplicationConst;
import cn.ar.aimark.server.config.context.UserContext;
import cn.ar.aimark.server.mapper.model.auto.User;
import cn.ar.aimark.server.service.UserService;

import cn.asr.appframework.utility.jwt.JWTTokenUtils;
import cn.asr.appframework.utility.jwt.TokenData;
import cn.asr.appframework.utility.jwt.annotation.AnonymousAnnotation;
import cn.asr.appframework.utility.jwt.annotation.RequireAuthAnnotation;
import cn.asr.appframework.utility.lang.StringExtUtils;
import cn.asr.appframework.utility.log.Log;
import cn.asr.appframework.utility.log.LoggerWrapper;
import cn.asr.appframework.utility.network.NetworkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 拦截请求, 获取并验证token.
 * 验证失败--401
 * 验证通过--将用户信息加入到Attribute{ userId }中
 *
 * @author yunjian.bian
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    private Log logger = LoggerWrapper.getLogger(String.valueOf(AuthenticationInterceptor.class));

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果不是映射到方法,则直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String ip = NetworkUtils.getIpAddress(request);
        UserContext.put(UserContext.IP, ip);

        //检查方法上的Annotation
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        logger.debug(method.getName());

        //如果有Anonymous注解, 允许匿名访问, 不需要检查身份
        if (method.isAnnotationPresent(AnonymousAnnotation.class)) {
            return true;
        }

        //如果有RequireAuthAnnotation, 需要检查身份
        if (method.isAnnotationPresent(RequireAuthAnnotation.class)) {
            // 从 http 请求头中取出 token
            String token = request.getHeader(ApplicationConst.HEADER_Authorization_Key);
            if (StringExtUtils.hasText(token) == false) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("token required, please login first");
                return false;
            }

            // 验证 token的有效性
            boolean tokenValid = JWTTokenUtils.verify(token);
            if (tokenValid == false) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("token invalid");
                return false;
            }

            //解析token, 获取用户信息
            String userId = "";

            TokenData tokenData = JWTTokenUtils.decodeToken(token);
            if (tokenData != null) {
                userId = tokenData.getUserId();
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("token invalid");
                return false;
            }

            User user = userService.findUserById(userId);
            if (user == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("用户不存在，请重新登录");
                return false;
            }

            //将用户信息加入到 Attribute{ userId }中
            UserContext.put(UserContext.USER_ID, user.getId().toString());
            UserContext.put(UserContext.USER_NAME, user.getName());
            UserContext.put(UserContext.ACCESS_TOKEN, token);

            request.setAttribute(UserContext.USER_ID, user.getId().toString());
            request.setAttribute(UserContext.USER_NAME, user.getName());
            request.setAttribute(UserContext.ACCESS_TOKEN, token);
        }

        //默认没有上面两种注解, 允许匿名访问, 不需要检查身份
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
