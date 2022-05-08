package fr.insa.leneve.projet_s2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;

import Ressyslin.utils.Console;
import fr.insa.leneve.projet_s2.interfa.Controleur;

/**
 *
 * @author cheraita
 */
public class Treillis_ { // composé 1 terrain, plusieurs noeuds, plusiseurs barres et types de barres...
    private Terrain terrain;
    private List<Noeud> noeuds; 
    private List<Barre> barres; // treillis composé de plusieurs barres (liste)
    private List<TypedeBarre> typesbarres;
    

    public Treillis_(Terrain terrain, Controleur controle){ // constructeur pour initialiser attributs
        this.terrain=terrain;
        this.noeuds= new ArrayList<>();
        this.barres= new ArrayList<>();
        this.typesbarres= new ArrayList<>();
        
        
    }
    public Terrain getterrain(){
        return(this.terrain);
    }
    public List<Noeud> getnoeuds(){
        return(this.noeuds);
    }
    public List<Barre> getbarres(){
        return(this.barres);
    }
    public List<TypedeBarre> gettypesbarres(){
        return(this.typesbarres);
    }
    
    
    public void setterrain(Terrain T){
        this.terrain = T;
    }
    public void setnoeuds(List<Noeud> N){
        this.noeuds = N;
    }
    public void setbarres(List<Barre> B){
        this.barres = B;
    }
    public void settypebarre(List<TypedeBarre> Tpbarre){
        this.typesbarres = Tpbarre;
    }
    
    

    @Override
    public String toString() {
        return "Treillis_{" + "terrain=" + terrain + ", noeuds=" + noeuds + ", barres=" + barres + ", typesbarres=" + typesbarres + ", noeudsimple="  + '}';
    }
    public int nbre_appuis_double(){
          // on récupère le nombre d'appui double car nous en aurons besoin dans la suite de la méthode 
        int  nbre_noeudappuis_double = 0;
        for(int h=0;h<this.noeuds.size();h++){
            
            if(this.noeuds.get(h) instanceof NoeudAppuiDouble){
                nbre_noeudappuis_double = nbre_noeudappuis_double+1;
            }
                
        }
        return nbre_noeudappuis_double;
    }
    public int nbre_appui_simple(){
        int nbre_appui_simple = 0;
        for(int h=0;h<this.noeuds.size();h++){
            
            if(this.noeuds.get(h) instanceof NoeudAppuiSimple){
                nbre_appui_simple = nbre_appui_simple+1;
            }
                
        }
        return nbre_appui_simple;
    }
     public int nbre_noeud_simple(){
        int nbre_noeud_simple = 0;
        for(int h=0;h<this.noeuds.size();h++){
            
            if(this.noeuds.get(h) instanceof NoeudSimple){
                nbre_noeud_simple = nbre_noeud_simple+1;
            }
                
        }
        return nbre_noeud_simple;
    }
    // Je vais essayer de créer une méthode qui permet  de compter le nombre d'inconnues dans le système, le nombre d'inconnues c'est le nombre de noeuds(simples ou doubles)
    //et le nomber de barres 
    public int Nombre_inconnues(){
        int nbre_noeuds  = this.noeuds.size(); // on accède au nombre d'élements de la liste ce qui nous permet d'accéder directement au nombre de noeuds
      
        int nbre_barres = this.barres.size();// on accède au nombre de barres dans la liste de barres, dans le cas où il n'y en pas il va renvoyer 0 barre 
        int nombre_inconnues = nbre_barres + this.nbre_appui_simple()+ 2*this.nbre_appuis_double() ;// le nomre d'inconnues du système correpond au nombre de noeuds et de barres
        // je pense que dans la formule les noeud appuis sont des appuis doubles d'où le fait qu'ils doivent 2 composantes et donc 2 inconnues par noeud double 
        return(nombre_inconnues);// on return le nombre d'inconnues du système grâce au nombre de noeuds simples, noeuds double et nombre de barres
    }
    public boolean isostasique(){
        /* pour que le système soit isostasique il faut que le nbre d'équations soit égal au nombre d'inconnues 
        du système. Cela se traduit par une relation à vérifier entre les noeuds et les barres. 
        si cette relation est vérifiée, le système de noeuds+barres considéré comme isostasique 
        La relation à montrer est : 2.Ns=Nb+Nsas+ 2.Nsad; Ns : Noeuds, Nb : Barres; Nsas : Appui simple
        Nsad : Noeudappuis
        Dans cette méthode on utilise le nombre d'inconnues calculées précédemment
        
        */
        boolean a;
        int nbre_noeudsimple = 0;
        int nbre_noeudappuis = 0;
        int nbre_noeuds  = this.noeuds.size();
        if(Nombre_inconnues()==2*this.noeuds.size()){ // on teste la relation entre le nombre d'inconnues et le nombre d'équations
            System.out.println("Le système est isostasique, il bien vérifie la relation : 2.Ns=Nb+Nsas+ 2.Nsad ");
            a=true;
        }
        else{
            a=false;
            throw new Error("le treillis sélectionné n'est pas isostasique, il n'est pas possible de résoudre les équations associées ");
        }
       
        
        
        
        
     return(a);
    }
    /* une fois que l'on sait que le système est isostasique on peut essayer de résoudre les équations 
    On va essayer de déterminer les valeurs des efforts en traction/compression et des forces de réaction.
    si l'effort est positif il s'agit d'un effort en traction sinon il s'agit d'un effort en compression
    on pourra donc caractériser le type de contraintes que subit les barres du treillis et déterminer 
    les forces de réaction au niveau des noeuds.
    
    */
    
