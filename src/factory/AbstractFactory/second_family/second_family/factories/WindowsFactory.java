public class WindowsFactory implements GUIFactory {
    
    @Override
    public Button createButton(String label) {
        return new WindowsButton(label);
    }
    
    @Override
    public Checkbox createCheckbox(String label) {
        return new WindowsCheckbox(label);
    }
    
    @Override
    public String getOSName() {
        return "Windows 11";
    }
}