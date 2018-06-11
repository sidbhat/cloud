-- MySQL dump 10.11
--
-- Host: localhost    Database: oneapp_dev
-- ------------------------------------------------------
-- Server version	5.0.81-community-nt

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL,
  `account_id` varchar(255) default NULL,
  `account_type` varchar(8) default NULL,
  `add_description` varchar(255) default NULL,
  `bill_to_address1` varchar(125) default NULL,
  `bill_to_address2` varchar(125) default NULL,
  `bill_to_city` varchar(150) default NULL,
  `bill_to_country` varchar(50) default NULL,
  `bill_to_state` varchar(50) default NULL,
  `bill_to_zip_code` varchar(150) default NULL,
  `contact_id` bigint(20) default NULL,
  `externalurl` varchar(255) default NULL,
  `follower_cnt` varchar(255) default NULL,
  `headlines` varchar(255) default NULL,
  `industry` varchar(255) default NULL,
  `latest_activity` varchar(255) default NULL,
  `likes_cnt` varchar(255) default NULL,
  `logourl` varchar(255) default NULL,
  `name` varchar(54) default NULL,
  `owner_id` bigint(20) default NULL,
  `ship_to_address1` varchar(125) default NULL,
  `ship_to_address2` varchar(125) default NULL,
  `ship_to_city` varchar(150) default NULL,
  `ship_to_country` varchar(50) default NULL,
  `ship_to_state` varchar(50) default NULL,
  `ship_to_zip_code` varchar(150) default NULL,
  `summary` varchar(255) default NULL,
  `tenant_id` int(11) default NULL,
  `total_employees` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKB9D38A2D6B20F54C` (`contact_id`),
  KEY `FKB9D38A2D92C6FFA2` (`owner_id`),
  CONSTRAINT `FKB9D38A2D92C6FFA2` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKB9D38A2D6B20F54C` FOREIGN KEY (`contact_id`) REFERENCES `contact_details` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_feed`
--

DROP TABLE IF EXISTS `activity_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_feed` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `activity_content` varchar(1024) NOT NULL,
  `config_id` bigint(20) default NULL,
  `created_by_id` bigint(20) NOT NULL,
  `date_created` datetime default NULL,
  `due_date` datetime default NULL,
  `feed_state` varchar(8) NOT NULL,
  `is_task` bit(1) default NULL,
  `last_updated` datetime default NULL,
  `reference_id` varchar(255) default NULL,
  `share_id` bigint(20) default NULL,
  `task_status` bit(1) default NULL,
  `tenant_id` int(11) NOT NULL,
  `visibility` varchar(10) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKC2373CCE69817FD7` (`config_id`),
  KEY `FKC2373CCEA3FCE767` (`created_by_id`),
  CONSTRAINT `FKC2373CCEA3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKC2373CCE69817FD7` FOREIGN KEY (`config_id`) REFERENCES `activity_feed_config` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_feed`
--

LOCK TABLES `activity_feed` WRITE;
/*!40000 ALTER TABLE `activity_feed` DISABLE KEYS */;
INSERT INTO `activity_feed` VALUES (1,2,'this is my first feed',1,1,'2018-06-11 07:34:28','2018-06-13 00:00:00','Active','','2018-06-11 07:35:07','#1',NULL,NULL,1,NULL);
/*!40000 ALTER TABLE `activity_feed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_feed_config`
--

DROP TABLE IF EXISTS `activity_feed_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_feed_config` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `class_name` varchar(255) default NULL,
  `config_name` varchar(255) NOT NULL,
  `created_by_id` bigint(20) default NULL,
  `date_created` datetime default NULL,
  `last_updated` datetime default NULL,
  `share_type` varchar(255) default NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK504EDFD3A3FCE767` (`created_by_id`),
  CONSTRAINT `FK504EDFD3A3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_feed_config`
--

