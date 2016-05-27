package org.hackpku.emotiondiary.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hackpku.emotiondiary.common.FaceHelper;
import org.hackpku.emotiondiary.R;
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

}
