package com.example.aerobankapp.workbench.threadServices;

import com.example.aerobankapp.model.RegistrationDTO;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.LoginService;
import com.example.aerobankapp.services.UserDAOService;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.generator.AccountNumberGenerator;
import com.example.aerobankapp.workbench.security.authentication.UserAuthority;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService
{
    private final AeroLogger aeroLogger = new AeroLogger(LoginServiceImpl.class);
    private AuthenticationManager authenticationManager;
    private final UserDAOService userRepo;

    @Autowired
    public LoginServiceImpl(UserServiceImpl userRepository)
    {
        this.userRepo = userRepository;
    }

    @Override
    public boolean authenticateUser(String user, String pass) {
        try{
            Authentication authenticate = new UsernamePasswordAuthenticationToken(user, pass);
            authenticate = authenticationManager.authenticate(authenticate);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return authenticate.isAuthenticated();
        }catch(AuthenticationException ex)
        {
            aeroLogger.error("AuthenticationException: ", ex);
        }
        return false;
    }

    @Override
    public void login()
    {

    }

    @Override
    public boolean registerUser(RegistrationDTO registrationRequest)
    {
        boolean isRegistered = false;
        if(registrationRequest != null)
        {
            // Build the user
            if(registrationRequest.isAdmin())
            {
                User admin = User.builder()
                        .user(registrationRequest.getUserName())
                        .email(registrationRequest.getEmail())
                        .password(registrationRequest.getPassword())
                    //    .accountNumber(AccountNumberGenerator::generate)
                        .pinNumber(registrationRequest.getPinNumber())
                        .userAuthority(UserAuthority.createAdminAuthority())
                        .build();
                isRegistered = true;

               // userRepo.save(admin);
            }
            else
            {
                User user = User.builder()
                        .user(registrationRequest.getUserName())
                        .email(registrationRequest.getEmail())
                        .password(registrationRequest.getPassword())
                    //    .accountNumber(AccountNumberGenerator::generate)
                        .pinNumber(registrationRequest.getPinNumber())
                        .userAuthority(UserAuthority.createUserAuthority())
                        .build();
                isRegistered = true;

                //userRepo.save(user);
            }

            //TODO:  Show Dialog that User Creation has been successful

            return isRegistered;
        }
        return false;
    }

}
