package com.example.myapplication.fragment;

import android.net.DhcpInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.domain.DHT;
import com.example.myapplication.util.DBUtils;

import butterknife.BindView;

public class TemperatureFragemnt  extends BaseCommonFragment  {

    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.temperature)
    TextView temperature;

    @Override
    protected void initData() {
        super.initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DHT dht=DBUtils.getTemperature();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        temperature.setText("室内溫度:"+dht.getTemperation()+"度");
                        humidity.setText("室内湿度:"+dht.getHumdity()+"% ");
                    }
                });

            }
        }).start();
    }

    /**
     * 构造方法
     * <p>
     * 固定写法
     *
     * @return
     */
    public static TemperatureFragemnt newInstance() {
        Bundle args = new Bundle();
        TemperatureFragemnt fragment = new TemperatureFragemnt();
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
        return inflater.inflate(R.layout.fragment_device, container, false);
    }
}
