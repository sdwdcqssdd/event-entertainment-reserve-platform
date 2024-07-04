import React, { useEffect, useState } from 'react';
import { getTopics, deleteTopic, starTopic } from '../api';
import { Link } from 'react-router-dom';
import './PostList.css'

const PostsList = () => {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        getTopics().then(response => {
            setPosts(response.data);
        }).catch(error => {
            console.error('Error fetching posts:', error);
        });
    }, []);

    const handleDeleteTopic = async (topicId) => {
        const token = localStorage.getItem('token');
        try {
            await deleteTopic(topicId, token);
            setPosts(posts.filter(post => post.topic.id !== topicId));
        } catch (error) {
            console.error('Error deleting topic:', error);
        }
    };

    const handleStarTopic = async (topicId) => {
        const token = localStorage.getItem('token');
        try {
            await starTopic(topicId, token);
            // Add any additional logic if needed, e.g., updating UI to show the topic is starred
        } catch (error) {
            console.error('Error starring topic:', error);
        }
    };

    return (
        <div className="posts-list">
            <h1 className="posts-title">Posts</h1>
            <ul className="posts-items">
                {posts.map(({ topic, user }) => (
                    <li key={topic.id} className="post-item">
                        <Link to={`/post/${topic.id}`} className="post-link">{topic.title}</Link> by {user.userName}
                        <p className="post-content">{topic.content}</p>
                        <p className="post-time">Posted on: {new Date(topic.postTime).toLocaleString()}</p>
                        <div className="post-buttons">
                            <button onClick={() => handleDeleteTopic(topic.id)}>Delete</button>
                            <button onClick={() => handleStarTopic(topic.id)}>Star</button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PostsList;
