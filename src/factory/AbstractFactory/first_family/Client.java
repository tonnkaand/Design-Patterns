public class Client {
    public static void main(String[] args) {
        // Utilisation de la première fabrique
        IProductFactory factory1 = new ProductFactory1();
        
        ProductA productA1 = factory1.getProductA();
        ProductB productB1 = factory1.getProductB();
        
        System.out.println("Utilisation de la première fabrique:");
        productA1.methodA();
        productB1.methodB();
        
        // Utilisation de la seconde fabrique
        IProductFactory factory2 = new ProductFactory2();
        
        ProductA productA2 = factory2.getProductA();
        ProductB productB2 = factory2.getProductB();
        
        System.out.println("\nUtilisation de la seconde fabrique:");
        productA2.methodA();
        productB2.methodB();
    }
}
