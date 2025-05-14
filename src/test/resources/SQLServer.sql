-- Création de la base de données
CREATE DATABASE TestAPI;
GO

-- Utilisation de la base
USE TestAPI;
GO

-- Table de test
CREATE TABLE employees (
     id INT PRIMARY KEY,
     first_name NVARCHAR(50),
     last_name NVARCHAR(50),
     email NVARCHAR(100),
     salary DECIMAL(10,2)
);
GO

-- Données de test
INSERT INTO employees VALUES
    (1, 'Sanae', 'Tafraouti', 'sanae.tafraouti@gmail.com', 3500.00),
    (2, 'Yaakoub', 'Hamani', 'yaakoub.hamani@gmail.com', 4200.50),
    (3, 'Mohamed', 'Akrami', 'mohamed.akrami@gmail.com', 3800.75);
GO