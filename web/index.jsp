<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <title>Gestion analytique</title>
</head>
<body class="py-5">
    <!-- Barre de navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Gestion Analytique</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"></li>
                        <a class="nav-link active" aria-current="page" href="index.jsp">Accueil</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="UniteOeuvreServlet">Unite d'oeuvre</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="RubriqueServlet">Rubriques</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="DepenseServlet">Depense</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="PartsServlet">Parts</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Contenu de la page -->
    <div class="d-flex al ign-items-center justify-content-center">
        <%
            String pageToInclude = (String) request.getAttribute("page");
            if (pageToInclude == null || pageToInclude.isEmpty()) {
        %>
            <h1>Bienvenue dans la gestion analytique</h1>
        <%
            } else {
        %>
            <jsp:include page="./pages/${page}.jsp" />
        <%
            }
        %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
