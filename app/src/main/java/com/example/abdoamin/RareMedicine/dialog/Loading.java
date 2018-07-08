package com.example.abdoamin.RareMedicine.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.health.TimerStat;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.GradientBackgroundPainter;
import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.activity.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static java.lang.Thread.sleep;

/**
 * Created by Abdo Amin on 3/18/2018.
 */

public class Loading extends Dialog {
    private Unbinder unbinder;
    private Context mContext;
    private AnimationDrawable animationDrawable;
    @BindView(R.id.imageView8)
    ImageView imageView;
    @BindView(R.id.textView2)
    TextView loadingTextView;

    private JumpingBeans jumpingBeans2;

    private GradientBackgroundPainter gradientBackgroundPainter;


    public Loading(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        unbinder = ButterKnife.bind(this, Loading.this);
//
//        //image background animation color
//        animationDrawable = (AnimationDrawable) imageView.getBackground();
//        animationDrawable.setEnterFadeDuration(5000);
//        animationDrawable.setExitFadeDuration(2000);
//        if (animationDrawable != null && !animationDrawable.isRunning())
//            animationDrawable.start();

        //new grediant
        final int[] drawables = new int[4];
        drawables[0] = R.drawable.gradient_4;
        drawables[1] = R.drawable.gradient_3;
        drawables[2] = R.drawable.gradient_2;
        drawables[3] = R.drawable.gradient_1;
        gradientBackgroundPainter = new GradientBackgroundPainter(imageView, drawables);
        gradientBackgroundPainter.start();


//     t = new Thread() {
//
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        Thread.sleep(1000);
//                        ((Activity) mContext).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (loadingTextView.getText().equals("Loading..."))
//                                    loadingTextView.setText("Loading.");
//                                else
//                                    loadingTextView.setText(loadingTextView.getText() + ".");
//                            }
//                        });
//                    }
//                } catch (InterruptedException e) {
//                }
//            }
//        };
//
//        t.start();
        jumpingBeans2= JumpingBeans.with(loadingTextView)
                .makeTextJump(0, loadingTextView.getText().toString().length())
                .setIsWave(true)
                .setLoopDuration(1800)//6*300 ms
                .setWavePerCharDelay(300)
                .build();
    }

    @Override
    public void dismiss() {
        unbinder.unbind();
//        if (animationDrawable != null && animationDrawable.isRunning())
//            animationDrawable.stop();
//        t.interrupt();
        jumpingBeans2.stopJumping();
        super.dismiss();
        gradientBackgroundPainter.stop();

    }


    @Override
    public void onBackPressed() {
        dismiss();
        ((Activity) mContext).onBackPressed();

    }

}

