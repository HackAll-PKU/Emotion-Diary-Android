package org.hackpku.emotiondiary.common.Diary;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by ChenLetian on 16/5/28.
 * 日记类
 */
public class Diary extends RealmObject {

    private Integer happiness;                   // 高兴值
    private String text;                         // 日记内容
    private DiaryPicture selfie;                 // 自拍照片
    private RealmList<DiaryPicture> pictures;    // 其他照片
    private Date date;                           // 日期

    public Diary() {}

    public Diary(Integer happiness, String text, Bitmap selfie, ArrayList<Bitmap> pictures, Date date) {
        this.happiness = happiness;
        this.text = text;
        this.selfie = new DiaryPicture(selfie);
        this.pictures = new RealmList<>();
        for (Bitmap picture: pictures) {
            DiaryPicture diaryPicture = new DiaryPicture(picture);
            this.pictures.add(diaryPicture);
        }
        this.date = date;
    }

    public Integer getHappiness() {
        return happiness;
    }

    public void setHappiness(Integer happiness) {
        this.happiness = happiness;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DiaryPicture getSelfie() {
        return selfie;
    }

    public void setSelfie(DiaryPicture selfie) {
        this.selfie = selfie;
    }

    public RealmList<DiaryPicture> getPictures() {
        return pictures;
    }

    public void setPictures(RealmList<DiaryPicture> pictures) {
        this.pictures = pictures;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
