package mz.org.fgh.disaapi.core.result.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "HIVVLLabResult")
@DiscriminatorValue("HIVVL")
public class HIVVLLabResult extends LabResult {

    @Column(name = "LastViralLoadResult")
    private String lastViralLoadResult;

    @Column(name = "LastViralLoadDate")
    private String lastViralLoadDate;

    @Column(name = "HIVVL_ViralLoadResult")
    private String hivViralLoadResult;

    @Column(name = "HIVVL_ViralLoadCAPCTM")
    private String viralLoadResultCopies;

    @Column(name = "HIVVL_VRLogValue")
    private String viralLoadResultLog;

    public String getLastViralLoadResult() {
        return lastViralLoadResult;
    }

    public void setLastViralLoadResult(String lastViralLoadResult) {
        this.lastViralLoadResult = lastViralLoadResult;
    }

    public String getLastViralLoadDate() {
        return lastViralLoadDate;
    }

    public void setLastViralLoadDate(String lastViralLoadDate) {
        this.lastViralLoadDate = lastViralLoadDate;
    }

    public String getHivViralLoadResult() {
        return hivViralLoadResult;
    }

    public void setHivViralLoadResult(String hivViralLoadResult) {
        this.hivViralLoadResult = hivViralLoadResult;
    }

    public String getViralLoadResultCopies() {
        return viralLoadResultCopies;
    }

    public void setViralLoadResultCopies(String viralLoadResultCopies) {
        this.viralLoadResultCopies = viralLoadResultCopies;
    }

    public String getViralLoadResultLog() {
        return viralLoadResultLog;
    }

    public void setViralLoadResultLog(String viralLoadResultLog) {
        this.viralLoadResultLog = viralLoadResultLog;
    }
}
