package org.me.hichat.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.me.hichat.model.bean.User;
import org.me.hichat.model.event.MessageEvent;
import org.me.hichat.utils.MyLogger;
import org.me.hichat.view.activity.UserPasswordActivity;
import org.me.hichat.wrap.SimpleEMCallBack;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by user on 2018/4/23.
 * 注册 Presenter 实现
 */

public class RegisterPresenterImpl implements RegisterPresenter {
    private UserPasswordActivity activity;
    public RegisterPresenterImpl(UserPasswordActivity activity) {
        this.activity = activity;
    }

    /**
     * 上传用户信息
     * @param user user
     */
    @Override
    public void uploadUserInfo(final User user) {
        user.getIcon().uploadblock(new UploadFileListener() { // 上传头像
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    activity.uploadHeadImageCallback(true); // 上传成功
                    user.save(new SaveListener<String>() {  // 上传其他信息
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                activity.uploadUserInfoCallback(true);
                                // 创建环信账户
                                createEMAccount(user);
                            } else {
                                MyLogger.i(e.toString());
                                activity.uploadUserInfoCallback(false);
                            }
                        }
                    });
                } else {
                    activity.uploadHeadImageCallback(false); // 上传失败
                }
            }
        });
    }

    /**
     * 创建环信账户
     * @param user user
     */
    @Override
    public void createEMAccount(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 该方法是一个同步的方法，有联网操作，需要在子线程执行
                    EMClient.getInstance().createAccount(user.getUserName(), user.getPassword());
                    // 创建成功
                    activity.createEMAccountCallback(true);
                    // 登录环信账户
                    loginEMAccount(user);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    activity.createEMAccountCallback(false);  // 创建失败
                }
            }
        }).start();
    }

    /**
     * 登录环信账户
     * @param user user
     */
    @Override
    public void loginEMAccount(final User user) {
        EMClient.getInstance().login(user.getUserName(), user.getPassword(), new SimpleEMCallBack() {//回调
            @Override
            public void onSuccess() {
                super.onSuccess();
                EventBus.getDefault().post(new MessageEvent(user, MessageEvent.LOGIN));
                activity.loginEMAccountCallback(true); // 登录成功
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code , message);
                activity.loginEMAccountCallback(false);  // 登录失败
            }
        });
    }
}