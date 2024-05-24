/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.26 : Database - xcplus_media
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`xcplus_media` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `xcplus_media`;

/*Table structure for table `media_files` */

DROP TABLE IF EXISTS `media_files`;

CREATE TABLE `media_files` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件id,md5值',
  `company_id` bigint DEFAULT NULL COMMENT '机构ID',
  `company_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机构名称',
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `file_type` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件类型（图片、文档，视频）',
  `tags` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签',
  `bucket` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '存储目录',
  `file_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '存储路径',
  `file_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件id',
  `url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '媒资文件访问地址',
  `username` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上传人',
  `create_date` datetime DEFAULT NULL COMMENT '上传时间',
  `change_date` datetime DEFAULT NULL COMMENT '修改时间',
  `status` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '1' COMMENT '状态,1:正常，0:不展示',
  `remark` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `audit_status` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核状态',
  `audit_mind` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核意见',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_fileid` (`file_id`) USING BTREE COMMENT '文件id唯一索引 '
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='媒资信息';

/*Data for the table `media_files` */

insert  into `media_files`(`id`,`company_id`,`company_name`,`filename`,`file_type`,`tags`,`bucket`,`file_path`,`file_id`,`url`,`username`,`create_date`,`change_date`,`status`,`remark`,`audit_status`,`audit_mind`,`file_size`) values ('0401bb18ffc89b7eda7de2e8aaba4f73',1232141425,NULL,'110、全文检索-ElasticSearch-进阶-两种查询方式.avi','001002','课程视频','video','0/4/0401bb18ffc89b7eda7de2e8aaba4f73/0401bb18ffc89b7eda7de2e8aaba4f73.avi','0401bb18ffc89b7eda7de2e8aaba4f73','/video/0/4/0401bb18ffc89b7eda7de2e8aaba4f73/0401bb18ffc89b7eda7de2e8aaba4f73.mp4',NULL,'2024-04-12 22:26:53',NULL,'1','','002003',NULL,53875774),('0dbb928c1c8ccc21f1e0db17ec332da3',1232141425,NULL,'4-6面试指导与作业.mp4','001002','课程视频','video','0/d/0dbb928c1c8ccc21f1e0db17ec332da3/0dbb928c1c8ccc21f1e0db17ec332da3.mp4','0dbb928c1c8ccc21f1e0db17ec332da3','/video/0/d/0dbb928c1c8ccc21f1e0db17ec332da3/0dbb928c1c8ccc21f1e0db17ec332da3.mp4',NULL,'2024-04-12 09:28:05',NULL,'1','','002003',NULL,33687670),('0f17b84e14410630b4b3dda30415ba5c',1232141425,NULL,'131、商城业务-商品上架-构造sku检索属性.avi','001002','课程视频','video','0/f/0f17b84e14410630b4b3dda30415ba5c/0f17b84e14410630b4b3dda30415ba5c.avi','0f17b84e14410630b4b3dda30415ba5c','/video/0/f/0f17b84e14410630b4b3dda30415ba5c/0f17b84e14410630b4b3dda30415ba5c.mp4',NULL,'2024-04-12 22:25:55',NULL,'1','','002003',NULL,51211218),('1137f04b2f44d1b2c37bcb73608864da',1232141425,NULL,'course29943168382846755.html','001003',NULL,'mediafiles','course/18.html','1137f04b2f44d1b2c37bcb73608864da',NULL,NULL,'2022-12-18 12:21:32',NULL,'1',NULL,'002003',NULL,34210),('13376223c8cf313491d1d46807f09d7c',1232141425,NULL,'4-5阿里系业务的供应链系统-如何评估子类业务的外包与采购.mp4','001002','课程视频','video','1/3/13376223c8cf313491d1d46807f09d7c/13376223c8cf313491d1d46807f09d7c.mp4','13376223c8cf313491d1d46807f09d7c','/video/1/3/13376223c8cf313491d1d46807f09d7c/13376223c8cf313491d1d46807f09d7c.mp4',NULL,'2024-04-12 09:26:18',NULL,'1','','002003',NULL,47859236),('1435c23a534b851d37568f39f2c1d318',1232141425,NULL,'21.2.1. 内容提要.mp4','001002','课程视频','video','1/4/1435c23a534b851d37568f39f2c1d318/1435c23a534b851d37568f39f2c1d318.mp4','1435c23a534b851d37568f39f2c1d318','/video/1/4/1435c23a534b851d37568f39f2c1d318/1435c23a534b851d37568f39f2c1d318.mp4',NULL,'2024-04-13 13:39:00',NULL,'1','','002003',NULL,11611505),('1580180577525002241',1232141425,NULL,'1.jpg','001003',NULL,'mediafiles',NULL,'8383a8c2c1d098fcc07da7d6e79ae31e','/mediafiles/2022/10/12/8383a8c2c1d098fcc07da7d6e79ae31e.jpg',NULL,'2022-10-12 20:56:23',NULL,'1',NULL,NULL,NULL,5767),('18f919e23bedab97a78762c4875addc4',1232141425,NULL,'垂直分库-插入和查询测试.avi','001002','课程视频','video','1/8/18f919e23bedab97a78762c4875addc4/18f919e23bedab97a78762c4875addc4.avi','18f919e23bedab97a78762c4875addc4',NULL,NULL,'2022-12-15 09:45:18',NULL,'1',NULL,'002003',NULL,16305152),('1b3d92819b255680c5144c42e10226d8',1232141425,NULL,'92_尚硅谷_MySQL基础_from后面的子查询使用.avi','001002','课程视频','video','1/b/1b3d92819b255680c5144c42e10226d8/1b3d92819b255680c5144c42e10226d8.avi','1b3d92819b255680c5144c42e10226d8','/video/1/b/1b3d92819b255680c5144c42e10226d8/1b3d92819b255680c5144c42e10226d8.mp4',NULL,'2024-04-15 23:58:23',NULL,'1','','002003',NULL,21485368),('1d0f0e6ed8a0c4a89bfd304b84599d9c',1232141425,NULL,'asset-icoGather.png','001001',NULL,'mediafiles','2022/09/20/1d0f0e6ed8a0c4a89bfd304b84599d9c.png','1d0f0e6ed8a0c4a89bfd304b84599d9c','/mediafiles/2022/09/20/1d0f0e6ed8a0c4a89bfd304b84599d9c.png',NULL,'2022-09-20 21:21:28',NULL,'1','','002003',NULL,8059),('1f229319d6fed3431d2f9d06193a433b',1232141425,NULL,'01-分布式事务专题课程介绍.avi','001002','课程视频','video','1/f/1f229319d6fed3431d2f9d06193a433b/1f229319d6fed3431d2f9d06193a433b.avi','1f229319d6fed3431d2f9d06193a433b','/video/1/f/1f229319d6fed3431d2f9d06193a433b/1f229319d6fed3431d2f9d06193a433b.mp4',NULL,'2022-09-14 18:30:02',NULL,'1','','002003',NULL,14705152),('20ab4f3f532f6bfc96bb62dd50da2026',1232141425,NULL,'4-2技术助力业务的两个方向.mp4','001002','课程视频','video','2/0/20ab4f3f532f6bfc96bb62dd50da2026/20ab4f3f532f6bfc96bb62dd50da2026.mp4','20ab4f3f532f6bfc96bb62dd50da2026','/video/2/0/20ab4f3f532f6bfc96bb62dd50da2026/20ab4f3f532f6bfc96bb62dd50da2026.mp4',NULL,'2024-04-12 21:49:46',NULL,'1','','002003',NULL,18328152),('23f83ae728bd1269eee7ea2236e79644',1232141425,NULL,'16-Nacos配置管理-课程总结.avi','001002','课程视频','video','2/3/23f83ae728bd1269eee7ea2236e79644/23f83ae728bd1269eee7ea2236e79644.avi','23f83ae728bd1269eee7ea2236e79644','/video/2/3/23f83ae728bd1269eee7ea2236e79644/23f83ae728bd1269eee7ea2236e79644.mp4',NULL,'2022-09-14 18:21:44',NULL,'1','','002003',NULL,26053632),('287cdd91c5d444e0752b626cbd95b41c',1232141425,NULL,'nacos01.mp4','001002','课程视频','video','2/8/287cdd91c5d444e0752b626cbd95b41c/287cdd91c5d444e0752b626cbd95b41c.mp4','287cdd91c5d444e0752b626cbd95b41c','/video/2/8/287cdd91c5d444e0752b626cbd95b41c/287cdd91c5d444e0752b626cbd95b41c.mp4',NULL,'2022-09-14 18:28:43',NULL,'1','','002003',NULL,25953447),('29c2e95ffd089918757febd95b840eea',1232141425,NULL,'110_尚硅谷_MySQL基础_【案例讲解】数据的增删改.avi','001002','课程视频','video','2/9/29c2e95ffd089918757febd95b840eea/29c2e95ffd089918757febd95b840eea.avi','29c2e95ffd089918757febd95b840eea','/video/2/9/29c2e95ffd089918757febd95b840eea/29c2e95ffd089918757febd95b840eea.mp4',NULL,'2024-04-15 23:58:52',NULL,'1','','002003',NULL,47716804),('2b3ef2c7a1909980d8270256d5bb81b1',1232141425,NULL,'4-1本章导航.mp4','001002','课程视频','video','2/b/2b3ef2c7a1909980d8270256d5bb81b1/2b3ef2c7a1909980d8270256d5bb81b1.mp4','2b3ef2c7a1909980d8270256d5bb81b1','/video/2/b/2b3ef2c7a1909980d8270256d5bb81b1/2b3ef2c7a1909980d8270256d5bb81b1.mp4',NULL,'2024-04-12 21:49:39',NULL,'1','','002003',NULL,10205933),('31fb977c7917d351da3f080ad8b2c841',1232141425,NULL,'course_template.html','001001',NULL,'mediafiles','2024/04/22/31fb977c7917d351da3f080ad8b2c841.html','31fb977c7917d351da3f080ad8b2c841','/mediafiles/2024/04/22/31fb977c7917d351da3f080ad8b2c841.html',NULL,'2024-04-22 17:09:30',NULL,'1',NULL,'002003',NULL,40213),('3250cc347cbafb4b17bdf12be90521a5',1232141425,NULL,'course6917847582234754518.html','001001',NULL,'mediafiles','course/22.html','3250cc347cbafb4b17bdf12be90521a5','/mediafiles/course/22.html',NULL,'2024-04-23 17:10:29',NULL,'1',NULL,'002003',NULL,40016),('33c643206bb7c08e2cb99b622d7a1b63',1232141425,NULL,'1.png','001001',NULL,'mediafiles','2022/10/07/33c643206bb7c08e2cb99b622d7a1b63.png','33c643206bb7c08e2cb99b622d7a1b63','/mediafiles/2022/10/07/33c643206bb7c08e2cb99b622d7a1b63.png',NULL,'2022-10-07 06:20:05',NULL,'1','','002003',NULL,169788),('345db593849aada5675ed1e438650eeb',1232141425,NULL,'1.png','001001',NULL,'mediafiles','2022/10/07/345db593849aada5675ed1e438650eeb.png','345db593849aada5675ed1e438650eeb','/mediafiles/2022/10/07/345db593849aada5675ed1e438650eeb.png',NULL,'2022-10-07 09:31:46',NULL,'1','','002003',NULL,70171),('3a5a861d1c745d05166132c47b44f9e4',1232141425,NULL,'01-Nacos配置管理-内容介绍.avi','001002','课程视频','video','3/a/3a5a861d1c745d05166132c47b44f9e4/3a5a861d1c745d05166132c47b44f9e4.avi','3a5a861d1c745d05166132c47b44f9e4','/video/3/a/3a5a861d1c745d05166132c47b44f9e4/3a5a861d1c745d05166132c47b44f9e4.mp4',NULL,'2022-09-14 18:19:24',NULL,'1','','002003',NULL,23839232),('3fb1d9a565cb92f395f384bd62ef24cd',1232141425,NULL,'1614759607876_0.png','001001','课程图片','mediafiles','2022/09/20/3fb1d9a565cb92f395f384bd62ef24cd.png','3fb1d9a565cb92f395f384bd62ef24cd','/mediafiles/2022/09/20/3fb1d9a565cb92f395f384bd62ef24cd.png',NULL,'2022-09-20 21:06:11',NULL,'1','','002003',NULL,58873),('43aab98976787e7c47805a118726b6b2',1232141425,NULL,'250、商城业务-消息队列-RabbitMQ安装.avi','001002','课程视频','video','4/3/43aab98976787e7c47805a118726b6b2/43aab98976787e7c47805a118726b6b2.avi','43aab98976787e7c47805a118726b6b2','/video/4/3/43aab98976787e7c47805a118726b6b2/43aab98976787e7c47805a118726b6b2.mp4',NULL,'2024-04-12 22:28:30',NULL,'1','','002003',NULL,58990386),('4f4d9a40117f1c847d8f4e13e1bc4060',1232141425,NULL,'4-3【案例分析】阿里新零售部门如何培养技术团队的业务知识.mp4','001002','课程视频','video','4/f/4f4d9a40117f1c847d8f4e13e1bc4060/4f4d9a40117f1c847d8f4e13e1bc4060.mp4','4f4d9a40117f1c847d8f4e13e1bc4060','/video/4/f/4f4d9a40117f1c847d8f4e13e1bc4060/4f4d9a40117f1c847d8f4e13e1bc4060.mp4',NULL,'2024-04-12 21:50:03',NULL,'1','','002003',NULL,82631788),('500598cae139f77c1bb54459909e0443',1232141425,NULL,'course8561649859933456434.html','001003',NULL,'mediafiles','course/119.html','500598cae139f77c1bb54459909e0443','/mediafiles/course/119.html',NULL,'2022-10-07 09:39:49',NULL,'1','','002003',NULL,35652),('538bd3d652593b8df70d84e643b12842',1232141425,NULL,'course6941513291436463735.html','001003',NULL,'mediafiles','course/121.html','538bd3d652593b8df70d84e643b12842',NULL,NULL,'2023-02-09 11:33:18',NULL,'1',NULL,'002003',NULL,36292),('56f415261643c59963fa482709e30d23',1232141425,NULL,'course1602318288921576697.html','001001',NULL,'mediafiles','course/28.html','56f415261643c59963fa482709e30d23','/mediafiles/course/28.html',NULL,'2024-04-23 17:16:58',NULL,'1',NULL,'002003',NULL,41794),('5878a684ee9a36daae5d0741aaca0747',1232141425,NULL,'Spring Security集成测试','001002',NULL,'video','5/8/5878a684ee9a36daae5d0741aaca0747/5878a684ee9a36daae5d0741aaca0747.avi','5878a684ee9a36daae5d0741aaca0747',NULL,NULL,'2022-10-16 15:30:17',NULL,'1',NULL,'002003',NULL,NULL),('5f7657549f4bd16d08b56ccad794f01a',1232141425,NULL,'R-C.jpg','001001',NULL,'mediafiles','2024/04/23/5f7657549f4bd16d08b56ccad794f01a.jpg','5f7657549f4bd16d08b56ccad794f01a','/mediafiles/2024/04/23/5f7657549f4bd16d08b56ccad794f01a.jpg',NULL,'2024-04-23 17:02:02',NULL,'1',NULL,'002003',NULL,82013),('6ad24a762f67c18f61966c1b8c55abe6',1232141425,NULL,'07-分布式事务基础理论-BASE理论.avi','001002','课程视频','video','6/a/6ad24a762f67c18f61966c1b8c55abe6/6ad24a762f67c18f61966c1b8c55abe6.avi','6ad24a762f67c18f61966c1b8c55abe6','/video/6/a/6ad24a762f67c18f61966c1b8c55abe6/6ad24a762f67c18f61966c1b8c55abe6.mp4',NULL,'2022-09-14 18:30:16',NULL,'1','','002003',NULL,13189632),('70a98b4a2fffc89e50b101f959cc33ca',1232141425,NULL,'22-Hmily实现TCC事务-开发bank2的confirm方法.avi','001002','课程视频','video','7/0/70a98b4a2fffc89e50b101f959cc33ca/70a98b4a2fffc89e50b101f959cc33ca.avi','70a98b4a2fffc89e50b101f959cc33ca','/video/7/0/70a98b4a2fffc89e50b101f959cc33ca/70a98b4a2fffc89e50b101f959cc33ca.mp4',NULL,'2022-09-14 18:30:52',NULL,'1','','002003',NULL,18444288),('74b386417bb9f3764009dc94068a5e44',1232141425,NULL,'test2.html','001003',NULL,'mediafiles','course/74b386417bb9f3764009dc94068a5e44.html','74b386417bb9f3764009dc94068a5e44','/mediafiles/course/74b386417bb9f3764009dc94068a5e44.html',NULL,'2022-09-20 21:56:02',NULL,'1','','002003',NULL,40124),('757891eae4473e7ba78827656b1ccacf',1232141425,NULL,'studyuser.png','001001',NULL,'mediafiles',NULL,'757891eae4473e7ba78827656b1ccacf','/mediafiles/2022/10/13/757891eae4473e7ba78827656b1ccacf.png',NULL,'2022-10-13 19:57:01',NULL,'1',NULL,'002003',NULL,4922),('8026f17cf7b8697eccec2c8406d0c96c',1232141425,NULL,'nacos.png','001001',NULL,'mediafiles','2022/10/04/8026f17cf7b8697eccec2c8406d0c96c.png','8026f17cf7b8697eccec2c8406d0c96c','/mediafiles/2022/10/04/8026f17cf7b8697eccec2c8406d0c96c.png',NULL,'2022-10-04 18:55:01',NULL,'1','','002003',NULL,128051),('809694a6a974c35e3a36f36850837d7c',1232141425,NULL,'1.avi','001002','课程视频','video',NULL,'809694a6a974c35e3a36f36850837d7c','/video/8/0/809694a6a974c35e3a36f36850837d7c/809694a6a974c35e3a36f36850837d7c.avi',NULL,'2022-10-13 21:27:14',NULL,'1','','002003',NULL,NULL),('81d7ed5153316f5774885d3b4c07d8bc',1232141425,NULL,'Spring Security快速上手-创建工程.avi','001002','课程视频','video','8/1/81d7ed5153316f5774885d3b4c07d8bc/81d7ed5153316f5774885d3b4c07d8bc.avi','81d7ed5153316f5774885d3b4c07d8bc',NULL,NULL,'2022-12-15 09:41:50',NULL,'1',NULL,'002003',NULL,19945472),('89f01d62309041188f8dfa8b953f1148',1232141425,NULL,'4-4如何围绕业务特点制定技术发展路线-阿里系和抖音案例.mp4','001002','课程视频','video','8/9/89f01d62309041188f8dfa8b953f1148/89f01d62309041188f8dfa8b953f1148.mp4','89f01d62309041188f8dfa8b953f1148','/video/8/9/89f01d62309041188f8dfa8b953f1148/89f01d62309041188f8dfa8b953f1148.mp4',NULL,'2024-04-12 21:50:06',NULL,'1','','002003',NULL,37234715),('8bbacbdc97eb5a6c6ef6bf0c6b4fde5d',1232141425,NULL,'course8910673733099648215.html','001001',NULL,'mediafiles','course/126.html','8bbacbdc97eb5a6c6ef6bf0c6b4fde5d','/mediafiles/course/126.html',NULL,'2024-04-23 09:00:49',NULL,'1',NULL,'002003',NULL,40565),('9b0a355a0a954fdb3671998b4b016474',1232141425,NULL,'test.html','001003',NULL,'mediafiles','course/test.html','9b0a355a0a954fdb3671998b4b016474',NULL,NULL,'2022-12-17 17:04:40',NULL,'1',NULL,'002003',NULL,34174),('a16da7a132559daf9e1193166b3e7f52',1232141425,NULL,'1.jpg','001003',NULL,'mediafiles','2022/09/20/a16da7a132559daf9e1193166b3e7f52.jpg','a16da7a132559daf9e1193166b3e7f52','/mediafiles/2022/09/20/a16da7a132559daf9e1193166b3e7f52.jpg',NULL,'2022-09-20 21:26:08',NULL,'1','','002003',NULL,248329),('a61805e1360ab946def5471aaefc0a98',1232141425,NULL,'teacherpic.jpg','001001',NULL,'mediafiles','2022/12/18/a61805e1360ab946def5471aaefc0a98.jpg','a61805e1360ab946def5471aaefc0a98','/mediafiles/2022/12/18/a61805e1360ab946def5471aaefc0a98.jpg',NULL,'2022-12-18 12:10:52',NULL,'1',NULL,'002003',NULL,11600),('a6fb4fc7faf1d3d0831819969529b888',1232141425,NULL,'1.项目背景.mp4','001002','课程视频','video',NULL,'a6fb4fc7faf1d3d0831819969529b888','/video/a/6/a6fb4fc7faf1d3d0831819969529b888/a6fb4fc7faf1d3d0831819969529b888.mp4',NULL,'2022-10-13 21:30:17',NULL,'1','','002003',NULL,NULL),('b2deb4a098bb12653c57bbaa0099697a',1232141425,NULL,'course3448922126748441722.html','001003',NULL,'mediafiles','course/117.html','b2deb4a098bb12653c57bbaa0099697a','/mediafiles/course/117.html',NULL,'2022-10-04 19:20:01',NULL,'1','','002003',NULL,37705),('b74ac442030572650b1d73e365911896',1232141425,NULL,'21.2.18. 响应式WEB设计.mp4','001002','课程视频','video','b/7/b74ac442030572650b1d73e365911896/b74ac442030572650b1d73e365911896.mp4','b74ac442030572650b1d73e365911896','/video/b/7/b74ac442030572650b1d73e365911896/b74ac442030572650b1d73e365911896.mp4',NULL,'2024-04-13 12:51:47',NULL,'1','','002003',NULL,10506746),('c00baee1c4305d76da3e478d4e184c51',1232141425,NULL,'106_尚硅谷_MySQL基础_修改多表的记录 .avi','001002','课程视频','video','c/0/c00baee1c4305d76da3e478d4e184c51/c00baee1c4305d76da3e478d4e184c51.avi','c00baee1c4305d76da3e478d4e184c51','/video/c/0/c00baee1c4305d76da3e478d4e184c51/c00baee1c4305d76da3e478d4e184c51.mp4',NULL,'2024-04-15 23:58:47',NULL,'1','','002003',NULL,58338576),('c051fe97e672dd399902a90f4ac67962',1232141425,NULL,'widget-3.jpg','001001',NULL,'mediafiles','2022/12/18/c051fe97e672dd399902a90f4ac67962.jpg','c051fe97e672dd399902a90f4ac67962','/mediafiles/2022/12/18/c051fe97e672dd399902a90f4ac67962.jpg',NULL,'2022-12-18 12:02:29',NULL,'1',NULL,'002003',NULL,62469),('ca1d75b0a37b85fafd5f2e443f6f3f01',1232141425,NULL,'course2996275631019924973.html','001003',NULL,'mediafiles','course/118.html','ca1d75b0a37b85fafd5f2e443f6f3f01','/mediafiles/course/118.html',NULL,'2022-10-07 06:40:51',NULL,'1','','002003',NULL,35905),('ce01d1928a0da1a21a2ef4e1e2d2ecfc',1232141425,NULL,'5210240e7e6d488734e466b7e16337be_1.jpg','001001',NULL,'mediafiles','2024/04/11/ce01d1928a0da1a21a2ef4e1e2d2ecfc.jpg','ce01d1928a0da1a21a2ef4e1e2d2ecfc','/mediafiles/2024/04/11/ce01d1928a0da1a21a2ef4e1e2d2ecfc.jpg',NULL,'2024-04-11 15:35:11',NULL,'1',NULL,'002003',NULL,35655),('d41d8cd98f00b204e9800998ecf8427e',1232141425,NULL,'1.jpg','001001',NULL,'mediafiles','2024/04/11/d41d8cd98f00b204e9800998ecf8427e.jpg','d41d8cd98f00b204e9800998ecf8427e','/mediafiles/2024/04/11/d41d8cd98f00b204e9800998ecf8427e.jpg',NULL,'2024-04-11 15:33:14',NULL,'1',NULL,'002003',NULL,0),('d4af959873182feb0fdb91dc6c1958b5',1232141425,NULL,'widget-5.png','001001','课程图片','mediafiles','2022/09/18/d4af959873182feb0fdb91dc6c1958b5.png','d4af959873182feb0fdb91dc6c1958b5','/mediafiles/2022/09/18/d4af959873182feb0fdb91dc6c1958b5.png',NULL,'2022-09-18 21:49:55',NULL,'1','','002003',NULL,17799),('d90f97f5e2870dafac9095d0a134bfce',1232141425,NULL,'246、商城业务-购物车-改变购物项数量.avi','001002','课程视频','video','d/9/d90f97f5e2870dafac9095d0a134bfce/d90f97f5e2870dafac9095d0a134bfce.avi','d90f97f5e2870dafac9095d0a134bfce','/video/d/9/d90f97f5e2870dafac9095d0a134bfce/d90f97f5e2870dafac9095d0a134bfce.mp4',NULL,'2024-04-12 22:27:16',NULL,'1','','002003',NULL,53012702),('db4e24f27d78ccac14401b7479b35c26',1232141425,NULL,'nonepic.jpg','001001',NULL,'mediafiles','2022/09/23/db4e24f27d78ccac14401b7479b35c26.jpg','db4e24f27d78ccac14401b7479b35c26','/mediafiles/2022/09/23/db4e24f27d78ccac14401b7479b35c26.jpg',NULL,'2022-09-23 18:27:26',NULL,'1','','002003',NULL,6919),('df39983fcad83a6ceef14bfeeb1bc523',1232141425,NULL,'add.jpg','001001',NULL,'mediafiles','2022/09/20/df39983fcad83a6ceef14bfeeb1bc523.jpg','df39983fcad83a6ceef14bfeeb1bc523','/mediafiles/2022/09/20/df39983fcad83a6ceef14bfeeb1bc523.jpg',NULL,'2022-09-20 21:13:59',NULL,'1','','002003',NULL,10487),('e00ce88f53cc391d9ffd51a81834d2af',1232141425,NULL,'widget-1.jpg','001001','课程图片','mediafiles','2022/09/18/e00ce88f53cc391d9ffd51a81834d2af.jpg','e00ce88f53cc391d9ffd51a81834d2af','/mediafiles/2022/09/18/e00ce88f53cc391d9ffd51a81834d2af.jpg',NULL,'2022-09-18 21:48:50',NULL,'1','','002003',NULL,71386),('e726b71ba99c70e8c9d2850c2a7019d7',1232141425,NULL,'asset-login_img.jpg','001001',NULL,'mediafiles','2022/09/20/e726b71ba99c70e8c9d2850c2a7019d7.jpg','e726b71ba99c70e8c9d2850c2a7019d7','/mediafiles/2022/09/20/e726b71ba99c70e8c9d2850c2a7019d7.jpg',NULL,'2022-09-20 21:44:50',NULL,'1','','002003',NULL,22620),('ef29eb93515e32f2d897956d5d914db7',1232141425,NULL,'Snipaste_2023-02-09_11-06-52.png','001001',NULL,'mediafiles','2023/02/09/ef29eb93515e32f2d897956d5d914db7.png','ef29eb93515e32f2d897956d5d914db7','/mediafiles/2023/02/09/ef29eb93515e32f2d897956d5d914db7.png',NULL,'2023-02-09 11:07:02',NULL,'1',NULL,'002003',NULL,327814),('efd2eacc4485946fc27feb0caef7506c',1232141425,NULL,'读写分离-理解读写分离.avi','001002','课程视频','video','e/f/efd2eacc4485946fc27feb0caef7506c/efd2eacc4485946fc27feb0caef7506c.avi','efd2eacc4485946fc27feb0caef7506c',NULL,NULL,'2022-12-15 09:45:19',NULL,'1',NULL,'002003',NULL,14879232),('f99ba626feafe8e66584d29b60d217c6',1232141425,NULL,'155、缓存-缓存使用-缓存击穿、穿透、雪崩.avi','001002','课程视频','video','f/9/f99ba626feafe8e66584d29b60d217c6/f99ba626feafe8e66584d29b60d217c6.avi','f99ba626feafe8e66584d29b60d217c6','/video/f/9/f99ba626feafe8e66584d29b60d217c6/f99ba626feafe8e66584d29b60d217c6.avi',NULL,'2024-04-12 22:26:40',NULL,'1','','002003',NULL,54723880),('fbb57de7001cccf1e28fbe34c7506ddc',1232141425,NULL,'asset-logo.png','001001',NULL,'mediafiles','2022/09/20/fbb57de7001cccf1e28fbe34c7506ddc.png','fbb57de7001cccf1e28fbe34c7506ddc','/mediafiles/2022/09/20/fbb57de7001cccf1e28fbe34c7506ddc.png',NULL,'2022-09-20 21:55:25',NULL,'1','','002003',NULL,4355),('fd43fa560fe907a822cd4c68bf7e1967',1232141425,NULL,'course2713729056722014048.html','001001',NULL,'mediafiles','course/130.html','fd43fa560fe907a822cd4c68bf7e1967','/mediafiles/course/130.html',NULL,'2024-04-29 09:42:48',NULL,'1',NULL,'002003',NULL,40225);

