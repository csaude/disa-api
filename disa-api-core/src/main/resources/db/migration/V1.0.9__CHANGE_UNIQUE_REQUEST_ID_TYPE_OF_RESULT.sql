ALTER TABLE `VlData`
    DROP INDEX `RequestID_UNIQUE`,
    ADD UNIQUE INDEX `RequestID_TypeOfResult_UNIQUE` (`RequestID` ASC, `TypeOfResult` ASC);
