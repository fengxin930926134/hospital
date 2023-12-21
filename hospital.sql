/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : hospital

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2020-08-19 10:43:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bill_info`
-- ----------------------------
DROP TABLE IF EXISTS `bill_info`;
CREATE TABLE `bill_info` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number` int(11) NOT NULL,
  `price` float NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `visit_number` int(11) NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bill_info
-- ----------------------------

-- ----------------------------
-- Table structure for `checklist`
-- ----------------------------
DROP TABLE IF EXISTS `checklist`;
CREATE TABLE `checklist` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `visit_number` int(20) NOT NULL COMMENT '就诊号',
  `user_name` varchar(20) NOT NULL,
  `medical_id` int(20) NOT NULL COMMENT '医技id',
  `doctor_id` int(20) NOT NULL COMMENT '医生id',
  `medical_number` int(10) NOT NULL DEFAULT '1' COMMENT '医技数量',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '0.待付款 1.待使用 2.已使用',
  `create_date` datetime NOT NULL COMMENT '开单日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of checklist
-- ----------------------------
INSERT INTO `checklist` VALUES ('1', '12', '成风', '1', '2', '12', '2', '2020-08-09 22:29:25');
INSERT INTO `checklist` VALUES ('2', '13', 'Admin', '1', '1', '1', '2', '2020-08-10 15:13:13');
INSERT INTO `checklist` VALUES ('3', '14', 'Admin', '1', '1', '1', '2', '2020-08-10 15:32:56');
INSERT INTO `checklist` VALUES ('4', '15', 'Admin', '1', '2', '2', '2', '2020-08-12 15:27:02');

-- ----------------------------
-- Table structure for `dept`
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `dept_id` int(20) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(30) NOT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES ('1', '妇科');
INSERT INTO `dept` VALUES ('2', '肛肠科');
INSERT INTO `dept` VALUES ('3', '眼科');
INSERT INTO `dept` VALUES ('4', '呼吸内科');
INSERT INTO `dept` VALUES ('5', '心胸外科');
INSERT INTO `dept` VALUES ('6', '产科');
INSERT INTO `dept` VALUES ('7', '神经外科');
INSERT INTO `dept` VALUES ('8', '肝病科');
INSERT INTO `dept` VALUES ('9', '艾滋病科');
INSERT INTO `dept` VALUES ('10', '精神科');
INSERT INTO `dept` VALUES ('11', '中医骨伤科');
INSERT INTO `dept` VALUES ('12', '放疗科');

-- ----------------------------
-- Table structure for `drug`
-- ----------------------------
DROP TABLE IF EXISTS `drug`;
CREATE TABLE `drug` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL COMMENT '药品名',
  `batch_number` varchar(20) DEFAULT NULL COMMENT '批号',
  `price` float(10,0) DEFAULT NULL COMMENT '单价',
  `category` varchar(20) DEFAULT NULL COMMENT '所属类别',
  `manufacture_date` date DEFAULT NULL COMMENT '生产日期',
  `due_month` int(10) DEFAULT NULL COMMENT '保质期/月',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of drug
-- ----------------------------
INSERT INTO `drug` VALUES ('1', '板蓝根', 'H16F040', '12', '非处方药', '2020-08-02', '12');

