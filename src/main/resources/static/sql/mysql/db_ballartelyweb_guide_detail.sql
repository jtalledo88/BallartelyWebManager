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
-- Table structure for table `guide_detail`
--

DROP TABLE IF EXISTS `guide_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guide_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `average` decimal(19,2) DEFAULT NULL,
  `boxes_quantity` int(11) DEFAULT NULL,
  `dead_quantity` int(11) DEFAULT NULL,
  `dead_weight` decimal(19,2) DEFAULT NULL,
  `guide_creation_date` datetime DEFAULT NULL,
  `guide_modification_date` datetime DEFAULT NULL,
  `initial_weight` decimal(19,2) DEFAULT NULL,
  `net_weight` decimal(19,2) DEFAULT NULL,
  `product_description` varchar(255) DEFAULT NULL,
  `product_quantity` int(11) DEFAULT NULL,
  `tara_weight` decimal(19,2) DEFAULT NULL,
  `unit_cost` decimal(19,2) DEFAULT NULL,
  `guide_head_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5k2rpjtgpw055d696cw8awe8v` (`guide_head_id`),
  CONSTRAINT `FK5k2rpjtgpw055d696cw8awe8v` FOREIGN KEY (`guide_head_id`) REFERENCES `guide_head` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guide_detail`
--

LOCK TABLES `guide_detail` WRITE;
/*!40000 ALTER TABLE `guide_detail` DISABLE KEYS */;
INSERT INTO `guide_detail` (`id`, `average`, `boxes_quantity`, `dead_quantity`, `dead_weight`, `guide_creation_date`, `guide_modification_date`, `initial_weight`, `net_weight`, `product_description`, `product_quantity`, `tara_weight`, `unit_cost`, `guide_head_id`) VALUES (1,1.99,280,0,0.00,'2018-07-29 19:23:16','2018-07-29 19:23:16',6941.80,5004.20,'PV Hembra',2520,1937.60,4.50,1);
/*!40000 ALTER TABLE `guide_detail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-03  1:00:38
