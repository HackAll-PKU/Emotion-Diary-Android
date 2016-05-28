package org.hackpku.emotiondiary.common.Diary;

import android.graphics.Bitmap;

/**
 * Created by ChenLetian on 16/5/28.
 * 日记类
 */
public class Diary {

    Integer happiness;   // 高兴值
    String text;         // 日记内容
    Bitmap selfie;       // 自拍照片
    Bitmap pictures[];   // 其他照片

    public Diary(Integer happiness, String text, Bitmap selfie, Bitmap[] pictures) {
        this.happiness = happiness;
        this.text = text;
        this.selfie = selfie;
        this.pictures = pictures;
    }
}
