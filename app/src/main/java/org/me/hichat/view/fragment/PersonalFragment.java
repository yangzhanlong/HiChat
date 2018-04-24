package org.me.hichat.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.me.hichat.R;
import org.me.hichat.base.BaseFragment;

/**
 * Created by user on 2018/4/16.
 * 个人Fragment
 */

public class PersonalFragment extends BaseFragment {
    @Override
    public void setEmptyViewInfo(ImageView ivEmpty, TextView tvInfo) {
        ivEmpty.setImageResource(R.drawable.ic_guest_person_empty);
        tvInfo.setText("可以让附近的人发现你");
    }

    @Override
    public void setDefaultTitle(TextView defaultTitle) {
        defaultTitle.setText("个人");
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

    @Override
    public void showLoginHeader() {
        super.showLoginHeader();
        setLoginTitle("我的");
    }
}
