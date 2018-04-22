package org.me.hichat.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import org.me.hichat.R;
import org.me.hichat.utils.CheckPermissionUtils;

public class SplashActivity extends AppCompatActivity {

    private static final int ENTER_MAIN_UI = 100;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ENTER_MAIN_UI:
                    // 进入主界面
                    enterMainUI();
                    break;
            }
        }
    };

    private void enterMainUI() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 检测权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            // 需要申请的权限都申请了
            // 2s 后进入主界面
            handler.sendEmptyMessageDelayed(ENTER_MAIN_UI, 2000);
        } else {
            // 申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    /**
     * 申请权限返回
     * @param requestCode 返回码
     * @param permissions 权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int grantResult : grantResults) {
                // 拒绝操作
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    finish();
                    return;
                }
            }
            // 1s 进入主界面
            handler.sendEmptyMessageDelayed(ENTER_MAIN_UI, 1000);
        }
    }
}