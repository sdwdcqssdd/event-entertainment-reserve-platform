import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';  // 引入 useNavigate 钩子


function Login() {
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();  // 获取 navigate 函数

    const onFinish = async (values) => {
        setLoading(true);
        
        const queryParams = new URLSearchParams(values).toString();
        try {

            const response = await fetch(`http://123.56.102.167:8080/signin?${queryParams}`, {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded'
                },
            });
            
            const data = await response.text();
            console.log(data);
            if (!response.ok) throw new Error(data.message || 'Login failed');
           
            localStorage.setItem('token', data);  // 保存token到localStorage
            message.success('Login successful!');
            navigate('/home');  // 使用 navigate 进行导航
        } catch (error) {
            message.error('Login failed: ' + error.message);
            console.log(error.message);
        } finally {
            setLoading(false);
        }
    };


    return (
        <div style={{ maxWidth: 300, margin: '100px auto' }}>
          <h3>登陆</h3>
            <Form
                name="normal_login"
                className="login-form"
                initialValues={{ remember: true }}
                onFinish={onFinish}
            >
                <Form.Item
                    name="username"
                    rules={[{ required: true, message: 'Please input your Username!' }]}
                >
                    <Input placeholder="Username" />
                </Form.Item>
                <Form.Item
                    name="password"
                    rules={[{ required: true, message: 'Please input your Password!' }]}
                >
                    <Input type="password" placeholder="Password" />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        登陆
                    </Button>
                    <Button type="link" onClick={() => navigate('/signup')}>
                      没有账号？注册一个！
                    </Button>
                </Form.Item>
            </Form>
       
        </div>
    );
}

export default Login;