/*Table structure for table `media_process` */

DROP TABLE IF EXISTS `media_process`;

CREATE TABLE `media_process` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_id` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件标识',
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `bucket` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储桶',
  `file_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '存储路径',
  `status` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态,1:未处理，2：处理成功  3处理失败',
  `create_date` datetime NOT NULL COMMENT '上传时间',
  `finish_date` datetime DEFAULT NULL COMMENT '完成时间',
  `fail_count` int DEFAULT '0' COMMENT '失败次数',
  `url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '媒资文件访问地址',
  `errormsg` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_fileid` (`file_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

/*Data for the table `media_process` */

insert  into `media_process`(`id`,`file_id`,`filename`,`bucket`,`file_path`,`status`,`create_date`,`finish_date`,`fail_count`,`url`,`errormsg`) values (1,'2b3ef2c7a1909980d8270256d5bb81b1','4-1本章导航.mp4','video','2/b/2b3ef2c7a1909980d8270256d5bb81b1/2b3ef2c7a1909980d8270256d5bb81b1.mp4','3','2024-04-12 21:59:18',NULL,3,NULL,''),(2,'20ab4f3f532f6bfc96bb62dd50da2026','4-2技术助力业务的两个方向.mp4','video','2/0/20ab4f3f532f6bfc96bb62dd50da2026/20ab4f3f532f6bfc96bb62dd50da2026.mp4','3','2024-04-12 21:59:52',NULL,3,NULL,''),(3,'4f4d9a40117f1c847d8f4e13e1bc4060','4-3【案例分析】阿里新零售部门如何培养技术团队的业务知识.mp4','video','4/f/4f4d9a40117f1c847d8f4e13e1bc4060/4f4d9a40117f1c847d8f4e13e1bc4060.mp4','3','2024-04-12 22:00:08',NULL,3,NULL,''),(4,'89f01d62309041188f8dfa8b953f1148','4-4如何围绕业务特点制定技术发展路线-阿里系和抖音案例.mp4','video','8/9/89f01d62309041188f8dfa8b953f1148/89f01d62309041188f8dfa8b953f1148.mp4','3','2024-04-12 22:00:26',NULL,3,NULL,''),(8,'f99ba626feafe8e66584d29b60d217c6','155、缓存-缓存使用-缓存击穿、穿透、雪崩.avi','video','f/9/f99ba626feafe8e66584d29b60d217c6/f99ba626feafe8e66584d29b60d217c6.avi','3','2024-04-12 22:26:40',NULL,3,'/video/f/9/f99ba626feafe8e66584d29b60d217c6/f99ba626feafe8e66584d29b60d217c6.avi','视频处理失败');

/*Table structure for table `media_process_history` */

DROP TABLE IF EXISTS `media_process_history`;

CREATE TABLE `media_process_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_id` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件标识',
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `bucket` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储源',
  `status` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态,1:未处理，2：处理成功  3处理失败',
  `create_date` datetime NOT NULL COMMENT '上传时间',
  `finish_date` datetime NOT NULL COMMENT '完成时间',
  `url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '媒资文件访问地址',
  `fail_count` int DEFAULT '0' COMMENT '失败次数',
  `file_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件路径',
  `errormsg` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

/*Data for the table `media_process_history` */

insert  into `media_process_history`(`id`,`file_id`,`filename`,`bucket`,`status`,`create_date`,`finish_date`,`url`,`fail_count`,`file_path`,`errormsg`) values (1,'81d7ed5153316f5774885d3b4c07d8bc','Spring Security快速上手-创建工程.avi','video','2','2022-12-15 09:41:50','2022-12-15 10:30:26','/video/8/1/81d7ed5153316f5774885d3b4c07d8bc/81d7ed5153316f5774885d3b4c07d8bc.mp4',0,'8/1/81d7ed5153316f5774885d3b4c07d8bc/81d7ed5153316f5774885d3b4c07d8bc.avi',NULL),(2,'18f919e23bedab97a78762c4875addc4','垂直分库-插入和查询测试.avi','video','2','2022-12-15 09:45:18','2022-12-15 10:30:11','/video/1/8/18f919e23bedab97a78762c4875addc4/18f919e23bedab97a78762c4875addc4.mp4',0,'1/8/18f919e23bedab97a78762c4875addc4/18f919e23bedab97a78762c4875addc4.avi',NULL),(3,'efd2eacc4485946fc27feb0caef7506c','读写分离-理解读写分离.avi','video','2','2022-12-15 09:45:19','2022-12-15 10:31:04','/video/e/f/efd2eacc4485946fc27feb0caef7506c/efd2eacc4485946fc27feb0caef7506c.mp4',0,'e/f/efd2eacc4485946fc27feb0caef7506c/efd2eacc4485946fc27feb0caef7506c.avi',NULL),(4,'0dbb928c1c8ccc21f1e0db17ec332da3','4-6面试指导与作业.mp4','video','2','2024-04-12 22:01:03','2024-04-12 22:16:50','/video/0/d/0dbb928c1c8ccc21f1e0db17ec332da3/0dbb928c1c8ccc21f1e0db17ec332da3.mp4',2,'0/d/0dbb928c1c8ccc21f1e0db17ec332da3/0dbb928c1c8ccc21f1e0db17ec332da3.mp4',''),(5,'13376223c8cf313491d1d46807f09d7c','4-5阿里系业务的供应链系统-如何评估子类业务的外包与采购.mp4','video','2','2024-04-12 22:00:46','2024-04-12 22:16:58','/video/1/3/13376223c8cf313491d1d46807f09d7c/13376223c8cf313491d1d46807f09d7c.mp4',2,'1/3/13376223c8cf313491d1d46807f09d7c/13376223c8cf313491d1d46807f09d7c.mp4',''),(6,'0f17b84e14410630b4b3dda30415ba5c','131、商城业务-商品上架-构造sku检索属性.avi','video','2','2024-04-12 22:25:55','2024-04-12 22:26:28','/video/0/f/0f17b84e14410630b4b3dda30415ba5c/0f17b84e14410630b4b3dda30415ba5c.mp4',0,'0/f/0f17b84e14410630b4b3dda30415ba5c/0f17b84e14410630b4b3dda30415ba5c.avi',NULL),(7,'d90f97f5e2870dafac9095d0a134bfce','246、商城业务-购物车-改变购物项数量.avi','video','2','2024-04-12 22:27:16','2024-04-12 22:28:09','/video/d/9/d90f97f5e2870dafac9095d0a134bfce/d90f97f5e2870dafac9095d0a134bfce.mp4',0,'d/9/d90f97f5e2870dafac9095d0a134bfce/d90f97f5e2870dafac9095d0a134bfce.avi',NULL),(8,'0401bb18ffc89b7eda7de2e8aaba4f73','110、全文检索-ElasticSearch-进阶-两种查询方式.avi','video','2','2024-04-12 22:26:53','2024-04-12 22:28:09','/video/0/4/0401bb18ffc89b7eda7de2e8aaba4f73/0401bb18ffc89b7eda7de2e8aaba4f73.mp4',0,'0/4/0401bb18ffc89b7eda7de2e8aaba4f73/0401bb18ffc89b7eda7de2e8aaba4f73.avi',NULL),(9,'43aab98976787e7c47805a118726b6b2','250、商城业务-消息队列-RabbitMQ安装.avi','video','2','2024-04-12 22:28:30','2024-04-12 22:29:01','/video/4/3/43aab98976787e7c47805a118726b6b2/43aab98976787e7c47805a118726b6b2.mp4',0,'4/3/43aab98976787e7c47805a118726b6b2/43aab98976787e7c47805a118726b6b2.avi',NULL),(10,'c00baee1c4305d76da3e478d4e184c51','106_尚硅谷_MySQL基础_修改多表的记录 .avi','video','2','2024-04-15 23:58:47','2024-04-16 00:00:31','/video/c/0/c00baee1c4305d76da3e478d4e184c51/c00baee1c4305d76da3e478d4e184c51.mp4',0,'c/0/c00baee1c4305d76da3e478d4e184c51/c00baee1c4305d76da3e478d4e184c51.avi',NULL),(11,'1b3d92819b255680c5144c42e10226d8','92_尚硅谷_MySQL基础_from后面的子查询使用.avi','video','2','2024-04-15 23:58:23','2024-04-16 00:00:31','/video/1/b/1b3d92819b255680c5144c42e10226d8/1b3d92819b255680c5144c42e10226d8.mp4',0,'1/b/1b3d92819b255680c5144c42e10226d8/1b3d92819b255680c5144c42e10226d8.avi',NULL),(12,'29c2e95ffd089918757febd95b840eea','110_尚硅谷_MySQL基础_【案例讲解】数据的增删改.avi','video','2','2024-04-15 23:58:52','2024-04-16 00:00:47','/video/2/9/29c2e95ffd089918757febd95b840eea/29c2e95ffd089918757febd95b840eea.mp4',0,'2/9/29c2e95ffd089918757febd95b840eea/29c2e95ffd089918757febd95b840eea.avi',NULL);

/*Table structure for table `mq_message` */

DROP TABLE IF EXISTS `mq_message`;

CREATE TABLE `mq_message` (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息id',
  `message_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型代码',
  `business_key1` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `mq_host` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息队列主机',
  `mq_port` int NOT NULL COMMENT '消息队列端口',
  `mq_virtualhost` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息队列虚拟主机',
  `mq_queue` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '队列名称',
  `inform_num` int unsigned NOT NULL COMMENT '通知次数',
  `state` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '处理状态，0:初始，1:成功',
  `returnfailure_date` datetime DEFAULT NULL COMMENT '回复失败时间',
  `returnsuccess_date` datetime DEFAULT NULL COMMENT '回复成功时间',
  `returnfailure_msg` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '回复失败内容',
  `inform_date` datetime DEFAULT NULL COMMENT '最近通知时间',
  `stage_state1` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '阶段1处理状态, 0:初始，1:成功',
  `stage_state2` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '阶段2处理状态, 0:初始，1:成功',
  `stage_state3` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '阶段3处理状态, 0:初始，1:成功',
  `stage_state4` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '阶段4处理状态, 0:初始，1:成功',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

/*Data for the table `mq_message` */

insert  into `mq_message`(`id`,`message_type`,`business_key1`,`business_key2`,`business_key3`,`mq_host`,`mq_port`,`mq_virtualhost`,`mq_queue`,`inform_num`,`state`,`returnfailure_date`,`returnsuccess_date`,`returnfailure_msg`,`inform_date`,`stage_state1`,`stage_state2`,`stage_state3`,`stage_state4`) values ('f29a3149-7429-40be-8a4e-9909f32003b0','xc.mq.msgsync.coursepub','111',NULL,NULL,'127.0.0.1',5607,'/','xc.course.publish.queue',0,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `mq_message_history` */

DROP TABLE IF EXISTS `mq_message_history`;

CREATE TABLE `mq_message_history` (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息id',
  `message_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型代码',
  `business_key1` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `mq_host` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息队列主机',
  `mq_port` int NOT NULL COMMENT '消息队列端口',
  `mq_virtualhost` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息队列虚拟主机',
  `mq_queue` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '队列名称',
  `inform_num` int(10) unsigned zerofill DEFAULT NULL COMMENT '通知次数',
  `state` int(10) unsigned zerofill DEFAULT NULL COMMENT '处理状态，0:初始，1:成功，2:失败',
  `returnfailure_date` datetime DEFAULT NULL COMMENT '回复失败时间',
  `returnsuccess_date` datetime DEFAULT NULL COMMENT '回复成功时间',
  `returnfailure_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '回复失败内容',
  `inform_date` datetime DEFAULT NULL COMMENT '最近通知时间',
  `stage_state1` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `stage_state2` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `stage_state3` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `stage_state4` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

/*Data for the table `mq_message_history` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
