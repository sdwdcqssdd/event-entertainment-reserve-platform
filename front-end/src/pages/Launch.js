import React, { useState, useEffect } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import { Modal, Form, Input, Button, Select, notification, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import 'react-big-calendar/lib/css/react-big-calendar.css';

const { TextArea } = Input;
const { Option } = Select;
const localizer = momentLocalizer(moment);

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

const Launch = () => {
  const [events, setEvents] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [currentDate, setCurrentDate] = useState(new Date());
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      notification.error({
        message: '无法发起活动',
        description: '请先登陆！',
      });
      navigate('/');
    }
    fetchEvents(currentDate);
  }, [currentDate, navigate]);

  const fetchEvents = (date) => {
    const formattedDate = moment(date).format('YYYY-MM-DD');
    fetch(`http://123.56.102.167:8080/events/by-date?date=${formattedDate}`)
      .then(response => response.json())
      .then(data => {
        setEvents(data.map(event => ({
          start: new Date(`${event.event.date}T${event.event.startTime}`),
          end: new Date(`${event.event.date}T${event.event.endTime}`),
          title: event.event.title + '\n' + event.event.type,
          resourceId: event.event.venueId
        })));
      })
      .catch(error => {
        notification.error({
          message: '加载事件失败',
          description: '无法从服务器加载事件数据。',
        });
      });
  };

  const isSlotAvailable = (venueId, slotStart, slotEnd) => {
    return !events.some(event => {
      return (
        event.resourceId === venueId &&
        moment(slotStart).isSameOrBefore(moment(event.end)) &&
        moment(slotEnd).isSameOrAfter(moment(event.start))
      );
    });
  };

  const handleSelectSlot = (slotInfo) => {
    const now = new Date();
    const startOfToday = moment().startOf('day');

    if (moment(slotInfo.start).isBefore(now)) {
      notification.error({
        message: '无法预约',
        description: '无法预约过去的时间。',
      });
      return;
    }

    if (moment(slotInfo.start).isSame(startOfToday, 'day')) {
      notification.error({
        message: '无法预约',
        description: '无法预约今天的时间。',
      });
      return;
    }

    const venueId = slotInfo.resourceId || 1;

    if (!isSlotAvailable(venueId, slotInfo.start, slotInfo.end)) {
      notification.error({
        message: '无法预约',
        description: `选定的时间段在场馆 ${venues.find(v => v.id === venueId).name} 已被占用。`,
      });
      return;
    }

    setSelectedSlot({ ...slotInfo, venueId: venueId });
    setIsModalVisible(true);
  };

  const handleNavigate = (date) => {
    setCurrentDate(date);
  };

  const handleSubmit = async (values) => {
    const token = localStorage.getItem('token');
    const newEvent = {
      title: values.eventName,
      description: values.eventDescription,
      date: moment(selectedSlot.start).format('YYYY-MM-DD'),
      startTime: moment(selectedSlot.start).toISOString(),
      endTime: moment(selectedSlot.end).toISOString(),
      venueId: selectedSlot.venueId,
      categoryId: values.categoryId,
      type: 'pending',
      organizerId: values.organizerId,
      allDay: selectedSlot.slots.length === 1,
    };

    fetch('http://123.56.102.167:8080/events', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `${token}`,
      },
      body: JSON.stringify(newEvent),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        notification.success({
          message: '预约成功',
          description: '您的预约已成功提交，等待管理员审核',
        });
        setEvents([...events, newEvent]);
        setIsModalVisible(false);
      })
      .catch((error) => {
        notification.error({
          message: '预约失败',
          description: '预约过程中发生错误，请稍后重试。',
        });
        console.error('Error:', error);
      });
  };

  const handleModalClose = () => {
    setIsModalVisible(false);
  };

  const eventPropGetter = (event) => {
    const now = new Date();
    if (event.start < now) {
      return {
        style: {
          backgroundColor: '#ddd',
          color: 'gray',
        },
      };
    } else {
      return {
        style: {
          backgroundColor: '#ffec3d',
        },
      };
    }
  };

  return (
    <div style={{ width: '80%', margin: '0 auto', marginTop: '100px', height: '600px' }}>
      <Calendar
        localizer={localizer}
        events={events}
        onSelectSlot={handleSelectSlot}
        onNavigate={handleNavigate}
        selectable={true}
        eventPropGetter={eventPropGetter}
        resources={venues.map(v => ({ resourceId: v.id, resourceTitle: v.name }))}
        resourceIdAccessor="resourceId"
        resourceTitleAccessor="resourceTitle"
        defaultView="day"
        views={['day']}
        step={30}
        timeslots={2}
        style={{ height: '100%' }}
      />
      <Modal
        title="预约详情"
        visible={isModalVisible}
        onCancel={handleModalClose}
        footer={null}
      >
        <Form onFinish={handleSubmit}>
          <Form.Item
            name="eventName"
            rules={[{ required: true, message: '请输入事件名称' }]}
          >
            <Input placeholder="事件名称" />
          </Form.Item>
          <Form.Item
            name="eventDescription"
            rules={[{ required: true, message: '请输入活动描述' }]}
          >
            <TextArea rows={4} placeholder="活动描述" />
          </Form.Item>
          <Form.Item
            name="categoryId"
            rules={[{ required: true, message: '请选择类别' }]}
          >
            <Select placeholder="选择类别" allowClear>
              <Option value={1}>讲座</Option>
              <Option value={2}>体育活动</Option>
              <Option value={3}>志愿者活动</Option>
              <Option value={4}>心理健康活动</Option>
              <Option value={5}>境外交流活动</Option>
              <Option value={6}>勤工俭学</Option>
            </Select>
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit">
              提交预约
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Launch;
