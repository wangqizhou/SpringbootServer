/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2016-12-23 17:00:23
*/

DELETE FROM categories;
-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES ('1', '电影', 'video');
INSERT INTO `categories` VALUES ('2', 'MV', 'video');
INSERT INTO `categories` VALUES ('3', 'VR全景', 'video');
INSERT INTO `categories` VALUES ('4', '景物', 'image');
INSERT INTO `categories` VALUES ('5', '人物', 'image');
INSERT INTO `categories` VALUES ('6', '建筑', 'image');
INSERT INTO `categories` VALUES ('7', '动物', 'image');
INSERT INTO `categories` VALUES ('8', '裸眼3D电视', 'product');
INSERT INTO `categories` VALUES ('9', '裸眼3D手机', 'product');
INSERT INTO `categories` VALUES ('10', '裸眼3D平板', 'product');
