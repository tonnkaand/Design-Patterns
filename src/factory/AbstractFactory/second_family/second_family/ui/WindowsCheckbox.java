public class WindowsCheckbox implements Checkbox {
    private String label;
    private boolean checked;
    
    public WindowsCheckbox(String label) {
        this.label = label;
        this.checked = false;
    }
    
    @Override
    public void render() {
        System.out.println("Rendu d'une case à cocher Windows: " + label);
        String check = checked ? "[X]" : "[ ]";
        System.out.println("  " + check + " " + label);
        System.out.println("  Style: Windows Classic");
    }
    
    @Override
    public void onCheck() {
        this.checked = !this.checked;
        System.out.println("Case Windows cochée/décochée: " + label);
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