/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

/**
 *
 * @author adrie
 */
public class NoeudAppui extends Noeud {
     private TriangleTerrain triangleterrain;
    private double Nx;
    private double Ny;

    public NoeudAppui(TriangleTerrain triangleterrain, Numeroteur<Noeud> N) {  
        super(N);
        this.triangleterrain = triangleterrain;
        this.Nx = 0;
        this.Ny = 0;
       
    }
    
    public NoeudAppui(TriangleTerrain t, int j,double alpha, Numeroteur<Noeud> N){
        super(N); // appeler constructeur de la super classe en premier 
        this.triangleterrain = t;
        if(0<=alpha && alpha<=1){
        Nx=1;
        
        if(j==0){
            /* l'utilisateur a le choix de rentrer différents indices qui correspondent aux numéro du point 
             le second point est déduit à partir du premier point rentré car il s'agit du suivant.
            Le constructeur caclule ensuite les valeurs des coordonées du noeud car il doit satisfaire certaines 
            conditions. Un noeud appui ne peut être quelquonque. Enfin le coef alpha doit être compris entre 0 et 1.
            alors le premier point est PT1 pour j=0 !
*/
            
                this.Nx=alpha*t.getPT1x()+(1-alpha)*t.getPT2x(); // ici on retrouve la formule du poly
                this.Ny=alpha*t.getPT1y()+(1-alpha)*t.getPT2y();
        }
            if(j==1){
                this.Nx=alpha*t.getPT2x()+(1-alpha)*t.getPT3x();
                this.Ny=alpha*t.getPT2y()+(1-alpha)*t.getPT3y();
            }
            if(j==2){
                this.Nx = alpha*t.getPT3x()+(1-alpha)*t.getPT1x();
                this.Ny = alpha*t.getPT3y()+(1-alpha)*t.getPT1y();
            }
            else{
                throw new Error("j doit satisfaire l'inégalité : 0<=j<3, retentez avec une autre valeur");
            }  
        }
            
        
        else{
            throw new Error("la valeur du coef alpha doit être comprise entre 0 et 1 (intervalle fermé)");
        }
    }
    
    //Méthodes get/set
    public TriangleTerrain getTriangleterrain() {
        return (this.triangleterrain) ;
    }
    
    public void setTriangleterrain(TriangleTerrain TT) {
        this.triangleterrain=TT ;
    }
    
    public double getNx() {
        return this.Nx;
    }
    
    public void setNx(double Nx) {
        this.Nx=Nx;
    }
    
    public double getNy() {
        return this.Ny;
    }
    
    public void setNy(double Ny) {
        this.Ny=Ny;
    } 

    @Override
    public String toString() {
        return "Noeudappuis{" + "triangleterrain=" + triangleterrain + ", Nx=" + Nx + ", Ny=" + Ny + '}';
    }
    
    public static void main(String[]args ){
        /* ce main permet d'initialiser de type Noeud et TriangleTerrain, on remarque que cela fonctionne bien car les 
        id pour des objets différents sont bien différents. 
        */
        double alpha  = 0.5;// alpha est un coeff utilisé pour calculer les coordonnées du noeud 
        Numeroteur<Noeud> numNA  = new Numeroteur();// initialisation d'un numéroteur pour la super classe noeud
        Numeroteur<TriangleTerrain> numTT  = new Numeroteur(); //initialisation d'un numéroteur pour la classe TriangleTerraon
        TriangleTerrain TT = new TriangleTerrain(numTT); //initialisation d'un triangle de terrain 
        numTT.associe(TT.getIdTT(), TT); // association entre le numéroteur et l'id du triangle de terrain
        TriangleTerrain TT1 = new TriangleTerrain(4,5,8,4,5,1,numTT); 
        System.out.println(TT1.getPT1x());
        numTT.associe(TT1.getIdTT(), TT1);
        NoeudAppui Noeud1 = new NoeudAppui(TT,2,alpha, numNA); //initialisation d'un noeud appui 
        numNA.associe(Noeud1.getIdNoeud(),Noeud1);// association entre l'id d'lobjet noeud appui et le numéroteur
        NoeudAppui Noeud2 = new NoeudAppui(TT,2,alpha, numNA);
        numNA.associe(Noeud2.getIdNoeud(),Noeud2);

        //Affichage 
        System.out.println("l'id de ce triangle de terrain est "+TT.getIdTT());
        System.out.println("l'id de ce triangle de terrain est "+TT1.getIdTT());
        System.out.println("l'id de ce noeud est "+Noeud1.getIdNoeud());
        System.out.println("l'id de ce noeud est"+Noeud2.getIdNoeud());



        Numeroteur<Noeud> num = new Numeroteur();
        NoeudAppui N = new NoeudAppui(TT1,2,0.2, num);
        double Px = N.getNx();
        System.out.println("la veleur de Nx est "+Px);

        }
}
