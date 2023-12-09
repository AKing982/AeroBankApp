package com.example.aerobankapp.rbac;

import com.example.aerobankapp.workbench.utilities.UserType;

import java.util.Map;
import java.util.Set;

public class SettingsRoleControl extends SystemRoleControl
{


    public SettingsRoleControl(UserType userRole, Set<SystemPermission> userPermissions)
    {
        super(userRole, userPermissions);
    }

    @Override
    public Set<SystemPermission> getSystemPermissions(int userID, UserType userType) {
        return null;
    }

    @Override
    public Set<SystemRoleControl> getSystemAccess(Map<Integer, Set<SystemPermission>> userPermissions) {
        return null;
    }

    @Override
    public boolean isAllowDelete() {
        return false;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isAllowAccess() {
        return false;
    }

    @Override
    public boolean isAllowCreate() {
        return false;
    }

    @Override
    public boolean isAllowUpdate() {
        return false;
    }

    @Override
    public void disable() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void create() {

    }
}
