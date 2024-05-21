DROP TABLE rendez_vous;
DROP TABLE patient;


CREATE TABLE Patient (
    Nom VARCHAR2(100),
    Prenom VARCHAR2(100),
    Telephone number(10),
    Antecedents VARCHAR2(255),
    Observations VARCHAR2(255),
    Ordonnance  VARCHAR2(255),
    constraint pk_Patient PRIMARY KEY (Nom,Prenom)
);



CREATE TABLE rendez_vous (
    Nom VARCHAR2(100),
    Prenom VARCHAR2(100),
    Annee VARCHAR2(100),
    Mois VARCHAR2(100),
    Jour VARCHAR2(100),
    Heure VARCHAR2(100),
    constraint pk_rendez_vous PRIMARY key (Nom,Prenom)
   );