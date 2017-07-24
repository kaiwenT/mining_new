/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : mining

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-06-21 16:31:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for issue_label
-- ----------------------------
DROP TABLE IF EXISTS `issue_label`;
CREATE TABLE `issue_label` (
  `issueId` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `labelid` int(11) NOT NULL,
  PRIMARY KEY (`issueId`,`labelid`),
  KEY `labelid` (`labelid`),
  CONSTRAINT `issue_label_ibfk_1` FOREIGN KEY (`issueId`) REFERENCES `issue` (`issue_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `issue_label_ibfk_2` FOREIGN KEY (`labelid`) REFERENCES `label` (`labelid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
