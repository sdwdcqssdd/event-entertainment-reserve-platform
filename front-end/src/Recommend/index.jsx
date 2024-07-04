import React, { useState, useEffect } from 'react';
import styles from './style.module.css';
import { Button, Avatar, message } from 'antd';
import { convertBase64ToBlobUrl, reserveEvent } from '../api/user';
import { UserOutlined } from '@ant-design/icons';
import FollowButton from '../components/FollowButton';

const Courses = () => {
  const [listItems, setListItems] = useState([]);
  const [avatarUrl, setAvatarUrl] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://123.56.102.167:8080/recommend`, {
          method: 'GET',
          headers: {
            'Authorization': `${token}`,
            'Content-Type': 'application/x-www-form-urlencoded'
          },
        });
      
        const data = await response.json();
        console.log("Data received:", data);
        const processedData = data.map(item => ({
          ...item,
          user: {
            ...item.user,
            avatarData: convertBase64ToBlobUrl(item.user.avatarData)
          }
        }));
        console.log(processedData);
        setListItems(processedData);
      } catch (error) {
        console.error('Error fetching data: ', error);
      }
    };

    fetchEvents();
  }, []);

  const handleReserve = async (eventId) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      await reserveEvent(token, eventId);
      message.success('预约成功,请等待管理员审核');
    } catch (error) {
      console.error('Error reserving event: ', error);
      message.error('请勿重复预约');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.courses} style={{ color: 'black' }}>
      <h1 style={{ color: 'black' }}>下面是为您个性化推荐的活动：</h1>
      <ul className={styles.list}>
        {listItems.map((item, index) => (
          <li className={styles.item} key={index} style={{ color: 'black' }}>
            <div style={{ color: 'black' }}>
              <Avatar
                style={{ marginLeft: 'auto', cursor: 'pointer' }}
                src={item.user.avatarData ? `${item.user.avatarData}` : undefined}
                icon={!avatarUrl && <UserOutlined />}
              />
              <p style={{ color: 'black' }}>{item.user.userName}</p>
              {isLoggedIn && <FollowButton followeeId={item.user.userId} />}
            </div>
            <div className={styles.text} style={{ color: 'black' }}>
              <h3 className={styles.title} style={{ color: 'black' }}>{item.event.title}</h3>
              <hr className={styles.line} />
              <p className={styles.desc} style={{ color: 'black' }}>{item.event.description}</p>
              <hr className={styles.line} />
              <p style={{ color: 'black' }}>开始时间: {item.event.startTime}</p>
              <p style={{ color: 'black' }}>结束时间: {item.event.endTime}</p>
            </div>
            <Button
              type="default"
              className={styles.bookButton}
              loading={loading}
              onClick={() => handleReserve(item.event.eventId)}
              style={{ color: 'black' }}
            >
              预定
            </Button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Courses;
