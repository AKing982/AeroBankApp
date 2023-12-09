package com.example.aerobankapp.rbac;

import java.util.Set;

public interface SystemRole
{
    Set<SystemRole> getSystemPermissions();
}
