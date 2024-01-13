package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.exceptions.IllegalRoleException;
import com.example.aerobankapp.services.UserDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{

    private UserDAOImpl userDAO;

    @Autowired
    public RoleService(UserDAOImpl userDAO)
    {
        this.userDAO = userDAO;
    }

    public String getDescription(Role role)
    {
        if(role == null)
        {
            throw new IllegalRoleException("Invalid Role!");
        }
        return role.getDescription();
    }

    public Role getRoleByUserName(String user)
    {
        if(user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException("User cannot be null or empty");
        }
        return userDAO.getUserRole(user);
    }

    public Role getRoleByUserType(UserType userType)
    {
        if(userType == null)
        {
            throw new IllegalArgumentException("Invalid User Role");
        }
        String userRole = userType.name();
        for (Role role : Role.values()) {
            String roleType = role.getDescription();
            if (roleType.endsWith(userRole))
            {
                return role;
            }
        }
        throw new IllegalRoleException("Role could not be found");
    }

    public Role getRoleByDescription(String description)
    {
        for(Role role : Role.values())
        {
            if(role.getDescription().equals(description))
            {
                return role;
            }
        }
        throw new IllegalRoleException("Role could not be found");
    }
}
