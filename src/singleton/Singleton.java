public final class Singleton {
    // Instance unique
    private static Singleton instance = null;
    
    // Attributs
    private String attribute1;
    private int attribute2;
    
    // Constructeur privé
    private Singleton() {
        super();
        this.attribute1 = "Valeur par défaut";
        this.attribute2 = 0;
    }
    
    // Méthode d'accès à l'instance unique
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
    
    // Méthodes de l'instance
    public void operation1(int x, int y, int z) {
        this.attribute1 = "bonjour";
        System.out.println("Operation1 exécutée avec x=" + x + ", y=" + y + ", z=" + z);
    }
    
    public void operation2(int x, int y, int z) {
        System.out.println("Operation2 exécutée avec x=" + x + ", y=" + y + ", z=" + z);
    }
    
    // Getters et Setters
    public String getAttribute1() {
        return attribute1;
    }
    
    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }
    
    public int getAttribute2() {
        return attribute2;
    }
    
    public void setAttribute2(int attribute2) {
        this.attribute2 = attribute2;
    }
    
    // Empêcher le clonage
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton ne peut pas être cloné");
    }
}
