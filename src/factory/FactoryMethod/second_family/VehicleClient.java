public class VehicleClient {
    public static void main(String[] args) {
        VehicleFactory factory = new VehicleFactory();
        
        System.out.println("=== Création de différents véhicules ===");
        
        // Créer une voiture
        Vehicle car = factory.createVehicle(
            VehicleFactory.TYPE_CAR, 
            "Toyota Camry", 
            2022, 
            4
        );
        car.displayInfo();
        car.start();
        car.stop();
        System.out.println();
        
        // Créer une moto
        Vehicle motorcycle = factory.createVehicle(
            VehicleFactory.TYPE_MOTORCYCLE,
            "Harley Davidson",
            2021,
            false
        );
        motorcycle.displayInfo();
        motorcycle.start();
        motorcycle.stop();
        System.out.println();
        
        // Créer un camion
        Vehicle truck = factory.createVehicle(
            VehicleFactory.TYPE_TRUCK,
            "Volvo FH",
            2023,
            20.5
        );
        truck.displayInfo();
        truck.start();
        truck.stop();
    }
}