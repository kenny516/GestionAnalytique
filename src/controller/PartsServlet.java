package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import database.Connect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Centre;
import models.PartsParCentre;
import models.Rubrique;

@WebServlet("/PartsServlet")
public class PartsServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Rubrique> rubriques = null;
        List<Centre> centres = null;
        Rubrique rselect = (Rubrique) request.getAttribute("Rubrique");

        try (Connection connection = Connect.getConnection()) {
            rubriques = Rubrique.getAll(connection);
            centres = Centre.getAll(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Passer la liste des unités d'œuvre à la vue
        request.setAttribute("Rubriques", rubriques);
        request.setAttribute("Centres", centres);
        request.setAttribute("RubriqueSelect", rselect);
        request.setAttribute("page", "parts");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        String idRubrique = request.getParameter("idRubrique");
        String date = request.getParameter("dateInsertion");

        Rubrique rubrique = new Rubrique();

        try (Connection conn = Connect.getConnection()) {
            rubrique.getById(conn, Integer.parseInt(idRubrique));
            List<Centre> lc = Centre.getAll(conn);
            for (Centre centre : lc) {
                String valeur = request.getParameter(centre.getNom());
                BigDecimal v = new BigDecimal(valeur);
                PartsParCentre PartCentre = new PartsParCentre(rubrique,centre,v,date);
                PartCentre.save(conn);
            }
            response.sendRedirect(request.getContextPath() + "/PartsServlet");
        } catch (Exception e) {
            PrintWriter out = response.getWriter();
            out.println(e);
            out.flush();
        }
    }
}
