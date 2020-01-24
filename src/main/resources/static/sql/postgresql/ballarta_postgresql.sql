--
-- Generated from mysql2pgsql.perl
-- http://gborg.postgresql.org/project/mysql2psql/
-- (c) 2001 - 2007 Jose M. Duarte, Joseph Speigle
--

-- warnings are printed for drop tables if they do not exist
-- please see http://archives.postgresql.org/pgsql-novice/2004-10/msg00158.php

-- ##############################################################
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: db_ballartelyweb
-- ------------------------------------------------------
-- Server version	8.0.18


--
-- Table structure for table account
--

CREATE TABLE  "account" (
   "id" serial8 ,
   "account_type"   char(1) NOT NULL, 
   "account_owner"   int DEFAULT NULL, 
   "account_creation_date"   timestamp without time zone DEFAULT NULL, 
   "account_modification_date"   timestamp without time zone DEFAULT NULL, 
   "account_status"   char(1) NOT NULL, 
   primary key ("id"),
 unique ("account_creation_date") 
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_job_execution
--

CREATE TABLE  "batch_job_execution" (
   "job_execution_id"   bigint NOT NULL, 
   "version"   bigint DEFAULT NULL, 
   "job_instance_id"   bigint NOT NULL, 
   "create_time"   timestamp without time zone NOT NULL, 
   "start_time"   timestamp without time zone DEFAULT NULL, 
   "end_time"   timestamp without time zone DEFAULT NULL, 
   "status"   varchar(10) DEFAULT NULL, 
   "exit_code"   varchar(2500) DEFAULT NULL, 
   "exit_message"   varchar(2500) DEFAULT NULL, 
   "last_updated"   timestamp without time zone DEFAULT NULL, 
   "job_configuration_location"   varchar(2500) DEFAULT NULL, 
   primary key ("job_execution_id")
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_job_execution_context
--

CREATE TABLE  "batch_job_execution_context" (
   "job_execution_id"   bigint NOT NULL, 
   "short_context"   varchar(2500) NOT NULL, 
   "serialized_context"   text, 
   primary key ("job_execution_id")
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_job_execution_params
--

CREATE TABLE  "batch_job_execution_params" (
   "job_execution_id"   bigint NOT NULL, 
   "type_cd"   varchar(6) NOT NULL, 
   "key_name"   varchar(100) NOT NULL, 
   "string_val"   varchar(250) DEFAULT NULL, 
   "date_val"   timestamp without time zone DEFAULT NULL, 
   "long_val"   bigint DEFAULT NULL, 
   "double_val"   double precision DEFAULT NULL, 
   "identifying"   char(1) NOT NULL
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_job_execution_seq
--

CREATE TABLE  "batch_job_execution_seq" (
   "id"   bigint NOT NULL, 
   "unique_key"   char(1) NOT NULL, 
 unique ("unique_key") 
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_job_instance
--

CREATE TABLE  "batch_job_instance" (
   "job_instance_id"   bigint NOT NULL, 
   "version"   bigint DEFAULT NULL, 
   "job_name"   varchar(100) NOT NULL, 
   "job_key"   varchar(32) NOT NULL, 
   primary key ("job_instance_id"),
 unique ("job_name", "job_key") 
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_job_params
--

CREATE TABLE  "batch_job_params" (
   "job_instance_id"   bigint NOT NULL, 
   "type_cd"   varchar(6) NOT NULL, 
   "key_name"   varchar(100) NOT NULL, 
   "string_val"   varchar(250) DEFAULT NULL, 
   "date_val"   timestamp without time zone DEFAULT NULL, 
   "long_val"   bigint DEFAULT NULL, 
   "double_val"   double precision DEFAULT NULL
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_job_seq
--

CREATE TABLE  "batch_job_seq" (
   "id"   bigint NOT NULL, 
   "unique_key"   char(1) NOT NULL, 
 unique ("unique_key") 
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_step_execution
--

CREATE TABLE  "batch_step_execution" (
   "step_execution_id"   bigint NOT NULL, 
   "version"   bigint NOT NULL, 
   "step_name"   varchar(100) NOT NULL, 
   "job_execution_id"   bigint NOT NULL, 
   "start_time"   timestamp without time zone NOT NULL, 
   "end_time"   timestamp without time zone DEFAULT NULL, 
   "status"   varchar(10) DEFAULT NULL, 
   "commit_count"   bigint DEFAULT NULL, 
   "read_count"   bigint DEFAULT NULL, 
   "filter_count"   bigint DEFAULT NULL, 
   "write_count"   bigint DEFAULT NULL, 
   "read_skip_count"   bigint DEFAULT NULL, 
   "write_skip_count"   bigint DEFAULT NULL, 
   "process_skip_count"   bigint DEFAULT NULL, 
   "rollback_count"   bigint DEFAULT NULL, 
   "exit_code"   varchar(2500) DEFAULT NULL, 
   "exit_message"   varchar(2500) DEFAULT NULL, 
   "last_updated"   timestamp without time zone DEFAULT NULL, 
   "job_configuration_location"   varchar(100) DEFAULT NULL, 
   primary key ("step_execution_id")
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_step_execution_context
--

CREATE TABLE  "batch_step_execution_context" (
   "step_execution_id"   bigint NOT NULL, 
   "short_context"   varchar(2500) NOT NULL, 
   "serialized_context"   text, 
   primary key ("step_execution_id")
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table batch_step_execution_seq
--

CREATE TABLE  "batch_step_execution_seq" (
   "id"   bigint NOT NULL, 
   "unique_key"   char(1) NOT NULL, 
 unique ("unique_key") 
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table customer
--

CREATE TABLE  "customer" (
   "id" serial8 ,
   "document_type"   varchar(10) NOT NULL, 
   "document_number"   varchar(11) NOT NULL, 
   "customer_names"   varchar(450) DEFAULT NULL, 
   "customer_address"   varchar(500) NOT NULL, 
   "customer_phone_number"   varchar(15) NOT NULL, 
   "customer_type"   varchar(10) NOT NULL, 
   "customer_creation_date"   timestamp without time zone DEFAULT NULL, 
   "customer_modification_date"   timestamp without time zone DEFAULT NULL, 
   "customer_status"   char(1) NOT NULL, 
   primary key ("id"),
 unique ("document_number") 
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table enterprise
--

CREATE TABLE  "enterprise" (
   "id" serial8 ,
   "address"   varchar(255) DEFAULT NULL, 
   "district"   varchar(255) DEFAULT NULL, 
   "dni"   varchar(255) DEFAULT NULL, 
   "enterprise_creation_date"   timestamp without time zone DEFAULT NULL, 
   "enterprise_modification_date"   timestamp without time zone DEFAULT NULL, 
   "login"   varchar(255) DEFAULT NULL, 
   "password"   varchar(255) DEFAULT NULL, 
   "ruc"   varchar(255) DEFAULT NULL, 
   "social_reason"   varchar(255) DEFAULT NULL, 
   primary key ("id")
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table enterprise_transport
--

CREATE TABLE  "enterprise_transport" (
   "id" serial8 ,
   "enterprise_id"   int NOT NULL, 
   "transport_id"   int NOT NULL, 
   primary key ("id")
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table general_parameter
--

CREATE TABLE  "general_parameter" (
   "id" serial8 ,
   "param_type"   varchar(15) NOT NULL, 
   "param_code"   varchar(10) NOT NULL, 
   "param_description"   varchar(250) NOT NULL, 
   "param_value"   varchar(2500) NOT NULL, 
   "param_creation_date"   timestamp without time zone DEFAULT CURRENT_TIMESTAMP, 
   "param_modification_date"   timestamp without time zone DEFAULT CURRENT_TIMESTAMP, 
   "param_status"   char(1) NOT NULL DEFAULT '1', 
   primary key ("id"),
 unique ("param_code") 
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table guide_cotization
--

CREATE TABLE  "guide_cotization" (
   "id" serial8 ,
   "unit_cost_guide"   decimal(10,2) NOT NULL, 
   "total_weight_dehydrated"   decimal(10,2) NOT NULL, 
   "total_expenses"   decimal(10,2) NOT NULL, 
   "total_decrease"   decimal(10,2) NOT NULL, 
   "total_unit_cost"   decimal(10,2) NOT NULL, 
   "cotization_creation_date"   timestamp without time zone NOT NULL, 
   "guide_head_id"   int NOT NULL, 
   primary key ("id")
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table guide_detail
--

CREATE TABLE  "guide_detail" (
   "id" serial8 ,
   "average"   decimal(19,2) DEFAULT NULL, 
   "boxes_quantity"   int DEFAULT NULL, 
   "dead_quantity"   int DEFAULT NULL, 
   "dead_weight"   decimal(19,2) DEFAULT NULL, 
   "guide_creation_date"   timestamp without time zone DEFAULT NULL, 
   "guide_modification_date"   timestamp without time zone DEFAULT NULL, 
   "initial_weight"   decimal(19,2) DEFAULT NULL, 
   "net_weight"   decimal(19,2) DEFAULT NULL, 
   "product_description"   varchar(255) DEFAULT NULL, 
   "product_quantity"   int DEFAULT NULL, 
   "tara_weight"   decimal(19,2) DEFAULT NULL, 
   "unit_cost"   decimal(19,2) DEFAULT NULL, 
   "guide_head_id"   int NOT NULL, 
   primary key ("id")
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table guide_detail_sales
--

CREATE TABLE  "guide_detail_sales" (
   "id" serial8 ,
   "product_quantity"   int DEFAULT NULL, 
   "metric_unit"   varchar(50) DEFAULT NULL, 
   "total_weight"   decimal(19,2) DEFAULT NULL, 
   "total_cost"   decimal(19,2) DEFAULT NULL, 
   "guide_creation_date"   timestamp without time zone DEFAULT NULL, 
   "guide_modification_date"   timestamp without time zone DEFAULT NULL, 
   "product_label_id"   int NOT NULL, 
   "guide_head_id"   int NOT NULL, 
   primary key ("id")
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table guide_head
--

CREATE TABLE  "guide_head" (
   "id" serial8 ,
   "emission_date"   timestamp without time zone DEFAULT NULL, 
   "end_point"   varchar(255) DEFAULT NULL, 
   "guide_creation_date"   timestamp without time zone DEFAULT NULL, 
   "guide_file"   varchar(255) DEFAULT NULL, 
   "guide_modification_date"   timestamp without time zone DEFAULT NULL, 
   "guide_number"   varchar(255) DEFAULT NULL, 
   "guide_type"   char(1) DEFAULT NULL, 
   "guide_status"   varchar(255) DEFAULT NULL, 
   "invoice_number"   varchar(255) DEFAULT NULL, 
   "observations"   varchar(255) DEFAULT NULL, 
   "reason"   varchar(255) DEFAULT NULL, 
   "start_point"   varchar(255) DEFAULT NULL, 
   "guide_benefied"   char(1) DEFAULT NULL, 
   "guide_cotized"   char(1) DEFAULT NULL, 
   "enterprise_id"   int NOT NULL, 
   "provider_id"   int DEFAULT NULL, 
   "customer_id"   int DEFAULT NULL, 
   "transport_id"   int DEFAULT NULL, 
   primary key ("id"),
 unique ("guide_number") ,
 unique ("invoice_number") 
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table invoice
--

CREATE TABLE  "invoice" (
   "id" serial8 ,
   "invoice_serie"   char(4) NOT NULL, 
   "invoice_number"   char(6) NOT NULL, 
   "invoice_emission_date"   timestamp without time zone NOT NULL, 
   "invoice_expiration_date"   date DEFAULT NULL, 
   "invoice_sunat_code"   varchar(10) DEFAULT NULL, 
   "invoice_sunat_message"   varchar(250) DEFAULT NULL, 
   "guide_head_id"   int NOT NULL, 
   primary key ("id"),
 unique ("invoice_serie", "invoice_number") 
)    ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table movements
--

CREATE TABLE  "movements" (
   "id" serial8 ,
   "movement_date"   date NOT NULL, 
   "movement_type"   varchar(45) NOT NULL, 
   "payment_documentnumber"   varchar(25) NOT NULL, 
   "account_id" int CHECK ("account_id" >= 0) NOT NULL,
   "provider_id"   int DEFAULT NULL, 
   "customer_id"   int DEFAULT NULL, 
   "movement_quantity"   int NOT NULL, 
   "movement_amount"   decimal(10,2) NOT NULL, 
   "movement_observation"   varchar(500) DEFAULT NULL, 
   primary key ("id"),
 unique ("payment_documentnumber") 
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table product_label
--

CREATE TABLE  "product_label" (
   "id" serial8 ,
   "product_label_code"   varchar(10) NOT NULL, 
   "product_label_description"   varchar(250) NOT NULL, 
   "product_label_weight"   decimal(10,5) NOT NULL, 
   "product_label_creation_date"   timestamp without time zone DEFAULT CURRENT_TIMESTAMP, 
   "product_label_modification_date"   timestamp without time zone DEFAULT CURRENT_TIMESTAMP, 
   "product_label_status"   char(1) NOT NULL, 
   primary key ("id"),
 unique ("product_label_code") 
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table product_stock
--

CREATE TABLE  "product_stock" (
   "id" serial8 ,
   "product_stock_cant"   int NOT NULL, 
   "product_stock_creation_date"   timestamp without time zone NOT NULL, 
   "product_label_id"   int NOT NULL, 
   "guide_head_id"   int NOT NULL, 
   primary key ("id")
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table provider
--

CREATE TABLE  "provider" (
   "id" serial8 ,
   "provider_ruc"   varchar(11) NOT NULL, 
   "provider_social_reason"   varchar(450) NOT NULL, 
   "provider_address"   varchar(500) NOT NULL, 
   "provider_phone_number"   varchar(15) NOT NULL, 
   "provider_creation_date"   timestamp without time zone DEFAULT NULL, 
   "provider_modification_date"   timestamp without time zone DEFAULT NULL, 
   "provider_status"   char(1) NOT NULL, 
   "provider_car_number"   varchar(255) DEFAULT NULL, 
   "provider_driver_license"   varchar(255) DEFAULT NULL, 
   primary key ("id"),
 unique ("provider_ruc") 
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table provider_transport
--

CREATE TABLE  "provider_transport" (
   "id" serial8 ,
   "provider_id"   int NOT NULL, 
   "transport_id"   int NOT NULL, 
   primary key ("id")
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table transport
--

CREATE TABLE  "transport" (
   "id" serial8 ,
   "car_number"   varchar(255) DEFAULT NULL, 
   "car_mark"   varchar(255) DEFAULT NULL, 
   "driver_license_number"   varchar(255) DEFAULT NULL, 
   "driver_names"   varchar(255) DEFAULT NULL, 
   "transport_creation_date"   timestamp without time zone DEFAULT NULL, 
   "transport_modification_date"   timestamp without time zone DEFAULT NULL, 
   "transport_status"   varchar(255) DEFAULT NULL, 
   primary key ("id")
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
CREATE INDEX "account_account_owner_idx" ON "account" USING btree ("account_owner");
ALTER TABLE "account" ADD FOREIGN KEY ("account_owner") REFERENCES "customer" ("id");
CREATE INDEX "batch_job_execution_job_instance_id_idx" ON "batch_job_execution" USING btree ("job_instance_id");
ALTER TABLE "batch_job_execution" ADD FOREIGN KEY ("job_instance_id") REFERENCES "batch_job_instance" ("job_instance_id");
ALTER TABLE "batch_job_execution_context" ADD FOREIGN KEY ("job_execution_id") REFERENCES "batch_job_execution" ("job_execution_id");
CREATE INDEX "batch_job_execution_params_job_execution_id_idx" ON "batch_job_execution_params" USING btree ("job_execution_id");
ALTER TABLE "batch_job_execution_params" ADD FOREIGN KEY ("job_execution_id") REFERENCES "batch_job_execution" ("job_execution_id");
CREATE INDEX "batch_job_params_job_instance_id_idx" ON "batch_job_params" USING btree ("job_instance_id");
ALTER TABLE "batch_job_params" ADD FOREIGN KEY ("job_instance_id") REFERENCES "batch_job_instance" ("job_instance_id");
CREATE INDEX "batch_step_execution_job_execution_id_idx" ON "batch_step_execution" USING btree ("job_execution_id");
ALTER TABLE "batch_step_execution" ADD FOREIGN KEY ("job_execution_id") REFERENCES "batch_job_execution" ("job_execution_id");
ALTER TABLE "batch_step_execution_context" ADD FOREIGN KEY ("step_execution_id") REFERENCES "batch_step_execution" ("step_execution_id");
CREATE INDEX "enterprise_transport_enterprise_id_idx" ON "enterprise_transport" USING btree ("enterprise_id");
CREATE INDEX "enterprise_transport_transport_id_idx" ON "enterprise_transport" USING btree ("transport_id");
ALTER TABLE "enterprise_transport" ADD FOREIGN KEY ("enterprise_id") REFERENCES "enterprise" ("id");
ALTER TABLE "enterprise_transport" ADD FOREIGN KEY ("transport_id") REFERENCES "transport" ("id");
CREATE INDEX "guide_cotization_guide_head_id_idx" ON "guide_cotization" USING btree ("guide_head_id");
ALTER TABLE "guide_cotization" ADD FOREIGN KEY ("guide_head_id") REFERENCES "guide_head" ("id");
ALTER TABLE "guide_cotization" ADD FOREIGN KEY ("guide_head_id") REFERENCES "guide_head" ("id");
CREATE INDEX "guide_detail_guide_head_id_idx" ON "guide_detail" USING btree ("guide_head_id");
ALTER TABLE "guide_detail" ADD FOREIGN KEY ("guide_head_id") REFERENCES "guide_head" ("id");
CREATE INDEX "guide_detail_sales_product_label_id_idx" ON "guide_detail_sales" USING btree ("product_label_id");
CREATE INDEX "guide_detail_sales_guide_head_id_idx" ON "guide_detail_sales" USING btree ("guide_head_id");
ALTER TABLE "guide_detail_sales" ADD FOREIGN KEY ("product_label_id") REFERENCES "product_label" ("id");
ALTER TABLE "guide_detail_sales" ADD FOREIGN KEY ("guide_head_id") REFERENCES "guide_head" ("id");
CREATE INDEX "guide_head_provider_id_idx" ON "guide_head" USING btree ("provider_id");
CREATE INDEX "guide_head_enterprise_id_idx" ON "guide_head" USING btree ("enterprise_id");
CREATE INDEX "guide_head_customer_id_idx" ON "guide_head" USING btree ("customer_id");
CREATE INDEX "guide_head_transport_id_idx" ON "guide_head" USING btree ("transport_id");
ALTER TABLE "guide_head" ADD FOREIGN KEY ("provider_id") REFERENCES "provider" ("id");
ALTER TABLE "guide_head" ADD FOREIGN KEY ("transport_id") REFERENCES "transport" ("id");
ALTER TABLE "guide_head" ADD FOREIGN KEY ("customer_id") REFERENCES "customer" ("id");
ALTER TABLE "guide_head" ADD FOREIGN KEY ("enterprise_id") REFERENCES "enterprise" ("id");
CREATE INDEX "invoice_guide_head_id_idx" ON "invoice" USING btree ("guide_head_id");
ALTER TABLE "invoice" ADD FOREIGN KEY ("guide_head_id") REFERENCES "guide_head" ("id");
CREATE INDEX "movements_account_id_idx" ON "movements" USING btree ("account_id");
CREATE INDEX "movements_provider_id_idx" ON "movements" USING btree ("provider_id");
CREATE INDEX "movements_customer_id_idx" ON "movements" USING btree ("customer_id");
ALTER TABLE "movements" ADD FOREIGN KEY ("account_id") REFERENCES "account" ("id");
ALTER TABLE "movements" ADD FOREIGN KEY ("customer_id") REFERENCES "customer" ("id");
ALTER TABLE "movements" ADD FOREIGN KEY ("provider_id") REFERENCES "provider" ("id");
CREATE INDEX "product_stock_guide_head_id_idx" ON "product_stock" USING btree ("guide_head_id");
CREATE INDEX "product_stock_product_label_id_idx" ON "product_stock" USING btree ("product_label_id");
ALTER TABLE "product_stock" ADD FOREIGN KEY ("guide_head_id") REFERENCES "guide_head" ("id");
ALTER TABLE "product_stock" ADD FOREIGN KEY ("product_label_id") REFERENCES "product_label" ("id");
ALTER TABLE "product_stock" ADD FOREIGN KEY ("guide_head_id") REFERENCES "guide_head" ("id");
ALTER TABLE "product_stock" ADD FOREIGN KEY ("product_label_id") REFERENCES "product_label" ("id");
CREATE INDEX "provider_transport_provider_id_idx" ON "provider_transport" USING btree ("provider_id");
CREATE INDEX "provider_transport_transport_id_idx" ON "provider_transport" USING btree ("transport_id");
ALTER TABLE "provider_transport" ADD FOREIGN KEY ("provider_id") REFERENCES "provider" ("id");
ALTER TABLE "provider_transport" ADD FOREIGN KEY ("transport_id") REFERENCES "transport" ("id");
