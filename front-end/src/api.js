import axios from 'axios';

const API_BASE_URL = 'http://123.56.102.167:8080';

// 创建话题
export const createTopic = (data, token) => {
    return axios.post(`${API_BASE_URL}/forum/topic`, data, {
        headers: { Authorization: `${token}` },
    });
};

// 删除话题
export const deleteTopic = (topicId, token) => {
    return axios.delete(`${API_BASE_URL}/forum/topic/delete/${topicId}`, {
        headers: { Authorization: `${token}` },
    });
};

// 收藏话题
export const starTopic = (topicId, token) => {
    return axios.post(`${API_BASE_URL}/forum/topic/${topicId}/star`, {}, {
        headers: { Authorization: `${token}` },
    });
};

// 回复话题
export const replyTopic = (data, token) => {
    return axios.post(`${API_BASE_URL}/forum/reply`, data, {
        headers: { Authorization: `${token}` },
    });
};

// 删除回复
export const deleteReply = (replyId, token) => {
    return axios.delete(`${API_BASE_URL}/forum/reply/delete/${replyId}`, {
        headers: { Authorization: `${token}` },
    });
};

// 点赞回复
export const likeReply = (replyId, token) => {
    return axios.post(`${API_BASE_URL}/forum/reply/like/${replyId}`, {}, {
        headers: { Authorization: `${token}` },
    });
};

// 获取指定话题
export const getTopic = (topicId) => {
    return axios.get(`${API_BASE_URL}/forum/${topicId}`);
};

// 获取话题列表
export const getTopics = () => {
    return axios.get(`${API_BASE_URL}/forum`);
};

// 获取话题一级回复
export const getTopicReplies = (topicId) => {
    return axios.get(`${API_BASE_URL}/forum/${topicId}/replies`);
};

// 获取话题二级回复
export const getReplyReplies = (replyId) => {
    return axios.get(`${API_BASE_URL}/forum/reply/${replyId}/replies`);
};

// 获取用户收藏主题
export const getStarredTopics = (token) => {
    return axios.get(`${API_BASE_URL}/forum/starred`, {
        headers: { Authorization: `${token}` },
    });
};
