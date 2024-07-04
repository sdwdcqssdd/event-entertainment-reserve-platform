# 校园活动与娱乐中心系统API

## 概述

​		校园活动与娱乐中心系统提供了一系列接口，为用户提供注册，登录，预约活动等一系列功能，为超级用户提供了审核，删除等权限。这些接口通过HTTP协议访问并且返回json数据。

## 接口列表

### UsersController类

1.注册用户

		URL：“/signup”
		方法：POST
		描述：注册新用户
		参数：
			“username”(String):用户名
			“email”(String):邮箱
			“password”(String):密码
		返回：
			注册成功返回生成的用户token
			注册失败返回错误信息


2.用户登录

		URL：“/signin”
		方法：POST
		描述：注册时验证邮箱
		参数：
			“username”(String):用户名
			“password”(String):密码
		返回：
			登录成功返回用户token
			登录失败返回错误信息     

3.获取用户信息

		URL：“/user_info”
		方法：GET
		描述：获取登录的用户信息
		参数：
			“token”(String):用户token
		返回：
			token有效返回用户信息
			token无效返回状态码401    

4.通过id获取用户信息

	    URL：“/user_info_byId”
	    方法：GET
	    描述：使用id获取用户信息
	    参数：
	        “userId”(int):用户id
	    返回：
	        用户存在返回用户信息
	        不存在返回状态码404    

5.关注用户

        URL：“/follow”
        方法：POST
        描述：关注用户
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "followeeId"(int):被关注用户id
        返回：
            如果token无效，返回状态码401
            关注成功返回成功信息   

6.取消关注

        URL：“/unfollow”
        方法：POST
        描述：取消关注
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "followeeId"(int):被关注用户id
        返回：
            如果token无效，返回状态码401
            取消关注成功返回成功信息   

7.粉丝列表

		URL：“/followers”
	    方法：GET
	    描述：查看关注列表
	    参数：
	        "token"(String):用户token(从HTTP请求头中获取"Authorization")
	    返回：
	        如果token无效，返回状态码401
	        token有效返回粉丝列表

8.关注列表

		URL：“/followees”
	    方法：GET
	    描述：查看关注列表
	    参数：
	        "token"(String):用户token(从HTTP请求头中获取"Authorization")
	    返回：
	        如果token无效，返回状态码401
	        token有效返回关注列表

9.是否正在关注指定的用户

		URL：“/is_following”
	    方法：GET
	    描述：查看是否关注指定用户
	    参数：
	        "token"(String):用户token(从HTTP请求头中获取"Authorization")
			"user_id"(int):指定的用户id
	    返回：
	        如果token无效，返回状态码401
	        token有效返回是否关注

### AvatarController类

10.获取头像

		URL：“/avatars”
	    方法：GET
	    描述：获取用户头像
	    参数：
	        "token"(String):用户token(从HTTP请求头中获取"Authorization")
			
	    返回：
	        如果token无效，返回状态码401
	        token有效返回审核通过的头像，没有则返回默认头像

11.上传头像

		URL：“/avatars”
	    方法：POST
	    描述：上传头像
	    参数：
	        "token"(String):用户token(从HTTP请求头中获取"Authorization")
			"requestBody"(Map<String, String>):请求体
	    返回：
	        如果token无效，返回状态码401
	        token有效如果上传失败返回错误信息，上传成功返回成功信息

12.超级用户获取待审核头像

		URL：“/avatars/pending”
	    方法：POST
	    描述：超级用户获取待审核头像列表
	    参数：
	        "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
		
	    返回：
	        如果token无效，返回状态码401
	        token有效返回待审核头像列表

13.超级用户批准头像

		URL：“/avatars/approve”
	    方法：POST
	    描述：超级用户批准审核头像
	    参数：
	        "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
			"avatarId"(int):头像id
	    返回：
	        如果token无效，返回状态码401
	        token有效，头像不存在返回错误信息，批准成功返回成功信息

14.超级用户拒绝头像

	    URL：“/avatars/reject”
	    方法：POST
	    描述：超级用户拒绝审核头像
	    参数：
	        "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
	        "avatarId"(int):头像id
	    返回：
	        如果token无效，返回状态码401
	        token有效，头像不存在返回错误信息，拒绝成功返回成功信息

### ChatController类

15.发送消息

        URL：“/chat.sendMessage”
        方法：MessageMapping
        描述：处理客户端发送的聊天消息
        参数：
            "chatMessage"(ChatMessage):聊天消息
        返回：
            无返回，发送消息目的地为"/queue/messages"

16.加入用户

        URL：“/chat.addUser”
        方法：MessageMapping
        描述：加入用户
        参数：
            "chatMessage"(ChatMessage):加入消息
        返回：
            将加入消息转发给"/topic/public"

