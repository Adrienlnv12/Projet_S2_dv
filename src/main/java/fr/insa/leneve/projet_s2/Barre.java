/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

/**
 *
 * @author adrie
 */
public class Barre {
        
    public TypedeBarre typebarre; // chaque barre a un type 
    public int id;
    public Noeud Noeud1,Noeud2; // une barre a 2 noeuds à ses extrémités
    private double traction;
    private double compression;
    private double prix;
    
    public Barre(TypedeBarre Tb, Noeud N1, Noeud N2) {
        this.typebarre = Tb;
        this.Noeud1 = N1;
        this.Noeud2 = N2;
    }
    public TypedeBarre gettypebarre(){
        return(this.typebarre);
    }

    public void setIdBarre(int id) {
        this.id = id;
    }
    
    public int getIdBarre() {
        return (this.id);
    }

    public Noeud getNoeud1() {
        return (this.Noeud1);
    }

    public Noeud getNoeud2() {
        return (this.Noeud2);
    }
    //Il s'agit des méthodes get pour accéder aux valeurs des coordonnées des noeuds, c'est compliqué car les noeuds peuvent être des noeuds appuis 
    // ou des noeuds simples du coup on peut pas directement récuperer l'attribut d'un attribut sachant que c'est la classe abstraite Noeud.

    public double getPxnoeud1(){ //a refaire
    /** méthode associe pour l'instant seulement dans cette méthode,
     * vérifier le fonctionnement de ces méthodes, il est surement possible de les optimiser
     */
        Numeroteur ntt = new Numeroteur<TriangleTerrain>();
        Numeroteur n = new Numeroteur<Noeud>();
        TriangleTerrain TT = new TriangleTerrain(ntt);
        ntt.associe(TT.getIdTT(), TT);
        NoeudSimple N1 = new NoeudSimple(n);
        n.associe(N1.getIdNoeud(), N1);
        NoeudAppui N1a = new NoeudAppui(TT, ntt);
        n.associe(N1a.getIdNoeud(), N1a);
        double Px = 0;
       if(this.Noeud1 instanceof NoeudSimple){
           N1=(NoeudSimple)this.Noeud1;
           Px = N1.getPx();
       }
       if(this.noeud1 instanceof Noeudappuis){
           N1a = (Noeudappuis)this.noeud1;
           Px = N1a.getNx();
       }
       return(Px);
    }
    public double getPynoeud1(){
        Numeroteur ntt = new Numeroteur<TriangleTerrain>(); 
        Numeroteur n = new Numeroteur<Noeud>();
        TriangleTerrain TT = new TriangleTerrain(ntt);
        Noeudsimple N1 = new Noeudsimple(n);
        Noeudappuis N1a = new Noeudappuis(TT,n);
        double Py = 0;
        if(this.noeud1 instanceof Noeudsimple){
            N1=(Noeudsimple)this.noeud1;
            Py = N1.getPy();
        }
        if(this.noeud1 instanceof Noeudappuis){
            N1a = (Noeudappuis)this.noeud1;
            Py = N1a.getNy();
        }
        return(Py);
    }
    public double getPxnoeud2(){
        Numeroteur ntt = new Numeroteur<TriangleTerrain>();
        Numeroteur n = new Numeroteur<Noeud>();
        TriangleTerrain TT = new TriangleTerrain(ntt);
        Noeudsimple N2 = new Noeudsimple(n);
        Noeudappuis N2a = new Noeudappuis(TT, n);
        double Px = 0;
        if(this.noeud2 instanceof Noeudsimple){
            N2=(Noeudsimple)this.noeud2;
            Px = N2.getPx();
        }
        if(this.noeud2 instanceof Noeudappuis){
            N2a = (Noeudappuis)this.noeud2;
            Px = N2a.getNx();
        }
        return(Px);
    }
    public double getPynoeud2(){
        Numeroteur ntt = new Numeroteur<TriangleTerrain>();
        Numeroteur n = new Numeroteur<Noeud>();
        TriangleTerrain TT = new TriangleTerrain(ntt);
        Noeudsimple N2 = new Noeudsimple(n);
        Noeudappuis N2a = new Noeudappuis(TT, n);
        double Py = 0;
        if(this.noeud2 instanceof Noeudsimple){
            N2=(Noeudsimple)this.noeud2;
            Py = N2.getPy();
        }
        if(this.noeud2 instanceof Noeudappuis){
            N2a = (Noeudappuis)this.noeud2;
            Py = N2a.getNy();
        }
        return(Py);
    }
   
