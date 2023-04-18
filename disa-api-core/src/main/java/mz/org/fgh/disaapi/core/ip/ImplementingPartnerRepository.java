package mz.org.fgh.disaapi.core.ip;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImplementingPartnerRepository extends JpaRepository<ImplementingPartner, Integer> {

    ImplementingPartner findByOrgName(String orgName);

}
