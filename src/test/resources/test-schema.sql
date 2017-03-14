/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : evistekdb2

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2016-12-28 14:55:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1' COMMENT '1：账号使能， 0:账号禁用',
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `ind_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for authorities
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `authority` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '该用户对应的权限角色',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_username` (`username`) USING BTREE,
  CONSTRAINT `fk_authorities_admins_username` FOREIGN KEY (`username`) REFERENCES `admins` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型：video、image、product',
  PRIMARY KEY (`id`),
  KEY `ind_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for devices
-- ----------------------------
DROP TABLE IF EXISTS `devices`;
CREATE TABLE `devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `system` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `client` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `client_version` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `access_time` datetime DEFAULT NULL,
  `imei` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for favorites
-- ----------------------------
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video_id` int(11) DEFAULT NULL COMMENT '收藏的video、image等资源的id',
  `video_name` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_video_id` (`video_id`) USING BTREE,
  KEY `ind_user_id` (`user_id`) USING BTREE,
  KEY `ind_video_name` (`video_name`) USING BTREE,
  CONSTRAINT `fk_favorites_users_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_favorites_videos_id` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_favorites_videos_name` FOREIGN KEY (`video_name`) REFERENCES `videos` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for images
-- ----------------------------
DROP TABLE IF EXISTS `images`;
CREATE TABLE `images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `category_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `format` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片格式如jpg，png等',
  `height` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `size` int(11) DEFAULT NULL COMMENT '以字节为单位',
  `url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumbnail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '缩略图地址',
  `owner_id` int(11) DEFAULT NULL,
  `audit` tinyint(1) DEFAULT '0' COMMENT '审核状态，1：已审核， 0：为审核',
  `download_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ind_url` (`url`) USING BTREE,
  KEY `ind_category_id` (`category_id`) USING BTREE,
  KEY `ind_category_name` (`category_name`) USING BTREE,
  KEY `ind_owner_id` (`owner_id`) USING BTREE,
  CONSTRAINT `fk_images_admins_id` FOREIGN KEY (`owner_id`) REFERENCES `admins` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_images_categories_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_images_categories_name` FOREIGN KEY (`category_name`) REFERENCES `categories` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for play_records
-- ----------------------------
DROP TABLE IF EXISTS `play_records`;
CREATE TABLE `play_records` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `video_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `duration` int(11) unsigned zerofill DEFAULT NULL COMMENT '播放时长，毫秒为单位',
  `client` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `client_version` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `video_name` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_video_id` (`video_id`) USING BTREE,
  KEY `ind_user_id` (`user_id`) USING BTREE,
  KEY `ind_video_name` (`video_name`) USING BTREE,
  CONSTRAINT `fk_play_records_users_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_play_records_videos_id` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_play_records_videos_name` FOREIGN KEY (`video_name`) REFERENCES `videos` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `img_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品图片地址',
  `website_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品在官网地址',
  `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '产品介绍',
  `category_id` int(11) DEFAULT NULL,
  `category_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  `audit` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ind_name` (`name`) USING BTREE,
  KEY `ind_category_id` (`category_id`) USING BTREE,
  KEY `ind_category_name` (`category_name`) USING BTREE,
  KEY `ind_owner_id` (`owner_id`) USING BTREE,
  CONSTRAINT `fk_products_admins_id` FOREIGN KEY (`owner_id`) REFERENCES `admins` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_categories_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_categories_name` FOREIGN KEY (`category_name`) REFERENCES `categories` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for total_play_records
-- ----------------------------
DROP TABLE IF EXISTS `total_play_records`;
CREATE TABLE `total_play_records` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `video_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `duration` int(11) unsigned zerofill DEFAULT NULL COMMENT '播放时长，毫秒为单位',
  `client` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `client_version` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `video_name` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_video_id` (`video_id`) USING BTREE,
  KEY `ind_user_id` (`user_id`) USING BTREE,
  KEY `ind_video_name` (`video_name`) USING BTREE,
  CONSTRAINT `fk_total_play_records_users_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_total_play_records_videos_id` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_total_play_records_videos_name` FOREIGN KEY (`video_name`) REFERENCES `videos` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话号码',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nickname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地理位置',
  `sex` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `figure_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像url',
  `source` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号来源于evistek还是第三方比如qq',
  `phone_device` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机型号',
  `phone_system` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机操作系统版本',
  `vr_device` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `ind_name` (`username`) USING BTREE,
  UNIQUE KEY `ind_email` (`email`) USING BTREE,
  UNIQUE KEY `ind_phone` (`phone`) USING BTREE,
  KEY `ind_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for videos
-- ----------------------------
DROP TABLE IF EXISTS `videos`;
CREATE TABLE `videos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `category_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建、上传时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新、修改时间',
  `release_time` datetime DEFAULT NULL COMMENT '上映时间',
  `format` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频封装格式比如mp4',
  `height` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `size` int(11) DEFAULT NULL COMMENT '以字节为单位',
  `duration` int(11) unsigned zerofill DEFAULT NULL COMMENT '视频时长，毫秒',
  `actors` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '演员',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频拍摄国家',
  `url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '视频文件地址',
  `landscape_cover_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '横版封面地址',
  `portrait_cover_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '竖版封面地址',
  `preview1_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '预览图、视频截图',
  `preview2_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `preview3_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `brief` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '一句话简介或宣传语之类',
  `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '详细介绍',
  `owner_id` int(11) DEFAULT NULL COMMENT '上传人员的id',
  `audit` tinyint(1) DEFAULT '0' COMMENT '审核状态  0:未审核 1:审核',
  `download_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ind_url` (`url`) USING BTREE,
  KEY `ind_category_id` (`category_id`) USING BTREE,
  KEY `ind_category_name` (`category_name`) USING BTREE,
  KEY `ind_owner_id` (`owner_id`) USING BTREE,
  KEY `ind_video_name` (`name`) USING BTREE,
  CONSTRAINT `fk_videos_admins_id` FOREIGN KEY (`owner_id`) REFERENCES `admins` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_videos_categories_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_videos_categories_name` FOREIGN KEY (`category_name`) REFERENCES `categories` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for total_heat_videos
-- ----------------------------
DROP TABLE IF EXISTS `total_heat_videos`;
CREATE TABLE `total_heat_videos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video_id` int(11) NOT NULL,
  `video_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `time` datetime NOT NULL,
  `download_count` int(11) DEFAULT NULL,
  `compare_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `video_id` (`video_id`),
  KEY `video_name` (`video_name`),
  CONSTRAINT `fk_total_heat_videos_video_id` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_total_heat_videos_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_total_heat_videos_video_name` FOREIGN KEY (`video_name`) REFERENCES `videos` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for email_group
-- ----------------------------
DROP TABLE IF EXISTS `email_group`;
CREATE TABLE `email_group` (
  `id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  KEY `fk_email_group_id` (`id`),
  KEY `fk_email_group_name` (`name`),
  CONSTRAINT `fk_email_group_id` FOREIGN KEY (`id`) REFERENCES `admins` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_email_group_name` FOREIGN KEY (`name`) REFERENCES `admins` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for loggers
-- ----------------------------
DROP TABLE IF EXISTS `loggers`;
CREATE TABLE `loggers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL,
  `action` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `owner` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
