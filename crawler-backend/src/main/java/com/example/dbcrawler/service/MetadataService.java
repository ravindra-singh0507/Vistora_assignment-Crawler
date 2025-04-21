package com.example.dbcrawler.service;

import com.example.dbcrawler.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class MetadataService {

    @Autowired
    private AppConfig config;

    // Fetch database metadata
    public List<Map<String, Object>> getDatabaseMetadata() {
        List<Map<String, Object>> tables = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(config.getDbUrl(), config.getUsername(), config.getPassword())) {

            String schema = conn.getCatalog();
            System.out.println(">> Connecting to schema: " + schema);

            DatabaseMetaData meta = conn.getMetaData();

            ResultSet rsTables = meta.getTables(schema, null, "%", new String[]{"TABLE"});
            while (rsTables.next()) {
                Map<String, Object> tableInfo = new HashMap<>();
                String tableName = rsTables.getString("TABLE_NAME");
                tableInfo.put("table", tableName);

                List<Map<String, Object>> columns = new ArrayList<>();
                ResultSet rsColumns = meta.getColumns(schema, null, tableName, "%");
                while (rsColumns.next()) {
                    Map<String, Object> column = new HashMap<>();
                    column.put("name", rsColumns.getString("COLUMN_NAME"));
                    column.put("type", rsColumns.getString("TYPE_NAME"));
                    column.put("size", rsColumns.getInt("COLUMN_SIZE"));
                    column.put("nullable", rsColumns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    columns.add(column);
                }
                tableInfo.put("columns", columns);

                // Fetch data from table
                List<Map<String, Object>> rows = new ArrayList<>();
                Statement stmt = conn.createStatement();
                ResultSet rsData = stmt.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData rsMetaData = rsData.getMetaData();
                int columnCount = rsMetaData.getColumnCount();

                while (rsData.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(rsMetaData.getColumnName(i), rsData.getObject(i));
                    }
                    rows.add(row);
                }
                tableInfo.put("data", rows);

                tables.add(tableInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tables;
    }

    // Generate model class for the given table and zip it
    public byte[] generateModelZipForTable(String tableName) throws IOException {
        String className = Character.toUpperCase(tableName.charAt(0)) + tableName.substring(1);
        String modelContent = generateModelClass(tableName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry entry = new ZipEntry(className + ".java");
            zos.putNextEntry(entry);
            zos.write(modelContent.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }
        return baos.toByteArray();
    }

    // Generate the model class for the given table
    public String generateModelClass(String tableName) {
        StringBuilder sb = new StringBuilder();
        String className = Character.toUpperCase(tableName.charAt(0)) + tableName.substring(1);

        sb.append("public class ").append(className).append(" {\n");

        try (Connection conn = DriverManager.getConnection(config.getDbUrl(), config.getUsername(), config.getPassword())) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet columns = meta.getColumns(conn.getCatalog(), null, tableName, "%");

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String typeName = columns.getString("TYPE_NAME");
                String javaType = sqlTypeToJavaType(typeName);

                sb.append("    private ").append(javaType).append(" ").append(columnName).append(";\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sb.append("}");
        return sb.toString();
    }
    

    // Convert SQL type to Java type
    private String sqlTypeToJavaType(String sqlType) {
        return switch (sqlType.toUpperCase()) {
            case "INT", "INTEGER", "SMALLINT" -> "int";
            case "BIGINT" -> "long";
            case "FLOAT", "REAL", "DOUBLE" -> "double";
            case "DECIMAL", "NUMERIC" -> "java.math.BigDecimal";
            case "DATE" -> "java.sql.Date";
            case "TIMESTAMP", "DATETIME" -> "java.sql.Timestamp";
            case "BOOLEAN", "BIT" -> "boolean";
            case "CHAR", "VARCHAR", "TEXT", "LONGTEXT" -> "String";
            default -> "String";
        };
    }
}
