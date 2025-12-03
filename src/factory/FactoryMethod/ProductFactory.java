
public class ProductFactory {
    public static final String TYPE_A = "A";
    public static final String TYPE_B = "B";
    public static final String TYPE_C = "C";
    
    public Product createProduct(String type) {
        Product product = null;
        
        switch(type) {
            case TYPE_A:
                product = new ProductA();
                break;
            case TYPE_B:
                product = new ProductB();
                break;
            case TYPE_C:
                product = new ProductC();
                break;
            default:
                throw new IllegalArgumentException("Type de produit inconnu: " + type);
        }
        
        return product;
    }
}
