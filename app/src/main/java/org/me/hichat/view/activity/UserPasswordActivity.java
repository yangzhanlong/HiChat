package org.me.hichat.view.activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.me.hichat.R;
import org.me.hichat.base.BaseActivity;
import org.me.hichat.model.bean.User;
import org.me.hichat.presenter.RegisterPresenter;
import org.me.hichat.presenter.RegisterPresenterImpl;
import org.me.hichat.utils.MyToast;
import org.me.hichat.utils.ValidateUtils;
import org.me.hichat.wrap.SimpleTextWatcher;

import java.io.File;

import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;

public class UserPasswordActivity extends BaseActivity implements UserPasswordActivityView{

    private static final int SUCCESS_UPLOAD_HEAD_IMAGE = 100;
    private static final int FAILED_UPLOAD_INFO = 101;
    private static final int SUCCESS_UPLOAD_INFO = 102;
    private static final int SUCCESS_CREATE_EMACCOUNT = 103;
    private static final int SUCCESS_LOGIN_EMSERVER = 104;
    private static final int ERROR_LOGIN_EMSERVER = 105;

    TextInputEditText etPwd;
    TextInputEditText etConfirmPwd;
    Button btRegister;
    TextInputEditText etUsername;
    private View rl_loading;
    private TextView tv_info;
    private RegisterPresenter presenter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS_UPLOAD_HEAD_IMAGE:
                case SUCCESS_UPLOAD_INFO:
                    tv_info.setText((String)msg.obj);
                    break;
                case FAILED_UPLOAD_INFO:
                    // 退出loading界面
                    rl_loading.setVisibility(View.GONE);
                    Toast.makeText(UserPasswordActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected View addLayoutToFrameLayout(LayoutInflater layoutInflater, FrameLayout frameLayout) {
        View view = layoutInflater.inflate(R.layout.activity_user_password, frameLayout, true);
        return view;
    }

    @Override
    protected void initView(View view) {
        etPwd = ButterKnife.findById(view, R.id.et_pwd);
        etConfirmPwd = ButterKnife.findById(view, R.id.et_confirm_pwd);
        btRegister = ButterKnife.findById(view, R.id.bt_register);
        etUsername = ButterKnife.findById(view, R.id.et_username);

        // 监听文本变化
        SimpleTextWatcher watcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                updateRegisterButtonState();
            }
        };

        etUsername.addTextChangedListener(watcher);
        etPwd.addTextChangedListener(watcher);
        etConfirmPwd.addTextChangedListener(watcher);

        //监听软件的操作
        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
                        MyToast.show(UserPasswordActivity.this, "用户名输入为空");
                    }
                }
                return false;
            }
        });

        // 监听注册点击事件
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        presenter = new RegisterPresenterImpl(this);

	/*
        etConfirmPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (btRegister.isEnabled()) {
                        register();
                    } else {
                        MyToast.show(UserPasswordActivity.this, "用户名或者密码没有输入");
                    }
                    return true;
                }
                return false;
            }
        });
	*/
    }

    // 更新注册button状态
    private void updateRegisterButtonState() {
        String username = etUsername.getText().toString();
        String pwd = etPwd.getText().toString();
        String confirm = etConfirmPwd.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirm)) {
            btRegister.setEnabled(false);
            return;
        }
        btRegister.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_register: // 为什么没有效果？
                register();
                break;
            case R.id.ib_back:
                if (rl_loading == null) {
                    super.onClick(view);
                }
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        //校验用户名、密码是否合法，密码和确认密码是否一致
        boolean is_valid = validateInfo();
        if (is_valid) {
            // 把加载布局添加到根布局
            showLoadingUI();
            // 保存用户信息到 Bmob
            User user = saveUserInfoToUser();
            // 保存用户信息到 Bmob
            presenter.uploadUserInfo(user);
        }
    }

    /**
     * 检查用户名和密码是否合法
     * @return true :合法 / false : 不合法
     */
    private boolean validateInfo() {
        String username = etUsername.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        String confirm = etConfirmPwd.getText().toString().trim();
        boolean valid_name = ValidateUtils.validateUserName(username);
        boolean valid_password = ValidateUtils.validatePassword(pwd);

        if (!valid_name) {
            MyToast.show(this, "用户名不合法");
            etUsername.setFocusable(true);
            return false;
        } else if (!valid_password) {
            MyToast.show(this, "密码输入不合法");
            etPwd.getText().clear();
            etPwd.setFocusable(true);
            etConfirmPwd.getText().clear();
            return false;
        }
        if (!confirm.equals(pwd)) {
            MyToast.show(this, "确认密码与输入密码不一致");
            etConfirmPwd.getText().clear();
            etConfirmPwd.setFocusable(true);
            return false;
        }
        return true;
    }

    /**
     * 显示正在注册界面
     */
    private void showLoadingUI() {
        View view = getLayoutInflater().inflate(R.layout.loading, getRlRoot(), true);
        rl_loading = ButterKnife.findById(view, R.id.rl_loading);
        tv_info = ButterKnife.findById(view, R.id.tv_info);
        tv_info.setText("正在注册");
    }

    /**
     * 保存用户和密码到 user
     */
    private User saveUserInfoToUser() {
        User user = (User) getIntent().getSerializableExtra("user");
        user.setUserName(etUsername.getText().toString().trim());
        user.setPassword(etPwd.getText().toString().trim());
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hichat_pic/head.jpg";
        File file = new File(file_path);
        BmobFile bmobFile = new BmobFile(file);
        user.setIcon(bmobFile);
        return user;
    }

    /**
     * presenter 上传头像回调
     * @param success boolean
     */
    @Override
    public void uploadHeadImageCallback(boolean success) {
        if (success) {
            sendMessage(SUCCESS_UPLOAD_HEAD_IMAGE, "上传头像成功");
        } else {
            sendMessage(FAILED_UPLOAD_INFO, "注册失败");
        }
    }

    /**
     * presenter 上传用户信息回调
     * @param success boolean
     */
    @Override
    public void uploadUserInfoCallback(boolean success) {
        if (success) {
            sendMessage(SUCCESS_UPLOAD_INFO, "保存用户信息成功");
        } else {
            sendMessage(FAILED_UPLOAD_INFO, "注册失败");
        }
    }

    /**
     * presenter 创建环信账户回调
     * @param success boolean
     */
    @Override
    public void createEMAccountCallback(boolean success) {
        if (success) {
            sendMessage(SUCCESS_CREATE_EMACCOUNT,"创建用户成功");
        } else {
            sendMessage(FAILED_UPLOAD_INFO, "注册失败");
        }
    }

    /**
     * presenter 登录环信账户回调
     * @param success boolean
     */
    @Override
    public void loginEMAccountCallback(boolean success) {
        if (success) {
            sendMessage(SUCCESS_LOGIN_EMSERVER,"登录成功");
        } else {
            sendMessage(ERROR_LOGIN_EMSERVER,"登录失败");
        }

        // 进入 MainActivity , 设置 MainActivity 为 SingleTask 启动模式
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * 发送消息, 更新界面
     * @param what 消息标识
     * @param obj 消息内容
     */
    private void sendMessage(int what, String obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_loading != null && rl_loading.getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}