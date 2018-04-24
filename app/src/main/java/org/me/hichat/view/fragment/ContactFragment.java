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
 *
 */

public class ContactFragment extends BaseFragment{
    @Override
    public void setEmptyViewInfo(ImageView ivEmpty, TextView tvInfo) {
        ivEmpty.setImageResource(R.drawable.ic_guest_contact_empty);
        tvInfo.setText("可以让附近的人互动");;
    }

    @Override
    public void setDefaultTitle(TextView defaultTitle) {
        defaultTitle.setText("通讯录");
    }

    @Override
    public View addLeftHeader(LayoutInflater inflater, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View addRightHeader(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.contacts_fragment_header, viewGroup, true);
        return view;
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
        setLoginFormatTitle("好友", 0);
    }
}
