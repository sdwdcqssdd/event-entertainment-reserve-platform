import React, { useEffect, useState } from 'react';
import { List, Card, Button, notification } from 'antd';
import moment from 'moment';

const PendingEvents = () => {
    const [events, setEvents] = useState([]);
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

    useEffect(() => {
        const fetchPendingEvents = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await fetch('http://123.56.102.167:8080/superuser/events', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `${token}`,
                    }
                });

                if (!response.ok) {
                    throw new Error(`HTTP错误！状态: ${response.status}`);
                }

                const data = await response.json();
                setEvents(data);
            } catch (error) {
                notification.error({
                    message: '加载事件失败',
                    description: '无法从服务器加载事件数据。',
                });
                console.error('获取待处理事件过程中出现问题:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchPendingEvents();
    }, []);

    const handleApprove = async (eventId) => {
        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`http://123.56.102.167:8080/superuser/events/approve?eventId=${eventId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `${token}`,
                },
                
            });

            if (!response.ok) {
                throw new Error(`HTTP错误！状态: ${response.status}`);
            }

            notification.success({
                message: '事件批准成功',
                description: '该事件已成功批准。',
            });

            // 移除已批准的事件
            setEvents(events.filter(event => event.id !== eventId));
        } catch (error) {
            notification.error({
                message: '批准事件失败',
                description: '批准事件过程中发生错误，请稍后重试。',
            });
            console.error('批准事件过程中出现问题:', error);
        }
    };

    return (
        <div style={{ maxWidth: 800, margin: '20px auto' }}>
            <h1>待批准的事件</h1>
            <List
                loading={loading}
                dataSource={events}
                renderItem={item => (
                    <List.Item>
                        <Card 
                            title={item.title} 
                            bordered={false} 
                            style={{ width: '100%' }}
                            extra={<Button type="primary" onClick={() => handleApprove(item.eventId)}>批准</Button>}
                        >
                            <p><strong>日期:</strong> {moment(item.date).format('YYYY年MM月DD日')}</p>
                            <p><strong>时间:</strong> {item.startTime} - {item.endTime}</p>
                            <p><strong>地点:</strong> {getVenueNameById(item.venueId)}</p>
                            <p><strong>描述:</strong> {item.description}</p>
                        </Card>
                    </List.Item>
                )}
            />
        </div>
    );
};

export default PendingEvents;