17.发送私人消息

        URL：“/chat.private.{receiver}”
        方法：MessageMapping
        描述：发送私人消息
        参数：
            "chatMessage"(ChatMessage):聊天消息
            "receiver"(String):接收者(获取`{receiver}`的值)
        返回：
            无返回，发送消息给指定用户，目的地为"/queue/private-messages"

### EventController类

18.获取当前所有活动

        URL：“/events”
        方法：GET
        描述：获取当前所有活动的列表
        参数：
            无
        返回：
             当前所有活动的列表

19.获取指定ID活动

        URL：“/events/{event_id}”
        方法：GET
        描述：获取指定ID活动
        参数：
            "eventId"(int):路径参数，指定活动id
        返回：
             如果活动不存在返回状态码404，存在返回活动

20.推荐活动

        URL：“/recommend”
        方法：GET
        描述：根据用户的活动预约为用户个性化推荐活动
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
        返回：
             token无效返回状态码401
    		 有效返回个性化推荐活动列表

21.超级用户获取待审核活动

        URL：“/superuser/events”
        方法：GET
        描述：超级用户获取待审核活动
        参数：
            "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
        返回：
             token无效返回状态码401
             有效返回待审核活动列表

22.超级用户审核活动

        URL：“/superuser/events/approve”
        方法：POST
        描述：超级用户审核活动
        参数：
            "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
        返回：
             token无效返回状态码401
             有效返回审核成功信息

23.超级用户拒绝活动

        URL：“/superuser/events/reject”
        方法：POST
        描述：超级用户拒绝活动
        参数：
            "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
        返回：
             token无效返回状态码401
             有效返回拒绝成功信息

24.超级用户或者活动组织者获取待审核活动

        URL：“/Appoint”
        方法：POST
        描述：超级用户拒绝活动
        参数：
            "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
    		"eventId"(int):活动id
        返回：
             token无效返回状态码401
             有效返回

25.超级用户或者活动组织者批准预约

        URL：“/Appoint/approve”
        方法：POST
        描述：超级用户或者活动组织者批准预约
        参数：
            "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
            "appointId"(int):批准活动id
        返回：
             token无效返回状态码401
             有效返回批准成功信息

26.超级用户或者活动组织者拒绝预约

        URL：“/Appoint/reject”
        方法：POST
        描述：超级用户或者活动组织者拒绝预约
        参数：
            "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
            "appointId"(int):拒绝活动id
        返回：
             token无效返回状态码401
             有效返回拒绝成功信息

27.返回指定日期的活动

        URL：“/events/by-date”
        方法：GET
        描述：返回指定日期的活动
        参数：
            "date"(String):活动日期
        返回：
             指定日期的活动

28.创建活动

        URL：“/events”
        方法：POST
        描述：创建活动
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "event"(Event):活动(请求体)
        返回：
             token无效返回状态码401
             有效创建成功返回成功信息，失败返回失败信息

29.预约活动

        URL：“/events/appoint”
        方法：POST
        描述：预约活动
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "eventId"(int):活动id
        返回：
             返回预约成功信息

30.登录用户获取自己的历史活动

        URL：“/userAppoint”
        方法：POST
        描述：登录用户获取自己的历史活动
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
        返回：
             token无效返回状态码401
             有效返回历史活动列表

### ForumController类

31.创建论坛主题

        URL：“/forum/topic”
        方法：POST
        描述：创建论坛主题
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "forumTopic"(ForumTopic):论坛主题
        返回：
             token无效返回状态码401
             有效返回创建结果

32.删除论坛主题

        URL：“/forum/topic/delete/{topicId}”
        方法：DELETE
        描述：删除论坛主题
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "topicId"(int):论坛主题id(路径参数)
        返回：
             token无效返回状态码401
             有效返回删除结果

33.收藏或取消收藏主题

        URL：“/forum/topic/{topicId}/star”
        方法：POST
        描述：收藏或取消收藏主题
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "topicId"(int):论坛主题id(路径参数)
        返回：
             token无效返回状态码401
             有效如果已经收藏返回取消收藏，未收藏返回成功收藏

34.创建论坛回复

        URL：“/forum/reply”
        方法：POST
        描述：创建论坛回复
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "content"(String):回复内容
            "topicId"(int):主题id
            "parentId"(int):一级回复id
        返回：
             token无效返回状态码401
             有效返回回复结果

35.删除论坛回复

        URL：“/forum/reply/delete/{replyId}”
        方法：DELETE
        描述：删除论坛回复
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "replyId"(int):回复id(路径参数)
        返回：
             token无效返回状态码401
             有效返回删除结果

36.点赞回复或取消点赞

        URL：“/forum/reply/like/{replyId}”
        方法：POST
        描述：收藏或取消收藏主题
        参数：
            "token"(String):用户token(从HTTP请求头中获取"Authorization")
            "replyId"(int):回复id(路径参数)
        返回：
             token无效返回状态码401
             有效如果已经点赞返回取消点赞，未点赞返回成功点赞

