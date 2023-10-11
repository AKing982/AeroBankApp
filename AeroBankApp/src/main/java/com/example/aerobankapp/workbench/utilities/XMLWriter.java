package com.example.aerobankapp.workbench.utilities;

public class XMLWriter
{
    private final String fileName = "db-config.xml";
    private String path;

    public XMLWriter(String path)
    {
        if(path.equals(" "))
        {

        }
    }

    public String getXMLPath()
    {
        return ":";
    }
}
