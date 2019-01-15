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
1.七牛云上传 (uri表现形式 )
    1.拍照的路径 在7.0之后 -- content://com.ngbj.wallpaper.provider/path/output_image.jpg -- ok
    2.拍照的路径 在7.0之前 -- file:///storage/sdcard/Android/data/com.ngbj.wallpaper/cache/output_image.jpg -- ok
   -------------------  fileprovider一般用在拍照 + 安装apk的时候  -------------------
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
    6.greendao只认原始数据s

5.明细的话选择全加入，不然索引有问题  -- 商定ok

2018.12.17
1.fragment切换时请求明细接口
2.接口
    1.手机号登录 -- ok
    2.验证码 -- ok

    4.搜索壁纸 -- 输入汉字，传到后台显示的是编码后的字符，这样明显不正确 -- URLEncoder.decode -- ok
    5.用户上传壁纸 -- 静态（）  --- 动态（）
    6.banner专题详情页 -- ok
    7.酷站导航列表页 -- ok
    8.热搜词搜索壁纸 -- ok


3.视频上传
4.fragment中的onActivityResult调用
    1.只嵌套一层做法 -- 直接使用startActivityForResult -- no

5.读取sd卡图片到inputstream中 -- new FileInputStream(fileName) -- no

6.让edittext不获取焦点
    1.让其父控件有焦点，这样它本身就没有焦点了 -- ok
7.下拉google官方控件 -- p中调用view.complete方法完成一些统一操作 -- ok

2018.12.18

2.优化Fragment传入参数和获取数据 -- ok
3.接口
    1.修改用户头像 -- ok
    2.用户举报 -- ok
    3.搜索页 -- ok
    4.用户下载，分享，收藏 -- ok
    5.用户关键字搜索历史 -- ok
    6.上传兴趣分类 -- ok
    7.用户上传壁纸
        1.静态壁纸 -- ok

4.我的界面授权不走自己的fragent中的onRequestPermissionsResult --
5.浏览器
    1.纯净版数据调试 -- ok
    2.蜜桃JPush -- ok
6.用于没考虑到图片的后缀名，xxx.jpeg的图片我没有判断，导致bitmap一直为null -- ok
7.圆角图片 -- https://www.jianshu.com/p/4107565955e4?open_source=weibo_search --
8.友盟appkey -- 5c18c4adb465f5aeb1000058 -- ok
9.base64图片转换 -- http://tool.chinaz.com/tools/imgtobase -- ok


2018.12.19
0.优化界面跳转构建，让逻辑跳转来更优雅,在一个界面中归纳参数  -- SearchActivity ,DetailActivityNew-- ok

1.视频上传研究（稍后 -- 先不做）
    1.获取本地视频缩略图展示 ， 拿到视频的地址

2.浏览器酷站导航不用翻页 和 百度ssp的研究
    1.纯净版数据优化 -- 在head中新增两个字段，将首页请求广告的请求两个字段去掉 -- ok
    2.浏览器酷站导航不用翻页 -- ok
        1.TODO 新增 -- xml替换 -- onCreate初始化控件 --  initCoolRecyclerView  -- 新增adapter -- 隐藏getAdData2,换成getAdData2_2 -- 其他的fragment照样 -- ok
        2.TODO 默认浏览器 -- xml添加 -- 布局/类添加 --
        3.TODO 酷站不翻页
        4.TODO 推送
        5.TODO 不强制更新 --
        6.TODO html定位 -- setting设置 -- setWebChromeClient --
3.接口
    1.获取用户下载，分享，收藏壁纸 -- ok
    2.用户上传壁纸历史 -- ok
    3.用户上传壁纸
4.列表item的优化
    1.图片圆角/圆形 -- ok
    2.圆形边框 -- ok
5.布局构建
    1.布局刚开始的加载中，加载失败 -- 利用emptyView布局在设置 --ok



产品分享：功能上的，一些关注喜好的话，app会类推和他相关的内容，比如我的想法是点击喜好的壁纸，在开辟一个发现模块，类似于喜好这款壁纸的人都在看什么等等，显示一些喜好明星的一些视频，新闻啥的。可以吸引用户


