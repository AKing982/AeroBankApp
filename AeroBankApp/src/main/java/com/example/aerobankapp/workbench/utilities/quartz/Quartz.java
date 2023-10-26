package com.example.aerobankapp.workbench.utilities.quartz;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@AllArgsConstructor
@Component
public class Quartz implements Serializable, QuartzDataModel
{
    private static final long serialVersionUID = 1L;
    private String dataSourceURL;
    private String dataSourceDriver;
    private String dataSourceUser;
    private String dataSourcePassword;
    private String dataSourceClass;
    private String dataSourceDelegateClass;
    private String dataSourceName;
    private String dataSourceTablePrefix;
    private String dataSourceInstanceID;
    private String dataSourceInstanceName;
    private int threadPoolCount;
}
