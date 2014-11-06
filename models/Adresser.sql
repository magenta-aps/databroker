SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Table `Kommune`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Kommune` (
  `KommuneId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `Kommunekode` INT(4) UNSIGNED NOT NULL ,
  `Navn` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`KommuneId`) ,
  UNIQUE INDEX `Kommunekode_UNIQUE` (`Kommunekode` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Registrering`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Registrering` (
  `RegistreringId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `RegistreringsType` VARCHAR(255) NULL ,
  `Oprettet` DATETIME NULL ,
  `Aendret` DATETIME NULL ,
  PRIMARY KEY (`RegistreringId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Virkning`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Virkning` (
  `VirkningId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `Aktiv` TINYINT(1) NULL ,
  `VirkningFra` DATETIME NULL ,
  `VirkningTil` DATETIME NULL ,
  PRIMARY KEY (`VirkningId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ReserveretVejnavn`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ReserveretVejnavn` (
  `ReserveretVejnavnId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `ReserveretAf` INT UNSIGNED NOT NULL ,
  `ReserveretVejnavnUUID` CHAR(36) UNICODE NOT NULL ,
  `Navneomraade` TEXT NULL ,
  `ReserveretNavn` VARCHAR(255) NULL ,
  `Status` VARCHAR(255) NULL ,
  `Reservationsudloebsdato` DATE NULL ,
  `Retskrivningskontrol` VARCHAR(255) NULL ,
  `RegistreringId` INT UNSIGNED NOT NULL ,
  `VirkningId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`ReserveretVejnavnId`) ,
  INDEX `reserveret_vejnavn_kommune_ref_idx` (`ReserveretAf` ASC) ,
  INDEX `fk_ReserveretVejnavn_Registrering1_idx` (`RegistreringId` ASC) ,
  INDEX `fk_ReserveretVejnavn_Virkning1_idx` (`VirkningId` ASC) ,
  UNIQUE INDEX `RegistreringId_UNIQUE` (`RegistreringId` ASC) ,
  UNIQUE INDEX `ReserveretVejnavnUUID_UNIQUE` (`ReserveretVejnavnUUID` ASC) ,
  CONSTRAINT `ReserveretVejnavnKommuneRef`
    FOREIGN KEY (`ReserveretAf` )
    REFERENCES `Kommune` (`KommuneId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ReserveretVejnavn_Registrering1`
    FOREIGN KEY (`RegistreringId` )
    REFERENCES `Registrering` (`RegistreringId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ReserveretVejnavn_Virkning1`
    FOREIGN KEY (`VirkningId` )
    REFERENCES `Virkning` (`VirkningId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ISOpoint`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ISOpoint` (
  `ISOpointId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `x` DOUBLE UNSIGNED NULL ,
  `y` DOUBLE UNSIGNED NULL ,
  `z` DOUBLE NULL ,
  PRIMARY KEY (`ISOpointId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vejnavneomraade`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Vejnavneomraade` (
  `VejnavneomraadeId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `VejnavneomraadeUUID` CHAR(36) UNICODE NOT NULL ,
  `Vejnavneomraade` TEXT NOT NULL ,
  `Vejnavnelinje` VARCHAR(255) NULL ,
  `VejtilslutningspunktId` INT UNSIGNED NOT NULL ,
  `Noejagtighedsklasse` VARCHAR(255) NULL ,
  `Kilde` VARCHAR(255) NULL ,
  `TekniskStandard` VARCHAR(255) NULL ,
  `RegistreringId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`VejnavneomraadeId`) ,
  INDEX `fk_Vejnavneomraade_Registrering1_idx` (`RegistreringId` ASC) ,
  INDEX `fk_Vejnavneomraade_ISOpoint1_idx` (`VejtilslutningspunktId` ASC) ,
  UNIQUE INDEX `RegistreringId_UNIQUE` (`RegistreringId` ASC) ,
  UNIQUE INDEX `VejnavneomraadeUUID_UNIQUE` (`VejnavneomraadeUUID` ASC) ,
  CONSTRAINT `fk_Vejnavneomraade_Registrering1`
    FOREIGN KEY (`RegistreringId` )
    REFERENCES `Registrering` (`RegistreringId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Vejnavneomraade_ISOpoint1`
    FOREIGN KEY (`VejtilslutningspunktId` )
    REFERENCES `ISOpoint` (`ISOpointId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `NavngivenVej`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `NavngivenVej` (
  `NavngivenVejId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `NavngivenVejUUID` CHAR(36) UNICODE NOT NULL ,
  `Vejnavn` VARCHAR(255) NULL ,
  `Status` VARCHAR(255) NULL ,
  `Vejaddresseringsnavn` VARCHAR(20) NULL ,
  `Beskrivelse` TEXT NULL ,
  `Retskrivningskontrol` VARCHAR(255) NULL ,
  `AnsvarligKommuneId` INT UNSIGNED NOT NULL ,
  `VejnavneomraadeId` INT UNSIGNED NOT NULL ,
  `RegistreringId` INT UNSIGNED NOT NULL ,
  `VirkningId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`NavngivenVejId`) ,
  INDEX `AnsvarligKommuneRef_idx` (`AnsvarligKommuneId` ASC) ,
  UNIQUE INDEX `VejnavneomraadeId_UNIQUE` (`VejnavneomraadeId` ASC) ,
  INDEX `fk_NavngivenVej_Registrering1_idx` (`RegistreringId` ASC) ,
  INDEX `fk_NavngivenVej_Virkning1_idx` (`VirkningId` ASC) ,
  UNIQUE INDEX `RegistreringId_UNIQUE` (`RegistreringId` ASC) ,
  CONSTRAINT `NavngivenVejAnsvarligKommuneRef`
    FOREIGN KEY (`AnsvarligKommuneId` )
    REFERENCES `Kommune` (`KommuneId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_NavngivenVej_Vejnavneomraade1`
    FOREIGN KEY (`VejnavneomraadeId` )
    REFERENCES `Vejnavneomraade` (`VejnavneomraadeId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_NavngivenVej_Registrering1`
    FOREIGN KEY (`RegistreringId` )
    REFERENCES `Registrering` (`RegistreringId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_NavngivenVej_Virkning1`
    FOREIGN KEY (`VirkningId` )
    REFERENCES `Virkning` (`VirkningId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KommunedelAfNavngivenVej`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `KommunedelAfNavngivenVej` (
  `KommunedelAfNavngivenVejId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `Vejkode` INT(4) UNSIGNED NOT NULL ,
  `NavngivenVej_NavngivenVejId` INT UNSIGNED NOT NULL ,
  `Kommune_KommuneId` INT UNSIGNED NOT NULL ,
  INDEX `fk_NavngivenVej_has_Kommune_Kommune1_idx` (`Kommune_KommuneId` ASC) ,
  INDEX `fk_NavngivenVej_has_Kommune_NavngivenVej1_idx` (`NavngivenVej_NavngivenVejId` ASC) ,
  PRIMARY KEY (`KommunedelAfNavngivenVejId`) ,
  CONSTRAINT `fk_NavngivenVej_has_Kommune_NavngivenVej1`
    FOREIGN KEY (`NavngivenVej_NavngivenVejId` )
    REFERENCES `NavngivenVej` (`NavngivenVejId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_NavngivenVej_has_Kommune_Kommune1`
    FOREIGN KEY (`Kommune_KommuneId` )
    REFERENCES `Kommune` (`KommuneId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ReserveretUligeHusnrInterval`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ReserveretUligeHusnrInterval` (
  `ReserveretUligeHusnrIntervalId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `KommunedelAfNavngivenVejId` INT UNSIGNED NOT NULL ,
  `IntervalStart` INT UNSIGNED NULL ,
  `IntervalSlut` INT UNSIGNED NULL ,
  `Notat` TEXT NULL ,
  PRIMARY KEY (`ReserveretUligeHusnrIntervalId`) ,
  INDEX `fk_KommunedelAfNavngivenVejReserveretUligeHusnrInterval_Kom_idx` (`KommunedelAfNavngivenVejId` ASC) ,
  CONSTRAINT `fk_KommunedelAfNavngivenVejReserveretUligeHusnrInterval_Kommu1`
    FOREIGN KEY (`KommunedelAfNavngivenVejId` )
    REFERENCES `KommunedelAfNavngivenVej` (`KommunedelAfNavngivenVejId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ReserveretLigeHusnrInterval`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ReserveretLigeHusnrInterval` (
  `ReserveretLigeHusnrIntervalId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `KommunedelAfNavngivenVejId` INT UNSIGNED NOT NULL ,
  `IntervalStart` INT UNSIGNED NULL ,
  `IntervalSlut` INT UNSIGNED NULL ,
  `Notat` TEXT NULL ,
  PRIMARY KEY (`ReserveretLigeHusnrIntervalId`) ,
  INDEX `fk_KommunedelAfNavngivenVejReserveretUligeHusnrInterval_Kom_idx` (`KommunedelAfNavngivenVejId` ASC) ,
  CONSTRAINT `fk_KommunedelAfNavngivenVejReserveretUligeHusnrInterval_Kommu10`
    FOREIGN KEY (`KommunedelAfNavngivenVejId` )
    REFERENCES `KommunedelAfNavngivenVej` (`KommunedelAfNavngivenVejId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Postnummer`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Postnummer` (
  `PostnummerId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`PostnummerId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Adgangspunkt`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Adgangspunkt` (
  `AdgangspunktId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `AdgangspunktUUID` CHAR(36) UNICODE NOT NULL ,
  `Status` VARCHAR(255) NOT NULL ,
  `PositionId` INT UNSIGNED NOT NULL ,
  `AdgangspunktsretningId` INT UNSIGNED NOT NULL ,
  `HusnummerretningId` INT UNSIGNED NOT NULL ,
  `Noejagtighedsklasse` VARCHAR(255) NOT NULL ,
  `Kilde` VARCHAR(255) NOT NULL ,
  `TekniskStandard` VARCHAR(255) NOT NULL ,
  `LiggerIPostnummerId` INT UNSIGNED NOT NULL ,
  `RegistreringId` INT UNSIGNED NOT NULL ,
  `VirkningId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`AdgangspunktId`) ,
  INDEX `fk_Adgangspunkt_Postnummer1_idx` (`LiggerIPostnummerId` ASC) ,
  INDEX `fk_Adgangspunkt_Virkning1_idx` (`VirkningId` ASC) ,
  INDEX `fk_Adgangspunkt_ISOpoint1_idx` (`PositionId` ASC) ,
  INDEX `fk_Adgangspunkt_ISOpoint2_idx` (`AdgangspunktsretningId` ASC) ,
  INDEX `fk_Adgangspunkt_ISOpoint3_idx` (`HusnummerretningId` ASC) ,
  INDEX `fk_Adgangspunkt_Registrering1_idx` (`RegistreringId` ASC) ,
  UNIQUE INDEX `AdgangspunktUUID_UNIQUE` (`AdgangspunktUUID` ASC) ,
  UNIQUE INDEX `RegistreringId_UNIQUE` (`RegistreringId` ASC) ,
  CONSTRAINT `fk_Adgangspunkt_Postnummer1`
    FOREIGN KEY (`LiggerIPostnummerId` )
    REFERENCES `Postnummer` (`PostnummerId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Adgangspunkt_Virkning1`
    FOREIGN KEY (`VirkningId` )
    REFERENCES `Virkning` (`VirkningId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Adgangspunkt_ISOpoint1`
    FOREIGN KEY (`PositionId` )
    REFERENCES `ISOpoint` (`ISOpointId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Adgangspunkt_ISOpoint2`
    FOREIGN KEY (`AdgangspunktsretningId` )
    REFERENCES `ISOpoint` (`ISOpointId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Adgangspunkt_ISOpoint3`
    FOREIGN KEY (`HusnummerretningId` )
    REFERENCES `ISOpoint` (`ISOpointId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Adgangspunkt_Registrering1`
    FOREIGN KEY (`RegistreringId` )
    REFERENCES `Registrering` (`RegistreringId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Husnummer`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Husnummer` (
  `HusnummerId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `HusnummerUUID` CHAR(36) UNICODE NOT NULL ,
  `NavngivenVejId` INT UNSIGNED NOT NULL ,
  `TilknyttetAdgangspunktId` INT UNSIGNED NOT NULL ,
  `Husnummerbetegnelse` VARCHAR(255) NULL ,
  PRIMARY KEY (`HusnummerId`) ,
  INDEX `fk_Husnummer_NavngivenVej1_idx` (`NavngivenVejId` ASC) ,
  INDEX `fk_Husnummer_Adgangspunkt1_idx` (`TilknyttetAdgangspunktId` ASC) ,
  UNIQUE INDEX `AdgangspunktId_UNIQUE` (`TilknyttetAdgangspunktId` ASC) ,
  UNIQUE INDEX `Husnummer_unique_virkning` (`HusnummerUUID` ASC, `NavngivenVejId` ASC, `TilknyttetAdgangspunktId` ASC) ,
  CONSTRAINT `fk_Husnummer_NavngivenVej1`
    FOREIGN KEY (`NavngivenVejId` )
    REFERENCES `NavngivenVej` (`NavngivenVejId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Husnummer_Adgangspunkt1`
    FOREIGN KEY (`TilknyttetAdgangspunktId` )
    REFERENCES `Adgangspunkt` (`AdgangspunktId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Doerpunkt`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Doerpunkt` (
  `DoerpunktId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `DoerpunktUUID` CHAR(36) UNICODE NOT NULL ,
  `PositionId` INT UNSIGNED NOT NULL ,
  `Noejagtighedsklasse` VARCHAR(255) NULL ,
  `Kilde` VARCHAR(255) NULL ,
  `TekniskStandard` VARCHAR(255) NULL ,
  `RegistreringId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`DoerpunktId`) ,
  INDEX `fk_Doerpunkt_Registrering1_idx` (`RegistreringId` ASC) ,
  UNIQUE INDEX `DoerpunktUUID_UNIQUE` (`DoerpunktUUID` ASC) ,
  INDEX `fk_Doerpunkt_ISOpoint1_idx` (`PositionId` ASC) ,
  UNIQUE INDEX `RegistreringId_UNIQUE` (`RegistreringId` ASC) ,
  CONSTRAINT `fk_Doerpunkt_Registrering1`
    FOREIGN KEY (`RegistreringId` )
    REFERENCES `Registrering` (`RegistreringId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Doerpunkt_ISOpoint1`
    FOREIGN KEY (`PositionId` )
    REFERENCES `ISOpoint` (`ISOpointId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Adresse`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Adresse` (
  `AdresseId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `AdresseUUID` CHAR(36) UNICODE NOT NULL ,
  `Status` VARCHAR(255) NOT NULL ,
  `DoerpunktId` INT UNSIGNED NULL ,
  `Doerbetegnelse` VARCHAR(255) NULL ,
  `Etagebetegnelse` VARCHAR(255) NULL ,
  `HusnummerId` INT UNSIGNED NOT NULL ,
  `RegistreringId` INT UNSIGNED NOT NULL ,
  `VirkningId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`AdresseId`) ,
  INDEX `fk_Adresse_Husnummer1_idx` (`HusnummerId` ASC) ,
  INDEX `fk_Adresse_Doerpunkt1_idx` (`DoerpunktId` ASC) ,
  UNIQUE INDEX `DoerpunktId_UNIQUE` (`DoerpunktId` ASC) ,
  INDEX `fk_Adresse_Registrering1_idx` (`RegistreringId` ASC) ,
  INDEX `fk_Adresse_Virkning1_idx` (`VirkningId` ASC) ,
  UNIQUE INDEX `RegistreringId_UNIQUE` (`RegistreringId` ASC) ,
  CONSTRAINT `fk_Adresse_Husnummer1`
    FOREIGN KEY (`HusnummerId` )
    REFERENCES `Husnummer` (`HusnummerId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Adresse_Doerpunkt1`
    FOREIGN KEY (`DoerpunktId` )
    REFERENCES `Doerpunkt` (`DoerpunktId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Adresse_Registrering1`
    FOREIGN KEY (`RegistreringId` )
    REFERENCES `Registrering` (`RegistreringId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Adresse_Virkning1`
    FOREIGN KEY (`VirkningId` )
    REFERENCES `Virkning` (`VirkningId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vejnavneforslag`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Vejnavneforslag` (
  `VejnavneforslagId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `Navn` VARCHAR(255) NULL ,
  `NavngivenVejId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`VejnavneforslagId`) ,
  INDEX `fk_Vejnavneforslag_NavngivenVej1_idx` (`NavngivenVejId` ASC) ,
  CONSTRAINT `fk_Vejnavneforslag_NavngivenVej1`
    FOREIGN KEY (`NavngivenVejId` )
    REFERENCES `NavngivenVej` (`NavngivenVejId` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
