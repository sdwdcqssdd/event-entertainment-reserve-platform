import axios from 'axios';
import { notification } from 'antd';

const API_URL = 'http://123.56.102.167:8080'; // 根据实际情况修改API地址

export const uploadAvatar = async (token, avatarBase64) => {

    try {
        if (avatarBase64.startsWith('data:image/jpeg;base64,')) {
            avatarBase64 = avatarBase64.replace('data:image/jpeg;base64,', '');
        } else if (avatarBase64.startsWith('data:image/png;base64,')) {
            avatarBase64 = avatarBase64.replace('data:image/png;base64,', '');
        }

        const response = await axios.post(`${API_URL}/user/avatar`, { avatar: avatarBase64 }, {
            headers: {
                'Authorization': token,
                'Content-Type': 'application/json'
            }
        });
        return response.data;
    } catch (error) {
        console.error('Failed to upload avatar:', error);
        throw error;
    }
};



export const getPendingAvatars = async (token) => {
    try {
        const response = await axios.get(`${API_URL}/avatars`, {
            headers: {
                'Authorization': token
            }
        });
        return response.data;
    } catch (error) {
        console.error('Failed to fetch pending avatars:', error);
        throw error;
    }
};

// 将Base64编码的字符串转换为Blob URL
export const convertBase64ToBlobUrl = (base64Str) => {

    if (base64Str.startsWith('dataimage/jpegbase64')) {
      base64Str= base64Str.replace('dataimage/jpegbase64', '');
    } else if (base64Str.startsWith('dataimage/pngbase64')) {
      base64Str = base64Str.replace('dataimage/pngbase64', '');
    }

    const byteCharacters = atob(base64Str);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: "image/png" });
    console.log(URL.createObjectURL(blob))
    return(URL.createObjectURL(blob));
  };

  export const getUserInfo = async () => {
    try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`${API_URL}/user_info`, {
            params: { token },
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (response.status === 200) {
        
            return response.data;
        } else {
            throw new Error(`HTTP错误！状态: ${response.status}`);
        }
    } catch (error) {
        notification.error({
            message: '加载用户信息失败',
            description: '无法从服务器加载用户信息。',
        });
        console.error('获取用户信息过程中出现问题:', error);
    } finally {
      
    }
};

export const followUser = async (token, followeeId) => {
    const response = await fetch(`http://123.56.102.167:8080/follow?followeeId=${followeeId}`, {
      method: 'POST',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) {
      throw new Error('Failed to follow user');
    }
  
    return response;
  };
  
  export const isFollowing = async (token, followeeId) => {
    const response = await fetch(`http://123.56.102.167:8080/is_following?user_id=${followeeId}`, {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
  
    if (!response.ok) {
      throw new Error('Failed to check following status');
    }
  
    return response;
  };
  
  export const getFollowers = async (token) => {
    const response = await fetch('http://123.56.102.167:8080/followers', {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
  
    if (!response.ok) {
      throw new Error('Failed to fetch followers');
    }
  
    return response.json();
  };
  
  export const unfollowUser = async (token, followeeId) => {
    const response = await fetch(`http://123.56.102.167:8080/unfollow?followeeId=${followeeId}`, {
      method: 'POST',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
  
    if (!response.ok) {
      throw new Error('Failed to unfollow user');
    }
  
    return response;
  };
  
  export const getFollowees = async (token) => {
    const response = await fetch('http://123.56.102.167:8080/followees', {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
  
    if (!response.ok) {
      throw new Error('Failed to fetch followees');
    }
  
    return response.json();
  };
  
  // api/user.js

export const reserveEvent = async (token, eventId) => {
  const response = await fetch(`http://123.56.102.167:8080/events/appoint`, {
    method: 'POST',
    headers: {
      'Authorization': `${token}`,
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams({ eventId: eventId.toString() })
  });

  if (!response.ok) {
    throw new Error('Failed to reserve event');
  }

  return response.json();
};

export const getEventById = async (eventId) => {
  const response = await fetch(`http://123.56.102.167:8080/events/${eventId}`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return await response.json();
};

// 其他API调用封装...
