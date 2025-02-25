package mz.org.fgh.disaapi.core.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.PcrEidLabResult;

public class PcrEidNidValidator implements ConstraintValidator<ValidPcrEidNid, LabResult> {

	@Override
	public boolean isValid(LabResult labResult, ConstraintValidatorContext context) {
		// Verifica se o objeto é do tipo PCR_EID com base na classe concreta
		if (labResult instanceof PcrEidLabResult) {
			String nid = labResult.getNid();
			// Positions 9 and 10 in human terms correspond to substring(8, 10) in Java (0-indexed).
			String requiredSubstring = nid.substring(8, 10);
			if (!"24".equals(requiredSubstring)) {
				return false;
			}
		}
		return true;
	}
}
