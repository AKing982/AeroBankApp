package com.example.aerobankapp.workbench.utilities.permissions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class BasePermissions
{
    protected Set<SysPermission> permissionSet;

    public BasePermissions()
    {
        permissionSet = new HashSet<>();
    }

    public void addPermissions(SysPermission permission)
    {
        this.permissionSet.add(permission);
    }

    public void deletePermission(SysPermission permission)
    {
        this.permissionSet.remove(permission);
    }

    public Set<SysPermission> getPermissions()
    {
        return Collections.unmodifiableSet(this.permissionSet);
    }

}
