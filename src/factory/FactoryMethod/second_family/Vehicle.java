public abstract class Vehicle {
    private String model;
    private int year;
    
    public Vehicle(String model, int year) {
        this.model = model;
        this.year = year;
    }
    
    public abstract void start();
    public abstract void stop();
    public abstract void displayInfo();
    
    public String getModel() {
        return model;
    }
    
    public int getYear() {
        return year;
    }
}