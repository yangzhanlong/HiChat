package org.me.hichat.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;

import org.me.hichat.R;
import org.me.hichat.utils.FragmentFactory;
import org.me.hichat.utils.MyLogger;
import org.me.hichat.view.fragment.ContactFragment;
import org.me.hichat.view.fragment.ConversationFragment;
import org.me.hichat.view.fragment.LiveFragment;
import org.me.hichat.view.fragment.NearByFragment;
import org.me.hichat.view.fragment.PersonalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.bottomNacigationBar)
    BottomNavigationBar bottomNacigationBar;

    // 底部导航图片资源
    int[] barIcons = new int[] {
            R.drawable.ic_nav_nearby_active,
            R.drawable.ic_nav_live_active,
            R.drawable.ic_nav_conversation_active,
            R.drawable.ic_nav_contacts_active,
            R.drawable.ic_nav_personal_active,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initBottomNavigationBar();
    }


    Class[] fragments = new Class[] {
            NearByFragment.class,
            LiveFragment.class,
            ConversationFragment.class,
            ContactFragment.class,
            PersonalFragment.class
    };

    private void initFragment() {
        // 获取fragment管理器
        FragmentManager fm = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction ft = fm.beginTransaction();
        // 把fragment添加到容器中
        ft.add(R.id.container, FragmentFactory.getInstance(fragments[0]), "0");
        // 提交事务
        ft.commit();
    }


    TextBadgeItem numberBadgeItem = new TextBadgeItem();


    // 初始化底部导航栏
    private void initBottomNavigationBar() {
        // 底部导航标签
        String[] barLabels = getResources().getStringArray(R.array.bottombarlabel);

        bottomNacigationBar
                .setBarBackgroundColor(R.color.bottombarcolor) // 设置背景颜色
                .addItem(new BottomNavigationItem(barIcons[0], barLabels[0]))
                .addItem(new BottomNavigationItem(barIcons[1], barLabels[1]))
                .addItem(new BottomNavigationItem(barIcons[2], barLabels[2]).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(barIcons[3], barLabels[3]))
                .addItem(new BottomNavigationItem(barIcons[4], barLabels[4]))
                .setActiveColor(R.color.activecolor) // 选中颜色
                .setInActiveColor(R.color.inactivecolor) // 未选中颜色
                .setMode(BottomNavigationBar.MODE_FIXED)  // 设置混合模式
                .setFirstSelectedPosition(0)
                .initialise(); // 初始化

        numberBadgeItem.setText("10")
                .setGravity(Gravity.RIGHT|Gravity.TOP);
        numberBadgeItem.hide();

        // 设置tab点击监听
        bottomNacigationBar.setTabSelectedListener(this);

    }

    // tab选中调用 (显示fragment)
    @Override
    public void onTabSelected(int position) {
        //MyLogger.i("onTabSelected:" + position);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(position + "");
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment == null) {
            ft.add(R.id.container, FragmentFactory.getInstance(fragments[position]), position + "");
        } else {
            ft.show(fragment);
        }
        ft.commit();
    }

    // 之前选中的tab未被选中 (隐藏fragment)
    @Override
    public void onTabUnselected(int position) {
        //MyLogger.i("onTabUnselected:" + position);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(position + "");
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }

    @Override
    public void onTabReselected(int position) {
        MyLogger.i("onTabReselected:" + position);
    }
}