    public void settypebarre(TypedeBarre T){
        this.typebarre=T;
    }
    
    public void setNoeud1(Noeud N){
        this.Noeud1=N;
    }
    
    public void setNoeud2(Noeud N){
        this.Noeud2=N;
    }
    
    @Override
    public String toString() {
        return "barre{" + "typebarre=" + typebarre + ", id=" + id + ", noeud1=" + Noeud1 + ", noeud2=" + Noeud2 + '}';
    }
    
     /* public double Longueur_barre(barre B){
      double longueur_barre = 0;
      
      Numeroteur ntt = new Numeroteur<TriangleTerrain>();
      Numeroteur n = new Numeroteur<Noeud>();
      TriangleTerrain TT = new TriangleTerrain(ntt);
      NoeudSimple N1 = new NoeudSimple(n);
      NoeudSimple N2 = new NoeudSimple(n);
      NoeudAppui N1a = new NoeudAppui(TT,ntt);
      NoeudAppui N2a = new NoeudAppui(TT,ntt);
      // on va utiliser la formule mathématique de la norme 
      // nous devons tester le type de chaque noeud pour pouvoir retouver le bon attribut, une belle galère !!
      /* Sachant que l'attribut est un Noeud, pour accéder à ses coordonnées on doit déjà savoir si il s'agit d'un 
      noeud simple ou double. Une fois qu'on a cette info J'ai utilisé la formule qu'on a vu en première je crois avec la racine pour calculer 
      la longueur de la barre. Je pense que la longueur de la barre ça devrait nous aider a calculer l'angle entre la barre et l'axe des abscisses. 
      
      
       longueur_barre = Math.sqrt(Math.pow(B.getPxnoeud2()-B.getPxnoeud1(),2)+Math.pow(B.getPynoeud2()-B.getPynoeud1(),2));
          
      
        
      return(longueur_barre);
    }
    /*cette méthode devrait permettre de recup l'angle entre la barre et Ox
    
    */
    public double AngleBarre(Barre B){
            double angle = 0;            
            //angle= Math.acos(Math.abs(B.getPxnoeud2()-B.getPxnoeud1())/Longueur_barre(B));
            return(angle);  
        /* cette partie commentaire va expliquer la méthode angle 
             cette méthode permet de recupérer en théorie l'angle entre une barre et l'axe des abscisses
            J'ai fait la même manip que précdemment pour connaitre la classe exacte du noeud afin de récuperer la coordonnée sur x 
           Cet angle est utilisé dans les équations d'équilibre statique. Une fois que l'on a l'angle, dans les cas d'un sytème isostasique 
            on peut résoudre le système. 
            Donc une condition de résolution du système un treillis isostasique 
            */
    }
    
    public double getAngleBarre(Barre B){
        return(AngleBarre(B));
    }
    
    /*public static void main(String[]args){
        Numeroteur TdB = new Numeroteur<TypedeBarre>();
        Numeroteur TTerr = new Numeroteur<TriangleTerrain>();
        Numeroteur N = new Numeroteur<Noeud>();
        TypedeBarre TB = new TypedeBarre(41,478,56,87,14,TdB);
        TdB.associe(TB.getIdTB(), TB);
        TriangleTerrain TT = new TriangleTerrain(4,12,54,78,11,23, TTerr);
        TTerr.associe(TT.getIdTT(), TT);
        Noeudappuis N1 = new Noeudappuis(TT,2,0.2, N);
        N.associe(N1.getIdNoeud(), N);
        System.out.println(N1.getNx());
        System.out.println(N1.getNy());        
        Noeudsimple N2 = new Noeudsimple(4,8, N);
        N.associe(N2.getIdNoeud(), N);
        barre B = new barre(TB,N1,N2);
        double Px = B.getPxnoeud1();
        System.out.println(Px);
        System.out.println(B.getPynoeud1());
        System.out.println(B.id+" " +N1.id);
        
        
    }*/
}
