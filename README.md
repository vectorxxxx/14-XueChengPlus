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
	"registry-mirrors":["https://docker.mirrors.ustc.edu.cn"]
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
--restart=always \
-d xuxueli/xxl-job-admin:2.3.1
```

访问: [http://192.168.56.14:8080/xxl-job-admin](http://192.168.56.14:8080/xxl-job-admin)

- 账号：admin
- 密码：123456

### 1.9、安装 Nginx

```bash
mkdir -p /usr/local/src/nginx

# 主要解决报错问题：docker: Error response from daemon: failed to create task for container: failed to create shim task: OCI runtime create failed: runc create failed: unable to start container process: error during container init: error mounting "/usr/local/src/nginx/conf/nginx.conf" to rootfs at "/etc/nginx/nginx.conf": mount /usr/local/src/nginx/conf/nginx.conf:/etc/nginx/nginx.conf (via /proc/self/fd/6), flags: 0x5000: not a directory: unknown: Are you trying to mount a directory onto a file (or vice-versa)? Check if the specified host path exists and is the expected type.
# 根因：不支持直接挂载文件，只能挂载文件夹
# 随便启动一个 nginx 实例，这一步只是为了复制出配置，后面会删掉重装
docker run -p 80:80 --name nginx -d nginx:1.23.1
docker container cp nginx:/etc/nginx /usr/local/src/nginx/conf/
docker stop nginx
docker rm nginx


# 运行容器
docker run \
--name nginx \
--restart=always \
-p 80:80 \
-p 443:443 \
-v /usr/local/src/nginx/conf/conf.d:/etc/nginx/conf.d \
-v /usr/local/src/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /usr/local/src/nginx/html:/usr/share/nginx/html \
-v /usr/local/src/nginx/logs:/var/log/nginx \
-d nginx:1.23.1


# 重新加载配置文件
docker exec nginx  nginx -s reload
```

### 1.10、安装 ElasticSearch

```bash
# 下载镜像文件
docker pull elasticsearch:7.4.2

# 初始化配置
mkdir -p /usr/local/src/elasticsearch/config
mkdir -p /usr/local/src/elasticsearch/data
# 允许被所有IP来源的机器访问
echo "http.host: 0.0.0.0" >> /usr/local/src/elasticsearch/config/elasticsearch.yml
# 递归更改权限
chmod -R 777 /usr/local/src/elasticsearch/

# 运行 elasticsearch 镜像实例
# 测试环境下，必须设置ES的初始内存和最大内存，否则默认占用内存过大会启动不了ES
docker run \
--name elasticsearch \
--restart=always \
-p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms64m -Xmx512m" \
-v /usr/local/src/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /usr/local/src/elasticsearch/data:/usr/share/elasticsearch/data \
-v /usr/local/src/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:7.4.2

# 查看启动日志
docker logs elasticsearch
```

访问验证：[http://192.168.56.14:9200](http://192.168.56.14:9200)

### 1.11、安装 Kibana

```bash
# 下载镜像文件
docker pull kibana:7.4.2

# 运行 kibana 镜像实例
docker run \
--name kibana \
--restart=always \
-p 5601:5601 \
-e ELASTICSEARCH_HOSTS=http://192.168.56.14:9200 \
-d kibana:7.4.2

# 查看启动日志
docker logs kibana
```

访问验证：[http://192.168.56.14:5601](http://192.168.56.14:5601)

### 1.12、安装 ik 分词器

```bash
# 进入 elasticsearch 插件目录
cd /usr/local/src/elasticsearch/plugins/

# 下载对应版本的 ik 分词器压缩包
yum install wget -y
wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.4.2/elasticsearch-analysis-ik-7.4.2.zip

# 解压
yum install unzip -y
unzip elasticsearch-analysis-ik-7.4.2.zip
rm -rf *.zip

# 移至 ik 目录下并赋权限
mkdir ik
mv * ik/
chmod -R 777 ik/

# 以交互模式进入 elasticsearch 容器的命令行中
docker exec -it elasticsearch /bin/bash

# 运行 elasticsearch-plugin
cd /bin
elasticsearch-plugin

# 查看插件是否已安装
elasticsearch-plugin list

# 重启 elasticsearch 容器
exit;
docker restart elasticsearch
```

测试

```bash
POST _analyze
{
  "analyzer": "ik_smart", 
  "text": "我是中国人"
}
```

### 1.13、安装 NVM

#### 安装

```bash
# 卸载npm 
npm uninstall npm -g
 
# 卸载node
yum remove nodejs npm -y

#清除残留文件
cd /usr/local/lib && rm -rf node*
cd /usr/local/include && rm -rf node*
cd /usr/local/bin && rm node*

# 删除nvm
rm -rf ~/.nvm
# 删除npm
rm -rf ~/.npm

# 如果执行下面命令报错找不到对应的可执行文件，即表示卸载成功
which nvm
which npm
which node


# 二选一（推荐第二种）
curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.1/install.sh | bash
wget -qO- https://raw.githubusercontent.com/creationix/nvm/v0.33.1/install.sh | bash

# 加载环境变量
source ~/.bashrc

# 修改镜像源
#在~/.bashrc里面加入如下，设置淘宝源
export NVM_NODEJS_ORG_MIRROR=https://npmmirror.com/mirrors/node/
```

#### 常用命令

```bash
# 查看 nvm 版本
nvm --version

