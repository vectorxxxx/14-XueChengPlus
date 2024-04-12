## 1、环境搭建

### 1.1、安装 linux

```bash
# 初始化一个centos7系统
vagrant init centos7 https://mirrors.ustc.edu.cn/centos-cloud/centos/7/vagrant/x86_64/images/CentOS-7.box

# 启动虚拟机
vagrant up

# 连接虚拟机
vagrant ssh

# 使用 root 账号登录
su root
vagrant

# 允许账号密码登录
vi /etc/ssh/sshd_config
# PasswordAuthentication yes
service sshd restart
```

### 1.2、安装 Docker

```bash
# 卸载旧版本
yum remove docker \
docker-client \
docker-client-latest \
docker-common \
docker-latest \
docker-latest-logrotate \
docker-logrotate \
docker-engine

# 更新缓存
yum makecache fast

# 设置阿里 docker 镜像仓库地址
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 安装必要的依赖
yum install -y yum-utils device-mapper-persistent-data lvm2

# 安装 docker 引擎
# 安装 Docker-CE（Community Edition，社区版）
yum -y install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 查看 docker 版本
docker -v

# 启动 docker
systemctl start docker
ps -ef | grep docker

# 自启动 docker
systemctl enable docker
systemctl is-enabled docker

# 配置 docker 镜像加速
mkdir -p /etc/docker
# 将JSON内容写入到 /etc/docker/daemon.json 文件中
tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": [
    "https://hub-mirror.c.163.com",
    "https://mirror.baidubce.com",
    "https://registry.docker-cn.com",
    "https://reg-mirror.qiniu.com",
    "https://dockerhub.azk8s.cn",
    "https://docker.mirrors.ustc.edu.cn"
  ]
}
EOF
# 重新加载systemd守护进程的配置文件
systemctl daemon-reload
# 重启 docker
systemctl restart docker

# 查看镜像
docker images
```



### 1.3、安装 MySQL

准备工作

```bash
mkdir -p /usr/local/src/mysql/log
mkdir -p /usr/local/src/mysql/data
mkdir -p /usr/local/src/mysql/conf.d

# 配置 MySQL
vi /usr/local/src/mysql/my.cnf
```

`/usr/local/src/mysql/my.cnf`

```bash
[mysqld]
user=mysql
character-set-server=utf8
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
expire_logs_days=7
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=1000

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8
```

创建 MySQL 实例

```bash
# 创建 MySQL 实例
docker run \
-p 3306:3306 \
--name mysql \
--restart=always \
--privileged=true \
--restart unless-stopped \
-v /usr/local/src/mysql/log:/var/log/mysql \
-v /usr/local/src/mysql/data:/var/lib/mysql \
-v /usr/local/src/mysql/my.cnf:/etc/mysql/my.cnf \
-v /usr/local/src/mysql/conf.d:/etc/mysql/conf.d \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:8.0.26
```

设置远程访问

```bash
docker exec -it mysql mysql -uroot -proot

# 查看授权情况
select user,host from user;

use mysql;
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'root';
FLUSH PRIVILEGES;
grant all on *.* to 'root'@'%';

select user,host from user;
```

### 1.4、安装 Git

```bash
yum install git -y

git --version
```

### 1.5、安装 Gogs

```bash
mkdir -p /usr/local/src/docker/gogs

docker run -d \
--name=gogs \
--restart=always \
--privileged=true \
-p 10022:22 \
-p 3000:3000 \
-v /usr/local/src/docker/gogs:/data \
gogs/gogs
```

### 1.6、安装 Nacos

```bash
mkdir -p /usr/local/src/nacos/logs

docker pull nacos/nacos-server:v2.2.3

docker run -d --name nacos-server \
-p 8848:8848 \
-p 9848:9848 \
--restart=always \
-e MODE=standalone \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_SERVICE_HOST=192.168.56.14 \
-e MYSQL_SERVICE_PORT=3306 \
-e MYSQL_SERVICE_DB_NAME=nacos_config \
-e MYSQL_SERVICE_USER=root \
-e MYSQL_SERVICE_PASSWORD=root \
-v /usr/local/src/nacos/logs:/home/nacos/logs \
nacos/nacos-server:v2.2.3
```

### 1.7、安装 MinIO

```bash
# 拉取镜像
docker pull quay.io/minio/minio

# 创建数据存储目录
mkdir -p /usr/local/src/minio/data1
mkdir -p /usr/local/src/minio/data2
mkdir -p /usr/local/src/minio/data3
mkdir -p /usr/local/src/minio/data4

# 创建minio
docker run -d \
--name minio \
-p 9000:9000 \
-p 9001:9001 \
-v /usr/local/src/minio/data1:/data1 \
-v /usr/local/src/minio/data2:/data2 \
-v /usr/local/src/minio/data3:/data3 \
-v /usr/local/src/minio/data4:/data4 \
-e "MINIO_ROOT_USER=minioadmin" \
-e "MINIO_ROOT_PASSWORD=minioadmin" \
--restart=always \
quay.io/minio/minio \
server /data1 /data2 /data3 /data4 \
--console-address ":9000" \
--address ":9001"
```

访问: [http://192.168.56.14:9001/](http://192.168.56.14:9001/)

- 账号：minioadmin
- 密码：minioadmin

### 1.8、安装 XXL-JOB

```bash
docker pull xuxueli/xxl-job-admin:2.3.1

docker run \
-e PARAMS="--spring.datasource.url=jdbc:mysql://mysql:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai --spring.datasource.username=root --spring.datasource.password=root" \
--link mysql:mysql \
-p 8080:8080 \
-v /usr/local/src/xxl-job/tmp:/data/applogs \
--name xxl-job-admin \
--restart=alw
-d xuxueli/xxl-job-admin:2.3.1
```

访问: [http://192.168.56.14:8080/xxl-job-admin](http://192.168.56.14:8080/xxl-job-admin)

- 账号：admin
- 密码：123456

## 2、启动前端项目

```bash
# 安装 cnpm
npm install -g cnpm --registry=https://registry.npm.taobao.org

# 安装依赖
cnpm i

# 启动项目
npm run serve
```



## 3、FFmpeg

```bash
# 查看版本
ffmpeg -v

# avi 转 mp4
ffmpeg -i test.avi test.mp4
# avi 转 mp3
ffmpeg -i test.avi test.mp3
# avi 转 gif
ffmpeg -i test.avi test.gif

# 
ffmpeg.exe \
-i test.avi \
-c:v libx264 \
-s 1280x720 \
-pix_fmt yuv420p \
-b:a 63k \
-b:v 753k \
-r 18 \
test.mp4
```

