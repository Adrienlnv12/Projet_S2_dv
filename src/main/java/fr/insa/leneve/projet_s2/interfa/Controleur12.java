/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.Barre;
import fr.insa.leneve.projet_s2.Noeud;
import fr.insa.leneve.projet_s2.Numeroteur;
import fr.insa.leneve.projet_s2.TriangleTerrain;
import fr.insa.leneve.projet_s2.Figure;
import fr.insa.leneve.projet_s2.Groupe;
import static java.lang.Math.hypot;
import static javafx.scene.paint.Color.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author adrie
 */
public class Controleur12 {
    
    private MainPanel vue;
    private int etat;
    private Noeud noeud1DansModel;
    private Numeroteur<TriangleTerrain> NTT;//a modifier
    private Numeroteur<Barre> NB;//a modifier
    private Numeroteur<Noeud> NNoeud;
    private List<Figure> selection;
    
    private Barre barreEnCoursDeCreation = null;
    
    private double P1x,P1y,P2x,P2y,P3x,P3y;
    private TriangleTerrain TriTerrain;
    
    final int Rayon=5;
    final double a = 0.5; // alpha compris entre 0 et 1 (pour la forme du noeud appui simple)
    final double cote = 5;
    
    
    public Controleur12(MainPanel vue){
        this.vue = vue;
        this.selection = new ArrayList<>();
 
        this.NNoeud = new Numeroteur<>();
        this.NTT = new Numeroteur<>();
        
    }
    
    /**
     * méthode pour changer l'état du controleur, 
     * puisque les différents boutons ne font pas les mêmes choses
     * on différencie les cas.
     * Pour l'instant la méthode change juste l'état, mais elle peut permettre
     * aussi de désélectionner certains boutons quand d'autres sont activés.
     * par exemple entre les boutons de gauche et de droite
     * @param nouvelEtat 
     */
    
    public void changeEtat(int nouvelEtat){ 
        switch (nouvelEtat) {
            case 20 :
                this.vue.getRbSelect().setSelected(true);
                this.selection.clear();
                this.barreEnCoursDeCreation = null;
                this.vue.redrawAll();
            case 30 :
                // creation de points
                this.vue.getRbNoeud().setSelected(true); //a modifier
                this.selection.clear();
                this.barreEnCoursDeCreation = null;
                this.vue.getbGrouper().setDisable(true);
                this.vue.redrawAll();
            case 40 : 
                // creation de segments étape 1
                this.vue.getRbBarre().setSelected(true);
                this.selection.clear();
                this.barreEnCoursDeCreation = null;
                this.vue.getbGrouper().setDisable(true);
                this.vue.redrawAll();
            case 41 : 
                // creation de segments étape 2
            //le comportement des boutons les uns par rapport aux autres n'a que très peu été défini pour l'instant
            default:
                break;
        }
        this.etat = nouvelEtat;
        this.activeBoutonsSuivantSelection();

    }
    
     /**
     * transforme les coordonnées (xVue,yVue) dans le repère de la vue, en un
     * point du modele en tenant compte de la transformation actuelle appliquée
     * à la vue.
     *
     * @param xVue pos x dans la vue
     * @param yVue pos y dans la vue
     * @return un Point apprès application de la transformation vue --> model
     */
    public Noeud posInModel(double xVue, double yVue) {
        Transform modelVersVue = this.vue.getcDessin().getTransform();
        Point2D ptrans;
        try {
            ptrans = modelVersVue.inverseTransform(xVue, yVue);
        } catch (NonInvertibleTransformException ex) {
            throw new Error(ex);
        }
        Noeud pclic = new Noeud(ptrans.getX(), ptrans.getY());
        pclic.setCouleur(this.vue.getCpCouleur().getValue());
        return pclic;
    }
    
    public void clicDansZoneDessin(MouseEvent t) {
        switch(this.etat){
            case 20 -> {
            // selection
            Noeud pclic = this.posInModel(t.getX(), t.getY());
            // pas de limite de distance entre le clic et l'objet selectionné
            Figure proche = this.vue.getModel().plusProche(pclic, Double.MAX_VALUE);
            // il faut tout de même prévoir le cas ou le groupe est vide
            // donc pas de plus proche
            if (proche != null) {
                if (t.isShiftDown()) {
                    this.selection.add(proche);
                } else if (t.isControlDown()) {
                    if (this.selection.contains(proche)) {
                        this.selection.remove(proche);
                    } else {
                        this.selection.add(proche);
                    }
                } else {
                    this.selection.clear();
                    this.selection.add(proche);
                }
                this.activeBoutonsSuivantSelection();
                this.vue.redrawAll();
            }
            }
            case 30 -> {
            // creation points
            Noeud pclic = this.posInModel(t.getX(), t.getY());
            Groupe model = this.vue.getModel();
            model.add(pclic);
            this.vue.redrawAll();
            }
            case 40 -> {
            // creation segment premier point
            this.noeud1DansModel = this.posInModel(t.getX(), t.getY());
            this.barreEnCoursDeCreation = new Barre(this.noeud1DansModel,
                    new Noeud(this.noeud1DansModel),
                    this.vue.getCpCouleur().getValue());
            this.changeEtat(41);
            }
            case 41 -> {
            // creation de segment deuxieme point
            Noeud pclic = this.posInModel(t.getX(), t.getY());
            Barre ns = new Barre(this.noeud1DansModel, pclic,
                    this.vue.getCpCouleur().getValue());
            this.vue.getModel().add(ns);
            this.barreEnCoursDeCreation = null;
            this.vue.redrawAll();
            this.changeEtat(40);
            }
        }
    }
    
