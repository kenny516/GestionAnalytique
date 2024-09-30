package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Connect;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Depenses;
import models.Rubrique;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/DepenseServlet")
public class DepenseServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Rubrique> rubriques = null;

        try (Connection connection = Connect.getConnection()) {
            rubriques = Rubrique.getAll(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Passer la liste des unités d'œuvre à la vue
        request.setAttribute("Rubriques", rubriques);
        request.setAttribute("page", "depenses");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        String idRubrique = request.getParameter("idRubrique");
        String date = request.getParameter("dateDepense");
        String montant = request.getParameter("montant");

        Rubrique rubrique = new Rubrique();

        try (Connection conn = Connect.getConnection()) {
            rubrique.getById(conn, Integer.parseInt(idRubrique));
            Depenses d = new Depenses(date,montant,rubrique);
            d.save(conn);
            request.setAttribute("page", "parts");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

}