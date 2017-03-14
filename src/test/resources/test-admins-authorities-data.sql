/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2016-12-21 16:43:46
*/

DELETE FROM admins;
-- ----------------------------
-- Records of admins
-- ----------------------------
INSERT INTO `admins` VALUES ('1', 'wqzhou', '9nyEW38DIiw=', 'wqzhou@evistek.com', '1');
INSERT INTO `admins` VALUES ('2', 'wxzhang', '9nyEW38DIiw=', 'wxzhang@evistek.com', '1');
INSERT INTO `admins` VALUES ('3', 'ymzhao', '9nyEW38DIiw=', 'ymzhao@evistek.com', '1');
INSERT INTO `admins` VALUES ('4', 'yshi', '9nyEW38DIiw=', 'yshi@evistek.com', '1');
INSERT INTO `admins` VALUES ('5', 'admin', '7czHPz3qHVU=', null, '1');
INSERT INTO `admins` VALUES ('6', 'yshi@evistek.com', '9nyEW38DIiw=', 'yshi@evistek.com', '1');

DELETE FROM authorities;
-- ----------------------------
-- Records of authorities
-- ----------------------------
INSERT INTO `authorities` VALUES ('1', 'wxzhang', 'ROLE_ADMIN');
INSERT INTO `authorities` VALUES ('2', 'admin', 'ROLE_USER');
INSERT INTO `authorities` VALUES ('3', 'wqzhou', 'ROLE_ADMIN');
INSERT INTO `authorities` VALUES ('4', 'ymzhao', 'ROLE_ADMIN');
INSERT INTO `authorities` VALUES ('5', 'yshi', 'ROLE_ADMIN');
