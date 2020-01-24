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
-- Table structure for table `guide_head`
--

DROP TABLE IF EXISTS `guide_head`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guide_head` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emission_date` datetime DEFAULT NULL,
  `end_point` varchar(255) DEFAULT NULL,
  `guide_creation_date` datetime DEFAULT NULL,
  `guide_file` varchar(255) DEFAULT NULL,
  `guide_modification_date` datetime DEFAULT NULL,
  `guide_number` varchar(255) DEFAULT NULL,
  `guide_status` varchar(255) DEFAULT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `observations` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `start_point` varchar(255) DEFAULT NULL,
  `guide_benefied` char(1) DEFAULT NULL,
  `guide_cotized` char(1) DEFAULT NULL,
  `enterprise_id` int(11) NOT NULL,
  `provider_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `guide_number_UNIQUE` (`guide_number`),
  UNIQUE KEY `invoice_number_UNIQUE` (`invoice_number`),
  KEY `FK6li3e9e4q0l0ewc7w65wtbnyt` (`provider_id`),
  KEY `FKsgi8v4f9yeqs1xlcydigdc9v9` (`enterprise_id`),
  CONSTRAINT `FK6li3e9e4q0l0ewc7w65wtbnyt` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`provider_id`),
  CONSTRAINT `FKsgi8v4f9yeqs1xlcydigdc9v9` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guide_head`
--

LOCK TABLES `guide_head` WRITE;
/*!40000 ALTER TABLE `guide_head` DISABLE KEYS */;
INSERT INTO `guide_head` (`id`, `emission_date`, `end_point`, `guide_creation_date`, `guide_file`, `guide_modification_date`, `guide_number`, `guide_status`, `invoice_number`, `observations`, `reason`, `start_point`, `guide_benefied`, `guide_cotized`, `enterprise_id`, `provider_id`) VALUES (1,'2018-07-30 00:00:00','Prlg. Mantaro Nro 589 Barrio Ulun Concepcion - Junin','2018-07-29 19:23:08','GUIA_0114660.jpg','2018-08-03 00:55:19','0114660','F','20508879400','','Venta','Carretera industrial a laredo km 1.5 Zona Industrial Trujillo La Libertad','Y','Y',1,1);
/*!40000 ALTER TABLE `guide_head` ENABLE KEYS */;
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
