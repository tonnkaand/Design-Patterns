public interface GUIFactory {
    Button createButton(String label);
    Checkbox createCheckbox(String label);
    String getOSName();
}