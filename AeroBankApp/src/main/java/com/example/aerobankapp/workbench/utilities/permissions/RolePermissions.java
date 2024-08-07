package com.example.aerobankapp.workbench.utilities.permissions;

import java.util.Set;

public class RolePermissions extends BasePermissions
{
    protected BasePermissions accountPermissions;
    protected BasePermissions schedulerPermissions;
    protected BasePermissions transactionPermissions;

    public RolePermissions()
    {
       initializePermissions();
    }

    void initializePermissions()
    {
        accountPermissions = new AccountPermissions();
        schedulerPermissions = new SchedulerPermissions();
        transactionPermissions = new TransactionPermissions();
    }

    public void addSchedulerPermissions(SysPermission permission)
    {
        this.schedulerPermissions.permissionSet.add(permission);
    }

    public void deleteSchedulerPermissions(SysPermission permission)
    {
        this.schedulerPermissions.permissionSet.remove(permission);
    }

    public void addTransactionPermissions(SysPermission permission)
    {
        this.transactionPermissions.permissionSet.add(permission);
    }

    public void addAccountPermissions(SysPermission permission)
    {
        this.accountPermissions.permissionSet.add(permission);
    }

    public void deleteTransactionPermission(SysPermission permission)
    {
        this.transactionPermissions.permissionSet.remove(permission);
    }

    public void deleteAccountPermission(SysPermission permission)
    {
        this.accountPermissions.permissionSet.remove(permission);
    }

}
