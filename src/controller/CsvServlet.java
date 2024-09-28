package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.utils.Csv;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/Csv")
public class CsvServlet extends HttpServlet {

    // get the csv of a table
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<?> data = Collections.singletonList(req.getParameter("data"));
        String fileName = req.getParameter("fileName");
        
        Csv.exportToCSV(data,fileName+".csv");
    }

    // add the csv in table

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }
}
