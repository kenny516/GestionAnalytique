package controller;

import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import database.Connect;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.AdministrationAnalytique;
import models.Centre;

@WebServlet("/analytics")
public class AnalyticServlet extends HttpServlet {
    Date defaultStartDate;
    Date defaultEndDate;

    @Override
    public void init() throws ServletException {
        LocalDate today = LocalDate.now();

        if (today.getDayOfMonth() < 7) {
            // start of PREVIOUS month till today if we are still in the first week
            defaultStartDate = Date.valueOf(LocalDate.of(today.getYear(), today.getMonthValue(), 1).minusMonths(1L));
        } else {
            // start of the CURRENT month till today otherwise
            defaultStartDate = Date.valueOf(LocalDate.of(today.getYear(), today.getMonthValue(), 1));
        }

        defaultEndDate = Date.valueOf(today);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("pages/tableau.jsp");

        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        Date startDate = null;
        Date endDate = null;

        if (startDateStr == null || startDateStr.trim().isEmpty()) {
            startDate = this.defaultStartDate;
        } else {
            startDate = Date.valueOf(LocalDate.parse(startDateStr));
        }

        if (endDateStr == null || endDateStr.trim().isEmpty()) {
            endDate = this.defaultEndDate;
        } else {
            endDate = Date.valueOf(LocalDate.parse(endDateStr));;
        }

        Connection c = null;
        try {
            c = Connect.getConnection();

            AdministrationAnalytique aa = new AdministrationAnalytique(startDate, endDate);
            req.setAttribute("adminAnalytics", aa); // sending the date and rubriques
            req.setAttribute("partsAndTotal", aa.getPartCentreAllRubrique(c)); // sending the individual totals

            HashMap<Centre, Double> parCentre = aa.getTotalDepenseParCentre(c);
            Set<Centre> centres = parCentre.keySet();

            req.setAttribute("centres", new ArrayList<Centre>(centres)); // sending all the centres

            // sending the totals (to avoid calculations in frontend)
            req.setAttribute("totalParCentre", parCentre); 
            req.setAttribute("totalFixe", aa.getTotalDepenseInvariable(c));
            req.setAttribute("totalVariable", aa.getTotalDepenseVariable(c));
            req.setAttribute("overallTotal", aa.getTotalDepense(c));
            req.setAttribute("totalParRubrique", aa.getTotalParRubrique(c));

            c.close();
        } catch (Exception e) {
            throw new ServletException("Problème analytique", e);
        }

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    
}
