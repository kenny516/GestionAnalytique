<%@ page import="java.time.LocalDate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.Rubrique" %>
<%@ page import="models.Centre" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.*" %>

<% List<Rubrique> rubriques = (List<Rubrique>) request.getAttribute("Rubriques"); %>
<% List<Centre> centres = (List<Centre>) request.getAttribute("Centres"); %>
<% Rubrique rselect = (Rubrique) request.getAttribute("RubriqueSelect"); %>

<form action="PartsServlet" class="col-6 py-3 px-4 card" method="post">
    <h3 class="text-uppercase card-title">Parts par centre</h3>
    <div class="card-body">
        <div class="mb-3">
            <label class="form-label">Rubrique</label>
            <select class="form-select" name="idRubrique" id="rubrique-select">
                <%
                if (rselect == null){
                    for (Rubrique ru : rubriques){
                %>
                    <option value="<%= ru.getId() %>"><%= ru.getNom() %> </option>
                <%
                }
                }
                else {
                    for (Rubrique ru : rubriques){
                        String select = "";
                        if(ru.getNom().equalsIgnoreCase(rselect.getNom())){
                            select = "selected";
                        }
                %>
                    <option value="<%= ru.getId() %>"><%= ru.getNom() %> </option>
                <%
                    }}
                %>            
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Valeur en %</label>
            <table class="table table-bordered">
                <thead>
                    <tr id="part-table-head">
                        <%
                            for (Centre c : centres){
                        %>
                            <th><%= c.getNom() %></th>
                        <%
                            }
                        %>
                    </tr>
                </thead>
                <tbody>
                    <tr id="part-table-body">
                        <%
                            for (Centre c : centres){
                        %>
                            <td><input type="text" name="<%= c.getNom() %>" class="form-control"></td>
                        <%
                            }
                        %>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="mb-3">
            <label class="form-label">Date</label>
            <input type="date" name="dateInsertion" class="form-control" value="<%= LocalDate.now() %>">
        </div>
        <button type="submit" class="btn btn-primary">Valider</button>
    </div>
</form>
