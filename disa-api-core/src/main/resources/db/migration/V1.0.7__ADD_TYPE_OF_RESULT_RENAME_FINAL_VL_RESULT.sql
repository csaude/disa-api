ALTER TABLE `VlData`
    ADD COLUMN `TypeOfResult` VARCHAR(50) NOT NULL,
    ADD COLUMN `Attribute1` VARCHAR(100) NULL,
    ADD COLUMN `Attribute2` VARCHAR(100) NULL,
    ADD COLUMN `Attribute3` VARCHAR(100) NULL,
    CHANGE COLUMN `FinalViralLoadResult` `FinalResult` VARCHAR(100) NULL DEFAULT NULL;

-- Set all current results to HIV Viral Load
UPDATE `VlData` SET `TypeOfResult` = 'HIVVL';
