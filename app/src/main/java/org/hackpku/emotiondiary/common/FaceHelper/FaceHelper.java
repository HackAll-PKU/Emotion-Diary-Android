package org.hackpku.emotiondiary.common.FaceHelper;

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
    private String TAG = "emotionDiary.test";

    /**
     * 在Face++中新建Person，并将person_id存入sharedPreferences
     * @throws requestError 网络请求或解析错误时会抛出此Exception
     */
    public void createPerson() throws requestError {
        try {
            JSONObject result = httpHandler.personCreate(new PostParameters());
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.FaceHelperPreference), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getResources().getString(R.string.FaceHelperPersonID), result.getString("person_id"));
            editor.commit();
            Log.v(TAG, "personID: " + result.getString("person_id"));
        } catch (FaceppParseException | JSONException e) {
            e.printStackTrace();
            Log.v(TAG, "createPerson failed");
            throw new requestError("create person request error");
        }
    }

    /**
     * 上传照片
     * @param img 照片(Bitmap)
     * @return 这张照片中face的face_id
     * @throws requestError 网络请求或解析错误时会抛出此Exception
     */
    public String uploadPhoto(final Bitmap img) throws requestError {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);

        imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        try {
            JSONObject result = httpHandler.detectionDetect(new PostParameters().setImg(data).setMode("oneface"));
            String faceID = result.getJSONArray("face").getJSONObject(0).getString("face_id");
            Log.v(TAG, "faceID: " + faceID);
            return faceID;
        } catch (FaceppParseException | JSONException e) {
            e.printStackTrace();
            Log.v(TAG, "uploadPhoto failed");
            throw new requestError("upload photo request error");
        }
    }

    /**
     * 添加Face到person
     * @param faceID    face的faceID
     * @return  api返回是否添加成功
     * @throws requestError 网络请求或解析错误时会抛出此Exception
     * @throws personIDNotFound 在storage中没有找到personID信息时会抛出此异常，请在create person后调用此接口
     */
    public boolean addFace(String faceID) throws requestError, personIDNotFound {
        String personID = getPersonIDFromStorage();
        try {
            JSONObject result = httpHandler.personAddFace(new PostParameters().setFaceId(faceID).setPersonId(personID));
            boolean success = result.getBoolean("success");
            Log.v(TAG, "addFace" + (success ? "OK" : "failed"));
            return success;
        } catch (FaceppParseException | JSONException e) {
            e.printStackTrace();
            Log.v(TAG, "addFace failed");
            throw new requestError("add face request error");
        }
    }

    /**
     * 训练person的verify能力，当有人脸加入或者删去时需要调用此方法（train耗时较久，API只会返回一个session_id，因此本方法若返回true只能表示此网络请求发送成功，不代表train成功）
     * @throws requestError 网络请求或解析错误时会抛出此Exception
     * @throws personIDNotFound 在storage中没有找到personID信息时会抛出此异常，请在create person后调用此接口
     */
    public void train() throws requestError, personIDNotFound {
        String personID = getPersonIDFromStorage();
        try {
            JSONObject result = httpHandler.trainVerify(new PostParameters().setPersonId(personID));
            String sessionID = result.getString("session_id");
            Log.v(TAG, "sessionID: " + sessionID);
        } catch (FaceppParseException | JSONException e) {
            e.printStackTrace();
            throw new requestError("train request error");
        }
    }

    /**
     * 验证某个faceID是否属于personID
     * @param faceID
     * @return 是否属于
     * @throws requestError 网络请求或解析错误时会抛出此Exception
     * @throws personIDNotFound 在storage中没有找到personID信息时会抛出此异常，请在create person后调用此接口
     */
    public boolean verify(String faceID) throws requestError, personIDNotFound {
        String personID = getPersonIDFromStorage();
        try {
            JSONObject result = httpHandler.recognitionVerify(new PostParameters().setPersonId(personID).setFaceId(faceID));
            boolean isSamePerson = result.getBoolean("is_same_person");
            Log.v(TAG, isSamePerson ? "isSamePerson" : "isNotSamePerson");
            return isSamePerson;
        }
        catch (FaceppParseException | JSONException e) {
            e.printStackTrace();
            throw new requestError("verify request error");
        }
    }

    /**
     * 从storage中获取personID
     * @return personID
     * @throws personIDNotFound 存储中没有personID，一般是没有调用createPerson造成的
     */
    private String getPersonIDFromStorage() throws personIDNotFound{
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.FaceHelperPreference), Context.MODE_PRIVATE);
        String personID = sharedPreferences.getString(context.getResources().getString(R.string.FaceHelperPersonID), "this is wrong");
        if (personID.equals("this is wrong")) {
            throw new personIDNotFound("can not find personID in storage! please make sure you've created person");
        }
        return personID;
    }

    /**
     * 网络请求或JSON解析错误
     */
    public class requestError extends Exception {
        public requestError(String detailMessage) {
            super(detailMessage);
        }
    }

    /**
     * storage中没有personID
     */
    public class personIDNotFound extends Exception {
        public personIDNotFound(String detailMessage) {
            super(detailMessage);
        }
    }

}
