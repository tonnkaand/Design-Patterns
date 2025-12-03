public class Client {
    public static void main(String[] args) {
        // Première méthode d'utilisation
        Singleton.getInstance().operation1(1, 2, 3);
        Singleton.getInstance().operation2(3, 4, 5);
        
        // Seconde méthode d'utilisation
        Singleton singleton = Singleton.getInstance();
        singleton.setAttribute1("Nouvelle valeur");
        singleton.setAttribute2(42);
        
        System.out.println("Attribut1: " + singleton.getAttribute1());
        System.out.println("Attribut2: " + singleton.getAttribute2());
        
        // Vérification que c'est la même instance
        Singleton anotherReference = Singleton.getInstance();
        System.out.println("Même instance? " + (singleton == anotherReference));
    }
}
