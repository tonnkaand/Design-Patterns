public class MacOSFactory implements GUIFactory {
    
    @Override
    public Button createButton(String label) {
        return new MacOSButton(label);
    }
    
    @Override
    public Checkbox createCheckbox(String label) {
        return new MacOSCheckbox(label);
    }
    
    @Override
    public String getOSName() {
        return "macOS Monterey";
    }
}