<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.UniteOeuvre" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.time.LocalDate" %>

<form action="RubriqueServlet" class="col-6 py-3 px-4 card" method="post">
    <h3 class="text-uppercase card-title">rubrique</h3>
    <div class="card-body">
        <div class="mb-3">
            <label class="form-label">Nom</label>
            <input type="text" name="nom" class="form-control">
        </div>
        <div class="mb-3">
            <label class="form-label">Unité d'oeuvre</label>
            <select class="form-select" id="oeuvres-select" name="idUniteOeuvre"></select>
        </div>
        <div class="mb-3">
            <label class="form-label">Date</label>
            <input type="date" name="date" class="form-control" value="<%= LocalDate.now() %>">
        </div>
        <div class="form-check form-switch mb-3">
            <input class="form-check-input" type="checkbox" role="switch" name="estVariable" checked>
            <label class="form-check-label">Variable</label>
        </div>
        <button type="submit" class="btn btn-primary">Valider</button>
    </div>
</form>


<script>
    const uoeuvres = [
        { id: 1, nom: 'KG' },
        { id: 2, nom: 'Litres' },
        { id: 3, nom: 'Cout périodique' }
    ];

    const selectElement = document.getElementById('oeuvres-select');

    uoeuvres.forEach(oeuvre => {
        const option = document.createElement('option');
        option.value = oeuvre.id;
        option.textContent = oeuvre.nom;
        selectElement.appendChild(option);
    });
</script>