package org.me.hichat.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.me.hichat.R;
import org.me.hichat.model.event.MessageEvent;
import org.me.hichat.utils.EMUtils;
import org.me.hichat.view.activity.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by user on 2018/4/16.
 * BaseFragment
 */

public abstract class BaseFragment extends Fragment {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.header_left)
    LinearLayout headerLeft;
    @BindView(R.id.header_right)
    LinearLayout headerRight;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.empty_ll)
    LinearLayout emptyLl;
    @BindView(R.id.login_layout)
    FrameLayout loginLayout;
    Unbinder unbinder;

    private LayoutInflater mInflater;
    private View leftHeader;
    private View rightHeader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = View.inflate(getContext(), R.layout.base_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        boolean isLogin = EMUtils.isLogin();
        if (isLogin) {
            hideEmptyView();
            hideDefaultTitle();
            showLoginHeader();
        } else {
           showLogOutHeader();
        }
        return view;
    }

    /**
     * 显示登录后的标题内容
     */
    public void showLoginHeader() {
        leftHeader = addLeftHeader(mInflater, headerLeft);
        rightHeader = addRightHeader(mInflater, headerRight);
        // 让不同的Fragment去处理标题文字的改变
    }

    /**
     * 设置登录后的标题
     * @param title 标题
     */
    public void setLoginTitle(String title) {
       tvTitle.setText(title);
    }

    /**
     * 设置格式化的登录后标题
     * @param title 标题内容
     * @param size 大小
     */
    public void setLoginFormatTitle(String title, int size) {
        String content = String.format(getString(R.string.formattitle), title, size);
        tvTitle.setText(content);
    }

    /**
     * 显示未登录的标题
     */
    private void showLogOutHeader() {
        setDefaultTitle(tvTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        ibBack.setVisibility(View.GONE);
        setEmptyViewInfo(ivEmpty,tvInfo);
        setDefaultTitle(tvTitle);
    }

    @OnClick({R.id.ib_back, R.id.bt_register, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                break;
            case R.id.bt_register:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.bt_login:
                break;
        }
    }

    public abstract void setEmptyViewInfo(ImageView ivEmpty, TextView tvInfo);
    public abstract void setDefaultTitle(TextView defaultTitle);
    // 头
    public abstract View addLeftHeader(LayoutInflater inflater, ViewGroup viewGroup);
    public abstract View addRightHeader(LayoutInflater inflater, ViewGroup viewGroup);
    // 隐藏空界面
    public void hideEmptyView() {
        emptyLl.setVisibility(View.GONE);
    }

    // 显示空界面
    public void showEmptyView() {
        emptyLl.setVisibility(View.VISIBLE);
    }

    // 隐藏默认标题
    public void hideDefaultTitle() {
        tvTitle.setText("");
    }

    // 添加登录后的布局
    public abstract View addAfLoginView(LayoutInflater inflater, ViewGroup viewGroup);
    // 初始化
    public abstract void initAfLoginView(View view);

    /**
     * EventBus 回调方法
     * @param event MessageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getType() == MessageEvent.LOGIN) {
            // 更新UI界面
            updateUI();
        }
    }

    /**
     * 更新UI界面
     */
    private void updateUI() {
        // 更新标题
        updateHeaderUI();
        // 更新内容
        updateBodyUI();
    }

    /**
     * 更新标题内容
     */
    private void updateHeaderUI() {
        boolean isLogin = EMUtils.isLogin();
        if (isLogin) {
            // 登录后的标题
            hideDefaultTitle();
            showLoginHeader();
        } else {
            // 未登录标题
            showLogOutHeader();
        }
    }

    /**
     * 更新Body内容
     */
    private void updateBodyUI() {
        boolean isLogin = EMUtils.isLogin();
        if (isLogin) {
            // 登陆后内容
            hideEmptyView();
            addAfLoginView(mInflater, loginLayout);
        } else {
            // 未登录内容
            showEmptyView();
        }
    }
}