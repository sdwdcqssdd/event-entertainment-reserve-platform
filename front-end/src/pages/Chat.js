import React, { useState, useEffect } from 'react';
import { Layout, Menu, Input, Button, List, Avatar, message } from 'antd';
import { UserOutlined, SendOutlined } from '@ant-design/icons';
import axios from 'axios';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import {  convertBase64ToBlobUrl, getUserInfo,getPendingAvatars } from '../api/user';

const { Header, Content, Sider } = Layout;
const { TextArea } = Input;

const Chat = () => {
  const [publicMessages, setPublicMessages] = useState([]);
  const [privateMessages, setPrivateMessages] = useState([]);
  const [currentMessage, setCurrentMessage] = useState('');
  const [username, setUsername] = useState('用户'); // 假设用户名
  const [receiver, setReceiver] = useState('');
  const [stompClient, setStompClient] = useState(null);
  const [avatarUrl, setAvatarUrl] = useState(null);

  

  useEffect(() => {
    const jwtToken = localStorage.getItem('token');
    if (!jwtToken) {
      console.error('JWT token not found in local storage');
      return;
    }
    const fetchUserData = async () => {
      try {
          const user = await getUserInfo(jwtToken);
          setUsername(user.username);
          const avatars = await getPendingAvatars(jwtToken);
          if (avatars) {
              setAvatarUrl(convertBase64ToBlobUrl(avatars.avatarData));
          }
      } catch (error) {
          console.error('Failed to fetch user data or avatar:', error);
      }
  };
  fetchUserData();

      const socket = new SockJS('http://123.56.102.167:8080/chatRoom?token=' + jwtToken);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: (frame) => {
        client.subscribe('/topic/public', (message) => {
          const newMessage = JSON.parse(message.body);
          setPublicMessages((prevMessages) => [...prevMessages, newMessage]);
        });
      },
    });
    client.activate();
    setStompClient(client);

    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };
  }, []);

  const handleSendMessage = () => {
    if (currentMessage.trim() === '') return;

    const messageDTO = {
      sender: username,
      content: currentMessage,
      type: 'CHAT',
    };

    stompClient.publish({
      destination: '/app/chat.sendMessage',
      body: JSON.stringify(messageDTO),
    });
    setCurrentMessage('');
  };

  const handleSendPrivateMessage = () => {
    if (currentMessage.trim() === '' || receiver.trim() === '') return;

    const chatMessage = {
      sender: username,
      receiver: receiver,
      content: currentMessage,
      type: 'CHAT',
    };

    axios.post('/private_chat', chatMessage, {
      headers: { Authorization: `${localStorage.getItem('token')}` },
    });

    setPrivateMessages((prevMessages) => [...prevMessages, chatMessage]);
    setCurrentMessage('');
  };

  return (
    <Layout style={{ height: '100vh' }}>
      <Sider width={200} className="site-layout-background">
        <Menu mode="inline" defaultSelectedKeys={['1']} style={{ height: '100%', borderRight: 0 }}>
          <h1>------</h1>
          <h1>------</h1>
          <h1>------</h1>
          <Menu.Item key="1" icon={<UserOutlined />}>
            Public Chat
          </Menu.Item>
          
        </Menu>
      </Sider>
      <Layout style={{ padding: '0 24px 24px' }}>
        <Header style={{ background: '#fff', padding: 0 }} />
        <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
          <List
            header={<div>公共聊天室</div>}
            bordered
            dataSource={publicMessages}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  title={item.sender}
                  description={item.content}
                />
              </List.Item>
            )}
          />
          <TextArea
            rows={4}
            value={currentMessage}
            onChange={(e) => setCurrentMessage(e.target.value)}
            placeholder="Enter your message"
          />
          <Button type="primary" icon={<SendOutlined />} onClick={handleSendMessage}>
            Send
          </Button>
          <br />
          <br />
   
  
        </Content>
      </Layout>
    </Layout>
  );
};

export default Chat;
