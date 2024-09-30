<%@ page import="java.time.LocalDate" %>

<form action="DepenseServlet" class="col-6 py-3 px-4 card" method="post">
    <h3 class="text-uppercase card-title">Depense</h3>
    <div class="card-body">
        <div class="mb-3">
            <label class="form-label">Rubrique</label>
            <select class="form-select" name="idRubrique" id="rubrique-select"></select>
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

<script>
    /* RUBRIQUES SELECT */
    const rubriques = [
        { id: 1, nom: 'Achat carburant' },
        { id: 2, nom: 'Electricité' },
    ];

    const selectElement = document.getElementById('rubrique-select');

    rubriques.forEach(rubrique => {
        const option = document.createElement('option');
        option.value = rubrique.id;
        option.textContent = rubrique.nom;
        selectElement.appendChild(option);
    });
</script>