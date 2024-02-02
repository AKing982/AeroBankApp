package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.UserRequest;
import com.example.aerobankapp.workbench.utilities.response.UserServiceResponse;
import jakarta.validation.Valid;
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

import static com.example.aerobankapp.controllers.utils.UserServiceControllerUtil.getUserDTOList;

@Controller
@RequestMapping(value="/api/users")
@CrossOrigin(value="http://localhost:3000")
public class UserServiceController {

    private final UserService userService;

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


        return ResponseEntity.ok("Posted");
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequest request, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

       String encryptedPassword = bCryptPasswordEncoder.encode(request.getPass());

        UserEntity userEntity = UserEntity.builder()
                        .username(request.getUser())
                        .role(Role.valueOf(request.getRole()))
                        .pinNumber(request.getPin())
                        .email(request.getEmail())
                        .password(encryptedPassword)
                        .accountNumber(request.getAccountNumber())
                        .isEnabled(true)
                        .build();

        userService.save(userEntity);

        return ResponseEntity.ok(new UserServiceResponse("User has been successfully saved"));

    }
}