2018.12.20
1.喜好接口逻辑 -- ok

2.透明状态栏效果 -- https://www.jianshu.com/p/afa921d8ed24 -- no
    标志位：
    5.0(API 21)以上:
     1.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS  +  FLAG_TRANSLUCENT_STATUS 同步设置-- 在API >= 21,系统支持给状态栏着色 --
    4.4(API 19)以上:
     1.系统不支持给状态栏着色，可通过(透明状态栏 + 透明背景颜色) 实现
        1.将系统状态栏设置为透明 -- DecorView会占用状态栏的高度，会是整体内容上移一段距离
        2.设置对应的背景颜色
     低版本：
      1.在API 19一下系统不支持沉浸式状态栏

3.签名的方式 -- https://www.cnblogs.com/gao-chun/p/4891275.html -- ok
    1.965e9af1dc4a5f91c6a1b6360b99c660 应用签名 -- ok


4.greendao的异常 Expected unique result, but count was 3 -- ok

5.展示动态壁纸
    1.设置动态的路径 -- ok
    2.给surfaceView设置图片 --
    3.设置动态壁纸 -- https://blog.csdn.net/lmj623565791/article/details/72170299
    4.下载壁纸 -- RxVolley
        0.RxVolley下载 -- unexpect response code 206 -- no
        1.下载路径 -- /sdcard/emlute/0/bianselong/wallpapers -- java.io.IOException: No such file or directory -- 一般逻辑是检查创建 -- ok
        2.原生下载到指定位置 -- ok
        3.通过网络设置动态壁纸 -- ok

6.加载视频时设置背景图片 --

2018.12.21
1.明细中收藏 ，如何改变首页的收藏
2.释放mediaplay资源 -- https://blog.csdn.net/sdfdzx/article/details/60144834 --
3.优化UI整体样式 --
4.优化整体逻辑(搜索 + 专题 )Activity:
    1.统一了壁纸Item的点击事件 -- super.initEvent --  ok
    2.获取数据后，封装数据库 -- 专题页中type 为空？ --

5.优化整体逻辑(搜索 + 专题 )Fragment:
    1.统一了壁纸Item的点击事件 --
    2.获取数据后，封装数据库 --

6.原生webview -- ok

产品分享：添加用户反馈模式，利用语音录制上传 ，要和用户体验


2018.12.23
1.viewpager 和 fragment 加载一个界面 -- 去掉预加载 -- ok

2.首页获取数据 -- 在获取数据时，将数据封装成mulBean，存入数据库中(type)
3.每个地方的跳转到详情页
    1.后台返回的是19条数据，其中最后一条数据是api广告 ，加入 -- ok
    2.跳转 -- ok
    3.专题页没有type -- ok


2018.12.24
1.微信第三方登录
    1.友盟sdk下载 -- 手动ok
    2.登录
        1.QQ --
        2.
2.fragment切换时，mediaplay不起作用了 -- ok
3.重构初始化接口 -- 利用手机自带定位，获取经纬度，调用百度地图地址 -- https://blog.csdn.net/wn1245343496/article/details/69229951 --
  取消收藏接口 -- ok


2018.12.25
1.百度基础定位sdk
    1.发布版的sha1 + 注册 + 权限 + 配置-- ok

2.明细加载数据库的数据，当明细获取请求数据后，如何更新显示的数据源
    1.获取数据源 -- 进来判断是否需要发送更新请求(根据类型 + 是否有值) -- 刚进来 + 切换 --  ok
    2.获取数据后 -- 请求成功后 ，更新数据库数据 + 取出数据从数据库中 + 更新界面 -- ok
    3.先设置默认的图片 -- 当请求完成后，播放视频 ，让缩略图渐变消失 -- ok
3.浏览器打包 -- 蜜桃 -- ok
4.视频资源加载完后才能播放
    0.确保在 onPrepared 方法调用之后
    1.长按按钮
    2.壁纸item ,广告item都会有wallpaperId,api广告是没有的 -- ok
    3.默认加载为缩略图 -- ok
5.微信登录
    1.后台修改了gender返回的类型，让其返回int

