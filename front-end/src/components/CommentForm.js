import React, { useState } from 'react';
import { Rate, notification } from 'antd';  // 引入 Rate 和 notification 组件

const CommentForm = ({ eventId }) => {
    const [content, setContent] = useState('');
    const [rating, setRating] = useState(0);  // 状态用于存储评分

    const handleSubmit = async (event) => {
        event.preventDefault();

        const commentData = {
            eventId: parseInt(eventId, 10) ,
            content,
             // 将评分转换为0-10的整数值
            rating: rating * 2, 
        };

        try {
            const token = localStorage.getItem('token');
            const queryParams = new URLSearchParams(commentData).toString();
            const response = await fetch(`http://123.56.102.167:8080/comments?${queryParams}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Authorization': `${token}`
                },
            });

            if (!response.ok) {
                throw new Error(`HTTP错误！状态: ${response.status}`);
            }

            const data = await response.json();
            notification.success({
                message: '评论提交成功',
                description: '您的评论已成功提交。',
            });

            // 清空表单
            setContent('');
            setRating(0);
        } catch (error) {
            notification.error({
                message: '评论提交失败',
                description: '提交评论过程中发生错误，请稍后重试。',
            });
            console.error('提交评论过程中出现问题:', error);
        }
    };

    const formStyle = {
        fontFamily: '-apple-system', // 设置字体
        maxWidth: '450px',
        margin: '0 auto',
        padding: '20px',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        borderRadius: '8px',
    };

    const inputStyle = {
        width: '100%',
        padding: '10px',
        margin: '10px 0',
        boxSizing: 'border-box',
        borderRadius: '4px',
        border: '1px inset #ccc'
    };

    const buttonStyle = {
        width: '100%',
        padding: '10px 0',
        backgroundColor: '#007BFF',
        color: '#fff',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
        marginTop: '10px',
        backgroundColor: 'white',
        color: 'black'
    };

    return (
        <div style={formStyle}>
            <h2>POST A COMMENT</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label style={{ display: 'block', marginBottom: '5px' }}>
                        Comments
                        <textarea value={content} onChange={e => setContent(e.target.value)} style={{ ...inputStyle, height: '100px' }} />
                    </label>
                </div>
                <div>
                    <label style={{ display: 'block', marginBottom: '5px' }}>
                        Rating
                        <Rate onChange={setRating} value={rating} style={{ fontSize: '20px' }} />
                    </label>
                </div>
                <button type="submit" style={buttonStyle}>POST COMMENT</button>
            </form>
        </div>
    );
}

export default CommentForm;
