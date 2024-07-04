import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
    getTopic,
    getTopicReplies,
    getReplyReplies,
    replyTopic,
    likeReply,
    deleteReply
} from '../api';
import './PostDetails.css';

const PostDetails = () => {
    const { postId } = useParams();
    const [topicDetails, setTopicDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [replyContent, setReplyContent] = useState('');
    const [subReplyContent, setSubReplyContent] = useState({});
    const [replyReplies, setReplyReplies] = useState({});

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('token');
            try {
                const topicResponse = await getTopic(postId, token);
                const topicData = topicResponse.data.topic;
                const repliesResponse = await getTopicReplies(postId, token);
                topicData.replies = repliesResponse.data.map(replyWrapper => replyWrapper.reply);
                setTopicDetails(topicData);
            } catch (error) {
                console.error('Error fetching topic details or replies:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [postId]);

    const fetchReplyReplies = async (replyId) => {
        const token = localStorage.getItem('token');
        try {
            const response = await getReplyReplies(replyId, token);
            setReplyReplies(prevState => ({
                ...prevState,
                [replyId]: response.data.map(replyWrapper => replyWrapper.reply),
            }));
        } catch (error) {
            console.error('Error fetching reply replies:', error);
        }
    };

    const handleReplyChange = (event) => {
        setReplyContent(event.target.value);
    };

    const handleSubReplyChange = (replyId, event) => {
        setSubReplyContent(prevState => ({
            ...prevState,
            [replyId]: event.target.value,
        }));
    };

    const handleReplySubmit = async (event) => {
        event.preventDefault();
        const token = localStorage.getItem('token');
        const replyData = new URLSearchParams();
        replyData.append('topicId', postId);
        replyData.append('content', replyContent);
        replyData.append('parentId', 0); // For top-level replies, use 0

        try {
            await replyTopic(replyData, token);
            setReplyContent('');
            window.location.reload(); // 刷新页面以显示新回复
        } catch (error) {
            console.error('Error submitting reply:', error);
        }
    };

    const handleSubReplySubmit = async (replyId, event) => {
        event.preventDefault();
        const token = localStorage.getItem('token');
        const replyData = new URLSearchParams();
        replyData.append('topicId', postId);
        replyData.append('content', subReplyContent[replyId] || '');
        replyData.append('parentId', replyId);

        try {
            await replyTopic(replyData, token);
            setSubReplyContent(prevState => ({
                ...prevState,
                [replyId]: '',
            }));
            window.location.reload(); // 刷新页面以显示新回复
        } catch (error) {
            console.error('Error submitting sub-reply:', error);
        }
    };

    const handleLikeReply = async (replyId) => {
        const token = localStorage.getItem('token');
        try {
            await likeReply(replyId, token); // 调用 likeReply 函数
            window.location.reload(); // 刷新页面以显示点赞后的状态
        } catch (error) {
            console.error('Error liking reply:', error);
        }
    };

    const handleDeleteReply = async (replyId) => {
        const token = localStorage.getItem('token');
        try {
            await deleteReply(replyId, token);
            window.location.reload(); // 刷新页面以显示删除后的状态
        } catch (error) {
            console.error('Error deleting reply:', error);
        }
    };

    if (loading) {
        return <div className="loading">Loading...</div>;
    }

    if (!topicDetails) {
        return <div className="error">Topic not found</div>;
    }

    const { topic, user, replies } = topicDetails;

    return (
        <div className="post-details">
            <h1 className="post-title">{topic.title}</h1>
            <p className="post-author">By {user ? user.userName : 'Unknown'}</p>
            <p className="post-content">{topic.content}</p>
            <p className="post-time">Posted on: {new Date(topic.postTime).toLocaleString()}</p>
            <h2 className="replies-title">Replies</h2>
            <ul className="replies-list">
                {replies && replies.length > 0 ? (
                    replies.map(reply => (
                        <li key={reply.id} className="reply-item">
                            <p className="reply-content">{reply.content}</p>
                            <p className="reply-author">By {reply.user ? reply.user.userName : 'Unknown'} on {new Date(reply.postTime).toLocaleString()}</p>
                            <div className="reply-buttons">
                                <button onClick={() => fetchReplyReplies(reply.id)}>Show Replies</button>
                                <button onClick={() => handleLikeReply(reply.id)}>Like</button>
                                <button onClick={() => handleDeleteReply(reply.id)}>Delete</button>
                            </div>
                            {replyReplies[reply.id] && (
                                <ul className="sub-replies-list">
                                    {replyReplies[reply.id].map(subReply => (
                                        <li key={subReply.id} className="sub-reply-item">
                                            <p className="sub-reply-content">{subReply.content}</p>
                                            <p className="sub-reply-author">By {subReply.user ? subReply.user.userName : 'Unknown'} on {new Date(subReply.postTime).toLocaleString()}</p>
                                            <div className="sub-reply-buttons">
                                                <button onClick={() => handleLikeReply(subReply.id)}>Like</button>
                                                <button onClick={() => handleDeleteReply(subReply.id)}>Delete</button>
                                            </div>
                                        </li>
                                    ))}
                                    <li className="sub-reply-form">
                                        <form onSubmit={(e) => handleSubReplySubmit(reply.id, e)}>
                                            <textarea
                                                value={subReplyContent[reply.id] || ''}
                                                onChange={(e) => handleSubReplyChange(reply.id, e)}
                                                placeholder="Write your reply here..."
                                                rows="2"
                                                cols="50"
                                            />
                                            <br />
                                            <button type="submit">Submit Reply</button>
                                        </form>
                                    </li>
                                </ul>
                            )}
                        </li>
                    ))
                ) : (
                    <li className="no-replies">No replies yet.</li>
                )}
            </ul>
            <h2 className="reply-form-title">Post a Reply</h2>
            <form onSubmit={handleReplySubmit} className="reply-form">
                <textarea
                    value={replyContent}
                    onChange={handleReplyChange}
                    placeholder="Write your reply here..."
                    rows="4"
                    cols="50"
                />
                <br />
                <button type="submit">Submit Reply</button>
            </form>
        </div>
    );
};

export default PostDetails;
