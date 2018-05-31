package com.yzd.example2.h5.utils.sessionExt;


import com.yzd.example2.h5.utils.cookieExt.CookieKeyList;
import com.yzd.example2.h5.utils.cookieExt.CookieUtil;
import com.yzd.example2.h5.utils.fastjson.FastJsonUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginSessionUtil {
    //登录时间调整为1天
    private static final Integer loginCookieExpireSeconds = 60 * 60 * 24;

    public static CurrentUser getCurrentUserByCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil cookieUtil = new CookieUtil(request, response);
        String value = cookieUtil.getCookieValue(CookieKeyList.L_U_Key.name());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return FastJsonUtil.deserialize(value, CurrentUser.class);
    }

    public static CurrentUser getCurrentUser() {
        return CurrentUserContextHolder.get();
    }

    public static void setLogin(CurrentUser currentUser) {
        CurrentUserContextHolder.set(currentUser);
    }

    public static void setLogin(CurrentUser currentUser, HttpServletRequest request, HttpServletResponse response) {
        String userStr = FastJsonUtil.serialize(currentUser);
        CookieUtil cookieUtil = new CookieUtil(request, response);
        cookieUtil.addCookie(CookieKeyList.L_U_Key.name(), userStr, loginCookieExpireSeconds);
        setLogin(currentUser);
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil cookieUtil = new CookieUtil(request, response);
        cookieUtil.deleteCookie(CookieKeyList.L_U_Key.name());
        CurrentUserContextHolder.remove();
    }

    public static boolean isLogin() {
        return getCurrentUser() != null;
    }

    public static void removeCurrentUserContextHolder() {
        CurrentUserContextHolder.remove();
    }
}
