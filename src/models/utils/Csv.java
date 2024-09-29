package models.utils;

import database.Connect;
import models.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Csv {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    public Csv() {
    }

    public static <T> void exportToCSV(List<T> data, OutputStream outputStream) throws IOException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("La liste de données est vide ou null");
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            Class<?> clazz = data.get(0).getClass(); // Fix for first element access
            Field[] fields = clazz.getDeclaredFields();

            // Write the CSV header
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true); // Ensure access to private fields
                writer.append(fields[i].getName());
                if (i < fields.length - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            // Write the data rows
            for (T obj : data) {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(obj); // Get the field value

                    // For custom object types, we only export the `id` field (assuming it exists)
                    switch (value) {
                        case UniteOeuvre uniteOeuvre -> writer.append(String.valueOf(uniteOeuvre.getId()));
                        case Centre centre -> writer.append(String.valueOf(centre.getId()));
                        case Depenses depenses -> writer.append(String.valueOf(depenses.getId()));
                        case NatureCentre natureCentre -> writer.append(String.valueOf(natureCentre.getId()));
                        case PartsParCentre partsParCentre -> writer.append(String.valueOf(partsParCentre.getId()));
                        case Produit produit -> writer.append(String.valueOf(produit.getId()));
                        case Production production -> writer.append(String.valueOf(production.getId()));
                        case Rubrique rubrique -> writer.append(String.valueOf(rubrique.getId()));
                        case null, default -> writer.append(formatValue(value));
                    }

                    if (i < fields.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
        } catch (IllegalAccessException e) {
            throw new IOException("Erreur lors de l'accès aux champs de l'objet", e);
        }
    }

    public static <T> ImportResult<T> importFromCSV(String fileName, Class<T> clazz) throws IOException {
        List<T> successfulImports = new ArrayList<>();
        List<ImportError> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("Le fichier CSV est vide");
            }
            String[] fieldNames = header.split(",");

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] values = line.split(",");
                    T obj = clazz.getDeclaredConstructor().newInstance();

                    for (int i = 0; i < fieldNames.length; i++) {
                        Field field = clazz.getDeclaredField(fieldNames[i]);
                        field.setAccessible(true);

                        if (field.getType() == UniteOeuvre.class) {
                            UniteOeuvre uniteOeuvre = new UniteOeuvre();
                            uniteOeuvre.setId(Integer.parseInt(values[i]));
                            field.set(obj, uniteOeuvre);
                        } else if (field.getType() == Centre.class) {
                            Centre centre = new Centre();
                            centre.setId(Integer.parseInt(values[i]));
                            field.set(obj, centre);
                        } else if (field.getType() == Depenses.class) {
                            Depenses depenses = new Depenses();
                            depenses.setId(Integer.parseInt(values[i]));
                            field.set(obj, depenses);
                        } else if (field.getType() == NatureCentre.class) {
                            NatureCentre natureCentre = new NatureCentre();
                            natureCentre.setId(Integer.parseInt(values[i]));
                            field.set(obj, natureCentre);
                        } else if (field.getType() == PartsParCentre.class) {
                            PartsParCentre partsParCentre = new PartsParCentre();
                            partsParCentre.setId(Integer.parseInt(values[i]));
                            field.set(obj, partsParCentre);
                        } else if (field.getType() == Produit.class) {
                            Produit produit = new Produit();
                            produit.setId(Integer.parseInt(values[i]));
                            field.set(obj, produit);
                        } else if (field.getType() == Production.class) {
                            Production production = new Production();
                            production.setId(Integer.parseInt(values[i]));
                            field.set(obj, production);
                        } else if (field.getType() == Rubrique.class) {
                            Rubrique rubrique = new Rubrique();
                            rubrique.setId(Integer.parseInt(values[i]));
                            field.set(obj, rubrique);
                        } else {
                            // Handle regular types (e.g., String, int, Date, etc.)
                            setFieldValue(obj, field, values[i]);
                        }
                    }
                    invokeSaveMethod(obj);
                    successfulImports.add(obj);
                    // call the methode save of obj

                } catch (Exception e) {
                    errors.add(new ImportError(lineNumber, line, e.getMessage()));
                }
            }
        } catch (IOException e) {
            throw new IOException("Erreur lors de la lecture du fichier CSV", e);
        }

        return new ImportResult<>(successfulImports, errors);
    }
    private static <T> void invokeSaveMethod(T obj) throws Exception {
        try {
            Connection connection = Connect.getConnection();
            Method saveMethod = obj.getClass().getMethod("save", Connection.class);
            saveMethod.invoke(obj, connection);
        } catch (NoSuchMethodException e) {
            throw new Exception("La méthode 'save' est introuvable pour l'objet: " + obj.getClass().getName());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new Exception("Erreur lors de l'invocation de la méthode 'save' pour l'objet: " + obj.getClass().getName(), e);
        }
    }

    private static void setFieldValue(Object obj, Field field, String value) throws Exception {
        Class<?> type = field.getType();
        if (value == null || value.isEmpty()) {
            if (type.isPrimitive()) {
                throw new IllegalArgumentException("Les types primitifs ne peuvent pas être null");
            }
            field.set(obj, null);
            return;
        }

        try {
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
                throw new UnsupportedOperationException("Type de champ non pris en charge : " + type);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de la conversion de la valeur '" + value + "' pour le champ " + field.getName(), e);
        }
    }

    public static class ImportResult<T> {
        private final List<T> successfulImports;
        private final List<ImportError> errors;

        public ImportResult(List<T> successfulImports, List<ImportError> errors) {
            this.successfulImports = successfulImports;
            this.errors = errors;
        }

        public List<T> getSuccessfulImports() {
            return successfulImports;
        }

        public List<ImportError> getErrors() {
            return errors;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }

    public static class ImportError {
        private final int lineNumber;
        private final String line;
        private final String errorMessage;

        public ImportError(int lineNumber, String line, String errorMessage) {
            this.lineNumber = lineNumber;
            this.line = line;
            this.errorMessage = errorMessage;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public String getLine() {
            return line;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public String toString() {
            return "Erreur à la ligne " + lineNumber + ": " + errorMessage + " (ligne: " + line + ")";
        }
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof AssoDepensesParts) {
            return String.valueOf((((AssoDepensesParts) value).getId()));
        } else if (value instanceof Centre) {
            return String.valueOf((((Centre) value).getId()));
        } else if (value instanceof Depenses) {
            return String.valueOf((((Depenses) value).getId()));
        } else if (value instanceof NatureCentre) {
            return String.valueOf((((NatureCentre) value).getId()));
        } else if (value instanceof PartsParCentre) {
            return String.valueOf((((PartsParCentre) value).getId()));
        } else if (value instanceof Produit) {
            return String.valueOf((((Produit) value).getId()));
        } else if (value instanceof Production) {
            return String.valueOf((((Production) value).getId()));
        } else if (value instanceof Rubrique) {
            return String.valueOf((((Rubrique) value).getId()));
        } else if (value instanceof UniteOeuvre) {
            return String.valueOf((((UniteOeuvre) value).getId()));
        } else if (value instanceof Date) {
            return DATE_FORMAT.format((Date) value);
        } else {
            return value.toString();
        }
    }
}