     public Matrice matrice_force_ext(){
         int nbre_ligne =2* this.noeuds.size();// c'est aussi égal à 2 fois le nombre d'équations car on étudie uniquement les treillis isostasique
        Matrice matrice_force_ext = new Matrice(2*this.noeuds.size(),1);
        double Px = 0;
        double Py = 0;
        for(int l =0,j=0;l<nbre_ligne&&j<this.noeuds.size();l=l+2,j++){
            
            Px = Console.entreeDouble("Saisissez une valeur de force extérieure sur la composante x pour le noeud "+this.noeuds.get(j).id);

            Py = Console.entreeDouble("Saisissez une valeur de force extérieure sur la composante y pour le noeud "+this.noeuds.get(j).id);
                  matrice_force_ext.set(l,0,Px);
                  matrice_force_ext.set(l+1,0,Py);
        }
        return matrice_force_ext;
         
     }
        
    public Matrice coeffmatrices(){
         int nbre_ligne;
        int nbre_colonne;
        nbre_ligne = this.Nombre_inconnues();
        nbre_colonne = nbre_ligne;
        //initialisation de la matrice résultat
        Matrice matrice_résultat = new Matrice(nbre_ligne,1);
        //initialisation de la matrice avec les inconnues 
       
        
        
        // remplissage de la matrice forces extérieures remplies par l'utilisateur 
        // Ces forces sont des contraintes supplémentaires ajoutées à la demande de l'utilsateur 
        // les contraintes doivent pas être trop importantes pour que les valeurs soient crédibles 
        Matrice Matrice_coeffs = new Matrice(nbre_ligne,nbre_colonne);
        if(this.isostasique() == true){
            System.out.println("nous pouvons résoudre ce système car le sytème est isostasique ");
            for(int i =0;i<this.barres.size();i++){
               
            }
        
        
       
        
        /*ici on va remplir la matrice contenant les coefficients devant les inconnues 
         Cette partie nécessite que la Arraylist contenant les noeuds soit classée dans un ordre précis 
           Noeud simple avec id croissant, Noeud appuis doubles avec id croissant et noeuds appuis simples avec id croissant 
        
        // ArayList classée en fonction des id pour obtenir la bonne matrice avec les coeffs au bon endroit, pour avoir bon syst linéaire
        
        cette boucle for est une boucle for a deux variables permettant d'incrémenter les noeuds et les lignes de la matrice 
         en même temps. On incrémente les lignes de la matrice avec un pas de 2.
       
*/       // on récupère le nombre d'appui double car nous en aurons besoin dans la suite de la méthode 
        
    


        for(int i = 0,j=0;i<this.Nombre_inconnues()&&j<this.noeuds.size();i=i+2,j++){
            // on se balade sur la liste des noeuds avec l'indice j et l'indice i permet de suivre les lignes de la matrice 
            // des coefficients
           
            
            
            
            // on récupère l'attribut noeud du treillis = ArrayList, on récup une valeur du tableau, et si c'est un noeud simple alors il vérifie relation 
            
            if(this.noeuds.get(j) instanceof NoeudSimple){// si le noeud de la liste est un noeud simple 
            // Cette partie du code va permettre de trouver les barres concourrantes à un noeud et de déterminer le 
            // coefficient devant l'effort inconnu de la barre
                for(int k=0;k<this.barres.size();k++){
                    // permet de trouver une barre k concourante au noeud j, un noeud peut avoir plusieurs barres concourantes 
                    if(this.noeuds.get(j)==this.barres.get(k).debut||this.noeuds.get(j)==this.barres.get(k).fin){ 
                        // Equations sur la composante x :
                        double angle_alpha = this.barres.get(k).getAngleBarre(this.barres.get(k));
                        Matrice_coeffs.set(i,this.barres.get(k).id,Math.cos(angle_alpha));
                        // Equations sur la composante y : 
                        Matrice_coeffs.set(i+1,this.barres.get(k).id,Math.sin(angle_alpha));
                    } // n° id de la barre, pour mettre coeff sur bonne colonne
                }
            }
        }
        for(int i = 2*this.nbre_noeud_simple(),j=0;i<this.Nombre_inconnues()&&j<this.noeuds.size();i=i+2,j++){
            
            
                if(this.noeuds.get(j) instanceof NoeudAppuiDouble){
                    for(int n = 0;n<this.barres.size();n++){
                        // dans un premier temps on s'intéresse aux coefficients devant les efforts 
                        if(this.noeuds.get(j)==this.barres.get(n).debut||this.noeuds.get(j)==this.barres.get(n).fin){
                        double angle_alpha = this.barres.get(n).getAngleBarre(this.barres.get(n));
                         // Equations sur la composante x :
                        Matrice_coeffs.set(i,this.barres.get(n).id,Math.cos(angle_alpha));
                        // Equations sur la composante y : 
                        Matrice_coeffs.set(i+1,this.barres.get(n).id,Math.sin(angle_alpha));                        
                        }
                    }
                    // nous allons nous occuper des coeffs devant les forces de réaction, cela revient à placer un coef 1
                        // à la bonne place dans la matrice 
                        //on s'occupe du coefficient devant la réaction sur x pour une barre n concourante au noeud j
                        Matrice_coeffs.set(i,this.barres.size()+2*this.noeuds.get(j).id,1);
                        //coeff devant la réaction sur y pour une barre n concourante au noeud j
                        Matrice_coeffs.set(i+1,this.barres.size()+2*this.noeuds.get(j).id+1,1);
                        // 1 noeud = 1 réaction ? Réaction 2 composantes
        
       
        }
    }
    
    for(int i =2*this.nbre_noeud_simple()+2*this.nbre_appuis_double() ,j=0;i<this.Nombre_inconnues()&&j<this.noeuds.size();i=i+2,j++){
    
                if(this.noeuds.get(j) instanceof NoeudAppuiSimple){
                    for(int m=0;m<this.barres.size();m++){
                        if(this.noeuds.get(j)==this.barres.get(m).debut||this.noeuds.get(j)==this.barres.get(m).fin){
                            //remplissage des coefficents devant les efforts des barres 
                            double angle_alpha = this.barres.get(m).getAngleBarre(this.barres.get(m));
                            // Equations sur la composante x :
                            Matrice_coeffs.set(i,this.barres.get(m).id,Math.cos(angle_alpha));
                            // Equations sur la composante y : 
                            Matrice_coeffs.set(i+1,this.barres.get(m).id,Math.sin(angle_alpha)); 
                        }
                        
                       
                    }
                    for(int t=0;t<this.terrain.getTriangles_Terrain().size();t++){
                        Numeroteur <TriangleTerrain> numTT= new Numeroteur();
                        Numeroteur <Noeud> numN= new Numeroteur();
                        TriangleTerrain triangle = new TriangleTerrain(numTT);
                        triangle = this.terrain.getTriangles_Terrain().get(t);
                        // ces grandes boucles permettent de tester si le noeud appui simple appartient à au moins un des segments de terreain associé à un triangle de terrain
                        // une fois ce test effectué on a accès au segment de terrain qui nous permet de déterminer la valeur de l'angle via 
                        // la méthode angle béta. Avec l'angle nous pouvons placer le coefficient correposndant à la réaction sur la composante 
                        // x et y et ainsi terminer le remplissage de la matrice 
                        if(triangle.recup_segment(1, 2).test_noeud_segment((NoeudAppuiSimple)this.noeuds.get(j)) == true){
                            // on récupère l'angle beta nous permettant de calculer les composantes de la réaction
                            double angle_beta = triangle.recup_segment(1, 2).angle_beta(numTT, numN); 
                            Matrice_coeffs.set(i,this.barres.size()+2*this.nbre_appuis_double()+this.noeuds.get(j).id, Math.cos(angle_beta));
                            Matrice_coeffs.set(i+1,this.barres.size()+2*this.nbre_appuis_double()+this.noeuds.get(j).id, Math.sin(angle_beta));
                            
                        }
                        if(triangle.recup_segment(1, 3).test_noeud_segment((NoeudAppuiSimple)this.noeuds.get(j)) == true){
                            double angle_beta = triangle.recup_segment(1, 3).angle_beta(numTT, numN); 
                            Matrice_coeffs.set(i,this.barres.size()+2*this.nbre_appuis_double(), Math.cos(angle_beta));
                            Matrice_coeffs.set(i+1,this.barres.size()+2*this.nbre_appuis_double() , Math.sin(angle_beta));
                        }
                        
                        
                        if(triangle.recup_segment(2, 3).test_noeud_segment((NoeudAppuiSimple)this.noeuds.get(j)) == true){
                            double angle_beta = triangle.recup_segment(2,3).angle_beta(numTT, numN); 
                            Matrice_coeffs.set(i,this.barres.size()+2*this.nbre_appuis_double(), Math.cos(angle_beta));
                            Matrice_coeffs.set(i+1,this.barres.size()+2*this.nbre_appuis_double() , Math.sin(angle_beta));
                        }
                        
                                
                        
                    }
                        
                        
                    }
                        }
    
                }
        else{
            throw new Error("On ne peut pas résoudre le système car le treillis n'est pas isostasique ");
        }
        System.out.println("Voici la matrice contenant les coeffcients du système linéaire associé au treillis ");
        return(Matrice_coeffs);
    }
    
        
    