    public void boutonSelect(ActionEvent t) {
        this.changeEtat(20);
    }

    public void boutonNoeud(ActionEvent t) {
        this.changeEtat(30);
    }

    public void boutonBarre(ActionEvent t) {
        this.changeEtat(40);
    }
    

    private void activeBoutonsSuivantSelection() {
        this.vue.getbGrouper().setDisable(true);
        this.vue.getBSupObj().setDisable(true);
        if (this.etat == 20) {
            if (this.selection.size() > 0) {
                this.vue.getBSupObj().setDisable(false);
                if (this.selection.size() > 1) {
                    this.vue.getbGrouper().setDisable(false);
                }
            }
        }
    }
    
    /**
     * @return the selection
     */
    public List<Figure> getSelection() {
        return selection;
    }

    public void boutonGrouper(ActionEvent t) {
        if (this.etat == 20 && this.selection.size() > 1) {
            // normalement le bouton est disabled dans le cas contraire
            Groupe ssGroupe = this.vue.getModel().sousGroupe(selection);
            this.selection.clear();
            this.selection.add(ssGroupe);
            this.activeBoutonsSuivantSelection();
            this.vue.redrawAll();
        }
    }

    public void boutonSupprimer(ActionEvent t) {
        if (this.etat == 20 && this.selection.size() > 0) {
            // normalement le bouton est disabled dans le cas contraire
            this.vue.getModel().removeAll(this.selection);
            this.selection.clear();
            this.activeBoutonsSuivantSelection();
            this.vue.redrawAll();
        }
    }

    public void boutonResolution(ActionEvent t) {
                
    }
    
    public void changeColor(Color value) {
        if (this.etat == 20 && this.selection.size() > 0) {
            for (Figure f : this.selection) {
                f.changeCouleur(value);
            }
            this.vue.redrawAll();
        } else if (this.etat == 41 && this.barreEnCoursDeCreation != null) {
            this.barreEnCoursDeCreation.changeCouleur(value);
        }
    }

    private void realSave(File f) {
        try {
            this.vue.getModel().sauvegarde(f);
            this.vue.setCurFile(f);
            this.vue.getInStage().setTitle(f.getName());
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème durant la sauvegarde");
            alert.setContentText(ex.getLocalizedMessage());

            alert.showAndWait();
        } finally {
            this.changeEtat(20);
        }
    }

    public void menuSave(ActionEvent t) {
        if (this.vue.getCurFile() == null) {
            this.menuSaveAs(t);
        } else {
            this.realSave(this.vue.getCurFile());
        }
    }

    public void menuSaveAs(ActionEvent t) {
        FileChooser chooser = new FileChooser();
        File f = chooser.showSaveDialog(this.vue.getInStage());
        if (f != null) {
            this.realSave(f);
        }
    }

    public void menuOpen(ActionEvent t) {
        FileChooser chooser = new FileChooser();
        File f = chooser.showOpenDialog(this.vue.getInStage());
        if (f != null) {
            try {
                Figure lue = Figure.lecture(f);
                Groupe glu = (Groupe) lue;
                Stage nouveau = new Stage();
                nouveau.setTitle(f.getName());
                Scene sc = new Scene(new MainPanel(nouveau, f, glu), 800, 600);
                nouveau.setScene(sc);
                nouveau.show();
            } catch (IOException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Problème durant la sauvegarde");
                alert.setContentText(ex.getLocalizedMessage());

                alert.showAndWait();
            } finally {
                this.changeEtat(20);
            }
        }
    }
//    }

    public void menuNouveau(ActionEvent t) {
        Stage nouveau = new Stage();
        nouveau.setTitle("Nouveau");
        Scene sc = new Scene(new MainPanel(nouveau), 800, 600);
        nouveau.setScene(sc);
        nouveau.show();
    }

    public void menuApropos(ActionEvent t) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("A propos");
        alert.setHeaderText(null);
        alert.setContentText("Trop super ce micro-logiciel de dessin vectoriel 2D\n"
                + "réalisé par François de Bertrand de Beuvron\n"
                + "comme tutoriel pour un cours de POO\n"
                + "à l'INSA de Strasbourg");

