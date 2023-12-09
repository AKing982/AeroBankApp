package com.example.aerobankapp.rbac;

import com.example.aerobankapp.workbench.utilities.BankAuthorization;
import com.example.aerobankapp.workbench.utilities.UserType;

import java.util.Set;

public abstract class SystemRoleControl implements SystemRole
{
    protected boolean allowAccess;
    protected boolean allowCreate;
    protected boolean allowModify;
    protected boolean allowDelete;
    private UserType userType;
    private BankAuthorization bankAuthorization;
    protected boolean isDisabled;
    protected boolean isHidden;
    private String systemRoleName;

    protected Set<SystemRole> permissions;

}
