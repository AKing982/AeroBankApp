package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.ExportType;
import com.example.aerobankapp.workbench.utilities.ReportCategory;

import java.time.LocalDate;

public record ReportDTO(String reportName,
                        LocalDate startDate,
                        LocalDate endDate,
                        String reportType,

                        String exportType) {
}
