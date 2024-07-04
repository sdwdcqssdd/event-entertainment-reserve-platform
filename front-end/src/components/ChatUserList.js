import React, { useEffect, useState } from 'react';
import { List, Avatar } from 'antd';
import axios from 'axios';

const ChatUserList = () => {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const fetchUsers = async () => {
      const token = localStorage.getItem('token'); // 假设token保存在localStorage
      const response = await axios.post('http://123.56.102.167:8080/chatList', {}, {
        headers: {
          Authorization: token,
        },
      });
      setUsers(response.data);
    };

    fetchUsers();
  }, []);

  return (
    <List
      itemLayout="horizontal"
      dataSource={users}
      renderItem={item => (
        <List.Item>
          <List.Item.Meta
            avatar={<Avatar>{item.userDTO.username[0]}</Avatar>}
            title={item.userDTO.username}
            description={item.read ? 'Read' : 'Unread'}
          />
        </List.Item>
      )}
    />
  );
};

export default ChatUserList;
