package org.hackpku.emotiondiary.Welcome.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.Welcome.presenter.IWelcomePresenter;
import org.hackpku.emotiondiary.Welcome.presenter.WelcomePresenterImpl;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, IWelcomeView {
    private Button btnLogIn;
    private Button btnRecordEmotion;
    private Button btnEnterHomepage;
    private AlphaAnimation alphaAnimation;

    IWelcomePresenter welcomePresenter; // 通过持有接口，而不是持有类，来提高代码的复用性

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏

        setContentView(R.layout.activity_welcome);

        // 绑定按钮
        btnLogIn = (Button) this.findViewById(R.id.btnLogIn);
        btnRecordEmotion = (Button) this.findViewById(R.id.btnRecordEmotion);
        btnEnterHomepage = (Button) this.findViewById(R.id.btnEnterHomepage);

        btnLogIn.setOnClickListener(this);
        btnRecordEmotion.setOnClickListener(this);
        btnEnterHomepage.setOnClickListener(this);

        btnRecordEmotion.setEnabled(false);
        btnEnterHomepage.setEnabled(false);

        welcomePresenter = new WelcomePresenterImpl(this);

        alphaAnimation = new AlphaAnimation(1.0f, 0.2f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setRepeatCount(-1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        btnLogIn.setAnimation(alphaAnimation);
        alphaAnimation.startNow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogIn:
                welcomePresenter.doLogIn();
                break;
            case R.id.btnRecordEmotion:
                welcomePresenter.recordEmotion();
                break;
            case R.id.btnEnterHomepage:
                welcomePresenter.enterHomepage();
                break;
        }
    }

    @Override
    public void onLogInResult(boolean logInResult, String msg) {
        btnRecordEmotion.setEnabled(logInResult);
        btnEnterHomepage.setEnabled(logInResult);
        btnLogIn.setEnabled(!logInResult);
        if (logInResult) {
            btnLogIn.clearAnimation();
            alphaAnimation.cancel();
        }
        makeAlertDialog(logInResult ? "解锁成功" : "解锁失败", msg);
    }

    @Override
    public void onRecordEmotion() {
        //*
        Toast.makeText(this, "Record emotion called", Toast.LENGTH_SHORT).show();
        /*/
        finish();
        //*/
    }

    @Override
    public void onEnterHomepage() {
        //*
        Toast.makeText(this, "Enter homepage called", Toast.LENGTH_SHORT).show();
        /*/
        finish();
        //*/
    }

    @Override
    public void makeToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void makeAlertDialog(String title, String message) {
        welcomePresenter.makeAlertDialog(title, message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        welcomePresenter.onActivityResult(requestCode, resultCode, data);
    }

}
