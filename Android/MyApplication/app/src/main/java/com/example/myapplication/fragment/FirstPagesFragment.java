package com.example.myapplication.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.dialog.ShowImageDialog;
import com.example.myapplication.event.HumanEvent;
import com.example.myapplication.util.ImageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;


/***
 * 首页Fragment
 * @author 胜利镇
 * @time 2020/8/7 8:16
 */
public class FirstPagesFragment extends BaseCommonFragment {

    @BindView(R.id.image)
    ImageView image;

    /**
     * 构造方法
     * <p>
     * 固定写法
     *
     * @return
     */
    public static FirstPagesFragment newInstance() {
        Bundle args = new Bundle();
        FirstPagesFragment fragment = new FirstPagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /***
     * 获取View
     */
    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_pages, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        handler = new Handler(Looper.getMainLooper());
        EventBus.getDefault().register(this);
    }

    int num = 0;
    Handler handler;
    Bitmap bitmap=null;

    //4、构建Runnable对象，在runnable中更新界面
    Runnable runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            image.setImageBitmap(bitmap);
        }
    };

    //订阅的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenWay(HumanEvent messageEvent) {
//        Log.d("dfjl111dfjl111", messageEvent.
//        ());
//        new ShowImageDialog(getContext() , "").show();
        status1.setText("监控状态:异常");
        num++;
        status2.setText("异常警告:" + num);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 对资源链接
                    URL url = new URL("URL");
                    // 打开输入流
                    InputStream inputStream = url.openStream();
                    // 对网上资源进行下载转换位图图片
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    //在其他线程更新UI
                    handler.post(runnableUi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    @BindView(R.id.status1)
    TextView status1;
    @BindView(R.id.status2)
    TextView status2;

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
