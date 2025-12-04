public class MacOSCheckbox implements Checkbox {
    private String label;
    private boolean checked;
    
    public MacOSCheckbox(String label) {
        this.label = label;
        this.checked = false;
    }
    
    @Override
    public void render() {
        System.out.println("Rendu d'une case à cocher macOS: " + label);
        String check = checked ? "☑" : "☐";
        System.out.println("  " + check + " " + label);
        System.out.println("  Style: macOS Big Sur");
    }
    
    @Override
    public void onCheck() {
        this.checked = !this.checked;
        System.out.println("Case macOS cochée/décochée: " + label);
        System.out.println("  Animation fluide macOS");
        System.out.println("  Nouvel état: " + (checked ? "coché" : "décoché"));
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
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