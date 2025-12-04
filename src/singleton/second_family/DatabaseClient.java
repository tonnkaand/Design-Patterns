public class DatabaseClient {
    public static void main(String[] args) {
        System.out.println("=== Gestion de Connexion à la Base de Données (Singleton) ===\n");
        
        // Premier accès - création avec paramètres
        DatabaseConnection db1 = DatabaseConnection.getInstance(
            "jdbc:mysql://localhost:3306/entreprise",
            "root",
            "securepass123"
        );
        
        db1.connect();
        db1.executeQuery("SELECT * FROM employees");
        db1.executeQuery("UPDATE employees SET salary = salary * 1.1");
        
        // Deuxième accès - récupération de la même instance
        System.out.println("\n--- Deuxième accès ---");
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        
        // Vérification que c'est la même instance
        System.out.println("Même instance? " + (db1 == db2));
        System.out.println("URL db1: " + db1.getUrl());
        System.out.println("URL db2: " + db2.getUrl());
        
        db2.connect(); // Déjà connecté
        db2.executeQuery("SELECT * FROM departments");
        
        // Troisième accès depuis un autre thread simulé
        System.out.println("\n--- Accès depuis 'autre thread' ---");
        DatabaseConnection db3 = DatabaseConnection.getInstance();
        System.out.println("Compteur de connexions: " + db3.getConnectionCount());
        
        // Déconnexion
        System.out.println("\n--- Déconnexion ---");
        db1.disconnect();
        db2.disconnect(); // Déjà déconnecté
        
        // Test de reconnexion
        System.out.println("\n--- Reconnexion ---");
        db1.connect();
        System.out.println("Nombre total de connexions: " + db1.getConnectionCount());
    }
}