package mz.org.fgh.disaapi.core.result.model;

public enum NotProcessingCause {
	NID_NOT_FOUND,
	@Deprecated
	NO_RESULT,
	@Deprecated
	FLAGGED_FOR_REVIEW,
	DUPLICATE_NID,
	DUPLICATED_REQUEST_ID,
	INVALID_RESULT;
}
