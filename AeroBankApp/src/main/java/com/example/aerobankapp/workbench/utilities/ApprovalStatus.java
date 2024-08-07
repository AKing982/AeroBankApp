package com.example.aerobankapp.workbench.utilities;

public enum ApprovalStatus
{
    LOAN_APPROVED("Loan Approved"),
    DEPOSIT_APPROVAL("Deposit Approval"),
    WITHDRAW_APPROVAL("Withdraw_Approval");
    private String status;

    ApprovalStatus(String code)
    {
        this.status = code;
    }
}