# 查看远程的node可用版本
nvm list-remote

# 安装 node 最新版本
nvm install node
 
# 安装一个指定版本的nodejs
nvm install v16.17.0
 
# 卸载指定版本的nodejs
nvm uninstall  v16.17.0

# 查看本地可用的nodejs版本
nvm ls
nvm list

# 使用指定版本的 node.js
nvm use v16.17.0

# 查看当前指向的nodejs版本
nvm current
 
# 指定node默认版本
nvm alias default v16.17.0
```



## 2、NPM

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



## 4、ElasticSearch

### 4.1、查看所有索引

```bash
#查看所有索引
GET /_cat/indices?v
```

### 4.2、创建索引

```bash
# 创建索引，并指定Mapping
PUT /course-publish
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "companyId": {
        "type": "keyword"
      },
      "companyName": {
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart",
        "type": "text"
      },
      "name": {
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart",
        "type": "text"
      },
      "users": {
        "index": false,
        "type": "text"
      },
      "tags": {
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart",
        "type": "text"
      },
      "mt": {
        "type": "keyword"
      },
      "mtName": {
        "type": "keyword"
      },
      "st": {
        "type": "keyword"
      },
      "stName": {
        "type": "keyword"
      },
      "grade": {
        "type": "keyword"
      },
      "teachmode": {
        "type": "keyword"
      },
      "pic": {
        "index": false,
        "type": "text"
      },
      "description": {
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart",
        "type": "text"
      },
      "createDate": {
        "format": "yyyy-MM-dd HH:mm:ss",
        "type": "date"
      },
      "status": {
        "type": "keyword"
      },
      "remark": {
        "index": false,
        "type": "text"
      },
      "charge": {
        "type": "keyword"
      },
      "price": {
        "type": "scaled_float",
        "scaling_factor": 100
      },
      "originalPrice": {
        "type": "scaled_float",
        "scaling_factor": 100
      },
      "validDays": {
        "type": "integer"
      }
    }
  }
}
```

### 4.3、查询索引结构

```bash
# 查询索引结构
GET /course-publish/_mapping
```

### 4.4、删除索引

```bash
# 删除索引
DELETE /course-publish
```



## FAQ

### 1）系统时间、硬件时间不一致导致的问题

**问题现象**

- 向MinIO上传文件时，抛出异常：The difference between the request time and the server's time is too large.
- docker报错: Get https://registry-1.docker.io/v2/: net/http: TLS handshake timeout

**解决方案**

```bash
# 查看当前系统的本地时间
date
# 查看硬件时钟（RTC），即BIOS中的实时时钟
hwclock
# 查看系统时间、硬件时钟设置，以及时区等信息
timedatectl

# 设置时区为 Asia/Shanghai
timedatectl set-timezone Asia/Shanghai
# 使用本地时间来存储硬件时钟的值，而不是UTC时间
timedatectl set-local-rtc 1

# 安装和配置 ntpdate 服务
yum install -y ntpdate
systemctl enable ntpdate
systemctl is-enabled ntpdate
systemctl status ntpdate
systemctl start ntpdate

# 手动同步时间
ntpdate pool.ntp.org

# 自动同步时间
crontab -e
# 每10分钟同步一次
*/10 * * * *  /usr/sbin/ntpdate -u pool.ntp.org >/dev/null 2>&1
# 重启服务
service crond restart
```

### 2）信号量与锁有何区别

信号量和锁都是用来控制多线程访问共享资源的工具，但它们有一些重要的区别：

1. 作用对象不同：信号量主要用于控制对一组资源的访问，可以允许多个线程同时访问资源；而锁主要用于控制对单个资源的访问，一次只能有一个线程访问资源。

2. 控制方式不同：信号量通常用于控制资源的数量，当资源数量为0时，等待的线程会被阻塞；而锁主要用于控制资源的访问权，当资源被锁定时，其他线程需要等待锁释放。

3. 使用场景不同：信号量适用于资源数量有限的情况，如连接池、缓冲区等；而锁适用于资源访问顺序敏感的情况，如生产者消费者模型、临界区等。

总的来说，信号量更加灵活，可以用于控制资源的数量和访问顺序；而锁更加简单直接，适用于控制资源的访问权。在实际应用中，可以根据具体需求选择信号量或锁来实现线程同步。

举个例子，当我们去餐厅吃饭时，可以用信号量和锁来做一个比喻：

1. 作用对象不同：假设餐厅有10张餐桌，信号量可以控制让10个人同时进入餐厅就座，每个人可以选择自己喜欢的餐桌；而锁则是用来控制每张餐桌的使用权，一旦有人坐在某张餐桌上，其他人就需要等待这个人吃完才能使用这张餐桌。

2. 控制方式不同：信号量就像是餐厅门口的排队队列，如果餐厅里有空位，队列里的人就可以进入就座；而锁就像是餐桌上的预订牌，一旦有人预订了某张餐桌，其他人就需要等待这个人用餐完毕才能使用这张餐桌。

3. 使用场景不同：信号量适用于控制餐厅内总人数，确保不超过餐桌数量；而锁适用于控制每张餐桌的使用权，保证每张餐桌只能被一个人使用。

通过这个比喻，你可以更直观地理解信号量和锁的区别：信号量控制资源的数量和访问顺序，而锁控制资源的访问权。
