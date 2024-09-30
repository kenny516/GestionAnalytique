<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.Date, java.util.*, models.*" %>

<%

AdministrationAnalytique aa = (AdministrationAnalytique) request.getAttribute("adminAnalytics");

Set<Centre> centres = (Set<Centre>) request.getAttribute("centres");
HashMap<Centre, Double> pCentres = (HashMap<Centre, Double>) request.getAttribute("totalParCentre");

if (pCentres != null && !pCentres.isEmpty()) {
    // Iterate through the HashMap and print each Centre's nom and Double value
    for (Map.Entry<Centre, Double> entry : pCentres.entrySet()) {
        Centre centre = entry.getKey();
        Double total = entry.getValue();

        out.println("Centre: " + centre.getNom() + " - Total: " + total + "<br>");
    }
} else {
    out.println("No data available.");
}

%>
<!DOCTYPE html>
<html>
  <head lang="fr">
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
      crossorigin="anonymous"
    />
    <title>Gestion analytique</title>
    <script src="./assets/js/dateutils.js"></script>
  </head>
  <body class="py-5">
    <div class="d-flex flex-column align-items-center justify-content-center">
      <h1>Calcul du cout de production de kg de sel</h1>
      <p>
        Periode : <span id="start-date"></span> au <span id="end-date"></span>
      </p>

      <div id="main-table">
        <table class="table table-bordered table-hover">
          <thead>
            <tr>
              <th>Rubrique</th>
              <th>Total</th>
              <th>Unite d'oeuvre</th>
              <th>Nature</th>
              <% for(Centre c : centres) { %>
                <th colspan="3"><%= c.getNom() %></th>
              <% } %>
            </tr>
            <tr>
              <th colspan="4"></th>
              <th>%</th>
              <th>Fixe</th>
              <th>Variable</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Poo</td>
              <td>780000</td>
              <td>KG</td>
              <td>Variable</td>
              <td>75</td>
              <td>0</td>
              <td>stuff</td>
            </tr>
          </tbody>
        </table>
      </div>

      <script>
        const startSpan = document.getElementById("start-date");
        const endSpan = document.getElementById("end-date");

        let startDate = "<%= aa.getDateDebut().toString() %>";
        let endDate = "<%= aa.getDateFin().toString() %>";

        startSpan.innerHTML = formatDateFR(startDate);
        endSpan.innerHTML = formatDateFR(endDate);
      </script>
    </div>

    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
      crossorigin="anonymous"
    ></script>
  </body>
</html>
