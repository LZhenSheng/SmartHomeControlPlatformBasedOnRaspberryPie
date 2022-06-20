package com.example.myapplication.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.R;

/**
 * 图片相关工具类
 */
public class ImageUtil {

    public static void showAvatar(Activity activity, ImageView view, int id){

        show(activity,view,id);
    }
    /**
     * 显示头像
     *
     * @param activity
     * @param view
     * @param uri
     */
    public static void showAvatar(Activity activity, ImageView view, String uri) {
        if (uri==null) {
            //没有头像

            //显示默认头像
            //iv_avatar.setImageResource(R.drawable.placeholder);

//            show(activity, view, R.drawable.fan_off);
        } else {
            //有头像

            if (uri.startsWith("http")) {
                //绝对路径
                showFull(activity, view, uri);
            } else {
                //相对路径
                show(activity, view, uri);
            }
        }
    }
    /**
     * 显示本地图片
     *
     * @param context
     * @param view
     * @param data
     */
    public static void showLocalImage(Context context, ImageView view, String data) {
        //获取通用配置
        RequestOptions options = getCommonRequestOptions();

        //使用Glide显示图片
        Glide.with(context)
                .load(data)
                .apply(options)
                .into(view);
    }

    /**
     * 显示绝对路径图片
     *
     * @param activity
     * @param view
     * @param uri
     */
    public static void showFull(Activity activity, ImageView view, String uri) {

        //获取功能配置
        RequestOptions options = getCommonRequestOptions();

        //显示图片
        Glide.with(activity)
                .load(uri)
                .apply(options)
                .into(view);
    }


//    /**
//     * 显示相对路径图片
//     *
//     * @param activity
//     * @param view
//     * @param uri
//     */
//    public static void show(Activity activity, ImageView view, String uri) {
//        //将图片地址转为绝对路径
//        uri = String.format(Constant.RESOURCE_ENDPOINT, uri);
//
//        showFull(activity, view, uri);
//    }

    /**
     * 显示相对路径图片
     *
     * @param view
     * @param uri
     */
    public static void show(Context context, ImageView view, String uri) {

        showFull(context, view, uri);
    }

    /**
     * 显示绝对路径图片
     *
     * @param context
     * @param view
     * @param uri
     */
    public static void showFull(Context context, ImageView view, String uri) {

        //获取功能配置
        RequestOptions options = getCommonRequestOptions();

        //显示图片
        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(options)
                .into(view);
    }

    /**
     * 显示资源目录图片
     *
     * @param activity
     * @param view
     * @param resourceId
     */
    public static void show(Activity activity, ImageView view, @RawRes @DrawableRes @Nullable int resourceId) {
        //获取公共配置
        RequestOptions options = getCommonRequestOptions();

        Glide.with(activity)
                .load(resourceId)
                .apply(options)
                .into(view);
    }

    /**
     * 获取公共配置
     *
     * @return
     */
    private static RequestOptions getCommonRequestOptions() {
        //创建配置选项
        RequestOptions options = new RequestOptions();

        //占位图
//        options.placeholder(R.drawable.fan_off);

        //出错后显示的图片
        //包括：图片不存在等情况
//        options.error(R.drawable.fan_on);

        //从中心裁剪
        options.centerCrop();

        return options;
    }


    /**
     * 显示资源图片
     *
     * @param context
     * @param view
     * @param resourceId
     */
    public static void showLocalImage(Context context, ImageView view, int resourceId) {
        //获取通用配置
        RequestOptions options = getCommonRequestOptions();

        //显示图片
        Glide.with(context)
                .load(resourceId)
                .apply(options)
                .into(view);
    }

}