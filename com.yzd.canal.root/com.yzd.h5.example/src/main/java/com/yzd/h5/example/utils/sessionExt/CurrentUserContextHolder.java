package com.yzd.h5.example.utils.sessionExt;

public class CurrentUserContextHolder {
    private static final ThreadLocal<CurrentUser> WEB_CONTEXT_HOLDER = new ThreadLocal<CurrentUser>();

    public CurrentUserContextHolder() {
    }

    public static CurrentUser get() {
        return WEB_CONTEXT_HOLDER.get();
    }

    public static void set(CurrentUser context) {
        WEB_CONTEXT_HOLDER.set(context);
    }

    public static void remove() {
        WEB_CONTEXT_HOLDER.remove();
    }
}
