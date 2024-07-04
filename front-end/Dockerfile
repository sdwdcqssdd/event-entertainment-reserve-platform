# 使用Nginx作为基础镜像
FROM nginx:alpine

# 复制打包后的前端文件到Nginx的html目录
COPY build/ /usr/share/nginx/html

# 暴露Nginx的默认端口
EXPOSE 80

# 启动Nginx
CMD ["nginx", "-g", "daemon off;"]
