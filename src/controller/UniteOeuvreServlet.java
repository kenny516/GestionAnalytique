package controller;

import java.io.IOException;
import java.sql.Connection;

import database.Connect;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.UniteOeuvre;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/UniteOeuvreServlet")
public class UniteOeuvreServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        request.setAttribute("page", "uoeuvre");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        String nom = request.getParameter("nom");

        UniteOeuvre uniteOeuvre = new UniteOeuvre();
        uniteOeuvre.setNom(nom);

        try (Connection conn = Connect.getConnection()) {
            uniteOeuvre.save(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}