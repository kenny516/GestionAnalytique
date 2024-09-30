<%@ page import="java.time.LocalDate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.Rubrique" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.*" %>


<% List<Rubrique> rubriques = (List<Rubrique>) request.getAttribute("Rubriques"); %>

<form action="DepenseServlet" class="col-6 py-3 px-4 card" method="post">
    <h3 class="text-uppercase card-title">Depense</h3>
    <div class="card-body">
        <div class="mb-3">
            <label class="form-label">Rubrique</label>
            <select class="form-select" name="idRubrique" id="rubrique-select">
                <%
                    for (Rubrique ru : rubriques){
                %>
                <option value="<%= ru.getId() %>"> <%= ru.getNom() %> </option>
                <%
                    }
                %>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Date</label>
            <input type="date" name="dateDepense" class="form-control" value="<%= LocalDate.now() %>">
        </div>
        <div class="mb-3">
            <label class="form-label">Montant</label>
            <input type="number" name="montant" class="form-control">
        </div>
        <button type="submit" class="btn btn-primary">Valider</button>
    </div>
</form>
