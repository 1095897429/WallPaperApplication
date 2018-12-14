22018.10.29
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
    0.界面整理 -- ok
    1.测试数据 -- ok

3.内容详情页
    1.举报 -- ok
    2.分享 -- ok
    3.上传选中逻辑 -- ok
    4.点击事件 -- ok
    5.选择文件  -- 弹框出现 -- ok

2018.12.8
    1.mac上 octotree的安装 -- no
    2.webview不能定位 -- https://www.jianshu.com/p/798cbc2b27a9 -- ok
    3.打包 -- 前期在com.ngbj上修改包名为com.assisii,后面的包专属为小米华为的包，导致前面的包在其他市场上能用，打包需要打两份包，累啊！ -- 谨记 -- ok

2018.12.10
    1.作品发布界面逻辑 -- 参考：https://blog.csdn.net/tongsiyuaichidami/article/details/79224849
        1.4.4以上 -- 相册获取的path为file:/// -- https://blog.csdn.net/qq_32266991/article/details/52655038 -- ok
        3.4.4以上的拍照 -- 单个危险camera权限 -- ok

    2.dialog 和 activity 数据交互
        1.观察者模式 -- 定义接口，被观察者主动发送通知，观察者接受通知 -- ok

    3.设置默认浏览器方案研究
        1.先在应用中设置 -- 判断是否有默认，有的话先清除 -- 然后再设置 -- 始终即可 -- ok
            https://www.cnblogs.com/hitnoah/p/4670553.html
            https://blog.csdn.net/lwyygydx/article/details/42873829
    4.内容详情页
        1.举报 ，返回 ， 下载，分享，预览 ，
    5.浏览器上传百度市场，之前的版本是没有加固的，现在加固后上传不了，原因是签名不正确，也不知道怎么回事,去掉加固的就可以了
            1.之后的代码 -- 需加固 在上传 -- ok

2018.12.11
1.七牛云上传 (uri表现形式)
    1.拍照的路径 在7.0之后 -- content://com.ngbj.wallpaper.provider/path/output_image.jpg -- ok
    2.拍照的路径 在7.0之前 -- file:///storage/sdcard/Android/data/com.ngbj.wallpaper/cache/output_image.jpg -- ok
    3.相册4.4以下 -- content://media/external/images/media/14 -- ok
    4.相册4.4以上 -- content://com.android.providers.media.documents/document/image%3A50 -- ok
    问题是：在7.0系统的手机上拍照，返回的uri是利用fileProvider的，不显示全路径，如何获取全路径
        https://blog.csdn.net/u010853225/article/details/80191880 -- 已解决

2.浏览器
    1.首页用户信息初始化新增两个字段，用于纯净版数据 -- 待做
    2.红米6pro 刘海屏适配 -- 审核中

3.内容详细页
    1.设置壁纸方案
        1.wallpapermanager设置 -- 无法调整图片
        2.通过系统程序设置 -- 裁剪图片，效果最好
    2.静态壁纸
        1.调用系统的方法设置 --
    3.动态壁纸
        1.adapter中利用了mediaplayer，如何释放？ --
4.activity管理，网络管理
        1.activity管理集合放在application中 -- ok
        2.网络管理
            1.android 7.0静态注册广播方式被取消了 -- 采用动态注册 -- ok
            2.参考 https://www.jianshu.com/p/89e3fbd33964 -- ok

5.广告纯净版接口 -- 浏览器 V1.1.0 -- 版本更新做 -- 2018.12.11开始

2018.12.12
1.leakcanary添加测试版 -- ok
2.默认浏览器
    1.弹出提示框 -- ok
    2.设置中新增默认浏览器 -- ok
    3.新增设置浏览器界面 -- ok
    4.检查是否已经设置了浏览器  -- ok
3.打开图库 和 打开系统相册
    1.系统相册 -- action : android.intent.action.GET_CONTENT -- ok
    2.图库 -- action : android.intent.action.PICK -- ok
4.静态壁纸
    1.下载网络图片 -- 更新到图库中 -- 在图库中查找此图片名称
5.产品分享：语音搜索 + 地址定位

2018.12.13
1.闪屏界面
    1.界面 -- ok
    2.数据联调 -- ok
    3.逻辑待定 -- 50%
2.小米6Pro适配
    1.在xml中新增配置 -- 绘制到耳朵区/填充屏幕底部 -- ok
3.公共参数的配置
    1.设备唯一标识 -- ok
    2.拦截器 -- https://blog.csdn.net/jdsjlzx/article/details/52063950 -- hashmap(gson) -- post发送 -- ok
    3.初始化接口 -- ok
    4.首页接口 -- 80% -- 并没有显示动态壁纸
        1.后台返回的recommand的并不是同一个，我们可以将其组合封装成一个Bean -- ok
    5.壁纸列表页接口
        1.下方的列表 -- ok
        2.上方的分类 -- no
4.6.0以下，先把图片下载下来 ，再设置
  6.0以上，有的是跳转到系统设置界面【1.不用设置  2.自动设置】
5.产品分享：好奇心（屏幕上有悬浮框，有美女跳舞）

2018.12.14
1.兴趣分类(有可能返回的key 和 value的 值 -- 可考虑map -- 有机会思考🤔)
    1.返回壁纸兴趣分类集合 -- ok
    2.上传兴趣分类 -- 数据格式不对
    3.壁纸详情页 -- ok
    4.获取图片上传的token -- ok
2.更新
    1.新版本上线 -- ok
    2.老版本维护 -- 记住签名 -- 周末修改
3.采用 viewpager fragment
    1.FragmentPaperAdapter 是将viewpager的每页每一个条目当成一个Fragment -- ok
    2.实例 -- ok
    3.带入项目 -- ok
4.首页获取数据 -- 存入到greendao中(有categoryid..) -- 点击明细时 -- 网络请求加载图片信息 -- 切换碎片 --  网络请求加载图片信息
    1.重新一个类 -- 只记录壁纸（wallpagerBean） -- ok
    2.首页获取数据 -- 保存到数据库中 -- ok
    3.从数据库中加载数据，切换到指定的索引 -- 【 壁纸 + item广告】作为一个整体 -- ok
    4.首页点击广告 -- 跳转到webview
    5.点击Item的加载默认的大图
        1.进入界面后加载数据，这样每个调用这个界面的入口都不要重新写 -- 逻辑正确 --
        2.进入界面前加载数据，这个在每个入口都要写，界面多起来很麻烦 -- 不建议采用
    6.greendao只认原始数据 --

5.明细的话选择全加入，不然索引有问题  -- 商定ok






























