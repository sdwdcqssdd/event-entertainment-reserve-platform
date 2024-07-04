import React from 'react';
import { Layout, Menu } from 'antd';
import ChatMessageList from '../components/ChatMessageList';
import ChatUserList from '../components/ChatUserList';

const { Header, Content, Sider } = Layout;

const PrivateChat = () => (
  <Layout style={{ minHeight: '100vh' }}>
    <Header className="header">
      <div className="logo" />
      <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['1']}>
        <Menu.Item key="1">Chat</Menu.Item>
      </Menu>
    </Header>
    <Layout>
      <Sider width={200} className="site-layout-background">
        <ChatUserList />
      </Sider>
      <Layout style={{ padding: '0 24px 24px' }}>
        <Content
          className="site-layout-background"
          style={{
            padding: 24,
            margin: 0,
            minHeight: 280,
          }}
        >
          <ChatMessageList />
        </Content>
      </Layout>
    </Layout>
  </Layout>
);

export default PrivateChat;
