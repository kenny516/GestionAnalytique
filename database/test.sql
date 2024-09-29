use gestion_analytique;

ALTER TABLE UniteOeuvre MODIFY COLUMN nom VARCHAR(50);


-- Insert data into NatureCentre
INSERT INTO NatureCentre (nom)
VALUES ('Production'),
       ('Administration'),
       ('Distribution');

-- Insert data into Centre
INSERT INTO Centre (nom, idNature)
VALUES ('Salt Evaporation Plant', 1),
       ('Warehouse', 1),
       ('Office', 2),
       ('Logistics Hub', 3);

-- Insert data into UniteOeuvre
INSERT INTO UniteOeuvre (nom)
VALUES ('Tonnes of Salt'),
       ('Hours Worked'),
       ('Liters of Brine Processed');

-- Insert data into Produit
INSERT INTO Produit (nom, idUniteOeuvre)
VALUES ('Raw Salt', 1),
       ('Refined Salt', 1),
       ('Brine', 3);

-- Insert data into Production
INSERT INTO Production (idProduit, date, quantite)
VALUES (1, '2024-09-01', 1000.00), -- Raw Salt production
       (2, '2024-09-02', 500.00),  -- Refined Salt production
       (3, '2024-09-03', 1500.00);
-- Brine production

-- Insert data into Rubrique
INSERT INTO Rubrique (nom, estVariable, idUniteOeuvre, dateInsertion)
VALUES ('Labor Costs', 1, 2, '2024-09-01'),        -- Variable labor costs linked to hours worked
       ('Raw Material Costs', 1, 3, '2024-09-01'), -- Variable material costs linked to brine processed
       ('Fixed Plant Maintenance', 0, 2, '2024-09-01');
-- Fixed costs linked to hours worked

-- Insert data into PartsParCentre
INSERT INTO PartsParCentre (idRubrique, idCentre, valeur, dateInsertion)
VALUES (1, 1, 15000.00, '2024-09-05'), -- Labor costs for Salt Evaporation Plant
       (2, 1, 5000.00, '2024-09-05'),  -- Raw Material costs for Salt Evaporation Plant
       (3, 2, 12000.00, '2024-09-05');
-- Plant Maintenance costs for Warehouse

-- Insert data into Depenses
INSERT INTO Depenses (dateDepense, montant)
VALUES ('2024-09-10', 20000.00), -- General expenses
       ('2024-09-15', 5000.00);
-- Additional expenses

-- Insert data into AssoDepensesParts
INSERT INTO AssoDepensesParts (idDepense, idPart)
VALUES (1, 1), -- Associate first expense with the first part (Labor costs)
       (2, 3); -- Associate second expense with the third part (Plant Maintenance)
