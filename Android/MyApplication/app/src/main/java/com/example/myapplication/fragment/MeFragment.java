package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.IntroductActivity;
import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页-我的界面
 */
public class MeFragment extends BaseCommonFragment {

    /**
     * 构造方法
     * <p>
     * 固定写法
     *
     * @return
     */
    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 返回布局文件
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public void relate(){
        startActivity(IntroductActivity.class);
    }
}