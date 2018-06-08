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
-- Table structure for table `shipping_detail_label`
--

DROP TABLE IF EXISTS `shipping_detail_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipping_detail_label` (
  `shipping_detail_label_id` int(11) NOT NULL AUTO_INCREMENT,
  `shipping_detail_id` int(11) NOT NULL,
  `product_label_id` int(11) NOT NULL,
  `shipping_detail_label_type` char(1) NOT NULL,
  `shipping_detail_label_creation_date` datetime NOT NULL,
  `shipping_detail_label_modification_date` datetime DEFAULT NULL,
  PRIMARY KEY (`shipping_detail_label_id`),
  KEY `fk_sdl_product_label_id_idx` (`product_label_id`),
  KEY `fk_sdl_shipping_detail_id_idx` (`shipping_detail_id`),
  CONSTRAINT `fk_sdl_product_label_id` FOREIGN KEY (`product_label_id`) REFERENCES `product_label` (`product_label_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_sdl_shipping_detail_id_idx` FOREIGN KEY (`shipping_detail_id`) REFERENCES `shipping_detail` (`shipping_detail_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipping_detail_label`
--

LOCK TABLES `shipping_detail_label` WRITE;
/*!40000 ALTER TABLE `shipping_detail_label` DISABLE KEYS */;
INSERT INTO `shipping_detail_label` (`shipping_detail_label_id`, `shipping_detail_id`, `product_label_id`, `shipping_detail_label_type`, `shipping_detail_label_creation_date`, `shipping_detail_label_modification_date`) VALUES (1,3,1,'O','2018-05-21 13:27:50',NULL),(2,4,1,'O','2018-05-21 13:27:50',NULL),(3,3,2,'A','2018-05-21 13:46:32',NULL);
/*!40000 ALTER TABLE `shipping_detail_label` ENABLE KEYS */;
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
