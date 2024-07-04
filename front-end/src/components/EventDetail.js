import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Spin, Descriptions, notification,Avatar } from 'antd';
import { getEventById } from '../api/user'; // 假设这个函数封装了获取活动详情的API调用
import { convertBase64ToBlobUrl } from '../api/user';
import { UserOutlined } from '@ant-design/icons';
const EventDetail = () => {
  const { eventId } = useParams();
  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);
  const venues = [
             { id: 1, name: '三教108' },
             { id: 2, name: '三教107' },
             { id: 3, name: '一科报告厅' },
             { id: 4, name: '工学院南楼' },
             { id: 5, name: '工学院北楼' },
             { id: 6, name: '图书馆报告厅' },
             { id: 7, name: '文科楼报告厅' },
             { id: 8, name: '润扬体育馆' },
             { id: 9, name: '理学院一楼化学系C103' }
         ];
         const getVenueNameById = (id) => {
             const venue = venues.find(venue => venue.id === id);
             return venue ? venue.name : '未知场馆';
         };
         const categories = [
             { id: 1, name: '讲座' },
             { id: 2, name: '体育活动' },
             { id: 3, name: '志愿者活动' },
             { id: 4, name: '心里健康活动' },
             { id: 5, name: '境外交流活动' },
             { id: 6, name: '勤工俭学' },
         ];
         const getCategoryNameById = (id) => {
             const category = categories.find(category=> category.id === id);
             return category ? category.name : '其他活动';
         };

  useEffect(() => {
    const fetchEvent = async () => {
      try {
        const response = await getEventById(eventId);
        setEvent(response);
      } catch (error) {
        console.error('Error fetching event details: ', error);
        notification.error({
          message: '加载失败',
          description: '无法加载活动详情，请稍后重试。',
        });
      } finally {
        setLoading(false);
      }
    };

    fetchEvent();
  }, [eventId]);

  if (loading) {
    return <Spin tip="加载中..." />;
  }

  if (!event) {
    return <div>活动未找到</div>;
  }

  return (
<div>
<h1>活动详情</h1>
    <Descriptions title="活动详情" bordered>
      <Descriptions.Item label="标题">{event.event.title}</Descriptions.Item>
      <Descriptions.Item label="描述">{event.event.description}</Descriptions.Item>
      <Descriptions.Item label="活动日期">{event.event.date}</Descriptions.Item>
      <Descriptions.Item label="开始时间">{event.event.startTime}</Descriptions.Item>
      <Descriptions.Item label="结束时间">{event.event.endTime}</Descriptions.Item>
      <Descriptions.Item label="活动场地">{getVenueNameById(event.event.venueId)}</Descriptions.Item>
      <Descriptions.Item label="最大报名人数">{event.event.capacityLimit}</Descriptions.Item>
      <Descriptions.Item label="剩余席位">{event.event.remaining}</Descriptions.Item>
      {/* 其他需要显示的字段 */}
    </Descriptions>

    <div>
    <h4>组织者</h4>
    <Avatar
                                style={{ marginLeft: 'auto', cursor: 'pointer' }}
                                src={event.user.avatarData ? `${convertBase64ToBlobUrl(event.user.avatarData)}` : undefined}
                                icon={!event.user.avatarData&& <UserOutlined />}
                              />
                          
                          <p>{event.user.userName}</p>
    </div>
    </div>
  );
};

export default EventDetail;
