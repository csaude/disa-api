package mz.org.fgh.disaapi.core.config;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Inherited
@Documented
@Constraint(validatedBy = PcrEidNidValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidPcrEidNid {
    String message() default "O NID não contém código '24' para tipo de serviço PCR_EID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
