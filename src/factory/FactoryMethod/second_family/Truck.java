public class Truck extends Vehicle {
    private double loadCapacity; // en tonnes
    
    public Truck(String model, int year, double loadCapacity) {
        super(model, year);
        this.loadCapacity = loadCapacity;
    }
    
    @Override
    public void start() {
        System.out.println("Camion " + getModel() + " démarré. Prêt pour le transport!");
    }
    
    @Override
    public void stop() {
        System.out.println("Camion " + getModel() + " arrêté.");
    }
    
    @Override
    public void displayInfo() {
        System.out.println("=== Truck Information ===");
        System.out.println("Model: " + getModel());
        System.out.println("Year: " + getYear());
        System.out.println("Load Capacity: " + loadCapacity + " tonnes");
        System.out.println("Type: Truck");
    }
    
    public double getLoadCapacity() {
        return loadCapacity;
    }
}