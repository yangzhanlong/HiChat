package org.me.hichat.view.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import org.me.hichat.base.BaseFragment;

/**
 * Created by user on 2018/4/16.
 * 附近Fragment
 */

public class NearByFragment extends BaseFragment {
    @Override
    public void setEmptyViewInfo(ImageView ivEmpty, TextView tvInfo) {

    }

    @Override
    public void setDefaultTitle(TextView defaultTitle) {
        defaultTitle.setText("附近");
    }
}
