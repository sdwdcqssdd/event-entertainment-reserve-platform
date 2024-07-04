import React, { useEffect, useState } from 'react';
import { List, Input, Button, Form } from 'antd';
import axios from 'axios';

const { TextArea } = Input;

const ChatMessageList = () => {
  const [messages, setMessages] = useState([]);
  const [username, setUsername] = useState(''); // 假设当前用户的聊天对象用户名

  useEffect(() => {
    const fetchMessages = async () => {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://123.56.102.167:8080/chatMessage', {
        headers: {
          Authorization: token,
        },
        params: {
          username,
        },
      });
      setMessages(response.data);
    };

    fetchMessages();
  }, [username]);

  const handleSendMessage = async (values) => {
    const token = localStorage.getItem('token');
    await axios.post('/chatMessage', {
      receiver: username,
      content: values.message,
    }, {
      headers: {
        Authorization: token,
      },
    });

    // 重新获取消息
    const response = await axios.get('/chatMessage', {
      headers: {
        Authorization: token,
      },
      params: {
        username,
      },
    });
    setMessages(response.data);
  };

  return (
    <div>
      <List
        itemLayout="horizontal"
        dataSource={messages}
        renderItem={item => (
          <List.Item>
            <List.Item.Meta
              title={item.sender}
              description={item.content}
            />
          </List.Item>
        )}
      />
      <Form onFinish={handleSendMessage}>
        <Form.Item name="message">
          <TextArea rows={4} />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit">
            Send
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default ChatMessageList;
