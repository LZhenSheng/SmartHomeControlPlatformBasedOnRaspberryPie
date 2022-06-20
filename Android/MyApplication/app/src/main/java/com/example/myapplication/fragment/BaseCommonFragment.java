package com.example.myapplication.fragment;

import android.content.Intent;
import android.util.Log;


/**
 * 通用Fragment逻辑
 */
public abstract class BaseCommonFragment extends BaseFragment {
    private static String TAG="MYTAGDETAIL";

    @Override
    protected void initData() {
        super.initData();
    }

    /**
     * 获取int值
     *
     * @param key
     * @return
     */
    protected int extraInt(String key) {
        return getArguments().getInt(key, -1);
    }


    /**
     * 启动界面
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * 启动界面并关闭当前界面
     *
     * @param clazz
     */
    protected void startActivityAfterFinishThis(Class<?> clazz) {
        startActivity(new Intent(getActivity(), clazz));
        getActivity().finish();
    }

    /**
     * 获取当前Fragment所在的Activity
     *
     * @return
     */
//    public BaseActivity getMainActivity() {
//        return (BaseActivity) getActivity();
//    }


    /***
     * 打印日志
     * @param content
     */
    public static void d(String content) {
        Log.i(TAG, content);
    }

}