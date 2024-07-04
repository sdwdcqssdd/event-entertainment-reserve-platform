import React, { useEffect, useState } from 'react';
import { getStarredTopics } from '../api';  // Assuming the function is defined and exported from the api.js
import { Link } from 'react-router-dom';

const StarredTopics = () => {
    const [starredPosts, setStarredPosts] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem('token');
        getStarredTopics(token).then(response => {
            setStarredPosts(response.data);
        }).catch(error => {
            console.error('Error fetching starred posts:', error);
        });
    }, []);

    return (
        <div>
            <h1>Starred Posts</h1>
            <ul>
                {starredPosts.map(({ topic, user }) => (
                    <li key={topic.id}>
                        <Link to={`/post/${topic.id}`}>{topic.title}</Link> by {user.userName}
                        <p>{topic.content}</p>
                        <p>Posted on: {new Date(topic.postTime).toLocaleString()}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default StarredTopics;
