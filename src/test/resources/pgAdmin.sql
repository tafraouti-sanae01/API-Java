-- Création de la base de données
CREATE DATABASE test_api;

-- Connexion à la base (à faire dans pgAdmin)

-- Table de test
CREATE TABLE IF NOT EXISTS employees (
    id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    salary DECIMAL(10,2)
    );

-- Données de test
INSERT INTO employees VALUES
    (1, 'Sanae', 'Tafraouti', 'sanae.tafraouti@gmail.com', 3500.00),
    (2, 'Yaakoub', 'Hamani', 'yaakoub.hamani@gmail.com', 4200.50),
    (3, 'Mohamed', 'Akrami', 'mohamed.akrami@gmail.com', 3800.75);