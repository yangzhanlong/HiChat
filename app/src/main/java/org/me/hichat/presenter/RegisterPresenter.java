package org.me.hichat.presenter;

import org.me.hichat.model.bean.User;

/**
 * Created by user on 2018/4/23.
 * 注册Presenter 接口
 */

public interface RegisterPresenter {
    void uploadUserInfo(User user);
    void createEMAccount(User user);
    void loginEMAccount(User user);
}
