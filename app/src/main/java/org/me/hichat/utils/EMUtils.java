package org.me.hichat.utils;

import com.hyphenate.chat.EMClient;

/**
 * Created by user on 2018/4/23.
 * 环信工具类
 */

public final class EMUtils {
    private EMUtils() {

    }

    /**
     * 判断用户是否已经登录到环信服务器
     * @return boolean
     */
    public static boolean isLogin() {
        // 是否连接到环信服务器 && 之前是否登录到了环信服务器
        return EMClient.getInstance().isConnected() && EMClient.getInstance().isLoggedInBefore();
    }
}
