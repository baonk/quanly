-- --------------------------------------------------------
-- Host:                         192.168.56.102
-- Server version:               10.0.31-MariaDB-0ubuntu0.16.04.2 - Ubuntu 16.04
-- Server OS:                    debian-linux-gnu
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for quanly
CREATE DATABASE IF NOT EXISTS `quanly` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `quanly`;

-- Dumping structure for table quanly.chatmessage
CREATE TABLE IF NOT EXISTS `chatmessage` (
  `message_id` varchar(150) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `cluster_id` varchar(1000) DEFAULT NULL,
  `receiver_id` varchar(100) DEFAULT NULL,
  `sender_id` varchar(100) DEFAULT NULL,
  `text_message` varchar(255) DEFAULT NULL,
  `sticker_src` varchar(255) DEFAULT NULL,
  `file_src` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `created_time` varchar(255) DEFAULT NULL,
  `user_image` varchar(255) DEFAULT NULL,
  `sender_name` varchar(255) NOT NULL,
  PRIMARY KEY (`message_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table quanly.chatmessage: ~0 rows (approximately)
/*!40000 ALTER TABLE `chatmessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `chatmessage` ENABLE KEYS */;

-- Dumping structure for table quanly.department
CREATE TABLE IF NOT EXISTS `department` (
  `department_id` varchar(100) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `department_name` varchar(255) NOT NULL,
  `department_path` varchar(255) NOT NULL,
  `company_id` varchar(80) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `parent_department` varchar(255) NOT NULL,
  PRIMARY KEY (`department_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table quanly.department: ~12 rows (approximately)
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` (`department_id`, `tenant_id`, `department_name`, `department_path`, `company_id`, `company_name`, `email`, `parent_department`) VALUES
	('canhnau', 0, 'Canh Nau Coporation', 'canhnau::', 'canhnau', 'Canh Nau Coporation', 'canhnau@gmail.com', 'self'),
	('development', 0, 'Development', 'huongngai::development', 'huongngai', 'HuongNgai coporation', 'development@gmail.com', 'huongngai'),
	('ggggg', 0, 'GGGGG', 'huongngai::management::ggggg', 'huongngai', 'HuongNgai coporation', 'ggggg@gmail.com', 'management'),
	('hhhh', 0, 'HHHH', 'canhnau::::hhhh', 'canhnau', 'Canh Nau Coporation', 'hhhh@gmail.com', 'canhnau'),
	('huongngai', 0, 'HuongNgai coporation', 'huongngai::', 'huongngai', 'HuongNgai coporation', 'huongNgai@gmail.com', 'self'),
	('management', 0, 'Management', 'huongngai::management', 'huongngai', 'HuongNgai coporation', 'management@gmail.com', 'huongngai'),
	('softwareDept', 0, 'Software Development ', 'huongngai::development::softwareDept', 'huongngai', 'HuongNgai coporation', 'softwaredept@gmail.com', 'development'),
	('sysmanagement', 0, 'System Management', 'huongngai::management::sysmanagement', 'huongngai', 'HuongNgai coporation', 'sysmanagement@gmail.com', 'management'),
	('test1', 0, 'Test1', 'huongngai::management::test1', 'huongngai', 'HuongNgai coporation', 'test1@gmail.com', 'management'),
	('test2', 0, 'Test2', 'huongngai::management::test1::test2', 'huongngai', 'HuongNgai coporation', 'test2@gmail.com', 'test1'),
	('test3', 0, 'Test3', 'huongngai::management::test1::test3', 'huongngai', 'HuongNgai coporation', 'test3@gmail.com', 'test1'),
	('webAppDept', 0, 'Web Application Dept', 'huongngai::development::softwareDept::webAppDept', 'huongngai', 'HuongNgai coporation', 'webappdept@gmail.com', 'softwareDept');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;

-- Dumping structure for table quanly.friends
CREATE TABLE IF NOT EXISTS `friends` (
  `id` varchar(100) NOT NULL,
  `userId` varchar(100) DEFAULT NULL,
  `friendId` varchar(100) DEFAULT NULL,
  `tenantId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`tenantId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table quanly.friends: ~0 rows (approximately)
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
INSERT INTO `friends` (`id`, `userId`, `friendId`, `tenantId`) VALUES
	('1', 'khacbao', 'baonk', 0),
	('2', 'bnkSys', 'baonk', 0),
	('3', 'baonk', 'testUser2', 0),
	('4', 'testUser1', 'testUser2', 0),
	('5', 'testUser2', 'bnkWeb', 0),
	('6', 'testUser2', 'bnkSys', 0);
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;

-- Dumping structure for table quanly.role
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table quanly.role: ~2 rows (approximately)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`role_id`, `role_name`) VALUES
	(1, 'ADMIN'),
	(2, 'USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- Dumping structure for table quanly.server
CREATE TABLE IF NOT EXISTS `server` (
  `tenant_id` int(11) NOT NULL,
  `server_name` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`tenant_id`,`server_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table quanly.server: ~0 rows (approximately)
/*!40000 ALTER TABLE `server` DISABLE KEYS */;
INSERT INTO `server` (`tenant_id`, `server_name`) VALUES
	(0, 'khacbao.com'),
	(0, 'localhost');
/*!40000 ALTER TABLE `server` ENABLE KEYS */;

-- Dumping structure for table quanly.user
CREATE TABLE IF NOT EXISTS `user` (
  `companyid` varchar(100) NOT NULL,
  `tenantid` int(11) NOT NULL,
  `userid` varchar(100) NOT NULL,
  `active` int(11) DEFAULT NULL,
  `birthday` varchar(255) NOT NULL,
  `departmentid` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT 'Employee',
  `email` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `lastlogin` varchar(255) DEFAULT NULL,
  `other_position` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `companyname` varchar(255) NOT NULL,
  `departmentname` varchar(255) NOT NULL,
  `hobby` varchar(255) DEFAULT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `homephone` varchar(255) DEFAULT NULL,
  `homeaddress` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `postcode` varchar(50) DEFAULT NULL,
  `sex` varchar(20) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`companyid`,`tenantid`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table quanly.user: ~8 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`companyid`, `tenantid`, `userid`, `active`, `birthday`, `departmentid`, `position`, `email`, `image`, `lastlogin`, `other_position`, `password`, `phone_number`, `name`, `companyname`, `departmentname`, `hobby`, `fax`, `homephone`, `homeaddress`, `nickname`, `postcode`, `sex`, `country`) VALUES
	('huongngai', 0, 'admin', 0, '1991-12-30', 'management', 'System Developer', 'administrator@gmail.com', '/file/uploadFile/376be9bb-2f7a-4983-9386-e8dc9bd0f4a5.jpg', NULL, NULL, '$2a$10$z2sjPtnGnAydnoJwZZO03.tHlWZ5J1ntzQjC7UuBWmXolMYxDwcZW', '01064989378', 'Admin', 'HuongNgai coporation', 'Management', NULL, NULL, NULL, NULL, NULL, NULL, 'Male', 'VietNam'),
	('huongngai', 0, 'baonk', 0, '1991-12-30', 'softwareDept', 'System Developer', 'bnk1991@gmail.com', '/file/uploadFile/0db7644f-9597-4c4f-a77c-a61eb2abe888.jpg', NULL, NULL, '$2a$10$E1qr392G9/mmuZ.aOIu.jOD.rkWM6y4wy1I.HXuPYMJNA2NA0xGyq', '01657010339', 'Nguyen Khac Bao', 'HuongNgai coporation', 'Software Development', NULL, NULL, NULL, NULL, NULL, NULL, 'Male', 'VietNam'),
	('huongngai', 0, 'bnkSys', 0, '2017-12-19', 'sysmanagement', 'Manager', 'baonksys@gmail.com', '/file/uploadFile/efbe848e-81e4-4cf7-ba2e-4fe9c78e3d2a.png', NULL, NULL, '$2a$10$rxmcM2baGWBJc2AeSFLs5epJEge6H9smptKLh1v2U8JnY7gzPNRNu', '01457983256', 'baonk system', 'HuongNgai coporation', 'System Management', NULL, NULL, NULL, NULL, NULL, NULL, 'Male', 'VietNam'),
	('huongngai', 0, 'bnkWeb', 0, '2017-12-21', 'webAppDept', 'System Developer', 'bnkWeb@gmail.com', '/file/uploadFile/57a6f665-9731-4a73-a562-6aa66d4f1936.jpg', NULL, NULL, '$2a$10$IaShTdDYajdFZ1hG7DXeK.qsyI63jh0NieVdF5NaG5dtPAak72ql6', '01065702346', 'bnkWeb', 'HuongNgai coporation', 'Web Application Dept', NULL, NULL, '046550160', NULL, NULL, NULL, 'Male', 'VietNam'),
	('huongngai', 0, 'khacbao', 1, '1992-02-20', 'development', 'System Developer', 'doker1991@gmail.com', '/file/uploadFile/a4535751-74c9-41f2-9902-31e713d9c817.jpg', NULL, NULL, '$2a$10$z2sjPtnGnAydnoJwZZO03.tHlWZ5J1ntzQjC7UuBWmXolMYxDwcZW', '01064989378', '응웬바오', 'HuongNgai coporation', 'Development', NULL, NULL, NULL, NULL, NULL, NULL, 'Male', 'VietNam'),
	('huongngai', 0, 'testUser1', 0, '2000-12-21', 'development', 'System Developer', 'testUser1@gmail.com', '/file/uploadFile/26c83b62-bcf8-4ff3-a676-2889ea9146a2.jpg', NULL, NULL, '$2a$10$qC6i8h3EX4UWqJp6PcS8A.szjCbwgFWFiZKq5.0AJs.Dk/nswLsXe', '012367854269', 'Test User1', 'HuongNgai coporation', 'Development', NULL, NULL, NULL, NULL, NULL, NULL, 'Male', 'Korea'),
	('huongngai', 0, 'testUser2', 1, '1998-12-15', 'development', 'Tester', 'testUser2@gmail.com', '/file/uploadFile/e796082f-b4d4-4641-b0e0-e16acdf9e91a.jpg', NULL, NULL, '$2a$10$Yz3rvZw8LSY.3O3aK6H79.Etv3LJatDY826O4hh/oRhT0Tb1yvAEG', '01289546378', 'Test User2', 'HuongNgai coporation', 'Development', NULL, NULL, NULL, NULL, NULL, NULL, 'Male', 'VietNam');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- Dumping structure for table quanly.user_role
CREATE TABLE IF NOT EXISTS `user_role` (
  `company_id` varchar(100) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`company_id`,`tenant_id`,`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
  CONSTRAINT `FKjhghkgr4i4yaipawl6nu9i2oi` FOREIGN KEY (`company_id`, `tenant_id`, `user_id`) REFERENCES `user` (`companyid`, `tenantid`, `userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table quanly.user_role: ~6 rows (approximately)
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`company_id`, `tenant_id`, `user_id`, `role_id`) VALUES
	('huongngai', 0, 'admin', 1),
	('huongngai', 0, 'baonk', 2),
	('huongngai', 0, 'bnkSys', 2),
	('huongngai', 0, 'bnkWeb', 2),
	('huongngai', 0, 'khacbao', 1),
	('huongngai', 0, 'testUser1', 2),
	('huongngai', 0, 'testUser2', 2);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
