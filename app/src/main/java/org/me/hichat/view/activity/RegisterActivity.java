package org.me.hichat.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import org.me.hichat.R;
import org.me.hichat.base.BaseActivity;
import org.me.hichat.wrap.SimpleTextWatcher;

import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextInputEditText etNickName;
    private Button btNext;


    @Override
    protected View addLayoutToFrameLayout(LayoutInflater layoutInflater, FrameLayout frameLayout) {
        //false :加载布局到内存   true:加载布局到指定的容器
        View view = layoutInflater.inflate(R.layout.activity_register, frameLayout, true);
        return view;
    }

    @Override
    protected void initView(View view) {
        etNickName = ButterKnife.findById(view, R.id.et_nickname);
        btNext = ButterKnife.findById(view, R.id.bt_next);
        // 设置textInput监听
        etNickName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (TextUtils.isEmpty(s.toString())) {
                    btNext.setEnabled(false);
                } else {
                    btNext.setEnabled(true);
                }
            }
        });

        // 设置button监听
        btNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                enterPersonalInfoActivity();
                break;
            case R.id.ib_back:
                // 显示退出对话框
                showExitDialog();
                break;
        }
    }

    private void enterPersonalInfoActivity() {
        Intent intent = new Intent(this, PersonalInfoActivity.class);
        String nickName = etNickName.getText().toString().trim();
        intent.putExtra("nickName", nickName);
        startActivity(intent);
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要放弃注册吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            showExitDialog();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
