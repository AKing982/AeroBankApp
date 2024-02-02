package com.example.aerobankapp.workbench.utilities.db;

public enum DBType
{
    MYSQL("MySQL"), SSQL("SQL Server"),
    PSQL("PostgreSQL"),
    NONE("NONE");

    private String code;

    DBType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public static DBType getEnum(String code)
    {
        switch(code)
        {
            case "MySQL":
                return MYSQL;
            case "SQL Server":
                return SSQL;
            case "PostgreSQL":
                return PSQL;
            default:
                return null;
        }
    }
}
