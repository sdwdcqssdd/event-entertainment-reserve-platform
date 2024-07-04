import React, { useState, useEffect } from 'react';
import { Button, Avatar, message } from 'antd';
import { convertBase64ToBlobUrl, reserveEvent } from '../../api/user';
import { UserOutlined } from '@ant-design/icons';
import FollowButton from '../../components/FollowButton';
import { useNavigate } from 'react-router-dom';

const Courses = () => {
  const [listItems, setListItems] = useState([]);
  const [avatarUrl, setAvatarUrl] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await fetch('http://123.56.102.167:8080/events');
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
        const token = localStorage.getItem('token');
        setIsLoggedIn(!!token);
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

  const handleItemClick = (eventId) => {
    navigate(`/events/${eventId}`);
  };

  return (
    <div style={{ color: 'black' }}>
      <ul>
        {listItems.map((item, index) => (
          <li 
            key={index} 
            onClick={() => handleItemClick(item.event.eventId)} 
            style={{ color: 'black', marginBottom: '20px', padding: '10px', border: '1px solid #ddd' }}
          >
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <Avatar
                style={{ marginLeft: 'auto', cursor: 'pointer' }}
                src={item.user.avatarData ? `${item.user.avatarData}` : undefined}
                icon={!avatarUrl && <UserOutlined />}
              />
              <p style={{ marginLeft: '10px', color: 'black' }}>{item.user.userName}</p>
              {isLoggedIn && <FollowButton followeeId={item.user.userId} />}
            </div>
            <div>
              <h3 style={{ color: 'black' }}>{item.event.title}</h3>
              <hr />
              <p style={{ color: 'black' }}>{item.event.description}</p>
              <hr />
              <p style={{ color: 'black' }}>开始时间: {item.event.startTime}</p>
              <p style={{ color: 'black' }}>结束时间: {item.event.endTime}</p>
            </div>
            <Button
              type="default"
              loading={loading}
              onClick={(e) => {
                e.stopPropagation();
                handleReserve(item.event.eventId);
              }}
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
