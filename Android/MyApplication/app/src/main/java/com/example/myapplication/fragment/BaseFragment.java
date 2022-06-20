package com.example.myapplication.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

/**
 * 所有Fragment通用父类
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 初始化控件
     */
    protected void initViews(){
        ButterKnife.bind(this,getView());
    }

    /**
     * 设置数据
     */
    protected void initData(){
    }

    /**
     * 设置监听器
     */
    protected void initListener(){
    }

    /**
     * 返回要显示的View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getLayoutView(inflater,container,savedInstanceState);
    }

    /**
     * 状态栏文字显示白色
     * 内容显示到状态栏下面
     */
    protected void lightStatusBar(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置为自定义颜色
            getActivity().getWindow().setStatusBarColor(getResources().getColor(id));
        }
    }

    /**
     * 返回View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View getLayoutView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * View创建完毕了
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initData();
        initListener();
    }
}
