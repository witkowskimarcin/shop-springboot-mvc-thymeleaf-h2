package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}

// BAZA
//CREATE DATABASE `baza` /*!40100 DEFAULT CHARACTER SET utf8 */;
//
//DROP TABLE IF EXISTS `user_role`;
//DROP TABLE IF EXISTS `persistent_logins`;
//DROP TABLE IF EXISTS `user`;
//DROP TABLE IF EXISTS `role`;
//
//CREATE TABLE  `role` (
//  `role_id` int(11) NOT NULL auto_increment,
//  `role` varchar(255) default NULL,
//  PRIMARY KEY  (`role_id`)
//) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
//
//CREATE TABLE  `user` (
//  `id` int(11) NOT NULL auto_increment,
//  `firstname` varchar(255) NOT NULL,
//  `lastname` varchar(255) NOT NULL,
//  `email` varchar(255) NOT NULL,
//  `password` varchar(255) NOT NULL,
//  `active` int(11) default NULL,
//  PRIMARY KEY  (`id`)
//) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
//
//CREATE TABLE  `user_role` (
//  `user_id` int(11) NOT NULL,
//  `role_id` int(11) NOT NULL,
//  PRIMARY KEY  (`user_id`,`role_id`),
//  KEY `user_role_key` (`role_id`),
//  CONSTRAINT `user_userrole` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
//  CONSTRAINT `role_userrole` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
//) ENGINE=InnoDB DEFAULT CHARSET=utf8;
//
//CREATE TABLE  `persistent_logins` (
//  `username` varchar(64) NOT NULL,
//  `series` varchar(64) NOT NULL,
//  `token` varchar(64) NOT NULL,
//  `last_used` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
//  PRIMARY KEY  (`series`)
//) ENGINE=InnoDB DEFAULT CHARSET=utf8;
//
//INSERT INTO `role` VALUES (1,'ADMIN');
//INSERT INTO `role` VALUES (2,'USER');