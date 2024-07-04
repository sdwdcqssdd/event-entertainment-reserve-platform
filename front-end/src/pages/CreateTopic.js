import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import { createTopic } from '../api';

const CreateTopic = () => {
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const onFinish = async (values) => {
        setLoading(true);
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                message.error('请先登录');
                setLoading(false);
                return;
            }

            const response = await createTopic(values, token);

            if (response.status === 200) {
                message.success('话题创建成功');
                navigate('/posts');
            } else {
                message.error('话题创建失败');
            }
        } catch (error) {
            console.error(error);
            message.error('发生错误，请稍后再试');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Form
            name="create_topic"
            layout="vertical"
            onFinish={onFinish}
            initialValues={{
                userId: 1,
                postTime: new Date().toISOString()
            }}
        >
            <Form.Item
                name="userId"
                label="用户ID"
                rules={[{ required: true, message: '请输入用户ID' }]}
            >
                <Input disabled />
            </Form.Item>
            <Form.Item
                name="title"
                label="标题"
                rules={[{ required: true, message: '请输入标题' }]}
            >
                <Input />
            </Form.Item>
            <Form.Item
                name="content"
                label="内容"
                rules={[{ required: true, message: '请输入内容' }]}
            >
                <Input.TextArea rows={4} />
            </Form.Item>
            <Form.Item
                name="postTime"
                label="发布时间"
                rules={[{ required: true, message: '请输入发布时间' }]}
            >
                <Input disabled />
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit" loading={loading}>
                    创建话题
                </Button>
            </Form.Item>
        </Form>
    );
};

export default CreateTopic;
