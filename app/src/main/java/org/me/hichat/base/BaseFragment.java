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

import org.me.hichat.R;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.base_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ibBack.setVisibility(View.GONE);
        setEmptyViewInfo(ivEmpty,tvInfo);
        setDefaultTitle(tvTitle);
    }

    public abstract void setEmptyViewInfo(ImageView ivEmpty, TextView tvInfo);
    public abstract void setDefaultTitle(TextView defaultTitle);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
}