    // partie qui permet de récupérer le vecteur solution par concaténation de la matrice des coefs avec la matrice résultats 
    // puis on utilise la méthode descente gauss pour créer des 0 en dessous des pivots
    // on utilise enfin la méthode remonte gauss pour créer des 0 au dessus des pivots 
    //en fin on rend la matrice unitaire en faisant apparaître des 1 pour les coefficients diagonaux.
    public Matrice Matrice_res(){
        this.coeffmatrices().concatCol(this.matrice_force_ext());
        Matrice res = new Matrice(this.Nombre_inconnues(),this.Nombre_inconnues());
        res = this.coeffmatrices().concatCol(this.matrice_force_ext());
        Matrice vecteur_resultat = new Matrice(this.Nombre_inconnues(),1);
        Matrice coefs = new Matrice(this.Nombre_inconnues(),this.Nombre_inconnues());
        res.descenteGauss();
        res.pivotsUnitaires(res.descenteGauss().rang);
        res.remonteeGauss(res.descenteGauss().rang);
        vecteur_resultat = res.subCols(this.Nombre_inconnues()-1, this.Nombre_inconnues()-1);          
        System.out.println("Voici le vecteur résultat contenant la valeur des efforts des barres et des réactions associés aux noeuds du treillis  ");
        return vecteur_resultat ;
    }
                
     
     
        
    
    
         
    
    
