package org.hackpku.emotiondiary.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.hackpku.emotiondiary.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

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

    /**
     * 上传照片
     * @param img 照片(Bitmap)
     * @return 这张照片中face的face_id，如果失败则为null
     */
    public String uploadPhoto(final Bitmap img) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);

        imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        try {
            JSONObject result = httpHandler.detectionDetect(new PostParameters().setImg(data).setMode("oneface"));
            return result.getJSONArray("face").getJSONObject(0).getString("face_id");
        } catch (FaceppParseException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
