package org.me.hichat.model.event;

import org.me.hichat.model.bean.User;

/**
 * Created by user on 2018/4/23.
 * 用户的行为事件类
 */

public class MessageEvent {
    private User user;
    private int type;

    public MessageEvent(User user, int type) {
        this.user = user;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public int getType() {
        return type;
    }

    public static final int LOGIN = 0;
    public static final int LOGOUT = 1;
}
