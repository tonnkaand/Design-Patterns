public class Application {
    private Button button;
    private Checkbox checkbox;
    private GUIFactory factory;
    
    public Application(GUIFactory factory) {
        this.factory = factory;
    }
    
    public void createUI() {
        System.out.println("=== Création d'interface pour " + factory.getOSName() + " ===");
        
        button = factory.createButton("Sauvegarder");
        checkbox = factory.createCheckbox("J'accepte les conditions");
        
        System.out.println("UI créée avec succès!");
    }
    
    public void renderUI() {
        System.out.println("\n=== Rendu de l'interface ===");
        button.render();
        checkbox.render();
    }
    
    public void simulateUserInteraction() {
        System.out.println("\n=== Simulation d'interaction utilisateur ===");
        button.onClick();
        checkbox.onCheck();
        checkbox.onCheck(); // Pour voir le changement
    }
    
    public static void main(String[] args) {
        System.out.println("=== Application Interface Utilisateur Multiplateforme ===\n");
        
        // Version Windows
        System.out.println("1. VERSION WINDOWS:");
        Application windowsApp = new Application(new WindowsFactory());
        windowsApp.createUI();
        windowsApp.renderUI();
        windowsApp.simulateUserInteraction();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Version macOS
        System.out.println("2. VERSION MACOS:");
        Application macApp = new Application(new MacOSFactory());
        macApp.createUI();
        macApp.renderUI();
        macApp.simulateUserInteraction();
    }
}