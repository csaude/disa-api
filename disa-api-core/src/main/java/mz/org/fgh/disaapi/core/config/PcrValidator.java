package mz.org.fgh.disaapi.core.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mz.org.fgh.disaapi.core.exception.ValidationException;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.PcrEidLabResult;

public class PcrValidator implements ConstraintValidator<ValidNid, LabResult>{

	@Override
	public boolean isValid(LabResult labResult, ConstraintValidatorContext context) {
		System.out.println("Validating LabResult: " + labResult);
		System.out.println("Details: Type of result " + labResult.getTypeOfResult() + " Nid " + labResult.getNid()); 
		// Validate only if typeOfResult is PCR_EID
		if (labResult instanceof PcrEidLabResult) {
			String nid = labResult.getNid();
			
			// Ensure nid matches the regex
			if (!nid.matches("^[0-9]{10}/[0-9]{4}/[0-9]{5}$")) {
				throw new ValidationException("Invalid NID format for PCR_EID type.");
			}
			
			// Check if position 9 and 10 contains "24"
			String firstPart = nid.substring(0, 10);
			if (!"24".equals(firstPart.substring(8, 10))) {
				throw new ValidationException("NID does not contain '24' in the required position for PCR_EID type.");
			}
		}
		
		return true;
	}
}
