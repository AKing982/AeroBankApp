package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.utilities.AccountNumberResponse;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.UserRequest;
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

    @GetMapping("/user-names-list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> getListOfUserNames()
    {
        List<String> userNamesList = userService.getListOfUserNames();
        return ResponseEntity.ok(userNamesList);
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

    private UserDTO convertToUserDTO(UserEntity userEntity)
    {
        return UserDTO.builder()
                .userID(userEntity.getUserID())
                .userName(userEntity.getUsername())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .isAdmin(userEntity.isAdmin())
                .email(userEntity.getEmail())
                .pinNumber(userEntity.getPinNumber())
                .password(userEntity.getPassword())
                .accountNumber(userEntity.getAccountNumber())
                .role(userEntity.getRole())
                .isEnabled(userEntity.isEnabled())
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

        UserEntity userEntity = UserEntity.builder()
                        .username(request.getUser())
                        .userID(request.getUserID())
                        .role(Role.valueOf(request.getRole()))
                        .pinNumber(request.getPin())
                        .lastName(request.getLastName())
                        .firstName(request.getFirstName())
                        .email(request.getEmail())
                        .password(encryptedPassword)
                        .accountNumber(request.getAccountNumber())
                        .isEnabled(true)
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
}
