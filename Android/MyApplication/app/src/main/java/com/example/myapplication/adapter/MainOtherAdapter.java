package com.example.myapplication.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.fragment.DeviceFragment;
import com.example.myapplication.fragment.FireAlarmFragemnt;
import com.example.myapplication.fragment.TemperatureFragemnt;

public class MainOtherAdapter extends  BaseFragmentPagerAdapter<Integer> {

    public MainOtherAdapter(Context context, FragmentManager fm) {
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
            return TemperatureFragemnt.newInstance();
        } else if (position==1){
            return DeviceFragment.newInstance();
        } else {
            return FireAlarmFragemnt.newInstance();
        }
    }

}
