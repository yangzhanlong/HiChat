package org.me.hichat.utils;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Created by user on 2018/4/16.
 * fragment工厂类
 */

public final class FragmentFactory {
    private static HashMap<Class, Fragment> map = new HashMap<>();


    public static synchronized Fragment getInstance(Class<? extends Fragment> clazz) {
        Fragment fragment = map.get(clazz);
        if (fragment == null) {
            try {
                fragment = clazz.newInstance();
                map.put(clazz, fragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }
}
