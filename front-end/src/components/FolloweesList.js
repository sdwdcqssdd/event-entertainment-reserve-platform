import React, { useEffect, useState } from 'react';
import { List, Avatar, Spin, message, Button } from 'antd';
import { getFollowees, unfollowUser } from '../api/user';
import { UserOutlined } from '@ant-design/icons';
import { convertBase64ToBlobUrl } from '../api/user';

const FolloweesList = () => {
  const [followees, setFollowees] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFollowees = async () => {
      try {
        const token = localStorage.getItem('token');
        const followeesData = await getFollowees(token);
        setFollowees(followeesData);
      } catch (error) {
        console.error('Error fetching followees: ', error);
        message.error('获取关注列表失败');
      } finally {
        setLoading(false);
      }
    };

    fetchFollowees();
  }, []);

  const handleUnfollow = async (followeeId) => {
    try {
      const token = localStorage.getItem('token');
      await unfollowUser(token, followeeId);
      setFollowees(followees.filter(followee => followee.userId !== followeeId));
      message.success('取消关注成功');
    } catch (error) {
      console.error('Error unfollowing user: ', error);
      message.error('取消关注失败');
    }
  };

  if (loading) {
    return <Spin tip="Loading followees..." />;
  }

  return (
    <>
    <h1>----------</h1>

      <h1>我关注的人</h1>
      <List
        itemLayout="horizontal"
        dataSource={followees}
        renderItem={followee => (
          <List.Item
            actions={[
              <Button type="link" onClick={() => handleUnfollow(followee.userId)}>
                取消关注
              </Button>
            ]}
            style={{ width: '500px' }}
          >
            <List.Item.Meta
              avatar={
                <Avatar
                  style={{ marginLeft: 'auto', cursor: 'pointer' }}
                  src={followee.avatarData ? convertBase64ToBlobUrl(followee.avatarData) : undefined}
                  icon={!followee.avatarData && <UserOutlined />}
                />
              }
              title={followee.userName}
              description={followee.email}
            />
          </List.Item>
        )}
      />
    </>
  );
};

export default FolloweesList;
