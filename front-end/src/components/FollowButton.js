import React, { useState, useEffect } from 'react';
import { Button, message } from 'antd';
import { followUser, isFollowing } from '../api/user'; // 假设你有一个API模块处理用户相关的请求

const FollowButton = ({ followeeId }) => {
  const [loading, setLoading] = useState(false);
  const [isFollowingState, setIsFollowingState] = useState(false);

  useEffect(() => {
    const checkFollowingStatus = async () => {
      try {
        const token = localStorage.getItem('token');
        const isFollowing = await isFollowing(token, followeeId);
        setIsFollowingState(isFollowing);
      } catch (error) {
        console.error('Error checking following status: ', error);
      }
    };

    checkFollowingStatus();
  }, [followeeId]);

  const handleFollow = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      await followUser(token, followeeId);
      setIsFollowingState(true);
      message.success('关注成功');
    } catch (error) {
      console.error('Error following user: ', error);
      message.error('关注失败');
    } finally {
      setLoading(false);
    }
  };

  if (isFollowingState) {
    return null; // 如果已经关注，则不显示按钮
  }

  return (
    <Button type="primary" loading={loading} onClick={handleFollow}>
      关注
    </Button>
  );
};

export default FollowButton;
