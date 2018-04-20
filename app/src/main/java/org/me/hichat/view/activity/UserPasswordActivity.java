package org.me.hichat.view.activity;

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

import org.me.hichat.R;
import org.me.hichat.base.BaseActivity;
import org.me.hichat.utils.MyToast;
import org.me.hichat.utils.ValidateUtils;
import org.me.hichat.wrap.SimpleTextWatcher;

import butterknife.ButterKnife;

public class UserPasswordActivity extends BaseActivity {

    TextInputEditText etPwd;
    TextInputEditText etConfirmPwd;
    Button btRegister;
    TextInputEditText etUsername;
    private View rl_loading;
    private TextView tv_info;


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

    private void register() {
        //校验用户名、密码是否合法，密码和确认密码是否一致
        boolean is_valid = validateInfo();
        if (is_valid) {
            // 把加载布局添加到根布局
            showLoadingUI();
        }
    }

    private void showLoadingUI() {
        View view = getLayoutInflater().inflate(R.layout.loading, getRlRoot(), true);
        rl_loading = ButterKnife.findById(view, R.id.rl_loading);
        tv_info = ButterKnife.findById(view, R.id.tv_info);
        tv_info.setText("正在注册");
    }

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