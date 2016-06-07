package org.hackpku.emotiondiary.RecordEmotion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import org.hackpku.emotiondiary.Homepage.HomePageActivity;
import org.hackpku.emotiondiary.MainApplication;
import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.common.Diary.Diary;
import org.hackpku.emotiondiary.common.Diary.DiaryHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecordEmotionActivity extends AppCompatActivity {


    private ArrayList<Bitmap> Pictures = new ArrayList<Bitmap>();
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Toast.makeText(this,"DEBUGGING",Toast.LENGTH_SHORT);

        switch(item.getItemId()){
            case R.id.action_ok:
                saveData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_emotion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.recordEmotionToolbar);
        toolbar.setTitle("记录心情");
        int color;

        Intent intent = getIntent();
        double smiling = ((MainApplication)getApplication()).getSmiling();
        if (smiling < 33) color = R.color.themeColorBlue;
        else if (smiling > 66) color = R.color.themeColorOrange;
        else color =  R.color.themeColorYellow;
        toolbar.setBackgroundColor(getResources().getColor(color));

        setSupportActionBar(toolbar);

        String mCurrentPath = intent.getStringExtra("photoPath");
        mCurrentPhotoPath = mCurrentPath;
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress((int)smiling*seekBar.getMax()/100);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPath);
        imageView.setImageBitmap(bitmap);


    }


    private String mCurrentPhotoPath;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private static final int REQUEST_CODE_CAMERA = 1;
    //Callback function
    public void addNewImage(View view){

        //Call camera to get a bitmap for the newImageButton
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);

        File photoFile = null;
        try{
            photoFile = createImageFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        if(photoFile!=null){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if(intent.resolveActivity(this.getPackageManager())!=null){
                this.startActivityForResult(intent,REQUEST_CODE_CAMERA);
            }else{
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) handleImage();
        }
    }
    public void handleImage(){
        LinearLayout imageList = (LinearLayout) findViewById(R.id.ImageListLinearLayout);
        ImageButton newImageButton = new ImageButton(this);
        File file = new File(mCurrentPhotoPath);
        Uri uri = Uri.fromFile(file);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            //TODO: Pictures.add(bitmap) is always failed?
            newImageButton.setImageBitmap(bitmap);
            Resources resources = getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,resources.getDisplayMetrics());
            imageList.addView(newImageButton,new LinearLayout.LayoutParams(px,px));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void saveData(){
        // 显示提示框
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("正在保存");
        dialog.setMessage("正在保存您的心情日记，请稍候...");
        dialog.show();

        new Thread() {
            @Override
            public void run() {
                SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
                int happiness = seekBar.getProgress();
                ((MainApplication)getApplication()).setSmiling(happiness);

                EditText editText = (EditText) findViewById(R.id.editText);
                String text = editText.getText().toString();

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                Calendar calendar = Calendar.getInstance();
                Diary diary = new Diary(happiness, text, bitmap, Pictures, calendar.getTime());
                DiaryHelper diaryHelper = new DiaryHelper(getApplicationContext());
                diaryHelper.saveDiary(diary);

                Message msg = new Message();
                msg.what = ID_DIARY_SAVED;
                msg.obj = dialog;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private static final int ID_DIARY_SAVED = 10086;

    // Message Handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ID_DIARY_SAVED:
                    ((Dialog) msg.obj).cancel();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
}
