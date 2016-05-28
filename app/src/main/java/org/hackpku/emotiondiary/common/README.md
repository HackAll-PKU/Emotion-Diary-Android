# Common
- common文件夹下为Model层及帮助类

## FaceHelper
[使用说明](./FaceHelper/FaceHelper.md)

## Diary相关
- 使用Realm存储

### Diary
- 日记类
- 继承自RealmObject
- 包含
    - happiness高兴值
    - text日记内容
    - selfie自拍照片(DiaryPicture)
    - pictures其他图片(DiaryPicture组成的RealmList)
    - date日记创建时间
- 使用getter和setter访问和设置
- **不可直接调用setter**，必须调用diaryHelper.updateDiary方法来更新Diary
- **注意**：DiaryHelper获取到的Diary都是和数据库处在“绑定”状态，即当数据库受到更新时那些获取到的Diary会自动更新

### DiaryPicture
- 日记中的图片类（包括selfie和pictures）
- 负责将bitmap转为byte数组，负责将byte数组解码为bitmap
- 调用getImage()方法即可获得bitmap
- 同样使用getter和setter访问和设置，但一般情况下使用getImage()方法就足够了

### DiaryHelper
- 日记帮助类**与日记有关操作均在此进行**
- 目前支持的功能
    - 保存Diary(saveDiary)
    - 更新Diary(updateDiary)
    - 获得最近一条Diary(getLastestDiary)
    - 获取某一天的所有Diary(getDiariesOfDay，可用Date或GregorianCalendar调用)
