import React, { useState, useEffect } from 'react';
import { useTheme } from './ThemeContext'; // 引入 useTheme 钩子
import { BrowserRouter as Router, Route, Routes, Link, useNavigate } from 'react-router-dom';
import { Menu, Switch, Button, Avatar, Dropdown } from 'antd';
import { AppstoreOutlined, MailOutlined, SettingOutlined, UserOutlined } from '@ant-design/icons';
import Login from './pages/Login';
import Booking from './pages/Booking';
import Home from './pages/Home';
import Recommend from './pages/Recommend';
import Launch from './pages/Launch';
import Signup from './pages/Signup';
import PostsList from './pages/PostsList';
import PostDetails from './pages/PostDetails';
import History from './pages/History';
import EventDetails from './pages/EventDetails';
import ChangeAvatar from './pages/ChangeAvatar.js';
import Appointment from './pages/Appointment.js';
import PendingEvents from './pages/PendingEvents.js';
import PendingAvatars from './pages/PendingAvatars.js';
import CreateTopic from './pages/CreateTopic.js';
import StarredTopics from './pages/StarredTopics';
import ReviewPage from './pages/ReviewPage.js'


import UserInfo from './pages/UserInfo.js';
import Chat from './pages/Chat.js'
import FollowersList from './components/FollowersList.js';
import FolloweesList from './components/FolloweesList.js';
import PrivateChat from './pages/PrivateChat.js';
import EventDetail from './components/EventDetail.js';
import './App.css'; // 确保导入了CSS文件
import About from './components/About.js';
import { getPendingAvatars, convertBase64ToBlobUrl, getUserInfo } from './api/user';




  const NavigationBar = ({ onThemeChange, isDarkMode }) => {
      const [current, setCurrent] = useState('mail');
      const [avatarUrl, setAvatarUrl] = useState(null);
      const [userInfo, setUserInfo] = useState(null);
      const navigate = useNavigate();
      const token = localStorage.getItem('token');
      const isLoggedIn = !!token;

      useEffect(() => {
        if (isLoggedIn) {
            const fetchUserData = async () => {
                try {
                    const user = await getUserInfo(token);
                    setUserInfo(user);
                    const avatars = await getPendingAvatars(token);
                    if (avatars.avatarData.startsWith('dataimage/jpegbase64')) {
                        avatars.avatarData = avatars.avatarData.replace('dataimage/jpegbase64', '');
                    } else if (avatars.avatarData.startsWith('dataimage/pngbase64')) {
                        avatars.avatarData = avatars.avatarData.replace('dataimage/pngbase64', '');
                    }
                    if (avatars) {
                        setAvatarUrl(convertBase64ToBlobUrl(avatars.avatarData));
                    }
                } catch (error) {
                    console.error('Failed to fetch user data or avatar:', error);
                }
            };
            fetchUserData();
        }
    }, [isLoggedIn, token]);

      const handleClick = e => {
          setCurrent(e.key);
      };

      const handleLogout = () => {
          localStorage.removeItem('token');
          navigate('/');
      };

      const menu = (
          <Menu>
          <Menu.Item key="user_info">
                <Link to="/user_info">个人主页</Link>
            </Menu.Item>
              <Menu.Item key="change_avatar">
                  <Link to="/change_avatar">修改头像</Link>
              </Menu.Item>
              <Menu.Item key="appointment">
                  <Link to="/appointment">我的预约</Link>
              </Menu.Item>
              <Menu.Item key="follower">
                  <Link to="/follower">我的粉丝</Link>
              </Menu.Item>
              <Menu.Item key="followee">
                  <Link to="/followee">我的关注</Link>
              </Menu.Item>
              <Menu.Item key="logout">
                  <Button type="link" onClick={handleLogout}>
                      退出
                  </Button>
              </Menu.Item>
          </Menu>
      );

      return (
          <Menu onClick={handleClick} selectedKeys={[current]} mode="horizontal" style={{ zIndex: 1001, width: '100%', position: 'fixed', top: 0 }}>
              <Menu.Item key="home" icon={<MailOutlined />}>
                  <Link to="/home">查看活动</Link>
              </Menu.Item>
              {isLoggedIn && (
              <Menu.Item key="recommend" icon={<AppstoreOutlined />}>
                  <Link to="/recommend">活动推荐</Link>
              </Menu.Item>
              
              )}
              
              {isLoggedIn && (
              <Menu.Item key="chat" icon={<AppstoreOutlined />}>
                  <Link to="/chat">公共聊天室</Link>
              </Menu.Item>
              
              )}
              <Menu.SubMenu key="forum" title="论坛">
                  <Menu.Item key="posts-list">
                      <Link to="/posts">查看帖子</Link>
                  </Menu.Item>
                  <Menu.Item key="create-topic">
                      <Link to="/create-topic">创建话题</Link>
                  </Menu.Item>
                  <Menu.Item key="starred-topics">
                      <Link to="/starred-topics">收藏话题</Link>
                  </Menu.Item>
              </Menu.SubMenu>
              <Menu.Item key="launch" icon={<AppstoreOutlined />}>
                  <Link to="/launch">发起活动</Link>
              </Menu.Item>
              <Menu.Item key="history" icon={<AppstoreOutlined />}>
                  <Link to="/history">历史档案馆</Link>
              </Menu.Item>
              {isLoggedIn && userInfo?.identity === 'SUPERUSER' && (
                <>
                    <Menu.Item key="pending" icon={<AppstoreOutlined />}>
                        <Link to="/pending">待批准的活动</Link>
                    </Menu.Item>
                    <Menu.Item key="pendingAvatar" icon={<AppstoreOutlined />}>
                        <Link to="/pendingAvatar">待批准的头像</Link>
                    </Menu.Item>
                    <Menu.Item key="review" icon={<AppstoreOutlined />}>
                        <Link to="/review">待批准的预约</Link>
                    </Menu.Item>
                </>
            )}
              <Menu.SubMenu key="SubMenu" icon={<SettingOutlined />} title="更多">
                  <Menu.Item key="setting:1">
                      <Link to="/contact">联系我们</Link>
                  </Menu.Item>
                  <Menu.Item>
                      <Switch checkedChildren="暗" unCheckedChildren="浅" checked={isDarkMode} onChange={onThemeChange} />
                  </Menu.Item>
              </Menu.SubMenu>

              {isLoggedIn && (
                 <Menu.Item key="login" style={{ marginLeft: 'auto' }}>
                  <Dropdown overlay={menu} placement="bottomRight">
                      <Avatar
                          style={{ marginLeft: 'auto', cursor: 'pointer' }}
                          src={avatarUrl ? `${avatarUrl}` : undefined}
                          icon={!avatarUrl && <UserOutlined />}
                      />
                  </Dropdown>
                   </Menu.Item>
              )}
              {!isLoggedIn && (
                  <Menu.Item key="login" style={{ marginLeft: 'auto' }}>
                      <Button type="link">
                          <Link to="/">登录</Link>
                      </Button>
                  </Menu.Item>
              )}
          </Menu>

      );
  };



