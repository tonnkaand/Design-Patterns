public class Client {
    public static void main(String[] args) {
        ProductFactory factory = new ProductFactory();
        
        // Produit de type A
        Product productA = factory.createProduct(ProductFactory.TYPE_A);
        productA.productMethod();
        
        // Produit de type B
        Product productB = factory.createProduct(ProductFactory.TYPE_B);
        productB.productMethod();
        
        // Produit de type C
        Product productC = factory.createProduct(ProductFactory.TYPE_C);
        productC.productMethod();
    }
}