2018.12.26
1.优化闪屏页 地址位置权限的逻辑 -- ok
2.明细中收藏/取消收藏 ，改变跳转数据源的状态值
    1.请求成功 -- 更新数据库 -- 更新主界面 -- ok

3 每个主界面收藏/取消收藏
    1.请求成功 -- 更新数据库 -- ok



5.都是跳到Detail界面
    1.IndexFragment -- 保存ok -- 爱好ok
    2.SearchActivity -- 保存ok -- 爱好ok
    3.CategoryFragment -- 保存ok -- 爱好ok
    4.NewAndHotFragment -- 保存ok -- 爱好ok
    5.CreatFragment -- 保存ok -- 爱好
    6.UploadHistoryFragment -- 保存ok -- 爱好ok

2018.12.27
1.修改数据限定最大值的判断 -- 不在前端设置阈值 ，之前设置为19 --  已去掉 list.size() < AppConstant.PAGESIZE -- ok
2.看创作这个界面的逻辑 -- 取消收藏后刷新 ？？？ -- 暂不写
3.目前只有上传界面需要登录
    1.新增逻辑 -- ok
4.优化桌面壁纸
    1.设置建议的大小 -- ok
5.明细界面，预加载下一页数据
    1.当数据快到底部的时候，加载下一页数据，保存在临时temp中，不保存到数据库中
      新增参数 ：page category order -- ok
    2.方式1：主界面数据存储数据库中 -- Detail界面中获取，存入temp中 -- 每次操作temp即可 -- 暂不考虑
      方式2：主界面数据存储数据库中 -- Detail界面中获取，存入temp中 -- 每次操作数据库
        1.通过调试 -- 发现广告返回的Id是一样的(null 1 2 3) -- 如何解决 -- 考虑？？？
        2.明细加载更多成功，保存到数据库中，然后更新主界面数据，目前返回没有调到具体的位置，需要把page传递到主界面 -- 考虑？？？ -- 暂不写

6.查看 图片 和 壁纸 和 广告 返回数据的区别？？
    1.广告 -- 构建成WallBean -- id  fromWhere title type wallpaperId .... -- ok
    2.因为广告从广告池中获取 -- 切换时先通过通过type 判断 -- ok
    3.明细界面切换 -- 通过temp中获取值 -- ok

7.明细界面分类的逻辑？？ -- 后台返回category数组 -- ok
    1.点击事件 -- 跳转到分类界面 -- ok

8.明细界面操作加载框？? -- ok
9.启动图片？? -- ok
10.主界面一次性申请多个权限？？-- ok
11.将page返回给主界面？？ -- ok

12.记录数据
    1.IndexFragment -- 明细界面加载  -- ok  mPresenter.getMoreRecommendData(mPage,mCategory,mOrder);
    2.SearchActivity -- 明细界面加载 [区分 3个不同的来源 ]-- ok
    3.CategoryFragment -- 明细界面加载  -- ok  mPresenter.getMoreRecommendData(mPage,mCategory,mOrder);
    4.NewAndHotFragment -- 明细界面加载  -- ok  mPresenter.getMoreRecommendData(mPage,mCategory,mOrder);
        1.调用两次事件，难道是fragment的问题

13.添加手势操作，监听滑动，让只有在其在下滑时才能发送请求 -- ok
14.当更多没有数据时，需要添加flag -- 让其不要发送请求  ？？？-- 暂不操作
15.问题描述：首页请求成功，搜索页请求成功，返回首页，点击明细 强退？？ -- ok
16.主界面 更多的跳转到所有分类界面 -- ok
17.设置界面清除缓存 -- ok

2018.12.28
1.锁屏壁纸？？-- 难度大，不同的机型需不同的设置
2.避免重复下载图片 和 视频 ？？
3.recycleView全刷新时，下一页加载完成？？
4.明细界面设置广告图片？？
5.下载后显示广告，需接入sdk？？
6.创作界面上传有问题，是因为token的原因吗？？
7.第三方分享？？
8.友盟渠道，点击统计？？
9.登录后跳转逻辑？？
10.登录时解析错误，http应该是先解析，loginbean 和 ""解析错误 ReponseBody？？？
11.首页点击刷新会让刷新到首页？？-- ok
12.分类滑动选中状态？？
13.喜好反馈变大？？ -- ok
14.重构缓存逻辑？？ -- ok
   list  --->   detail
                  |
     （EventBus） |
   尾加  <---   list1
