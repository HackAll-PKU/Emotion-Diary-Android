package org.hackpku.emotiondiary.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.common.Diary.Diary;
import org.hackpku.emotiondiary.common.Diary.DiaryHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by ChenLetian on 16/5/27.
 * DiaryData测试类
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DiaryDataTest {

    private Context context;
    private DiaryHelper diaryHelper;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        diaryHelper = new DiaryHelper(context);
    }

    @Test
    public void testBasicSaveAndGet() {
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
        ArrayList<Bitmap> pictures = new ArrayList<>();
        pictures.add(img);
        Diary toSaveDiary = new Diary(100, "哈哈哈", img, pictures, new GregorianCalendar().getTime());
        diaryHelper.saveDiary(toSaveDiary);
        Diary gettedDiary = diaryHelper.getLastestDiary();
        Assert.assertEquals(toSaveDiary.getDate(), gettedDiary.getDate());
        Assert.assertEquals(toSaveDiary.getHappiness(), gettedDiary.getHappiness());
        Assert.assertArrayEquals(toSaveDiary.getPictures().first().getData(), gettedDiary.getPictures().first().getData());
        Assert.assertArrayEquals(toSaveDiary.getSelfie().getData(), gettedDiary.getSelfie().getData());
        Assert.assertEquals(toSaveDiary.getText(), gettedDiary.getText());
        Bitmap gettedImage = gettedDiary.getSelfie().getImage();
        Assert.assertNotNull(gettedImage);
    }

    @Test
    public void testDeleteDiary() {
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
        ArrayList<Bitmap> pictures = new ArrayList<>();
        pictures.add(img);
        Diary toSaveDiary = new Diary(99, "测试删除Diary", img, pictures, new GregorianCalendar().getTime());
        diaryHelper.saveDiary(toSaveDiary);
        Diary gettedDiary = diaryHelper.getLastestDiary();
        Assert.assertEquals(toSaveDiary.getDate(), gettedDiary.getDate());
        diaryHelper.deleteDiary(gettedDiary);
        gettedDiary = diaryHelper.getLastestDiary();
        Assert.assertNotEquals(toSaveDiary.getDate(), gettedDiary.getDate());
    }

    @Test
    public void testUpdate() {
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
        ArrayList<Bitmap> pictures = new ArrayList<>();
        pictures.add(img);
        Diary toSaveDiary = new Diary(100, "哈哈哈", img, pictures, new GregorianCalendar().getTime());
        diaryHelper.saveDiary(toSaveDiary);
        final Diary gettedDiary = diaryHelper.getLastestDiary();
        diaryHelper.updateDiary(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                gettedDiary.setText("哈哈哈哈");
            }
        });
        Assert.assertTrue(gettedDiary.getText().equals("哈哈哈哈"));
    }

    @Test
    public void testGetDiaryOfDayUsingGregorianCalendar() {
        RealmResults<Diary> diaries = diaryHelper.getDiariesOfDay(new GregorianCalendar());
    }

    @Test
    public void testGetSmilingForTime() {
        TreeMap<Date, Double> result = diaryHelper.getHappinessForTime(2);
        Assert.assertNotNull(result);
    }

    @Test
    public void testAddALotOfDiary() {
        GregorianCalendar thisDay = new GregorianCalendar();
        for (int loop = 0; loop < 3; loop++) {
            Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
            ArrayList<Bitmap> pictures = new ArrayList<>();
            pictures.add(img);
            Diary toSaveDiary = new Diary(100, "哈哈哈", img, pictures, thisDay.getTime());
            diaryHelper.saveDiary(toSaveDiary);
        }
        thisDay.add(Calendar.DAY_OF_YEAR, -1);
        for (int loop = 0; loop < 3; loop++) {
            Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
            ArrayList<Bitmap> pictures = new ArrayList<>();
            pictures.add(img);
            Diary toSaveDiary = new Diary(60, "喵喵喵", img, pictures, thisDay.getTime());
            diaryHelper.saveDiary(toSaveDiary);
        }
        thisDay.add(Calendar.MONTH, -1);
        for (int loop = 0; loop < 3; loop++) {
            Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
            ArrayList<Bitmap> pictures = new ArrayList<>();
            pictures.add(img);
            Diary toSaveDiary = new Diary(20, "汪汪汪", img, pictures, thisDay.getTime());
            diaryHelper.saveDiary(toSaveDiary);
        }
        RealmResults<Diary> diaries = diaryHelper.getDiariesOfDay(thisDay);
        Assert.assertEquals(20, (int)(diaries.last().getHappiness()));
    }

}
