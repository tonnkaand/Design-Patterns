public final class DatabaseConnection {
    // Instance unique
    private static DatabaseConnection instance = null;
    
    // Attributs de connexion
    private String url;
    private String username;
    private String password;
    private boolean isConnected;
    private int connectionCount;
    
    // Constructeur privé
    private DatabaseConnection() {
        // Configuration par défaut
        this.url = "jdbc:mysql://localhost:3306/mydatabase";
        this.username = "admin";
        this.password = "password";
        this.isConnected = false;
        this.connectionCount = 0;
        System.out.println("Instance DatabaseConnection créée (configuration par défaut)");
    }
    
    // Méthode d'accès thread-safe
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    // Méthode avec paramètres optionnels
    public static synchronized DatabaseConnection getInstance(String url, String username, String password) {
        if (instance == null) {
            instance = new DatabaseConnection();
            instance.url = url;
            instance.username = username;
            instance.password = password;
            System.out.println("Instance DatabaseConnection créée (configuration personnalisée)");
        }
        return instance;
    }
    
    // Méthodes de connexion
    public void connect() {
        if (!isConnected) {
            isConnected = true;
            connectionCount++;
            System.out.println("Connecté à la base de données: " + url);
            System.out.println("Utilisateur: " + username);
            System.out.println("Nombre total de connexions: " + connectionCount);
        } else {
            System.out.println("Déjà connecté à la base de données");
        }
    }
    
    public void disconnect() {
        if (isConnected) {
            isConnected = false;
            System.out.println("Déconnecté de la base de données");
        } else {
            System.out.println("Déjà déconnecté");
        }
    }
    
    public void executeQuery(String query) {
        if (isConnected) {
            System.out.println("Exécution de la requête: " + query);
            System.out.println("Résultat simulé: 10 lignes affectées");
        } else {
            System.out.println("Erreur: Non connecté à la base de données");
        }
    }
    
    // Getters et Setters
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
        System.out.println("URL de la base de données mise à jour: " + url);
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public int getConnectionCount() {
        return connectionCount;
    }
    
    // Empêcher le clonage
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("DatabaseConnection ne peut pas être cloné");
    }
    
    // Sérialisation sécurisée
    protected Object readResolve() {
        return getInstance();
    }
}