create DATABASE gestion_analytique;

use gestion_analytique;

create table NatureCentre
(
    id  INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
) engine = innodb;

create table Centre
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nom      VARCHAR(50) NOT NULL,
    idNature INT,
    FOREIGN KEY (idNature) REFERENCES NatureCentre (id)
) engine = innodb;

create table UniteOeuvre
(
    id  INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL
) engine = innodb;

create table Produit
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nom           VARCHAR(50) NOT NULL,
    idUniteOeuvre INT,
    FOREIGN KEY (idUniteOeuvre) REFERENCES UniteOeuvre (id)
) engine = innodb;

create table Production
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    idProduit INT,
    date      DATE           NOT NULL,
    quantite  DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (idProduit) REFERENCES Produit (id)
) engine = innodb;

create table Rubrique
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nom           VARCHAR(255) NOT NULL,
    estVariable   BOOLEAN      NOT NULL,
    idUniteOeuvre INT,
    dateInsertion DATE         NOT NULL,
    FOREIGN KEY (idUniteOeuvre) REFERENCES UniteOeuvre (id)
) engine = innodb;

create table PartsParCentre
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    idRubrique    INT,
    idCentre      INT,
    valeur        DECIMAL(10, 2) NOT NULL,
    dateInsertion DATE           NOT NULL,
    FOREIGN KEY (idRubrique) REFERENCES Rubrique (id),
    FOREIGN KEY (idCentre) REFERENCES Centre (id)
) engine = innodb;

create table Depenses
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    dateDepense DATE           NOT NULL,
    montant     DECIMAL(10, 2) NOT NULL
) engine = innodb;

create table AssoDepensesParts
(
    id        INT,
    idDepense INT,
    idPart    INT,
    PRIMARY KEY (idDepense, idPart),
    FOREIGN KEY (idDepense) REFERENCES Depenses (id),
    FOREIGN KEY (idPart) REFERENCES PartsParCentre (id)
) engine = innodb;