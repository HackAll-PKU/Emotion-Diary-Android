# org.hackpku.emotiondiary.welcome

显示软件登陆时的欢迎页。

同时也是MVP设计范式的样板。
MVP设计范式的主要目的是把业务逻辑，如登陆，以及界面元素的显示逻辑，如大小的控制，动画的控制等，从activity中剥离出去，从而使代码更加清晰，降低耦合程度。

比如说，所有相应按钮，点击的逻辑，如果稍微复杂些，都可以放在Presenter中进行处理。
而更新界面的逻辑则放在View中，不过这次的示例没有单独的View，而是直接把View实现在了activity中。

参考资料：https://segmentfault.com/a/1190000003927200

http://blog.csdn.net/maosidiaoxian/article/details/40109337