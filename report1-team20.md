# [CS304] 20小组项目提议

## 项目提议
### 项目前瞻

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们组计划建立一个校园活动与娱乐中心的网页系统。我们主要服务的人群是南方科技大学在校的学生以及教职工。这个系统旨在发布各种校园活动信息，如校园演出、学术讲座等。用户可以在该系统上实现预约活动、评价活动等功能。通过这些功能，我们可以使用户能够便利地预定心仪的活动，并及时反馈活动的体验感，方便组织者进行相关改进。我们最终的目标是开发一个具有丰富功能的跨平台（网页端、移动端）、可靠稳定的软件系统，这个系统不仅能够成为一个信息发布和预约的平台，而且能够成为一个给学生和教职工提供便利，丰富校园生活，提高校园活动的质量和多样性的校园活动中心。


### 功能性需求

1. **查看各种活动的具体信息**
用户可以查看活动名称、时间、位置、活动容纳人数等信息。
2. **预约活动**
用户可以为各种活动进行报名预订。
3. **支持往期活动评价与回顾**
用户可以发表参加活动的感想，主办方也可以发表活动的回顾。
4. **支持个性化定制**
系统可以根据用户的历史活动和现有活动的热度对用户进行活动推荐。
5. **发起活动**
用户可以发起讲座、音乐节、比赛等活动。
6. **支持跨平台**
可以通过网站或小程序进行访问。
7. **支持用户反馈**
若用户在使用软件中出现bug，可以反馈给运维团队。

8. **支持志愿者板块**
用户可以发布志愿者招募信息，也可以报名成为志愿者。

9. **至此个性化日程表**
网页提供个性化日程表，上面显示用户的课程与预约的讲座活动信息。

#### 非功能性需求
1.	系统整体可靠性达到99%以上
2.	预定应当在1秒内返回结果
3.	系统应支持高并发
4.	系统应确保用户登录密码不会泄露和盗用
5.	软件系统应当在16周前交付并遵循给定的过程规范

#### 数据需求
1.	用户登录密码，用户注册时获得
2.	活动具体信息，用户申办活动时获得
3.	活动评论及回顾，用户发布时获得
4.	用户参加的历史活动，用户预定活动时获得
5.	可举办活动的场所信息（可使用时间，容纳人数等），软件使用前设置好，后续管理员也可以修改

#### 技术栈
一、前端：
1. **网页设计实现：** HTML、CSS、JavaScript
2. **网页框架：** Angular、React或Vue.js
3. **移动端应用开发框架：** React Native、Swift（iOS）、Kotlin（Android）
4. **用户界面和体验设计：** Sketch、Adobe XD、Figma等

二、后端：
1. **编程语言：** Python、Java
2. **API与前端通信的接口**
3. **数据库：** PostgreSQL、MySQL用于存储用户数据、网页活动等的内容
4. **处理大量数据：** Apache Hadoop、Spark技术
5. **个性化推荐系统：** TensorFlow、PyTorch等服务
6. **数据安全保证：** HTTPS、SSL
7. **实施SQL注入防护**

服务器和部署：
1. **Docker**
2. **Kubernetes容器编排**
3. **用Markdown编写文档**


## 项目规划

![](https://pic.imgdb.cn/item/65f68e609f345e8d0380c4a4.png)
![](https://pic.imgdb.cn/item/65f68e959f345e8d0381df0b.png)

---
## AI使用

- Have you used AI to propose features for the project?
是的。
![](https://pic.imgdb.cn/item/65f439ec9f345e8d03cb2c73.png)
我们没有直接使用ChatGpt的回答，而是把它的答案当作一种建议来激发我们的灵感。最后的特点也是由我们自己总结归纳的。

- Have you used AI to conduct the preliminary requirement analysis (e.g., identify functional and nonfunctional requirements)?
没有。
![](https://pic.imgdb.cn/item/65f4489e9f345e8d033e1573.png)
ChactGpt的需求分析太笼统晦涩，不如我们自己写的清晰。而且ChatGpt很多需求并没有考虑到重心。
- Have you used AI to generate user stories?
没有。
![](https://pic.imgdb.cn/item/65f442179f345e8d0308f221.png)
ChatGpt的用户故事明显劣于我们的写法。我们的写法更清楚明晰具有内在逻辑。因为我们在编写用户故事的时候带入了每个角色并从实现与功能的角度上考虑的。

- Have you used AI to generate issues or tasks?
是的。
![](https://pic.imgdb.cn/item/65f4495c9f345e8d0345b14d.jpg)
我们没有直接使用ChatGpt的回答，而是把它的答案当作一种建议来激发我们的灵感。最后的特点也是由我们自己总结归纳的。