37.获取论坛主题

        URL：“/forum/{topicId}”
        方法：GET
        描述：获取论坛主题
        参数：
            "topicId"(int):主题id(路径参数)
        返回：
             返回论坛主题

38.获取论坛主题列表

        URL：“/forum”
        方法：GET
        描述：获取论坛主题列表
        参数：
             无
        返回：
             返回论坛主题列表

39.根据关键字和时间范围搜索论坛主题

        URL：“/forum/search”
        方法：GET
        描述：根据关键字和时间范围搜索论坛主题
        参数：
             "keyword"(String):关键字
             "startDate"(Date):开始时间
             "endDate"(Date):结束时间
        返回：
             返回符合条件的论坛主题列表

40.获取论坛回复

        URL：“/forum/{topicId}/replies”
        方法：GET
        描述：获取论坛回复
        参数：
             "topicId"(int):主题id(路径参数)
        返回：
             返回论坛回复列表

41.获取二级回复

        URL：“/forum/reply/{replyId}/replies”
        方法：GET
        描述：获取二级回复
        参数：
             "replyId"(int):回复id(路径参数)
        返回：
             返回二级回复列表

42.获取用户收藏的主题

        URL：“/forum/starred”
        方法：GET
        描述：获取用户收藏的主题
        参数：
             "token"(String):用户token(从HTTP请求头中获取"Authorization")
        返回：
             token无效返回状态码401
             有效返回收藏主题列表

### HistoryEventController类

43.获取指定活动信息

        URL：“/historyevents/{eventId}”
        方法：GET
        描述：获取指定活动id信息
        参数：
             "eventId"(int):活动id(路径参数)
        返回：
             返回指定活动id的信息

44.获取历史活动

        URL：“/historyevents”
        方法：POST
        描述：获取历史活动
        参数：
             "query"(EventQuery):查询条件
        返回：
             返回活动列表

45.发起评分

        URL：“/comments”
        方法：POST
        描述：发起评分
        参数：
             "token"(String):用户token(从HTTP请求头中获取"Authorization")
             "eventId"(int):活动id
             "content"(String):评价内容
             "rating"(int):评分
        返回：
             token无效返回状态码401
             有效返回评价

46.修改评分

        URL：“/comments”
        方法：PUT
        描述：修改评分
        参数：
             "token"(String):用户token(从HTTP请求头中获取"Authorization")
             "eventId"(int):活动id
             "content"(String):评价内容
             "rating"(int):评分
        返回：
             token无效返回状态码401
             有效返回评价

47.删除评分

        URL：“/comments”
        方法：DELETE
        描述：删除评分
        参数：
             "token"(String):用户token(从HTTP请求头中获取"Authorization")
             "eventId"(int):活动id
        返回：
             token无效返回状态码401
             有效返回状态码204

### MessageHistoryController类

48.查询历史聊天

        URL：“/messages/history”
        方法：GET
        描述：查询历史聊天
        参数：
             "username"(String):用户名称
        返回：
             聊天记录

### ReportController类

49.举报话题

        URL：“/forum/topics/{topicId}/report”
        方法：POST
        描述：举报话题
        参数：
             "token"(String):用户token(从HTTP请求头中获取"Authorization")
             "topicId"(int):话题id(路径参数)
             "reason"(String):举报原因
        返回：
             token无效返回状态码401
             有效返回举报成功信息

50.举报回复

        URL：“/forum/topics/{topicId}/replies/{replyId}/report”
        方法：POST
        描述：举报话题
        参数：
             "token"(String):用户token(从HTTP请求头中获取"Authorization")
             "topicId"(int):话题id(路径参数)
             "replyId"(int):回复id(路径参数)
             "reason"(String):原因
        返回：
             token无效返回状态码401
             有效返回成功信息

51.获取所有举报信息

        URL：“/forum/reports”
        方法：GET
        描述：获取所有举报信息
        参数：
             "token"(String):用户token(从HTTP请求头中获取"Authorization")
        返回：
             token无效返回状态码401
             有效返回举报信息列表

52.超级用户删除举报信息

        URL：“/forum/reports/{reportId}”
        方法：DELETE
        描述：超级用户删除举报信息
        参数：
             "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
             "repoertId"(int):举报id(路径参数)
        返回：
             token无效返回状态码401
             有效返回删除举报成功信息

53.处理举报信息

        URL：“/forum/reports/{reportId}”
        方法：POST
        描述：如果是举报话题，删除话题 如果是举报回复，删除回复
        参数：
             "token"(String):超级用户token(从HTTP请求头中获取"Authorization")
             "repoertId"(int):举报id(路径参数)
        返回：
             token无效返回状态码401
             有效如果举报信息不存在返回错误信息，否则返回处理成功信息