15.每次都会加载adpter的getView方法，其次请求完成后只更新少数的数据？？


2018.12.29
1.明细中请求数据后加载大图缓慢??
    1.先经过adapter -- 再经过playVideo --
2.分类滑动选中状态？？ -- ok
    list --->  map<key , bean>(显示)
                    |
                    |
   改变bean   <--- 点击

3.激励视频研究与接入？？


2019.1.2
1.预加载把数据返回给来源 [有刷新的不返回] -- ok
2.AS中的logcat恢复，去掉windowed mode -- ok
3.login存储问题的解决 -- ok
4.上传时一些判空操作，以及上传成功后图片需要审核 -- ok
5.激励视频研究与接入？？
    1.研究其demo 和 文档 ,将初始化放在DetailActivity中 -- 待测试 -- ok
6.下载页的逻辑整理
    1.激励视频弹出框 -- ok
    2.每天免费给予三次下载机会，超过三次就必须看视频解锁下载 -- ok
7.关于的界面 -- ok
8.获取验证码多次，没有响应的提示 -- ok

2019.1.3
1.在我的界面登录成功后，头像和昵称没有修改 -- ok
2.退出登录用户信息没有清除 -- ok
3.验证码倒计时？？-- ok
5.明细壁纸优化？?
    1.直接点击 -- 优化updateToDesktop 方法 -- ok
    2.滑动翻页 -- 减少findviewById的时间 --
    3.最底层是视频 -- 缩略图 -- 大图[让视频显示，不然加载不出，在适配器中通过alpha让其看不见，在点击时让其看见] -- ok
    4.一开始界面白屏如何解决？？
6.调试新增返回壁纸分类接口？？-- ok
7.分类兴趣界面背景改变?? -- ok
8.专题页面的banner图没有修改？？-- ok
9.列表页收藏，更新temps??【每个presenter都需要写，太麻烦！！】
    1.首页 -- ok
    2.专题页 -- ok
    3.搜索页 -- ok
    4.分类子页 -- ok
    5.最新最热页 -- ok
    6.下载，分享，下载页 -- ok
    7.创作页 -- ok

2019.1.4
1.搜索记录退出？? -- ok
2.动态壁纸下载到本地比直接加载网络url快3秒以上，下载不同的动态壁纸调用系统方法却显示上一个壁纸的路径？?
3.动态视频为啥自动播放？？-- ok
4.点击播放没有优化，在资源加载完成后???
5.优化加载的下载锁的逻辑 -- ok
6.优化兴趣爱好界面??？-- ok
7.华为手机[后台统一返回 1080  1920 ] [屏幕1 1080  1794 ][屏幕2 720  1280] --  ok
8.分类页中顶部添加蒙层？？-- ok
9.点击推荐再返回来时 列表会滑动到顶部,在有头部view的情况下？？-- ok


2019.1.5
1.小广告走api -- 通过type判断 -- 价值高的放在滑动页

2019.1.7
1.优化下载逻辑，第二天忘记设置当前时间了？？ -- ok
3.优化明细界面加载更多？？-- ok
4.优化自动清除缓存？？-- ok

2019.1.8
1.优化壁纸性能
2.RecyclerView.mContext -- LeakCanary?? -- ok
3.优化壁纸详情页全屏？？
4.定义上传图片规则？? -- ok
5.重构界面传值，防止内存泄漏？? -- ok
6.重构壁纸实体，新增橱窗广告id ，如下图

    id
    ad_id(先不用这个)/id()   --->  wallpaperId(实体id)
    api_id

