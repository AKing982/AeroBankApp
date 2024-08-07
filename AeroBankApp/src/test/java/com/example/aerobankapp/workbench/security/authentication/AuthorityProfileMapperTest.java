package com.example.aerobankapp.workbench.security.authentication;

import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnit38ClassRunner.class)
class AuthorityProfileMapperTest {

    private AuthorityProfileMapper authorityProfileMapper;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    @DisplayName("Test Constructor")
    public void testConstructor()
    {
        authorityProfileMapper = new AuthorityProfileMapper();
        assertNotNull(authorityProfileMapper);
    }

    @Test
    @DisplayName("Test All Arg Constructor with Null Values")
    public void testAllArgsConstructorWithNulls()
    {
        authorityProfileMapper = new AuthorityProfileMapper();

        assertNotNull(authorityProfileMapper);

    }

    @ParameterizedTest
    @MethodSource("provideRolesForTesting")
    public void testUserSecurityProfileRetreival(GrantedAuthority authority, UserSecurityProfile profile)
    {
        authorityProfileMapper = new AuthorityProfileMapper();
        authorityProfileMapper.addSecurityProfile(authority, profile);

     //   securityDataMap.addSecurityProfile(role, result);
     //   UserSecurityProfile actualProfile = securityDataMap.getUserSecurityProfile(role);

       // assertEquals(result, actualProfile);
    }

    private static Stream<Arguments> provideRolesForTesting()
    {
        return Stream.of(
              //  Arguments.of("Admin", UserSecurityProfile.createAdminAuthority())
        );
    }

    @ParameterizedTest
    @MethodSource("provideValuesToAddSecurityProfiles")
    public void testAddingSecurityProfiles(String role, UserSecurityProfile userSecurityProfile)
    {
        authorityProfileMapper = new AuthorityProfileMapper();
       // securityDataMap.addSecurityProfile(role, userSecurityProfile);

      //  assertEquals(userSecurityProfile, securityDataMap.getUserSecurityProfile(role));
        assertNotNull(userSecurityProfile);
        assertNotNull(role);
      //  assertNotNull(securityDataMap.getUserSecurityProfile(role));

    }



    @Test
    public void testGetRoleFromAuthority()
    {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ADMIN");
       // UserSecurityProfile adminProfile = UserSecurityProfile.createAdminAuthority();
        authorityProfileMapper = new AuthorityProfileMapper();

     //   authorityProfileMapper.addSecurityProfile(grantedAuthority, adminProfile);
        String authority = authorityProfileMapper.getRoleFromAuthority(grantedAuthority);

        assertEquals("ADMIN", authority);
    }

    @AfterEach
    void tearDown() {
    }
}