public class VehicleFactory {
    public static final String TYPE_CAR = "CAR";
    public static final String TYPE_MOTORCYCLE = "MOTORCYCLE";
    public static final String TYPE_TRUCK = "TRUCK";
    
    public Vehicle createVehicle(String type, String model, int year, Object... params) {
        Vehicle vehicle = null;
        
        switch(type.toUpperCase()) {
            case TYPE_CAR:
                int doors = params.length > 0 ? (int) params[0] : 4;
                vehicle = new Car(model, year, doors);
                break;
                
            case TYPE_MOTORCYCLE:
                boolean hasSideCar = params.length > 0 ? (boolean) params[0] : false;
                vehicle = new Motorcycle(model, year, hasSideCar);
                break;
                
            case TYPE_TRUCK:
                double capacity = params.length > 0 ? (double) params[0] : 5.0;
                vehicle = new Truck(model, year, capacity);
                break;
                
            default:
                throw new IllegalArgumentException("Type de véhicule inconnu: " + type);
        }
        
        return vehicle;
    }
}