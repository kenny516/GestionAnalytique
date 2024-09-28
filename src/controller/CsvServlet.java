package controller;

import database.Connect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.*;
import models.utils.Csv;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;

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
    
}
