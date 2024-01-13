package com.example.aerobankapp.workbench.utilities.permissions.rolePermissions;

import com.example.aerobankapp.workbench.utilities.permissions.AccountPermissions;
import com.example.aerobankapp.workbench.utilities.permissions.SchedulerPermissions;
import com.example.aerobankapp.workbench.utilities.permissions.SysPermission;
import com.example.aerobankapp.workbench.utilities.permissions.TransactionPermissions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Component
public class AdminPermissions
{
    private Set<SysPermission> permissionsSet;

    public AdminPermissions()
    {
        permissionsSet = new HashSet<>();
        initializePermissions();
    }

    void initializePermissions()
    {
        permissionsSet.addAll(new SchedulerPermissions().getPermissions());
        permissionsSet.addAll(new AccountPermissions().getPermissions());
        permissionsSet.addAll(new TransactionPermissions().getPermissions());
    }
}
