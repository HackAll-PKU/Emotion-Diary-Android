package org.hackpku.emotiondiary.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.hackpku.emotiondiary.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ChenLetian on 16/5/27.
 * Face++API的helper类
 * 采用单例模式，使用FaceHelper.getInstance(context)获取单例
 * 其中所有的网络请求均为同步请求，若不希望阻塞主线程请新开线程调用
 */
public class FaceHelper {
    private static FaceHelper ourInstance;

    public static FaceHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new FaceHelper(context);
        }
        return ourInstance;
    }

    private FaceHelper(Context context) {
        httpHandler = new HttpRequests(context.getResources().getString(R.string.FacePlusPlusAPIKEY), context.getResources().getString(R.string.FacePlusPlusAPISecret), true, true);
        this.context = context;
    }

    private Context context;
    private HttpRequests httpHandler;

    /**
     * 在Face++中新建Person，并将person_id存入sharedPreferences
     * @return 是否创建成功
     */
    public boolean createPerson() {
        try {
            JSONObject result = httpHandler.personCreate(new PostParameters());
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.FaceHelperPreference), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getResources().getString(R.string.FaceHelperPersonID), result.getString("person_id"));
            editor.commit();
            Log.v("SavePersonID", result.getString("person_id"));
            return true;
        } catch (FaceppParseException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
