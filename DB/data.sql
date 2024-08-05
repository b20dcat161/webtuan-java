-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: student
-- ------------------------------------------------------
-- Server version	8.0.38
CREATE DATABASE IF NOT EXISTS student;

USE student;
SET NAMES utf8mb4;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `sdt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES 
(40,0,'1','3','$2a$10$6U.WCKMIo9Zm1APFrcCDNeTvt6qFro36IZ0JHJ8vgwowkosYqrQ8O','1','1'),
(41,1,'4','4','$2a$10$Is7zyGYRuNSveaWS7POACejSn.n3lkc8GUR5f1gdhFJA7WfzQ14iK','4','4'),
(42,5,'5','5','$2a$10$eBhThzamLP7/RA3OS39k6Oq55W/XWu79LzZtWoQE5Mv0sW64hhAhq','5','5'),
(43,0,'6','6','$2a$10$Gi5ZYHMieoxO.f.2E22s2.iTjTvPMdvOh8CrtObOeEftUjCb.oNY.','6','6'),
(44,1,'Ngô Sỹ Tuấn','student1','$2a$10$9FjyquGuM2oS2HhWjqUe2uyv2Rdcs2bUIGBxMYR2Sw42.OCltqOLG','a@gmail.com','009090283023'),
(45,1,'Đức Toàn','student2','$2a$10$OI3j6deptVSM7Ods7kJuJO3ySVRbNv9OqbVO2aCf0gr4IfMgP9YCK','a1@gmail.com','009090283023'),
(46,0,'Trần Văn Đông','teacher1','$2a$10$OWiL8pMq4wiwmtuK9JLyq.XRSSuNrEdOppVSVmI3WALN6EO3XdJJu','a@gmail.com','009090283023'),
(47,0,'Nguyễn Khuyến','teacher2','$2a$10$kwKy5t2LMUhGjArocB9Zse4clL1iQEBYf5MlHD4x10tortN/MA1U2','a@gmail.com','009090283023');
UNLOCK TABLES;

--
-- Table structure for table `homework_files`
--

DROP TABLE IF EXISTS `homework_files`;
CREATE TABLE `homework_files` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `homework_files_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `homework_files`
--

LOCK TABLES `homework_files` WRITE;
INSERT INTO `homework_files` VALUES 
(5,40,'file.xml','C:\\Users\\tuanlee\\IdeaProjects\\demo8\\src\\main\\resources\\static\\upload\\file.xml'),
(6,40,'browserstack.exe','C:\\Users\\tuanlee\\IdeaProjects\\demo8\\src\\main\\resources\\static\\upload\\browserstack.exe');
UNLOCK TABLES;

--
-- Table structure for table `submission_files`
--

DROP TABLE IF EXISTS `submission_files`;
CREATE TABLE `submission_files` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `homework_id` int NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `homework_id` (`homework_id`),
  CONSTRAINT `submission_files_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `submission_files_ibfk_2` FOREIGN KEY (`homework_id`) REFERENCES `homework_files` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `submission_files`
--

LOCK TABLES `submission_files` WRITE;
INSERT INTO `submission_files` VALUES 
(3,40,5,'0XXE.XML','C:\\Users\\tuanlee\\IdeaProjects\\demo8\\src\\main\\resources\\static\\upload\\0XXE.XML');
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `message` text NOT NULL,
  `sent_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sender_id` (`sender_id`),
  KEY `receiver_id` (`receiver_id`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
INSERT INTO `messages` VALUES 
(23,40,40,'xss111','2024-07-31 09:11:36'),
(24,40,40,'xsssssss1','2024-07-31 09:11:41'),
(25,40,40,'hello ha','2024-07-31 09:11:47'),
(26,40,41,'40 nhan1','2024-07-31 09:14:29'),
(27,41,40,'41 tra lời11','2024-07-31 09:14:59'),
(29,41,40,'41 noi tiep\r\n','2024-07-31 09:17:47'),
(31,41,40,'oke chưa\r\n','2024-07-31 09:25:20'),
(32,40,41,'\"><Script>alert(1)</Script>','2024-07-31 09:25:51');
UNLOCK TABLES;

-- Dump completed on 2024-08-02 10:30:35
