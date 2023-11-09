package com.example.aerobankapp.workbench.utilities.quartz;

import com.example.aerobankapp.workbench.utilities.connections.BasicDataSource;
import com.example.aerobankapp.workbench.utilities.connections.BasicDataSourceImpl;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
public class QuartzDataSourceImpl implements QuartzDataSource
{
    private String dbQuartzURL;
    private String jobStore;
    private BasicDataSourceImpl dbSource;
    private DBType dbType;
    private AeroLogger aeroLogger = new AeroLogger(QuartzDataSourceImpl.class);

    public QuartzDataSourceImpl(final BasicDataSourceImpl basicDataSource, DBType dbType)
    {
        nullCheck(basicDataSource);
        this.dbSource = basicDataSource;
        this.dbType = dbType;
        this.dbQuartzURL = getQuartzURL();
        this.jobStore = getJobStore();
    }

    public QuartzDataSourceImpl(final BasicDataSourceImpl basicDataSource)
    {
        nullCheck(basicDataSource);
        this.dbSource = basicDataSource;
        this.dbType = basicDataSource.getDBType();
        this.dbQuartzURL = getQuartzURL();
        this.jobStore = getJobStore();
    }


    public boolean nullCheck(final BasicDataSource basicDataSource)
    {
        boolean isNull = false;
        if(basicDataSource == null)
        {
            isNull = true;
        }
        return isNull;
    }

    /**
     * Retrieves the Quartz URL
     * @return
     */
    @Override
    public String getQuartzURL()
    {
        StringBuilder quartzURL = null;
        switch(dbType)
        {
            case POSTGRESQL:
            case MYSQL:
                quartzURL = new StringBuilder();
                quartzURL.append(dbSource.getDBProtocol());
                quartzURL.append("://");
                quartzURL.append(dbSource.getDBServer());
                quartzURL.append(":");
                quartzURL.append(dbSource.getDBPort());
                quartzURL.append("/");
                quartzURL.append(dbSource.getDBName());
                dbQuartzURL = quartzURL.toString();
                break;
            case SQLSERVER:
                StringBuilder quartSSQL = new StringBuilder();
                quartSSQL.append(dbSource.getDBProtocol());
                quartSSQL.append("://");
                quartSSQL.append(dbSource.getDBServer());
                quartSSQL.append(":");
                quartSSQL.append(dbSource.getDBPort());
                quartSSQL.append(";databaseName=");
                quartSSQL.append(dbSource.getDBName());
                quartSSQL.append(";");
                quartSSQL.append("integratedSecurity=false;");
                quartSSQL.append("encrypt=true;");
                quartSSQL.append("trustServerCertificate=true");
                dbQuartzURL = quartSSQL.toString();
                break;
        }
        return dbQuartzURL;
    }

    @Override
    public String getJobStore()
    {
        String jobStoreLocation = "";
        switch(dbType)
        {
            case MYSQL:
            case SQLSERVER:
                jobStore = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";
                break;
            case POSTGRESQL:
                jobStore = "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate";
                break;
        }
        return jobStore;
    }
}
