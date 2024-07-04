import React, { useState } from 'react';

const ReplyForm = ({ onSubmit }) => {
    const [content, setContent] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(content);
        setContent('');
    };

    return (
        <div>
            <h2>Reply to Topic</h2>
            <form onSubmit={handleSubmit}>
                <div>
          <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              required
          ></textarea>
                </div>
                <button type="submit">Reply</button>
            </form>
        </div>
    );
};

export default ReplyForm;
