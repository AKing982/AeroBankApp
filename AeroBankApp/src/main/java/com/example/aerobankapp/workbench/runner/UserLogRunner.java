package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.model.LoginModel;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Getter
public class UserLogRunner implements Runnable {
    private final UserLogServiceImpl userLogService;
    private final UserServiceImpl userService;
    private final LoginModel loginModel;
    private AeroLogger aeroLogger = new AeroLogger(UserLogRunner.class);

    @Autowired
    public UserLogRunner(UserLogServiceImpl userLogService, UserServiceImpl userSvc, LoginModel loginModel) {
        this.userLogService = userLogService;
        this.userService = userSvc;
        this.loginModel = loginModel;
    }

    private String getUserName() {
        return getLoginModel().getUsername();
    }

    private LocalDateTime getDate() {
        return null;
    }

    public int getCurrentUserID()
    {
        int userID = 0;
        List<UserEntity> users = getUserService().findByUserName(getUserName());
        aeroLogger.info("Users size: " + users.size());

        if (!users.isEmpty())
        {
            for (UserEntity user : users)
            {
                aeroLogger.info("User: " + user.toString());
                if (user.getUsername().equals(getUserName()))
                {
                    userID = user.getUserID();
                    break;
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("No User match in UserLog");
        }
        return userID;
    }


    @Override
    @Async
    public void run()
    {
        try
        {
            addUserLogToDatabase();
            Thread.sleep(5000);

        }catch(Exception e)
        {
            aeroLogger.error("Unable to store UserLog to database: ", e);
        }
    }

    public void addUserLogToDatabase()
    {
        UserLogEntity userLog1 = createUserLog(getUserName(), getCurrentUserID(), getDate());
        storeUserLog(userLog1);
    }

    public UserLogEntity createUserLog(final String username, final int userID, final LocalDateTime date)
    {
        return UserLogEntity.builder()
                .userID(userID)
                .username(username)
                .lastLogin(date)
                .id(1)
                .build();
    }

    public void storeUserLog(UserLogEntity userLog)
    {
        if(userLog != null)
        {
            getUserLogService().save(userLog);
        }
    }
}
