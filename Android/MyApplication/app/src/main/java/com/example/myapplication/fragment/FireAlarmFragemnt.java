package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.event.FireEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class FireAlarmFragemnt extends BaseCommonFragment{

    /**
     * 构造方法
     * <p>
     * 固定写法
     *
     * @return
     */
    public static FireAlarmFragemnt newInstance() {
        Bundle args = new Bundle();
        FireAlarmFragemnt fragment = new FireAlarmFragemnt();
        fragment.setArguments(args);
        return fragment;
    }

    /***
     * 获取View
     */
    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fire_alarm, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @BindView(R.id.status)
    TextView status;

    //订阅的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenWay(FireEvent messageEvent) {
        status.setText("异常");
    }
}
