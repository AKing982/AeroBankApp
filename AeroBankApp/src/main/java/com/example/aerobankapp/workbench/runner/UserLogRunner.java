package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.services.UserLogServiceImpl;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.model.LoginModel;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    private Date getDate() {
        return new Date();
    }

    public int getCurrentUserID()
    {
        int userID = 0;
        List<Users> users = getUserService().findByUserName(getUserName());
        aeroLogger.info("Users size: " + users.size());

        if (!users.isEmpty())
        {
            for (Users user : users)
            {
                aeroLogger.info("User: " + user.toString());
                if (user.getUsername().equals(getUserName()))
                {
                    userID = user.getId();
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
        UserLog userLog1 = createUserLog(getUserName(), getCurrentUserID(), getDate());
        storeUserLog(userLog1);
    }

    public UserLog createUserLog(final String username, final int userID, final Date date)
    {
        return UserLog.builder()
                .userID(userID)
                .username(username)
                .lastLogin(date)
                .id(1)
                .build();
    }

    public void storeUserLog(UserLog userLog)
    {
        if(userLog != null)
        {
            getUserLogService().save(userLog);
        }
    }
}
