<form action="PartsServlet" class="col-6 py-3 px-4 card" method="post">
    <h3 class="text-uppercase card-title">Parts par centre</h3>
    <div class="card-body">
        <div class="mb-3">
            <label class="form-label">Rubrique</label>
            <select class="form-select" name="idRubrique" id="rubrique-select"></select>
        </div>
        <div class="mb-3">
            <label class="form-label">Valeur en %</label>
            <table class="table table-bordered">
                <thead>
                    <tr id="part-table-head"></tr>
                </thead>
                <tbody>
                    <tr id="part-table-body"></tr>
                </tbody>
            </table>
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

    /* PARTS TABLE */
    const centres = [
        {id: 1, nom: 'ADMIN/DIST'},
        {id: 2, nom: 'USINE'},
        {id: 3, nom: 'MARAIS'},
    ];

    const thead = document.getElementById('part-table-head');
    const tbody = document.getElementById('part-table-body');

    centres.forEach(centre => {
        const th = document.createElement("th");
        th.innerHTML = centre.nom;
        thead.appendChild(th);

        const td = document.createElement("td");
        
        const input = document.createElement("input");
        input.setAttribute("type", "text");
        input.setAttribute("class", "form-control");
        input.setAttribute("name", centre.nom); // dans ce cas-ci, chaque input aura pour name le nom du centre correspondant, à adapter selon l'intégration

        td.appendChild(input);
        tbody.appendChild(td);
    });
</script>