/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50542
Source Host           : 127.0.0.1:3306
Source Database       : mail

Target Server Type    : MYSQL
Target Server Version : 50542
File Encoding         : 65001

Date: 2017-01-13 14:36:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mail_queue
-- ----------------------------
DROP TABLE IF EXISTS `mail_queue`;
CREATE TABLE `mail_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) NOT NULL COMMENT 'ip来源',
  `title` varchar(255) DEFAULT NULL,
  `address` varchar(800) DEFAULT NULL,
  `content` text,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  `send_time` timestamp NULL DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0' COMMENT '是否发送成功,0未发送，1发送成功,2发送失败',
  `count` int(11) DEFAULT '0' COMMENT '发送次数',
  PRIMARY KEY (`id`),
  KEY `address` (`address`(255)) USING HASH,
  KEY `time` (`create_time`,`update_time`,`send_time`),
  KEY `count_status` (`status`,`count`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
