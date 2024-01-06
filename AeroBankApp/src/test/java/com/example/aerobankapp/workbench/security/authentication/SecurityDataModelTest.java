package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.workbench.utilities.Role;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@RunWith(JUnit38ClassRunner.class)
class SecurityDataModelTest {

    private SecurityDataModel securityDataModel;

    @Mock
    private GrantedAuthority mockAuthority;

    @Mock
    private UserSecurityProfile mockSecurityProfile;

    @BeforeEach
    void setUp()
    {
        mockAuthority = mock(GrantedAuthority.class);
        mockSecurityProfile = mock(UserSecurityProfile.class);
    }

    @Test
    public void testSecurityDataModelConstructor()
    {
        Collection<GrantedAuthority> grantedAuthorityCollection = new HashSet<>();
        grantedAuthorityCollection.add(mockAuthority);
        securityDataModel = new SecurityDataModel(grantedAuthorityCollection, mockSecurityProfile);

        Collection<GrantedAuthority> authorities = securityDataModel.getGrantedAuthorities();
        UserSecurityProfile securityProfile = securityDataModel.getUserSecurityProfile();

        assertNotNull(securityDataModel);
        assertNotNull(authorities);
        assertNotNull(securityProfile);
    }

    @Test
    public void testSecurityDataModelNullConstructor()
    {

        assertThrows(NullPointerException.class,
                () -> {securityDataModel = new SecurityDataModel(null, null);});
    }

    @Test
    public void testAddingAuthorities()
    {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        GrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities.add(adminAuthority);

        Role adminRole = Role.ADMIN;
        UserSecurityProfile userSecurityProfile = new UserSecurityProfile(adminRole);
        UserSecurityProfile adminUserProfile = userSecurityProfile.getUserSecurityProfileFromFactory();

        securityDataModel = new SecurityDataModel(authorities, adminUserProfile);
        securityDataModel.addAuthorities(authorities, adminUserProfile);

        assertEquals(1, securityDataModel.getGrantedAuthorities().size());
    }




    @AfterEach
    void tearDown() {
    }
}