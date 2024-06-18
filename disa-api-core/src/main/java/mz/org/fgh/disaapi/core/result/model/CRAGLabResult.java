package mz.org.fgh.disaapi.core.result.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Cryptococcus test
 */
@Entity(name = "GRAGLabResult")
@DiscriminatorValue("CRAG")
public class CRAGLabResult extends LabResult {

}
