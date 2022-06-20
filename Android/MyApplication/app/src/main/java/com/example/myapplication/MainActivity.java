package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myapplication.adapter.MainAdapter;
import com.example.myapplication.event.HumanEvent;
import com.example.myapplication.mqtt.MqttManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MqttManager mqttManager;

    public void init(){
        vp=findViewById(R.id.vp);
        indicator_iv1=findViewById(R.id.indicator_iv1);
        indicator_iv2=findViewById(R.id.indicator_iv2);
        indicator_iv5=findViewById(R.id.indicator_iv5);
        indicator_iv3=findViewById(R.id.indicator_iv3);
        indicator_iv4=findViewById(R.id.indicator_iv4);
        indicator_iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackground();
                indicator_iv1.setImageResource(R.mipmap.home_selected);
                vp.setCurrentItem(0);
            }
        });
        indicator_iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackground();
                indicator_iv2.setImageResource(R.mipmap.notice_selected);
                vp.setCurrentItem(1);
            }
        });
        indicator_iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackground();
                indicator_iv3.setImageResource(R.mipmap.notice_selected);
                vp.setCurrentItem(2);
            }
        });
        indicator_iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackground();
                indicator_iv4.setImageResource(R.mipmap.my_selected);
                vp.setCurrentItem(3);
            }
        });
        indicator_iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackground();
                indicator_iv5.setImageResource(R.mipmap.my_selected);
                vp.setCurrentItem(4);
            }
        });
        initData();
    }
    private static final String TAG = "MainActivity";
    ViewPager vp;

    ImageView indicator_iv2;
    ImageView indicator_iv1;
    ImageView indicator_iv5;
    ImageView indicator_iv3;
    ImageView indicator_iv4;


    private void clearBackground() {
        indicator_iv1.setImageResource(R.mipmap.home);
        indicator_iv3.setImageResource(R.mipmap.device);
        indicator_iv2.setImageResource(R.mipmap.th);
        indicator_iv4.setImageResource(R.mipmap.notice);
        indicator_iv5.setImageResource(R.mipmap.me);
    }

    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mqttManager= MqttManager.getInstance(this);
        mqttManager.connectServer();
    }


    public void initData() {

        //缓存页面数量 默认是缓存一个
        vp.setOffscreenPageLimit(3);

        //主界面页面MainAda
        adapter = new MainAdapter(getApplicationContext(), getSupportFragmentManager());
        vp.setAdapter(adapter);

        ArrayList<Integer> datas = new ArrayList<>();
        datas.add(0);
        datas.add(1);
        datas.add(2);
        datas.add(3);
        datas.add(4);
        adapter.setDatum(datas);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clearBackground();
                switch (position){
                    case 0:
                        indicator_iv1.setImageResource(R.mipmap.home_selected);
                        break;
                    case 1:
                        indicator_iv2.setImageResource(R.mipmap.th_selected);
                        break;
                    case 2:
                        indicator_iv3.setImageResource(R.mipmap.device_selected);
                        break;
                    case 3:
                        indicator_iv4.setImageResource(R.mipmap.notice_selected);
                        break;
                    case 4:
                        indicator_iv5.setImageResource(R.mipmap.me_selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
    }

    boolean flag1=false;
    boolean flag2=false;

    public  void fan(View view){
        flag2=!flag2;
        if(flag2){
            view.setBackgroundResource(R.drawable.fan_on);
            mqttManager.pubTopic("/gxtyIZJ9otJ/myapp/user/putFan","{fan:1}");
        }else{
            view.setBackgroundResource(R.drawable.fan_off);
            mqttManager.pubTopic("/gxtyIZJ9otJ/myapp/user/putFan","{fan:0}");
        }
    }
    public void light(View view){
        flag1=!flag1;
        if(flag1){
            view.setBackgroundResource(R.drawable.led_on);
            mqttManager.pubTopic("/gxtyIZJ9otJ/myapp/user/putLight","{light:1}");
        }else{
            view.setBackgroundResource(R.drawable.led_off);
            mqttManager.pubTopic("/gxtyIZJ9otJ/myapp/user/putLight","{light:0}");
        }
    }

    public void relate(View view){
        final Dialog bgSetDialog = new Dialog(MainActivity.this,R.style.BottomDialogStyle);
        //填充对话框的布局
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_introduct, null);
        ImageView textView=view1.findViewById(R.id.finish);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgSetDialog.dismiss();
            }
        });

        //将布局设置给Dialog
        bgSetDialog.setContentView(view1);
        //获取当前Activity所在的窗体
        Window dialogWindow = bgSetDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainActivity.this.getWindowManager().getDefaultDisplay().getWidth()*0.95);
        lp.y = 60; //设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp); //将属性设置给窗体
        bgSetDialog.show();//显示对话框
    }

    public void description(View view){
        final Dialog bgSetDialog = new Dialog(MainActivity.this,R.style.BottomDialogStyle);
        //填充对话框的布局
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_relative, null);
        ImageView textView=view1.findViewById(R.id.finish);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgSetDialog.dismiss();
            }
        });

        //将布局设置给Dialog
        bgSetDialog.setContentView(view1);
        //获取当前Activity所在的窗体
        Window dialogWindow = bgSetDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainActivity.this.getWindowManager().getDefaultDisplay().getWidth()*0.95);
        lp.y = 60; //设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp); //将属性设置给窗体
        bgSetDialog.show();//显示对话框
    }

    public void wait(View view){
        final Dialog bgSetDialog = new Dialog(MainActivity.this,R.style.BottomDialogStyle);
        //填充对话框的布局
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_wait, null);
        ImageView textView=view1.findViewById(R.id.finish);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgSetDialog.dismiss();
            }
        });

        //将布局设置给Dialog
        bgSetDialog.setContentView(view1);
        //获取当前Activity所在的窗体
        Window dialogWindow = bgSetDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainActivity.this.getWindowManager().getDefaultDisplay().getWidth()*0.95);
        lp.y = 60; //设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp); //将属性设置给窗体
        bgSetDialog.show();//显示对话框
    }


}
