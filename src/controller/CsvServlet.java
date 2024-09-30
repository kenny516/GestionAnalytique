package controller;

import database.Connect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import models.*;
import models.utils.Csv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;

// usage of export csv just use get with parameter type="produit" or type="production" or type="rubrique" or type="partsparcentre" or ....


@WebServlet("/Csv")
public class CsvServlet extends HttpServlet {

    // get the csv of a table
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String fileName = type + ".csv";

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream out = response.getOutputStream()) {
            List<?> data = getData(type);
            Csv.exportToCSV(data, out);
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'exportation CSV", e);
        }

    }

    private List<?> getData(String type) throws Exception {
        Connection connection = Connect.getConnection();
        return switch (type.toLowerCase()) {
            case "production" -> new Production().getAll(connection);
            case "produit" -> new Produit().getAll(connection);
            case "rubrique" -> new Rubrique().getAll(connection);
            case "partsparcentre" -> new PartsParCentre().getAll(connection);
            case "depenses" -> Depenses.getAll(connection);
            case "assodepensesparts" -> AssoDepensesParts.getAll(connection);
            case "centre" -> Centre.getAll(connection);
            case "naturecentre" -> NatureCentre.getAll(connection);
            case "uniteoeuvre" -> UniteOeuvre.getAll(connection);
            default -> throw new IllegalArgumentException("Type de données non reconnu : " + type);
        };
    }
    private Object getObject(String type){
        return switch (type.toLowerCase()) {
            case "production" -> new Production();
            case "produit" -> new Produit();
            case "rubrique" -> new Rubrique();
            case "partsparcentre" -> new PartsParCentre();
            case "depenses" -> new Depenses();
            case "assodepensesparts" -> new AssoDepensesParts();
            case "centre" -> new Centre();
            case "naturecentre" -> new NatureCentre();
            case "uniteoeuvre" -> new UniteOeuvre();
            default -> throw new IllegalArgumentException("Type de données non reconnu : " + type);
        };
    }

    // i need to implement method for import csv use the class Csv.java
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String type = request.getParameter("type");
    Part filePart = request.getPart("file"); // Récupère le fichier uploadé
    String fileName = "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\GestionAnalytique\\public\\csv\\" + type + ".csv";

    try (InputStream fileContent = filePart.getInputStream();
         FileOutputStream fos = new FileOutputStream(fileName)) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileContent.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
    }

    try {
        Csv.ImportResult<?> importResult = Csv.importFromCSV(fileName, getObject(type).getClass());
        Connection connection = Connect.getConnection();
        for (Object obj : importResult.getSuccessfulImports()) {
            response.getWriter().println("Importation réussie : " + obj);
        }
        for (Csv.ImportError error : importResult.getErrors()) {
            response.getWriter().println("Erreur à la ligne " + error.getLineNumber() + " : " + error.getErrorMessage());
        }
    } catch (Exception e) {
        throw new ServletException("Erreur lors de l'importation CSV", e);
    }
}
    
}
