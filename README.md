# API-Java

## Objectif
Ce projet vise à développer une API Java packagée en fichier .jar qui offre une interface unifiée pour la gestion de l'accès à différents systèmes de gestion de base de données relationnelles (SGBDR), tels que MySQL, PostgreSQL, SQL Server et Oracle. L'objectif est de masquer les différences techniques entre les SGBD et de fournir une API simple et uniforme pour faciliter l'interaction avec plusieurs types de bases de données.

## Structure du Projet
Le projet suit la structure suivante :
```
ma/
└── ensa/
    ├── db/
    │   ├── DatabaseManager.java
    │   └── impl/
    │       ├── MySQLManager.java
    │       ├── PostgreSQLManager.java
    │       └── SQLServerManager.java
    ├── util/
    │   └── DBConfigLoader.java
    └── Main.java (Exemple d'utilisation)
resources/
└── db.properties
```

## Fonctionnalités
1. **Connexion à la base de données** : Méthodes pour se connecter à un SGBD donné (type, URL, utilisateur, mot de passe).
2. **Exécution de requêtes SQL** : Support pour les requêtes SELECT, INSERT, UPDATE, DELETE, avec retour des résultats sous forme de `List<Map<String, Object>>`.
3. **Paramétrage dynamique** : Utilisation d'un fichier de configuration (.properties) pour stocker les paramètres de connexion.
4. **Gestion des erreurs** : Gestion propre des exceptions SQL avec messages clairs et personnalisés, et fermeture automatique des connexions via try-with-resources.
5. **Design modulaire** : Utilisation d'interfaces génériques et du polymorphisme pour gérer les différents SGBD.
6. **Utilisation de Lombok** : Réduction du code boilerplate grâce aux annotations Lombok.
7. **Tests unitaires** : Utilisation de JUnit pour tester les classes avec un jeu de données stocké dans un fichier .csv.

## Dépendances
- Lombok
- MySQL Connector
- PostgreSQL JDBC
- SQL Server JDBC
- JUnit

## Génération du .jar
Pour générer le fichier .jar, exécutez la commande suivante à la racine du projet :
```bash
mvn clean package
```
Le fichier .jar sera généré dans le dossier `target/`.

## Exemple d'Utilisation
Voici un exemple d'utilisation de l'API :
```java
import ma.ensa.db.DatabaseManager;
import ma.ensa.db.impl.MySQLManager;
import ma.ensa.util.DBConfigLoader;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            Properties props = DBConfigLoader.loadProperties("db.properties");
            DatabaseManager mysqlManager = new MySQLManager(
                    props.getProperty("mysql.url") + "/" + props.getProperty("mysql.database"),
                    props.getProperty("mysql.username"),
                    props.getProperty("mysql.password")
            );
            mysqlManager.connect();
            List<Map<String, Object>> results = mysqlManager.executeQuery("SELECT * FROM employees");
            for (Map<String, Object> row : results) {
                System.out.println(row);
            }
            mysqlManager.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Documentation
Pour plus d'informations sur l'utilisation de l'API, consultez le fichier `db.properties` pour les paramètres de connexion et les exemples de code dans `Main.java`.
