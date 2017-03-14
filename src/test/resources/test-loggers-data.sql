/*
Navicat MySQL Data Transfer

Source Server         : evistek
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : evistekdb2

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-03-09 11:33:35
*/


DELETE FROM loggers;

-- ----------------------------
-- Records of loggers
-- ----------------------------
INSERT INTO `loggers` VALUES ('1', '2017-01-01 11:28:28', 'LOGIN', 'ymzhao', 'admin login: ymzhao');
INSERT INTO `loggers` VALUES ('2', '2016-12-01 11:28:30', 'QUERY_RESOURCE', 'ymzhao', 'query image number');
INSERT INTO `loggers` VALUES ('3', '2017-03-09 11:28:30', 'AUDIT_RESOURCE', 'ymzhao', 'get unaudited images: 0');
INSERT INTO `loggers` VALUES ('4', '2016-09-01 11:28:31', 'QUERY_RESOURCE', 'ymzhao', 'query image number');
INSERT INTO `loggers` VALUES ('5', '2017-03-09 11:28:31', 'AUDIT_RESOURCE', 'ymzhao', 'get unaudited images: 0');
INSERT INTO `loggers` VALUES ('6', '2017-03-09 11:28:31', 'QUERY_RESOURCE', 'ymzhao', 'query video number');
INSERT INTO `loggers` VALUES ('7', '2017-03-09 11:28:31', 'AUDIT_RESOURCE', 'ymzhao', 'get unaudited videos: 0');
INSERT INTO `loggers` VALUES ('8', '2017-03-09 11:28:31', 'QUERY_RESOURCE', 'ymzhao', 'query video number');
INSERT INTO `loggers` VALUES ('9', '2017-03-09 11:28:31', 'AUDIT_RESOURCE', 'ymzhao', 'get unaudited videos: 0');
INSERT INTO `loggers` VALUES ('10', '2017-03-09 11:28:31', 'QUERY_RESOURCE', 'ymzhao', 'query product number with audit status: false');
INSERT INTO `loggers` VALUES ('11', '2017-03-09 11:28:31', 'AUDIT_RESOURCE', 'ymzhao', 'get unaudited products(ads): 0');
INSERT INTO `loggers` VALUES ('12', '2017-03-09 11:28:31', 'QUERY_RESOURCE', 'ymzhao', 'query product number with audit status: false');
INSERT INTO `loggers` VALUES ('13', '2017-03-09 11:28:31', 'AUDIT_RESOURCE', 'ymzhao', 'get unaudited products(ads): 0');
