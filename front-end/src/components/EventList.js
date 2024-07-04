import React, { useState, useEffect } from 'react';
import { Select, Input, DatePicker, Button, List, Tag, Avatar } from 'antd';
import moment from 'moment';
import { convertBase64ToBlobUrl } from '../api/user';
import { UserOutlined } from '@ant-design/icons';

const listItemStyle = {
  width: '100%',
  height: '400px',
  backgroundImage: `url('/assets/event.png')`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  fontFamily: '-apple-system',
  fontSize: "1rem",
  fontWeight: 1.5,
  lineHeight: 1.5,
  color: "#292b2c",
};

const { Option } = Select;

function EventList() {
  const [venueIds, setVenueIds] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [selectedDates, setSelectedDates] = useState([]);
  const [organizers, setOrganizers] = useState([]);
  const [events, setEvents] = useState([]);

  const handleSearch = async () => {
    const query = {};
    if (venueIds.length > 0) query.venueIds = venueIds;
    if (keyword) query.keyword = keyword;
    if (selectedDates.length > 0) {
      query.dates = selectedDates.map(date => date.format('YYYY-MM-DD'));
    }
    if (organizers.length > 0) query.organizers = organizers;

    try {
      const response = await fetch('http://123.56.102.167:8080/historyevents', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(query)
      });

      if (!response.ok) {
        throw new Error(`HTTP错误！状态: ${response.status}`);
      }

      const data = await response.json();
      setEvents(data);
    } catch (error) {
      console.error('请求过程中出现问题:', error);
    }
  };

  useEffect(() => {
    handleSearch(); // 执行搜索函数
  }, []); // 空数组意味着只在组件挂载时执行一次

  const openDetailsPage = (event) => {
    // 通过新标签页打开活动详情页
    console.log(event);
    window.open(`/event-details/${event.eventId}`, '_blank');
  };

  const handleDateChange = (date) => {
    if (date && !selectedDates.some(d => d.isSame(date, 'day'))) {
      setSelectedDates([...selectedDates, date]);
    }
  };

  const handleDateRemove = (date) => {
    setSelectedDates(selectedDates.filter(d => !d.isSame(date, 'day')));
  };

  return (
    <div style={{ fontFamily: "-apple-system", textAlign: "center", color: 'black' }}>
      <h1 style={{ color: 'black' }}>历史档案馆</h1>
      <Select
        mode="multiple"
        showSearch
        style={{ width: 300, marginBottom: 16, color: 'black' }}
        placeholder="选择场地"
        onChange={setVenueIds}
        filterOption={(input, option) =>
          option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
        }
      >
        <Option value="1">三教108</Option>
        <Option value="2">三教107</Option>
        <Option value="3">一科报告厅</Option>
        <Option value="4">工学院南楼</Option>
        <Option value="5">工学院北楼</Option>
        <Option value="6">图书馆报告厅</Option>
        <Option value="7">文科楼报告厅</Option>
        <Option value="8">润扬体育馆</Option>
        <Option value="9">理学院一楼化学系C103</Option>
        {/* 根据需要添加更多选项 */}
      </Select>
      <Input
        style={{ width: 300, marginBottom: 16, color: 'black' }}
        placeholder="输入关键字"
        onChange={e => setKeyword(e.target.value.trim())}
      />
      <Select
        mode="tags"
        style={{ width: 300, marginBottom: 16, color: 'black' }}
        placeholder="输入组织者名字"
        onChange={setOrganizers}
      />
      <DatePicker
        style={{ width: 300, marginBottom: 16, color: 'black' }}
        placeholder="选择日期"
        disabledDate={(current) => current && current > moment().endOf('day')}
        onChange={handleDateChange}
      />
      <div style={{ marginBottom: 16, color: 'black' }}>
        {selectedDates.map(date => (
          <Tag
            key={date.format('YYYY-MM-DD')}
            closable
            onClose={() => handleDateRemove(date)}
            style={{ color: 'black' }}
          >
            {date.format('YYYY-MM-DD')}
          </Tag>
        ))}
      </div>
      <Button type="primary" onClick={handleSearch}>搜索</Button>

      <List
        dataSource={events}
        renderItem={item => (
          <List.Item onClick={() => openDetailsPage(item.event)}>
            <List.Item.Meta
              style={listItemStyle}
              description={
                <div style={{ textAlign: "center", color: 'black' }}>
                  <div><p style={{
                    marginBottom: "0",
                    fontWeight: 800,
                    fontSize: "2rem",
                    color: "black"
                  }}>{item.event.title}</p></div>
                  <div><Avatar
                    style={{ marginLeft: 'auto', cursor: 'pointer' }}
                    src={item.user.avatarData ? `${convertBase64ToBlobUrl(item.user.avatarData)}` : undefined}
                    icon={!item.user.avatarData && <UserOutlined />}
                  />{item.user.userName}</div>
                  <div style={{ color: 'black' }}>活动日期: {item.event.date} {item.event.startTime}- {item.event.endTime}</div>
                  <div><p style={{
                    marginBottom: "0",
                    fontWeight: 600,
                    fontSize: "1rem",
                    color: "black"
                  }}>{item.event.description}</p></div>
                </div>
              }
            />
          </List.Item>
        )}
      />
    </div>
  );
}

export default EventList;
