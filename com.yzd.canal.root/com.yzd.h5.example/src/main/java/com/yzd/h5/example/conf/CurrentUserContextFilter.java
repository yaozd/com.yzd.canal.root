package com.yzd.h5.example.conf;

import com.yzd.h5.example.utils.cookieExt.CookieKeyList;
import com.yzd.h5.example.utils.cookieExt.CookieUtil;
import com.yzd.h5.example.utils.fastjson.FastJsonUtil;
import com.yzd.h5.example.utils.sessionExt.CurrentUser;
import com.yzd.h5.example.utils.sessionExt.CurrentUserContextHolder;
import com.yzd.h5.example.utils.sessionExt.LoginSessionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CurrentUserContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        //
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        //设置当前用户信息的上下文
        CurrentUser currentUser = LoginSessionUtil.getCurrentUserByCookie(request, response);
        LoginSessionUtil.setLogin(currentUser);
        filterChain.doFilter(req, res);
    }


    @Override
    public void destroy() {

    }
}
