package com.yizhilu.community.utils;

public class Address {
	// 社区-主域名
	public static String SNSHOST = "http://sns.268xue.com";
	// 社区-图片
	public static String IMAGE = "http://static.268xue.com";
	// 社区-我的
	public static String SNSMY = SNSHOST + "/app/user/userInfo.json";
	// 社区-话题点赞
	public static String TOPICPRAISE = SNSHOST + "/app/topic/goToTopicPraise.json";
	// 社区-话题详情
	public static String TOPICINFO = SNSHOST + "/app/topic/topicInfo.json";
	// 社区-话题评论
	public static String TOPICCOMMENTLIST = SNSHOST + "/app/comment/topicCommentList.json";
	// 社区-发现
	public static String FINDLIST = SNSHOST + "/app/group/groupList.json";
	// 社区-热点
	public static String HOTTOPICLIST = SNSHOST + "/app/topic/getHotTopic.json";
	// 社区-小组详情
	public static String GROUPINFO = SNSHOST + "/app/group/groupInfo.json";
	// 社区-小组详情的话题列表
	public static String GROUPTOPICLIST = SNSHOST + "/app/group/getGroupTopic.json";
	// 社区-小组成员
	public static String ALLGROUPMEMBER = SNSHOST + "/app/group/allGroupMember.json";
	// 社区-选择话题类型
	public static String TOPICTYPE = SNSHOST + "/app/topic/toAddTopic.json";
	// 社区-发表话题
	public static String ADDTOPIC = SNSHOST + "/app/topic/addTopic.json";
	// 社区-我的话题
	public static String MYTOPICLIST = SNSHOST + "/app/user/myTopicList.json";
	// 社区-我的话题
	public static String MYGROUP = SNSHOST + "/app/user/myGroup.json";
	// 社区-发表话题前验证
	public static String CHECKIFCANADDTOPIC = SNSHOST + "/app/group/checkIfCanAddTopic.json";
	// 社区-加入小组
	public static String JOINGROUP = SNSHOST + "/app/group/joinGroup.json";
	// 社区-退出小组
	public static String EXITGROUP = SNSHOST + "/app/group/exitGroup.json";
	// 社区-添加评论
	public static String CREATECOMMENT = SNSHOST + "/app/comment/createComment.json";
	// 社区-评论点赞
	public static String COMMENTPRAISE = SNSHOST + "/app/comment/commentPraise.json";
	// 社区-热门小组
	public static String HOTGROUP = SNSHOST + "/app/group/hotGroupForIndex.json";
	// 社区-是否已加入小组
	public static String JOINGROUPED = SNSHOST + "/app/group/whetherJoinGroupByUserId.json";
	//社区-话题|小组搜索
	public static String SEARCH_GROUP_TOPI = SNSHOST + "/app/searchGroupTopic.json";
}
