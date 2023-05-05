ALTER TABLE `VlData`
    ADD COLUMN `TypeOfResult` VARCHAR(50) NOT NULL,
    ADD COLUMN `CD4Percentage` VARCHAR(80) NULL AFTER `TypeOfResult`,
    ADD COLUMN `CD4FinalResult` INT NULL AFTER `CD4Percentage`;

-- Set all current results to HIV Viral Load
UPDATE `VlData` SET `TypeOfResult` = 'HIVVL';
