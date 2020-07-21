package cn.airesearch.aimarkserver.filter;

import cn.airesearch.aimarkserver.support.TokenUtil;
import cn.airesearch.aimarkserver.support.UserTokenInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局过滤器
 * 所有请求都会执行
 * 可拦截get、post等请求做逻辑处理
 */
@Component
public class RequestFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uriString = request.getRequestURI();
        System.out.println("request uri:" + uriString);
        String method = request.getMethod();
        System.out.println("request method:" + method);


        if (checkAuthToken(request)) {
        } else {
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.setStatus(401);

            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Max-Age", "3600");
            resp.addHeader("Access-Control-Allow-Methods", request.getMethod());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean checkAuthToken(HttpServletRequest request) {
        String uriString = request.getRequestURI();
        if (uriString.contains("login")) {
            return true;
        }
        String method = request.getMethod();
        if (method.equalsIgnoreCase("OPTIONS")) {
            return true;
        }

        String authToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authToken) == false) {
            if (authToken.startsWith(TokenUtil.Bearer_Prefix)) {
                String token = authToken.substring(TokenUtil.Bearer_Prefix.length());
                UserTokenInfo tokenInfo = TokenUtil.decodeToken(token);
                if (tokenInfo != null && tokenInfo.isValid()) {
                    return true;
                }

            }
        }
        return false;
    }
}
