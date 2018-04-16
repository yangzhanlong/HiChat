package org.me.hichat.view.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import org.me.hichat.R;
import org.me.hichat.base.BaseFragment;

/**
 * Created by user on 2018/4/16.
 *
 */

public class ConversationFragment extends BaseFragment {
    @Override
    public void setEmptyViewInfo(ImageView ivEmpty, TextView tvInfo) {
        ivEmpty.setImageResource(R.drawable.ic_guest_messag_empty);
        tvInfo.setText("可以让附近的人发收消息");
    }

    @Override
    public void setDefaultTitle(TextView defaultTitle) {
        defaultTitle.setText("HI聊");
    }
}
