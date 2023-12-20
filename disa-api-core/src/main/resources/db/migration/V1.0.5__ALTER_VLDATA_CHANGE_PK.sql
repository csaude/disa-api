ALTER TABLE `VlData`
  CHANGE COLUMN `ID` `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  DROP PRIMARY KEY,
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE INDEX `RequestID_UNIQUE` (`RequestID` ASC);
