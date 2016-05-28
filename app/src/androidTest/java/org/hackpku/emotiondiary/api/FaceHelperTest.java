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
import org.hackpku.emotiondiary.common.FaceHelper.FaceHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ChenLetian on 16/5/27.
 * FaceHelper的测试文件
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
        try {
            faceHelper.createPerson();
            SharedPreferences sharedPreferences = context.getSharedPreferences(resources.getString(R.string.FaceHelperPreference), Context.MODE_PRIVATE);
            String faceHelperPeopleID = sharedPreferences.getString(resources.getString(R.string.FaceHelperPersonID), "this is wrong");
            assertTrue(!faceHelperPeopleID.equals("this is wrong"));
        } catch (FaceHelper.requestError requestError) {
            requestError.printStackTrace();
            assertTrue("requestError!", false);
        }
    }

    @Test
    public void testUploadImg() {
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test1);
        assertTrue(img != null);
        try {
            String faceID = faceHelper.uploadPhoto(img);
            assertTrue(faceID != null);
        } catch (FaceHelper.requestError requestError) {
            requestError.printStackTrace();
            assertTrue("requestError!", false);
        }
    }

    @Test
    public void testAddFace() {
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test1);
        assertTrue(img != null);
        try {
            String faceID = faceHelper.uploadPhoto(img);
            assertTrue(faceID != null);
            boolean result = faceHelper.addFace(faceID);
            assertTrue(result);
        } catch (FaceHelper.requestError requestError) {
            requestError.printStackTrace();
            assertTrue("requestError!", false);
        } catch (FaceHelper.personIDNotFound personIDNotFound) {
            personIDNotFound.printStackTrace();
            assertTrue("personIDNotFound!", false);
        }
    }

    @Test
    public void testTrain() {
        try {
            faceHelper.train();
        } catch (FaceHelper.requestError requestError) {
            requestError.printStackTrace();
            assertTrue("requestError!", false);
        } catch (FaceHelper.personIDNotFound personIDNotFound) {
            personIDNotFound.printStackTrace();
            assertTrue("personIDNotFound!", false);
        }
    }

    @Test
    public void testVerify() {
        try {
            Bitmap img1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
            assertTrue(img1 != null);
            String faceID1 = faceHelper.uploadPhoto(img1);
            assertTrue(faceID1 != null);
            boolean addFaceResult1 = faceHelper.addFace(faceID1);
            assertTrue(addFaceResult1);

            faceHelper.train();
            Thread.sleep(5000);

            Bitmap img2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
            assertTrue(img2 != null);
            String faceID2 = faceHelper.uploadPhoto(img2);
            boolean result = faceHelper.verify(faceID2);
            assertTrue(result);
        } catch (FaceHelper.requestError requestError) {
            requestError.printStackTrace();
            assertTrue("requestError!", false);
        } catch (FaceHelper.personIDNotFound personIDNotFound) {
            personIDNotFound.printStackTrace();
            assertTrue("personIDNotFound!", false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue("InterruptedException!", false);
        }
    }

    @Test
    public void testGetSmiling() {
        try {
            Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);
            assertTrue(img != null);
            String faceID = faceHelper.uploadPhoto(img);
            assertTrue(faceID != null);
            double smiling = faceHelper.getSmiling(faceID);
            assertEquals(smiling, 95.9057, 1);
        } catch (FaceHelper.requestError requestError) {
            requestError.printStackTrace();
            assertTrue("requestError!", false);
        }
    }

}
