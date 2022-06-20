package com.example.myapplication.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.util.ImageUtil;

public class ShowImageDialog extends Dialog {

    private ImageView imageView;
    private Object showImage;
    private boolean canCancle;

    public ShowImageDialog(Context context, Object showImage) {
        this(context, showImage, true);
    }

    public ShowImageDialog(Context context, Object showImage, boolean canCancle) {
        super(context, R.style.ShowImageDialog);
        this.showImage = showImage;
        this.canCancle = canCancle;
    }

    public void setShowImage(Object showImage) {
        this.showImage = showImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        imageView = findViewById(R.id.imageView);
        ImageUtil.show(getContext(),imageView,"URI");
        // 设置点击屏幕或物理返回键，dialog是否消失
        setCanceledOnTouchOutside(canCancle);
        //设置点击返回键，dialog是否消失
        setCancelable(canCancle);
        Window w = getWindow();
        WindowManager.LayoutParams lp = null;
        if (w != null) {
            lp = w.getAttributes();
            lp.x = 0;
            lp.y = 40;
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        onWindowAttributesChanged(lp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
