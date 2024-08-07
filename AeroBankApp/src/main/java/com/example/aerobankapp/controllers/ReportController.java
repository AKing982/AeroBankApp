package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.ReportDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/api/reports")
public class ReportController {

    private Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @PostMapping("/generate-pdf")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> generatePDF(@RequestBody ReportDTO reportDTO) throws IOException  {
        LOGGER.info("Report Request: " + reportDTO);

        // Prepare PDF output stream
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
//        PdfDocument pdf = new PdfDocument(writer);
//        Document document = new Document(pdf);
//
//        // Adding content to the PDF
//        document.add(new Paragraph("Report Name: " + reportDTO.reportName()));
//        document.add(new Paragraph("Start Date: " + reportDTO.startDate()));
//        document.add(new Paragraph("End Date: " + reportDTO.endDate()));
//
//        // More sophisticated PDF generation can go here
//        // Close the PDF document
//        document.close();

//        // Set headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        String filename = reportDTO.reportName() + ".pdf";
//        headers.setContentDisposition(ContentDisposition.builder("inline").filename(filename).build());
//
//        // Return the PDF in a ResponseEntity
//        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        return null;
    }

}
