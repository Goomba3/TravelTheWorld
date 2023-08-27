-- MariaDB dump 10.19  Distrib 10.5.19-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: TravelTheWorld
-- ------------------------------------------------------
-- Server version	10.5.19-MariaDB-0+deb11u2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Aeroport`
--

DROP TABLE IF EXISTS `Aeroport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Aeroport` (
  `id` int(11) NOT NULL,
  `max_avions` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `Aeroport_ibfk_1` FOREIGN KEY (`id`) REFERENCES `Station` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Aeroport`
--

LOCK TABLES `Aeroport` WRITE;
/*!40000 ALTER TABLE `Aeroport` DISABLE KEYS */;
INSERT INTO `Aeroport` VALUES (1,7),(3,15);
/*!40000 ALTER TABLE `Aeroport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Avion`
--

DROP TABLE IF EXISTS `Avion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Avion` (
  `id` int(11) NOT NULL,
  `nb_places` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `Avion_ibfk_1` FOREIGN KEY (`id`) REFERENCES `MoyenTransport` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Avion`
--

LOCK TABLES `Avion` WRITE;
/*!40000 ALTER TABLE `Avion` DISABLE KEYS */;
INSERT INTO `Avion` VALUES (1,60),(4,84),(5,0),(6,0);
/*!40000 ALTER TABLE `Avion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Gare`
--

DROP TABLE IF EXISTS `Gare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Gare` (
  `id` int(11) NOT NULL,
  `max_trains` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `Gare_ibfk_1` FOREIGN KEY (`id`) REFERENCES `Station` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Gare`
--

LOCK TABLES `Gare` WRITE;
/*!40000 ALTER TABLE `Gare` DISABLE KEYS */;
INSERT INTO `Gare` VALUES (2,25),(4,2),(5,15);
/*!40000 ALTER TABLE `Gare` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MoyenTransport`
--

DROP TABLE IF EXISTS `MoyenTransport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MoyenTransport` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MoyenTransport`
--

LOCK TABLES `MoyenTransport` WRITE;
/*!40000 ALTER TABLE `MoyenTransport` DISABLE KEYS */;
INSERT INTO `MoyenTransport` VALUES (1,'A302'),(2,'T204'),(3,'T808'),(4,'A201'),(5,NULL),(6,NULL);
/*!40000 ALTER TABLE `MoyenTransport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ParcDisney`
--

DROP TABLE IF EXISTS `ParcDisney`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ParcDisney` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `id_ville` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_ville` (`id_ville`),
  CONSTRAINT `ParcDisney_ibfk_1` FOREIGN KEY (`id_ville`) REFERENCES `Ville` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ParcDisney`
--

LOCK TABLES `ParcDisney` WRITE;
/*!40000 ALTER TABLE `ParcDisney` DISABLE KEYS */;
INSERT INTO `ParcDisney` VALUES (1,'DisneyLand Paris',1),(2,'Disney Pékin',2),(3,'DisneyLand Tokyo',3);
/*!40000 ALTER TABLE `ParcDisney` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ParcVisite`
--

DROP TABLE IF EXISTS `ParcVisite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ParcVisite` (
  `id_user` int(11) NOT NULL,
  `id_parc` int(11) NOT NULL,
  `date_visite` date DEFAULT NULL,
  PRIMARY KEY (`id_user`,`id_parc`),
  KEY `id_parc` (`id_parc`),
  CONSTRAINT `ParcVisite_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `User` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ParcVisite_ibfk_2` FOREIGN KEY (`id_parc`) REFERENCES `ParcDisney` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ParcVisite`
--

LOCK TABLES `ParcVisite` WRITE;
/*!40000 ALTER TABLE `ParcVisite` DISABLE KEYS */;
INSERT INTO `ParcVisite` VALUES (0,1,'2012-12-12');
/*!40000 ALTER TABLE `ParcVisite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Station`
--

DROP TABLE IF EXISTS `Station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Station` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `id_ville` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_ville` (`id_ville`),
  CONSTRAINT `Station_ibfk_1` FOREIGN KEY (`id_ville`) REFERENCES `Ville` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Station`
--

LOCK TABLES `Station` WRITE;
/*!40000 ALTER TABLE `Station` DISABLE KEYS */;
INSERT INTO `Station` VALUES (1,'Charles',1),(2,'Nord',1),(3,'Francis',2),(4,'Sud',1),(5,'Tokyo SNCB',3);
/*!40000 ALTER TABLE `Station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Train`
--

DROP TABLE IF EXISTS `Train`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Train` (
  `id` int(11) NOT NULL,
  `nb_wagons` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `Train_ibfk_1` FOREIGN KEY (`id`) REFERENCES `MoyenTransport` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Train`
--

LOCK TABLES `Train` WRITE;
/*!40000 ALTER TABLE `Train` DISABLE KEYS */;
INSERT INTO `Train` VALUES (2,0),(3,0);
/*!40000 ALTER TABLE `Train` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Trajet`
--

DROP TABLE IF EXISTS `Trajet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Trajet` (
  `id` int(11) NOT NULL,
  `id_station_depart` int(11) DEFAULT NULL,
  `id_station_arrivee` int(11) DEFAULT NULL,
  `id_moyen_transport` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_station_depart` (`id_station_depart`),
  KEY `id_station_arrivee` (`id_station_arrivee`),
  KEY `id_moyen_transport` (`id_moyen_transport`),
  CONSTRAINT `Trajet_ibfk_1` FOREIGN KEY (`id_station_depart`) REFERENCES `Station` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Trajet_ibfk_2` FOREIGN KEY (`id_station_arrivee`) REFERENCES `Station` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Trajet_ibfk_3` FOREIGN KEY (`id_moyen_transport`) REFERENCES `MoyenTransport` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Trajet`
--

LOCK TABLES `Trajet` WRITE;
/*!40000 ALTER TABLE `Trajet` DISABLE KEYS */;
/*!40000 ALTER TABLE `Trajet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (0,'Admin');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Ville`
--

DROP TABLE IF EXISTS `Ville`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Ville` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Ville`
--

LOCK TABLES `Ville` WRITE;
/*!40000 ALTER TABLE `Ville` DISABLE KEYS */;
INSERT INTO `Ville` VALUES (1,'Paris'),(2,'Pékin'),(3,'Tokyo');
/*!40000 ALTER TABLE `Ville` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Voyage`
--

DROP TABLE IF EXISTS `Voyage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Voyage` (
  `id_user` int(11) NOT NULL,
  `id_trajet` int(11) NOT NULL,
  `date_voyage` date DEFAULT NULL,
  PRIMARY KEY (`id_user`,`id_trajet`),
  KEY `id_trajet` (`id_trajet`),
  CONSTRAINT `Voyage_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `User` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Voyage_ibfk_2` FOREIGN KEY (`id_trajet`) REFERENCES `Trajet` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Voyage`
--

LOCK TABLES `Voyage` WRITE;
/*!40000 ALTER TABLE `Voyage` DISABLE KEYS */;
/*!40000 ALTER TABLE `Voyage` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-27 12:08:43
