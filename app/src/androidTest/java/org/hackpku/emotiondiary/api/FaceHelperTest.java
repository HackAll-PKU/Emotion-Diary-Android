package org.hackpku.emotiondiary.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.common.FaceHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Created by ChenLetian on 16/5/27.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FaceHelperTest {

    private FaceHelper faceHelper;
    private Context context;
    private Resources resources;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        faceHelper = FaceHelper.getInstance(context);
        resources = context.getResources();
    }

    @Test
    public void testCreatePeople() {
        faceHelper.createPerson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(resources.getString(R.string.FaceHelperPreference), Context.MODE_PRIVATE);
        String faceHelperPeopleID = sharedPreferences.getString(resources.getString(R.string.FaceHelperPersonID), "this is wrong");
        assertTrue(!faceHelperPeopleID.equals("this is wrong"));
    }

    @Test
    public void testUploadImg() {
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test1);
        assertTrue(img != null);
        String faceID = faceHelper.uploadPhoto(img);
        assertTrue(faceID != null);
    }

    @Test
    public void testAddFace() {
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test1);
        assertTrue(img != null);
        String faceID = faceHelper.uploadPhoto(img);
        assertTrue(faceID != null);
        boolean result = faceHelper.addFace(faceID);
        assertTrue(result);
    }

    @Test
    public void testTrain() {
        boolean result = faceHelper.train();
        assertTrue(result);
    }

}
