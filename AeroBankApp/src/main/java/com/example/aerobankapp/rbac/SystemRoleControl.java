package com.example.aerobankapp.rbac;

import com.example.aerobankapp.workbench.utilities.UserType;

import java.util.Map;
import java.util.Set;

public abstract class SystemRoleControl implements SystemRole
{
    protected boolean isAllowAccess;
    protected boolean isAllowCreate;
    protected boolean isAllowUpdate;
    protected boolean isAllowDelete;
    private UserType userType;
    protected boolean isDisabled;
    protected boolean isHidden;
    private String systemRoleName;

    protected Set<SystemPermission> permissions;

    public SystemRoleControl(UserType userRole, Set<SystemPermission> userPermissions)
    {
        this.userType = userRole;
        this.permissions = userPermissions;
    }

    public abstract boolean isAllowDelete();
    public abstract boolean isDisabled();
    public abstract boolean isHidden();
    public abstract boolean isAllowAccess();
    public abstract boolean isAllowCreate();
    public abstract boolean isAllowUpdate();


    public abstract void disable();
    public abstract void update();
    public abstract void delete();
    public abstract void hide();
    public abstract void create();

}
