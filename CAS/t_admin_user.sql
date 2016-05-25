# Host: 192.168.66.142  (Version: 5.5.44-MariaDB)
# Date: 2016-05-25 13:38:21
# Generator: MySQL-Front 5.3  (Build 4.234)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "t_admin_user"
#

DROP TABLE IF EXISTS `t_admin_user`;
CREATE TABLE `t_admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `rule_allow` varchar(255) DEFAULT NULL,
  `rule_deny` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Data for table "t_admin_user"
#

INSERT INTO `t_admin_user` VALUES (1,'a@a.a','a','a','a','ROLE_TEST','zhangsan,lisi','zhaowu'),(2,'b@b.b','b','b','b',NULL,NULL,NULL),(3,NULL,'joe','joe','joe',NULL,NULL,NULL);
