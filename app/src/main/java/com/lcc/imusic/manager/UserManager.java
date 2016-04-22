package com.lcc.imusic.manager;

import com.lcc.imusic.bean.LoginBean;
import com.lcc.imusic.utils.Json;
import com.lcc.imusic.utils.PrfUtil;

/**
 * Created by lcc_luffy on 2016/3/12.
 */
public class UserManager {

    private static class ClassHolder {
        private static final UserManager USER_MANAGER = new UserManager();
    }

    public static final String KEY = "USER";
    private LoginBean loginBean;

    private UserManager() {
        loginBean = Json.fromJson(PrfUtil.get().getString(KEY, null), LoginBean.class);
    }

    public static UserManager instance() {
        return ClassHolder.USER_MANAGER;
    }

    public static boolean isLogin() {
        return instance().loginBean != null;
    }

    public static void logout() {
        PrfUtil.start().remove(KEY);
    }

    public static void save(LoginBean loginBean) {
        PrfUtil.start().putString(KEY, Json.toJson(loginBean)).commit();
    }
}
