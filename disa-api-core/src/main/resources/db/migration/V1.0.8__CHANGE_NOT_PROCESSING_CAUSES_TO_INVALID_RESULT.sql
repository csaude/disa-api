UPDATE `VlData` SET `NOT_PROCESSING_CAUSE` = 'INVALID_RESULT'
WHERE `NOT_PROCESSING_CAUSE` in ('FLAGGED_FOR_REVIEW', 'NO_RESULT');
