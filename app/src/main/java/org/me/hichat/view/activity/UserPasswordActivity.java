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
import org.me.hichat.wrap.SimpleTextWatcher;

import butterknife.ButterKnife;

public class UserPasswordActivity extends BaseActivity {

    TextInputEditText etPwd;
    TextInputEditText etConfirmPwd;
    Button btRegister;
    TextInputEditText etUsername;


    @Override
    protected View addLayoutToFrameLayout(LayoutInflater layoutInflater, FrameLayout frameLayout) {
        View view = layoutInflater.inflate(R.layout.activity_user_password, frameLayout, true);
        return view;
    }

    @Override
    protected void initView(View view) {
        etPwd = ButterKnife.findById(view,R.id.et_pwd);
        etConfirmPwd = ButterKnife.findById(view,R.id.et_confirm_pwd);
        btRegister = ButterKnife.findById(view,R.id.bt_register);
        etUsername = ButterKnife.findById(view,R.id.et_username);

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
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    if(TextUtils.isEmpty(etUsername.getText().toString().trim())){
                        MyToast.show(UserPasswordActivity.this,"用户名输入为空");
                    }
                }
                return false;
            }
        });

        etConfirmPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (btRegister.isEnabled()) {
                        register();
                    } else {
                        MyToast.show(UserPasswordActivity.this,"用户名或者密码没有输入");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void register() {
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
        super.onClick(view);
        switch (view.getId()) {
            case R.id.bt_register:
                register();
                break;
        }
    }
}
