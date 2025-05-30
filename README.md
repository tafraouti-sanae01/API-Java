# API-Java

## Objectif
Ce projet développe une API Java qui offre une interface unifiée pour la gestion de l'accès à différents systèmes de gestion de base de données relationnelles (SGBDR), notamment MySQL, PostgreSQL et SQL Server. L'API permet d'exécuter des opérations CRUD (Create, Read, Update, Delete) de manière uniforme, indépendamment du SGBD utilisé.

## Structure du Projet
Le projet suit la structure suivante :
```
src/
├── main/
│   ├── java/
│   │   └── ma/
│   │       └── ensa/
│   │           ├── db/
│   │           │   ├── DatabaseManager.java
│   │           │   └── impl/
│   │           │       ├── MySQLManager.java
│   │           │       ├── PostgreSQLManager.java
│   │           │       └── SQLServerManager.java
│   │           ├── util/
│   │           │   └── DBConfigLoader.java
│   │           └── Main.java
│   └── resources/
│       └── db.properties
└── test/
    └── java/
        └── ma/
            └── ensa/
                └── db/
                    └── impl/
                        ├── DatabaseManagerTest.java
```

## Fonctionnalités
1. **Support Multi-SGBD** : Gestion unifiée de MySQL, PostgreSQL et SQL Server
2. **Opérations CRUD** :
   - SELECT : Récupération des données avec retour sous forme de `List<Map<String, Object>>`
   - INSERT : Ajout de nouveaux enregistrements
   - DELETE : Suppression d'enregistrements
3. **Gestion des Connexions** :
   - Connexion automatique avec paramètres de configuration
   - Fermeture propre des connexions
   - Vérification de l'état de la connexion
4. **Configuration Flexible** :
   - Paramètres de connexion externalisés dans `db.properties`
   - Support pour différentes configurations par SGBD

## Exemple d'Utilisation
```java
// Chargement des propriétés
Properties props = DBConfigLoader.loadProperties("db.properties");

// Création d'un gestionnaire MySQL
DatabaseManager mysqlManager = new MySQLManager(
    props.getProperty("mysql.url") + "/" + props.getProperty("mysql.database"),
    props.getProperty("mysql.username"),
    props.getProperty("mysql.password")
);

// Connexion
mysqlManager.connect();

// Exécution d'une requête SELECT
List<Map<String, Object>> results = mysqlManager.executeQuery("SELECT * FROM employees");

// Affichage des résultats
for (Map<String, Object> row : results) {
    System.out.println(row);
}

// Insertion de données
int affectedRows = mysqlManager.executeUpdate(
    "INSERT INTO employees VALUES (4, 'Test1', 'User1', 'test1.user1@email.com', 4700.00)"
);

// Fermeture de la connexion
mysqlManager.disconnect();
```

## Configuration
Le fichier `db.properties` doit contenir les paramètres de connexion pour chaque SGBD :
```properties
# MySQL
mysql.url=jdbc:mysql://localhost:3306
mysql.database=your_database
mysql.username=your_username
mysql.password=your_password

# PostgreSQL
postgresql.url=jdbc:postgresql://localhost:5432
postgresql.database=your_database
postgresql.username=your_username
postgresql.password=your_password

# SQL Server
sqlserver.url=jdbc:sqlserver://localhost:1433;databaseName=your_database
sqlserver.username=your_username
sqlserver.password=your_password
```

## Tests
Le projet inclut des tests unitaires pour chaque implémentation de SGBD, vérifiant :
- La connexion à la base de données
- L'exécution des requêtes SELECT
- Les opérations d'insertion et de suppression
- La gestion des erreurs

## Dépendances
- MySQL Connector
- PostgreSQL JDBC
- SQL Server JDBC
- JUnit (pour les tests)

## Compilation et Exécution
Pour compiler et exécuter le projet :
```bash
mvn test
```

## Génération du fichier .jar
Pour générer l’archive .jar de l’application
```bash
mvn clean package
```

