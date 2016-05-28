package org.hackpku.emotiondiary.Welcome.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.Welcome.view.IWelcomeView;
import org.hackpku.emotiondiary.common.FaceHelper.FaceHelper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Archimekai on 5/24/2016.
 * Presenter是MVP模式的核心
 */
public class WelcomePresenterImpl implements IWelcomePresenter{
    IWelcomeView welcomeView;  // presenter通过view来操作activity的表现
    Activity welcomeActivity;
    FaceHelper faceHelper;
    private static final int REQUEST_CODE_CAMERA = 1;
    boolean initFlag = false;

    public WelcomePresenterImpl(IWelcomeView welcomeView){
        this.welcomeView = welcomeView;
        this.welcomeActivity = (Activity) welcomeView;
        this.faceHelper = FaceHelper.getInstance(this.welcomeActivity);
    }

    @Override
    public void doLogIn(){
        // 此处实现登陆逻辑
        SharedPreferences sharedPreferences = welcomeActivity.getSharedPreferences(welcomeActivity.getResources().getString(R.string.FaceHelperPreference), Context.MODE_PRIVATE);
        String faceHelperPeopleID = sharedPreferences.getString(welcomeActivity.getResources().getString(R.string.FaceHelperPersonID), "this is wrong");

        Log.i("faceHelperPeopleID", faceHelperPeopleID);

        if (faceHelperPeopleID.equals("this is wrong")){
            initFlag = true;
            new Thread(){
                @Override
                public void run() {
                    try {
                        faceHelper.createPerson();
                    } catch (FaceHelper.requestError requestError) {
                        requestError.printStackTrace();
                    }
                }
            }.start();
        }

        // 检测sd是否可用
        if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            Toast.makeText(welcomeActivity, "外部存储不可用", Toast.LENGTH_LONG).show();
            return;
        }

        //指定一个图片路径对应的file对象
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        name = "temp.jpg";
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/EmotionDiary/Image/");
        if(!dir.exists()) dir.mkdirs();
        File file =new File(dir, name);
        if (file.exists()) file.delete();
        Uri uri = Uri.fromFile(file);

        //实例化一个intent，并指定action
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        //启动activity
        welcomeActivity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    final static int ID_LOGIN_SUCCESS = 0;
    final static int ID_LOGIN_FAILED = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ID_LOGIN_SUCCESS:
                    welcomeView.onLogInResult(true, "主人，欢迎回来~");
                    break;
                case ID_LOGIN_FAILED:
                    welcomeView.onLogInResult(false, (String) msg.obj);
                    break;
            }
        }
    };

    private void afterCamera(){
        File file = new File(Environment.getExternalStorageDirectory() + "/EmotionDiary/Image/" + "temp.jpg");
        Uri uri = Uri.fromFile(file);
        try {
            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(welcomeActivity.getContentResolver(), uri);
            new Thread(){
                @Override
                public void run() {
                    try {
                        String faceID = faceHelper.uploadPhoto(bitmap);
                        if (!initFlag) {
                            boolean verifyResult = faceHelper.verify(faceID);
                            if (!verifyResult) {
                                Message msg = new Message();
                                msg.what = ID_LOGIN_FAILED;
                                msg.obj = "这好像不是你哦，请不要调戏我T_T";
                                mHandler.sendMessage(msg);
                                return;
                            }
                        }
                        mHandler.sendEmptyMessage(ID_LOGIN_SUCCESS);
                        boolean addResult = faceHelper.addFace(faceID);
                        Log.i("addResult", addResult?"true":"false");
                        faceHelper.train();
                        Log.i("train", "train");
                    } catch (FaceHelper.requestError requestError) {
                        requestError.printStackTrace();
                        Message msg = new Message();
                        msg.what = ID_LOGIN_FAILED;
                        msg.obj = "没有找到人脸哦，请换个姿势吧T_T";
                        mHandler.sendMessage(msg);
                    } catch (FaceHelper.personIDNotFound personIDNotFound) {
                        personIDNotFound.printStackTrace();
                        Message msg = new Message();
                        msg.what = ID_LOGIN_FAILED;
                        msg.obj = personIDNotFound.getMessage();
                        mHandler.sendMessage(msg);
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void recordEmotion() {
        //Intent intent = new Intent();
        //intent.setClass(welcomeActivity, RecordEmotion.class);
        //welcomeActivity.startActivity(intent);
        welcomeView.onRecordEmotion();
    }

    @Override
    public void enterHomepage() {
        //Intent intent = new Intent();
        //intent.setClass(welcomeActivity, Homepage.class);
        //welcomeActivity.startActivity(intent);
        welcomeView.onEnterHomepage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    afterCamera();
                } else {
                    Message msg = new Message();
                    msg.what = ID_LOGIN_FAILED;
                    msg.obj = "用户取消";
                    mHandler.sendMessage(msg);
                }
                break;
        }
    }
}
