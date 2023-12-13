package com.example.aerobankapp.workbench.utilities.quartz;

import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serializable;

@Component
@Getter
public class QuartzWriter implements Serializable
{
    private QuartzDTO quartzDTO;
    private final String fileName = "quartz-config.yml";
    private final AeroLogger aeroLogger = new AeroLogger(QuartzWriter.class);
    private File quartzFile;

    @Autowired
    public QuartzWriter(@Qualifier("quartzDTO")QuartzDTO quartzDTO)
    {
        if(quartzDTO != null)
        {
            this.quartzDTO = quartzDTO;
        }
        else
        {
            throw new NullPointerException();
        }
    }


    public QuartzDTO read()
    {
        return null;
    }

    public void write()
    {

    }

    public void write(QuartzDTO quartzDTO)
    {

    }


}
