# EmotionDiary for Android

* API Level: 21+ (Android 5.0)
* IDE: Android Studio 2.1 +

##组成
本项目按照活动（或者说视图），包括五个主要部分（Activity）：

* Welcome: 打开应用后的欢迎页，同时有引导用户登录的功能，基本代码已经给出，供参考。
* Homepage: 首页，显示日历和当天的日记
* Stats: 显示用户的统计信息
* RecordEmotion: 记录日记
* ShowDiary: 显示日记

每一部分均位于单独的包中，同时，每个包都是一个MVP结构，分别有model、view、presenter文件夹，activity class在view文件夹下。每个文件夹中，需要有接口和实现至少两个class文件，接口类的类名以大写I开头，实现类的类名以Impl结尾（activity class除外）。在每个目录下，还应尽量提供README.md以帮助快速了解。

此外，如果有认为需要共用的代码，请实现在common文件夹下。