7.优化壁纸明细页，加载过的数据不能点击 ？？ -- ok
8.更新sigmob？？-- ok
9.更新头像更新不成功？？后台返回的数据pc端打不开？？ -- ok
10.首页banner 和 酷站导航可能有广告，注意区分？？-- ok
11.重构了列表页中ap广告实体Bean?? -- ok
12.详情页切换广告时直接跳转到其落地页？?
14.优化我的页面中下载，分享的适配器？？ -- ok
15.详情页中标题去掉？？ -- ok


2018.1.9
2.极光推送 + 保活？？-- ok
3.搜索页广告？？ok
4.广告点击数 ，广告点击用户数？？-- 橱窗位置ok,酷站 + banner广告？？
5.美团打包不可忘记app.build中的关闭签名方案？？ -- ok
6.酷站导航返回的类型type是3？？酷站和banner广告返回0？？ -- ok
7.讨论下推送自动跳转到html的逻辑？？
8.列表中api广告？？-- ok
9.优化退出逻辑？？建议退出后，回到未登录状态-我的页面 -- 退出后创作不可见？？ -- 待测试
10.下载需判断是否有读入sd权限?? -- ok
11.优化上图图片的路径，改为线上的版本？？-- ok
12.登录成功后，创作界面下拉加载框不消失？？ -- ok
13.看视频下载没有向后退发送下载请求？？ -- 将代码中的wallpaperId删除了，导致没有赋值成功 -- ok
14.webview点开后，有的url后退不能返回到上一页？？

16.友盟appKey给予的不正确，看log?? -- ok



2019.1.10
1.app跳转到设置权限界面，手动设置，返回app强退？？ -- ok
2.广告点击数 ，广告点击用户数？？-- 橱窗位置ok,酷站 + banner广告 + search广告 + 搜索广告？？ -- 待测试 -- ok
3.上传壁纸文本限定10个汉字？？ -- 测试 -- OK
6.优化壁纸上传页？？ -- 相机拍照的图片太大，上传图片慢 -- 添加了加载框 -- ok
7.优化专题页，内容更新上？？ -- ok
8.首页新增刷新按钮？？ -- 将数据集合置为空 --  ok
9.开屏广告？？第一次不给予广告，在跳转到主界面之前放广告？？
10.退出时发布界面没有刷新？？ -- 测试 -- 退出后，集合刷新了 --  ok
11.广告页添加返回按钮？？ -- 添加广告点击事件 id给予10000 -- 点击事件还没有弄 -- ok
12.webview新增返回按钮？？ -- ok
4.上线记得去掉leakCanary??
5.登录时countDownTimer内存泄漏？？-- ok
13.推送逻辑，分4个类别？？ -- ok
14.搜索时，隐藏软键盘？？-- ok
15.分享的时候，点击了就发送请求？？ -- ok



2019.1.11
1.优化列表页中新增返回按钮？？-- 分类 + 搜索 + 最新/热 -- ok
2.超时的提示以及下拉刷新的取消？？-- ok
3.数据加密优调？？
5.列表页返回的nickname为空，啥都不显示？？ -- ok
6.优化明细界面展示？？ -- ok
7.查看百度联盟sdk？?
8.新增修改昵称界面？？ -- ok
9.激励视频广告线上版本??
10.广告视频id?? -- ok
11.下载图片弹出框的背景Dialog?? -- ok
12.data parcel size 524152 bytes ,跳转到明细界面携带的参数过大？？
    1.做个数据判断，如果太大就提示 ？？---

2019.1.12
 1.搜索 每点击搜索第一次就清空数据 -- 然后再把结果加入数据库  -- 明细界面 获取数据库 -- ？？-- ok
    2.主界面 -- AppConstant.INDEX -- 修改事件 -- 修改收藏事件 -- ok
    3.分类 --  AppConstant.CATEGORY + category --- 【 hashmap<categoryId,temps> -- 不能实现目标，太大导致崩溃】-- ok
    3.最新最热 --  AppConstant.CATEGORY_NEW_HOT_TEST + category  --- 修改事件 -- 修改收藏事件 -- ok
    4.搜索 -- AppConstant.SEARCH -- 修改事件 -- 修改收藏事件 -- ok
    5.专题页 -- AppConstant.SPECIAL -- 修改事件 -- 修改收藏事件 -- ok
    6.下载/分享 -- 利用临时变量 -- ok
