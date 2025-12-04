public class Motorcycle extends Vehicle {
    private boolean hasSideCar;
    
    public Motorcycle(String model, int year, boolean hasSideCar) {
        super(model, year);
        this.hasSideCar = hasSideCar;
    }
    
    @Override
    public void start() {
        System.out.println("Moto " + getModel() + " démarrée. Kickstart!");
    }
    
    @Override
    public void stop() {
        System.out.println("Moto " + getModel() + " arrêtée.");
    }
    
    @Override
    public void displayInfo() {
        System.out.println("=== Motorcycle Information ===");
        System.out.println("Model: " + getModel());
        System.out.println("Year: " + getYear());
        System.out.println("Has Side Car: " + hasSideCar);
        System.out.println("Type: Motorcycle");
    }
    
    public boolean hasSideCar() {
        return hasSideCar;
    }
}