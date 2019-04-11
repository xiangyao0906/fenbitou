package com.fenbitou.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体类中公共的实体
 */
public class EntityPublic implements Serializable {

    private int id;
    private String imagesUrl;
    private int courseId;
    private String title;
    private String keyWord;
    private int seriesNumber;
    private String color;
    private String previewUrl;
    private String keyType;
    private  boolean isSaleUser;
    private int count_4;
    private int count_1;
    private int count_3;
    private int count_2;
    private EntityPublic book;
    private int bookId;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public EntityPublic getBook() {
        return book;
    }

    public void setBook(EntityPublic book) {
        this.book = book;
    }

    private List<EntityPublic> courseLiveList;
    /**
     * thirdlogin : {"verifyWeiXin":"OFF","verifyQQ":"OFF","verifySINA":"OFF"}
     */

    private ThirdloginBean thirdlogin;

    public List<EntityPublic> getCourseLiveList() {
        return courseLiveList;
    }

    public void setCourseLiveList(List<EntityPublic> courseLiveList) {
        this.courseLiveList = courseLiveList;
    }


    public int getCount_4() {
        return count_4;
    }

    public void setCount_4(int count_4) {
        this.count_4 = count_4;
    }

    public int getCount_1() {
        return count_1;
    }

    public void setCount_1(int count_1) {
        this.count_1 = count_1;
    }

    public int getCount_3() {
        return count_3;
    }

    public void setCount_3(int count_3) {
        this.count_3 = count_3;
    }

    public int getCount_2() {
        return count_2;
    }

    public void setCount_2(int count_2) {
        this.count_2 = count_2;
    }

    public boolean isSaleUser() {
        return isSaleUser;
    }

