public class WindowsButton implements Button {
    private String label;
    
    public WindowsButton(String label) {
        this.label = label;
    }
    
    @Override
    public void render() {
        System.out.println("Rendu d'un bouton Windows: " + label);
        System.out.println("  [ " + label + " ]");
        System.out.println("  Style: Windows 11");
    }
    
    @Override
    public void onClick() {
        System.out.println("Bouton Windows cliqué: " + label);
        System.out.println("  Action: Ouvrir une fenêtre Windows");
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