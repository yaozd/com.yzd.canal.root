/*
Navicat MySQL Data Transfer

Source Server         : 192.168.3.30-CANAL
Source Server Version : 50638
Source Host           : 192.168.3.30:3306
Source Database       : tb_other_test

Target Server Type    : MYSQL
Target Server Version : 50638
File Encoding         : 65001

Date: 2018-04-26 15:21:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `other_01`
-- ----------------------------
DROP TABLE IF EXISTS `other_01`;
CREATE TABLE `other_01` (
  `uid` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of other_01
-- ----------------------------
INSERT INTO `other_01` VALUES ('6', '116-030915');
INSERT INTO `other_01` VALUES ('7', '12-045207');
INSERT INTO `other_01` VALUES ('8', '12-045203');
INSERT INTO `other_01` VALUES ('9', '12-050411');
INSERT INTO `other_01` VALUES ('10', '23-0418311');
INSERT INTO `other_01` VALUES ('11', '60-05241');
INSERT INTO `other_01` VALUES ('15', '61-092450');
INSERT INTO `other_01` VALUES ('16', '61-092500');
INSERT INTO `other_01` VALUES ('17', '75-100620');
INSERT INTO `other_01` VALUES ('18', '116-030228');

-- ----------------------------
-- Table structure for `other_02`
-- ----------------------------
DROP TABLE IF EXISTS `other_02`;
CREATE TABLE `other_02` (
  `uid` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of other_02
-- ----------------------------
INSERT INTO `other_02` VALUES ('6', 'c');
