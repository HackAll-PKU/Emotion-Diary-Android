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
        Diary toSaveDiary = new Diary(100, "哈哈哈", img, pictures, new Date());
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
    public void testUpdate() {
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
        ArrayList<Bitmap> pictures = new ArrayList<>();
        pictures.add(img);
        Diary toSaveDiary = new Diary(100, "哈哈哈", img, pictures, new Date());
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
    public void testGetDiaryOfDayUsingDate() {
        RealmResults<Diary> diaries = diaryHelper.getDiariesOfDay(new Date());
    }

    @Test
    public void testGetDiaryOfDayUsingGregorianCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        RealmResults<Diary> diaries = diaryHelper.getDiariesOfDay(new GregorianCalendar(year, month, day));
    }

}