        alert.showAndWait();
    }

    public void zoomDouble() {
        this.vue.setZoneModelVue(this.vue.getZoneModelVue().scale(0.5));
        this.vue.redrawAll();
    }

    public void zoomDemi() {
        this.vue.setZoneModelVue(this.vue.getZoneModelVue().scale(2));
        this.vue.redrawAll();
    }

    public void zoomFitAll() {
        this.vue.fitAll();
        this.vue.redrawAll();
    }

    public void translateGauche() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateGauche(0.8));
        this.vue.redrawAll();
   }

    public void translateDroite() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateDroite(0.8));
        this.vue.redrawAll();
   }

    public void translateHaut() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateHaut(0.8));
        this.vue.redrawAll();
   }

    public void translateBas() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateBas(0.8));
        this.vue.redrawAll();
   }

    void mouseMovedDansZoneDessin(MouseEvent t) {
        if (this.etat == 41) {
            // attente deuxieme point segment
            this.barreEnCoursDeCreation.setFin(this.posInModel(t.getX(), t.getY()));
            this.vue.redrawAll();
        }
    }

    /**
     * @return the segmentEnCoursDeCreation
     */
    public Barre getBarreEnCoursDeCreation() {
        return barreEnCoursDeCreation;
    }

    void creeNoeudParDialog() {
        Optional<Noeud> p = EnterNoeudDialog.demandeNoeud();
        if (p.isPresent()) {
            this.vue.getModel().add(p.get());
            this.vue.redrawAll();
        }
    }
    
    /*void clicDansZoneDessin(MouseEvent t) {
        System.out.println("clac");
        switch (etat) {
            case 0: // les états commençant par 0 sont les différentes étapes de création d'un triangle terrain
                posPoint1(t);
                this.changeEtat(01);
                break;
            case 01:
                posPoint2(t);
                this.changeEtat(02);
                break;
            case 02:
                creeTriangleTerrain(t);
                this.changeEtat(0);// on revient au début de la création d'un TT pour pouvoir en créer un nouveau
                break;
            case 10: // les états commençant par 1 sont les différentes étapes de création d'un noeud appui
                boolean l = clicSurTT(t);
                if (l==true){
                    if (vue.getTbNoeudAS().isSelected()){ // si le bouton noeud appui simple 
                        //est sélectionné; on change l'état pour créer un noeud appui simple
                       this.changeEtat(11); 
                    }
                    else if (vue.getTbNoeudAS().isSelected()){ // else seul suffit
                        // si le bouton noeud appui double est sélectionné 
                        //on change l'état pour créer un noeud appui double
                        this.changeEtat(12);
                    }
                }
                else { 
                    System.out.println("Le clic ne se trouve pas sur un Triangle Terrain, "
                            + "pour mettre un noeud appui à cet endroit il faut en créer un nouveau");
// il faudrait mettre le message directement sur la fenêtre d'interface pour l'utilisateur avec un textArea en lecture seule
                    this.changeEtat(03);
                }
                break;
            case 11: // 11X pour les noeuds appui simple
                creeNoeudAS(t, TriTerrain);
                break;
            case 12: // 12X pour les noeuds appuis doubles
                creeNoeudAD(t, TriTerrain);
            case 20:// 2X pour les noeuds simple mais un seul états a été défini
                creeNoeudS(t);
                break;
        }
    }
    
    /**
     * récupère les coordonnées d'un clic
     * utile pour le triangle terrain et les barres
     * @param t 
     */
         
    /**
     * créer un noeud simple dans l'interface à la position d'un clic
     * le noeud simple est rond et de la couleur sélectionnée
     * à sa création le noeud est associé à son identificateur dans le numéroteur NNoeud
     * ce qui est utile pour la sauvegarde
     * @param t 
     */
    /*public void creeNoeudS(MouseEvent t){
       NoeudSimple NS = new NoeudSimple(t.getX(),t.getY(), NNoeud); // création du noeud simple
       NNoeud.associe(NS.getIdNoeud(), NS); // on le met dans le numeroteur 
       vue.getcDessin().getRealCanvas().getGraphicsContext2D().setFill(vue.getCouleur());
       vue.getcDessin().getRealCanvas().getGraphicsContext2D().fillOval(NS.getPx()-Rayon, NS.getPy()-Rayon, 2*Rayon, 2*Rayon);
    }*/
    
    /**
     * créer un triangle terrain en utilisant les coordonnées de trois clics
     * les coordonnées des deux premiers sont d'abbord stockés pour ensuite,
     * avec celles du troisième pouvoir créer le TT
     * Il n'est pas possibles de modifier la couleur des TT depuis l'interface
     * ceux-ci ont des couleurs prédéfinies
     * pour voir clairement les limites d'un TT, les bords sont affichés d'une autre couleur que l'intérieur du triangle 
     * @param t 
     */
    
    
    /**
     * permet de déterminer si un clic se trouve sur un triangle terrain pour la création des noeuds appuis
     * on parcourt donc tout les TT se trouvant dans le numeroteur NTT
     * on récupère les coordonnées d'un tt à la fois pour voir 
     * si les coordonnées choisie appartiennent à un tt
     * on recupère le dernier pour lequel c'est vrai
     * on sait qu'un point appartient à un triangle terrain si les angles formés sont du même signe
     * @param t
     * @return 
     */
   /* public boolean clicSurTT(MouseEvent t){
        System.out.println("TT?");
        int compteur=0;
        for(int i=0; i<NTT.getProchainID(); i++){
            P1x = NTT.getObjet(i).getPT1x();
            P1y = NTT.getObjet(i).getPT1y();
            P2x = NTT.getObjet(i).getPT2x();
            P2y = NTT.getObjet(i).getPT2y();
            P3x = NTT.getObjet(i).getPT3x();
            P3y = NTT.getObjet(i).getPT3y();
            int a =0;
            int b =0;
            double angle;
            angle = calculDAngle(t, this.P1x, this.P1y, this.P2x, this.P2y);
            if(angle > 0%2*Math.PI && angle < (Math.PI)%2*Math.PI){
                a++;
                System.out.println("a =" + a);
            }
            if (angle > (Math.PI)%2*Math.PI && angle < 2*(Math.PI)%2*Math.PI){
                b++;
                System.out.println("b =" + b);
            }
            double angle2;
            angle2 = calculDAngle(t, this.P2x, this.P2y, this.P3x, this.P3y);
            if(angle2 > 0%2*Math.PI && angle2 < (Math.PI)%2*Math.PI){
                a++;
                System.out.println("a =" + a);
            }
            if (angle2 > (Math.PI)%2*Math.PI && angle2 < 2*(Math.PI)%2*Math.PI){
                b++;
                System.out.println("b =" + b);
            }
            double angle3;
            angle3 = calculDAngle(t, this.P3x, this.P3y, this.P1x, this.P1y);
            if(angle3 > 0%2*Math.PI && angle3 < (Math.PI)%2*Math.PI){
                a++;
                System.out.println("a =" + a);
            }
            if (angle3 > (Math.PI)%2*Math.PI && angle3 < 2*(Math.PI)%2*Math.PI){
                b++;
                System.out.println("b =" + b);
            }
            if (a==3 || b==3){ // on vérifie les signes des angles formés pour un TT
               compteur++;
               TriTerrain = NTT.getObjet(i);
            }            
        }
        if (compteur!=0){
            return true;
        }
        else{
            return false;
        }
    }
    
    public double calculDAngle(MouseEvent t, double x, double y, double xx, double yy){
        double theta;
        theta = Math.acos(((xx-x)*(t.getX()-x)+(yy-y)*(t.getSceneY()-y))/hypot((xx-x),(yy-y))*hypot((t.getX()-x), (t.getY()-y))); 
        return theta;
    }
    
    /**
     * créer un noeud appui simple sur un triangle terrain
     * pour qu'il soit plus précis il faudrait qu'il soit sur un segment de terrain
     * puisqu'il se déplace en translation selon un segment de terrain
     * la couleur est celle choisie
     * la forme est le bord d'un carré arrondi 
     * @param t
     * @param tt 
     */
    /*public void creeNoeudAS(MouseEvent t, TriangleTerrain tt){
        System.out.println("cnas");
        NoeudAppuiSimple NAS = new NoeudAppuiSimple(tt, NNoeud); 
        NNoeud.associe(NAS.getIdNoeud(), NAS);
        vue.getcDessin().getRealCanvas().getGraphicsContext2D().setStroke(vue.getCouleur());
        vue.getcDessin().getRealCanvas().getGraphicsContext2D().strokeRoundRect(t.getX(), t.getY(), cote, cote, a, a);
    }*/
     
    /**
     * créer un noeud appui double sur un triangle terrain
     * la forme est le bord d'un carré et sa couleur est celle choisie
     * @param t
     * @param tt 
     */
    /*public void creeNoeudAD(MouseEvent t, TriangleTerrain tt ){
        System.out.println("cnad");
        NoeudAppuiDouble NAD = new NoeudAppuiDouble(tt, NNoeud); 
        NNoeud.associe(NAD.getIdNoeud(), NAD);
        vue.getcDessin().getRealCanvas().getGraphicsContext2D().setStroke(vue.getCouleur());
        vue.getcDessin().getRealCanvas().getGraphicsContext2D().strokeRect(t.getX(), t.getY(), cote, cote);
    }*/

}
