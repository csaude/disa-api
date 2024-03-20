package mz.org.fgh.disaapi.core.result.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "CD4LabResult")
@DiscriminatorValue("CD4")
public class CD4LabResult extends LabResult {

    @Column(name = "Attribute1")
    private String cd4Percentage;

    public String getCd4Percentage() {
        return cd4Percentage;
    }

    public void setCd4Percentage(String cd4Percentage) {
        this.cd4Percentage = cd4Percentage;
    }
}
