package com.example.aerobankapp.controllers;


import com.example.aerobankapp.dto.UserLogDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.services.UserLogService;
import com.example.aerobankapp.workbench.UserLogResponse;
import com.example.aerobankapp.workbench.utilities.LogoutRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value="/api/session")
@CrossOrigin(value="http://localhost:3000")
public class UserLogController
{
    private final UserLogService userLogService;
    private final Logger LOGGER = LoggerFactory.getLogger(UserLogController.class);

    @Autowired
    public UserLogController(UserLogService userLogService)
    {
        this.userLogService = userLogService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<UserLogResponse> getUserLogById(@PathVariable int id)
    {
        return null;
       // return ResponseEntity.ok(new UserLogResponse(id, 1, "AKing94", LocalDateTime.now(), LocalDateTime.now(), 6049, true, 1, "ajajajsdfasdf"));
    }

    @GetMapping("/byAttempts")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getUserLogByNumberOfLoginAttempts(@RequestParam("Attempts") int attempts, @RequestParam("UserID") int userID){
        if(attempts < 0 || userID < 1){
            return ResponseEntity.badRequest().body("Invalid Number of Attempts or UserID has been entered.");
        }

        UserLogEntity userLogEntityOptional = userLogService.getUserLogByNumberOfLoginAttempts(attempts, userID);
        if(userLogEntityOptional == null){
            return ResponseEntity.badRequest().body("No UserLogs could be found.");
        }
        LOGGER.info("UserLog: " + userLogEntityOptional.toString());

        int foundUserID = userLogEntityOptional.getUserEntity().getUserID();
        int sessionID = userLogEntityOptional.getId();
        String userName = userLogEntityOptional.getUserEntity().getUsername();
        LocalDateTime lastLogout = userLogEntityOptional.getLastLogout();
        LocalDateTime lastLogin = userLogEntityOptional.getLastLogin();
        int sessionDuration = userLogEntityOptional.getSessionDuration();
        boolean loginSuccess = userLogEntityOptional.isLoginSuccess();
        int loginAttempts = userLogEntityOptional.getLoginAttempts();

        UserLogResponse userLogResponse = new UserLogResponse(sessionID, foundUserID, userName, lastLogout, lastLogin, sessionDuration, loginSuccess, loginAttempts, "");

        return ResponseEntity.ok(userLogResponse);
    }

    @GetMapping("/byActiveState")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findUserLogEntriesByActiveStateAndUserID(@RequestParam("isActive") boolean isActive, @RequestParam("userID") int userID)
    {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID has been entered.");
        }

        Optional<UserLogEntity> userLogEntity = userLogService.findUserLogEntriesByActiveStateAndUserID(isActive, userID);
        if(userLogEntity == null){
            return ResponseEntity.badRequest().body("No User Log records have been retrieved from the database.");
        }

        return ResponseEntity.ok(userLogEntity);
    }

    @GetMapping("/byLastLogin")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserLogsByLastLogin(@RequestParam("id") Long id, @RequestParam("lastLogin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastLogin){
        if(id < 1){
            return ResponseEntity.badRequest().body("Invalid ID has been entered.");
        }
            List<UserLogEntity> userLogEntities = userLogService.getUserLogsByLastLogin(id, lastLogin);
            LOGGER.info("Found User Logs: " + userLogEntities);
            if(userLogEntities.isEmpty()){
                return ResponseEntity.badRequest().body("Unable to retrieve User Logs from the database.");
            }

            return ResponseEntity.ok(userLogEntities);
    }

    @PostMapping("/addUserLog")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createUserLog(@RequestBody UserLogDTO userLogDTO) {
        Long foundID = userLogDTO.id();
        try
        {
           UserLogEntity userLogEntity = createUserLogEntity(userLogDTO);

            userLogService.save(userLogEntity);
            return ResponseEntity.ok("User Log Was successfully created.");

        }catch(Exception e)
        {
            return ResponseEntity.badRequest().body("There was an error saving UserLog with id: " + foundID + " due to error: " + e.getMessage());
        }
    }

    @PutMapping("/updateUserLog/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUserLog(@PathVariable Long id, @RequestBody UserLogDTO userLogDTO){
        Long foundID = userLogDTO.id();
        boolean isActive = userLogDTO.isActive();
        LocalDateTime lastLogin = userLogDTO.lastLogin();
        LocalDateTime lastLogout = userLogDTO.lastLogout();
        int duration = userLogDTO.sessionDuration();
        boolean success = userLogDTO.loginSuccess();
        int attempts = userLogDTO.loginAttempts();

        try
        {
            userLogService.updateUserLog(foundID, isActive, lastLogin, lastLogout, attempts, success, duration);
            return ResponseEntity.ok("Updating the User Log with id: " + foundID + " was successful");

        }catch(Exception e)
        {
            return ResponseEntity.badRequest().body("There was an error Updating the User Log: " + e.getMessage());
        }

    }

    @PutMapping("/updateUser/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserEntity userEntity) {
        userLogService.updateUser(id, userEntity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/currentLoggedOnUser/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> getCurrentLoggedOnUserID(@PathVariable Long id) {
        try {
            int userId = userLogService.getCurrentLoggedOnUserID(id);
            return ResponseEntity.ok(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/isCurrentlyLoggedIn")
    public ResponseEntity<Boolean> isUserCurrentlyLoggedIn(@RequestParam int userID) {
        return ResponseEntity.ok(userLogService.isUserCurrentlyLoggedIn(userID));
    }

    @GetMapping("/{user}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<List<UserLogResponse>> getUserLogByUserName(@PathVariable String user){
        return null;
    }


    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllUserLogs()
    {
        return null;
    }


    private UserLogEntity createUserLogEntity(UserLogDTO userLogDTO){
        UserLogEntity userLogEntity = new UserLogEntity();
        userLogEntity.setId(userLogEntity.getId());
        userLogEntity.setActive(userLogDTO.isActive());
        userLogEntity.setLastLogout(userLogDTO.lastLogout());
        userLogEntity.setLastLogin(userLogDTO.lastLogin());
        userLogEntity.setLoginAttempts(userLogDTO.loginAttempts());
        userLogEntity.setLoginSuccess(userLogDTO.loginSuccess());
        userLogEntity.setSessionDuration(userLogDTO.sessionDuration());
        userLogEntity.setUserEntity(UserEntity.builder().userID(userLogDTO.userID()).build());
        return userLogEntity;
    }
}
