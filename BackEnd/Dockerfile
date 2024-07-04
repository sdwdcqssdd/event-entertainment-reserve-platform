# 使用官方 OpenJDK 镜像作为基础镜像
FROM openjdk:17-alpine

# 设置工作目录
WORKDIR /app

# 复制JAR文件到容器中
COPY ./target/backend.jar /app/backend.jar

# 暴露应用程序运行的端口
EXPOSE 8080

# 定义运行应用程序的命令
CMD ["java", "-jar", "backend.jar"]
