# [CS304] Team Project - Sprint 1

___

## Part I. Architecture Design

### Architectural Design

考虑到小组分工，以及我们想要实现的网站特性，我们采用了**前后端分离**的模式。我们认为通过前后端分离，我们获得了**彼此独立的技术栈**，**更高的团队协作效率**，**更高的可扩展性与灵活性**，**更好的可维护性**以及**更多的性能优化手段**。

同时，在后端，我们选择了分层架构。我们认为分层架构比起其他架构，代码层面更清晰易懂，板块之间的关系更明晰独立。

在后端代码中，我们将所有代码分为控制器层，模型层，数据访问层，业务逻辑层。

**1. 控制器层(Controller Layer)**
- 控制器层是应用程序的入口点，负责接收和处理来自客户端的 HTTP 请求。
- 控制器层包含处理程序，解析请求并调用适用的业务逻辑返回合适结果。

**2. 业务逻辑层(Service Layer)**
- 实现核心处理逻辑，包含核心处理过程
- 负责业务规则计算等
- 包含数据库与其它的交互却不直接访问数据库

**3. 数据访问层(Repository Layer)**
- 与数据库直接交互，执行CRUD（创建、读取、更新、删除）
- 负责数据库连接与查询

**4. 模型层(Model Layer)**
- 包含整个项目的业务对象与数据模型
- 封装


**核心优势**：通过将项目代码拆分为这些层，可以实现代码的**高内聚性**和**低耦合性**，使得各个部分可以**独立开发**、**测试**和**维护**。这种分层架构也使得代码更易于**理解**、**扩展**和**重用**。

**项目层级关系图如下**
```bash
├── BackEnd                                # 后端代码
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── team20
│   │   │   │           ├── controller      # 控制器层 负责跟前段对接
│   │   │   │           │   ├── EventController.java
│   │   │   │           │   └── UsersController.java
│   │   │   │           ├── model           # 模型层
│   │   │   │           │   ├── Event.java
│   │   │   │           │   ├──UserEvent.java
│   │   │   │           │   └── User.java
│   │   │   │           ├── repository      # 数据访问层 负责跟数据库交互
│   │   │   │           │   ├── EventRepository.java
│   │   │   │           │   ├── UserEventRepository.java
│   │   │   │           │   └── UsersRepository.java
│   │   │   │           ├── service         # 业务逻辑层 实现任务逻辑
│   │   │   │           │   ├── EventService.java
│   │   │   │           │   ├── EventServiceImpl.java
│   │   │   │           │   ├── UserEventService.java
│   │   │   │           │   ├── UserEventServiceImpl.java
│   │   │   │           │   ├── UsersService.java
│   │   │   │           │   └── UsersServiceImpl.java
│   │   │   │           └── BackEndApplication.java #启动器
│   │   │   └── resources
│   │   │       ├── application.properties                       
│   │   │       └── application.yml                 
│   │   └── test                                   # 测试代码
│   │       └── java
│   │           └── com
│   │               └── team20
│   │                   └── backend
│   │                       └── BackEndApplicationTests.java
└── pom.xml
```
```bash
front-end
├── .DS_Store
├── .gitignore
├── README.md
├── package.json					# 存储项目的元数据及管理项目的依赖、脚本等
├── package-lock.json					# 锁定安装时的包的版本，确保其他开发者环境一致性
├── public
│   ├── index.html
│   ├── favicon.ico					# 网站的图标
│   ├── logo192.png					# 网站的logo，通常用于显示在标签页和书签
│   ├── logo512.png					# 大尺寸的网站logo，可用于应用程序启动屏幕
│   ├── manifest.json					# 应用程序的元数据，如应用名称、图标等
│   └── robots.txt
└── src
    ├── App.css
    ├── App.js
    ├── App.test.js
    ├── index.css
    ├── index.js
    ├── logo.svg
    ├── reportWebVitals.js
    ├── setupTests.js
    ├── Home					
    │   ├── Banner					# 主页上的横幅组件
    │   │   ├── bg.jpeg
    │   │   ├── index.jsx
    │   │   └── style.module.css
    │   └── Courses					# 活动展示组件
    │       ├── index.jsx
    │       ├── style.module.css
    │       └── test.jpg
    └── pages					#主要的界面内容文件
        ├── Booking.js
        ├── Home.js
        ├── Launch.js
        ├── Login.js
        ├── Recommend.js
        └── Signup.js
```

**层级关系图如下**
![](https://img2.imgtp.com/2024/04/13/oWezC3Sj.png)

## Part II UI Design

### 1.活动列表查看

<img src=".\figure\decdfc0e5918462853880235b952b3c.png">



### 2.发起活动（表单）

<img src=".\figure\发起活动 (表单).png">

### 3.活动推荐页面（轮播图）

<img src=".\figure\活动推荐页面 轮播图.png">

### 4.个人日程表

<img src=".\figure\个人日程表.png">

### 5.活动信息讨论（论坛）

<img src=".\figure\5c21f780aacb16c9998edca8084bedf.png">

<img src=".\figure\4de2028da38a4223bb96a92d4c7316b.png">
