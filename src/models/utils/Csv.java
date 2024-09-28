package models.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Csv {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Csv() {
    }

    public static void exportToCSV(List<?> data, String fileName) throws IOException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty or null");
        }else{
            try (FileWriter writer = new FileWriter(fileName)) {
                Class<?> clazz = data.getFirst().getClass();
                Field[] fields = clazz.getDeclaredFields();

                // Write header
                for (int i = 0; i < fields.length; i++) {
                    writer.append(fields[i].getName());
                    if (i < fields.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");

                // Write data
                for (Object obj : data) {
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        Object value = fields[i].get(obj);
                        writer.append(formatValue(value));
                        if (i < fields.length - 1) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }
            } catch (IllegalAccessException e) {
                throw new IOException("Error accessing object fields", e);
            }
        }
    }

    public static <T> List<T> importFromCSV(String fileName, Class<T> clazz) throws IOException {
        List<T> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String header = reader.readLine();
            String[] fieldNames = header.split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                T obj = clazz.getDeclaredConstructor().newInstance();

                for (int i = 0; i < fieldNames.length; i++) {
                    Field field = clazz.getDeclaredField(fieldNames[i]);
                    field.setAccessible(true);
                    setFieldValue(obj, field, values[i]);
                }

                result.add(obj);
            }
        } catch (Exception e) {
            throw new IOException("Error reading CSV file", e);
        }

        return result;
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Date) {
            return DATE_FORMAT.format((Date) value);
        } else {
            return value.toString();
        }
    }

    private static void setFieldValue(Object obj, Field field, String value) throws Exception {
        Class<?> type = field.getType();
        if (type == String.class) {
            field.set(obj, value);
        } else if (type == int.class || type == Integer.class) {
            field.set(obj, Integer.parseInt(value));
        } else if (type == double.class || type == Double.class) {
            field.set(obj, Double.parseDouble(value));
        } else if (type == boolean.class || type == Boolean.class) {
            field.set(obj, Boolean.parseBoolean(value));
        } else if (type == Date.class) {
            field.set(obj, DATE_FORMAT.parse(value));
        } else if (type == BigDecimal.class) {
            field.set(obj, new BigDecimal(value));
        } else {
            throw new UnsupportedOperationException("Unsupported field type: " + type);
        }
    }
}