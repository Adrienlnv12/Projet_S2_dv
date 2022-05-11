package fr.insa.leneve.projet_s2.Boutton;

import javafx.scene.control.RadioButton;


public class RadioBouton extends RadioButton {

    private String info = "";
    private final String name;

    public RadioBouton(String name) {
        super(name);
        this.name = name;
        this.setId("myRB");

    }

    public RadioBouton(String name, String info) {
        super(name);
        this.info = info;
        this.name = name;
        this.setId("myRB");
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }
}
