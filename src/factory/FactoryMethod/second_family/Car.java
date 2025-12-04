public class Car extends Vehicle {
    private int numberOfDoors;
    
    public Car(String model, int year, int numberOfDoors) {
        super(model, year);
        this.numberOfDoors = numberOfDoors;
    }
    
    @Override
    public void start() {
        System.out.println("Voiture " + getModel() + " démarrée. Tournez la clé!");
    }
    
    @Override
    public void stop() {
        System.out.println("Voiture " + getModel() + " arrêtée.");
    }
    
    @Override
    public void displayInfo() {
        System.out.println("=== Car Information ===");
        System.out.println("Model: " + getModel());
        System.out.println("Year: " + getYear());
        System.out.println("Doors: " + numberOfDoors);
        System.out.println("Type: Car");
    }
    
    public int getNumberOfDoors() {
        return numberOfDoors;
    }
}