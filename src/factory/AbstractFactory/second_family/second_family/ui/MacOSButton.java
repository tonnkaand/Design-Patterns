public class MacOSButton implements Button {
    private String label;
    
    public MacOSButton(String label) {
        this.label = label;
    }
    
    @Override
    public void render() {
        System.out.println("Rendu d'un bouton macOS: " + label);
        System.out.println("  [ " + label + " ]");
        System.out.println("  Style: macOS Monterey - Arrondi");
    }
    
    @Override
    public void onClick() {
        System.out.println("Bouton macOS cliqué: " + label);
        System.out.println("  Action: Animation macOS spécifique");
    }
    
    @Override
    public String getLabel() {
        return label;
    }
    
    @Override
    public void setLabel(String label) {
        this.label = label;
    }
}