2.可以去掉temps这个临时变量数据？？
3.专题页 || 我的界面 没有加载更多，在切换的时候不判断即可！！ -- ok
4.下载广告的图片接口？？ -- ok -- 记录广告 + 跳转广告落地页  ？？ --- ok
5.首屏广告优化？？-- ok
6.应用签名获取？？-- GenSignature -- 测试
7.打包
    1.切换到相应的目录下 -- cd channel目录
    2.执行python 目录下的MultiChannelBuildTool.py
    3.多渠道打包 -- mac目前还不行，移交给window
8.加固
    1.腾讯云中去下载加固工具，乐固 -- ok
    2.【没加固】 -- 应用签名：965e9af1dc4a5f91c6a1b6360b99c660
    3.【加固 -- 签名打包 ---> 验证过已加固】 --应用签名：965e9af1dc4a5f91c6a1b6360b99c660
    4.再去多渠道打包 -- 在经过3之后，也可以获取到渠道名



9.混淆代码 -- ok
    1.添加不需要混淆代码
        1.七牛云 -- 混淆
        2.butterknife -- 不混淆
        3.easypermissions --
        4.banner -- 不混淆
        5.SecurityEnvSDk(友盟 -- 金融风控组件) -- 已放在友盟不混淆中
10.在我的界面中点击获取渠道？？ -- ok
11.先测试混淆代码?? -- ok


2019.1.14
1.修改其他4款软著包名以及方法名（ 软著和app名称不一致 -- ok）
    变色龙壁纸 com.ngbj.wallpaper
    二次元壁纸 vegetarian.anime.post
    小姐姐壁纸 airspace.sister.card
    精品壁纸   empty.black.singer
    一张壁纸   shear.one.actor

3.软著的名字一定要和开发的名字一样，这个需要检查？？ -- 今重新上传了 -- ok
4.其他4款应用名 友盟appkey(自己申请 -- ok)
        变色龙壁纸 5c18c4adb465f5aeb1000058
        二次元壁纸 5c3bf84bf1f556bcd6000ae5
        小姐姐壁纸 5c3bf93db465f5ac4d001003
        精品壁纸   5c3bf970b465f5abd60010d3
        一张壁纸   5c3bf9b0b465f5bde6001395
5.优化加密获取后台数据
    1.防止数据被盗取，采取将数据加密返回，在app端采用解密的方式
        a.com.google.gson.internal.LinkedTreeMap cannot be cast to com.ngbj.wallpaper.bean.entityBean.InitUserBean


6.申请百度地位的appKey
    变色龙壁纸  ayUcDL2bZnbVrYzuRkyywo48MoLhz4Az
    二次元壁纸
    小姐姐壁纸
    精品壁纸
    一张壁纸

7.开发二次元壁纸？？
    1.项目开发好了去申请百度定位sdk

8.第一版遗留问题
    1.第一次启动，点击倒计时，跳转到兴趣分类界面，随后跳转到主界面？？
         a.点击没有关闭定时器，导致跳过 -- 已修复
    2.分享图片太大，导致启动分享慢 以及 取消分享，显示分享成功的问题？？
        a.用户设置的图片大小最好不要超过250k，缩略图不要超过18k
    3.分享成功后，桌面壁纸修改了？？
        a.忘记屏蔽代码了 -- 已修复

9.账号整理发送给小六？？ -- ok
10.整理to-do-list -- ok

2019.1.15
1.继续完善加密？？ -- ok
2.继续完善分享图片太大的问题？？-- 质量压缩 -- ok
3.公共参数fromPlat填入default？？ -- 修复为AnalyticsConfig.getChannel(this) -- ok
    反思：前期写代码时，一些动态的参数不能写死，用TODO 标注下 此参数需要动态修改的
4.腾讯云推送？？



时间节点：
1.19号之前完成4款壁纸?？
2.26号完成百度ssp，拉新两个主要的功能点，可以延后一到两天？?
3.26号之后需猜灯谜活动调起页，搜索赚钱第三方，悬浮广告接入，能做多少做多少？?











