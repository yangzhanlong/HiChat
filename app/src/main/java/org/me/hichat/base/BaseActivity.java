package org.me.hichat.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.me.hichat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.content)
    FrameLayout frameLayout;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        // 把布局添加到frameLayout
        View view = addLayoutToFrameLayout(getLayoutInflater(), frameLayout);
        // 初始化布view
        initView(view);
    }

    // 设置标题内容
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    //返回根布局 (给loading布局使用)
    public RelativeLayout getRlRoot(){
        return rlRoot;
    }

    @OnClick(R.id.ib_back)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_back:
                finish();
                break;
        }
    }

    protected abstract void initView(View view);
    protected abstract View addLayoutToFrameLayout(LayoutInflater layoutInflater, FrameLayout frameLayout);
}