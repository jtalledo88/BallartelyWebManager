CREATE DATABASE  IF NOT EXISTS `db_ballartelyweb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `db_ballartelyweb`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: db_ballartelyweb
-- ------------------------------------------------------
-- Server version	5.7.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `product_label`
--

DROP TABLE IF EXISTS `product_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_label` (
  `product_label_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_label_code` varchar(10) NOT NULL,
  `product_label_description` varchar(250) NOT NULL,
  `product_label_creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `product_label_modification_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `product_label_status` char(1) NOT NULL,
  PRIMARY KEY (`product_label_id`),
  UNIQUE KEY `product_label_code_UNIQUE` (`product_label_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_label`
--

LOCK TABLES `product_label` WRITE;
/*!40000 ALTER TABLE `product_label` DISABLE KEYS */;
INSERT INTO `product_label` (`product_label_id`, `product_label_code`, `product_label_description`, `product_label_creation_date`, `product_label_modification_date`, `product_label_status`) VALUES (1,'P2','POLLO 2KG','2018-05-09 13:50:53',NULL,'1'),(2,'P3','POLLO 3KG','2018-05-09 13:51:01',NULL,'1'),(3,'P4','POLLO 4KG','2018-05-09 13:51:12',NULL,'1');
/*!40000 ALTER TABLE `product_label` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-21 13:58:07
