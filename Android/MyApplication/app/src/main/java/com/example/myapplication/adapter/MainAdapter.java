package com.example.myapplication.adapter;

import android.bluetooth.BluetoothClass;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.fragment.DeviceFragment;
import com.example.myapplication.fragment.FireAlarmFragemnt;
import com.example.myapplication.fragment.FirstPagesFragment;
import com.example.myapplication.fragment.MeFragment;
import com.example.myapplication.fragment.MessageFrgament;
import com.example.myapplication.fragment.TemperatureFragemnt;

/**
 * 主界面ViewPager的Adapter
 *
 */
public class MainAdapter extends BaseFragmentPagerAdapter<Integer> {

    public MainAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    /**
     * 返回Fragment
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FirstPagesFragment.newInstance();
        } else if (position==1){
            return TemperatureFragemnt.newInstance();
        }else if(position==2){
            return DeviceFragment.newInstance();
        }else if(position==3){
            return FireAlarmFragemnt.newInstance();
        } else {
            return MeFragment.newInstance();
        }
    }


}