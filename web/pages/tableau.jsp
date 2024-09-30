<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.Date, java.util.*, models.*" %>

<%

AdministrationAnalytique aa = (AdministrationAnalytique) request.getAttribute("adminAnalytics");

ArrayList<Centre> centres = (ArrayList<Centre>) request.getAttribute("centres");
HashMap<Centre, Double> pCentres = (HashMap<Centre, Double>) request.getAttribute("totalParCentre");

List<Rubrique> rubriques = (List<Rubrique>) aa.getRubriques();
List<HashMap<Centre, double[]>> partsAndTotal = (List<HashMap<Centre, double[]>>) request.getAttribute("partsAndTotal");
HashMap<Rubrique, Double> totalParRubrique = (HashMap<Rubrique, Double>) request.getAttribute("totalParRubrique");
 
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
    <script src="./assets/js/formatutils.js"></script>

    <style>
    .number-cell {
      text-align: right;
    }
    </style>
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
              <th>Unite d'oeuvre</th>
              <th>Variable</th>
              <th>Total</th>
              <% for(Centre c : centres) { %>
                <th colspan="3"><%= c.getNom() %></th>
              <% } %>
              <th colspan="2">Total</th>
            </tr>
            <tr>
                <th colspan="4"></th>
              <% for(Centre c : centres) { %>
                <th>%</th>
                <th>Fixe</th>
                <th>Variable</th>
              <% } %>
                <th>Fixe</th>
                <th>Variable</th>
            </tr>
          </thead>
          <tbody>
          <% for(int i = 0; i < rubriques.size(); i++) { 
            Rubrique r = rubriques.get(i);
            
          %>
            <tr>
              <td><%= r.getNom() %></td>
              <td><%= r.getUniteOeuvre().getNom() %></td>
              <td>
                <%
                  double totalR = r.getInMap(totalParRubrique);
                  String checked = r.isEstVariable() ? "checked" : "";
                
                %>
                <input class="form-check-input" type="checkbox" value="" id="flexCheckCheckedDisabled" <%= checked %> disabled>
              </td>
              <td class="number-cell"><%= totalR %></td>
              <% 
                HashMap<Centre, double[]> dataPerCentre = partsAndTotal.get(i);
                double totalFixe = r.isEstVariable() ? 0 : totalR;
                double totalVariable = r.isEstVariable() ? totalR : 0;

                for(Centre c : centres) { 
                  double[] values = AdministrationAnalytique.get(dataPerCentre, c);
                  double fixe = r.isEstVariable() ? 0 : values[1];
                  double variable = r.isEstVariable() ? values[1] : 0;
              %>
                <td class="number-cell"><%= values[0] %></td>
                <td class="number-cell"><%= fixe %></td>
                <td class="number-cell"><%= variable %></td>
              <% } %>
                <td class="number-cell"><%= totalFixe %></td>
                <td class="number-cell"><%= totalVariable %></td>
            </tr>
          <% } %>
          </tbody>
          <tfoot>
            <tr>
              <th colspan="3">Total</th>
              <td class="number-cell">
                <%
                double overallTotal = (double) request.getAttribute("overallTotal");
                out.println(overallTotal);
                %>
              </td>
                <% 
                
                  for(Centre c : centres) { 
                  Double d = AdministrationAnalytique.getDouble(pCentres, c);
                %>
                <td class="number-cell" colspan="3"><%= d %></td>

              <% } 
              
              double totalOverallF = (double) request.getAttribute("totalFixe");
              double totalOverallV = (double) request.getAttribute("totalVariable");
              
              %>
              <td class="number-cell"><%= totalOverallF %></td>
              <td class="number-cell"><%= totalOverallV %></td>
            </tr>
          </tfoot>
        </table>
      </div>

      <script>
        const startSpan = document.getElementById("start-date");
        const endSpan = document.getElementById("end-date");

        let startDate = "<%= aa.getDateDebut().toString() %>";
        let endDate = "<%= aa.getDateFin().toString() %>";

        startSpan.innerHTML = formatDateFR(startDate);
        endSpan.innerHTML = formatDateFR(endDate);

        numbersElements = document.getElementsByClassName("number-cell");
        for(let n of numbersElements) {
          let number = n.innerHTML;
          let formattedNB = formatNumberToFrenchLocale(number);
          n.innerHTML= formattedNB;
        }
      </script>
    </div>

    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
      crossorigin="anonymous"
    ></script>
  </body>
</html>
