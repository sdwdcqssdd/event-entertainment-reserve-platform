import CropperImage from 'antd-cropper-img';
import React, { useState } from 'react';
import { Upload,message ,Button} from 'antd';
import axios from 'axios';

const ChangeAvatar = () => {

  const [fileList, setFileList] = useState([
  ]);
  const [uploading, setUploading] = useState(false);
  const token = localStorage.getItem('token');

  const handleUpload = async () => {
    if (fileList.length === 0) {
      message.error('Please select an image first');
      return;
    }
    setUploading(true);
    try {
      const file = fileList[0].originFileObj;
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = async () => {
        const base64Image = reader.result;

        // Send base64 image to the backend
        const response = await axios.post(
          'http://123.56.102.167:8080/avatars', // Modify this URL to match your backend endpoint
          { avatarData: base64Image },
          {
            headers: {
              Authorization: token,
              'Content-Type': 'application/json',
            },
          }
        );

        if (response.status === 201) {
          message.success('头像上传成功，等待审核');
          setFileList([]);
        } else {
          message.error('Failed to upload avatar');
        }
      };
    } catch (error) {
      message.error('Failed to upload avatar');
    } finally {
      setUploading(false);
    }
  };

  const onChange = ({ fileList: newFileList }) => {
    setFileList(newFileList);
  };
  const onPreview = async (file) => {
    let src = file.url;
    if (!src) {
      src = await new Promise((resolve) => {
        const reader = new FileReader();
        reader.readAsDataURL(file.originFileObj);
        reader.onload = () => resolve(reader.result);
      });
    }
    const image = new Image();
    image.src = src;
    const imgWindow = window.open(src);
    imgWindow?.document.write(image.outerHTML);
  };
  return (
    <div style={{ maxWidth: 300, margin: '200px 400px' }}>
    <CropperImage>
      <Upload
        action="https://run.mocky.io/v3/435e224c-44fb-4773-9faf-380c5e6a2188"
        listType="picture-card"
        fileList={fileList}
        onChange={onChange}
        onPreview={onPreview}
      >
        {fileList.length < 1 && '+ Upload'}
      </Upload>
    </CropperImage>
    <Button
        type="primary"
        onClick={handleUpload}
        disabled={fileList.length === 0}
        loading={uploading}

      >上传头像</Button>
    </div>
  );
};
export default ChangeAvatar;
