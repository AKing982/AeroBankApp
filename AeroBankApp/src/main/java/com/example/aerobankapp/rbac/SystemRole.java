package com.example.aerobankapp.rbac;

import com.example.aerobankapp.workbench.utilities.UserType;

import java.util.Map;
import java.util.Set;

public interface SystemRole
{
    Set<SystemPermission> getSystemPermissions(int userID, UserType userType);
    Set<SystemRoleControl> getSystemAccess(Map<Integer, Set<SystemPermission>> userPermissions);
}
