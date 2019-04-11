package com.fenbitou.utils;

/**
 * @author 杨财宾 修改人: 时间:2015-8-29 下午1:37:10 类说明:接口地址的类
 */
public class Address {
    // 主域名
    public static String HOST = "http://t.268xue.com/app/";
    // 访问图片
    public static String IMAGE_NET = "http://static.268xue.com";
    // 上传图片
    public static String UP_IMAGE_NET = "http://image.268xue.com/goswf";
    // 分享的域名
    public static String HOST_SHARE = "http://t.268xue.com/";


    //分享关于我们
    public static String MOBILE_INDEX = HOST_SHARE + "mobile/index";
    // 支付异步回掉的域名
    public static String HOST_PAY = "http://t.268xue.com/";
    // 注册接口
    public static String REGISTER = HOST + "register.json";
    // 登录接口
    public static String LOGIN = HOST + "login.json";
    // 个人资料接口
    public static String MY_MESSAGE = HOST + "user/info.json";
    // 修改头像的接口
    public static String UPDATE_HEAD = HOST + "user/avatar.json";
    // 广告图
    public static String BANNER = HOST + "index/banner.json";
    // 推荐课程
    public static String RECOMMEND_COURSE = HOST + "index/course.json";
    // 首页公告
    public static String ANNOUNCEMENT = HOST + "index/article.json";
    // 课程列表
    public static String COURSE_LIST = HOST + "course/list.json";
    // 课程介绍
    public static String COURSE_CONTENT = HOST + "course/content/";
    // 课程播放记录列表
    public static String COURSE_PLAY_RECORD_LIST = HOST + "study/records.json";
    // 删除课程播放记录
    public static String DELETE_COURSE_PLAY_RECORD = HOST + "study/del.json";
    // 课程详情
    public static String COURSE_DETAILS = HOST + "course/info.json";
    // 验证播放节点
    public static String VERIFICATION_PLAY = HOST + "check/kpoint.json";
    // 资讯列表
    public static String INFORMATION_LIST = HOST + "article/list.json";
    // 资讯详情
    public static String INFORMATION_DETAILS = HOST + "article/info/";
    // 课程评论列表
    public static String COURSE_COMMENT_LIST = HOST + "course/assess/list.json";
    // 添加课程评论
    public static String ADD_COURSE_COMMENT = HOST + "course/assess/add.json";
    // 课程收藏列表
    public static String COURSE_COLLECT_LIST = HOST + "collection/list.json";
    // 添加课程收藏
    public static String ADD_COURSE_COLLECT = HOST + "collection/add.json";
    // 删除课程收藏
    public static String DELETE_COURSE_COLLECT = HOST + "collection/del.json";
    // 帮助反馈
    public static String HELP_FEEDBACK = HOST + "feedback/add.json";
    // 支付宝信息
    public static String ALIPAY_INFO = HOST + "alipay/info.json";
    // 微信信息
    public static String WEIXIN_INFO = HOST + "weixin/info.json";
    // 我购买的课程
    public static String MY_BUY_COURSE = HOST + "buy/courses.json";
    // 修改密码
    public static String UPDATE_PASSWORD = HOST + "user/update/pwd.json";
    // 修改个人信息
    public static String UPDATE_MYMESSAGE = HOST + "user/update/info.json";
    // 讲师列表
    public static String TEACHER_LIST = HOST + "teacher/list.json";
    // 讲师详情
    public static String TEACHER_DETAILS = HOST + "teacher/info.json";
    // 我的订单的接口
    public static String MY_ORDER_LIST = HOST + "order/list.json";
    // 创建订单的接口
    public static String CREATE_ORDER = HOST + "create/pay/order.json";
    // 支付前检测接口
    public static String PAYMENT_DETECTION = HOST + "order/payment.json";
    // 支付成功回调接口（注：在订单检测接口返回已支付成功的不能调用此接口）
    public static String PAYSUCCESS_CALL = HOST + "order/paysuccess.json";
    // 重新支付检验订单的接口
    public static String AGAINPAYVERIFICATIONORDER = HOST + "order/repayUpdateOrder.json";
    // 账户信息
    public static String USER_MESSAGE = HOST + "user/acc.json";
    // 播放记录
    public static String PLAY_HISTORY = HOST + "study/records.json";
    // 专业列表
    public static String MAJOR_LIST = HOST + "subject/list.json";
    // 课程里的教师列表
    public static String COURSE_TEACHER_LIST = HOST + "teacher/query.json";
    // 教师课程
    public static String TEACHER_COURSE = HOST + "course/teacher/list.json";
    // 请空课程
    public static String COLLECTION_CLEAN = HOST + "collection/clean.json";
    // 系统通告
    public static String SYSTEM_ANNOUNCEMENT = HOST + "user/letter.json";
    // 搜索
    public static String SEACRH = HOST + "search/result.json";
    // 社区搜索
    public static String GROUPSEARCH = HOST + "searchGroupTopic.json";
    // 检测版本更新
    public static String VERSIONUPDATE = HOST + "update/info.json";
    // 课程分享
    public static String COURSE_SHARE = HOST_SHARE + "mobile/course/info/";
    // 资讯的分享
    public static String INFORMATION_SHARE = HOST_SHARE + "mobile/article/";
    // 获取手机验证码
    public static String GET_PHONE_CODE = HOST + "sendMobileMessage.json";
    // 获取sgin
    public static String GET_SGIN = HOST + "getMobileKey.json";
    // 获取邮箱的验证码
    public static String GET_EMAIL_CODE = HOST + "sendEmailMessage.json";
    // 找回密码
    public static String GET_PASSWORD = HOST + "retrievePwd.json";
    // 个人简历
    public static String GET_PERSONAL_RESUME = HOST + "userResume.json";
    // js交互的接口
    public static String GET_JS = HOST + "limitLogin?";
    // 獲取優惠券
    public static String GET_USER_COUPON = HOST + "queryMyCouponCode.json";
    // 课程加载图文类型
    public static String GET_WEBVIEW_COURSE = HOST + "course/kpointWebView.json";
    // 添加登陆记录
    public static String ADD_LOGIN_RECORD = HOST_SHARE + "api/appWebsite/addlogin.json";
    // 添加安装记录
    public static String ADD_INSTALL_RECORD = HOST_SHARE + "api/install/addinstall.json";
    // 添加使用记录
    public static String ADD_APPLY_RECORD = HOST_SHARE + "api/use/addUse.json";
    // 获取优惠券列表
    public static String GET_COUPON_LIST = HOST + "coupon/getCourseCoupon.json";
    // 第三方登录的接口
    public static String THIRDPARTYLOGIN_URL = HOST + "appLoginReturn.json";
    // 待支付订单
    public static String ORDER_NO_PAYMENT = HOST + "order/repay.json";
    // 获取邮箱或手机号接受验证码开关的接口
    public static String GETCODESWITCH = HOST + "emailMobileCodeSwitch.json";
    // 绑定已有账号
    public static String BINDINGEXISTACCOUNT = HOST + "loginBinding.json";
    // 绑定第三方注册接口
    public static String REGISTERBINDING = HOST + "appRegisterBinding.json";
    // 获取个人中心中绑定第三方接口
    public static String QUERYUSERBUNDLING = HOST + "queryUserBundling.json";
    // 解除绑定
    public static String UNBUNDLING = HOST + "unBundling.json";
    // 添加绑定接口
    public static String ADDBUNDLING = HOST + "addBundling.json";
    // 播放视频的类型
    public static String GET_PLAYVIDEO_TYPE = HOST + "video/playInfo.json";
    // 播放记录清空
    public static String PLAYHISTORY_CLEAN = HOST + "studyHistory/clean.json";
    // 删除播放记录
    public static String DELETE_COURSE_PLAYHISTORY = HOST + "studyHistory/del.json";
    // 注册类型的接口
    public static String REGIST_TYPE = HOST + "registerType.json";
    // 用户协议
    public static String USER_AGREEMENT = HOST + "user/queryUserProtocol.json";
    // 关于分享的链接
    public static String ABOUT_SHARE = "https://apple.268xue.com/index/index.html";
    // 取消收藏的接口
    public static String CANCEL_COLLECT = HOST + "collection/cleanFavorites.json";
    // 检测用户是否在其他地方登录
    public static String CHECK_USERISLOGIN = HOST + "check/userIsLogin.json";
    // 发现课程音频课程列表
    public static String AUDIO_AUDIOLIST = HOST + "audio/audioList.json";
    // 音频课程详情
    public static String AUDIO_AUDIOINFO = HOST + "audio/audioInfo.json";
    // 音频评论列表
    public static String AUDIO_AUDIONODECOMMENT = HOST + "audioNodeComment/getAudioNodeCommentList.json";
    // 后台控制开关
    public static String WEBSITE_VERIFY_LIST = HOST + "website/verify/list.json";
    // 音频——评论列表
    public static String TOADD_AUDIONODECOMMENT = HOST + "audioNodeComment/toAddAudioNodeComment.json";
    // 音频——根据章id获取章节
    public static String AUDIO_NODELIST_BYNODEID = HOST + "audioNode/getNodeListByNodeId.json";
    // 音频——添加评论
    public static String ADDAUDIO_NODE_COMMENT = HOST + "audioNodeComment/addAudioNodeComment.json";
    // 音频——验证用户是否可播放某个音频节点
    // public static String CHECK_AUDIONODE_CANPLAY = HOST +
    // "audioNode/checkAudioNodeCanPlay";
    // 音频——下载前验证接口
    public static String CHEC_KDOWLOAD = HOST + "audioNode/checkDownload.json";
    // 音频——课程专业集合
    public static String AUDIO_SUBJECTLIST = HOST + "audio/subjectList.json";
    // 音频——相关推荐的接口
    public static String AUDIO_RECOMMEND = HOST + "audio/audioRecommend.json";
    // 音频——节点详情
    public static String AUDIO_GETAUDIONODEINFO = HOST + "audioNode/getAudioNodeInfo.json";
    // 音频——音频课程列表
    public static String AUDIO_QUERYMYAUDIOLIST = HOST + "audio/queryMyAudioList.json";
    // 创建音频课程订单接口
    public static String AUDIO_ORDER_CREATEORDER = HOST + "audio/order/createOrder.json";
    // 创建音频课程订单接口
    public static String AUDIO_ORDER_CHECKPAYORDER = HOST + "audio/order/checkPayOrder.json";
    // 支付成功回调接口
    public static String AUDIO_ORDER_PAYSUCCESS = HOST + "audio/order/paySuccess.json";
    // 音频——课程介绍
    public static String AUDIO_QUERYAUDIO_INFO = HOST + "audio/queryAudioInfoText.json";
    // 音频——订单列表
    public static String AUDIO_ORDERLIST = HOST + "audio/order/queryMyAudioOrderList.json";
    // 记录登录时间
    public static String LASTLOGINTIME = HOST + "lastLoginTime.json";
    // 直播列表
    public static String LIVE = HOST + "live.json";
    public static String LIVE_LIST = HOST + "live/list.json";
    // 我的直播
    public static String LIVE_USER = HOST + "live/user.json";
    // 直播详情
    public static String LIVE_INFO = HOST + "live/info.json";
    //新第三方登录
    public static String LOGINBYTHIRD = HOST + "open/bindingApp";
    //新解绑
    public static String UNBINDINGAPP = HOST + "open/unbindingApp";
    //查询用户信息
    public static String QUERYUSERINFO = HOST + "mobile/queryUserInfo";
    //绑定手机或邮箱  获取验证码
    public static String GETVERIFICATIONCODE = HOST + "mobile/sendCaptcha";
    //验证验证码
    public static String VERIFICATIONVERIFYCODE = HOST + "mobile/verifyCaptcha";
    //绑定的方法
    public static String BINDSTRING = HOST + "mobile/bind";
    //分销-要请与奖励
    public static String SCORERECORD = HOST + "pocket/scoreRecord";
    //分销-结算记录
    public static String WINDUPRECORD = HOST + "pocket/windupRecord";
    //分销-申请结算
    public static String BOOKWINDUP = HOST + "pocket/bookWindup";
    //分销-我的钱包
    public static String MYPOCKE = HOST + "pocket/myPocket";
    //DEMO中下载文件的缓存目录
    public static String DOWNLOADCACHE = "/democache";
    //下载课程前验证
    public static String DOWNLOAD_CHECK = HOST + "download/check/kpoint";
    //订单取消
    public static String CANCEL_ORDER = HOST + "order/cancelOrderState";

}
