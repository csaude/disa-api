package mz.org.fgh.disaapi.core.result.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "TBLamLabResult")
@DiscriminatorValue("TBLAM")
public class TBLamLabResult extends LabResult {

    @Column(name = "Attribute1")
    private String positivityLevel;

    public String getPositivityLevel() {
        return positivityLevel;
    }

    public void setPositivityLevel(String positivityLevel) {
        this.positivityLevel = positivityLevel;
    }

}
