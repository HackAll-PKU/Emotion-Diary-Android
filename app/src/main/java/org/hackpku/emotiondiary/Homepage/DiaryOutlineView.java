package org.hackpku.emotiondiary.Homepage;

import android.content.*;
import android.util.*;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;

import org.hackpku.emotiondiary.R;

/**
 * Created by Liu Zhengyuan on 2016/6/2.
 */
public class DiaryOutlineView extends FrameLayout {

    private RoundImageView riv_photo;  //圆形自拍图片
    private RoundImageView riv_emotion;  //圆形心情图片
    private TextView tv_time;  //日记记录时间
    private TextView tv_diary;  //日记内容
    private ImageView iv_heart;  //是红心还是灰心

    public DiaryOutlineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.diary_outline, this);
        riv_photo = (RoundImageView) findViewById(R.id.photo_view);
        riv_emotion = (RoundImageView) findViewById(R.id.emotion_view);
        tv_time = (TextView) findViewById(R.id.time_text);
        tv_diary = (TextView) findViewById(R.id.diary_text);
        iv_heart.setTag(R.drawable.heart_grey);

        /**
         * 设置红心灰心点击事件
         */
        iv_heart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if((int)iv_heart.getTag()==R.drawable.heart_grey) {
                    iv_heart.setTag(R.drawable.heart_red);
                    iv_heart.setImageResource(R.drawable.heart_red);
                }
                else{
                    iv_heart.setTag(R.drawable.heart_grey);
                    iv_heart.setImageResource(R.drawable.heart_grey);
                }
            }
        });
    }

    /**
     * 设置日记记录时间
     * @param timeText
     */
    public void setRecordTimeText(String timeText) {
        tv_diary.setText(timeText);
    }

    /**
     * 设置日记文本
     * @param diaryText
     */
    public void setDiaryText(String diaryText) {
        tv_diary.setText(diaryText);
    }

    /**
     * 设置自拍图片
     */
    public void setPhotoImage(){
        //TODO
    }

    /**
     * 根据心情指数设置心情图片
     * @param emtionIndex 心情指数，待修改
     */
    public void setEmotionImage(int emtionIndex) {
        final int[] emotionImages = new int[]{R.drawable.laugh3, R.drawable.smile3, R.drawable.sad3};
        if (emtionIndex == 1)
            riv_emotion.setImageResource(emotionImages[2]);
        else if (emtionIndex == 2)
            riv_emotion.setImageResource(emotionImages[1]);
        else
            riv_emotion.setImageResource(emotionImages[0]);
    }

}
