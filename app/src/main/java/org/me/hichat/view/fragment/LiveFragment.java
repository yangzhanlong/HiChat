package org.me.hichat.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.me.hichat.base.BaseFragment;

/**
 * Created by user on 2018/4/16.
 * 直播
 */

public class LiveFragment extends BaseFragment{
    @Override
    public void setEmptyViewInfo(ImageView ivEmpty, TextView tvInfo) {

    }

    @Override
    public void setDefaultTitle(TextView defaultTitle) {
        defaultTitle.setText("直播");
    }

    @Override
    public View addLeftHeader(LayoutInflater inflater, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View addRightHeader(LayoutInflater inflater, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View addAfLoginView(LayoutInflater inflater, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void initAfLoginView(View view) {

    }

    /**
     * 显示登录后的标题
     */
    @Override
    public void showLoginHeader() {
        super.showLoginHeader();
        setLoginTitle("嗨聊直播");
    }
}
