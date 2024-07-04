import React, { useEffect, useState } from 'react';
import { List, Avatar, Spin, message } from 'antd';
import { getFollowers } from '../api/user';
import { UserOutlined } from '@ant-design/icons';
import { convertBase64ToBlobUrl } from '../api/user';

const FollowersList = () => {
  const [followers, setFollowers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFollowers = async () => {
      try {
        const token = localStorage.getItem('token');
        const followersData = await getFollowers(token);
        setFollowers(followersData);
      } catch (error) {
        console.error('Error fetching followers: ', error);
        message.error('获取关注者列表失败');
      } finally {
        setLoading(false);
      }
    };

    fetchFollowers();
  }, []);

  if (loading) {
    return <Spin tip="Loading followers..." />;
  }

  return (
    <>
    <h1>----------</h1>
        <h1>我的粉丝</h1>
    <List
      itemLayout="horizontal"
      dataSource={followers}
      renderItem={follower => (
        <>
         
        <List.Item style={{width:'500px'}}>
          <List.Item.Meta
          avatar={<Avatar
            style={{ marginLeft: 'auto', cursor: 'pointer' }}
            src={follower.avatarData ? `${convertBase64ToBlobUrl(follower.avatarData)}` : undefined}
            icon={!follower.avatarData  && <UserOutlined />}
          />}
            title={follower.userName}
            description={follower.email}
          />
        </List.Item>
        </>
      )}
    />
    </>
  );
};

export default FollowersList;
