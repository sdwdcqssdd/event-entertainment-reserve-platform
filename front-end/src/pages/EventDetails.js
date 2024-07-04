import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Card, Button,Rate,List } from 'antd';
import moment from 'moment';
import CommentForm from "../components/CommentForm";

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

function EventDetails() {
    const { eventId } = useParams(); // 从 URL 中获取 eventId
    const [event, setEvent] = useState(null);

    useEffect(() => {
        // 从服务器获取活动详情
        const fetchEventDetails = async () => {
            try {
                const response = await fetch(`http://123.56.102.167:8080/historyevents/${eventId}`);
                if (!response.ok) {
                    throw new Error(`HTTP错误！状态: ${response.status}`);
                }
                const data = await response.json();
                data.event.event.location = getVenueNameById(data.event.event.venueId); // 映射场馆名称
                console.log(data)
                setEvent(data);
            } catch (error) {
                console.error('获取活动详情过程中出现问题:', error);
            }
        };

        fetchEventDetails();
    }, [eventId]);

    if (!event) {
        return <div>加载中...</div>;
    }

    return (
        <div style={{ maxWidth: 800, margin: '20px auto' }}>
            <h1>活动详情</h1>
            <Card title={event.title} bordered={false}>
                <p><strong>日期:</strong> {moment(event.event.date).format('YYYY年MM月DD日')}</p>
                <p><strong>时间:</strong> {event.event.event.startTime} - {event.event.event.endTime}</p>
                <p><strong>地点:</strong> {event.event.event.location}</p>
                <p><strong>描述:</strong> {event.event.event.description}</p>
                <p><strong>评分:</strong> <Rate disabled defaultValue={event.event.event.rating / 2} /></p>
            </Card>
            <h2>评论</h2>
            <List
                dataSource={event.comments}
                renderItem={item => (
                    <List.Item>
                        <Card title={`用户: ${item.user.username}`} bordered={false} style={{ width: '100%' }}>
                            <p><strong>评论内容:</strong> {item.content}</p>
                            <p><strong>评分:</strong> <Rate disabled defaultValue={item.rating / 2} /></p>
                            <p><strong>评论时间:</strong> {moment(item.created_at).format('YYYY年MM月DD日')}</p>
                        </Card>
                       
                    </List.Item>
                )}
            />
            <CommentForm eventId={eventId} />
        </div>
    );
}

export default EventDetails;
