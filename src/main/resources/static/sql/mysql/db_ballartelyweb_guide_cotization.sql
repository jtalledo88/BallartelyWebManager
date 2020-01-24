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
-- Table structure for table `guide_cotization`
--

DROP TABLE IF EXISTS `guide_cotization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guide_cotization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_cost_guide` decimal(10,2) NOT NULL,
  `total_weight_dehydrated` decimal(10,2) NOT NULL,
  `total_expenses` decimal(10,2) NOT NULL,
  `total_decrease` decimal(10,2) NOT NULL,
  `total_unit_cost` decimal(10,2) NOT NULL,
  `cotization_creation_date` datetime NOT NULL,
  `guide_head_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_guide_head_cotization_idx` (`guide_head_id`),
  CONSTRAINT `FK85sahv329pl4gk5i8rwqvpycl` FOREIGN KEY (`guide_head_id`) REFERENCES `guide_head` (`id`),
  CONSTRAINT `fk_guide_head_cotization` FOREIGN KEY (`guide_head_id`) REFERENCES `guide_head` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guide_cotization`
--

LOCK TABLES `guide_cotization` WRITE;
/*!40000 ALTER TABLE `guide_cotization` DISABLE KEYS */;
INSERT INTO `guide_cotization` (`id`, `unit_cost_guide`, `total_weight_dehydrated`, `total_expenses`, `total_decrease`, `total_unit_cost`, `cotization_creation_date`, `guide_head_id`) VALUES (1,4.50,4933.64,1014.40,806.40,4.54,'2018-08-03 00:00:00',1);
/*!40000 ALTER TABLE `guide_cotization` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-03  1:00:37
