/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;


import javafx.scene.layout.BorderPane;
/**
 *
 * @author adrie
 */
public final class MainPanel extends BorderPane {
    /**
     * multiplicateur pour l'espace de départ : pour ne pas que les bords de la
     * figure soit confondus avec les bords de la fenêtre, on considère que l'on
     * veut que la fenêtre puisse contenir une figure MULT_POUR_FIT_ALL fois
     * plus grande que la figure réelle. Par exemple, si MULT_POUR_FIT_ALL = 2,
     * la figure complète n'occupera en fait qu'environ la moitié de la fenetre
     * graphique.
     */
    
    private final Controleur controleur;
 
    private final DessinCanvas cDessin;

    private final MainMenu menu;
    private final FenetreInfo Info;
    
    private final BoutonGauche boutong;
    private final BoutonDroite boutond;
    
    public MainPanel(int w, int h, Controleur controleur){ 
        super();
        this.controleur = controleur;
       
        
        boutond = new BoutonDroite(this);
        this.setRight(boutond);
        
        boutong = new BoutonGauche(this);
        this.setLeft(boutong);
                
        Info = new FenetreInfo(this);
        this.setRight(Info);
        
        this.cDessin = new DessinCanvas(this);// zone de dessin
        this.setCenter(this.cDessin);
        
        this.menu = new MainMenu(this);
        this.setTop(this.menu);
    }  
        

    public void redrawAll() {
        this.cDessin.redrawAll();
    }

    /**
     * @return the controleur
     */
    public Controleur getControleur() {
        return controleur;
    }
    
    public FenetreInfo getInfos() {
        return Info;
    }
    
    /**
     * @return the cDessin
     */
    public DessinCanvas getcDessin() {
        return cDessin;
    }

    public BoutonDroite getBoutonDroite() {
        return boutond;
    }
    
    public BoutonGauche getBoutonGauche() {
        return boutong;
    }
   
    
}
