2018.10.29
1.代码上传到github -- ok
2.七牛云上传头像图片 -- ok
    参考：https://blog.csdn.net/fnhfire_7030/article/details/77146682
3.七牛云上传1920*1080图片（1440 * 1280也即可，640 * 960会以中间拉伸）
    1.直接从图库中拿去 -- 判断系统版本号 获取图片路径
4.设置系统壁纸
    1.设置壁纸权限 -- ok
    2.预览图 -- 设置壁纸
        1.图片为960 * 640 -- 屏幕为 1360 * 720 -- 显示有点拉伸，建议用1920 * 1080图片

5.设置锁屏壁纸 -- 复杂！

2018.11.14
1.项目中准备greendao
    优点：1.支持数据库加密 2.轻量级 3.代码自动生成 4.存取数据快 5.ORM对象关系映射
2.Retrofit
    1.Retrofit配置
3.RxJava(ReactiveXJava) -- 响应式编程

2018.11.23
1.加密逻辑的验证 -- ok

2018.11.26
1.https://www.jianshu.com/p/084a79648570 -- ResourceSubscriber
2.https://www.jianshu.com/p/32bfd5fd8b48 -- retrofit json字符串
3.获取验证码 demo -- 成功

2018.11.27
1.parcelable插件的安装 -- ok
2.item的布局时在用到BaseRecyclerViewAdapterHelper 时 高度需要设置wrap_content -- ok
3.gridlayoutmanager布局的居中 -- linearlayout 宽度是match_parent

2018.11.28
1.加载更多 -- 逻辑实现 （ok） -- 自定义布局
2.广告的实体bean -- ok
3.滑动到顶部 -- ok
4.首页的各部分点击事件 -- ok
5.首页的喜好点击事件 -- 绑定子view的点击事件 -- ok
6.点击收藏之后 （全局优化ok）局部刷新 -- https://blog.csdn.net/jdsjlzx/article/details/52893469 -- 待优化
7.recyclerView去掉滑动边界阴影 -- https://blog.csdn.net/ming2316780/article/details/51578621 -- ok
8.封装的toast使用 --  ok  --（研究去掉不显示的问题）-- 待研究


2018.11.29
1.分类的蒙层，用于显示出文本 -- 待优
2.首页的搜索跳转 -- ok
3.github上传 -- as配置时不显示登录密码 -- 查bug
4.分类中各部分点击事件 -- ok
5.内容详情页
    1.点击图片，隐藏上下部分的布局 -- ok
    2.各种点击事件 -- ok
    3.播放视频
        1.原生播放 -- surfaceview + mediaplayer -- 测试机不能播放 -- ok
        2.mediaplayer访问raw目录下的文件 -- ok
        3.获取url 或者 本地视频视频的第一帧图片 -- https://blog.csdn.net/jyp123123/article/details/51841168 --
    4.滑动切换下一页
        1.展示（上下切换，左右切换） -- https://github.com/castorflex/VerticalViewPager -- ok
        2.临时数据（数据库） --

2018.12.2
1.我的界面
    1.viewpager的适配器 默认为v4下的FragmentPagerAdapter -- https://blog.csdn.net/u012756920/article/details/78582448 -- ok
    2.标题的显示 -- ok
    3.毛玻璃效果 -- https://blog.csdn.net/cen_yuan/article/details/52205237 -- ok
    4.vp中列表显示不全的问题 -- ok
    5.点击事件 -- ok
    6.添加空布局 -- ok
2.mac 链接的话，首先在Terminal上授权，所谓的授权就是通过  git config --global 配置远程仓库 ，然后在设置中的github填入密码 -- ok

2018.12.3
1.抓包工具
2.CPL -- 广告对接 -- ok
3.mac打包 -- 利用python打包工具 -- ok
    cd channel(切换到对应目录下) -- python /Users/niaogebiji/channel/MultiChannelBuildTool.py(执行命令)

4.软著的代码和使用手册 -- 预计周三下午给代码
5.微信登录
    1.-- https://blog.csdn.net/qq_15796477/article/details/78813665 -- ok

2018.12.4
1.搜索界面
   1.热搜点击 -- ok
   2.广告 + delete广告 -- ok
   3.历史记录  -- ok
   4.历史记录删除 -- ok
   5.软键盘 -- 搜索action -- ok
   6.数据库查询 -- 确定热搜中没有广告 -- dbmanager放application中 -- ok
   7.点击搜索 -- 添加到数据库中 -- ok
   8.点击热搜 -- 添加到数据库中 -- ok
   9.点击删除 -- 删除到数据库中 -- ok

2.调用第三方浏览器打开网址或下载文件 -- https://blog.csdn.net/yingtian648/article/details/79128663/ -- ok

2018.12.5
1.了解测试(https://blog.csdn.net/lmj623565791/article/details/79623159)
2.兴趣分类
    1.点击Item -- ok
    2.点击完成 -- 记录兴趣id -- ok
3.免责条款 -- 添加文本 -- ok
4.搜索列表页 -- 放在搜索界面一起
    1.点击热搜搜索 -- 打开列表页，隐藏历史记录页 -- edittxt显示文本 -- ok
    2.点击关键字搜索 -- 打开列表页，隐藏历史记录页 -- edittxt显示文本 -- ok
    3.清除文本框 -- 隐藏列表页，打开历史记录页 -- ok
    4.搜索历史记录重复的bug -- ok
    5.点击历史记录 --  打开列表页，隐藏历史记录页  -- edittxt显示文本 -- ok
5.软著代码 和  说明书 -- ok
6.一元提现需要加入授权 --
7.分类头部点击 -- 跳转到最新最热 -- ok


2018.12.6
1.上传界面
    1.点击事件 -- ok
    2.选中事件 -- ok
    3.选中展示 -- eventbus -- no
2.实体 和 集合 以及 第三方的一定要判断  --- 防止空指针异常 -- 谨记
3.变量自动加m -- google开发规范之一 -- command + 逗号 -- Code Style -- Java -- Code Generate -- m -- ok
4.浏览器中html5 显示不出位置 -- https://www.jianshu.com/p/798cbc2b27a9 -- 添加webview的地位 -- ok
5.专题详情页
    1.添加置顶按钮 -- ok
    2.监听view的高度 -- ok
6.登录界面
    1.ui和逻辑 -- 注册验证码 -- 第三方登录(缺少) -- 80%

2018.12.7(周五)
1.ppt -- ok
2.上传界面
    0.界面整理 --
    1.测试数据 -- init初始化原始数据，为啥点击item时 点击返回按钮 -- 原始数据会变 -- error

3.内容详情页
    1.举报 -- ok
    2.分享 -- ok
    3.上传选中逻辑 -- ok
    4.点击事件 --
    4.选择文件  --




