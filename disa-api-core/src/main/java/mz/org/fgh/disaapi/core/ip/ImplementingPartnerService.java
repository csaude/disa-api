package mz.org.fgh.disaapi.core.ip;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class ImplementingPartnerService {

    @Inject
    private ImplementingPartnerRepository partnerRepository;

    public ImplementingPartner findByOrgName(String orgName) {
        return partnerRepository.findByOrgName(orgName);
    }

    public void save(ImplementingPartner partner) {
        partnerRepository.save(partner);
    }

}
