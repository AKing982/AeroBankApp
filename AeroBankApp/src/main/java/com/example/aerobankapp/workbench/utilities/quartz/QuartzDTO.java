package com.example.aerobankapp.workbench.utilities.quartz;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class QuartzDTO
{
    private String dbName;
    private String jobStore;
    private String url;
    private String driver;
    private String user;
    private String password;
    private DBType dbType;
    private Date dateModified;
}
