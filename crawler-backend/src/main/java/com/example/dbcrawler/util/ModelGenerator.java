package com.example.dbcrawler.util;

import java.util.*;

public class ModelGenerator {

    public static Map<String, String> generateModels(List<Map<String, Object>> tables) {
        Map<String, String> classMap = new HashMap<>();
        for (Map<String, Object> table : tables) {
            String className = capitalize((String) table.get("table"));
            List<Map<String, Object>> columns = (List<Map<String, Object>>) table.get("columns");
            StringBuilder sb = new StringBuilder();
            sb.append("public class ").append(className).append(" {\n");
            for (Map<String, Object> column : columns) {
                String type = mapToJavaType((String) column.get("type"));
                sb.append("    private ").append(type).append(" ")
                        .append(column.get("name")).append(";\n");
            }
            sb.append("}\n");
            classMap.put(className, sb.toString());
        }
        return classMap;
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private static String mapToJavaType(String sqlType) {
        return switch (sqlType.toLowerCase()) {
            case "varchar", "text" -> "String";
            case "int", "integer" -> "int";
            case "bigint" -> "long";
            case "datetime", "timestamp" -> "java.util.Date";
            case "boolean" -> "boolean";
            case "double", "float" -> "double";
            default -> "String";
        };
    }
}