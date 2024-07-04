import React, { useEffect, useState } from 'react';
import { Card, notification } from 'antd';
import axios from 'axios';

const API_URL = 'http://123.56.102.167:8080'; // 根据实际情况修改API地址

const UserInfo = () => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get(`${API_URL}/user_info`, {
                    params: { token },
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `${token}`
                    }
                });

                if (response.status === 200) {
                    setUser(response.data);
                } else {
                    throw new Error(`HTTP错误！状态: ${response.status}`);
                }
            } catch (error) {
                notification.error({
                    message: '加载用户信息失败',
                    description: '无法从服务器加载用户信息。',
                });
                console.error('获取用户信息过程中出现问题:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchUserInfo();
    }, []);

    if (loading) {
        return <div>加载中...</div>;
    }

    if (!user) {
        return <div>没有找到用户信息</div>;
    }

    return (
        <div style={{ maxWidth: 800, margin: '20px auto' }}>
            <h1>个人信息</h1>
            <Card title="用户详情" bordered={false}>
                <p><strong>用户名:</strong> {user.username}</p>
                <p><strong>邮箱:</strong> {user.email}</p>
                <p><strong>身份:</strong> {user.identity}</p>
            </Card>
        </div>
    );
};



export default UserInfo;
