package org.hackpku.emotiondiary.Welcome.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.Welcome.view.IWelcomeView;
import org.hackpku.emotiondiary.common.FaceHelper.FaceHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Archimekai on 5/24/2016.
 * Presenter是MVP模式的核心
 */
public class WelcomePresenterImpl implements IWelcomePresenter {
    private IWelcomeView welcomeView;  // presenter通过view来操作activity的表现
    private Activity welcomeActivity;
    private FaceHelper faceHelper;
    private boolean initFlag = false;
    private double smiling;

    public WelcomePresenterImpl(IWelcomeView welcomeView) {
        this.welcomeView = welcomeView;
        this.welcomeActivity = (Activity) welcomeView;
        this.faceHelper = FaceHelper.getInstance(this.welcomeActivity);
    }

    private void checkPerson() {
        SharedPreferences sharedPreferences = welcomeActivity.getSharedPreferences(welcomeActivity.getResources().getString(R.string.FaceHelperPreference), Context.MODE_PRIVATE);
        String faceHelperPeopleID = sharedPreferences.getString(welcomeActivity.getResources().getString(R.string.FaceHelperPersonID), "this is wrong");

        if (faceHelperPeopleID.equals("this is wrong")) {
            // 显示提示框
            final ProgressDialog dialog = new ProgressDialog(welcomeActivity);
            dialog.setTitle("欢迎使用");
            dialog.setMessage("这是您的第一次使用，正在为您创建用户，请稍候...");
            dialog.show();

            new Thread() {
                @Override
                public void run() {
                    try {
                        faceHelper.createPerson();
                        makeToast("用户创建成功");
                        initFlag = true;
                    } catch (FaceHelper.requestError requestError) {
                        requestError.printStackTrace();
                        makeToast("用户创建失败");
                    } finally {
                        Message msg = new Message();
                        msg.what = ID_DIALOG_CANCEL;
                        msg.obj = dialog;
                        mHandler.sendMessage(msg);
                    }
                }
            }.start();
        }
    }

    @Override
    public void doLogIn() {
        // 判断是否有用户
        checkPerson();

        // 检测sd是否可用
        if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            welcomeView.makeToast("外部存储不可用");
            return;
        }

        // 实例化一个intent，并指定action
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);

        // 指定图片路径对应的file对象
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 启动activity
        if (photoFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            if (intent.resolveActivity(welcomeActivity.getPackageManager()) != null) {
                welcomeActivity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } else {
                welcomeView.makeToast("相机不可用");
            }
        }
    }

    private static final int ID_LOGIN_SUCCESS = 0;
    private static final int ID_LOGIN_FAILED = 1;
    private static final int ID_DIALOG_CANCEL = 10086;
    private static final int ID_MAKE_TOAST = 65535;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ID_LOGIN_SUCCESS:
                    welcomeView.onLogInResult(true, "主人，欢迎回来~");
                    break;
                case ID_LOGIN_FAILED:
                    welcomeView.onLogInResult(false, (String) msg.obj);
                    break;
                case ID_DIALOG_CANCEL:
                    ((Dialog) msg.obj).cancel();
                    break;
                case ID_MAKE_TOAST:
                    welcomeView.makeToast((String) msg.obj);
                    break;
            }
        }
    };

    private void makeToast(String str) {
        Message msg = new Message();
        msg.what = ID_MAKE_TOAST;
        msg.obj = str;
        mHandler.sendMessage(msg);
    }

    private String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        File storageDir = welcomeActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void afterCamera() {
        // 显示提示框
        final ProgressDialog dialog = new ProgressDialog(welcomeActivity);
        dialog.setTitle("确保这是你");
        dialog.setMessage("正在识别照片中的面孔，请稍候...");
        dialog.show();

        final File file = new File(mCurrentPhotoPath);
        final Uri uri = Uri.fromFile(file);
        try {
            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(welcomeActivity.getContentResolver(), uri);
            new Thread() {
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
                                if (file.exists()) file.delete();
                                return;
                            }
                        }
                        faceHelper.addFace(faceID);
                        faceHelper.train();
                        smiling = faceHelper.getSmiling(faceID);
                        mHandler.sendEmptyMessage(ID_LOGIN_SUCCESS);
                    } catch (FaceHelper.requestError requestError) {
                        requestError.printStackTrace();
                        Message msg = new Message();
                        msg.what = ID_LOGIN_FAILED;
                        msg.obj = "没有找到人脸哦，请换个姿势吧T_T";
                        mHandler.sendMessage(msg);
                        if (file.exists()) file.delete();
                    } catch (FaceHelper.personIDNotFound personIDNotFound) {
                        personIDNotFound.printStackTrace();
                        Message msg = new Message();
                        msg.what = ID_LOGIN_FAILED;
                        msg.obj = personIDNotFound.getMessage();
                        mHandler.sendMessage(msg);
                        if (file.exists()) file.delete();
                    } finally {
                        Message msg = new Message();
                        msg.what = ID_DIALOG_CANCEL;
                        msg.obj = dialog;
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
        //TODO:实现RecordEmotion后取消注释
        /*
        Intent intent = new Intent();
        intent.setClass(welcomeActivity, RecordEmotion.class);
        intent.putExtra("smiling", smiling);
        intent.putExtra("photoPath", mCurrentPhotoPath);
        welcomeActivity.startActivity(intent);
        */
        welcomeView.onRecordEmotion();
    }

    @Override
    public void enterHomepage() {
        //TODO:实现Homepage后取消注释
        /*
        Intent intent = new Intent();
        intent.setClass(welcomeActivity, Homepage.class);
        intent.putExtra("smiling", smiling);
        intent.putExtra("photoPath", mCurrentPhotoPath);
        welcomeActivity.startActivity(intent);
        */
        welcomeView.onEnterHomepage();
    }

    private static final int REQUEST_CODE_CAMERA = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
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

    @Override
    public void makeAlertDialog(String title, String message) {
        final AlertDialog dialog =
                new AlertDialog.Builder(welcomeActivity)
                        .setTitle(title)
                        .setMessage(message)
                        .create();
        dialog.show();

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = ID_DIALOG_CANCEL;
                msg.obj = dialog;
                mHandler.sendMessage(msg);
            }
        };
        timer.schedule(task, 1000);
    }
}
