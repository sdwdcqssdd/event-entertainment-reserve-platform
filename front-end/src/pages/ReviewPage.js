import React, { useState } from 'react';
import { Form, Input, Button, List, message, Card } from 'antd';
import axios from 'axios';

const { TextArea } = Input;

const ReviewPage = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [eventId, setEventId] = useState(null);

  const fetchPendingEvents = async (eventId) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token'); // Assuming you store token in localStorage
      const response = await axios.post('http://123.56.102.167:8080/Appoint', null, {
        headers: {
          Authorization: token,
        },
        params: {
          eventId,
        },
      });
      setEvents(response.data);
      message.success('Fetched pending events successfully');
    } catch (error) {
      message.error('Failed to fetch pending events');
    } finally {
      setLoading(false);
    }
  };

  const approveEvent = async (appointId) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token'); // Assuming you store token in localStorage
      await axios.post('http://123.56.102.167:8080/Appoint/approve', null, {
        headers: {
          Authorization: token,
        },
        params: {
          appointId,
        },
      });
      message.success('Event approved successfully');
      fetchPendingEvents(eventId); // Refresh the list
    } catch (error) {
      message.error('Failed to approve event');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: 20 }}>
             <h1>-----</h1>
      <Card title="Fetch Pending Events" style={{ marginBottom: 20 }}>
        <Form
          layout="inline"
          onFinish={({ eventId }) => {
            setEventId(eventId);
            fetchPendingEvents(eventId);
          }}
        >
          <Form.Item
            name="eventId"
            rules={[{ required: true, message: 'Please input the event ID!' }]}
          >
            <Input placeholder="Event ID" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading}>
              Fetch Pending Events
            </Button>
          </Form.Item>
        </Form>
      </Card>

      <Card title="Pending Events">
        <List
          loading={loading}
          itemLayout="horizontal"
          dataSource={events}
          renderItem={(item) => (
            <List.Item
              actions={[
                <Button
                  type="primary"
                  onClick={() => approveEvent(item.id)}
                  loading={loading}
                >
                  Approve
                </Button>,
              ]}
            >
              <List.Item.Meta
                title={`Event ID: ${item.eventId}`}
                description={`User: ${item.userId}, Status: ${item.status}`}
              />
            </List.Item>
          )}
        />
      </Card>
    </div>
  );
};

export default ReviewPage;
