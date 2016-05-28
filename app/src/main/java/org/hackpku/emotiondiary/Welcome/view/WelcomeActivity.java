package org.hackpku.emotiondiary.Welcome.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.Welcome.presenter.IWelcomePresenter;
import org.hackpku.emotiondiary.Welcome.presenter.WelcomePresenterImpl;

public class WelcomeActivity extends Activity implements View.OnClickListener, IWelcomeView {
    private Button btnLogIn;
    private Button btnRecordEmotion;
    private Button btnEnterHomepage;

    IWelcomePresenter welcomePresenter; // 通过持有接口，而不是持有类，来提高代码的复用性

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_welcome);

        // 绑定按钮
        btnLogIn = (Button) this.findViewById(R.id.btnLogIn);
        btnRecordEmotion = (Button) this.findViewById(R.id.btnRecordEmotion);
        btnEnterHomepage = (Button) this.findViewById(R.id.btnEnterHomepage);

        btnLogIn.setOnClickListener(this);
        btnRecordEmotion.setOnClickListener(this);
        btnEnterHomepage.setOnClickListener(this);

        welcomePresenter = new WelcomePresenterImpl(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
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
    public void onLogInResult(boolean logInResult){
        Toast.makeText(this, "LogIn Status: " + logInResult, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordEmotion(){
        //*
        Toast.makeText(this, "Record emotion called", Toast.LENGTH_SHORT).show();
        /*/
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, RecordEmotion.class);
        startActivity(intent);
        //finish();
        //*/
    }

    @Override
    public void onEnterHomepage(){
        //*
        Toast.makeText(this, "Enter homepage called", Toast.LENGTH_SHORT).show();
        /*/
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, Homepage.class);
        startActivity(intent);
        finish();
        //*/
    }
}
