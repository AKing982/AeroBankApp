package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import com.example.aerobankapp.workbench.security.authentication.factory.*;
import com.example.aerobankapp.workbench.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityProfileProducer
{
    private AbstractUserSecurityProfileFactory abstractUserSecurityProfileFactory;

    @Autowired
    private AuditorSecurityProfileFactory auditorSecurityProfileFactory;

    @Autowired
    private UserSecurityAdminProfileFactory userSecurityAdminProfileFactory;

    @Autowired
    private TellerSecurityProfileFactory tellerSecurityProfileFactory;

    @Autowired
    private StandardSecurityUserProfileFactory standardSecurityUserProfileFactory;

    @Autowired
    private ManagerSecurityProfileFactory managerSecurityProfileFactory;

    public UserSecurityProfile getSecurityProfileFactory(Role bankRole)
    {
        return switch (bankRole) {
            case AUDITOR -> {
                abstractUserSecurityProfileFactory = new AuditorSecurityProfileFactory();
                yield abstractUserSecurityProfileFactory.createAuthority();
            }
            case MANAGER -> {
                abstractUserSecurityProfileFactory = new ManagerSecurityProfileFactory();
                yield abstractUserSecurityProfileFactory.createAuthority();
            }
            case ADMIN -> {
                abstractUserSecurityProfileFactory = new UserSecurityAdminProfileFactory();
                yield abstractUserSecurityProfileFactory.createAuthority();
            }
            case TELLER -> {
                abstractUserSecurityProfileFactory = new TellerSecurityProfileFactory();
                yield abstractUserSecurityProfileFactory.createAuthority();
            }
            case CUSTOMER -> {
                abstractUserSecurityProfileFactory = new StandardSecurityUserProfileFactory();
                yield abstractUserSecurityProfileFactory.createAuthority();
            }
        };
    }
}
