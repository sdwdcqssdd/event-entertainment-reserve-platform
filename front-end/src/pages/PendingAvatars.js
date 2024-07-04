import React, { useEffect, useState } from 'react';
import { List, Card, Button, notification } from 'antd';
import { convertBase64ToBlobUrl } from '../api/user';


const PendingAvatars = () => {
    const [avatars, setAvatars] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchPendingAvatars = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await fetch('http://123.56.102.167:8080/avatars/pending', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `${token}`
                    }
                });

                if (!response.ok) {
                    throw new Error(`HTTP错误！状态: ${response.status}`);
                }

                const data = await response.json();
                const updatedData = data.map(item => {
                    if (item.avatarData.startsWith('dataimage/jpegbase64')) {
                        item.avatarData = item.avatarData.replace('dataimage/jpegbase64', '');
                    } else if (item.avatarData.startsWith('dataimage/pngbase64')) {
                        item.avatarData = item.avatarData.replace('dataimage/pngbase64', '');
                    }
                    console.log(item.avatarData);
                    return item;
                });
                setAvatars(updatedData);
            } catch (error) {
                notification.error({
                    message: '加载头像失败',
                    description: '无法从服务器加载头像数据。',
                });
                console.error('获取待处理头像过程中出现问题:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchPendingAvatars();
    }, []);

    const handleApprove = async (avatarId) => {
        try {
            const token = localStorage.getItem('token');

            const response = await fetch(`http://123.56.102.167:8080/avatars/approve?avatarId=${avatarId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `${token}`
                },

            });

            if (!response.ok) {
                throw new Error(`HTTP错误！状态: ${response.status}`);
            }

            notification.success({
                message: '头像批准成功',
                description: '该头像已成功批准。',
            });

            // 移除已批准的头像
            setAvatars(avatars.filter(avatar => avatar.id !== avatarId));
        } catch (error) {
            notification.error({
                message: '批准头像失败',
                description: '批准头像过程中发生错误，请稍后重试。',
            });
            console.error('批准头像过程中出现问题:', error);
        }
    };

    

    return (
        <div style={{ maxWidth: 800, margin: '20px auto' }}>
            <h1>待批准的头像</h1>
            <List
                loading={loading}
                dataSource={avatars}
                renderItem={item => (
                    <List.Item>
                        <Card 
                            title={`用户: ${item.userId}`} 
                            bordered={false} 
                            style={{ width: '100%' }}
                            extra={<Button type="primary" onClick={() => handleApprove(item.avatarId)}>批准</Button>}
                        >
                            <img src={`${convertBase64ToBlobUrl(item.avatarData)}`} alt="Avatar" style={{ width: '100%', height: 'auto' }} />
                        </Card>
                    </List.Item>
                )}
            />
        </div>
    );
};

export default PendingAvatars;
