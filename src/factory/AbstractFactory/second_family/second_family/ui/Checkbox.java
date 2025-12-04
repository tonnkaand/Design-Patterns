public interface Checkbox {
    void render();
    void onCheck();
    boolean isChecked();
    void setChecked(boolean checked);
    String getLabel();
    void setLabel(String label);
}