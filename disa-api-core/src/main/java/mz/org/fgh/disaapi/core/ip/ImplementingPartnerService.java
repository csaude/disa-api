package mz.org.fgh.disaapi.core.ip;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

@Service
public class ImplementingPartnerService {

    @Inject
    private ImplementingPartnerRepository partnerRepository;

    @Transactional
    public ImplementingPartner findByOrgName(String orgName) {
        ImplementingPartner partner = partnerRepository.findByOrgName(orgName);
        if (partner != null) {
            Hibernate.initialize(partner.getOrgUnits());
        }
        return partner;
    }

    public void save(ImplementingPartner partner) {
        partnerRepository.save(partner);
    }

}
