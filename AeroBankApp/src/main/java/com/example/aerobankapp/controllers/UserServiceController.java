package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.UserBooleanResponse;
import com.example.aerobankapp.workbench.UserNameResponse;
import com.example.aerobankapp.workbench.utilities.*;
import com.example.aerobankapp.workbench.utilities.response.PasswordVerificationResponse;
import com.example.aerobankapp.workbench.utilities.response.UserServiceResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.aerobankapp.controllers.utils.UserServiceControllerUtil.getUserDTOList;

@Controller
@RequestMapping(value="/api/users")
@CrossOrigin(value="http://localhost:3000")
public class UserServiceController {

    private final UserService userService;

    private Logger LOGGER = LoggerFactory.getLogger(UserServiceController.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceController(@Qualifier("userServiceImpl") UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserDTO>> getListOfUsers()
    {
        List<UserEntity> userEntities = userService.findAll();
        List<UserDTO> userDTOList = getUserDTOList(userEntities);

        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/find/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verifyUserExists(@PathVariable String username){
        boolean exists = userService.userNameExists(username);
        return ResponseEntity.ok(new UserBooleanResponse(exists));
    }

    @GetMapping("/email/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserEmailResponse> fetchUserEmail(@PathVariable String username){
        String email = userService.getEmailByUserName(username);
        return ResponseEntity.ok(new UserEmailResponse(email));
    }

    @GetMapping("/user-names-list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserNameResponse>> getListOfUserNames()
    {
        List<String> userNamesList = userService.getListOfUserNames();
        List<UserNameResponse> userNameResponses = getUserNameResponseList(userNamesList);
        return ResponseEntity.ok(userNameResponses);
    }

    private List<UserNameResponse> getUserNameResponseList(List<String> userNameList){
        List<UserNameResponse> userNameResponses = new ArrayList<>();
        for(String user : userNameList){
            UserNameResponse userNameResponse = new UserNameResponse(user);
            userNameResponses.add(userNameResponse);
        }
        return userNameResponses;
    }

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserDetailsByUserName(@PathVariable String username)
    {
        List<UserEntity> userEntity = userService.findByUserName(username);
        List<UserDTO> userDTOS = userEntity.stream()
                .map(this::convertToUserDTO)
                .toList();

        return ResponseEntity.ok(userDTOS);
    }

    @GetMapping("/id/{userName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserIDByUserName(@PathVariable String userName){
        int userID = userService.getUserIDByUserName(userName);
        return ResponseEntity.ok(userID);
    }

    @GetMapping("/id-num/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchUserIDByAccountNumber(@PathVariable String accountNumber){
        int userID = userService.getUserIDByAccountNumber(accountNumber);
        return ResponseEntity.ok(userID);
    }

    @PutMapping("/update-password")
    @PreAuthorize("isAuthenticated()")
    //TODO: UNIT TEST THIS METHOD
    public ResponseEntity<?> updateUserPassword(@RequestBody ResetRequest request){
        String username = request.getUser();
        String password = request.getPassword();
        LOGGER.info("Reset Request: " + request.toString());
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        try{
            userService.updateUserPassword(encryptedPassword, username);
            return ResponseEntity.ok("User: " + username + " password has been successfully updated.");
        }catch(Exception e){
            LOGGER.error("There was an error updating the password for user: " + username + "due to: " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("Unable to update user's password.");
    }

    //TODO: UNIT TEST METHOD
    @PostMapping("/verify-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verifyPasswordsMatch(@RequestBody PasswordVerificationRequest passwordVerificationRequest){
        String newPassword = passwordVerificationRequest.newPassword();
        String user = passwordVerificationRequest.user();
        if(newPassword.isEmpty() || user.isEmpty()){
            return ResponseEntity.badRequest().body("Request parameters are found empty.");
        }
        boolean passwordsMatch = userService.doesNewPasswordMatchCurrentPassword(user, newPassword);
        return ResponseEntity.ok(new PasswordVerificationResponse(passwordsMatch));
    }

    @GetMapping("/name/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUsersFullName(@PathVariable int id)
    {
        String fullName = userService.getUsersFullNameById(id);

        String[] segs = fullName.split(" ");
        String first_name = segs[0];
        String last_name = segs[1];

        return ResponseEntity.ok(new UserResponse(first_name, last_name));
    }



    private UserDTO convertToUserDTO(UserEntity userEntity)
    {
        return UserDTO.builder()
                .userID(userEntity.getUserID())
                .userName(userEntity.getUserCredentials().getUsername())
                .firstName(userEntity.getUserDetails().getFirstName())
                .lastName(userEntity.getUserDetails().getLastName())
                .isAdmin(userEntity.getUserSecurity().isAdmin())
                .email(userEntity.getUserDetails().getEmail())
                .pinNumber(userEntity.getUserSecurity().getPinNumber())
                .password(userEntity.getUserCredentials().getPassword())
                .accountNumber(userEntity.getUserDetails().getAccountNumber())
                .role(userEntity.getUserSecurity().getRole())
                .isEnabled(userEntity.getUserSecurity().isEnabled())
                .build();
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequest request, BindingResult bindingResult)
    {
        LOGGER.info("User Request: " + request.toString());
        if(bindingResult.hasErrors())
        {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

       String encryptedPassword = bCryptPasswordEncoder.encode(request.getPass());

        UserDetails userDetails = UserDetails.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .accountNumber(request.getAccountNumber())
                .build();

        UserSecurity userSecurity = UserSecurity.builder()
                .role(Role.valueOf(request.getRole()))
                .pinNumber(request.getPin())
                .isEnabled(true)
                .build();

        UserCredentials userCredentials = UserCredentials.builder()
                .password(encryptedPassword)
                .username(request.getUser())
                .build();

        UserEntity userEntity = UserEntity.builder()
                        .userID(request.getUserID())
                        .userCredentials(userCredentials)
                        .userDetails(userDetails)
                        .userSecurity(userSecurity)
                        .build();

        LOGGER.info("UserEntity: " + userEntity);

        // Validate if the User already exists in the database
        boolean userExists = userService.userNameExists(request.getUser());
        LOGGER.info("User: " + request.getUser() + " Exists: " + userExists);
        if(userExists)
        {
            userService.update(userEntity);
        }
        else
        {
            userService.save(userEntity);
        }

        return ResponseEntity.ok(new UserServiceResponse("User has been successfully saved"));

    }

    @GetMapping("/account/{user}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAccountNumberByUserName(@PathVariable String user)
    {
        String accountNumber = userService.getAccountNumberByUserName(user);
        return ResponseEntity.ok(new AccountNumberResponse(accountNumber));
    }

    @GetMapping("/exists/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> doesAccountNumberExist(@PathVariable String accountNumber){
        boolean exists = userService.doesAccountNumberExist(accountNumber);

        return ResponseEntity.ok(new AccountNumberBoolResponse(exists));
    }

    @GetMapping("/generateAccountNumber/{username}")
    @PreAuthorize("isAuthenticated() ")
    public ResponseEntity<?> generateAccountNumber(@PathVariable String username)
    {
       // String accountNumber = userService.generateAccountNumber(username);
        return ResponseEntity.ok("");
    }
}
