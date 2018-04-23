package org.me.hichat.view.activity;

/**
 * Created by user on 2018/4/23.
 * 注册回调接口
 */

interface UserPasswordActivityView {
    void uploadHeadImageCallback(boolean success);
    void uploadUserInfoCallback(boolean success);
    void createEMAccountCallback(boolean success);
    void loginEMAccountCallback(boolean success);
}
