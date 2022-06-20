package com.example.myapplication.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MainAdapter;
import com.example.myapplication.adapter.MainOtherAdapter;

import java.util.ArrayList;

import butterknife.BindView;


/***
* 用户聊天界面Fragment
* @author 胜利镇
* @time 2020/10/13
* @dec 
*/
public class MessageFrgament extends BaseCommonFragment {

    MainOtherAdapter adapter;


    @BindView(R.id.vp)
    ViewPager vp;


    @BindView(R.id.indicator_iv2)
    TextView indicator_iv2;
    @BindView(R.id.indicator_iv3)
    TextView indicator_iv3;
    @BindView(R.id.indicator_iv4)
    TextView indicator_iv4;
    /**
     * 构造方法
     *
     * 固定写法
     *
     * @return
     */
    public static MessageFrgament newInstance() {
        Bundle args = new Bundle();
        MessageFrgament fragment = new MessageFrgament();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message,container,false);
    }

    @Override
    protected void initData() {
        super.initData();
        //缓存页面数量 默认是缓存一个
        vp.setOffscreenPageLimit(3);

        //主界面页面MainAda
        adapter = new MainOtherAdapter(getContext(), getActivity().getSupportFragmentManager());
        vp.setAdapter(adapter);

        ArrayList<Integer> datas = new ArrayList<>();
        datas.add(0);
        datas.add(1);
        datas.add(2);
        adapter.setDatum(datas);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clearBackground();
                switch (position) {
                    case 0:
                        indicator_iv2.setTextColor(Color.parseColor("#ff669900"));
                        break;
                    case 1:
                        indicator_iv3.setTextColor(Color.parseColor("#ff669900"));
                        break;
                    case 2:
                        indicator_iv4.setTextColor(Color.parseColor("#ff669900"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicator_iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackground();
                vp.setCurrentItem(0,false);
                indicator_iv2.setTextColor(Color.parseColor("#ff669900"));
            }
        });
        indicator_iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackground();
                vp.setCurrentItem(1,false);
                indicator_iv3.setTextColor(Color.parseColor("#ff669900"));
            }
        });
        indicator_iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackground();
                vp.setCurrentItem(2,false);
                indicator_iv4.setTextColor(Color.parseColor("#ff669900"));
            }
        });
    }

    private void clearBackground() {
        indicator_iv2.setTextColor(Color.parseColor("#aaaaaa"));
        indicator_iv3.setTextColor(Color.parseColor("#aaaaaa"));
        indicator_iv4.setTextColor(Color.parseColor("#aaaaaa"));
    }
}
