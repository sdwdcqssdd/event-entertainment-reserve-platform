import React, { useEffect, useState } from 'react';
import { List, Card, notification } from 'antd';
import moment from 'moment';

const Appointment = () => {
    const [appointments, setAppointments] = useState([]);
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
        const fetchUserAppointments = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await fetch('http://123.56.102.167:8080/userAppoint', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `${token}`
                    }
                });

                if (!response.ok) {
                    throw new Error(`HTTP错误！状态: ${response.status}`);
                }

                const data = await response.json();
                console.log(data)
                setAppointments(data);
            } catch (error) {
                notification.error({
                    message: '加载预约失败',
                    description: '无法从服务器加载预约数据。',
                });
                console.error('获取用户预约活动过程中出现问题:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchUserAppointments();
    }, []);

    return (
        <div style={{ maxWidth: 800, margin: '20px auto' }}>
            <h1>------</h1>
            <h1>我的预约</h1>
            <List
                loading={loading}
                dataSource={appointments}
                renderItem={item => (
                    <List.Item>
                        <Card 
                            title={item.title} 
                            bordered={false} 
                            style={{ width: '100%', position: 'relative' }}
                            extra={item.userEvent.status === 'approved' && (
                                <img
                                    src="/assets/approved.png"
                                    alt="Approved"
                                    style={{ width: 80, height: 80 }}
                                />
                            )}
                        >                            <p><strong>活动名称:</strong> {(item.eventDTO.event.title)}</p>
   <p><strong>活动id:</strong> {(item.eventDTO.event.eventId)}</p>
                            <p><strong>日期:</strong> {moment(item.eventDTO.event.date).format('YYYY年MM月DD日')}</p>
                            <p><strong>时间:</strong> {item.eventDTO.event.startTime} - {item.eventDTO.event.endTime}</p>
                            <p><strong>地点:</strong> {getVenueNameById(item.eventDTO.event.venueId)}</p>
                            <p><strong>描述:</strong> {item.eventDTO.event.description}</p>
                            <p><strong>状态:</strong> {item.userEvent.status}</p>
                        </Card>
                    </List.Item>
                )}
            />
        </div>
    );
};

export default Appointment;
