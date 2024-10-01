package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import java.sql.SQLException;
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
            PrintWriter out = response.getWriter();

        String idRubrique = request.getParameter("idRubrique");
        String date = request.getParameter("dateDepense");
        String montant = request.getParameter("montant");

        Rubrique rubrique = new Rubrique();

        try (Connection conn = Connect.getConnection()) {
            rubrique.getById(conn, Integer.parseInt(idRubrique));
            // out.println(rubrique.getId());
            rubrique.setpCentre(conn);
            Depenses d = new Depenses(date,montant,rubrique);
            d.save(conn, rubrique.getpCentre());
            
            response.sendRedirect(request.getContextPath() + "/DepenseServlet");

        } catch (Exception e) {
            out.println(e);
            out.flush();
        }
    }

}