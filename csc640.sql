-- phpMyAdmin SQL Dump
-- version 4.0.10.12
-- http://www.phpmyadmin.net
--
-- Host: 127.10.159.130:3306
-- Generation Time: Feb 06, 2016 at 06:09 PM
-- Server version: 5.5.45
-- PHP Version: 5.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `csc640`
--

-- --------------------------------------------------------

--
-- Table structure for table `CAREGIVER_ELDERLY`
--

CREATE TABLE IF NOT EXISTS `CAREGIVER_ELDERLY` (
  `caregiver_username` varchar(255) NOT NULL,
  `elderly_username` varchar(255) NOT NULL,
  PRIMARY KEY (`caregiver_username`,`elderly_username`),
  KEY `FK_tbje485nnako9w1045ftq7v9a` (`elderly_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ELDERLY_FAMILY`
--

CREATE TABLE IF NOT EXISTS `ELDERLY_FAMILY` (
  `elderly_username` varchar(255) NOT NULL,
  `family_username` varchar(255) NOT NULL,
  PRIMARY KEY (`elderly_username`,`family_username`),
  KEY `FK_ebjg6jbmpcedqmny6pgi2qwxn` (`family_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `MONITORS`
--

CREATE TABLE IF NOT EXISTS `MONITORS` (
  `type` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `note` varchar(255) DEFAULT NULL,
  `schedule` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `doctorName` varchar(255) DEFAULT NULL,
  `bloodPressure` bit(1) DEFAULT NULL,
  `bloodSugar` bit(1) DEFAULT NULL,
  `heartBeat` bit(1) DEFAULT NULL,
  `medicationName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_24f0ujsrph9vl33fr1k6dfdn0` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

-- --------------------------------------------------------

--
-- Table structure for table `MONITORS_RESPONSE`
--

CREATE TABLE IF NOT EXISTS `MONITORS_RESPONSE` (
  `reponse_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bloodPressure` varchar(255) DEFAULT NULL,
  `bloodSugar` varchar(255) DEFAULT NULL,
  `done` bit(1) DEFAULT NULL,
  `heartBeat` varchar(255) DEFAULT NULL,
  `id` bigint(20) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`reponse_id`),
  KEY `FK_3k4soe8l1oxx9dn6t92h1icbo` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table `USERS`
--

CREATE TABLE IF NOT EXISTS `USERS` (
  `type` varchar(31) NOT NULL,
  `username` varchar(255) NOT NULL,
  `email_address` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `CAREGIVER_ELDERLY`
--
ALTER TABLE `CAREGIVER_ELDERLY`
  ADD CONSTRAINT `FK_ox4ne84rreuhdb1hrm0wjhlr7` FOREIGN KEY (`caregiver_username`) REFERENCES `USERS` (`username`),
  ADD CONSTRAINT `FK_tbje485nnako9w1045ftq7v9a` FOREIGN KEY (`elderly_username`) REFERENCES `USERS` (`username`);

--
-- Constraints for table `ELDERLY_FAMILY`
--
ALTER TABLE `ELDERLY_FAMILY`
  ADD CONSTRAINT `FK_ebjg6jbmpcedqmny6pgi2qwxn` FOREIGN KEY (`family_username`) REFERENCES `USERS` (`username`),
  ADD CONSTRAINT `FK_saatomnu8a8oag1owmb6g1n8g` FOREIGN KEY (`elderly_username`) REFERENCES `USERS` (`username`);

--
-- Constraints for table `MONITORS_RESPONSE`
--
ALTER TABLE `MONITORS_RESPONSE`
  ADD CONSTRAINT `FK_3k4soe8l1oxx9dn6t92h1icbo` FOREIGN KEY (`id`) REFERENCES `MONITORS` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
