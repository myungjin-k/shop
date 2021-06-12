-- MySQL Script generated by MySQL Workbench
-- Fri Jun 11 17:15:38 2021
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mall_order
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mall_order
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mall_order` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;
USE `mall_order` ;

-- -----------------------------------------------------
-- Table `mall_order`.`order_master`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mall_order`.`order_master` ;

CREATE TABLE IF NOT EXISTS `mall_order`.`order_master` (
  `id` VARCHAR(50) NOT NULL,
  `item_name_abbr` VARCHAR(255) NOT NULL,
  `total_amount` INT NOT NULL DEFAULT 0,
  `status` VARCHAR(10) NOT NULL,
  `create_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP(),
  `update_at` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mall_order`.`order_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mall_order`.`order_item` ;

CREATE TABLE IF NOT EXISTS `mall_order`.`order_item` (
  `id` VARCHAR(50) NOT NULL,
  `count` INT NOT NULL DEFAULT 0,
  `create_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `update_at` DATETIME NULL,
  `order_id` VARCHAR(50) NOT NULL,
  `item_id` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_ORDER_ITEM_TO_ORDER_MASTER_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `FK_ORDER_ITEM_TO_ORDER_MASTER`
    FOREIGN KEY (`order_id`)
    REFERENCES `mall_order`.`order_master` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
