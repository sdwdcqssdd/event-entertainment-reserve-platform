import React from 'react';
import { Form, Input, Button, Select, DatePicker } from 'antd';
const { RangePicker } = DatePicker;

const { Option } = Select;

const SearchForm = () => {
  const onFinish = (values) => {
    console.log('Received values of form: ', values);
  };

  return (
    <Form layout="inline" onFinish={onFinish}>


    <Form.Item
        name="event"
        label="活动"
        rules={[{ required: true, message: '请输入活动关键词' }]}
      >
        <Input placeholder="输入关键词" style={{ width: 180 }} />
      </Form.Item>
      

    
      <Form.Item
        name="location"
        label="活动地址"
        rules={[{ required: false }]}
      >
        <Select placeholder="选择开课学院" style={{ width: 180 }}>
          <Option value="engineering">工程学院</Option>
          <Option value="arts">文学院</Option>
        </Select>
      </Form.Item>




      <Form.Item
        name="dateRange"
        label="日期范围"
      >
        <RangePicker style={{ width: 250 }} />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit">
          查询
        </Button>
      </Form.Item>
    </Form>
  );
};

export default SearchForm;
