-------------------------------------------------数据库结构----------------------------------------------------------
CREATE DATABASE `class_design` CHARACTER SET 'utf8mb4';

CREATE TABLE `class_design`.`position`  (
  `id` int NOT NULL,
  `province` varchar(45) NULL,
  `city` varchar(45) NULL,
  `county` varchar(45) NULL,
  PRIMARY KEY (`id`)
);


-- 站点
CREATE TABLE `class_design`.`design_station`  (
  `id` int NOT NULL,
  `name` varchar(45) NULL,
  `address` varchar(255) NULL,
  `longitude` double(10, 5) NULL,
  `latitude` double(10, 5) NULL,
  `province` varchar(45) NULL,
  `city` varchar(45) NULL,
  `county` varchar(45) NULL,
  `image` varchar(255) NULL,
  `remark` varchar(255) NULL,
  `manager_id` int NULL,
  `manager_name` varchar(45) NULL,
  `manager_phone` varchar(45) NULL,
  `status` tinyint NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `class_design`.`receive_station`  (
  `id` int NOT NULL,
  `name` varchar(45) NULL,
  `address` varchar(255) NULL,
  `longitude` double(10, 5) NULL,
  `latitude` double(10, 5) NULL,
  `province` varchar(45) NULL,
  `city` varchar(45) NULL,
  `image` varchar(255) NULL,
  `remark` varchar(255) NULL,
  `manager_id` int NULL,
  `manager_name` varchar(45) NULL,
  `manager_phone` varchar(45) NULL,
  `status` tinyint NULL,
  PRIMARY KEY (`id`)
);

-- 人员
CREATE TABLE `class_design`.`admin`  (
  `id` int NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `avatar` varchar(255) NULL,
  `salt` varchar(45) NOT NULL,
  `token` varchar(45) NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `class_design`.`user`  (
  `id` int NOT NULL,
  `username` varchar(45) NULL,
  `avatar` varchar(255) NULL,
  `password` varchar(255) NULL,
  `salt` varchar(45) NULL,
  `token` varchar(45) NULL,
  `phone` varchar(45) NULL,
  `address` varchar(255) NULL,
  `longitude` double(10, 5) NULL,
  `latitude` double(10, 5) NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `class_design`.`design_station_manager`  (
  `id` int NOT NULL,
  `username` varchar(45) NULL,
  `password` varchar(255) NULL,
  `salt` varchar(45) NULL,
  `token` varchar(45) NULL,
  `phone` varchar(45) NULL,
  `id_card` varchar(45) NULL,
  `station_id` int NULL,
  `avatar` varchar(255) NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `class_design`.`receive_station_manager`  (
  `id` int NOT NULL,
  `username` varchar(45) NULL,
  `password` varchar(255) NULL,
  `salt` varchar(45) NULL,
  `token` varchar(45) NULL,
  `phone` varchar(45) NULL,
  `id_card` varchar(45) NULL,
  `avatar` varchar(255) NULL,
  `station_id` int NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `class_design`.`go_manager`  (
  `id` int NOT NULL,
  `name` varchar(45) NULL,
  `phone` varchar(45) NULL,
  `city` varchar(45) NULL,
  PRIMARY KEY (`id`)
);

-- 运送单
CREATE TABLE `class_design`.`user_order`  (
  `id` int NOT NULL,
  `number` varchar(45) NULL,
  `status` tinyint NULL,
  `user_id` int NULL,
  `username` varchar(45) NULL,
  `phone` varchar(45) NULL,
  `total_weight` int NULL,
  `total_amount` int NULL,
  `address` varchar(255) NULL,
  `longitude` double(45, 0) NULL,
  `latitude` double(45, 0) NULL,
  `the_date` varchar(45) NULL,
  `design_id` int NULL,
  `design_name` varchar(45) NULL,
  `design_address` varchar(255) NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `class_design`.`design_order`  (
  `id` int NULL,
  `number` varchar(45) NULL,
  `status` tinyint NULL,
  `design_manager_id` int NULL,
  `design_manager_name` varchar(45) NULL,
  `design_manager_phone` varchar(45) NULL,
  `go_manager_name` varchar(45) NULL,
  `go_manager_phone` varchar(45) NULL,
  `receive_manager_id` int NULL,
  `receive_manager_name` varchar(45) NULL,
  `receive_manager_phone` varchar(45) NULL,
  `total_weight` int NULL,
  `total_amount` int NULL,
  `start_id` int NULL,
  `start_name` varchar(45) NULL,
  `start_address` varchar(255) NULL,
  `start_date` varchar(45) NULL,
  `end_id` int NULL,
  `end_name` varchar(45) NULL,
  `end_address` varchar(255) NULL,
  `end_date` varchar(45) NULL
);

-- 资源
CREATE TABLE `class_design`.`resource`  (
  `id` int NOT NULL,
  `type_code` tinyint NULL,
  `type_name` varchar(45) NULL,
  `weight` int NULL,
  `image` varchar(255) NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `class_design`.`design_station_resource`  (
  `station_id` int NOT NULL,
  `type_code` tinyint NULL,
  `amount` int NULL,
  PRIMARY KEY (`station_id`)
);

CREATE TABLE `class_design`.`receive_station_resource`  (
  `station_id` int NOT NULL,
  `type_code` tinyint NULL,
  `amount` int NULL,
  PRIMARY KEY (`station_id`)
);

CREATE TABLE `class_design`.`design_order_resource`  (
  `number` varchar(45) NOT NULL,
  `type_code` tinyint NULL,
  `amount` int NULL,
  PRIMARY KEY (`number`)
);

CREATE TABLE `class_design`.`user_order_resource`  (
  `number` varchar(45) NOT NULL,
  `type_code` tinyint NULL,
  `amount` int NULL,
  PRIMARY KEY (`number`)
);

----------------------------------------------------------数据--------------------------------------------------------
INSERT INTO `class_design`.`admin`(`id`, `username`, `password`, `salt`) VALUES (999999, 'admin', '4381A4A67D264FBC4C88629B9CB469BD', 'MySalt');