    public void setSaleUser(boolean saleUser) {
        isSaleUser = saleUser;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setSeriesNumber(int seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public int getSeriesNumber() {
        return seriesNumber;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    private List<EntityPublic> indexCenterBanner;

    public void setIndexCenterBanner(List<EntityPublic> indexCenterBanner) {
        this.indexCenterBanner = indexCenterBanner;
    }

    public List<EntityPublic> getIndexCenterBanner() {
        return indexCenterBanner;
    }

    //课程
    private int kpointId;
    private int isPay;
    private int playCount;
    private int recommendId;
    private String mobileLogo;
    private int orderNum;
    private int pageViewcount;
    private double currentPrice;
    private int lessionnum;
    private String courseName;
    private int subjectId;
    private String name;
    private String subjectName;
    private PageEntity page;
    private List<EntityCourse> courseList;
    private List<EntityCourse> favouriteCourses;
    private List<EntityPublic> studyList;

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setRecommendId(int recommendId) {
        this.recommendId = recommendId;
    }

    public int getRecommendId() {
        return recommendId;
    }

    public void setMobileLogo(String mobileLogo) {
        this.mobileLogo = mobileLogo;
    }

    public String getMobileLogo() {
        return mobileLogo;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setPageViewcount(int pageViewcount) {
        this.pageViewcount = pageViewcount;
    }

    public int getPageViewcount() {
        return pageViewcount;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setLessionnum(int lessionnum) {
        this.lessionnum = lessionnum;
    }

    public int getLessionnum() {
        return lessionnum;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public PageEntity getPage() {
        return page;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    public List<EntityCourse> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<EntityCourse> courseList) {
        this.courseList = courseList;
    }

    public List<EntityCourse> getFavouriteCourses() {
        return favouriteCourses;
    }

    public void setFavouriteCourses(List<EntityCourse> favouriteCourses) {
        this.favouriteCourses = favouriteCourses;
    }

    public List<EntityPublic> getStudyList() {
        return studyList;
    }

    public void setStudyList(List<EntityPublic> studyList) {
        this.studyList = studyList;
    }

    public int getKpointId() {
        return kpointId;
    }

    public void setKpointId(int kpointId) {
        this.kpointId = kpointId;
    }

    //用户
    private String balance;
    private boolean notPayOrder;
    private EntityPublic userExpandDto;
    private int courseNum;
    private int courseStudyNum;

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public void setNotPayOrder(boolean notPayOrder) {
        this.notPayOrder = notPayOrder;
    }

    public boolean getNotPayOrder() {
        return notPayOrder;
    }

    public void setUserExpandDto(EntityPublic userExpandDto) {
        this.userExpandDto = userExpandDto;
    }

    public EntityPublic getUserExpandDto() {
        return userExpandDto;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseStudyNum(int courseStudyNum) {
        this.courseStudyNum = courseStudyNum;
    }

    public int getCourseStudyNum() {
        return courseStudyNum;
    }


    private String nickname;
    private String email;
    private int emailIsavalible;
    private String mobile;
    private int mobileIsavalible;
    private String password;
    private int isavalible;
    private String customerkey;
    private String createdate;
    private String userip;
    private String realname;
    private int gender;
    private String avatar;
    private String bannerUrl;
    private int studysubject;
    private int userType;
    private int weiBoNum;
    private int fansNum;
    private int attentionNum;
    private int msgNum;
    private int sysMsgNum;
    private String lastSystemTime;
    private String lastLoginTime;
    private int unreadFansNum;
    private int loginNum;
    private int studyNum;
    private int noteNum;
    private int assessNum;
    private int answerNum;
    private int examNum;
    private String showname;
    private String userInfo;
    private int cusId;
    private int commonFriendNum;
    private int friendId;
    private int mutual;
    private int cusNum;
    private int current;
    private String registerFrom;
    private int totalCount;
    private int endCount;

    public int getEndCount() {
        return endCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmailIsavalible(int emailIsavalible) {
        this.emailIsavalible = emailIsavalible;
    }

    public int getEmailIsavalible() {
        return emailIsavalible;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobileIsavalible(int mobileIsavalible) {
        this.mobileIsavalible = mobileIsavalible;
    }

    public int getMobileIsavalible() {
        return mobileIsavalible;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setIsavalible(int isavalible) {
        this.isavalible = isavalible;
    }

    public int getIsavalible() {
        return isavalible;
    }

    public void setCustomerkey(String customerkey) {
        this.customerkey = customerkey;
    }

    public String getCustomerkey() {
        return customerkey;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setUserip(String userip) {
        this.userip = userip;
    }

    public String getUserip() {
        return userip;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRealname() {
        return realname;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setStudysubject(int studysubject) {
        this.studysubject = studysubject;
    }

    public int getStudysubject() {
        return studysubject;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserType() {
        return userType;
    }

    public void setWeiBoNum(int weiBoNum) {
        this.weiBoNum = weiBoNum;
    }

    public int getWeiBoNum() {
        return weiBoNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public void setSysMsgNum(int sysMsgNum) {
        this.sysMsgNum = sysMsgNum;
    }

    public int getSysMsgNum() {
        return sysMsgNum;
    }

    public void setLastSystemTime(String lastSystemTime) {
        this.lastSystemTime = lastSystemTime;
    }

    public String getLastSystemTime() {
        return lastSystemTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setUnreadFansNum(int unreadFansNum) {
        this.unreadFansNum = unreadFansNum;
    }

    public int getUnreadFansNum() {
        return unreadFansNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setStudyNum(int studyNum) {
        this.studyNum = studyNum;
    }

    public int getStudyNum() {
        return studyNum;
    }

    public void setNoteNum(int noteNum) {
        this.noteNum = noteNum;
    }

    public int getNoteNum() {
        return noteNum;
    }

    public void setAssessNum(int assessNum) {
        this.assessNum = assessNum;
    }

    public int getAssessNum() {
        return assessNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setExamNum(int examNum) {
        this.examNum = examNum;
    }

    public int getExamNum() {
        return examNum;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getShowname() {
        return showname;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setCusId(int cusId) {
        this.cusId = cusId;
    }

    public int getCusId() {
        return cusId;
    }

    public void setCommonFriendNum(int commonFriendNum) {
        this.commonFriendNum = commonFriendNum;
    }

    public int getCommonFriendNum() {
        return commonFriendNum;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setMutual(int mutual) {
        this.mutual = mutual;
    }

    public int getMutual() {
        return mutual;
    }

    public void setCusNum(int cusNum) {
        this.cusNum = cusNum;
    }

    public int getCusNum() {
        return cusNum;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }

    public void setRegisterFrom(String registerFrom) {
        this.registerFrom = registerFrom;
    }

    public String getRegisterFrom() {
        return registerFrom;
    }


    //课程详情
    private String logo;
    private List<TeacherEntity> teacherList;
    private float sourceprice;
    private float currentprice;
    private int losetype;
    private String context;
    private int parentId;
    private int type;
    private int status;
    private String addTime;
    private int sort;
    private int playcount;
    private int playNum;
    private int isfree;
    private String videotype;
    private String videourl;
    private int teacherId;
    private int courseMinutes;
    private int courseSeconds;
    private String videojson;
    private String meta;
    private String description;
    private String picture;
    private String createTime;
    private String updateTime;
    private String author;
    private int clickTimes;
    private List<EntityPublic> childKpoints;
    private int level;
    private String currentPirce;
    private String courseImgUrl;
    private int currentCourseId;
    private String courseTitle;
    private String lessionNum;
    private String loseAbsTime;
    private String loseTime;
    private String shopName;
    private String shopImg;
    private int favouriteId;
    private String fileType;
    private int dataId;
    private String kpointName;
    // 音频
    private int audioId;
    private String audioName;
    private String summary;
    private String info;
    private String imgUrl;
    private int commentNum;
    private int lookNum;
    private int pageLookNum;
    private int buyNum;
    private int pageBuyNum;
    private int pagePlayNum;
    private int validityTime;
    private float price;
    private int nowPrice;
    private String teacherName;
    // 直播
    private String oneLiveName;
    private int isavaliable;
    private String addtime;
    private String coursetag;
    private String packageLogo;
    private String updateuser;
    private int pageBuycount;
    private String sellType;
    private String liveBeginTime;
    private String liveEndTime;
    private int couponId;
    private String examLink;
    private String courseYear;
    private int viewcount;
    private int count;
    private int OneCount;
    private int endOneCount;
    private int liveId;
    private String liveName;
    private String liveUrl;
    private String token;
    private String startTime;
    private String endTime;
    private int minute;
    private int second;
    private int liveType;
    private int livePlayStatu;
    private String courseImg;
    private float rebatePrice;

    public float getRebatePrice() {
        return rebatePrice;
    }

    public void setRebatePrice(float rebatePrice) {
        this.rebatePrice = rebatePrice;
    }
    private String bookName;
    private String bookImg;
    private String stockNum;
    private int shopState;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }

    public int getShopState() {
        return shopState;
    }

    public void setShopState(int shopState) {
        this.shopState = shopState;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<TeacherEntity> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherEntity> teacherList) {
        this.teacherList = teacherList;
    }

    public float getSourceprice() {
        return sourceprice;
    }

    public void setSourceprice(float sourceprice) {
        this.sourceprice = sourceprice;
    }

    public float getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(float currentprice) {
        this.currentprice = currentprice;
    }

    public int getLosetype() {
        return losetype;
    }

    public void setLosetype(int losetype) {
        this.losetype = losetype;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public int getIsfree() {
        return isfree;
    }

    public void setIsfree(int isfree) {
        this.isfree = isfree;
    }

    public String getVideotype() {
        return videotype;
    }

    public void setVideotype(String videotype) {
        this.videotype = videotype;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getCourseMinutes() {
        return courseMinutes;
    }

    public void setCourseMinutes(int courseMinutes) {
        this.courseMinutes = courseMinutes;
    }

    public int getCourseSeconds() {
        return courseSeconds;
    }

    public void setCourseSeconds(int courseSeconds) {
        this.courseSeconds = courseSeconds;
    }

    public String getVideojson() {
        return videojson;
    }

    public void setVideojson(String videojson) {
        this.videojson = videojson;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(int clickTimes) {
        this.clickTimes = clickTimes;
    }

    public List<EntityPublic> getChildKpoints() {
        return childKpoints;
    }

    public void setChildKpoints(List<EntityPublic> childKpoints) {
        this.childKpoints = childKpoints;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCurrentPirce() {
        return currentPirce;
    }

    public void setCurrentPirce(String currentPirce) {
        this.currentPirce = currentPirce;
    }

    public String getCourseImgUrl() {
        return courseImgUrl;
    }

    public void setCourseImgUrl(String courseImgUrl) {
        this.courseImgUrl = courseImgUrl;
    }

    public int getCurrentCourseId() {
        return currentCourseId;
    }

    public void setCurrentCourseId(int currentCourseId) {
        this.currentCourseId = currentCourseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getLessionNum() {
        return lessionNum;
    }

    public void setLessionNum(String lessionNum) {
        this.lessionNum = lessionNum;
    }

    public String getLoseAbsTime() {
        return loseAbsTime;
    }

    public void setLoseAbsTime(String loseAbsTime) {
        this.loseAbsTime = loseAbsTime;
    }

    public String getLoseTime() {
        return loseTime;
    }

    public void setLoseTime(String loseTime) {
        this.loseTime = loseTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public int getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(int favouriteId) {
        this.favouriteId = favouriteId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getKpointName() {
        return kpointName;
    }

    public void setKpointName(String kpointName) {
        this.kpointName = kpointName;
    }

    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLookNum() {
        return lookNum;
    }

    public void setLookNum(int lookNum) {
        this.lookNum = lookNum;
    }

    public int getPageLookNum() {
        return pageLookNum;
    }

    public void setPageLookNum(int pageLookNum) {
        this.pageLookNum = pageLookNum;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getPageBuyNum() {
        return pageBuyNum;
    }

    public void setPageBuyNum(int pageBuyNum) {
        this.pageBuyNum = pageBuyNum;
    }

    public int getPagePlayNum() {
        return pagePlayNum;
    }

    public void setPagePlayNum(int pagePlayNum) {
        this.pagePlayNum = pagePlayNum;
    }

    public int getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(int validityTime) {
        this.validityTime = validityTime;
    }

    public int getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(int nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getOneLiveName() {
        return oneLiveName;
    }

    public void setOneLiveName(String oneLiveName) {
        this.oneLiveName = oneLiveName;
    }

    public int getIsavaliable() {
        return isavaliable;
    }

    public void setIsavaliable(int isavaliable) {
        this.isavaliable = isavaliable;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getCoursetag() {
        return coursetag;
    }

    public void setCoursetag(String coursetag) {
        this.coursetag = coursetag;
    }

    public String getPackageLogo() {
        return packageLogo;
    }

    public void setPackageLogo(String packageLogo) {
        this.packageLogo = packageLogo;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

    public int getPageBuycount() {
        return pageBuycount;
    }

    public void setPageBuycount(int pageBuycount) {
        this.pageBuycount = pageBuycount;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public String getLiveBeginTime() {
        return liveBeginTime;
    }

    public void setLiveBeginTime(String liveBeginTime) {
        this.liveBeginTime = liveBeginTime;
    }

    public String getLiveEndTime() {
        return liveEndTime;
    }

    public void setLiveEndTime(String liveEndTime) {
        this.liveEndTime = liveEndTime;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getExamLink() {
        return examLink;
    }

    public void setExamLink(String examLink) {
        this.examLink = examLink;
    }

    public String getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public int getViewcount() {
        return viewcount;
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOneCount() {
        return OneCount;
    }

    public void setOneCount(int oneCount) {
        OneCount = oneCount;
    }

    public int getEndOneCount() {
        return endOneCount;
    }

    public void setEndOneCount(int endOneCount) {
        this.endOneCount = endOneCount;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }

    public int getLivePlayStatu() {
        return livePlayStatu;
    }

    public void setLivePlayStatu(int livePlayStatu) {
        this.livePlayStatu = livePlayStatu;
    }

    private String sumPrice;
    private String realPrice;
    private List<EntityPublic> letterList;
    // 课程列表
    private List<EntityPublic> subjectList;
    private EntityPublic teacher;
    // 课程详情
    private EntityCourse course;
    private int defaultKpointId;
    private List<EntityPublic> courseKpoints;
    private boolean isok;
    private boolean isFav;
    private List<EntityPublic> coursePackageList;
    private List<EntityCourse> articleList;
    // banner图
    // 支付宝信息
    private String sellerEmail;
    private String alipaykey;
    private String alipaypartnerID;
    private String publickey;
    private String privatekey;
    // 我的订单
    private List<EntityPublic> trxorderList;
    private List<OrderEntity> orderList;
    private String orderNo;
    private String out_trade_no;
    private String bankAmount;
    private String payType;
    private int orderId;
    private int userId;
    private String content;
    private String shortContent;
    // 我的账户
    private List<EntityAccList> accList;
    private EntityUserAccount userAccount;
    private List<EntityPublic> assessList;
    // 版本更新
    private String android_url;
    private String ios_v;
    private String ios_url;
    private String android_v;
    private EntityPublic user;
    // 优惠券
    public List<CouponEntity> couponList;
    // 验证
    private String videoUrl;
    private String videoType;
    private String message;
    private boolean success;
    private List<EntityPublic> entity;
    private String requestId;
    private int trxorderId;
    private String couponCode;
    private int limitAmount;
    private String amount;
    private int useType;
    private String optuserName;
    private String remindStatus;
    // 待支付订单
    private EntityPublic trxorder;
    private List<EntityPublic> detailList;
    private String orderAmount;
    private String courseLogo;
    private EntityPublic couponCodeDTO;
    private String verifyRegEmailCode;
    private String verifyRegMobileCode;
    // 第三方
    private String profiletype;
    private String value;
    private int userid;
    private String profiledate;
    // 微信信息
    private String mobilePayKey;
    private String mobileAppId;
    private String mobileMchId;
    // 视频类型
    private EntityPublic CC;
    private EntityPublic POLYV;
    private EntityPublic P56;
    private EntityPublic LETV;
    // CC视频
    private String ccappID;
    private String ccappKEY;
    // 保利视频
    private String plUserId;
    private String appSdk;
    private String readtoken;
    private String secretkey;
    private String writetoken;
    // 登录
    private String memTime;
    private String downloadUrl;
    private String kType;
    private String versionNo;
    private String depict;
    // group
    private int groupNo;
    private int topicNo;
    private List<EntityPublic> topicList;
    private ArrayList<String> htmlImagesList;
    private List<EntityPublic> groupMembers;
    private int browseCounts;
    private int praiseCounts;
    private int commentCounts;
    private String htmlImages;
    private String imageUrl;
    private EntityPublic topic;
    private List<EntityPublic> commentDtoList;
    private String commentContent;
    private int praiseNumber;
    private List<EntityPublic> groupList;
    private String introduction;
    private int memberNum;
    private int topicCounts;
    private List<EntityPublic> topics;
    private int groupId;
    private String groupName;
    private String nickName;
    private EntityPublic groupCreator;
    private String showName;
    private EntityPublic group;
    private List<EntityPublic> allTopicList;
    private List<EntityPublic> smallGroupLeader;
    private EntityPublic groupLeader;
    private List<EntityPublic> joinGroupList;
    private List<EntityPublic> manageGroupList;
    private List<EntityPublic> hotGroupList;
    private int jobType;
    private int ifAudit;
    private int top;
    private int essence;
    private int fiery;
    private int whetherTheMembers;
    private boolean haveGroup;
    // 咨询
    private List<EntityPublic> articleTypeList;
    // 音频
    private List<EntityPublic> audioConditionList;
    private String Summary;
    private EntityPublic audioCondition;
    private boolean isOk;
    private List<EntityPublic> audioNodeCommentList;
    private List<EntityPublic> allNodeList;
    private String nodeName;
    private String playUrl;
    private int nodeId;
    private List<EntityPublic> parentList;
    private List<EntityPublic> audioNodeList;
    private Boolean isCanPlay;
    private List<EntityPublic> audioRecommend;
    // 后台控制开关
    private String verifyExam;
    private String verifyCourseLive;
    private String verifyLogin;
    private String verifyApp;
    private String verifyTranspond;
    private String verifyLimitLogin;
    private String verifySensitive;
    private String verifyCourse;
    private String verifyGro;
    private String yee;
    private EntityPublic verifyShare;
    private String share;
    private String verifyH5;
    private String verifyTeacherMien;
    private String verifyPractice;
    private String verifyRegister;
    private String verifyAlipay;
    private String verifyEmail;
    private String verifykq;
    private String verifywx;
    private String verifyTeacherArticle;
    private String verifySns;
    private String visitorsToSeeTheCourse;
    private String verifyPhone;
    private String verifyCourseDiscuss;
    private String nodeSize;
    private Boolean haveAudio;
    private Boolean haveVideo;
    //直播
    private List<EntityPublic> listCourseWeekLiveSum;
    //我的直播
    private List<EntityCourse> myLive;
    private List<EntityPublic> scoreRecordList;
    private String score;
    private String userName;
    private int scoreType;
    private String currentScore;
    private String windupScore;
    private List<EntityPublic> saleUserBalance;
    private String money;
    private String stage;
    private EntityPublic saleUser;
    private String historyScore;
    private int browseSettings;
    private boolean yes;

    public String getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public List<EntityPublic> getLetterList() {
        return letterList;
    }

    public void setLetterList(List<EntityPublic> letterList) {
        this.letterList = letterList;
    }

    public List<EntityPublic> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<EntityPublic> subjectList) {
        this.subjectList = subjectList;
    }

    public EntityPublic getTeacher() {
        return teacher;
    }

    public void setTeacher(EntityPublic teacher) {
        this.teacher = teacher;
    }

    public EntityCourse getCourse() {
        return course;
    }

    public void setCourse(EntityCourse course) {
        this.course = course;
    }

    public int getDefaultKpointId() {
        return defaultKpointId;
    }

    public void setDefaultKpointId(int defaultKpointId) {
        this.defaultKpointId = defaultKpointId;
    }

    public List<EntityPublic> getCourseKpoints() {
        return courseKpoints;
    }

    public void setCourseKpoints(List<EntityPublic> courseKpoints) {
        this.courseKpoints = courseKpoints;
    }

    public boolean isok() {
        return isok;
    }

    public void setIsok(boolean isok) {
        this.isok = isok;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public List<EntityPublic> getCoursePackageList() {
        return coursePackageList;
    }

    public void setCoursePackageList(List<EntityPublic> coursePackageList) {
        this.coursePackageList = coursePackageList;
    }

    public List<EntityCourse> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<EntityCourse> articleList) {
        this.articleList = articleList;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getAlipaykey() {
        return alipaykey;
    }

    public void setAlipaykey(String alipaykey) {
        this.alipaykey = alipaykey;
    }

    public String getAlipaypartnerID() {
        return alipaypartnerID;
    }

    public void setAlipaypartnerID(String alipaypartnerID) {
        this.alipaypartnerID = alipaypartnerID;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public List<EntityPublic> getTrxorderList() {
        return trxorderList;
    }

    public void setTrxorderList(List<EntityPublic> trxorderList) {
        this.trxorderList = trxorderList;
    }

    public List<OrderEntity> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderEntity> orderList) {
        this.orderList = orderList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(String bankAmount) {
        this.bankAmount = bankAmount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public List<EntityAccList> getAccList() {
        return accList;
    }

    public void setAccList(List<EntityAccList> accList) {
        this.accList = accList;
    }

    public EntityUserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(EntityUserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public List<EntityPublic> getAssessList() {
        return assessList;
    }

    public void setAssessList(List<EntityPublic> assessList) {
        this.assessList = assessList;
    }

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }

    public String getIos_v() {
        return ios_v;
    }

    public void setIos_v(String ios_v) {
        this.ios_v = ios_v;
    }

    public String getIos_url() {
        return ios_url;
    }

    public void setIos_url(String ios_url) {
        this.ios_url = ios_url;
    }

    public String getAndroid_v() {
        return android_v;
    }

    public void setAndroid_v(String android_v) {
        this.android_v = android_v;
    }

    public EntityPublic getUser() {
        return user;
    }

    public void setUser(EntityPublic user) {
        this.user = user;
    }

    public List<CouponEntity> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponEntity> couponList) {
        this.couponList = couponList;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<EntityPublic> getEntity() {
        return entity;
    }

    public void setEntity(List<EntityPublic> entity) {
        this.entity = entity;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getTrxorderId() {
        return trxorderId;
    }

    public void setTrxorderId(int trxorderId) {
        this.trxorderId = trxorderId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public String getOptuserName() {
        return optuserName;
    }

    public void setOptuserName(String optuserName) {
        this.optuserName = optuserName;
    }

    public String getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(String remindStatus) {
        this.remindStatus = remindStatus;
    }

    public EntityPublic getTrxorder() {
        return trxorder;
    }

    public void setTrxorder(EntityPublic trxorder) {
        this.trxorder = trxorder;
    }

    public List<EntityPublic> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<EntityPublic> detailList) {
        this.detailList = detailList;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCourseLogo() {
        return courseLogo;
    }

    public void setCourseLogo(String courseLogo) {
        this.courseLogo = courseLogo;
    }

    public EntityPublic getCouponCodeDTO() {
        return couponCodeDTO;
    }

    public void setCouponCodeDTO(EntityPublic couponCodeDTO) {
        this.couponCodeDTO = couponCodeDTO;
    }

    public String getVerifyRegEmailCode() {
        return verifyRegEmailCode;
    }

    public void setVerifyRegEmailCode(String verifyRegEmailCode) {
        this.verifyRegEmailCode = verifyRegEmailCode;
    }

    public String getVerifyRegMobileCode() {
        return verifyRegMobileCode;
    }

    public void setVerifyRegMobileCode(String verifyRegMobileCode) {
        this.verifyRegMobileCode = verifyRegMobileCode;
    }

    public String getProfiletype() {
        return profiletype;
    }

    public void setProfiletype(String profiletype) {
        this.profiletype = profiletype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getProfiledate() {
        return profiledate;
    }

    public void setProfiledate(String profiledate) {
        this.profiledate = profiledate;
    }

    public String getMobilePayKey() {
        return mobilePayKey;
    }

    public void setMobilePayKey(String mobilePayKey) {
        this.mobilePayKey = mobilePayKey;
    }

    public String getMobileAppId() {
        return mobileAppId;
    }

    public void setMobileAppId(String mobileAppId) {
        this.mobileAppId = mobileAppId;
    }

    public String getMobileMchId() {
        return mobileMchId;
    }

    public void setMobileMchId(String mobileMchId) {
        this.mobileMchId = mobileMchId;
    }

    public EntityPublic getCC() {
        return CC;
    }

    public void setCC(EntityPublic CC) {
        this.CC = CC;
    }

    public EntityPublic getPOLYV() {
        return POLYV;
    }

    public void setPOLYV(EntityPublic POLYV) {
        this.POLYV = POLYV;
    }

    public EntityPublic getP56() {
        return P56;
    }

    public void setP56(EntityPublic p56) {
        P56 = p56;
    }

    public EntityPublic getLETV() {
        return LETV;
    }

    public void setLETV(EntityPublic LETV) {
        this.LETV = LETV;
    }

    public String getCcappID() {
        return ccappID;
    }

    public void setCcappID(String ccappID) {
        this.ccappID = ccappID;
    }

    public String getCcappKEY() {
        return ccappKEY;
    }

    public void setCcappKEY(String ccappKEY) {
        this.ccappKEY = ccappKEY;
    }

    public String getPlUserId() {
        return plUserId;
    }

    public void setPlUserId(String plUserId) {
        this.plUserId = plUserId;
    }

    public String getAppSdk() {
        return appSdk;
    }

    public void setAppSdk(String appSdk) {
        this.appSdk = appSdk;
    }

    public String getReadtoken() {
        return readtoken;
    }

    public void setReadtoken(String readtoken) {
        this.readtoken = readtoken;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getWritetoken() {
        return writetoken;
    }

    public void setWritetoken(String writetoken) {
        this.writetoken = writetoken;
    }

    public String getMemTime() {
        return memTime;
    }

    public void setMemTime(String memTime) {
        this.memTime = memTime;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getkType() {
        return kType;
    }

    public void setkType(String kType) {
        this.kType = kType;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public int getTopicNo() {
        return topicNo;
    }

    public void setTopicNo(int topicNo) {
        this.topicNo = topicNo;
    }

    public List<EntityPublic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<EntityPublic> topicList) {
        this.topicList = topicList;
    }

    public ArrayList<String> getHtmlImagesList() {
        return htmlImagesList;
    }

    public void setHtmlImagesList(ArrayList<String> htmlImagesList) {
        this.htmlImagesList = htmlImagesList;
    }

    public List<EntityPublic> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<EntityPublic> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public int getBrowseCounts() {
        return browseCounts;
    }

    public void setBrowseCounts(int browseCounts) {
        this.browseCounts = browseCounts;
    }

    public int getPraiseCounts() {
        return praiseCounts;
    }

    public void setPraiseCounts(int praiseCounts) {
        this.praiseCounts = praiseCounts;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }

    public String getHtmlImages() {
        return htmlImages;
    }

    public void setHtmlImages(String htmlImages) {
        this.htmlImages = htmlImages;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public EntityPublic getTopic() {
        return topic;
    }

    public void setTopic(EntityPublic topic) {
        this.topic = topic;
    }

    public List<EntityPublic> getCommentDtoList() {
        return commentDtoList;
    }

    public void setCommentDtoList(List<EntityPublic> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(int praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public List<EntityPublic> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<EntityPublic> groupList) {
        this.groupList = groupList;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public int getTopicCounts() {
        return topicCounts;
    }

    public void setTopicCounts(int topicCounts) {
        this.topicCounts = topicCounts;
    }

    public List<EntityPublic> getTopics() {
        return topics;
    }

    public void setTopics(List<EntityPublic> topics) {
        this.topics = topics;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public EntityPublic getGroupCreator() {
        return groupCreator;
    }

    public void setGroupCreator(EntityPublic groupCreator) {
        this.groupCreator = groupCreator;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public EntityPublic getGroup() {
        return group;
    }

    public void setGroup(EntityPublic group) {
        this.group = group;
    }

    public List<EntityPublic> getAllTopicList() {
        return allTopicList;
    }

    public void setAllTopicList(List<EntityPublic> allTopicList) {
        this.allTopicList = allTopicList;
    }

    public List<EntityPublic> getSmallGroupLeader() {
        return smallGroupLeader;
    }

    public void setSmallGroupLeader(List<EntityPublic> smallGroupLeader) {
        this.smallGroupLeader = smallGroupLeader;
    }

    public EntityPublic getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(EntityPublic groupLeader) {
        this.groupLeader = groupLeader;
    }

    public List<EntityPublic> getJoinGroupList() {
        return joinGroupList;
    }

    public void setJoinGroupList(List<EntityPublic> joinGroupList) {
        this.joinGroupList = joinGroupList;
    }

    public List<EntityPublic> getManageGroupList() {
        return manageGroupList;
    }

    public void setManageGroupList(List<EntityPublic> manageGroupList) {
        this.manageGroupList = manageGroupList;
    }

    public List<EntityPublic> getHotGroupList() {
        return hotGroupList;
    }

    public void setHotGroupList(List<EntityPublic> hotGroupList) {
        this.hotGroupList = hotGroupList;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public int getIfAudit() {
        return ifAudit;
    }

    public void setIfAudit(int ifAudit) {
        this.ifAudit = ifAudit;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getEssence() {
        return essence;
    }

    public void setEssence(int essence) {
        this.essence = essence;
    }

    public int getFiery() {
        return fiery;
    }

    public void setFiery(int fiery) {
        this.fiery = fiery;
    }

    public int getWhetherTheMembers() {
        return whetherTheMembers;
    }

    public void setWhetherTheMembers(int whetherTheMembers) {
        this.whetherTheMembers = whetherTheMembers;
    }

    public boolean isHaveGroup() {
        return haveGroup;
    }

    public void setHaveGroup(boolean haveGroup) {
        this.haveGroup = haveGroup;
    }

    public List<EntityPublic> getArticleTypeList() {
        return articleTypeList;
    }

    public void setArticleTypeList(List<EntityPublic> articleTypeList) {
        this.articleTypeList = articleTypeList;
    }

    public List<EntityPublic> getAudioConditionList() {
        return audioConditionList;
    }

    public void setAudioConditionList(List<EntityPublic> audioConditionList) {
        this.audioConditionList = audioConditionList;
    }

    public EntityPublic getAudioCondition() {
        return audioCondition;
    }

    public void setAudioCondition(EntityPublic audioCondition) {
        this.audioCondition = audioCondition;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public List<EntityPublic> getAudioNodeCommentList() {
        return audioNodeCommentList;
    }

    public void setAudioNodeCommentList(List<EntityPublic> audioNodeCommentList) {
        this.audioNodeCommentList = audioNodeCommentList;
    }

    public List<EntityPublic> getAllNodeList() {
        return allNodeList;
    }

    public void setAllNodeList(List<EntityPublic> allNodeList) {
        this.allNodeList = allNodeList;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public List<EntityPublic> getParentList() {
        return parentList;
    }

    public void setParentList(List<EntityPublic> parentList) {
        this.parentList = parentList;
    }

    public List<EntityPublic> getAudioNodeList() {
        return audioNodeList;
    }

    public void setAudioNodeList(List<EntityPublic> audioNodeList) {
        this.audioNodeList = audioNodeList;
    }

    public Boolean getCanPlay() {
        return isCanPlay;
    }

    public void setCanPlay(Boolean canPlay) {
        isCanPlay = canPlay;
    }

    public List<EntityPublic> getAudioRecommend() {
        return audioRecommend;
    }

    public void setAudioRecommend(List<EntityPublic> audioRecommend) {
        this.audioRecommend = audioRecommend;
    }

    public String getVerifyExam() {
        return verifyExam;
    }

    public void setVerifyExam(String verifyExam) {
        this.verifyExam = verifyExam;
    }

    public String getVerifyCourseLive() {
        return verifyCourseLive;
    }

    public void setVerifyCourseLive(String verifyCourseLive) {
        this.verifyCourseLive = verifyCourseLive;
    }

    public String getVerifyLogin() {
        return verifyLogin;
    }

    public void setVerifyLogin(String verifyLogin) {
        this.verifyLogin = verifyLogin;
    }

    public String getVerifyApp() {
        return verifyApp;
    }

    public void setVerifyApp(String verifyApp) {
        this.verifyApp = verifyApp;
    }

    public String getVerifyTranspond() {
        return verifyTranspond;
    }

    public void setVerifyTranspond(String verifyTranspond) {
        this.verifyTranspond = verifyTranspond;
    }

    public String getVerifyLimitLogin() {
        return verifyLimitLogin;
    }

    public void setVerifyLimitLogin(String verifyLimitLogin) {
        this.verifyLimitLogin = verifyLimitLogin;
    }

    public String getVerifySensitive() {
        return verifySensitive;
    }

    public void setVerifySensitive(String verifySensitive) {
        this.verifySensitive = verifySensitive;
    }

    public String getVerifyCourse() {
        return verifyCourse;
    }

    public void setVerifyCourse(String verifyCourse) {
        this.verifyCourse = verifyCourse;
    }

    public String getVerifyGro() {
        return verifyGro;
    }

    public void setVerifyGro(String verifyGro) {
        this.verifyGro = verifyGro;
    }

    public String getYee() {
        return yee;
    }

    public void setYee(String yee) {
        this.yee = yee;
    }

    public EntityPublic getVerifyShare() {
        return verifyShare;
    }

    public void setVerifyShare(EntityPublic verifyShare) {
        this.verifyShare = verifyShare;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getVerifyH5() {
        return verifyH5;
    }

    public void setVerifyH5(String verifyH5) {
        this.verifyH5 = verifyH5;
    }

    public String getVerifyTeacherMien() {
        return verifyTeacherMien;
    }

    public void setVerifyTeacherMien(String verifyTeacherMien) {
        this.verifyTeacherMien = verifyTeacherMien;
    }

    public String getVerifyPractice() {
        return verifyPractice;
    }

    public void setVerifyPractice(String verifyPractice) {
        this.verifyPractice = verifyPractice;
    }

    public String getVerifyRegister() {
        return verifyRegister;
    }

    public void setVerifyRegister(String verifyRegister) {
        this.verifyRegister = verifyRegister;
    }

    public String getVerifyAlipay() {
        return verifyAlipay;
    }

    public void setVerifyAlipay(String verifyAlipay) {
        this.verifyAlipay = verifyAlipay;
    }

    public String getVerifyEmail() {
        return verifyEmail;
    }

    public void setVerifyEmail(String verifyEmail) {
        this.verifyEmail = verifyEmail;
    }

    public String getVerifykq() {
        return verifykq;
    }

    public void setVerifykq(String verifykq) {
        this.verifykq = verifykq;
    }

    public String getVerifywx() {
        return verifywx;
    }

    public void setVerifywx(String verifywx) {
        this.verifywx = verifywx;
    }

    public String getVerifyTeacherArticle() {
        return verifyTeacherArticle;
    }

    public void setVerifyTeacherArticle(String verifyTeacherArticle) {
        this.verifyTeacherArticle = verifyTeacherArticle;
    }

    public String getVerifySns() {
        return verifySns;
    }

    public void setVerifySns(String verifySns) {
        this.verifySns = verifySns;
    }

    public String getVisitorsToSeeTheCourse() {
        return visitorsToSeeTheCourse;
    }

    public void setVisitorsToSeeTheCourse(String visitorsToSeeTheCourse) {
        this.visitorsToSeeTheCourse = visitorsToSeeTheCourse;
    }

    public String getVerifyPhone() {
        return verifyPhone;
    }

    public void setVerifyPhone(String verifyPhone) {
        this.verifyPhone = verifyPhone;
    }

    public String getVerifyCourseDiscuss() {
        return verifyCourseDiscuss;
    }

    public void setVerifyCourseDiscuss(String verifyCourseDiscuss) {
        this.verifyCourseDiscuss = verifyCourseDiscuss;
    }

    public String getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(String nodeSize) {
        this.nodeSize = nodeSize;
    }

    public Boolean getHaveAudio() {
        return haveAudio;
    }

    public void setHaveAudio(Boolean haveAudio) {
        this.haveAudio = haveAudio;
    }

    public Boolean getHaveVideo() {
        return haveVideo;
    }

    public void setHaveVideo(Boolean haveVideo) {
        this.haveVideo = haveVideo;
    }

    public List<EntityPublic> getListCourseWeekLiveSum() {
        return listCourseWeekLiveSum;
    }

    public void setListCourseWeekLiveSum(List<EntityPublic> listCourseWeekLiveSum) {
        this.listCourseWeekLiveSum = listCourseWeekLiveSum;
    }

    public List<EntityCourse> getMyLive() {
        return myLive;
    }

    public void setMyLive(List<EntityCourse> myLive) {
        this.myLive = myLive;
    }

    public List<EntityPublic> getScoreRecordList() {
        return scoreRecordList;
    }

    public void setScoreRecordList(List<EntityPublic> scoreRecordList) {
        this.scoreRecordList = scoreRecordList;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScoreType() {
        return scoreType;
    }

    public void setScoreType(int scoreType) {
        this.scoreType = scoreType;
    }

    public String getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(String currentScore) {
        this.currentScore = currentScore;
    }

    public String getWindupScore() {
        return windupScore;
    }

    public void setWindupScore(String windupScore) {
        this.windupScore = windupScore;
    }

    public List<EntityPublic> getSaleUserBalance() {
        return saleUserBalance;
    }

    public void setSaleUserBalance(List<EntityPublic> saleUserBalance) {
        this.saleUserBalance = saleUserBalance;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public EntityPublic getSaleUser() {
        return saleUser;
    }

    public void setSaleUser(EntityPublic saleUser) {
        this.saleUser = saleUser;
    }

    public String getHistoryScore() {
        return historyScore;
    }

    public void setHistoryScore(String historyScore) {
        this.historyScore = historyScore;
    }

    public int getBrowseSettings() {
        return browseSettings;
    }

    public void setBrowseSettings(int browseSettings) {
        this.browseSettings = browseSettings;
    }

    public boolean isYes() {
        return yes;
    }

    public void setYes(boolean yes) {
        this.yes = yes;
    }

    private String education;
    private String career;
    private int isStar;
    private String picPath;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public ThirdloginBean getThirdlogin() {
        return thirdlogin;
    }

    public void setThirdlogin(ThirdloginBean thirdlogin) {
        this.thirdlogin = thirdlogin;
    }

    public static class ThirdloginBean {
        /**
         * verifyWeiXin : OFF
         * verifyQQ : OFF
         * verifySINA : OFF
         */

        private String verifyWeiXin;
        private String verifyQQ;
        private String verifySINA;

        public String getVerifyWeiXin() {
            return verifyWeiXin;
        }

        public void setVerifyWeiXin(String verifyWeiXin) {
            this.verifyWeiXin = verifyWeiXin;
        }

        public String getVerifyQQ() {
            return verifyQQ;
        }

        public void setVerifyQQ(String verifyQQ) {
            this.verifyQQ = verifyQQ;
        }

        public String getVerifySINA() {
            return verifySINA;
        }

        public void setVerifySINA(String verifySINA) {
            this.verifySINA = verifySINA;
        }
    }
}