   /*

   public static void main(String[]args){
            double a;
            Numeroteur<Terrain> numT = new Numeroteur();
            Numeroteur<Noeud> numN = new Numeroteur();
            Numeroteur<TriangleTerrain> numTerrain = new Numeroteur();
            Terrain terrain = new Terrain(0,200,0,200,numT);
            Treillis_ T = new Treillis_(terrain,));
            TriangleTerrain TT = new TriangleTerrain(4,65,78,69,12,14,numTerrain);
            terrain.getTriangles_Terrain().add(TT);
            Noeudsimple Noeud1 = new Noeudsimple(10,20,numN);
            Noeudsimple Noeud2 = new Noeudsimple(14,25,numN);
            System.out.println(Noeud1.id);
            System.out.println(Noeud2.id);
            
            int b =1;
             Appuis_simple As = new Appuis_simple(TT,2,0.1,numN);
             System.out.println(As.id);
             T.noeuds.add(As);
             T.noeuds.add(Noeud1);
             T.noeuds.add(Noeud2);
             System.out.println(T.noeuds.get(2).id);
             //System.out.println(T.Nombre_inconnues());
             //System.out.println(T.noeuds.size());
             T.matrice_force_ext();
            
           
            //T.noeuds.add(e)
            Matrice A = new Matrice(T.Nombre_inconnues(),0);
            
            
            /*
            
            Matrice N = new Matrice(1,3);
            
            Matrice B = new Matrice(3,1);
             Matrice M= new Matrice(3,4);
            
             Matrice G= new Matrice(3,1);
             for(int i=0;i<3;i++){
                 G.set(i, 0, 1);
             }
            
            for(int i = 0;i<3;i++){
                for(int j = 0;j<3;j++){
                    a = Math.random()*100;
                   // a = Console.entreeDouble("saisissez une valeur pour le coefficient de la ligne "+i+" de la colonne "+j);
                    N.set(i, j,a);
                  
                    
                }
                
            }
             M= N.concatCol(G);
            
            
            System.out.println(N);
            System.out.println(M);
             M.descenteGauss();
             M.pivotsUnitaires(M.descenteGauss().rang);
             System.out.println(M);
             M.remonteeGauss(M.descenteGauss().rang);
             System.out.println(M);
             G = M.subCols(3, 3);
             System.out.println(G);
             
             //N.subCols(1, 2);
             //System.out.println(N);
             */
       // }  
    
    
}

