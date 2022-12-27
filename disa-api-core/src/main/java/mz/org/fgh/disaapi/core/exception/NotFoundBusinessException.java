package mz.org.fgh.disaapi.core.exception;

import mz.co.msaude.boot.frameworks.exception.BusinessException;

public class NotFoundBusinessException extends BusinessException {

    public NotFoundBusinessException() {
    }

    public NotFoundBusinessException(String message) {
        super(message);
    }

}
