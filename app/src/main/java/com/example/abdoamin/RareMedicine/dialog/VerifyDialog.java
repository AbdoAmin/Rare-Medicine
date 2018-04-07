package com.example.abdoamin.RareMedicine.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.activity.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Abdo Amin on 3/18/2018.
 */

public class VerifyDialog extends Dialog {
    private Unbinder unbinder;
    private Context mContext;
    public VerifyDialog(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.verification_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        unbinder= ButterKnife.bind(this,VerifyDialog.this);
    }

    @OnClick({R.id.verification_dialog_resend_verification_btn,R.id.verification_dialog_log_out_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verification_dialog_resend_verification_btn:
                Utiltis.currentUser.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(mContext, "verify resend to your email "+Utiltis.currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(mContext, "This Email Not Found "+Utiltis.currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.verification_dialog_log_out_btn:
                Utiltis.mAuth.signOut();
                mContext.startActivity(new Intent(mContext, LogInActivity.class));
                ((Activity) mContext).finish();
                dismiss();
                break;
            default:
                dismiss();
                break;
        }

    }

    @Override
    public void dismiss() {
        unbinder.unbind();
        super.dismiss();

    }

    @Override
    public void onBackPressed() {
        Utiltis.mAuth.signOut();
        mContext.startActivity(new Intent(mContext, LogInActivity.class));
        ((Activity) mContext).finish();
        dismiss();
        super.onBackPressed();
    }
}

