package org.me.hichat.view.fragment;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;

import org.me.hichat.R;
import org.me.hichat.base.BaseFragment;

/**
 * Created by user on 2018/4/16.
 * 附近Fragment
 */

public class NearByFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @Override
    public void setEmptyViewInfo(ImageView ivEmpty, TextView tvInfo) {

    }

    @Override
    public void setDefaultTitle(TextView defaultTitle) {
        defaultTitle.setText("附近");
    }

    @Override
    public View addLeftHeader(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.nearby_header, viewGroup, true);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab);
        addTabs(tabLayout);
        return view;
    }

    /**
     * 添加tab
     * @param tabLayout TabLayout
     */
    private void addTabs(TabLayout tabLayout) {
        // 获取tab数组
        String[] tabs = getContext().getResources().getStringArray(R.array.nearby);
        for (int i = 0; i < tabs.length; i++) {
            //tabLayout.addTab(tabLayout.newTab().setText(tab));
            TextView textView = new TextView(getContext());
            textView.setText(tabs[i]);
            textView.setTextSize(DensityUtil.sp2px(getContext(), 6));
            textView.setGravity(Gravity.CENTER);
            if (i == 0) {
                textView.setTextColor(Color.BLACK);
            } else {
                textView.setTextColor(Color.DKGRAY);
            }
            tabLayout.addTab(tabLayout.newTab().setCustomView(textView));
            tabLayout.addOnTabSelectedListener(this);
        }
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
    }

    // 选中tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        TextView tv = (TextView) tab.getCustomView();
        assert tv != null;
        tv.setTextColor(Color.BLACK);
    }

    // 切换tab
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        TextView tv = (TextView) tab.getCustomView();
        assert tv != null;
        tv.setTextColor(Color.DKGRAY);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
