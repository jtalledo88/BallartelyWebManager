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
-- Table structure for table `general_parameter`
--

DROP TABLE IF EXISTS `general_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `general_parameter` (
  `param_id` int(11) NOT NULL AUTO_INCREMENT,
  `param_type` varchar(15) NOT NULL,
  `param_code` varchar(10) NOT NULL,
  `param_description` varchar(250) NOT NULL,
  `param_value` varchar(2500) NOT NULL,
  `param_creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `param_modification_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `param_status` char(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`param_id`),
  UNIQUE KEY `param_code_UNIQUE` (`param_code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_parameter`
--

LOCK TABLES `general_parameter` WRITE;
/*!40000 ALTER TABLE `general_parameter` DISABLE KEYS */;
INSERT INTO `general_parameter` (`param_id`, `param_type`, `param_code`, `param_description`, `param_value`, `param_creation_date`, `param_modification_date`, `param_status`) VALUES (1,'STATUS','A','ESTADO ACTIVO','ACTIVO','2018-05-09 13:47:45',NULL,'1'),(2,'STATUS','I','ESTADO INACTIVO','INACTIVO','2018-05-09 13:48:00',NULL,'1'),(3,'DOCTYPE','DNI','DOCUMENTO NACIONAL DE IDENTIDAD','D.N.I','2018-05-09 13:48:39',NULL,'1'),(4,'DOCTYPE','RUC','REGISTRO ÚNICO CALIFICADO','R.U.C','2018-05-09 13:49:18',NULL,'1'),(5,'CLIENTTYPE','M','CLIENTE MOROSO','MOROSO','2018-05-09 13:49:52',NULL,'1'),(6,'CLIENTTYPE','N','CLIENTE NORMAL','NORMAL','2018-05-09 13:50:11',NULL,'1'),(7,'CLIENTTYPE','P','CLIENTE PREFERENCIAL','PREFERENCIAL','2018-05-09 13:50:26',NULL,'1'),(8,'GENERIC','IGV','Impuesto general a las ventas','0.18','2018-05-15 19:19:23',NULL,'1'),(9,'GENERIC','UPLOADDIR','Ruta de comprobantes de pago digitales','C:\\Users\\javie\\Documents\\Programacion\\Proyecto Ballartely\\comprobantes_digitales\\','2018-05-17 17:43:04','2018-05-17 19:35:35','1');
/*!40000 ALTER TABLE `general_parameter` ENABLE KEYS */;
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