LOCK TABLES `activity_feed_config` WRITE;
/*!40000 ALTER TABLE `activity_feed_config` DISABLE KEYS */;
INSERT INTO `activity_feed_config` VALUES (1,0,NULL,'content',1,'2018-06-11 07:34:28','2018-06-11 07:34:28',NULL,1);
/*!40000 ALTER TABLE `activity_feed_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_feed_drop_down`
--

DROP TABLE IF EXISTS `activity_feed_drop_down`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_feed_drop_down` (
  `activity_feed_shared_depts_id` bigint(20) default NULL,
  `drop_down_id` bigint(20) default NULL,
  `shared_depts_idx` int(11) default NULL,
  KEY `FK9C02B9C13CBABE69` (`drop_down_id`),
  CONSTRAINT `FK9C02B9C13CBABE69` FOREIGN KEY (`drop_down_id`) REFERENCES `drop_down` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_feed_drop_down`
--

LOCK TABLES `activity_feed_drop_down` WRITE;
/*!40000 ALTER TABLE `activity_feed_drop_down` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_feed_drop_down` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_feed_group_details`
--

DROP TABLE IF EXISTS `activity_feed_group_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_feed_group_details` (
  `activity_feed_shared_groups_id` bigint(20) default NULL,
  `group_details_id` bigint(20) default NULL,
  `shared_groups_idx` int(11) default NULL,
  KEY `FK2A22ED1768CC66B` (`group_details_id`),
  CONSTRAINT `FK2A22ED1768CC66B` FOREIGN KEY (`group_details_id`) REFERENCES `group_details` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_feed_group_details`
--

LOCK TABLES `activity_feed_group_details` WRITE;
/*!40000 ALTER TABLE `activity_feed_group_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_feed_group_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_feed_role`
--

DROP TABLE IF EXISTS `activity_feed_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_feed_role` (
  `activity_feed_shared_roles_id` bigint(20) default NULL,
  `role_id` bigint(20) default NULL,
  `shared_roles_idx` int(11) default NULL,
  KEY `FKCFE4CD4781B58BAA` (`role_id`),
  CONSTRAINT `FKCFE4CD4781B58BAA` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_feed_role`
--

LOCK TABLES `activity_feed_role` WRITE;
/*!40000 ALTER TABLE `activity_feed_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_feed_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_feed_user`
--

DROP TABLE IF EXISTS `activity_feed_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_feed_user` (
  `activity_feed_shared_users_id` bigint(20) default NULL,
  `user_id` bigint(20) default NULL,
  `shared_users_idx` int(11) default NULL,
  KEY `FKCFE6389C26E04F8A` (`user_id`),
  CONSTRAINT `FKCFE6389C26E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_feed_user`
--

LOCK TABLES `activity_feed_user` WRITE;
/*!40000 ALTER TABLE `activity_feed_user` DISABLE KEYS */;
INSERT INTO `activity_feed_user` VALUES (1,3,0);
/*!40000 ALTER TABLE `activity_feed_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_feed_user_state`
--

DROP TABLE IF EXISTS `activity_feed_user_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_feed_user_state` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `user_feed_state` varchar(8) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKD4F7B2E26E04F8A` (`user_id`),
  KEY `FKD4F7B2E3E183259` (`feed_id`),
  CONSTRAINT `FKD4F7B2E3E183259` FOREIGN KEY (`feed_id`) REFERENCES `activity_feed` (`id`),
  CONSTRAINT `FKD4F7B2E26E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_feed_user_state`
--

LOCK TABLES `activity_feed_user_state` WRITE;
/*!40000 ALTER TABLE `activity_feed_user_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_feed_user_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_notification`
--

DROP TABLE IF EXISTS `activity_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_notification` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `action_by_id` bigint(20) NOT NULL,
  `action_on_feed_id` bigint(20) NOT NULL,
  `action_time` datetime NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `user_feed_state` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKFD0FB01B8D1491A2` (`action_on_feed_id`),
  KEY `FKFD0FB01B9D08B435` (`action_by_id`),
  CONSTRAINT `FKFD0FB01B9D08B435` FOREIGN KEY (`action_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKFD0FB01B8D1491A2` FOREIGN KEY (`action_on_feed_id`) REFERENCES `activity_feed` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_notification`
--

LOCK TABLES `activity_notification` WRITE;
/*!40000 ALTER TABLE `activity_notification` DISABLE KEYS */;
INSERT INTO `activity_notification` VALUES (1,0,1,1,'2018-06-11 07:34:28',1,'Jack shared a feed with you'),(2,0,1,1,'2018-06-11 07:34:33',1,'Jack rated the feed'),(3,0,1,1,'2018-06-11 07:34:37',1,'Jack commented on feed'),(4,0,1,1,'2018-06-11 07:35:07',1,'Jack added a task');
/*!40000 ALTER TABLE `activity_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_log`
--

DROP TABLE IF EXISTS `api_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_log` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `call_count` bigint(20) default NULL,
  `log_date` datetime default NULL,
  `user_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_log`
--

LOCK TABLES `api_log` WRITE;
/*!40000 ALTER TABLE `api_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_log`
--

DROP TABLE IF EXISTS `app_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_log` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `device_type` varchar(255) NOT NULL,
  `ip` varchar(125) NOT NULL,
  `last_updated` datetime NOT NULL,
  `msg` varchar(2056) default NULL,
  `msg_type` varchar(1) default NULL,
  `uri` varchar(256) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKD0B2542626E04F8A` (`user_id`),
  CONSTRAINT `FKD0B2542626E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_log`
--

LOCK TABLES `app_log` WRITE;
/*!40000 ALTER TABLE `app_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_conf`
--

DROP TABLE IF EXISTS `application_conf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_conf` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `asyn_email_job_interval` int(11) default NULL,
  `form` bigint(20) NOT NULL,
  `form_for_trial` bigint(20) NOT NULL,
  `log_info` bit(1) NOT NULL,
  `max_form_controls` int(11) NOT NULL,
  `rules_job_interval` int(11) default NULL,
  `send_email_default_from` varchar(255) default NULL,
  `track_changes` bit(1) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_conf`
--

LOCK TABLES `application_conf` WRITE;
/*!40000 ALTER TABLE `application_conf` DISABLE KEYS */;
INSERT INTO `application_conf` VALUES (1,0,NULL,75,2,'\0',40,NULL,NULL,'\0');
/*!40000 ALTER TABLE `application_conf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_conf_form`
--

DROP TABLE IF EXISTS `application_conf_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_conf_form` (
  `application_conf_copy_forms_id` bigint(20) default NULL,
  `form_id` bigint(20) default NULL,
  `copy_forms_idx` int(11) default NULL,
  `application_conf_copy_forms_trial_id` bigint(20) default NULL,
  `copy_forms_trial_idx` int(11) default NULL,
  KEY `FK555A88D035BB3DB` (`form_id`),
  CONSTRAINT `FK555A88D035BB3DB` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_conf_form`
--

LOCK TABLES `application_conf_form` WRITE;
/*!40000 ALTER TABLE `application_conf_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `application_conf_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asynchronous_email_storage`
--

DROP TABLE IF EXISTS `asynchronous_email_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asynchronous_email_storage` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `email_data` longtext NOT NULL,
  `email_from` varchar(255) NOT NULL,
  `email_sent_status` int(11) NOT NULL,
  `email_subject` varchar(255) NOT NULL,
  `emailto` varchar(255) NOT NULL,
  `is_html` bit(1) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asynchronous_email_storage`
--

LOCK TABLES `asynchronous_email_storage` WRITE;
/*!40000 ALTER TABLE `asynchronous_email_storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `asynchronous_email_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `content_type` varchar(255) default NULL,
  `date_created` datetime NOT NULL,
  `domain_instance_id` bigint(20) default NULL,
  `ext` varchar(255) default NULL,
  `input_name` varchar(255) NOT NULL,
  `length` bigint(20) NOT NULL,
  `lnk_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `poster_class` varchar(255) NOT NULL,
  `poster_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK8AF75923BE8D343C` (`lnk_id`),
  CONSTRAINT `FK8AF75923BE8D343C` FOREIGN KEY (`lnk_id`) REFERENCES `attachment_link` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment_link`
--

DROP TABLE IF EXISTS `attachment_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment_link` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `reference_class` varchar(255) NOT NULL,
  `reference_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment_link`
--

LOCK TABLES `attachment_link` WRITE;
/*!40000 ALTER TABLE `attachment_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `attachment_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base`
--

DROP TABLE IF EXISTS `base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `active` bit(1) NOT NULL,
  `archived` bit(1) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `status_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK2E06D1ADA81A69` (`status_id`),
  CONSTRAINT `FK2E06D1ADA81A69` FOREIGN KEY (`status_id`) REFERENCES `drop_down` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base`
--

LOCK TABLES `base` WRITE;
/*!40000 ALTER TABLE `base` DISABLE KEYS */;
INSERT INTO `base` VALUES (1,1,'','\0','2018-06-10 19:26:36','2018-06-11 07:31:35',NULL);
/*!40000 ALTER TABLE `base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_approval`
--

DROP TABLE IF EXISTS `base_approval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_approval` (
  `id` bigint(20) NOT NULL,
  `approved_by_id` bigint(20) default NULL,
  `approved_date` datetime default NULL,
  `created_by_id` bigint(20) default NULL,
  `note` varchar(255) default NULL,
  `updated_by_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKC838ABF1A3FCE767` (`created_by_id`),
  KEY `FKC838ABF15A7477F6` (`approved_by_id`),
  KEY `FKC838ABF1C4EB5EFA` (`updated_by_id`),
  CONSTRAINT `FKC838ABF1C4EB5EFA` FOREIGN KEY (`updated_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKC838ABF15A7477F6` FOREIGN KEY (`approved_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKC838ABF1A3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_approval`
--

LOCK TABLES `base_approval` WRITE;
/*!40000 ALTER TABLE `base_approval` DISABLE KEYS */;
/*!40000 ALTER TABLE `base_approval` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_object`
--

DROP TABLE IF EXISTS `base_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_object` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `account_id` bigint(20) default NULL,
  `amount` decimal(19,2) default NULL,
  `date_created` datetime NOT NULL,
  `issue_date` datetime default NULL,
  `last_updated` datetime NOT NULL,
  `tax_amount` decimal(19,2) default NULL,
  `tax_amount2` decimal(19,2) default NULL,
  `tax_percentage` decimal(19,2) default NULL,
  `tax_percentage2` decimal(19,2) default NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK8A657C6D314312EA` (`account_id`),
  CONSTRAINT `FK8A657C6D314312EA` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_object`
--

LOCK TABLES `base_object` WRITE;
/*!40000 ALTER TABLE `base_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `base_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_object_base_object_item`
--

DROP TABLE IF EXISTS `base_object_base_object_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_object_base_object_item` (
  `base_object_items_id` bigint(20) default NULL,
  `base_object_item_id` bigint(20) default NULL,
  KEY `FKBBFD2137FCA6B8AC` (`base_object_items_id`),
  KEY `FKBBFD213787D26648` (`base_object_item_id`),
  CONSTRAINT `FKBBFD213787D26648` FOREIGN KEY (`base_object_item_id`) REFERENCES `base_object_item` (`id`),
  CONSTRAINT `FKBBFD2137FCA6B8AC` FOREIGN KEY (`base_object_items_id`) REFERENCES `base_object` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_object_base_object_item`
--

LOCK TABLES `base_object_base_object_item` WRITE;
/*!40000 ALTER TABLE `base_object_base_object_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `base_object_base_object_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_object_item`
--

DROP TABLE IF EXISTS `base_object_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_object_item` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `description` varchar(255) NOT NULL,
  `last_updated` datetime NOT NULL,
  `parent_id` int(11) NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_object_item`
--

LOCK TABLES `base_object_item` WRITE;
/*!40000 ALTER TABLE `base_object_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `base_object_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `billing`
--

DROP TABLE IF EXISTS `billing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billing` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `amount` bigint(20) default NULL,
  `client_id` bigint(20) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `description` varchar(255) default NULL,
  `intial_amount` bigint(20) default NULL,
  `reference_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKF974227B4B3CA38A` (`client_id`),
  CONSTRAINT `FKF974227B4B3CA38A` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billing`
--

LOCK TABLES `billing` WRITE;
/*!40000 ALTER TABLE `billing` DISABLE KEYS */;
/*!40000 ALTER TABLE `billing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `billing_history`
--

DROP TABLE IF EXISTS `billing_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billing_history` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `amount` decimal(19,2) NOT NULL,
  `bill_date` datetime NOT NULL,
  `client_id` bigint(20) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `description` varchar(255) default NULL,
  `paypal_transaction_id` varchar(255) default NULL,
  `transaction_id` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK5CE302D04B3CA38A` (`client_id`),
  CONSTRAINT `FK5CE302D04B3CA38A` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billing_history`
--

LOCK TABLES `billing_history` WRITE;
/*!40000 ALTER TABLE `billing_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `billing_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blocked_ip`
--

DROP TABLE IF EXISTS `blocked_ip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blocked_ip` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `form_id` bigint(20) default NULL,
  `ip_adress` varchar(255) NOT NULL,
  `reason` varchar(255) NOT NULL,
  `username` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blocked_ip`
--

LOCK TABLES `blocked_ip` WRITE;
/*!40000 ALTER TABLE `blocked_ip` DISABLE KEYS */;
/*!40000 ALTER TABLE `blocked_ip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buyer_information`
--

DROP TABLE IF EXISTS `buyer_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buyer_information` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `address_confirmed` bit(1) NOT NULL,
  `city` varchar(255) default NULL,
  `company_name` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `country_code` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `last_name` varchar(255) default NULL,
  `phone_number` varchar(255) default NULL,
  `receiver_name` varchar(255) default NULL,
  `state` varchar(255) default NULL,
  `street` varchar(255) default NULL,
  `unique_customer_id` varchar(255) default NULL,
  `zip` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyer_information`
--

LOCK TABLES `buyer_information` WRITE;
/*!40000 ALTER TABLE `buyer_information` DISABLE KEYS */;
/*!40000 ALTER TABLE `buyer_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `id` bigint(20) NOT NULL,
  `consumer_key` varchar(54) default NULL,
  `description` varchar(1024) default NULL,
  `disable_email` bit(1) default NULL,
  `domain` varchar(255) default NULL,
  `externalurl` varchar(255) default NULL,
  `form` bigint(20) default NULL,
  `gcm_api_key` longtext,
  `is_recursive` bit(1) default NULL,
  `license_collaboration` bit(1) default NULL,
  `license_form_builder` bit(1) default NULL,
  `logo` tinyblob,
  `logourl` varchar(255) default NULL,
  `max_attachment_size` bigint(20) default NULL,
  `max_email_account` bigint(20) default NULL,
  `max_email_fetch_count` bigint(20) default NULL,
  `max_form_entries` bigint(20) default NULL,
  `max_users` bigint(20) default NULL,
  `name` varchar(54) default NULL,
  `plan_id` bigint(20) default NULL,
  `valid_from` datetime default NULL,
  `valid_to` datetime default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `consumer_key` (`consumer_key`),
  UNIQUE KEY `name` (`name`),
  KEY `FKAF12F3CB1232AACA` (`plan_id`),
  CONSTRAINT `FKAF12F3CB1232AACA` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'5d514b3b38bd41a487b7d0014ea6381b',NULL,'\0',NULL,NULL,5,NULL,'\0','','\0',NULL,NULL,2000,2,100,100,5,'Default Client',NULL,NULL,NULL);
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_user`
--

DROP TABLE IF EXISTS `client_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_user` (
  `client_user_id` bigint(20) default NULL,
  `user_id` bigint(20) default NULL,
  KEY `FKF5A657BF26E04F8A` (`user_id`),
  KEY `FKF5A657BF45394B16` (`client_user_id`),
  CONSTRAINT `FKF5A657BF45394B16` FOREIGN KEY (`client_user_id`) REFERENCES `client` (`id`),
  CONSTRAINT `FKF5A657BF26E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_user`
--

LOCK TABLES `client_user` WRITE;
/*!40000 ALTER TABLE `client_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `client_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `body` longtext NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `poster_class` varchar(255) NOT NULL,
  `poster_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,0,'Thanks for adding','2018-06-11 07:34:37','2018-06-11 07:34:37','com.oneapp.cloud.core.User',1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_link`
--

DROP TABLE IF EXISTS `comment_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_link` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `comment_id` bigint(20) NOT NULL,
  `comment_ref` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK2F689DDAF37C8E8A` (`comment_id`),
  CONSTRAINT `FK2F689DDAF37C8E8A` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_link`
--

LOCK TABLES `comment_link` WRITE;
/*!40000 ALTER TABLE `comment_link` DISABLE KEYS */;
INSERT INTO `comment_link` VALUES (1,0,1,1,'activityFeed');
/*!40000 ALTER TABLE `comment_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_activity`
--

DROP TABLE IF EXISTS `company_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company_activity` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `activity_content` varchar(512) NOT NULL,
  `activity_date` datetime NOT NULL,
  `created_by_id` bigint(20) NOT NULL,
  `date_created` datetime default NULL,
  `date_updated` datetime default NULL,
  `media` mediumblob,
  `media_name` varchar(255) default NULL,
  `media_type` varchar(255) default NULL,
  `share_action_abs_link` varchar(255) default NULL,
  `share_action_rel_link` varchar(255) default NULL,
  `share_id` bigint(20) default NULL,
  `share_link` varchar(255) default NULL,
  `share_type` varchar(255) default NULL,
  `tenant_id` int(11) NOT NULL,
  `url` varchar(255) default NULL,
  `username` varchar(255) NOT NULL,
  `visibility` varchar(7) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKB449A491A3FCE767` (`created_by_id`),
  CONSTRAINT `FKB449A491A3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_activity`
--

LOCK TABLES `company_activity` WRITE;
/*!40000 ALTER TABLE `company_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_details`
--

DROP TABLE IF EXISTS `contact_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_details` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `add_description` varchar(2048) default NULL,
  `birthday` datetime default NULL,
  `city` varchar(255) default NULL,
  `company_name` varchar(255) default NULL,
  `contact_date` varchar(255) default NULL,
  `contact_id` varchar(255) default NULL,
  `contact_name` varchar(54) NOT NULL,
  `created_by_id` bigint(20) default NULL,
  `current_status` varchar(255) default NULL,
  `department` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `facebook_pictureurl` varchar(255) default NULL,
  `fax` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `follower_cnt` varchar(255) default NULL,
  `friend_cnt` varchar(255) default NULL,
  `headlines` varchar(255) default NULL,
  `last_name` varchar(255) default NULL,
  `last_updated` datetime NOT NULL,
  `latest_activity` varchar(255) default NULL,
  `linkedin_pictureurl` varchar(255) default NULL,
  `location` varchar(255) default NULL,
  `mobile` varchar(255) default NULL,
  `personal` bit(1) NOT NULL,
  `phone` varchar(255) default NULL,
  `picture` varchar(255) default NULL,
  `pictureurl` varchar(255) default NULL,
  `profileurl` varchar(255) default NULL,
  `recommendations` varchar(255) default NULL,
  `source` varchar(255) default NULL,
  `summary` varchar(255) default NULL,
  `tenant_id` int(11) NOT NULL,
  `title` varchar(255) default NULL,
  `twitter_pictureurl` varchar(255) default NULL,
  `user` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKA3499D23A3FCE767` (`created_by_id`),
  CONSTRAINT `FKA3499D23A3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_details`
--

LOCK TABLES `contact_details` WRITE;
/*!40000 ALTER TABLE `contact_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `currency_map`
--

DROP TABLE IF EXISTS `currency_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency_map` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `currency_map`
--

LOCK TABLES `currency_map` WRITE;
/*!40000 ALTER TABLE `currency_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `currency_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `device_id` varchar(255) NOT NULL,
  `device_user_id` bigint(20) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `token` longtext NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKB06B1E569CDA5EA1` (`device_user_id`),
  CONSTRAINT `FKB06B1E569CDA5EA1` FOREIGN KEY (`device_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain_class`
--

DROP TABLE IF EXISTS `domain_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_class` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `date_created` datetime default NULL,
  `last_updated` datetime default NULL,
  `name` varchar(255) NOT NULL,
  `source` longtext NOT NULL,
  `updated` bit(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain_class`
--

LOCK TABLES `domain_class` WRITE;
/*!40000 ALTER TABLE `domain_class` DISABLE KEYS */;
INSERT INTO `domain_class` VALUES (1,4,'2018-06-11 07:22:13','2018-06-11 08:33:15','Form1.Form1','package Form1\nclass Form1 implements com.macrobit.grails.plugins.attachmentable.core.Attachmentable {\nString field1528716120384\nString field1528716120908\nString field1528716764681\nString field1528719771630\nString field1528717878695\n\n com.oneapp.cloud.core.User createdBy \n 	 com.oneapp.cloud.core.User updatedBy \n Date dateCreated\nDate lastUpdated\nstatic constraints = {\nfield1528716120384 nullable:true, required: false,maxSize:255\nfield1528716120908 nullable:true, required: false,maxSize:255\nfield1528716764681 nullable:true, required: false,maxSize:255\nfield1528719771630 nullable:true, required: false\nfield1528717878695 nullable:true, required: false,maxSize:255\n createdBy nullable:true, blank:true \n updatedBy nullable:true, blank:true \n}\nstatic mapping = { \n}\nstatic hasMany = []\n}','');
/*!40000 ALTER TABLE `domain_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain_tenant_map`
--

DROP TABLE IF EXISTS `domain_tenant_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_tenant_map` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `domain_name` varchar(255) NOT NULL,
  `mapped_tenant_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain_tenant_map`
--

LOCK TABLES `domain_tenant_map` WRITE;
/*!40000 ALTER TABLE `domain_tenant_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `domain_tenant_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drop_down`
--

DROP TABLE IF EXISTS `drop_down`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drop_down` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drop_down`
--

LOCK TABLES `drop_down` WRITE;
/*!40000 ALTER TABLE `drop_down` DISABLE KEYS */;
INSERT INTO `drop_down` VALUES (1,0,'Internal','INTERNAL',1,'EMPLOYEE_TYPE'),(2,0,'External','EXTERNAL',1,'EMPLOYEE_TYPE'),(3,0,'Added','ADDED',1,'UPDATE_TYPE'),(4,0,'Deleted','DELETED',1,'UPDATE_TYPE'),(5,0,'Updated','UPDATED',1,'UPDATE_TYPE'),(6,0,'Date','DATE',1,'UPDATE_TYPE'),(7,0,'All','ALL',1,'SHARE_TYPE'),(8,0,'Group','GROUP',1,'SHARE_TYPE'),(9,0,'Private','PRIVATE',1,'SHARE_TYPE'),(10,0,'Company','COMPANY',1,'GROUP_TYPE'),(11,0,'HR','HR',1,'DEPARTMENT'),(12,0,'Sales','Sales',1,'DEPARTMENT'),(13,0,'Finance','Finance',1,'DEPARTMENT'),(14,0,'Accounting','Accounting',1,'DEPARTMENT'),(15,0,'NA Sales','NA Sales',1,'SUB_ORG'),(16,0,'NA Field Services','NA Field Services',1,'SUB_ORG'),(17,0,'Internal','INTERNAL',1,'EMPLOYEE_TYPE');
/*!40000 ALTER TABLE `drop_down` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_details`
--

DROP TABLE IF EXISTS `email_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_details` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `attachment_name` varchar(255) default NULL,
  `content` longtext,
  `created_by_id` bigint(20) default NULL,
  `email_from` varchar(255) default NULL,
  `email_to` longtext,
  `message_number` bigint(20) default NULL,
  `message_time` datetime default NULL,
  `msg_size` double default NULL,
  `rule_account` bigint(20) NOT NULL,
  `subject` varchar(1024) default NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK7363889FA3FCE767` (`created_by_id`),
  CONSTRAINT `FK7363889FA3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_details`
--

LOCK TABLES `email_details` WRITE;
/*!40000 ALTER TABLE `email_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_settings`
--

DROP TABLE IF EXISTS `email_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_settings` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `email_address` varchar(255) default NULL,
  `password` varchar(255) NOT NULL,
  `pop_serverurl` varchar(255) default NULL,
  `port` varchar(255) default NULL,
  `secure_connection` bit(1) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK104AF16626E04F8A` (`user_id`),
  CONSTRAINT `FK104AF16626E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_settings`
--

LOCK TABLES `email_settings` WRITE;
/*!40000 ALTER TABLE `email_settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exchange_rate`
--

DROP TABLE IF EXISTS `exchange_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exchange_rate` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `base_currency` varchar(255) NOT NULL,
  `date` datetime NOT NULL,
  `rate` decimal(19,2) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `to_currency` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `to_currency` (`to_currency`,`base_currency`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exchange_rate`
--

LOCK TABLES `exchange_rate` WRITE;
/*!40000 ALTER TABLE `exchange_rate` DISABLE KEYS */;
INSERT INTO `exchange_rate` VALUES (1,0,'EUR','2018-06-10 19:26:36','1.46',0,'USD'),(2,0,'EUR','2018-06-10 19:26:36','1.33',0,'GBP'),(3,0,'JPY','2018-06-10 19:26:36','0.01',0,'USD'),(4,0,'USD','2018-06-10 19:26:36','111.34',0,'JPY'),(5,0,'GBP','2018-06-10 19:26:36','2.02',0,'USD');
/*!40000 ALTER TABLE `exchange_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `date_created` datetime default NULL,
  `form_id` bigint(20) NOT NULL,
  `last_updated` datetime default NULL,
  `name` varchar(255) NOT NULL,
  `sequence` int(11) NOT NULL,
  `settings` longtext NOT NULL,
  `type` varchar(255) NOT NULL,
  `fields_list_idx` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK5CEA0FA35BB3DB` (`form_id`),
  CONSTRAINT `FK5CEA0FA35BB3DB` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field`
--

LOCK TABLES `field` WRITE;
/*!40000 ALTER TABLE `field` DISABLE KEYS */;
INSERT INTO `field` VALUES (1,0,'2018-06-11 07:22:13',1,'2018-06-11 07:22:13','field1528716120384',0,'{\"en\":{\"text\":\"Rich Text 1\",\"classes\":[\"leftAlign\",\"middleAlign\"],\"styles\":{\"fontFamily\":\"\",\"fontSize\":\"default\",\"fontStyles\":[0,0,0]}},\"zh_CN\":{\"text\":\"????? 1\",\"classes\":[\"rightAlign\",\"middleAlign\"],\"styles\":{\"fontFamily\":\"\",\"fontSize\":\"default\",\"fontStyles\":[0,0,0]}},\"_persistable\":true,\"styles\":{\"color\":\"000000\",\"backgroundColor\":\"default\"}}','PlainText',0),(2,0,'2018-06-11 07:22:13',1,'2018-06-11 07:22:13','field1528716120908',1,'{\"en\":{\"label\":\"Single Line Text 1\",\"value\":\"\",\"description\":\"\",\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[1,0,0]}},\"zh_CN\":{\"label\":\"?????? 1\",\"value\":\"\",\"description\":\"\",\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[1,0,0]}},\"_persistable\":true,\"required\":false,\"hideFromUser\":false,\"mapMasterForm\":\"\",\"fieldSize\":\"mClass\",\"minRange\":\"0\",\"maxRange\":\"\",\"mapMasterField\":\"\",\"restriction\":\"no\",\"styles\":{\"label\":{\"color\":\"default\",\"backgroundColor\":\"default\"},\"value\":{\"color\":\"default\",\"backgroundColor\":\"default\"},\"description\":{\"color\":\"777777\",\"backgroundColor\":\"default\"}}}','SingleLineText',1),(3,0,'2018-06-11 07:32:49',1,'2018-06-11 07:32:49','field1528716764681',2,'{\"en\":{\"label\":\"Like/Dislike 1\",\"value\":[\"Like\",\"Dislike\"],\"description\":\"\",\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[1,0,0]}},\"zh_CN\":{\"label\":\"?????? 1\",\"value\":[\"1\",\"0\"],\"description\":\"\",\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[1,0,0]}},\"_persistable\":true,\"required\":false,\"hideFromUser\":false,\"restriction\":\"no\",\"likeAndVote\":false,\"styles\":{\"label\":{\"color\":\"default\",\"backgroundColor\":\"\"},\"value\":{\"color\":\"default\",\"backgroundColor\":\"default\"},\"description\":{\"color\":\"777777\",\"backgroundColor\":\"default\"}}}','LikeDislikeButton',2),(4,1,'2018-06-11 07:51:20',1,'2018-06-11 08:23:04','field1528717878695',4,'{\"en\":{\"label\":\"Likert 1\",\"value\":\"\",\"description\":\"\",\"rows\":[\"Usability\",\"Interface Look\",\"Colors\",\"Clarity\"],\"columns\":[\"Very Satisfied\",\"Satisfied\",\"Neutral\",\"Dissatisfied\"],\"switchRowCol\":false,\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[1,0,0]}},\"zh_CN\":{\"label\":\"?????? 1\",\"value\":\"\",\"description\":\"\",\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[1,0,0]}},\"_persistable\":true,\"required\":false,\"hideFromUser\":false,\"moodRate\":false,\"restriction\":\"no\",\"_randomize\":false,\"styles\":{\"label\":{\"color\":\"default\",\"backgroundColor\":\"default\"},\"value\":{\"color\":\"default\",\"backgroundColor\":\"default\"},\"description\":{\"color\":\"777777\",\"backgroundColor\":\"default\"}}}','Likert',4),(5,1,'2018-06-11 08:22:59',1,'2018-06-11 08:23:04','field1528719771630',3,'{\"en\":{\"label\":\" 1\",\"value\":\"\",\"description\":\"\",\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[0,0,0]}},\"zh_CN\":{\"label\":\"?????? 1\",\"value\":\"\",\"description\":\"\",\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[0,0,0]}},\"_persistable\":true,\"required\":false,\"hideFromUser\":false,\"mapMasterForm\":\"\",\"mapMasterField\":\"\",\"subForm\":\"\",\"restriction\":\"no\",\"styles\":{\"label\":{\"color\":\"default\",\"backgroundColor\":\"default\"},\"value\":{\"color\":\"default\",\"backgroundColor\":\"default\"},\"description\":{\"color\":\"777777\",\"backgroundColor\":\"default\"}}}','PageBreak',3);
/*!40000 ALTER TABLE `field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form`
--

DROP TABLE IF EXISTS `form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `created_by_id` bigint(20) default NULL,
  `date_created` datetime NOT NULL,
  `description` varchar(255) default NULL,
  `domain_class_id` bigint(20) NOT NULL,
  `form_cat` varchar(255) default NULL,
  `last_updated` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `number_of_column_in_list` int(11) default NULL,
  `number_of_row_per_page` int(11) default NULL,
  `persistable_fields_count` int(11) NOT NULL,
  `settings` longtext NOT NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK300CC4D6ACFDB0` (`domain_class_id`),
  KEY `FK300CC4A3FCE767` (`created_by_id`),
  CONSTRAINT `FK300CC4A3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK300CC4D6ACFDB0` FOREIGN KEY (`domain_class_id`) REFERENCES `domain_class` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form`
--

LOCK TABLES `form` WRITE;
/*!40000 ALTER TABLE `form` DISABLE KEYS */;
INSERT INTO `form` VALUES (1,6,1,'2018-06-11 07:22:13',NULL,1,'N','2018-06-11 08:33:15','Form1',NULL,NULL,2,'{\"en\":{\"name\":\"First Form\",\"description\":\"Please fill out this form. Thank you!\",\"classes\":[\"leftAlign\"],\"heading\":\"h2\",\"styles\":{\"fontFamily\":\"Lucida Sans Unicode\",\"fontSize\":\"13\",\"fontStyles\":[1,0,0]}},\"zh_CN\":{\"name\":\"??\",\"classes\":[\"rightAlign\"],\"heading\":\"h2\",\"styles\":{\"fontFamily\":\"default\",\"fontSize\":\"default\",\"fontStyles\":[1,0,0]}},\"styles\":{\"form\":{\"color\":\"default\",\"backgroundColor\":\"cbcefb\"},\"heading\":{\"color\":\"default\",\"backgroundColor\":\"default\"},\"description\":{\"color\":\"default\",\"backgroundColor\":\"default\"}},\"reCaptcha\":false,\"width\":\"640px\",\"themeText\":\"none\",\"labelDisplay\":\"0\"}',1);
/*!40000 ALTER TABLE `form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form1`
--

DROP TABLE IF EXISTS `form1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form1` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL default '0',
  `created_by_id` bigint(20) default NULL,
  `updated_by_id` bigint(20) default NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `field1528716120384` varchar(255) default NULL,
  `field1528716120908` varchar(255) default NULL,
  `field1528716764681` varchar(255) default NULL,
  `field1528717878695` longtext,
  `field1528719771630` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `created_by_id` (`created_by_id`),
  KEY `updated_by_id` (`updated_by_id`),
  CONSTRAINT `form1_ibfk_1` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `form1_ibfk_2` FOREIGN KEY (`updated_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form1`
--

LOCK TABLES `form1` WRITE;
/*!40000 ALTER TABLE `form1` DISABLE KEYS */;
INSERT INTO `form1` VALUES (1,1,1,1,'2018-06-11 07:30:15','2018-06-11 07:30:20',NULL,'new entry updated',NULL,NULL,NULL),(2,0,1,NULL,'2018-06-11 08:33:38','2018-06-11 08:33:38',NULL,NULL,NULL,'[0, null, null, null]',NULL);
/*!40000 ALTER TABLE `form1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_admin`
--

DROP TABLE IF EXISTS `form_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_admin` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `conf_on_create` bit(1) default NULL,
  `conf_on_update` bit(1) default NULL,
  `date_created` datetime NOT NULL,
  `ext_noti_on_create` bit(1) default NULL,
  `ext_noti_on_update` bit(1) default NULL,
  `field_rules_data` longtext,
  `form_id` bigint(20) NOT NULL,
  `form_login` varchar(8) default NULL,
  `form_password` varchar(255) default NULL,
  `form_submission_action` varchar(255) default NULL,
  `form_submission_to_external` longtext,
  `form_submit_message` varchar(255) default NULL,
  `form_type` varchar(8) default NULL,
  `last_updated` datetime NOT NULL,
  `mail_chimp_details_id` bigint(20) default NULL,
  `noti_on_create` bit(1) default NULL,
  `noti_on_update` bit(1) default NULL,
  `open_for_edit` bit(1) default NULL,
  `page_rules_data` longtext,
  `published` bit(1) NOT NULL,
  `query` varchar(255) default NULL,
  `redirect_url` varchar(255) default NULL,
  `searchable` bit(1) default NULL,
  `shorturl` varchar(255) default NULL,
  `show_status_to_user` bit(1) NOT NULL,
  `special_condition` bit(1) NOT NULL,
  `status_field_id` bigint(20) default NULL,
  `track_changes` bit(1) NOT NULL,
  `web_hook_details_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKEDED01435BB3DB` (`form_id`),
  KEY `FKEDED0143EF80F5` (`web_hook_details_id`),
  KEY `FKEDED0143F0AA52B` (`mail_chimp_details_id`),
  KEY `FKEDED0147594B4A6` (`status_field_id`),
  CONSTRAINT `FKEDED0147594B4A6` FOREIGN KEY (`status_field_id`) REFERENCES `field` (`id`),
  CONSTRAINT `FKEDED01435BB3DB` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`),
  CONSTRAINT `FKEDED0143EF80F5` FOREIGN KEY (`web_hook_details_id`) REFERENCES `web_hook_details` (`id`),
  CONSTRAINT `FKEDED0143F0AA52B` FOREIGN KEY (`mail_chimp_details_id`) REFERENCES `mail_chimp_details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_admin`
--

LOCK TABLES `form_admin` WRITE;
/*!40000 ALTER TABLE `form_admin` DISABLE KEYS */;
INSERT INTO `form_admin` VALUES (1,1,'\0','\0','2018-06-11 07:22:28','\0','\0',NULL,1,'Public',NULL,'Eamil',NULL,NULL,'Survey','2018-06-11 07:30:02',NULL,'','','',NULL,'',NULL,NULL,'','http://tinyurl.com/y8hpxpjl','\0','\0',NULL,'\0',NULL);
/*!40000 ALTER TABLE `form_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_admin_block_user_editing`
--

DROP TABLE IF EXISTS `form_admin_block_user_editing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_admin_block_user_editing` (
  `form_admin_id` bigint(20) default NULL,
  `block_user_editing_string` varchar(255) default NULL,
  `block_user_editing_idx` int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_admin_block_user_editing`
--

LOCK TABLES `form_admin_block_user_editing` WRITE;
/*!40000 ALTER TABLE `form_admin_block_user_editing` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_admin_block_user_editing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_admin_form_email_field`
--

DROP TABLE IF EXISTS `form_admin_form_email_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_admin_form_email_field` (
  `form_admin_id` bigint(20) default NULL,
  `form_email_field_long` bigint(20) default NULL,
  `form_email_field_idx` int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_admin_form_email_field`
--

LOCK TABLES `form_admin_form_email_field` WRITE;
/*!40000 ALTER TABLE `form_admin_form_email_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_admin_form_email_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_admin_user`
--

DROP TABLE IF EXISTS `form_admin_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_admin_user` (
  `form_admin_form_submission_to_id` bigint(20) default NULL,
  `user_id` bigint(20) default NULL,
  `form_submission_to_idx` int(11) default NULL,
  `form_admin_published_with_id` bigint(20) default NULL,
  `published_with_idx` int(11) default NULL,
  KEY `FKA375C11626E04F8A` (`user_id`),
  CONSTRAINT `FKA375C11626E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_admin_user`
--

LOCK TABLES `form_admin_user` WRITE;
/*!40000 ALTER TABLE `form_admin_user` DISABLE KEYS */;
INSERT INTO `form_admin_user` VALUES (1,1,0,NULL,NULL);
/*!40000 ALTER TABLE `form_admin_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_counter`
--

DROP TABLE IF EXISTS `form_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_counter` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_counter`
--

LOCK TABLES `form_counter` WRITE;
/*!40000 ALTER TABLE `form_counter` DISABLE KEYS */;
INSERT INTO `form_counter` VALUES (1,0);
/*!40000 ALTER TABLE `form_counter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_history`
--

DROP TABLE IF EXISTS `form_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_history` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `action` varchar(255) NOT NULL,
  `data` longtext,
  `form_id` bigint(20) NOT NULL,
  `instance_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_history`
--

LOCK TABLES `form_history` WRITE;
/*!40000 ALTER TABLE `form_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_report`
--

DROP TABLE IF EXISTS `form_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_report` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `_private` bit(1) NOT NULL,
  `created_by_id` bigint(20) default NULL,
  `description` varchar(255) default NULL,
  `filters` varchar(255) default NULL,
  `form_id` bigint(20) NOT NULL,
  `key_figures` varchar(255) default NULL,
  `non_key_figures` varchar(255) default NULL,
  `view_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `view_name` (`view_name`),
  KEY `FKEA0D210F35BB3DB` (`form_id`),
  KEY `FKEA0D210FA3FCE767` (`created_by_id`),
  CONSTRAINT `FKEA0D210FA3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKEA0D210F35BB3DB` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_report`
--

LOCK TABLES `form_report` WRITE;
/*!40000 ALTER TABLE `form_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_template`
--

DROP TABLE IF EXISTS `form_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_template` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `category` varchar(255) NOT NULL,
  `created_by_id` bigint(20) default NULL,
  `date_created` datetime NOT NULL,
  `form_id` bigint(20) NOT NULL,
  `global` bit(1) NOT NULL,
  `last_updated` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `updated_by_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK65F80D9535BB3DB` (`form_id`),
  KEY `FK65F80D95A3FCE767` (`created_by_id`),
  KEY `FK65F80D95C4EB5EFA` (`updated_by_id`),
  CONSTRAINT `FK65F80D95C4EB5EFA` FOREIGN KEY (`updated_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK65F80D9535BB3DB` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`),
  CONSTRAINT `FK65F80D95A3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_template`
--

LOCK TABLES `form_template` WRITE;
/*!40000 ALTER TABLE `form_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `free_marker_template`
--

DROP TABLE IF EXISTS `free_marker_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `free_marker_template` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime default NULL,
  `name` varchar(255) NOT NULL,
  `source` varchar(10000) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `free_marker_template`
--

LOCK TABLES `free_marker_template` WRITE;
/*!40000 ALTER TABLE `free_marker_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `free_marker_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_details`
--

DROP TABLE IF EXISTS `group_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_details` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `created_by_id` bigint(20) default NULL,
  `created_on` datetime default NULL,
  `group_description` varchar(108) NOT NULL,
  `group_id` varchar(255) default NULL,
  `group_name` varchar(54) NOT NULL,
  `group_source` varchar(255) default NULL,
  `group_type_id` bigint(20) NOT NULL,
  `image` tinyblob,
  `personal` bit(1) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `url` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK5FE6A082F9416461` (`group_type_id`),
  KEY `FK5FE6A082A3FCE767` (`created_by_id`),
  CONSTRAINT `FK5FE6A082A3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK5FE6A082F9416461` FOREIGN KEY (`group_type_id`) REFERENCES `drop_down` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_details`
--

LOCK TABLES `group_details` WRITE;
/*!40000 ALTER TABLE `group_details` DISABLE KEYS */;
INSERT INTO `group_details` VALUES (1,0,NULL,'2018-06-10 19:26:36','Sales Group',NULL,'Sales ',NULL,10,NULL,'\0',1,NULL),(2,0,NULL,'2018-06-10 19:26:36','Human Resources',NULL,'HR ',NULL,10,NULL,'\0',1,NULL),(3,0,NULL,'2018-06-10 19:26:36','Accounting',NULL,'Accounting ',NULL,10,NULL,'\0',1,NULL);
/*!40000 ALTER TABLE `group_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_details_contact_details`
--

DROP TABLE IF EXISTS `group_details_contact_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_details_contact_details` (
  `group_details_contacts_id` bigint(20) default NULL,
  `contact_details_id` bigint(20) default NULL,
  KEY `FK4D544F66F624F4BD` (`group_details_contacts_id`),
  KEY `FK4D544F6656513969` (`contact_details_id`),
  CONSTRAINT `FK4D544F6656513969` FOREIGN KEY (`contact_details_id`) REFERENCES `contact_details` (`id`),
  CONSTRAINT `FK4D544F66F624F4BD` FOREIGN KEY (`group_details_contacts_id`) REFERENCES `group_details` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_details_contact_details`
--

LOCK TABLES `group_details_contact_details` WRITE;
/*!40000 ALTER TABLE `group_details_contact_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_details_contact_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_details_user`
--

DROP TABLE IF EXISTS `group_details_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_details_user` (
  `group_details_user_id` bigint(20) default NULL,
  `user_id` bigint(20) default NULL,
  `user_idx` int(11) default NULL,
  KEY `FK8A85056826E04F8A` (`user_id`),
  CONSTRAINT `FK8A85056826E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_details_user`
--

LOCK TABLES `group_details_user` WRITE;
/*!40000 ALTER TABLE `group_details_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_details_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `id` bigint(20) NOT NULL auto_increment,
  `description` varchar(255) default NULL,
  `error_level` varchar(255) default NULL,
  `error_type` varchar(255) default NULL,
  `log_time` datetime default NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` VALUES (1,'java.text.ParseException: Unparseable date: \"2018-06-11\"','ERROR','grails.app.controller.com.oneapp.cloud.core.ReportController','2018-06-11 07:31:55',1),(2,'Exception occured in job: GRAILS_JOBS.DailyUpdateJob','ERROR','org.codehaus.groovy.grails.plugins.quartz.listeners.ExceptionPrinterJobListener','2018-06-11 09:00:01',0);
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_failure_log`
--

DROP TABLE IF EXISTS `login_failure_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login_failure_log` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `_date` datetime default NULL,
  `ip` varchar(255) default NULL,
  `msg` varchar(255) default NULL,
  `uri` varchar(255) default NULL,
  `user` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_failure_log`
--

LOCK TABLES `login_failure_log` WRITE;
/*!40000 ALTER TABLE `login_failure_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `login_failure_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mail_chimp_details`
--

DROP TABLE IF EXISTS `mail_chimp_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail_chimp_details` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `apikey` varchar(255) NOT NULL,
  `field_mappings` varchar(2000) NOT NULL,
  `list_id` varchar(255) NOT NULL,
  `optinemail` bit(1) NOT NULL,
  `send_choice` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mail_chimp_details`
--

LOCK TABLES `mail_chimp_details` WRITE;
/*!40000 ALTER TABLE `mail_chimp_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `mail_chimp_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `from_user_id` bigint(20) default NULL,
  `message` varchar(5000) default NULL,
  `message_time` datetime default NULL,
  `recvd` bit(1) NOT NULL,
  `to_user_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK38EB0007AE6359C6` (`to_user_id`),
  KEY `FK38EB00071F0AC3F5` (`from_user_id`),
  CONSTRAINT `FK38EB00071F0AC3F5` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK38EB0007AE6359C6` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mile_stone`
--

DROP TABLE IF EXISTS `mile_stone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mile_stone` (
  `id` bigint(20) NOT NULL,
  `description` varchar(256) default NULL,
  `due_date` datetime default NULL,
  `start_date` datetime default NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mile_stone`
--

LOCK TABLES `mile_stone` WRITE;
/*!40000 ALTER TABLE `mile_stone` DISABLE KEYS */;
/*!40000 ALTER TABLE `mile_stone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mile_stone_task`
--

DROP TABLE IF EXISTS `mile_stone_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mile_stone_task` (
  `mile_stone_task_id` bigint(20) default NULL,
  `task_id` bigint(20) default NULL,
  KEY `FK32F9AF8927CC6CDF` (`mile_stone_task_id`),
  KEY `FK32F9AF8934C966DC` (`task_id`),
  CONSTRAINT `FK32F9AF8934C966DC` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`),
  CONSTRAINT `FK32F9AF8927CC6CDF` FOREIGN KEY (`mile_stone_task_id`) REFERENCES `mile_stone` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mile_stone_task`
--

LOCK TABLES `mile_stone_task` WRITE;
/*!40000 ALTER TABLE `mile_stone_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `mile_stone_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `money`
--

DROP TABLE IF EXISTS `money`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `money` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `amount` decimal(19,2) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `money`
--

LOCK TABLES `money` WRITE;
/*!40000 ALTER TABLE `money` DISABLE KEYS */;
/*!40000 ALTER TABLE `money` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notify_condition`
--

DROP TABLE IF EXISTS `notify_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notify_condition` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `cond_op` varchar(255) default NULL,
  `field_id` bigint(20) default NULL,
  `form_admin_id` bigint(20) NOT NULL,
  `op` varchar(255) default NULL,
  `sequence` int(11) default NULL,
  `val` varchar(255) default NULL,
  `conditions_idx` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKF3D837A5D202AE10` (`form_admin_id`),
  CONSTRAINT `FKF3D837A5D202AE10` FOREIGN KEY (`form_admin_id`) REFERENCES `form_admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notify_condition`
--

LOCK TABLES `notify_condition` WRITE;
/*!40000 ALTER TABLE `notify_condition` DISABLE KEYS */;
/*!40000 ALTER TABLE `notify_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth_details`
--

DROP TABLE IF EXISTS `oauth_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_details` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `access_token` varchar(255) NOT NULL,
  `oauth_identifier` varchar(255) NOT NULL,
  `source` varchar(255) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `token_secret` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_details`
--

LOCK TABLES `oauth_details` WRITE;
/*!40000 ALTER TABLE `oauth_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauth_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `buyer_id` bigint(20) NOT NULL,
  `buyer_information_id` bigint(20) default NULL,
  `currency` varchar(255) NOT NULL,
  `discount_cart_amount` decimal(19,2) NOT NULL,
  `form_id` varchar(255) default NULL,
  `instance_id` varchar(255) default NULL,
  `paypal_transaction_id` varchar(255) default NULL,
  `status` varchar(9) NOT NULL,
  `subscr_id` varchar(255) default NULL,
  `tax` double NOT NULL,
  `transaction_id` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKD11C32064DF7D5C4` (`buyer_information_id`),
  CONSTRAINT `FKD11C32064DF7D5C4` FOREIGN KEY (`buyer_information_id`) REFERENCES `buyer_information` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_item`
--

DROP TABLE IF EXISTS `payment_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_item` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `amount` decimal(19,2) NOT NULL,
  `discount_amount` decimal(19,2) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `item_number` varchar(255) NOT NULL,
  `payment_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  `payment_items_idx` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKE25C55AC2990F5A9` (`payment_id`),
  CONSTRAINT `FKE25C55AC2990F5A9` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_item`
--

LOCK TABLES `payment_item` WRITE;
/*!40000 ALTER TABLE `payment_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persistent_logins`
--

DROP TABLE IF EXISTS `persistent_logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `persistent_logins` (
  `series` varchar(64) NOT NULL,
  `last_used` datetime NOT NULL,
  `token` varchar(64) NOT NULL,
  `username` varchar(64) NOT NULL,
  PRIMARY KEY  (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persistent_logins`
--

LOCK TABLES `persistent_logins` WRITE;
/*!40000 ALTER TABLE `persistent_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `persistent_logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `dob` datetime default NULL,
  `email` varchar(255) default NULL,
  `facebook_profile` varchar(255) default NULL,
  `fax_number` varchar(20) default NULL,
  `first_name` varchar(30) NOT NULL,
  `home_address_address1` varchar(125) default NULL,
  `home_address_address2` varchar(125) default NULL,
  `home_address_city` varchar(150) default NULL,
  `home_address_country` varchar(50) default NULL,
  `home_address_state` varchar(50) default NULL,
  `home_address_zip_code` varchar(150) default NULL,
  `last_name` varchar(30) default NULL,
  `linked_in_profile` varchar(255) default NULL,
  `mobile_phone` varchar(20) default NULL,
  `office_address_address1` varchar(125) default NULL,
  `office_address_address2` varchar(125) default NULL,
  `office_address_city` varchar(150) default NULL,
  `office_address_country` varchar(50) default NULL,
  `office_address_state` varchar(50) default NULL,
  `office_address_zip_code` varchar(150) default NULL,
  `office_phone` varchar(20) default NULL,
  `social_nw_name` varchar(255) default NULL,
  `twitter_profile` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,9,'2018-06-10 19:26:36',NULL,NULL,NULL,'Jack',NULL,NULL,NULL,NULL,NULL,NULL,'Smith',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,1,'2018-06-25 19:26:36',NULL,NULL,NULL,'John',NULL,NULL,NULL,NULL,NULL,NULL,'Kahn',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,0,'2018-06-03 19:26:36',NULL,NULL,NULL,'Vladimir',NULL,NULL,NULL,NULL,NULL,NULL,'Phelaagine',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,0,NULL,NULL,NULL,NULL,'Public',NULL,NULL,NULL,NULL,NULL,NULL,'User',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plan`
--

DROP TABLE IF EXISTS `plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plan` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(255) default NULL,
  `form` bigint(20) NOT NULL,
  `max_email_account` bigint(20) NOT NULL,
  `max_form_entries` bigint(20) NOT NULL,
  `max_storage` bigint(20) NOT NULL,
  `max_users` bigint(20) NOT NULL,
  `plan_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan`
--

LOCK TABLES `plan` WRITE;
/*!40000 ALTER TABLE `plan` DISABLE KEYS */;
INSERT INTO `plan` VALUES (1,0,9.95,'Basic - $9.95/month',2,2,100,2048,5,'Basic'),(2,0,19.95,'Professional - $19.95/month',5,5,100,5120,10,'Basic'),(3,0,29.95,'Enterprise - $29.95/month',10,15,100,10240,15,'Basic');
/*!40000 ALTER TABLE `plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferences`
--

DROP TABLE IF EXISTS `preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preferences` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `days_to_retrieve_posts` int(11) NOT NULL,
  `feed_update_frequency` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferences`
--

LOCK TABLES `preferences` WRITE;
/*!40000 ALTER TABLE `preferences` DISABLE KEYS */;
/*!40000 ALTER TABLE `preferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `rater_class` varchar(255) NOT NULL,
  `rater_id` bigint(20) NOT NULL,
  `stars` double NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
INSERT INTO `rating` VALUES (1,0,'2018-06-11 07:34:33','2018-06-11 07:34:33','com.oneapp.cloud.core.User',1,2);
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating_link`
--

DROP TABLE IF EXISTS `rating_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating_link` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `rating_id` bigint(20) NOT NULL,
  `rating_ref` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK1827315C45884E64` (`rating_id`),
  CONSTRAINT `FK1827315C45884E64` FOREIGN KEY (`rating_id`) REFERENCES `rating` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating_link`
--

LOCK TABLES `rating_link` WRITE;
/*!40000 ALTER TABLE `rating_link` DISABLE KEYS */;
INSERT INTO `rating_link` VALUES (1,0,1,1,'activityFeed');
/*!40000 ALTER TABLE `rating_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registration_code`
--

DROP TABLE IF EXISTS `registration_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registration_code` (
  `id` bigint(20) NOT NULL auto_increment,
  `date_created` datetime NOT NULL,
  `token` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registration_code`
--

LOCK TABLES `registration_code` WRITE;
/*!40000 ALTER TABLE `registration_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `registration_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request_token_oauth_details`
--

DROP TABLE IF EXISTS `request_token_oauth_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request_token_oauth_details` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `oauth_secret` varchar(255) NOT NULL,
  `oauth_token` varchar(255) NOT NULL,
  `request_token` varchar(255) NOT NULL,
  `source` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `wallpost` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request_token_oauth_details`
--

LOCK TABLES `request_token_oauth_details` WRITE;
/*!40000 ALTER TABLE `request_token_oauth_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `request_token_oauth_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `authority` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,0,'ROLE_USER','User'),(2,0,'ROLE_SUPER_ADMIN','Super User'),(3,0,'ROLE_ADMIN','Client Administrator'),(4,0,'ROLE_HR_MANAGER','HR Manager'),(5,0,'ROLE_TRIAL_USER','Trail User');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule`
--

DROP TABLE IF EXISTS `rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `_condition` varchar(3) default NULL,
  `_order` int(11) NOT NULL,
  `attribute_name` varchar(255) NOT NULL,
  `class_name` varchar(255) NOT NULL,
  `operator` varchar(7) NOT NULL,
  `rule_set_id` bigint(20) NOT NULL,
  `status` varchar(8) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `rule_set_id` (`rule_set_id`,`_order`),
  KEY `FK3596FCEB307CD1` (`rule_set_id`),
  CONSTRAINT `FK3596FCEB307CD1` FOREIGN KEY (`rule_set_id`) REFERENCES `rule_set` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule`
--

LOCK TABLES `rule` WRITE;
/*!40000 ALTER TABLE `rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_set`
--

DROP TABLE IF EXISTS `rule_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule_set` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `_action` varchar(11) NOT NULL,
  `_order` int(11) NOT NULL,
  `created_by_id` bigint(20) default NULL,
  `date_created` datetime default NULL,
  `last_updated` datetime default NULL,
  `name` varchar(255) NOT NULL,
  `result_class` varchar(17) NOT NULL,
  `result_instance` varchar(255) NOT NULL,
  `status` varchar(8) NOT NULL,
  `subscription_id` bigint(20) default NULL,
  `tenant_id` int(11) NOT NULL,
  `updated_by_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `subscription_id` (`subscription_id`,`_order`),
  KEY `FK2D86A49FA3FCE767` (`created_by_id`),
  KEY `FK2D86A49FC4EB5EFA` (`updated_by_id`),
  KEY `FK2D86A49FD7F9C64A` (`subscription_id`),
  CONSTRAINT `FK2D86A49FD7F9C64A` FOREIGN KEY (`subscription_id`) REFERENCES `subscription` (`id`),
  CONSTRAINT `FK2D86A49FA3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK2D86A49FC4EB5EFA` FOREIGN KEY (`updated_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_set`
--

LOCK TABLES `rule_set` WRITE;
/*!40000 ALTER TABLE `rule_set` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sender`
--

DROP TABLE IF EXISTS `sender`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sender` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `attachment_count` bigint(20) default NULL,
  `email` varchar(255) default NULL,
  `email_count` bigint(20) default NULL,
  `largest_size` double default NULL,
  `least_recent` datetime default NULL,
  `most_recent` datetime default NULL,
  `name` varchar(255) default NULL,
  `total_size` double default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sender`
--

LOCK TABLES `sender` WRITE;
/*!40000 ALTER TABLE `sender` DISABLE KEYS */;
/*!40000 ALTER TABLE `sender` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setting` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `code` varchar(100) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `type` varchar(7) NOT NULL,
  `value` varchar(100) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setting`
--

LOCK TABLES `setting` WRITE;
/*!40000 ALTER TABLE `setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `settings` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `date_format` varchar(255) NOT NULL,
  `number_format` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `created_by_id` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `follow_category` varchar(255) default NULL,
  `follow_class` varchar(255) default NULL,
  `follow_id` bigint(20) default NULL,
  `follow_user_id` bigint(20) default NULL,
  `follower_id` bigint(20) NOT NULL,
  `last_updated` datetime NOT NULL,
  `state` varchar(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK1456591D8F235657` (`follower_id`),
  KEY `FK1456591DA3FCE767` (`created_by_id`),
  KEY `FK1456591D3B2F0FC` (`follow_user_id`),
  CONSTRAINT `FK1456591D3B2F0FC` FOREIGN KEY (`follow_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK1456591D8F235657` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK1456591DA3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_links`
--

DROP TABLE IF EXISTS `tag_links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_links` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  `tag_ref` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK7C35D6D45A3B441D` (`tag_id`),
  CONSTRAINT `FK7C35D6D45A3B441D` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_links`
--

LOCK TABLES `tag_links` WRITE;
/*!40000 ALTER TABLE `tag_links` DISABLE KEYS */;
INSERT INTO `tag_links` VALUES (1,0,1,1,'activityFeed');
/*!40000 ALTER TABLE `tag_links` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (1,0,'task');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` bigint(20) NOT NULL,
  `assigned_to_id` bigint(20) default NULL,
  `description` varchar(2048) default NULL,
  `due_date` datetime default NULL,
  `name` varchar(54) NOT NULL,
  `start_date` datetime default NULL,
  `task_type_id` bigint(20) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK3635851F823D67` (`task_type_id`),
  KEY `FK363585BD924D29` (`assigned_to_id`),
  CONSTRAINT `FK363585BD924D29` FOREIGN KEY (`assigned_to_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK3635851F823D67` FOREIGN KEY (`task_type_id`) REFERENCES `drop_down` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_task_updates`
--

DROP TABLE IF EXISTS `task_task_updates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_task_updates` (
  `task_task_update_id` bigint(20) default NULL,
  `task_update_id` bigint(20) default NULL,
  KEY `FK68767C8AB53318D8` (`task_task_update_id`),
  KEY `FK68767C8AD8B36E87` (`task_update_id`),
  CONSTRAINT `FK68767C8AD8B36E87` FOREIGN KEY (`task_update_id`) REFERENCES `task_updates` (`id`),
  CONSTRAINT `FK68767C8AB53318D8` FOREIGN KEY (`task_task_update_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_task_updates`
--

LOCK TABLES `task_task_updates` WRITE;
/*!40000 ALTER TABLE `task_task_updates` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_task_updates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_updates`
--

DROP TABLE IF EXISTS `task_updates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_updates` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `content` longtext NOT NULL,
  `date` datetime default NULL,
  `from_` varchar(255) default NULL,
  `subject` varchar(255) default NULL,
  `task_id` int(11) NOT NULL,
  `task_name` varchar(255) default NULL,
  `tenant_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_updates`
--

LOCK TABLES `task_updates` WRITE;
/*!40000 ALTER TABLE `task_updates` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_updates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_domain`
--

DROP TABLE IF EXISTS `test_domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_domain` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_domain`
--

LOCK TABLES `test_domain` WRITE;
/*!40000 ALTER TABLE `test_domain` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_domain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_rater`
--

DROP TABLE IF EXISTS `test_rater`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_rater` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_rater`
--

LOCK TABLES `test_rater` WRITE;
/*!40000 ALTER TABLE `test_rater` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_rater` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tracker`
--

DROP TABLE IF EXISTS `tracker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tracker` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `action` varchar(255) default NULL,
  `clientid` varchar(255) default NULL,
  `client_name` varchar(255) default NULL,
  `controller` varchar(255) default NULL,
  `date_created` datetime default NULL,
  `formid` varchar(255) default NULL,
  `ip` varchar(255) default NULL,
  `load_time` varchar(255) default NULL,
  `params` longtext,
  `recordid` varchar(255) default NULL,
  `user_id` bigint(20) default NULL,
  `user_agent` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKC060D73826E04F8A` (`user_id`),
  CONSTRAINT `FKC060D73826E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tracker`
--

LOCK TABLES `tracker` WRITE;
/*!40000 ALTER TABLE `tracker` DISABLE KEYS */;
/*!40000 ALTER TABLE `tracker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unique_form_entry`
--

DROP TABLE IF EXISTS `unique_form_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unique_form_entry` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `form_id` bigint(20) NOT NULL,
  `instance_id` bigint(20) NOT NULL,
  `unique_id` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unique_form_entry`
--

LOCK TABLES `unique_form_entry` WRITE;
/*!40000 ALTER TABLE `unique_form_entry` DISABLE KEYS */;
INSERT INTO `unique_form_entry` VALUES (1,0,1,1,'dac76930'),(2,0,1,2,'c0aaaf7c');
/*!40000 ALTER TABLE `unique_form_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `api_key` varchar(255) default NULL,
  `claimed_id` varchar(255) default NULL,
  `created_by_id` bigint(20) default NULL,
  `date_created` datetime default NULL,
  `department_id` bigint(20) default NULL,
  `enabled` bit(1) NOT NULL,
  `hourly_rate` decimal(19,2) default NULL,
  `hourly_rate_curr` varchar(255) default NULL,
  `last_activity` datetime default NULL,
  `last_log_in` datetime default NULL,
  `last_updated` datetime default NULL,
  `last_viewed` datetime default NULL,
  `password` varchar(255) default NULL,
  `password_expired` bit(1) NOT NULL,
  `picture` mediumblob,
  `pictureurl` varchar(255) default NULL,
  `reports_to_id` bigint(20) default NULL,
  `short_name` varchar(255) default NULL,
  `sub_org_id` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `type_id` bigint(20) default NULL,
  `user_tenant_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `FK36EBCB625A6B09` (`department_id`),
  KEY `FK36EBCB39C185A` (`reports_to_id`),
  KEY `FK36EBCBD2D53516` (`sub_org_id`),
  KEY `FK36EBCBA3FCE767` (`created_by_id`),
  KEY `FK36EBCBF633B621` (`type_id`),
  CONSTRAINT `FK36EBCBF633B621` FOREIGN KEY (`type_id`) REFERENCES `drop_down` (`id`),
  CONSTRAINT `FK36EBCB39C185A` FOREIGN KEY (`reports_to_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK36EBCB625A6B09` FOREIGN KEY (`department_id`) REFERENCES `drop_down` (`id`),
  CONSTRAINT `FK36EBCBA3FCE767` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK36EBCBD2D53516` FOREIGN KEY (`sub_org_id`) REFERENCES `drop_down` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'\0','\0',NULL,NULL,2,'2018-06-10 19:26:36',NULL,'','100.00','USD','2018-06-11 07:33:01','2018-06-11 08:56:29','2018-06-11 08:56:29','2018-06-11 07:33:01','c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec','\0',NULL,NULL,NULL,NULL,NULL,'CTO',NULL,1,'admin@oneappcloud.com'),(2,'\0','\0',NULL,NULL,3,'2018-06-10 19:26:36',NULL,'','100.00','USD',NULL,NULL,'2018-06-10 19:26:36',NULL,'5fc2ca6f085919f2f77626f1e280fab9cc92b4edc9edc53ac6eee3f72c5c508e869ee9d67a96d63986d14c1c2b82c35ff5f31494bea831015424f59c96fff664','\0',NULL,NULL,1,NULL,NULL,'EVP',NULL,1,'manager@oneappcloud.com'),(3,'\0','\0',NULL,NULL,NULL,'2018-06-10 19:26:36',NULL,'','100.00','USD',NULL,NULL,'2018-06-10 19:26:36',NULL,'b14361404c078ffd549c03db443c3fede2f3e534d73f78f77301ed97d4a436a9fd9db05ee8b325c0ad36438b43fec8510c204fc1c1edb21d0941c00e9e2c1ce2','\0',NULL,NULL,2,NULL,NULL,'Engineer',NULL,1,'user@oneappcloud.com'),(4,'\0','\0',NULL,NULL,NULL,'2018-06-10 19:26:36',NULL,'','100.00',NULL,NULL,NULL,'2018-06-10 19:26:36',NULL,'cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e','\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'publicuser@oneappcloud.com');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_app_access_detail`
--

DROP TABLE IF EXISTS `user_app_access_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_app_access_detail` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `access_mode` varchar(10) default NULL,
  `access_time` datetime default NULL,
  `access_type` varchar(7) default NULL,
  `accessed_class` varchar(255) default NULL,
  `action` varchar(6) default NULL,
  `ip_address` varchar(255) default NULL,
  `location` varchar(255) default NULL,
  `tenant_id` int(11) NOT NULL,
  `user_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK55AD5E7A26E04F8A` (`user_id`),
  CONSTRAINT `FK55AD5E7A26E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_app_access_detail`
--

LOCK TABLES `user_app_access_detail` WRITE;
/*!40000 ALTER TABLE `user_app_access_detail` DISABLE KEYS */;
INSERT INTO `user_app_access_detail` VALUES (1,0,'Chrome','2018-06-11 07:22:38','Browser','Form1.Form1','View','0:0:0:0:0:0:0:1','',1,1),(2,0,'Chrome','2018-06-11 07:29:41','Browser','Form1.Form1','View','0:0:0:0:0:0:0:1','',1,NULL),(3,0,'Chrome','2018-06-11 07:30:11','Browser','Form1.Form1','View','0:0:0:0:0:0:0:1','',1,1),(4,0,'Chrome','2018-06-11 07:30:15','Browser','Form1.Form1','Create','0:0:0:0:0:0:0:1','',1,1),(5,0,'Chrome','2018-06-11 07:30:25','Browser','Form1.Form1','View','0:0:0:0:0:0:0:1','',1,1),(6,0,'Chrome','2018-06-11 07:30:50','Browser','Form1.Form1','View','0:0:0:0:0:0:0:1','',1,1),(7,0,'Chrome','2018-06-11 07:51:26','Browser','Form1.Form1','View','0:0:0:0:0:0:0:1','',1,1),(8,0,'Chrome','2018-06-11 08:23:10','Browser','Form1.Form1','View','0:0:0:0:0:0:0:1','',1,1),(9,0,'Chrome','2018-06-11 08:33:30','Browser','Form1.Form1','View','0:0:0:0:0:0:0:1','',1,1),(10,0,'Chrome','2018-06-11 08:33:38','Browser','Form1.Form1','Create','0:0:0:0:0:0:0:1','',1,1);
/*!40000 ALTER TABLE `user_app_access_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_log`
--

DROP TABLE IF EXISTS `user_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_log` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `last_login` datetime default NULL,
  `lasturi` varchar(256) default NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKF022E0D026E04F8A` (`user_id`),
  CONSTRAINT `FKF022E0D026E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_log`
--

LOCK TABLES `user_log` WRITE;
/*!40000 ALTER TABLE `user_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_profile`
--

DROP TABLE IF EXISTS `user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_profile` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `background` varchar(255) default NULL,
  `currency` varchar(255) default NULL,
  `current_status` varchar(255) default NULL,
  `date_created` datetime NOT NULL,
  `default_hours` decimal(4,2) default NULL,
  `default_language` varchar(255) default NULL,
  `default_timezone` varchar(255) default NULL,
  `email_expense_subscribed` bit(1) NOT NULL,
  `email_project_subscribed` bit(1) NOT NULL,
  `email_subscribed` bit(1) NOT NULL,
  `email_time_subscribed` bit(1) NOT NULL,
  `expense_entry_alert` decimal(19,2) default NULL,
  `header` varchar(255) default NULL,
  `invoice_alert` decimal(19,2) default NULL,
  `is_online` bit(1) NOT NULL,
  `last_updated` datetime NOT NULL,
  `num_of_rows` int(11) default NULL,
  `project_budget_alert` decimal(19,2) default NULL,
  `reports_subscribed` bit(1) NOT NULL,
  `sync_facebook_contacts` bit(1) NOT NULL,
  `sync_linkedin_contacts` bit(1) NOT NULL,
  `sync_twitter_contacts` bit(1) NOT NULL,
  `task_id` bigint(20) default NULL,
  `time_entry_alert` decimal(19,2) default NULL,
  `user_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  KEY `FK487E213526E04F8A` (`user_id`),
  KEY `FK487E213534C966DC` (`task_id`),
  CONSTRAINT `FK487E213534C966DC` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`),
  CONSTRAINT `FK487E213526E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_profile`
--

LOCK TABLES `user_profile` WRITE;
/*!40000 ALTER TABLE `user_profile` DISABLE KEYS */;
INSERT INTO `user_profile` VALUES (1,1,NULL,NULL,NULL,'2018-06-11 07:30:55',NULL,NULL,NULL,'\0','\0','','\0',NULL,NULL,NULL,'\0','2018-06-11 07:31:02',10,NULL,'\0','\0','\0','\0',NULL,NULL,1);
/*!40000 ALTER TABLE `user_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`role_id`,`user_id`),
  KEY `FK143BF46A81B58BAA` (`role_id`),
  KEY `FK143BF46A26E04F8A` (`user_id`),
  CONSTRAINT `FK143BF46A26E04F8A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK143BF46A81B58BAA` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,3),(2,1),(4,2);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_verification`
--

DROP TABLE IF EXISTS `user_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_verification` (
  `id` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL,
  `api_key` varchar(255) NOT NULL,
  `date_created` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_verification`
--

LOCK TABLES `user_verification` WRITE;
/*!40000 ALTER TABLE `user_verification` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `web_hook_details`
--

DROP TABLE IF EXISTS `web_hook_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `web_hook_details` (
  `id` bigint(20) NOT NULL auto_increment,
  `version` bigint(20) NOT NULL,
  `hand_shake_key` varchar(255) default NULL,
  `include_field_and_form` bit(1) default NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `web_hook_details`
--

LOCK TABLES `web_hook_details` WRITE;
/*!40000 ALTER TABLE `web_hook_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `web_hook_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-11 13:18:35
