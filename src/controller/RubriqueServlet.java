package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

import database.Connect;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Rubrique;
import models.UniteOeuvre;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/RubriqueServlet")
public class RubriqueServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<UniteOeuvre> uniteOeuvres = null;

        try (Connection connection = Connect.getConnection()) {
            uniteOeuvres = UniteOeuvre.getAll(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String jsonUniteOeuvres = gson.toJson(uniteOeuvres);

        // Passer le JSON à la JSP
        response.setCharacterEncoding("UTF-8");

        request.setAttribute("uniteOeuvresJson", jsonUniteOeuvres);
        request.setAttribute("page", "rubrique");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String idUniteOeuvreStr = request.getParameter("idUniteOeuvre");
        String estVariableStr = request.getParameter("estVariable");
        String date = request.getParameter("date");
        boolean estVariable = (estVariableStr != null);

        try (Connection conn = Connect.getConnection()) {
            UniteOeuvre uo = new UniteOeuvre();
            uo.getById(conn, Integer.parseInt(idUniteOeuvreStr));

            Rubrique r = new Rubrique(nom,estVariable,uo,date);

            r.save(conn);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}