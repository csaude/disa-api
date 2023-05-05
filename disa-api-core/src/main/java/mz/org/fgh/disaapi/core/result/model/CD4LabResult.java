package mz.org.fgh.disaapi.core.result.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "CD4LabResult")
@DiscriminatorValue("CD4")
public class CD4LabResult extends LabResult {

    @Column(name = "CD4Percentage")
    private String cd4Percentage;

    @Column(name = "CD4FinalResult")
    private Integer cd4FinalResult;

    public String getCd4Percentage() {
        return cd4Percentage;
    }

    public void setCd4Percentage(String cd4Percentage) {
        this.cd4Percentage = cd4Percentage;
    }

    public Integer getCd4FinalResult() {
        return cd4FinalResult;
    }

    public void setCd4FinalResult(Integer cd4FinalResult) {
        this.cd4FinalResult = cd4FinalResult;
    }
}
