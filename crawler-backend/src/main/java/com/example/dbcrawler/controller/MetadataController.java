package com.example.dbcrawler.controller;

import com.example.dbcrawler.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    // Get all tables and their metadata
    @GetMapping("/tables")
    public Object getTablesMetadata() {
        try {
            return metadataService.getDatabaseMetadata();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching metadata";
        }
    }

    // Generate and return model class (for a single table)
    @GetMapping("/models")
    public Object getGeneratedModels() {
        try {
            return metadataService.generateModelClass("your_table_name_here");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating models";
        }
    }

    // Export model as a ZIP file for a specific table
    @GetMapping("/export/model/{tableName}")
    public ResponseEntity<byte[]> exportModelZip(@PathVariable String tableName) {
        try {
            byte[] zipBytes = metadataService.generateModelZipForTable(tableName);

            // Set headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(tableName + ".zip").build());

            // Return the zip file as response
            return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(("Failed to export model for " + tableName).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
