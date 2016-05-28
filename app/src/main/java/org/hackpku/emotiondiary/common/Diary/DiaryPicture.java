package org.hackpku.emotiondiary.common.Diary;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import io.realm.RealmObject;

/**
 * Created by ChenLetian on 16/5/28.
 * 日记中的照片类，包括自拍和图片
 */
public class DiaryPicture extends RealmObject {

    private byte[] data;

    public DiaryPicture() {}

    public DiaryPicture(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.data = stream.toByteArray();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
