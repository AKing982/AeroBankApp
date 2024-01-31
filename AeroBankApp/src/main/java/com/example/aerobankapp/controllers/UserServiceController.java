package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.utilities.response.UserServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static com.example.aerobankapp.controllers.utils.UserServiceControllerUtil.getUserDTOList;

@Controller
@RequestMapping(value="/api/users", method = RequestMethod.GET)
@CrossOrigin(value="http://localhost:3000")
public class UserServiceController {

    private final UserService userService;

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
        return null;
    }
}
