import React from 'react';
import { Form, Input, Button, Row, Col, Card, message } from 'antd';
import { useNavigate } from 'react-router-dom'; // 导入 useNavigate 钩子

 /**
     * AI-generated-content
     * tool: ChatGPT
     * version: 4
     * usage:  prompt "帮我用antd和React生成一个注册页面"
     * 生成大体框架
     */


const Signup = () => {
  const navigate = useNavigate(); // 获取 navigate 函数

  const onFinish = (values) => {
    console.log('Received values of form: ', values);
    sendDataToBackend(values);
  };

  const sendDataToBackend = async (formData) => {
    const queryParams = new URLSearchParams(formData).toString();
    try {
      const response = await fetch(`http://123.56.102.167:8080/signup?${queryParams}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
      });
      const data = await response.json();
      console.log(data);
      // 检查消息内容决定反馈给用户的信息
      switch (data.message) {
        case 'sign up successfully':
          message.success('注册成功！即将跳转到登录页面...');
          setTimeout(() => {
            navigate('/'); // 成功后跳转到登录页面
          }, 2000);
          break;
        case 'username is already used':
          message.error('用户名已被使用，请选择其他用户名！');
          break;
        case 'email is already used':
          message.error('电子邮件地址已被使用，请使用其他电子邮件地址！');
          break;
        default:
          message.error(data.message || '注册失败，请重试！');
      }
    } catch (error) {
      console.error('Error getting form data:', error);
      message.error('网络错误，请稍后重试！');
    }
  };

  return (
    <div className="form-container">
      <Row justify="center" align="middle" style={{ minHeight: '100vh' }}>
        <Col xs={20} sm={16} md={12} lg={8} xl={6}>
          <Card title="注册用户" bordered={false}>
            <Form
              name="register"
              onFinish={onFinish}
              scrollToFirstError
              layout="vertical"
            >
              <Form.Item
                name="username"
                label="用户名"
                rules={[{ required: true, message: '请输入你的用户名!' }]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                name="email"
                label="E-mail"
                rules={[
                  { type: 'email', message: '输入的电子邮件无效!' },
                  { required: true, message: '请输入你的电子邮件!' },
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                name="password"
                label="密码"
                rules={[{ required: true, message: '请输入你的密码!' }]}
                hasFeedback
              >
                <Input.Password />
              </Form.Item>

              <Form.Item
                name="confirm"
                label="确认密码"
                dependencies={['password']}
                hasFeedback
                rules={[
                  { required: true, message: '请确认你的密码!' },
                  ({ getFieldValue }) => ({
                    validator(_, value) {
                      if (!value || getFieldValue('password') === value) {
                        return Promise.resolve();
                      }
                      return Promise.reject(new Error('两次输入的密码不匹配!'));
                    },
                  }),
                ]}
              >
                <Input.Password />
              </Form.Item>

              <Form.Item>
                <Button type="primary" htmlType="submit" block>
                  注册
                </Button>
              </Form.Item>
            </Form>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Signup;