-- ----------------------------
-- Table structure for `drugstore`
-- ----------------------------
DROP TABLE IF EXISTS `drugstore`;
CREATE TABLE `drugstore` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `drug_id` int(20) NOT NULL,
  `drug_name` varchar(255) NOT NULL,
  `number` int(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of drugstore
-- ----------------------------
INSERT INTO `drugstore` VALUES ('1', '1', '板蓝根', '60');

-- ----------------------------
-- Table structure for `medical`
-- ----------------------------
DROP TABLE IF EXISTS `medical`;
CREATE TABLE `medical` (
  `medical_id` int(20) NOT NULL AUTO_INCREMENT,
  `medical_name` varchar(30) NOT NULL COMMENT '医技名',
  `medical_price` float(10,2) NOT NULL,
  PRIMARY KEY (`medical_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of medical
-- ----------------------------
INSERT INTO `medical` VALUES ('1', 'X光', '20.00');

-- ----------------------------
-- Table structure for `medical_record`
-- ----------------------------
DROP TABLE IF EXISTS `medical_record`;
CREATE TABLE `medical_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '就诊号',
  `patient_id` int(20) NOT NULL COMMENT '患者id',
  `checklist_ids` varchar(255) DEFAULT NULL COMMENT '检查单id',
  `prescribe_ids` varchar(255) DEFAULT NULL COMMENT '处方id',
  `create_date` datetime NOT NULL COMMENT '就诊日期',
  `remark` varchar(255) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '0.未检查 1.待付款',
  `history` int(2) NOT NULL DEFAULT '0' COMMENT '是否病历 0.否 1.是 >1有还未使用药品或者检查单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of medical_record
-- ----------------------------
INSERT INTO `medical_record` VALUES ('7', '2', null, '14,', '2020-08-08 23:11:09', 'dawdawd', '1', '1');
INSERT INTO `medical_record` VALUES ('12', '1', '1,', '15,', '2020-08-09 22:25:16', 'dadawdawddadawdawddadawdawddadawdawddadawdawd', '1', '1');
INSERT INTO `medical_record` VALUES ('13', '2', '2,', '16,', '2020-08-10 15:12:03', '这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案这是文案', '1', '1');
INSERT INTO `medical_record` VALUES ('14', '2', '3,', '19,', '2020-08-10 15:30:28', 'adwdawd', '1', '1');
INSERT INTO `medical_record` VALUES ('15', '2', '4,', '20,', '2020-08-12 15:25:38', '文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案文案', '1', '1');

-- ----------------------------
-- Table structure for `prescribe`
-- ----------------------------
DROP TABLE IF EXISTS `prescribe`;
CREATE TABLE `prescribe` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `visit_number` int(20) NOT NULL COMMENT '就诊号',
  `user_name` varchar(20) NOT NULL,
  `drug_id` int(20) NOT NULL COMMENT '药品id',
  `doctor_id` int(20) NOT NULL COMMENT '医生id',
  `drug_number` int(10) NOT NULL DEFAULT '1' COMMENT '药品数量',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '0.待付款 1.待使用 2.已使用',
  `create_date` datetime NOT NULL COMMENT '开单日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of prescribe
-- ----------------------------
INSERT INTO `prescribe` VALUES ('14', '7', 'Admin', '1', '1', '1', '2', '2020-08-08 23:11:22');
INSERT INTO `prescribe` VALUES ('15', '12', '成风', '1', '2', '1', '2', '2020-08-09 22:29:18');
INSERT INTO `prescribe` VALUES ('16', '13', 'Admin', '1', '1', '1', '2', '2020-08-10 15:13:10');
INSERT INTO `prescribe` VALUES ('19', '14', 'Admin', '1', '1', '1', '2', '2020-08-10 15:32:51');
INSERT INTO `prescribe` VALUES ('20', '15', 'Admin', '1', '1', '1', '2', '2020-08-12 15:26:49');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `sex` int(1) DEFAULT '1' COMMENT '1男 2女',
  `phone` varchar(11) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `dept_id` int(20) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'user', '$2a$10$016mx7TZ8x5tEZFLYGplV.FMTZedK0dpZaOYGawSniupSw51IGrxa', '成风', '1', '', '1998-01-31', null, 'ROLE_ADMIN,ROLE_DOCTOR,ROLE_TECHNICAL,ROLE_PHARMACIST,ROLE_USER,');
INSERT INTO `user` VALUES ('2', 'admin', '$2a$10$a7v1t8ECBhpJq3aqztbRJ.Q3eOroSDcvqHfyoFyLIf7mg7Ln.1LCS', 'Admin', '1', '', '1998-01-31', '3', 'ROLE_USER,ROLE_ADMIN,ROLE_DOCTOR,ROLE_TECHNICAL,ROLE_PHARMACIST,');