// 应用主组件
const App = () => {
    const { theme, toggleTheme } = useTheme();  // 使用 useTheme 钩子来获取当前主题和切换函数


    useEffect(() => {
        document.body.setAttribute('data-theme', theme);  // 动态更新 body 的 data-theme 属性
    }, [theme]);

    return (
        <Router>
            <NavigationBar onThemeChange={toggleTheme} isDarkMode={theme === 'dark'} />
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/recommend" element={<Recommend />} />
                <Route path="/booking" element={<Booking />} />
                <Route path="/launch" element={<Launch />} />
                <Route path="/home" element={<Home />} />
                <Route path="/posts" element={<PostsList />} />
                <Route path="/create-topic" element={<CreateTopic />} />
                <Route path="/post/:postId" element={<PostDetails />} />
                <Route path="/history" element={<History />} />
                <Route path="/event-details/:eventId" element={<EventDetails />} />
                <Route path="/change_avatar" element={<ChangeAvatar />} /> {/* 添加修改头像路由 */}
                <Route path="/appointment" element={<Appointment/>} />
                <Route path="/pending" element={<PendingEvents/>} />
                <Route path="/pendingAvatar" element={<PendingAvatars />} /> 
                <Route path="/user_info" element={<UserInfo />} /> 
                <Route path="/chat" element={<Chat />} />
                <Route path="/follower" element={<FollowersList />} />
                <Route path="/followee" element={<FolloweesList />} />
                <Route path="/starred-topics" element={<StarredTopics />} />  // 添加收藏话题路由
                <Route path="/private_chat" element={<PrivateChat />} />
                <Route path="/events/:eventId" element={<EventDetail />} />
                <Route path="/about" element={<About />} />
                <Route path="/review" element={<ReviewPage />} />

                <Route
                    path="*"
                    element={
                        <>
                            <div>404 Not Found</div>
                        </>
                    }
                />
            </Routes>
        </Router>
    );
};

export default App;
