package la_teca_del_giardiniere.classes;

public class SidebarItem {
    private String image;
    private String text;
    private String alt;

    public SidebarItem(String image, String text) {
        this.image = image;
        this.text = text;
        this.alt = text; // L'attributo 'alt' può essere uguale al testo per semplicità
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public String getAlt() {
        return alt;
    }
    
    public void setAlt(String alt) {
        this.alt = alt;
    }
}