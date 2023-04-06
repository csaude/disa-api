package mz.org.fgh.disaapi.core.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import mz.org.fgh.disaapi.core.ip.ImplementingPartner;
import mz.org.fgh.disaapi.core.ip.ImplementingPartnerService;

public class ImplementingPartnerUserDetailsManager implements UserDetailsManager {

    private ImplementingPartnerService partnerService;

    public ImplementingPartnerUserDetailsManager(ImplementingPartnerService partnerRepository) {
        this.partnerService = partnerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ImplementingPartner partner = partnerService.findByOrgName(username);
        if (partner == null) {
            throw new UsernameNotFoundException("Username" + username + "not found");
        }
        return partner;
    }

    @Override
    public void createUser(UserDetails user) {
        ImplementingPartner partner = new ImplementingPartner();
        partner.setOrgName(user.getUsername());
        partner.setPassword(user.getPassword());
        partner.setEnabled(true);
        partnerService.save(partner);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        return partnerService.findByOrgName(username) != null;
    }

}
